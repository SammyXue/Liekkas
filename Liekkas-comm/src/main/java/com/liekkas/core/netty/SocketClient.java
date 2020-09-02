package com.liekkas.core.netty;


import com.liekkas.core.message.Command;
import com.liekkas.core.message.MessageCreater;
import com.liekkas.core.message.proto.Protocol;

import java.io.IOException;
import java.net.Socket;

/**
 * 因为NettyClient已经封装了协议层
 *
 * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder
 * @see io.netty.handler.codec.LengthFieldPrepender
 * @see RpcNettyClient
 * 所以用原生socket写一个客户端来说明协议
 * 最好的写法是参考netty源码中encode和decode的写法
 * 但是这里只是简单实验加说明
 */
public class SocketClient {

    /**
     * int转换成byte[]
     *
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
        long before = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {

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
//        byte[] send = new byte[2*length + 8];
//        System.arraycopy(bytes,0,send,0,bytes.length);
//        System.arraycopy(bytes,0,send,bytes.length,bytes.length);
//
//        byte[] send = new byte[length + 2];
//
//        System.arraycopy(bytes, 2, send, 0, send.length );
//
//        socket.getOutputStream().write(new byte[]{bytes[0],bytes[1]});

            for (int j = 0; j < 1; j++) {


                socket.getOutputStream().write(bytes);

                /**                                  收包                               **/
                byte[] receive = new byte[10240];
                socket.getInputStream().read(receive);
                int receiveLen = byteToInt(receive);

                byte[] receiveBody = new byte[receiveLen];
                System.arraycopy(receive, 4, receiveBody, 0, receiveLen);
                Protocol.Response response = Protocol.Response.parseFrom(receiveBody);
//                System.out.println(response);
            }
            socket.close();
        }
        long after = System.currentTimeMillis();
        System.out.println(after-before);


    }
}