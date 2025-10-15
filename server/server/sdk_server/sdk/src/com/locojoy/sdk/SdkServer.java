package com.locojoy.sdk;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import mkdb.util.JMXServer;
import mkdb.util.MBeans;

import com.sun.net.httpserver.HttpServer;

public class SdkServer {
	private static Logger logger = Logger.getLogger(SdkServer.class);
	private  static javax.management.MBeanServer mbs=java.lang.management.ManagementFactory.getPlatformMBeanServer();
	
	static void registerMbean(Object obj,String name) throws javax.management.NotCompliantMBeanException,javax.management.MBeanRegistrationException,javax.management.InstanceAlreadyExistsException,javax.management.MalformedObjectNameException{
		mbs.registerMBean(obj, new javax.management.ObjectName("bean:name="+name));
	}

	static public void initXdbModule() throws Exception {
		new File("mkdb").mkdirs();
		new File("mbackup").mkdirs();
		
		final mkdb.MkdbConf conf=new mkdb.MkdbConf("sdk.db.xml");
		final mkdb.Mkdb mymkdb = mkdb.Mkdb.getInstance();
		mymkdb.setConf(conf);

		//if file mkdb.inuse exist,delete it
		File inUse = new File("mkdb/mkdb.inuse");
		if(inUse.exists())
			inUse.delete();
		mymkdb.start();
	}
	
	static private void initOnlinesModule() throws Exception {
		try {
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("sdk.xio.xml");
			mkio.Engine.getInstance().register( new mkio.MkioConf(doc.getDocumentElement()));
		} catch (final ClassNotFoundException ex) {
			throw ex;
		}
	}

	static private void initHttpServer() throws Exception {
		InetSocketAddress sa = new InetSocketAddress(Config.getInstance().getHttpServerIP(), Config.getInstance().getHttpServerPort());
		final HttpServer server = HttpServer.create(sa, 0);
		final ExecutorService executor = Executors.newCachedThreadPool();
		server.setExecutor(executor);
		server.createContext("/", new HttpServerHandler());
		server.start();
	}
	
	static private void initJMXServer() throws IOException {
		new JMXServer(Config.getInstance().getJmxRmiPort(), Config.getInstance().getJmxServerPort(), null, null, null).start();
		
		final MBeans.Manager manager = new MBeans.Manager();
		manager.register(new MBeanOperators(), "userinfo:type=userinfo");
	}
	
	private static void init() throws Exception {
		try {
			PropertyConfigurator.configure("log4j.properties");
			logger.info("log4j init over!");
			initXdbModule();
			Config.Init();
			initJMXServer();
			if(Config.getInstance().isRedisConnect()){
				RedisManager.getPool();
				logger.info("redis pool init over!");
			}
			initOnlinesModule();
			initHttpServer();
		} catch (Exception e) {
			mkdb.Trace.error("module init exception", e);
			throw e;
		}
	}
	
	public static void exit() {
		RedisManager.getPool().close();
		mkio.Engine.getInstance().close();
		mkdb.Mkdb.getInstance().stop();
	}

	public static void main(String[] args) throws Exception {
		init();
		serverOnlineLogThread();
		mkio.Engine.getInstance().open();
		final Stopper stopper=new Stopper();
		registerMbean(stopper,"stopper");
		stopper.doWait();
		exit();
	}
	
	private static void serverOnlineLogThread(){
		Thread thread=new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						XioManager.getInstance().showCurrentServer();
						Thread.sleep(1000 * 60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		thread.start();
	}
}
