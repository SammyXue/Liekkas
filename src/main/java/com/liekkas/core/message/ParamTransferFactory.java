package com.liekkas.core.message;

import java.util.HashMap;
import java.util.Map;

public class ParamTransferFactory {
    static Map<Class, ParamTransfer> map = new HashMap<>();
    static final IntegerParamTransfer INTEGER_PARAM_TRANSFER = new IntegerParamTransfer();
    static final LongParamTransfer LONG_PARAM_TRANSFER = new LongParamTransfer();
    static final StringParamTransfer STRING_PARAM_TRANSFER = new StringParamTransfer();

    static {
        map.put(int.class, INTEGER_PARAM_TRANSFER);

        map.put(Integer.class, INTEGER_PARAM_TRANSFER);
        map.put(Long.class, LONG_PARAM_TRANSFER);
        map.put(long.class, LONG_PARAM_TRANSFER);
        map.put(String.class, STRING_PARAM_TRANSFER);


    }

    public static ParamTransfer getByParamType(Class clazz) {

        ParamTransfer paramTransfer = map.get(clazz);
        if (paramTransfer == null) {
            throw new UnsupportedOperationException("Unsupport for param type " + clazz.getName());
        }
        return paramTransfer;
    }

}
