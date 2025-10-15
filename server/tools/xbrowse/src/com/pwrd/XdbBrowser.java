package com.pwrd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.console.XdbBrowserConsole;
import com.pwrd.io.IO;
import com.pwrd.ui.GsdLauncher;

public class XdbBrowser
{
	private static Log logger = LogFactory.getLog(XdbBrowser.class);
	
	public static void main(String[] args)
	{
		// 启动Xdb数据库
		/**
		xdb.Xdb.getInstance().setConf(new xdb.XdbConf("resources/xdb.xml"));
		xdb.Xdb.getInstance().start();
		xtable._Tables_.startExplicitLockCheck();
		*/
		boolean startCmd = false;
		for(int i = 0; i < args.length; ++ i)
		{
			if(args[i].equals("-cmd"))
			{
				startCmd = true;
				break;
			}
		}
		
		boolean startExportJson = false;
		for(int i = 0; i < args.length; ++ i)
		{
			if(args[i].equals("-exportjson"))
			{
				startExportJson = true;
				break;
			}
		}
		
		if(startExportJson){
			// 继续获取参数
			String xbeansPath = null;
			for(int i = 0; i < args.length; ++ i)
			{
				if(args[i].equals("-lib"))
				{
					xbeansPath = args[++ i];
					break;
				}
			}
			
			if(!JavaLibraryLoader.load(xbeansPath)){
				logger.error("JavaLibraryLoader error!!!");
				return;
			}
			
			String flagOut = "dbout=";
			String flagIn = "dbin=";
			
			String jsonDBIn = null;
			String jsonDBOut = null;
			for(int i = 0; i < args.length; ++ i)
			{
				if(args[i].startsWith(flagIn) && args[i].length() > flagIn.length())
				{
					jsonDBIn = args[i].split("=")[1];
				} else if (args[i].startsWith(flagOut) && args[i].length() > flagOut.length()) {
					jsonDBOut = args[i].split("=")[1];
				}
			}
			
			if(jsonDBIn == null || jsonDBIn.isEmpty()){
				logger.error("Invaild Mkdb Directory");
				return;
			}
			
			jsonDBIn = jsonDBIn.replace("\\", "/");
			File fJsonDBIn = new File(jsonDBIn);
			if(!fJsonDBIn.isDirectory()){
				logger.error("Invaild Mkdb Directory");
				return;
			}
			
			if(jsonDBOut == null || jsonDBOut.isEmpty()){
				jsonDBOut = "./jsonout";
			}
			
			File fJsonOutDir = new File(jsonDBOut);
			if(!fJsonOutDir.isDirectory()){
				if(!fJsonOutDir.mkdir()){
					jsonDBOut = "./jsonout";
					if(!fJsonOutDir.mkdir()){
						logger.error("fJsonOutDir.mkdir error!");
					}
				}
			}else{
				for (File fDel : fJsonOutDir.listFiles()) {
					if(!fDel.delete()){
						IO.getInstance().println("JSON,删除目录" + jsonDBOut + "内容失败");
						return;
					}
				}
			}
			 
			String[] strDBInInfo = jsonDBIn.split("/");
			if(strDBInInfo.length < 1){
				logger.error("Invaild Mkdb Directory");
				return;
			}
			
			logger.error("json input directory is " + jsonDBIn);
			logger.error("json output directory is " + jsonDBOut);
			try {
				XdbBrowserConsole.getInstance().execute("adddbx " + jsonDBIn);
				XdbBrowserConsole.getInstance().execute("use " + strDBInInfo[strDBInInfo.length - 1]);
				XdbBrowserConsole.getInstance().execute("json " + jsonDBOut);
			} catch (IOException e) {
				logger.error("json output error!!!");
				e.printStackTrace();
			}
			return;
		}
		
		// 支持从控制台输出
		if(startCmd)
		{
			String xbeansPath = null;
			// 继续获取参数
			for(int i = 0; i < args.length; ++ i)
			{
				if(args[i].equals("-lib"))
				{
					xbeansPath = args[++ i];
					break;
				}
			}
			
			if(JavaLibraryLoader.load(xbeansPath))
				XdbBrowserConsole.getInstance().execute();
			else
				logger.error("Invalid xbeans library: " + xbeansPath + ", failed to startup xbrowse console.");
		}
		else
		{
			GsdLauncher gsdLauncher = new GsdLauncher();
			gsdLauncher.setVisible(true);
		}
	}
}