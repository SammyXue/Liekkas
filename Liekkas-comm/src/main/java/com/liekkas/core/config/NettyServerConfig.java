package com.liekkas.core.config;

public interface NettyServerConfig extends ServerConfig {
    int getBossGroupSize();

    int getWorkerGroupSize();
}