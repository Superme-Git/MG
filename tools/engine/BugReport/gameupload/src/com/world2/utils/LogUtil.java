package com.world2.utils;

import org.apache.log4j.*;

public class LogUtil {
    /**
     * 初始化的Logger（配置Appender的Logger）
     */
	public static final Logger logger = initLogger();


    /**
    * 初始化logger
    * @return
    */
	private static Logger initLogger() {
		Logger logger = Logger.getRootLogger();

       //Logger Appender定义的文件路径
		String fileName = UploadConfig.getInstance().defaultTomcatCommonConfigPath() + "log4j.conf";
		
		if (fileName == null) { //如果没有指定log文件，则设置基本配置
			BasicConfigurator.configure();
			logger.debug( "Log4j config file not found! logger basic configured.");
			return logger;
       }

       //用配置文件配置logger appender
       PropertyConfigurator.configure(fileName);

       logger.debug("loggerConf File: " + fileName);

       return logger;
   }


}
