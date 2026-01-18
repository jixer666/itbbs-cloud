# SSE推送没法实时推送问题

## 问题场景

AI 流式聊天接口使用 SSE 进行消息推送，调用 `itbbs.com` 的域名接口没法推送，需要等请求数据全部收到后才会一次性全部推送过来，但调用 `localhost:14000` 聊天服务接口可以实时推送过来，两者的唯一区别在于域名接口多了一层 Nginx，返回的数据先要进过 Nginx，由 Nginx 返回给客户端

## 解决方案

Nginx 会缓存代理服务器的响应（聚合类型），服务推送的数据被 Nginx 缓存到缓冲区，导致客
户端没有实时收到数据，而是等到服务所有数据推送完后，客户端才一次性收到了所有数据

解决办法：**禁用缓存功能，添加响应头：`X-Accel-Buffering:no`**

Nginx配置如下：

```nginx
location /ai/bot/chat/stream {
    # 基础代理配置
    proxy_pass http://itbbs-gateway/ai/bot/chat/stream;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    
    # ==== 关键：禁用缓冲 ====
    proxy_buffering off;
    
    # ==== 适当设置缓冲区（避免关闭太快）====
    proxy_buffer_size 4k;
    proxy_buffers 4 4k;
    
    # ==== 超时时间（与后端匹配）====
    proxy_read_timeout 300s;     # 5分钟（先测试短时间）
    proxy_send_timeout 300s;
    proxy_connect_timeout 30s;
    
    # ==== HTTP 1.1 ====
    proxy_http_version 1.1;
    
    # ==== 保持连接 ====
    proxy_set_header Connection '';
    
    # ==== 添加响应头 ====
    add_header X-Accel-Buffering no;
    add_header Cache-Control no-cache;
}
```

## 参考文章

[(https://blog.csdn.net/weixin_43837268/article/details/147930394](https://blog.csdn.net/weixin_43837268/article/details/147930394)