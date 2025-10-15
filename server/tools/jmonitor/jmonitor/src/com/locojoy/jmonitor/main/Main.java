package com.locojoy.jmonitor.main;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.locojoy.jmonitor.Manager;
import com.locojoy.jmonitor.Monitor;
import com.locojoy.jmonitor.util.ShutdownThread;
import com.locojoy.jmonitor.webapp.AccessManager;
import com.locojoy.jmonitor.webapp.FreemarkerViewProcessor;
import com.locojoy.jmonitor.webapp.MyLogFilter;
import com.locojoy.jmonitor.webapp.MySecurityFilter;


public class Main {

	static private Logger logger = Logger.getLogger(Main.class.getName());

	private static AccessManager accessManager;
	
	private static String configureFn;
	private static String jmxportFn;
	private static String accessFn;
	private static String lastfetchFn;
	private static boolean debug;
	
	public static void setLastfetchFn(String fn){
		lastfetchFn = fn;
	}
	
	public static AccessManager getAccessManager(){
		return accessManager;
	}
	
	public static String getConfigureFn(){
		return configureFn;
	}
	
	public static String getJmxportFn(){
		return jmxportFn;
	}
	
	public static String getAccessFn(){
		return accessFn;
	}
	
	public static String getLastfetchFn(){
		return lastfetchFn;
	}
	
	public static boolean isDebug(){
		return debug;
	}
	
	
	public static void main(String[] args) throws Exception {
		debug = Main.class.getResource("Main.class").getProtocol().equals("file");
		
		if (args.length < 4){
			System.out.println("usage : java -jar jmonitor.jar mbeandb.xml jmxports.xml access.xml port [ip]");
			return;
		}
		
		configureFn = args[0];
		jmxportFn = args[1];
		accessFn = args[2];
		accessManager = new AccessManager( accessFn );
		
		final Manager manager = Monitor.getManager();
		manager.start(configureFn, jmxportFn);
		
		ResourceConfig rc = new PackagesResourceConfig("com.locojoy.jmonitor.webapp");
		
		rc.getFeatures().put(LoggingFilter.FEATURE_LOGGING_DISABLE_ENTITY, true);
		rc.getProperties().put(FreemarkerViewProcessor.FREEMARKER_TEMPLATES_BASE_PATH, "/WEB-INF/templates");
		
		
		rc.getProperties().put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, 
				new StringBuilder()
					.append(MySecurityFilter.class.getName()).append(",")
					//.append(LoggingFilter.class.getName()).append(",")
					.append(GZIPContentEncodingFilter.class.getName())
					.toString());
		rc.getProperties().put(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, 
				new StringBuilder()
					.append(MyLogFilter.class.getName()).append(",")
					//.append(LoggingFilter.class.getName()).append(",")
					.append(GZIPContentEncodingFilter.class.getName())
					.toString());
		
		int port = Integer.parseInt(args[3]);
		InetSocketAddress sa = args.length > 4 ? new InetSocketAddress(args[4], port)  :  new InetSocketAddress(port);
		final HttpServer server = HttpServer.create(sa, 0);
		final ExecutorService executor = Executors.newCachedThreadPool();
		server.setExecutor(executor);
		server.createContext("/", ContainerFactory.createContainer(HttpHandler.class, rc));
		server.start();
		
		ShutdownThread.register(new Runnable() {
			
			@Override
			public void run() {
				logger.info("jmonitor shutdown");
				executor.shutdown();
				server.stop(1);
				manager.stop();
			}
		});
		
	}
	
	

}
