package com.xcm.action;

import com.xcm.message.Command;
import com.xcm.message.Param;
import com.xcm.message.Path;
import com.xcm.proto.Protocol;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldAction extends BaseAction {

    @Path(Command.HelloWord)
    public Protocol.Response helloWord() {
        return getResult("hello".getBytes());
    }

    @Path(Command.Login)
    public Protocol.Response Login(@Param("userId") long userId, @Param("password") String password) {

        return getResult("hello".getBytes());
    }

    @Path(Command.LogOut)
    public Protocol.Response logOut() {
        return getResult("hello".getBytes());
    }

}
