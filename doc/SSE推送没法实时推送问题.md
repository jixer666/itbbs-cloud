# SSE推送没法实时推送问题

## 问题场景

AI 流式聊天接口使用 SSE 进行消息推送，调用 `itbbs.com` 的域名接口没法推送，需要等请求数据全部收到后才会一次性全部推送过来，但调用 `localhost:14000` 聊天服务接口可以实时推送过来，两者的唯一区别在于域名接口多了一层 Nginx，返回的数据先要进过 Nginx，由 Nginx 返回给客户端

## 解决方案

Nginx 会缓存代理服务器的响应（聚合类型），服务推送的数据被 Nginx 缓存到缓冲区，导致客
户端没有实时收到数据，而是等到服务所有数据推送完后，客户端才一次性收到了所有数据

解决办法：**禁用缓存功能，添加响应头：`X-Accel-Buffering:no`**

代码如下：

```java
@ApiOperation("AI聊天Stream流")
@PostMapping("/chat/stream")
public SseEmitter chatStream(@RequestBody BotChatDTO botChatDTO,
                             HttpServletResponse response) {
    forbiddenNginxFlux(response);
    return botService.chatStream(botChatDTO);
}

private void forbiddenNginxFlux(HttpServletResponse response) {
    response.setHeader("X-Accel-Buffering", "no"); // 禁用Nginx缓冲
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Connection", "keep-alive");
    response.setHeader("Content-Type", "text/event-stream; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    try {
        // 立即刷新响应头
        response.flushBuffer();
    } catch (IOException e) {
        throw new GlobalException(BizCodeEnum.BIZ_ERROR.getCode(), "刷新响应头失败");
    }
}
```

## 参考文章

[(https://blog.csdn.net/weixin_43837268/article/details/147930394](https://blog.csdn.net/weixin_43837268/article/details/147930394)