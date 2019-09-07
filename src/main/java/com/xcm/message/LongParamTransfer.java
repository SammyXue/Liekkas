package com.xcm.message;

import io.netty.util.internal.StringUtil;
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
