package com.liekkas.service;

import com.liekkas.core.action.BaseAction;
import com.liekkas.core.exception.ServiceException;
import com.liekkas.core.message.*;
import com.liekkas.core.message.param.Param;
import com.liekkas.core.message.proto.Protocol;
import com.liekkas.core.session.Session;
import com.liekkas.core.session.SessionService;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldAction extends BaseAction {

    @Path(Command.HelloWord)
    public Protocol.Response helloWord() {
        return getResult("hello".getBytes());
    }

    @Path(Command.TestException)
    public Protocol.Response testException() {
        throw new RuntimeException("test ");
    }

    @Path(Command.TestStandardException)
    public Protocol.Response testStandardException() {
        throw new ServiceException("test ");
    }


    @Path(Command.Login)
    public Protocol.Response Login(@Param("userId") long userId, @Param("password") String password, StandardRequest request) {
        if (request.getSession() == null) {
            Session session = SessionService.getInstance().createNewSession();
            session.addAttribute("userId", userId);
        }
        return getResult("success".getBytes(),request);
    }



    @Path(value = Command.LogOut, rpc = true)
    public Protocol.Response logOut(StandardRequest request) {
        SessionService.getInstance().remove(request.getSession());
        return getResult("success".getBytes());
    }

}
