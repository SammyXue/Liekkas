package com.liekkas.core.message.param;

import org.springframework.util.StringUtils;

public class LongParamTransfer implements ParamTransfer<Long> {
    @Override
    public Long transferTo(String str) {
        if (StringUtils.isEmpty(str)){
            return 0L;
        }
        return Long.valueOf(str);
    }
}
