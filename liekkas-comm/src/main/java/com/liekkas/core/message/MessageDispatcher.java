package com.liekkas.core.message;

import com.liekkas.core.exception.ServiceException;
import com.liekkas.core.invoker.ActionInvoker;
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
                ctx.writeAndFlush(ActionInvoker.genErrorResponse(new ServiceException("no such command "+command.name()),request.getRequestId()));

                return;
            }

            //logger.info("handleRequest \n"+request.getProtocolRequest());

            ctx.writeAndFlush(invoker.invoke(request));

        } catch (Exception e) {
            logger.error("handleRequest error",e);
        }
    }
}
