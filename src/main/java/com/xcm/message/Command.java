package com.xcm.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuecm
 */
public enum Command {

    HelloWord,
    TestException,
    TestStandardException,
    Login,
    LogOut,
    ;
    static Map<String, Command> commandMap = new HashMap<>();

    static {
        for (Command value : values()) {
            commandMap.put(value.name(), value);
        }
    }

    public static Command getCommandByName(String name) {

        Command cmd = commandMap.get(name);
        if (cmd == null) {
            throw new RuntimeException("Unknow command " + name);
        }
        return cmd;
    }
}
