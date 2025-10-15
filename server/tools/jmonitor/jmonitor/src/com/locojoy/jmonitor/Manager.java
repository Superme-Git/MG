package com.locojoy.jmonitor;

import java.util.List;
import java.util.Map;

public interface Manager {
	
	//生命周期管理
	public void start(String configfilename, String portfilename) throws Exception;
	
	public void stop();

	// Server列表
	public List<IServer> getServers();
	
	public void addServer(int serverId, String url, String name) throws Exception;
	
	public boolean removeServer(int serverId) throws Exception;
	
	// Table列表
	public List<ITable> getTables();

	public void addTable(String xml) throws Exception;

	public boolean removeTable(String identification) throws Exception;

	// Chart列表
	public List<IChart> getCharts();
	
	public void addChart(String xml) throws Exception;
	
	public boolean removeChart(String chartName) throws Exception;
	
	// ChartChain列表
	public List<ChartChain> getChartChains();

	public void addChartChain(String xml) throws Exception;

	public boolean removeChartChain(String chartChainName) throws Exception;

	// Localize列表
	public List<Localize> getLocalizes();

	public void addLocalize(String xml) throws Exception;

	public boolean removeLocalize(String beanName, boolean isBaseKey) throws Exception;

	//////////////////////////////////////////////
	
	// 取这个服务器的MBeanInfo，这个影响getTables
	public void fetchServer(int serverId) throws Exception;
	
	// Map.Key为 <LineName>/<LineKey>
	public Map<String, Map<Long, Long>> queryChart(String chartName, Map<String, MultiLine.Range> ranges) throws Exception;
	
	// Map.Key为Value 或 SepKey.UpLimit
	public Map<String, Map<Long, Long>> queryBean(MultiLine query) throws Exception;

	
	public static class TableData{
		public List<String> head;
		public List<List<Long>> data;
	}
	
	// zid为-1表示SUMZID, timelow,timeup可为0,可为负数
	public TableData queryBeanRaw(String beanName, int zid, long timelow, long timeup, boolean dayline) throws Exception;
	
	public void testPortForwarderAlive() throws Exception;
	

}
