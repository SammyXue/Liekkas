package com.xcm.service.core;

import com.xcm.message.Command;
import com.xcm.message.StandardRequest;
import com.xcm.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {


    protected static Map<Command, ActionInvoker> commandHandleMap = new HashMap<>();

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void handleRequest(ChannelHandlerContext ctx, StandardRequest request) {
        executorService.execute(() -> {
            try {
                Command command = Command.getCommandByName(request.getProtocolRequest().getHeader().getCommand());
                ActionInvoker invoker = commandHandleMap.get(command);
                if (invoker == null) {
//                    log
                    return;
                }


                ctx.writeAndFlush(invoker.invoke(request));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
