package com.locojoy.jmonitor.spi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import com.locojoy.db.ApplicationThreadFrameWork;
import com.locojoy.jmonitor.util.DomHelper;

public class Configure {

	private ServerManager servermanager;
	private TableManager tablemanager;
	private ChartManager chartmanager;
	private ChartChainManager chartchainmanager;
	private LocalizeManager localizemanager;

	private String filename;
	private Document document;
	private Element element;

	Configure(String filename) throws Exception {
		this.filename = filename;
		File file = new File(filename);

		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		element = document.getDocumentElement();

		Element servermanager_element = null;
		Element chartmanager_element = null;
		Element chartchainmanager_element = null;
		Element connection_element = null;
		Element localizemanager_element = null;
		Element tablemanager_element = null;
		
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element e = (Element) node;
			String nodeName = e.getNodeName();

			if (nodeName.compareToIgnoreCase("servermanager") == 0)
				servermanager_element = e;
			
			else if (nodeName.compareToIgnoreCase("localizemanager") == 0)
				localizemanager_element = e;

			else if (nodeName.compareToIgnoreCase("tablemanager") == 0)
				tablemanager_element = e;
			
			else if (nodeName.compareToIgnoreCase("chartmanager") == 0)
				chartmanager_element = e;

			else if (nodeName.compareToIgnoreCase("chartchainmanager") == 0)
				chartchainmanager_element = e;

			else if (nodeName.compareToIgnoreCase("connection") == 0)
				connection_element = e;
		}

		String connection = connection_element.getAttribute("name");
		ApplicationThreadFrameWork.createInstance(file, connection, new DbLog());

		tablemanager = new TableManager(this, tablemanager_element);
		servermanager = new ServerManager(this, servermanager_element);
		chartmanager = new ChartManager(this, chartmanager_element);
		chartchainmanager = new ChartChainManager(chartchainmanager_element);
		localizemanager = new LocalizeManager(this, localizemanager_element);
	}

	void start() throws Exception {
		tablemanager.prepareDb();
		servermanager.start();
	}

	void stop() {
		servermanager.stop();
		ApplicationThreadFrameWork.shutdownConnection();
	}

	TableManager getTableManager() {
		return tablemanager;
	}

	ServerManager getServerManager() {
		return servermanager;
	}

	ChartManager getChartManager() {
		return chartmanager;
	}

	ChartChainManager getChartChainManager() {
		return chartchainmanager;
	}

	LocalizeManager getLocalizeManager() {
		return localizemanager;
	}
	
	String getFileName(){
		return filename;
	}
	
	Document getDocument() {
		return document;
	}

	void saveConfig() throws Exception {
		DomHelper.prettySave(document, filename);
	}

	void loadPortConfig(PortConfig pc) throws Exception {
		for (PortConfig.Port p : pc.getPorts()) {
			try {
				servermanager.addServer(p.id, p.serverurl, p.name, p.extra);
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
		}
		saveConfig();
	}

	static private Logger logger = Logger.getLogger(Configure.class.getName());

	static class DbLog extends ApplicationThreadFrameWork.Log {
		@Override
		public void info(String msg) {
			logger.info(msg);
		}
	}
	
	// 原来生成 DAY 的代码有bug，导致生成的不对，这里加个修复方法吧
	// 如果peak类型数据改变了抽样间隔也在这里进行
	void repairDb_DAY(long from) throws Exception{
		tablemanager.repairDb_DAY(from);
	}
	
	void unshardingDb() throws Exception{
		tablemanager.unshardingDb();
	}
	
	public static void main(String[] args) throws Exception {
		String configureFn = args[0];
		String method = args[1];
		
		if (method.equals("repairDay")){
			long fromtime = new SimpleDateFormat("yyyyMMdd").parse(args[2]).getTime();
			new Configure(configureFn).repairDb_DAY(fromtime);
		}else if (method.equals("unshardingDb")){
			new Configure(configureFn).unshardingDb();
		}
		
	}

}
