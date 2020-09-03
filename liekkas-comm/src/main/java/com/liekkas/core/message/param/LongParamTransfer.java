package com.liekkas.core.message.param;


public class LongParamTransfer implements ParamTransfer<Long> {
    @Override
    public Long transferTo(String str) {
        if (str==null||str.isEmpty()){
            return 0L;
        }
        return Long.valueOf(str);
    }
}
