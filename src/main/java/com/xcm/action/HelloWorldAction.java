package com.xcm.action;

import com.xcm.message.Command;
import com.xcm.message.Path;
import com.xcm.proto.Protocol;

public class HelloWorldAction extends BaseAction{

    @Path(command = Command.HelloWord)
    public Protocol.Response helloWord() {
        return getResult("hello".getBytes());
    }

}
