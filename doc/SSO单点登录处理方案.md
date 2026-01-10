# SSO 单点登录处理方案

## 业务场景

在系统中有多个网站，例如：`itbbs.com` 主站、 `blog.itbbs.com` 博客网站

现在想要实现在主站登录一次后，在博客网站就无需登录，自动就登录上

## 技术实现

### 共享 Cookie

适用于同一个顶级域名的情况

在登录成功后由统一认证服务将 token 放到 cookie 中，所属的域是顶级域名

本次情况适用于方法


### URL 重定向传播会话

适用于不同客户端，同一个 Redis 的情况

现在有两个客户端：`clientA.com`，`clientB.com`，一个服务端：`server.com`

1、用户访问 `user.clientA.com` 发现未登录，跳转到 `auth.clientA.com?back=user.clientA.com` clientA 的登录页面，然后 clientA 的登录页面重定向到 `auth.server.com?redirect=auth.clientA.com&back=user.clientA.com` 统一认证的登陆页面

2、用户在 `auth.server.com` 登录后，会在 `auth.server.com` 保存一个全局的会话（向统一认证网站保存一个 cookie）， 统一认证服务会生成一个 ticketA 重定向到 clientA 登陆页 `auth.clientA.com?back=user.clientA.com&ticket=xxxxx`，这个登录页会根据 ticketA 进行校验，校验成功后会删除 ticketA，然后会生成一个新的 ticketB（Token） 返回给前端，这样就拿到了 Token 信息

3、用户此时又访问 `user.clientB.com` 发现未登录，跳转到 `auth.clientB.com?back=user.clientB.com` clientA 的登录页面，然后 clientA 的登录页面重定向到 `auth.server.com?redirect=auth.clientA.com&back=user.clientA.com` 统一认证的登陆页面，此时统一认证登录页是带上了全局会话的 cookie 的所以会生成一个 ticketA，然后带着 ticketA 重定向到系统 B 的登录页 `auth.clientB.com?back=user.clientB.com&ticket=xxxx，这个登录页会根据 ticketA 进行校验，校验成功后会删除 ticketA，然后会生成一个新的 ticketB（Token） 返回给前端，这样就拿到了 Token 信息

### Http 请求获取会话

适用于不同客户端，不同 Redis 的情况

是 URL 重定向传播会话的一种变种，区别是在拿到 ticketA 后由原先的自己服务内查询 redis 就能校验变为调用 Http 查询统一认证服务获取然后生成 ticketB（Token） 返回

## 参考

- [SSO模式一 共享Cookie同步会话](https://sa-token.cc/doc.html#/sso/sso-type1)
- [SSO模式二 URL重定向传播会话](https://sa-token.cc/doc.html#/sso/sso-type2)
- [SSO模式三 Http请求获取会话](https://sa-token.cc/doc.html#/sso/sso-type3)
