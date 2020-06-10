package com.liekkas.core;

/**
 * 游戏服务器的种类，目前有三类，可向后拓展
 */
public enum ServerType {

    GATEWAY(1), lOGIN(2), GAME(3);

    private int type;

    ServerType(int type) {
        this.type = type;
    }

    public static String getNameByType(int type) {
        for (ServerType serverType : ServerType.values()) {
            if (serverType.type == type) return serverType.name();
        }
        throw new RuntimeException("unknown serverType type:" + type);
    }
}
