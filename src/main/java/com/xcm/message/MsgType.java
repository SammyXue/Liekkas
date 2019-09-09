package com.xcm.message;

public enum MsgType {
    //回包
    ECHO(1),
    //推送
    PUSH(2);
    private int value;

    MsgType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
