package com.locojoy.jmonitor.spi;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.locojoy.jmonitor.ChartChain;
import com.locojoy.jmonitor.IChart;
import com.locojoy.jmonitor.Localize;
import com.locojoy.jmonitor.Manager;
import com.locojoy.jmonitor.IServer;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.MultiLine;
import com.locojoy.jmonitor.MultiLine.SepKey;
import com.locojoy.jmonitor.spi.Chart.Line;

public class ManagerImpl implements Manager {

	private Configure configure;
	private PortConfig portconfig;

	@Override
	public void start(String configfn, String portfn) throws Exception {
		configure = new Configure(configfn);
		configure.start();

		portconfig = new PortConfig(portfn);
		configure.loadPortConfig(portconfig);

	}

	@Override
	public void stop() {
		configure.stop();
	}

	@Override
	public List<IServer> getServers() {
		return new ArrayList<IServer>(configure.getServerManager().getServers());
	}

	@Override
	public void addServer(int serverId, String url, String name) throws Exception {
		if (null == url || url.length() == 0 || null == name)
			throw new Exception("url null/empty or name null");
		
		configure.getServerManager().addServer(serverId, url, name);
		portconfig.addPort(serverId, url, name);
		portconfig.saveConfig();
	}

	@Override
	public boolean removeServer(int serverId) throws Exception {
		if (configure.getServerManager().removeServer(serverId) && portconfig.removePort(serverId)){
			portconfig.saveConfig();
			return true;
		}
		return false;
	}

	@Override
	public void fetchServer(int serverid) throws Exception {
		configure.getServerManager().fetchServerBeans(serverid);
	}

	@Override
	public List<ITable> getTables() {
		List<ITable> newList = new ArrayList<ITable>();
		for (ServerBeans.Bean bean : configure.getServerManager().getServerBeans().getBeans().values()) {
			try {
				newList.add(new TableAddable(configure, bean));
			} catch (Exception ignore) {
			}
		}

		List<ITable> list = new ArrayList<ITable>(configure.getTableManager().getTableList());
		Collections.sort(list, new Comparator<ITable>() {

			@Override
			public int compare(ITable o1, ITable o2) {
				return o1.getIdentification().compareTo(o2.getIdentification());
			}

		});
		list.addAll(newList);
		return list;
	}

	@Override
	public void addTable(String xml) throws Exception {
		Element e;
		try{
			e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
		}catch(SAXParseException except){
			throw new Exception(except.getMessage() + ",如果有2个table请选择一个！");
		}
		if (!e.getTagName().equals("table"))
			throw new Exception("tagname not table");

		Table table = new Table(configure.getTableManager(), (Element) configure.getDocument().importNode(e, true));

		configure.getTableManager().addTable(table);

		configure.saveConfig();
	}

	@Override
	public boolean removeTable(String identification) throws Exception {
		if (configure.getTableManager().removeTable(identification)) {
			configure.saveConfig();
			return true;
		}
		return false;
	}

	@Override
	public List<IChart> getCharts() {
		List<IChart> res = new ArrayList<IChart>(configure.getChartManager().getCharts());
		Collections.sort(res, new Comparator<IChart>() {

			@Override
			public int compare(IChart o1, IChart o2) {
				return o1.getChartName().compareTo(o2.getChartName());
			}

		});
		return res;
	}

	@Override
	public void addChart(String xml) throws Exception {
		Element e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
		if (!e.getTagName().equals("chart"))
			throw new Exception("tagname not chart");

		Chart chart = new Chart(configure.getChartManager(), (Element) configure.getDocument().importNode(e, true));
		configure.getChartManager().addChart(chart);
		configure.saveConfig();
	}

	@Override
	public boolean removeChart(String chartName) throws Exception {
		if (configure.getChartManager().removeChart(chartName)) {
			configure.saveConfig();
			return true;
		}
		return false;
	}

	@Override
	public List<ChartChain> getChartChains() {
		List<ChartChain> res = configure.getChartChainManager().getChartChains();

		Collections.sort(res, new Comparator<ChartChain>() {

			@Override
			public int compare(ChartChain o1, ChartChain o2) {
				return o1.getChartChainName().compareTo(o2.getChartChainName());
			}

		});

		return res;
	}

	@Override
	public void addChartChain(String xml) throws Exception {
		Element e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
		if (!e.getTagName().equals("chartchain"))
			throw new Exception("tagname not chartchain");

		ChartChainImpl chain = new ChartChainImpl(configure.getChartChainManager(), (Element) configure.getDocument().importNode(e, true));
		configure.getChartChainManager().addChartChain(chain);
		configure.saveConfig();
	}

	@Override
	public boolean removeChartChain(String chartChainName) throws Exception {
		if (configure.getChartChainManager().removeChartChain(chartChainName)) {
			configure.saveConfig();
			return true;
		}
		return false;
	}

	@Override
	public List<Localize> getLocalizes() {
		List<Localize> res = configure.getLocalizeManager().getLocalizes();
		Collections.sort(res, new Comparator<Localize>() {

			@Override
			public int compare(Localize o1, Localize o2) {
				if (o1.isReadOnly() && !o2.isReadOnly())
					return -1;
				if (!o1.isReadOnly() && o2.isReadOnly())
					return 1;
				
				if (o1.isBaseKey() && !o2.isBaseKey())
					return -1;
				if (!o1.isBaseKey() && o2.isBaseKey())
					return 1;
				
				return o1.getName().compareTo(o2.getName());
			}

		});
		return res;
	}

	@Override
	public void addLocalize(String xml) throws Exception {
		Element e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
		boolean isbasekey = e.getTagName().equals("basekey");
		if (!isbasekey && !e.getTagName().equals("localize"))
			throw new Exception("tagname not localize or basekey");

		if (isbasekey) {
			LocalizeImpl.BaseKey basekey = new LocalizeImpl.BaseKey(configure.getLocalizeManager(), (Element) configure.getDocument().importNode(e, true));
			configure.getLocalizeManager().addBaseKey(basekey);
		} else {
			LocalizeImpl local = new LocalizeImpl(configure.getLocalizeManager(), (Element) configure.getDocument().importNode(e, true));
			configure.getLocalizeManager().addLocalize(local);
		}
		configure.saveConfig();
	}

	@Override
	public boolean removeLocalize(String beanName, boolean isBaseKey) throws Exception {

		if (isBaseKey) {
			if (configure.getLocalizeManager().removeBaseKey(beanName)) {
				configure.saveConfig();
				return true;
			}
		} else {
			if (configure.getLocalizeManager().removeLocalize(beanName)) {
				configure.saveConfig();
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, Map<Long, Long>> queryBean(MultiLine query) throws Exception {
		query.verify();

		Table mbt = configure.getTableManager().getTable(query.getBean());
		if (null == mbt)
			throw new Exception("bean not found " + query.getBean());

		if (null == query.getSepkey()) {
			return mbt.queryValues(query.getValues(), query.getKey(), query.getRanges(), query.isDayline());
		}

		SepKey sepkey = query.getSepkey();
		int sep = sepkey.getSep();
		int min = sepkey.getMin();
		int max = sepkey.getMax();

		TreeMap<Long, Map<Long, Long>> res = mbt.queryValueWithSepKey(query.getValues().get(0), sepkey.getKey(), query.getKey(), query.getRanges(),
				query.isDayline());
		Map<String, Map<Long, Long>> map = new TreeMap<String, Map<Long, Long>>();

		Map<Long, Long> segv = new HashMap<Long, Long>();
		long lasttop = min - 1;
		for (Map.Entry<Long, Map<Long, Long>> e : res.entrySet()) {
			long sk = e.getKey();

			if (sk - lasttop > sep) { // 放前面判断，因为SEPKEY可能会有间隔
				lasttop += sep;
				if (lasttop >= max) {
					map.put(String.valueOf(max), segv);
					break;
				}
				map.put(String.valueOf(lasttop), segv);
				segv = new HashMap<Long, Long>();
			}

			for (Map.Entry<Long, Long> ve : e.getValue().entrySet()) {
				long k = ve.getKey();
				Long old = segv.get(k);
				if (null == old)
					old = 0L;
				segv.put(k, old + ve.getValue());
			}
		}
		if (lasttop < max) {
			lasttop += sep;
			map.put(String.valueOf(Math.min(max, lasttop)), segv);
		}

		return map;
	}

	@Override
	public Map<String, Map<Long, Long>> queryChart(String chartName, Map<String, MultiLine.Range> ranges) throws Exception {
		Chart chart = configure.getChartManager().getChart(chartName);
		if (null == chart)
			throw new Exception(chartName + " not exist");

		Map<String, MultiLine.Range> commonRanges = chart.tryApplyRanges(ranges);
		
		Map<String, Map<Long, Long>> map = new HashMap<String, Map<Long, Long>>();
		for (Map.Entry<String, Line> entry : chart.getLines().entrySet()) {
			String linename = entry.getKey();
			Line line = entry.getValue().clone();
			Map<String, MultiLine.Range> finalRanges = line.applyRanges(commonRanges);
			
			Table mbt = configure.getTableManager().getTable(line.getBean());
			if (null == mbt)
				throw new Exception("bean not found " + line.getBean());
			map.put(linename + "/" + line.getKey() + "/" + line.getBean() + 
					(line.getGraph().isEmpty() ?  "" : "/" + line.getGraph()) + (!line.getType().isEmpty() ? "/"+line.getType() : "") , 
					mbt.queryValue(line.getValue(), line.getKey(), finalRanges));
		}

		return map;
	}

	@Override
	public void testPortForwarderAlive() throws Exception {
		configure.getServerManager().testPfAlive();
	}

	@Override
	public TableData queryBeanRaw(String beanName, int zid, long timelow, long timeup, boolean dayline) throws Exception {
		Table table = configure.getTableManager().getTable(beanName);
		if (null == table)
			throw new Exception("bean not found " + beanName);

		return table.queryRaw(zid, timelow, timeup, dayline);
	}
	
	public static void main(String[] argv) throws Exception{
		String[] seps = "AngleInterruptCount".split("\\.");
		System.out.println(seps.length + "," + seps[0]);
		String xml = "<RO_test></RO_test>";
		Element e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))).getDocumentElement();
		System.out.println(e);
	}

}
