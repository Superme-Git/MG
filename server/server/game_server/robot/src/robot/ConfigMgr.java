package robot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import mkdb.Mkdb;
import mkdb.MkdbConf;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import robot.bingfeng.BingFengUtils;
import robot.manager.MissionMgr;
import robot.manager.NPCMgr;
import robot.protocol.UniqNameUtils;
import robot.zhuogui.ZhuoGuiUtils;

public class ConfigMgr {

	public static java.util.Map<String, Object> beannames = new java.util.concurrent.ConcurrentHashMap<String, Object>();

	public static final java.util.Map<String, Properties> propnames = new java.util.concurrent.ConcurrentHashMap<String, Properties>();

	private static ConfigMgr instance = new ConfigMgr();

	// 技能配置文件路径
	public final static String SKILLS_CONFIG_DIR = "config/mt3_skills.xml";

	// 一般配置文件存放路径
	public final static String TASK_CONFIG_PATH = "config/auto";

	// 一般配置文件存放路径
	public final static String PRO_CONFIG_PATH = "config/properties";

	// 职业对应的技能
	private Map<Integer, List<Integer>> playerType2Skills = new HashMap<Integer, List<Integer>>();

	private Mkdb mkdb;

	// 是否开启每天活跃处理
	boolean isStartHY = false;

	// 默认是普通，1点卡
	public int payserverType = 0;

	// 空间地址
	public String weiBoAddress = "";

	// 服务器id
	public String serverid;

	private ConfigMgr() {
	}

	public static ConfigMgr getInstance() {
		return instance;
	}

	static void init(String xmldir, Unmarshaller unmarshaller) {
		for (final java.io.File f : new java.io.File(xmldir).listFiles()) {
			if (!f.getName().endsWith(".xml"))
				continue;
			Object o;
			try {
				o = unmarshaller.unmarshal(new FileInputStream(f));
			} catch (final Exception ex) {
				System.out.println("载入" + f.getAbsolutePath() + "失败");
				continue;
			}
			final String beanname = f.getName().substring(0, f.getName().length() - new String(".xml").length());
			System.out.println("register bean name=" + beanname);
			beannames.put(beanname, o);
			if (!(o instanceof java.util.Map<?, ?>))
				continue;
		}
	}

	public Properties getPropConf(String name) {
		return propnames.get(name);
	}

	public static String getStringValue(Properties prop, String key) {
		return prop.getProperty(key);
	}

	static void init(String propdir) {
		Properties prop = null;
		FileInputStream fis = null;
		File file = new java.io.File(propdir);
		if (!file.exists()) {
			return;
		}
		for (final java.io.File f : file.listFiles()) {
			if (!f.getName().endsWith(".properties"))
				continue;
			prop = new Properties();
			try {
				fis = new FileInputStream(f);
				prop.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			final String propname = f.getName().substring(0, f.getName().indexOf(".properties"));
			propnames.put(propname, prop);
		}
	}

	/**
	 * 返回值(TreeMap可以保持原有类型)
	 */
	public <T> java.util.TreeMap<Integer, T> getConfig(Class<T> t) {
		Object o = beannames.get(t.getName());
		if (o == null)
			return null;
		try {
			@SuppressWarnings("unchecked")
			java.util.TreeMap<Integer, T> result = (java.util.TreeMap<Integer, T>) o;
			return result;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}

	void init() throws SAXException, IOException, ParserConfigurationException {
		loadSkillsConfig();
		init(TASK_CONFIG_PATH, new XStreamUnmarshaller());
		init(PRO_CONFIG_PATH);
		MissionMgr.getInstance();
		NPCMgr.getInstance().init();
		BingFengUtils.getInstance().init();
		ZhuoGuiUtils.getInstance().init();
		proInit();
	}

	/**
	 * 属性文件内容初始化
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月30日 下午8:53:00
	 * @version 1.0
	 */
	private void proInit() {
		Properties sysProp = ConfigMgr.getInstance().getPropConf("sys");
		if (sysProp != null) {
			isStartHY = FireProp.getBooleanValue(sysProp, "isStartHY");
			payserverType = FireProp.getIntValue(sysProp, "sys.payserver.type");
			weiBoAddress = FireProp.getStringValue(sysProp, "sys.weibo.address");
			serverid = FireProp.getStringValue(sysProp, "serverid");
		}
	}

	/**
	 * 初始化这个文件中的唯一命名
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月16日 上午11:31:08
	 * @version 1.0
	 */
	public void initMkdb() {
		File mkdbFile = new File("mkdb");
		File mbackup = new File("mbackup");
		if (!mkdbFile.exists()) {
			mkdbFile.mkdir();
		}
		if (!mbackup.exists()) {
			mbackup.mkdir();
		}
		new File("").mkdir();
		final MkdbConf conf = new mkdb.MkdbConf("gsx.mkdb.xml");
		mkdb = Mkdb.getInstance();
		mkdb.setConf(conf);
		UniqNameUtils.initialize();
	}

	/**
	 * 技能信息
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private void loadSkillsConfig() throws SAXException, IOException, ParserConfigurationException {
		File f = new File(SKILLS_CONFIG_DIR);
		if (!f.exists())
			throw new IllegalArgumentException("技能配置文件不存在");
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
		if (doc == null)
			throw new IllegalArgumentException("技能配置解析失败");
		Element eleRoot = doc.getDocumentElement();
		NodeList nodesPlayerType = eleRoot.getElementsByTagName("school");
		for (int i = 0; i < nodesPlayerType.getLength(); i++) {
			Node playerTypeNode = nodesPlayerType.item(i);
			Node playerTypeId = playerTypeNode.getAttributes().getNamedItem("id");
			Integer id = Integer.valueOf(playerTypeId.getNodeValue());
			List<Integer> skills = new LinkedList<Integer>();
			NodeList nodesSkills = playerTypeNode.getChildNodes();
			for (int j = 0; j < nodesSkills.getLength(); j++) {
				Node skillnode = nodesSkills.item(j);
				if (!(skillnode instanceof Element))
					continue;
				Node skillidnode = skillnode.getAttributes().getNamedItem("id");
				Integer skillid = Integer.valueOf(skillidnode.getNodeValue());
				skills.add(skillid);
			}
			playerType2Skills.put(id, skills);
		}
	}

	public int randSkillByPlayerType(int schoolid) {
		List<Integer> skills = playerType2Skills.get(schoolid);
		if (skills == null || skills.isEmpty())
			return 0;
		int index = Misc.getRandomBetween(0, skills.size());
		if (index == skills.size())
			return 0;
		return skills.get(index);
	}
}
