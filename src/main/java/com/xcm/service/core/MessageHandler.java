package com.xcm.service.core;

import com.xcm.message.Command;
import com.xcm.message.Path;
import com.xcm.proto.Protocol;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {


    static Map<Command, ActionInvoker> commandHandleMap = new HashMap<>();

    static {
        try {
            Class clazz = Class.forName("com.xcm.action.HelloWorldAction");

            Object object = clazz.getConstructor().newInstance();

            for (Method method : clazz.getDeclaredMethods()) {
                Path path = method.getAnnotation(Path.class);
                if (path != null) {
                    ActionInvoker actionInvoker = new ActionInvoker(method, object);
                    commandHandleMap.put(path.command(), actionInvoker);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void handleRequest(ChannelHandlerContext ctx, Protocol.Request request) {
        executorService.execute(() -> {
            try {
                Command command = Command.getCommandByName(request.getHeader().getCommand());
                ActionInvoker invoker = commandHandleMap.get(command);
                if (invoker == null) {
//                    log
                    return;
                }


                ctx.writeAndFlush(invoker.invoke());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
