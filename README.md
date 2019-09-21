# Liekkas
一个基于netty的网络框架，非常轻量和简单。
## 1. 处理请求的写法
```java
@Component
public class HelloWorldAction extends BaseAction {
    
    /**
    * 不带任何参数的一个请求
    * 
    *  @return 
    */
    @Path(Command.HelloWord)
    public Protocol.Response helloWord() {
        return getResult("hello".getBytes());
    }

    /**
    * 当请求发生异常时
    * 
    *  @return 
    */
    @Path(Command.TestException)
    public Protocol.Response testException() {
        throw new RuntimeException("test ");
    }

    /**
    * StandardSystemException是我自定义的一个异常，表示业务处理时候的异常。
    * 其errmsg会作为返回值的一部分包含在response中
    * 
    *  @return 
    */
    @Path(Command.TestStandardException)
    public Protocol.Response testStandardException() {
        throw new StandardSystemException("test ");
    }
    
    /**
    * 一个登陆demo,包含若干请求参数，以及session的使用
    * 你也可以把一个StandardRequest对象作为一个参数放在参数列表中，他将这次请求的所有内容
    * @param userId
    * @param password
    * @param request
    * @return 
    */
    @Path(Command.Login)
    public Protocol.Response Login(@Param("userId") long userId, @Param("password") String password, StandardRequest request) {
        if (request.getSession()==null){
            Session session = SessionManager.getInstance().createNewSession();
            session.addAttribute("userId",userId);
        }
        return getResult("success".getBytes());
    }
    
    
    /**
    * 登出demo，移除session
    * 
    * @param request
    * @return 
    */
    @Path(Command.LogOut)
    public Protocol.Response logOut(StandardRequest request) {
        SessionManager.getInstance().remove(request.getSession());
        return getResult("success".getBytes());
    }

}


```

##2. rpc调用的写法
我提供了同步和异步两种方式以适应各种逻辑。
* 同步rpc

首先创建一个RpcProxy对象,调用send函数即可发送请求给服务器，send会返回一个RpcFuture对象调用RpcFuture.get()会阻塞并返回一个response。
```java
    RpcProxy proxy = new RpcProxy(new RpcNettyClient("127.0.0.1", 5656));
    RpcFuture rpcFuture = proxy.send(Command.Login,arg1,arg2);
    Protocol.Response response = rpcFuture.get();
```

* 异步rpc

在写异步调用是，你可以新建一个Callback对象最为第二个参数传入send函数，他会在收到请求返回时根据返回结果选择调用onSuccess还是onFail。

```java
    RpcFuture rpcFuture = proxy.send(Command.Login,new RpcCallback() {
        @Override
        public void onFail(Protocol.Response response) {
            ...
        }

        @Override
        public void onSuccess(Protocol.Response response) {
            ...
        }
    },arg1,arg2);
```
如果需要有多个callback，也支持了在后续addCallback
```java
    rpcFuture.addCallback(new RpcCallback() {
        @Override
        public void onFail(Protocol.Response response) {
            ...
        }

        @Override
        public void onSuccess(Protocol.Response response) {
            ...
        }
    });
```