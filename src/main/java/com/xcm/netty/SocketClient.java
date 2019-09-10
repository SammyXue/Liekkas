package com.xcm.netty;


import com.xcm.message.Command;
import com.xcm.message.MessageCreater;
import com.xcm.proto.Protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 因为NettyClient已经封装了协议层
 *
 * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder
 * @see io.netty.handler.codec.LengthFieldPrepender
 * @see com.xcm.netty.NettyClient
 * 所以用原生socket写一个客户端来说明协议
 * 最好的写法是参考netty源码中encode和decode的写法
 * 但是这里只是简单实验加说明
 */
public class SocketClient {

    /**
     * int转换成byte[]
     * @param value
     * @return
     */
    static byte[] intToByte(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) value;
        return bytes;

    }

    /**
     *
     * @param bytes
     * @return
     */
    static int byteToInt(byte[] bytes) {
        return handleFirst(bytes[3]) + (handleFirst(bytes[2]) << 8) + (handleFirst(bytes[1]) << 16) + (unhandleFirst(bytes[0]) << 24);
    }

    /**
     * 不处理补码
     *
     * @return
     */
    static int unhandleFirst(byte b) {
        return b;
    }

    /**
     * 处理补码
     *
     * @return
     */
    static int handleFirst(byte b) {
        return (b & 0xff);
    }


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5656);


        /**                                  发包                               **/
        Protocol.Param userId = Protocol.Param.newBuilder().setKey("userId").setValue("123").build();
        Protocol.Param password = Protocol.Param.newBuilder().setKey("password").setValue("12asddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddsadsadasd3").build();
        Protocol.Param sessionId = Protocol.Param.newBuilder().setKey("session").setValue("sessions").build();

        Protocol.Request request = MessageCreater.generateRequest(Command.Login, userId, password, sessionId);
        int length = request.toByteArray().length;
        byte[] bytes = new byte[length + 4];
        System.arraycopy(request.toByteArray(), 0, bytes, 4, length);
        //前四个字节是长度
        bytes[0] = (byte) (length >> 24);
        bytes[1] = (byte) (length >> 16);
        bytes[2] = (byte) (length >> 8);
        bytes[3] = (byte) length;
        socket.getOutputStream().write(bytes);


        /**                                  收包                               **/
        byte[] receive = new byte[10240];
        socket.getInputStream().read(receive);
        int receiveLen = byteToInt(receive);

        byte[] receiveBody = new byte[receiveLen];
        System.arraycopy(receive, 4, receiveBody, 0, receiveLen);
        Protocol.Response response = Protocol.Response.parseFrom(receiveBody);
        System.out.println(response);
        socket.close();
    }
}