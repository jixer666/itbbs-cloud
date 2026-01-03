package com.abc.itbbs.common.security.aspect;

import com.abc.itbbs.common.core.annotation.RateLimiter;
import com.abc.itbbs.common.core.domain.enums.BizCodeEnum;
import com.abc.itbbs.common.core.domain.enums.LimitType;
import com.abc.itbbs.common.core.exception.GlobalException;
import com.abc.itbbs.common.core.util.IpUtils;
import com.abc.itbbs.common.core.util.StringUtils;
import com.google.protobuf.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    private RedisTemplate<Object, Object> redisTemplate;

    private RedisScript<Long> limitScript;

    // SpEL解析器
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Autowired
    public void setRedisTemplate1(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setLimitScript(RedisScript<Long> limitScript) {
        this.limitScript = limitScript;
    }

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        List<Object> keys = Collections.singletonList(combineKey);
        try {
            Long number = redisTemplate.execute(limitScript, keys, count, time);
            if (StringUtils.isNull(number) || number.intValue() > count) {
                throw new GlobalException(BizCodeEnum.RATE_LIMIT_ERROR);
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), combineKey);
        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        String key = parseKey(rateLimiter.key(), point);

        StringBuffer stringBuffer = new StringBuffer(key);
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append("-").append(IpUtils.getIpAddr()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }

    /**
     * 解析SpEL表达式
     */
    private String parseKey(String keyExpression, JoinPoint joinPoint) {
        // 如果不是SpEL表达式（不包含#或${}），直接返回
        if (!isSpelExpression(keyExpression)) {
            return keyExpression;
        }

        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();

            // 获取方法参数名
            String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
            if (paramNames == null) {
                return keyExpression;
            }

            // 创建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = joinPoint.getArgs();

            // 将参数设置到上下文中
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            // 解析表达式
            Expression expression = parser.parseExpression(keyExpression);
            return expression.getValue(context, String.class);
        } catch (Exception e) {
            log.error("解析限流Key的SpEL表达式失败: {}", keyExpression, e);
            return keyExpression;
        }
    }

    /**
     * 判断是否为SpEL表达式
     */
    private boolean isSpelExpression(String expression) {
        if (StringUtils.isEmpty(expression)) {
            return false;
        }
        // 包含#表示是SpEL表达式
        return expression.contains("#");
    }
}
