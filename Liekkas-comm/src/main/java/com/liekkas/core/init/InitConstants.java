package com.liekkas.core.init;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InitConstants {
    private static Logger logger = Logger.getLogger(InitConstants.class);

    /**
     * server.properties
     */
    public static Properties severProperties = new Properties();


    /**
     * 资源文件path
     */
    public static String RESOURCE_PATH = System.getProperty("user.dir") + File.separator +
            "src" + File.separator +
            "main" + File.separator +
            "resources" + File.separator;

    public static final String SESSION_MANAGER_NAME = "sessionManager";

    public static final String INIT_SERVICE_MANAGER = "initServiceManager";



}
