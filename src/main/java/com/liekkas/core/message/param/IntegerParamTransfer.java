package com.liekkas.core.message.param;

import org.springframework.util.StringUtils;

/**
 * @author xuecm
 */
public class IntegerParamTransfer implements ParamTransfer<Integer>{
    @Override
    public Integer transferTo(String str) {
        if (StringUtils.isEmpty(str)){
            return 0;
        }


        return Integer.valueOf(str);
    }
}
