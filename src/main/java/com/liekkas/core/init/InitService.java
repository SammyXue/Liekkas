package com.liekkas.core.init;


import org.apache.log4j.Logger;

/**
 * 启动时需要初始化的类
 *
 */
public interface InitService {

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
