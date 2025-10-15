package com.locojoy.jmonitor.spi;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.jmonitor.main.Main;
import com.locojoy.jmonitor.util.DomHelper;


class ServerManager {
	
	static class TableLastFetch {
		
		private String filename;
		private Document document;
		private Element element;
		
		private Map<String, Long> lastfetch_map = new HashMap<String, Long>();
		private volatile long last_sum_day;
		private volatile long last_rename_table;
		
		private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		
		public TableLastFetch(String fn) throws Exception {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename = fn);
			element = document.getDocumentElement();
			
			String lsd = element.getAttribute("last_sum_day");
			if (lsd.length() > 0)
				last_sum_day = df.parse( lsd ).getTime();
			
			String lrt = element.getAttribute("last_rename_table");
			if (lrt.length() > 0)
				last_rename_table = df.parse( lrt ).getTime();
			
			for (Element e : DomHelper.getElementsByName(element, "lastfetch")) {
				String table = e.getAttribute("table");
				long time = df.parse( e.getAttribute("time") ).getTime();
				if (null != lastfetch_map.put(table, time))
					throw new Exception(table + " duplicate table name");
			}
		}
		
		long getLastSumDayTime(){
			return last_sum_day;
		}
		
		void setLastSumDayTime(long time){
			last_sum_day = time;
			element.setAttribute("last_sum_day", df.format(new Date(time)));
		}
		
		long getLastRenameTableTime(){
			return last_rename_table;
		}
		
		void setLastRenameTableTimee(long time){
			last_rename_table = time;
			element.setAttribute("last_rename_table", df.format(new Date(time)));
		}
		
		synchronized long getLastFetchTime(String tablename) {
			Long v = lastfetch_map.get(tablename);
			return null != v ? v : 0;
		}
		
		synchronized void setLastFetchTime(String tablename, long time) {
			if (null != lastfetch_map.put(tablename, time) ){
				for (Element e : DomHelper.getElementsByName(element, "lastfetch")) {
					if (e.getAttribute("table").equals(tablename)){
						e.setAttribute("time", df.format(new Date(time)));
						return;
					}
				}
			}
			
			Element e = document.createElement("lastfetch");
			e.setAttribute("table", tablename);
			e.setAttribute("time", df.format(new Date(time)));
			element.appendChild(e);
		}
		
		void saveConfig() {
			try {
				DomHelper.prettySave(document, filename);
			} catch (Exception e) {
				logger.warning( "lastfetch.saveConfig error: " + e.getMessage());
			}
		}
		
	}
	
	static private Logger logger = Logger.getLogger(ServerManager.class.getName());
	static private final long MS_PER_DAY = 1000 * 60 * 60 * 24;
	
	private Map<Integer, Server> server_map = new HashMap<Integer, Server>();
	private Object mutex = new Object();
	
	private ScheduledExecutorService executor;
	private ServerBeans beans = new ServerBeans();

	static final int MIN_RENAME_DAY  = 7;
	static final int MIN_RENAME_SIZE = 1024*1024*100;

	Configure configure;
	private TableLastFetch table_last_fetch;
	
	private String pfname;
	private String pfip;
	private int pfport;
	
	ServerManager(Configure cfg, Element ele) throws Exception{
		configure = cfg;
		
		for (Element e : DomHelper.getElementsByName(ele, "portforwarder")) {
			pfname = e.getAttribute("name");
			pfip = e.getAttribute("ip");
			pfport = Integer.parseInt( e.getAttribute("port") );
			break;
		}
				
		String fn = new File(new File(cfg.getFileName()).getParentFile(), ele.getAttribute("table_last_fetch")).getAbsolutePath();
		Main.setLastfetchFn(fn);
		table_last_fetch = new TableLastFetch(fn);
	}
	
	void testPfAlive() throws Exception{
		if (null == pfname)
			throw new Exception("no portforwarder");

		Socket sk = new Socket();
		try{
			sk.connect(new InetSocketAddress(pfip, pfport), 5000);
		}catch(Exception e){
			throw new Exception(pfname + ", " + pfip + ":" + pfport + ", " + e.getMessage());
		}finally{
			try{
				sk.close();
			}catch(IOException e){
			}
		}
	}
	

	void start() throws Exception {

		executor = Executors.newSingleThreadScheduledExecutor();
		
		executor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				long now = System.currentTimeMillis();
				long now_minute = now / 60000;
				long now_minute_align  = now_minute * 60000;
				
				List<Table> tables = configure.getTableManager().getTableList();
				
				List<Table> pulltables = new ArrayList<Table>();
				for (Table t : tables){
					long last_fetch_minute = table_last_fetch.getLastFetchTime(t.getIdentification()) / 60000 ;
					
					int offset = t.getFetchOffsetMinute();
					int period = t.getFetchPeriodMinute();
					
					if (   (0 != last_fetch_minute && ( now_minute - last_fetch_minute ) >= period) ||
						   (0 == last_fetch_minute && ((now_minute - offset) % period) <= 5 ) ){
						pulltables.add(t);
					}
				}
				
				
				if (!pulltables.isEmpty()){
					List<Server> servers = getServers();
					boolean hasConnectedServer = false;
					for (Server s : servers){
						s.pull(now_minute_align, pulltables);
						if (s.isOpened())
							hasConnectedServer = true;
					}
					
					//只要有服务器连上就综合
					if (hasConnectedServer){
						// 抓取完一轮后，把所有的服务器数据综合一次
						for (Table t : pulltables){
							t.storeDb_SUMZID(now_minute_align);
							
							int offset = t.getFetchOffsetMinute();
							int period = t.getFetchPeriodMinute();
							table_last_fetch.setLastFetchTime(t.getIdentification(), ((now_minute - offset) / period * period + offset )* 60000);
						}
						
						table_last_fetch.saveConfig();
					}

					
					logger.info(new StringBuilder()
						.append("fetch server.size=").append(servers.size()).append(", ")
						.append("table.size=").append(pulltables.size()).append(", ")
						.append("used.millis=").append(System.currentTimeMillis() - now)
						.toString());
					
				}
				
				
				now = System.currentTimeMillis();
				long now_day = now / MS_PER_DAY;
				long now_day_align = now_day * MS_PER_DAY;
				
				long lastsumday = table_last_fetch.getLastSumDayTime();
				// 每天标准时间0点（北京时间8点）综合前天数据
				if ( (0 != lastsumday && (now - lastsumday) >=  MS_PER_DAY) ||
					 (0 == lastsumday && (now % MS_PER_DAY) <= 300000) ){
					
					for(Table t : tables)
						t.storeDb_DAY(now_day_align);
					
					logger.info( new StringBuilder().append("fetch.storeDay used.millis=").append(System.currentTimeMillis() - now).toString());
					
					table_last_fetch.setLastSumDayTime(now_day_align);

					// 每隔一定天数rename表
					if (now_day % MIN_RENAME_DAY == 0) {
						long lastrename = table_last_fetch.getLastRenameTableTime();
						if( (0 != lastrename && (now - lastrename) >=  MS_PER_DAY*MIN_RENAME_DAY) ||
							(0 == lastrename && (now % MS_PER_DAY) <= 300000) ){
							
							for(Table t : tables){
								if (t.getCurrentTableSize() > MIN_RENAME_SIZE)
									t.renameDb(now_day_align);
							}
							
							table_last_fetch.setLastRenameTableTimee(now_day_align);
						}
					}
					
					table_last_fetch.saveConfig();
				}
				
			}

		}, 30, 30, TimeUnit.SECONDS);

		logger.info("ServerManager started!");

	}

	void stop() {
		executor.shutdown();
		logger.info("ServerManager shutdown");
		for (Server s : getServers())
			s.close();
	}

	void addServer(int id, String url, String desc) throws Exception {
		addServer(id, url, desc, Collections.<String, String> emptyMap());
	}
	
	void addServer(int id, String url, String desc, Map<String, String> extra) throws Exception {
		if (null == url || url.length() == 0 || id <= 0 || null == desc)
			throw new Exception("url null/empty or id < 0 or desc null");

		Server server = new Server(this, url, id, desc, extra);
		synchronized (mutex) {
			if (server_map.containsKey(id))
				throw new Exception("id = " + id + " already started");
			Set<String> urlset = new HashSet<String>();
			for (Server s : server_map.values())
				urlset.add(s.getUrl().toLowerCase());
			if (urlset.contains(url.toLowerCase()))
				throw new Exception("url = " + url + " already started");
			server_map.put(id, server);
		}

		try {
			server.connect();
		} catch (Exception e) {
		}
	}

	boolean removeServer(int id) {
		synchronized (mutex) {
			return null != server_map.remove(id);
		}
	}

	Server getServer(int serverId) {
		synchronized (mutex) {
			return server_map.get(serverId);
		}
	}

	List<Server> getServers() {
		synchronized (mutex) {
			return new ArrayList<Server>(server_map.values());
		}
	}

	synchronized void fetchServerBeans(int serverId) throws Exception {
		Server s = getServer(serverId);
		if (null == s)
			throw new Exception(serverId + " not found");

		if (s.getConnector() == null)
			throw new Exception(serverId + " not connected");

		ServerBeans b = new ServerBeans();
		s.connect();
		b.fetch(s.getConnector().getMBeanServerConnection());
		beans = b;
	}

	ServerBeans getServerBeans() {
		return beans;
	}



}
