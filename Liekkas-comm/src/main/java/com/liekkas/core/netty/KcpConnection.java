package com.liekkas.core.netty;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KcpConnection {
    InetSocketAddress targetAddress;
    DatagramSocket socket;
    KCP kcp;

    public KcpConnection(int conversationId, String targetIp, int targetPort, int localPort, Object user) throws SocketException {
        socket = new DatagramSocket(localPort);
        this.targetAddress = new InetSocketAddress(targetIp, targetPort);
        kcp = new KCP(conversationId, user) {
            @Override
            protected void output(byte[] buffer, int size) {
                DatagramPacket packet = new DatagramPacket(buffer, size);
                packet.setSocketAddress(targetAddress);
                try {
//                    if (user.equals("client")) {
                        socket.send(packet);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        refresh();
        receive();

    }

    /**
     * 开启一个线程接受udp消息并input进kcp
     */
    private void receive() {

        new Thread(() -> {
            while (true) {
                byte[] receBuf = new byte[10240];
                DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
                try {
                    socket.receive(recePacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (recePacket.getLength() != 0) {
                    byte[] arr = Arrays.copyOf(receBuf, recePacket.getLength());

                    printKCPData(arr);
                    synchronized (kcp) {
                        kcp.Input(arr);
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void printKCPData(byte[] arr) {
        System.out.println("----------------------");

        int offset = 0;

        System.out.println(kcp.user);
        System.out.println("udp data");
        /**
         * conversationId对话id
         */
        long conv = KCP.ikcp_decode32u(arr, offset);
        offset += 4;
        /**
         * cmd
         * @see com.liekkas.core.netty.KCP.IKCP_CMD_PUSH 81
         * @see com.liekkas.core.netty.KCP.IKCP_CMD_ACK 82
         * @see com.liekkas.core.netty.KCP.IKCP_CMD_WASK 83
         * @see com.liekkas.core.netty.KCP.IKCP_CMD_WINS 84
         */
        int cmd = arr[offset++];
        /**
         * 分片，用户数据可能会被分成多个KCP包，发送出去
         */
        int frg = arr[offset++];
        /**
         * 接收窗口大小，发送方的发送窗口不能超过接收方给出的数值
         */
        int wnd = KCP.ikcp_decode16u(arr, offset);
        offset += 2;
        /**
         * 时间序列
         */
        long ts = KCP.ikcp_decode32u(arr, offset);
        offset += 4;
        /**
         * 序列号
         */
        long sn = KCP.ikcp_decode32u(arr, offset);
        offset += 4;
        /**
         * 下一个可接收的序列号。其实就是确认号，收到sn=10的包，una为11
         */
        long una = KCP.ikcp_decode32u(arr, offset);
        offset += 4;
        /**
         * 数据长度
         */
        int len = (((int) arr[offset++]) << 24) + (((int) arr[offset++]) << 16) + (((int) arr[offset++]) << 8) + (int) arr[offset++];
        String data = new String(Arrays.copyOfRange(arr, offset, arr.length));
        System.out.println("conv:" + conv);
        System.out.println("cmd:" + cmd);
        System.out.println("frg:" + frg);
        System.out.println("wnd:" + wnd);
        System.out.println("ts:" + ts);
        System.out.println("sn:" + sn);
        System.out.println("una:" + una);
        System.out.println("len:" + len);
        System.out.println("data:" + data);

    }

    /**
     * 每隔10ms刷新kcp内部消息
     */
    public void refresh() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int len = 0;
            byte[] data = new byte[10240];
            synchronized (kcp) {
                kcp.Update(System.currentTimeMillis());
                len = kcp.Recv(data);
            }
            if (len > 0) {
                String str = new String(data, 0, len, CharsetUtil.UTF_8);
                System.out.println("----------------------");
                System.out.println(kcp.user);
                System.out.println("len:"+len);
                System.out.println("kcp data:" + str);
            } else {
//                System.err.println("error occur len:" + len);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void sendMessage(byte[] buffer) {
        synchronized (kcp) {

            kcp.Send(buffer);

        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        KcpConnection server = new KcpConnection(1, "localhost", 8800, 9900, "server");

        KcpConnection client = new KcpConnection(1, "localhost", 9900, 8800, "client");


        int index =0;
        while (true) {
            String str = "liekkas"+index++;
            client.sendMessage(str.getBytes());
//            server.sendMessage("server say Hello".getBytes(CharsetUtil.UTF_8));
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        kcp.Input("HELLO".getBytes());
    }
}
