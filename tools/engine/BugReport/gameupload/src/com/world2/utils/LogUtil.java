package com.world2.utils;

import org.apache.log4j.*;

public class LogUtil {
    /**
     * ��ʼ����Logger������Appender��Logger��
     */
	public static final Logger logger = initLogger();


    /**
    * ��ʼ��logger
    * @return
    */
	private static Logger initLogger() {
		Logger logger = Logger.getRootLogger();

       //Logger Appender������ļ�·��
		String fileName = UploadConfig.getInstance().defaultTomcatCommonConfigPath() + "log4j.conf";
		
		if (fileName == null) { //���û��ָ��log�ļ��������û�������
			BasicConfigurator.configure();
			logger.debug( "Log4j config file not found! logger basic configured.");
			return logger;
       }

       //�������ļ�����logger appender
       PropertyConfigurator.configure(fileName);

       logger.debug("loggerConf File: " + fileName);

       return logger;
   }


}
