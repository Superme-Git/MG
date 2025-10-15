package com.locojoy.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.locojoy.base.Octets;

public class Config {
	private static Logger logger = Logger.getLogger(Config.class);

	public static final String OCTETS_CHARSET_ANSI = "ISO-8859-1";
	public static final String OCTETS_CHARSET_UNICODE = "UTF-16LE";
	
	private int defaultplattype;
	private int jmxrmiport;
	private int jmxserverport;
	private String httpserverip;
	private int httpserverport;
	private Octets xiooutputsecurity;
	private Octets xioinputsecurity;
	private boolean isredisconnect;
	private String redisserverip;
	private int redisserverport;
	private boolean allowrobot;
	private String allowrobotname;
	
	private Map<String, Integer> chargeRefunds = new HashMap<String, Integer>(); //封测充值返还数据
	private Map<String, Integer> charges = new HashMap<String, Integer>(); //封测充值数据
	private java.util.List<String> refundsList = new java.util.LinkedList<String>();
	
	@Override
	public String toString() {
		return "(defaultplattype = " + defaultplattype + 
				" jmxrmiport = " + jmxrmiport + " jmxserverport = " + jmxserverport + 
				" httpserverip = " + httpserverip + " httpserverport = " + httpserverport + 
				" xiooutputsecurity = " + xiooutputsecurity.getString(OCTETS_CHARSET_ANSI) + " xioinputsecurity = " + xioinputsecurity.getString(OCTETS_CHARSET_ANSI) +
				" redisserverip = " + redisserverip + " redisserverport = " + redisserverport +
				")"; 
	}

	private static Config instance;
	private Config() throws Exception {
		load("sdk.config.xml");
		initChargeRefundInfo();
	}
	static public void Init() throws Exception {
		instance = new Config();
	}
	static public Config getInstance() {
		return instance;
	}
	
	public int getDefaultPlatType() {
		return defaultplattype;
	}
	
	public String getHttpServerIP() {
		return httpserverip;
	}
	public int getHttpServerPort() {
		return httpserverport;
	}
	
	public int getJmxRmiPort() {
		return jmxrmiport;
	}
	public int getJmxServerPort() {
		return jmxserverport;
	}
	
	public boolean isRedisConnect() {
		return isredisconnect;
	}
	
	public String getRedisServerIP() {
		return redisserverip;
	}
	
	public int getRedisServerPort() {
		return redisserverport;
	}
	
	public boolean isAllowRobot() {
		return allowrobot;
	}
	
	public String getAllowRobotName() {
		return allowrobotname;
	}
	
	public Integer getChargeRefund(String username) {
		return chargeRefunds.get(username);
	}
	
	public Integer getCharge(String username) {
		return charges.get(username);
	}
	
	private void initChargeRefundInfo() {
		try {
			for(String refundsFile : refundsList){
				File outFile = new File(refundsFile);
				if(!outFile.exists())
					continue;
				Scanner in = new Scanner(outFile);
				while(in.hasNextLine()) {
					String str = in.nextLine();
					String[] items = str.split(",");
					String username = items[0];//帐号
					int charge = Integer.parseInt(items[1]);//充值数据,人民币
					int refund = Integer.parseInt(items[2]);//返还数据,符石
					int oldRefund = chargeRefunds.get(username) == null ? 0 : chargeRefunds.get(username);
					int oldCharge = charges.get(username) == null ? 0 : charges.get(username);
					chargeRefunds.put(username, refund + oldRefund);
					charges.put(username, charge + oldCharge);
				}
				in.close();
			}
			logger.debug("Config.initChargeRefundInfo chargeRefunds size:" + chargeRefunds.size());
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	private void load(String filename) throws SAXException, IOException, ParserConfigurationException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
		final Element root = doc.getDocumentElement();
		defaultplattype = Integer.parseInt(root.getAttribute("default.plat.type"));
		
		jmxrmiport = Integer.parseInt(root.getAttribute("jmx.rmiport"));
		jmxserverport = Integer.parseInt(root.getAttribute("jmx.serverport"));
		
		httpserverip = root.getAttribute("http.server.ip");
		httpserverport = Integer.parseInt(root.getAttribute("http.server.port"));
		
		xiooutputsecurity = Octets.wrap(root.getAttribute("mkio.output.security"), OCTETS_CHARSET_ANSI);
		xioinputsecurity = Octets.wrap(root.getAttribute("mkio.input.security"), OCTETS_CHARSET_ANSI);
		
		isredisconnect = Boolean.parseBoolean(root.getAttribute("redis.isuse"));
		redisserverip = root.getAttribute("redis.server.ip");
		redisserverport = Integer.parseInt(root.getAttribute("redis.server.port"));
		
		allowrobot = Boolean.parseBoolean(root.getAttribute("robot.allow"));
		allowrobotname = root.getAttribute("robot.allowname");
		
		String refundsListStr = root.getAttribute("refunds.list");
		String[] refundsListArr = refundsListStr.split(",");
		refundsList.clear();
		for(int i = 0; i < refundsListArr.length; ++i){
			refundsList.add(refundsListArr[i]);
			mkdb.Trace.debug("Pre load refunds list:" + refundsListArr[i]);
		}
		
		if(mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("Config.load(" + filename + ") " +  this); 
		
		final NodeList subs = root.getChildNodes(); 
		for(int i = 0; i < subs.getLength(); i ++ ) {
			final Node node = subs.item(i);
			if(Node.ELEMENT_NODE == node.getNodeType())
				parseElement((Element)node);
		}
		if(allowrobot)
			logger.warn("=====Start allow robot name startwith '" + allowrobotname + "' login in Config!Maybe not security!=====");
	}
	
	private void parseElement(Element ele) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

		if(0 != ele.getNodeName().compareToIgnoreCase("plat"))
			return;
	
		final String classname = ele.getAttribute("class");
		final int plattype = Integer.valueOf(ele.getAttribute("type"));
		if(null == classname || classname.isEmpty())
			return;

		if(mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug("Config.load plat classname = " + classname + " plattype = " + plattype);
		
		Class<?> cls = getClass().getClassLoader().loadClass(classname);
		PlatProcess process = (PlatProcess)cls.newInstance();
		process.init(ele);
		PlatManager.insertPlatProcess(plattype, process);
	}
	
	public Octets getXioOutputSecurity() {
		return xiooutputsecurity;
	}
	public Octets getXioInputSecurity() {
		return xioinputsecurity;
	}
}
