package com.liekkas.core.message.param;


/**
 * @author xuecm
 */
public class IntegerParamTransfer implements ParamTransfer<Integer>{
    @Override
    public Integer transferTo(String str) {
        if (str==null||str.isEmpty()){
            return 0;
        }


        return Integer.valueOf(str);
    }
}
