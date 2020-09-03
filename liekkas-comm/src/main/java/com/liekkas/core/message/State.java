package com.liekkas.core.message;

public enum State {
    //成功
    SUCCESS(1),
    //失败
    FAIL(2);
    private int value;

    State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
