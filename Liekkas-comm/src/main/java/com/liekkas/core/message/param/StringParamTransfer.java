package com.liekkas.core.message.param;

public class StringParamTransfer implements ParamTransfer<String> {
    @Override
    public String transferTo(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
}
