package com.xcm.message;

public class StringParamTransfer implements ParamTransfer<String> {
    @Override
    public String transferTo(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }
}
