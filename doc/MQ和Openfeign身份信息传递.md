## 业务需求

`itbbs-blog` 服务中发布文章需要发送一条创建文章静态文件的 MQ 消息，这个 MQ 消息中需要用到用户上下文的信息，MQ 发送的消息是另外的一个线程，所以 MQ 监听者没法获取到用户的信息。此外 MQ 用 openfeign 远程调用 `itbbs-system` 服务进行文件上传，文件上传也需要用到用户信息，所以这里也获取不到

## 技术实现

### MQ 身份传递

1、修改 MQ 生产者发送消息，传入当前的 Token 和用户信息

```java
@Component
public class RabbitMQProducer {

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routingKey, Object msgData){
        rabbitTemplate.convertAndSend(exchange, routingKey, msgData, message -> {
            Object token = ThreadLocalTempVar.getTempTokenVar();
            if (Objects.isNull(token)){
                return message;
            }
            // 传递Token(防止MQ中再次进行远程调用导致出现请求头丢失的情况)和用户信息
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setHeader(tokenHeader, CommonConstants.TOKEN_PREFIX + token);
            messageProperties.setHeader(CommonConstants.MQ_USER_INFO, SecurityUtils.getLoginUser());
            return message;
        });
    }

}
```

2、修改 MQ 消息转化器，从 MQ 中获取到 Token 和用户信息（如果有），就放入当前的线程的上下文中

```java
@Bean
public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter(){
        @Override
        public Object fromMessage(Message message) throws MessageConversionException {
            // 判断消息不为空
            if (ObjectUtil.isNull(message)){
                return super.fromMessage(message);
            }
            // 从消息头中取出用户信息
            String userInfoStr = message.getMessageProperties().getHeader(CommonConstants.MQ_USER_INFO);
            if (StringUtils.isEmpty(userInfoStr)){
                return super.fromMessage(message);
            }
            // 存入上下文
            LoginUserDTO loginUserDTO = BeanUtil.toBean(userInfoStr, LoginUserDTO.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO, null, loginUserDTO.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return super.fromMessage(message);
        }
    };
}
```

### Openfeign 身份传递

1、新增 openfeign 请求拦截器，传入当前线程的 token 和 openfeign 内部调用规定的 whiteToken

```java
@Bean("requestInterceptor")
public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            // 拿到当前线程的请求头，本质上是一个ThreadLocal
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 拿到token
            Object token = ThreadLocalTempVar.getTempTokenVar();
            // 放入自定义头到template请求中
            if (Objects.nonNull(token)) {
                template.header(tokenHeader, token.toString());
            }
            // 放入feign调用token
            template.header(CommonConstants.FEIGN_TOKEN_HEADER, whiteToken);
        }
    };
```

2、新增 openfeign 请求过滤器，这个过滤器是在 Jwt 过滤器之前执行

```java
@Component
public class FeignTokenFilter extends OncePerRequestFilter {
    @Value("${system.whiteToken}")
    private String whiteToken;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String feignToken = request.getHeader(CommonConstants.FEIGN_TOKEN_HEADER);
        if (StringUtils.isEmpty(feignToken) && !whiteToken.equals(feignToken)) {
            // 校验是否是 openfeign 调用，如果不是就直接返回
            filterChain.doFilter(request, response);
            return;
        }
        request.setAttribute(CommonConstants.FEIGN_REQUEST_FLAG, true);
        filterChain.doFilter(request, response);
    }
}
```

3、修改 Jwt 过滤器，添加处理 openfeign 请求调用的部分

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
    Boolean isFeignRequest = (Boolean) request.getAttribute(CommonConstants.FEIGN_REQUEST_FLAG);
    String token = tokenService.getToken(request);
    if (Objects.nonNull(isFeignRequest) && isFeignRequest && StringUtils.isEmpty(token)) {
        // 如果是feign调用并且token为空
        chain.doFilter(request, response);
        return;
    }
    if (StringUtils.isEmpty(token)) {
        chain.doFilter(request, response);
        return;
    }
    LoginUserDTO loginUserDTO = tokenService.getLoginUserDTO(token);
    if (Objects.isNull(loginUserDTO)) {
        chain.doFilter(request, response);
        return;
    }
    try {
        ThreadLocalTempVar.setTempTokenVar(token);
        tokenService.validateToken(loginUserDTO);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDTO, null, loginUserDTO.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    } finally {
        ThreadLocalTempVar.removeTempTokenVar();
    }
}
```

