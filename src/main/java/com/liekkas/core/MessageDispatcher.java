package com.liekkas.core;

import com.liekkas.core.message.Command;
import com.liekkas.core.message.StandardRequest;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {

    static Logger logger = Logger.getLogger(MessageDispatcher.class);

    public static Map<Command, ActionInvoker> commandHandleMap = new HashMap<>();


    public static void handleRequest(ChannelHandlerContext ctx, StandardRequest request) {
        try {
            Command command = Command.getCommandByName(request.getProtocolRequest().getHeader().getCommand());
            ActionInvoker invoker = commandHandleMap.get(command);
            if (invoker == null) {
                return;
            }


            ctx.writeAndFlush(invoker.invoke(request));

        } catch (Exception e) {
            logger.info("handleRequest error",e);
        }
    }
}
