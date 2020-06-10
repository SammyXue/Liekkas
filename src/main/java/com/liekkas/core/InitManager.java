package com.liekkas.core;


/**
 * 启动时需要初始化
 */
public interface InitManager {

    void init() throws Exception;

    /**
     * 有时候初始化类需要先后顺序 请设置这个值，值越大执行顺序越前
     *
     * @return
     */
    default int priority() {
        return 0;
    }
}
