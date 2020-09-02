package com.liekkas.core.netty;

public class KcpServer {
    public static void main(String[] args) {
        byte[] arr = new byte[4];
        arr[3] = 1;
        int offset = 0;
        int conv = (((int) arr[offset++]) << 24) + (((int) arr[offset++]) << 16) + (((int) arr[offset++]) << 8) + (int) arr[offset++];
        System.out.println(conv);
        System.out.println(System.currentTimeMillis());

    }
}
