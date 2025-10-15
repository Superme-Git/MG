package fire.pb.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import fire.pb.mbean.Module;

import org.apache.log4j.Logger;

public class ConfigManager implements ModuleInterface{

	private static Logger logger = Logger.getLogger("SYSTEM");
	private final static int DEFAULT_RMI_PORT = 20981;
	private static ConfigManager instance = null;
	private static int zoneid = 2662;
	private static int rmiport = DEFAULT_RMI_PORT;
	private static boolean USEMYSQL = false;
	private static String GAMEKEY = "";
	private static int GAMEID = 0;
	private static int CALLBACK_PORT = 8081;
	private static boolean CHARGE_DIRECT = false;
	public static String CONFIG_PATH = "gamedata/xml/auto";
	public static String PROPERTY_PATH = "properties";
	public String serverArea ;
	private boolean needActive = false;
	
	private static boolean isCrossServer = false;
	
	public static int initpoolsize = 8;
	public static int maxpoolsize = 20;
	public static int maxqueuesize = 1000;
	
	public static int CHARGEENABLE = 1;
	public static boolean FUSHI_2_DAYPAY = false;   //是否打开符石到点卡的转换
	public static boolean OPEN_DY = true; //是否打开订阅
	public static boolean isCanCreateRole = true; // 是否可以创建角色
	
	public static boolean isChargeLimit = false; //是否打开小额充值次数限制,默认关闭
	
	public static boolean isOpenRecruit = true; //是否打开招募 ,默认打开
	
	private int loginLimitType = 0;
	private int errorType;
	private List<Integer> userIDList = new LinkedList<Integer>();
	
	private final java.util.Map<String,Object> beannames = new java.util.concurrent.ConcurrentHashMap<String,Object>();
	private final java.util.Map<String,Properties> propnames = new java.util.concurrent.ConcurrentHashMap<String,Properties>();
	
	public synchronized static ConfigManager getInstance() {
		return instance;
	}


    public Properties getPropConf(String name){
    	return propnames.get(name);
    }
    
    /**
     * 返回值使用了TreeMap而非接口Map是为了保持原有的类型，
     * 但会关联thoughtworks版本实现的更改，如果要用新的thoughtworks版本，注意其Map是否有新的实现
     * @param <T>
     * @param t
     * @return
     */
	public <T> java.util.TreeMap<Integer,T> getConf(Class<T> t)
    {
    	Object o = beannames.get(t.getName());
    	if(o == null)
    		return null;
    	try
    	{
    		@SuppressWarnings("unchecked")
    		java.util.TreeMap<Integer,T> result = (java.util.TreeMap<Integer,T>)o;
    		return result;
    	}
    	catch(ClassCastException e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
	
	
	/**
	 * 根据配置id取得数据
	 * @author cn
	 */
	public <T> T getConfigById(Class<T> t,int configId){
		Map<Integer,T> map=getConf(t);
		if (map==null||map.isEmpty()) {
			logger.warn(t.getCanonicalName() +" config is null");
			return null;
		}
		return map.get(configId);
	}
    
	static void usage() {
		System.out.println("Usage: java -jar gsxdb.jar [options]");
		System.out.println("    -rmiport    rmi port");
		System.out.println("    -zoneid      zone id");
		System.out.println("    -usemysql    1");
		System.exit(0);
	}

	private static String ARGS(String args[], int i) {
		if (i < args.length) return args[i];
		usage();
		return null;
	}
	
	public int getLoginLimitType() {
		return loginLimitType;
	}

	public void setLoginLimitType(int loginLimitType) {
		this.loginLimitType = loginLimitType;
	}
	
	public List<Integer> getUserIDList() {
		return userIDList;
	}
	
	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	
	
	/**
	 * 理论来说，这个init方法是可以调用多次的
	 */
	static void init(String xmldir,Unmarshaller unmarshaller,String propdir) {
		/*synchronized (ConfigManager.class) {
			instance = null;
		} 注释掉的原因是，本方法执行结束前，还是可以getInstance的，只有在最后才把instance替换为新的*/

		final ConfigManager cm=new ConfigManager();
		//初始化系统参数
		rmiport = DEFAULT_RMI_PORT;
		String[] args = Gs.args;
		for (int i = 0; i < args.length; ++i)
		{
			if (args[i].equals("-rmiport"))
				rmiport = Integer.valueOf(ARGS(args, ++i));
			else if (args[i].equals("-zoneid"))
				zoneid = Integer.valueOf(ARGS(args, ++i));
			else if (args[i].equals("-usemysql"))
				USEMYSQL = Integer.valueOf(ARGS(args, ++i)) > 0 ? true : false;
			else if (DEFAULT_RMI_PORT == rmiport)
				rmiport = Integer.valueOf(args[i]); // 兼容
			else
				usage();
		}
		//cm.unmarshaller = unmarshaller;
		cm.beannames.clear();

		for (final java.io.File f : new java.io.File(xmldir).listFiles()) {
			if (!f.getName().endsWith(".xml"))
				continue;
			Object o;
			try {
				o = unmarshaller.unmarshal(new FileInputStream(f));
			} catch (final Exception ex) {
				logger.error("载入" + f.getAbsolutePath() + "失败", ex);
				continue;
			}
			final String beanname = f.getName().substring(0,
					f.getName().length() - new String(".xml").length());			
			logger.info("register bean name=" + beanname);
			cm.beannames.put(beanname, o);
			if(!(o instanceof java.util.Map<?,?>)) continue;			
		}
		
		mytools.ConvMain.doCheck(cm.beannames);
		
		Properties prop=null;
		FileInputStream fis=null;
		for (final java.io.File f : new java.io.File(propdir).listFiles()){
			if (!f.getName().endsWith(".properties"))
				continue;
			prop=new Properties();
			try {
				fis=new FileInputStream(f);
				prop.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
				logger.error(f.getAbsolutePath()+"not found", e);
				continue;
			} catch (IOException e) {
				logger.error(f.getAbsolutePath()+"load error", e);
				continue;
			}
			final String propname=f.getName().substring(0, f.getName().indexOf(".properties"));
			cm.propnames.put(propname, prop);
			logger.info("register properties=" + propname);
		}
		synchronized (ConfigManager.class) {
			instance = cm;
		}
		Integer integer = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.needActive"));
		instance.setNeedActive(integer==0?false:true);
		instance.serverArea = instance.getServerAreaString();
		
		GAMEKEY = instance.propnames.get("sys").getProperty("sys.charge.gamekey");
		GAMEID = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.charge.gameid"));
		CALLBACK_PORT = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.charge.callback"));
		CHARGE_DIRECT = Boolean.parseBoolean(instance.propnames.get("sys").getProperty("sys.charge.direct"));
		initpoolsize = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.threadpool.initpoolsize"));
		maxpoolsize = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.threadpool.maxpoolsize"));
		maxqueuesize = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.threadpool.maxqueuesize"));
		CHARGEENABLE = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.charge.enable"));
		FUSHI_2_DAYPAY = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.fushi.daypay")) == 1 ? true : false;
		isCrossServer = Integer.parseInt(instance.propnames.get("sys").getProperty("sys.cross")) == 1 ? true : false;
	}
	public static boolean getIsCrossServer() {
		return isCrossServer;
	}
	
	public static boolean getChargeDirect() {
		return CHARGE_DIRECT;
	}
	
	public static String getGameKey() {
		return GAMEKEY;
	}
	
	public static int getGameId() {
		return GAMEID;
	}
	
	public static int getCallBackPort() {
		return CALLBACK_PORT;
	}

	private String getServerAreaString() {	
		return ServerArea.CHINA;
	}

	@Override
	public void exit() {
		
	}

	@Override
	public void init() throws Exception {
		init(CONFIG_PATH,new XStreamUnmarshaller(),PROPERTY_PATH);	
	}

	public static int getGsZoneId()
	{
		return zoneid;
	}
	
	public static int getRmiPort()
	{
		return rmiport;
	}
	
	public static boolean getUseMysql()
	{
		return USEMYSQL;
	}
	
	/**
	 * 以下数据将来要挪到XDB中去，xdb可以更好的支持回滚等操作
	 */
	private static AtomicInteger onlineRoleNum = new AtomicInteger(0);//当前在线人数
	private static AtomicInteger dayMaxRoleNum = new AtomicInteger(0);//今天最大在线人数
	
	
	public static void setOnlineRoleNum(int onlinenum)
	{
		onlineRoleNum.set(onlinenum);
		if(onlinenum > dayMaxRoleNum.get() )
			dayMaxRoleNum.set(onlinenum);
	}
	
	public static int getOnlineRoleNum()
	{
		return onlineRoleNum.get();
	}
	public static int getDayMaxRoleNum()
	{
		return dayMaxRoleNum.get();
	}
	public static void cleanDayMaxRoleNum()
	{
		dayMaxRoleNum.set(0);
	}
	
	public void putConfig(String beanname, Object o)
	{
		beannames.put(beanname, o);
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		java.io.File hotfixcfg = new java.io.File(ConfigManager.CONFIG_PATH +"/fire.pb.main.HotfixConfig.xml");
		if(!hotfixcfg.exists())
		{
			return new ReloadResult(false,"Hotfix file not exists. \n");
		}
		if (!hotfixcfg.getName().endsWith(".xml"))
		{
			return new ReloadResult(false,"Hotfix file is not valid. \n");
		}
		ReloadResult result = new ReloadResult(true);
		List<File> files = new LinkedList<File>();
		files.add(hotfixcfg);
		result.appendResult(reloadConfigFiles(files));
		if(!result.isSuccess()){
			return result;
		}
		
		Map<Integer,HotfixConfig> hotfixs = ConfigManager.getInstance().getConf(HotfixConfig.class);
		
		//reload xml文件
		files.clear();
		for(HotfixConfig hotfix : hotfixs.values())
		{
			if(hotfix.type == 1){//xml文件
				files.add(new java.io.File(ConfigManager.CONFIG_PATH +"/" + hotfix.name));
			}
		}
		
		result.appendResult(reloadConfigFiles(files));
		if(!result.isSuccess()){
			return result;
		}
		//reload properties文件
		files.clear();
		for(HotfixConfig hotfix : hotfixs.values())
		{
			if(hotfix.type == 3){//properties文件
				files.add(new java.io.File(ConfigManager.PROPERTY_PATH +"/" + hotfix.name));
			}
		}
		
		result.appendResult(reloadPropertyFiles(files));
		if(!result.isSuccess())
			return result;
		
		Module.logger.info("load files success. \n");
		return result;
	}
	
	private ReloadResult reloadConfigFiles(List<File> cfgfiles)
	{
		Unmarshaller unmarshaller = new XStreamUnmarshaller();
		Map<String,Object> cfgs = new HashMap<String, Object>();
		ReloadResult result = new ReloadResult(true);
		for (final java.io.File f : cfgfiles) {
			if(!f.exists())
			{
				String msg = f.getAbsolutePath() + " not exists. \n";
				Module.logger.error(msg);
				return new ReloadResult(false,msg);
			}
			if (!f.getName().endsWith(".xml"))
			{
				String msg = f.getAbsolutePath() + " is not end with xml \n";
				Module.logger.error(msg);
				return new ReloadResult(false);
			}
			Object o;
			try {
				o = unmarshaller.unmarshal(new FileInputStream(f));
			} catch (final Exception ex) {
				String msg = "load " + f.getAbsolutePath() + " fail. \n";
				Module.logger.error(msg, ex);
				return new ReloadResult(false,msg);
			}
			final String beanname = f.getName().substring(0,f.getName().length() - new String(".xml").length());
			String msg = "File: " + f.getName() + " reload success. \n";
			result.appendMsg(msg);
			Module.logger.info(msg);
			cfgs.put(beanname, o);
		}
		if(!mytools.ConvMain.doCheck(cfgs)){
			return new ReloadResult(false,"load success, but check error.");
		}
		//全加载成功后，再替换
		for(Map.Entry<String,Object> entry : cfgs.entrySet()){
			beannames.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}
	
	private ReloadResult reloadPropertyFiles(List<File> propfiles)
	{
		Map<String,Properties> props= new HashMap<String, Properties>();
		ReloadResult result = new ReloadResult(true);
		for (final java.io.File f : propfiles) {
			if(!f.exists())
			{
				String msg = f.getAbsolutePath() + " not exists. \n";
				Module.logger.error(msg);
				return new ReloadResult(false,msg);
			}
			if (!f.getName().endsWith(".properties"))
			{
				String msg = f.getAbsolutePath() + " is not end with properties \n";
				Module.logger.error(msg);
				return new ReloadResult(false);
			}
			
			Properties prop = new Properties();
			try {
				FileInputStream fis=new FileInputStream(f);
				prop.load(fis);
				fis.close();
			} catch (final Exception ex) {
				String msg = "load " + f.getAbsolutePath() + " fail. \n";
				Module.logger.error(msg, ex);
				return new ReloadResult(false,msg);
			}
			final String propname=f.getName().substring(0, f.getName().indexOf(".properties"));
			String msg = "File: " + f.getAbsolutePath() + " reload success. \n";
			result.appendMsg(msg);
			Module.logger.info(msg);
			props.put(propname, prop);
		}
		
		//全加载成功后，再替换
		for(Map.Entry<String,Properties> entry : props.entrySet()){
			propnames.put(entry.getKey(), entry.getValue());
		}
		
		return result;
	}
	
	
	public boolean isNeedActive() {
	
		return needActive;
	}

	
	public void setNeedActive(boolean needActive) {
	
		this.needActive = needActive;
	}

	public boolean isChinaServer(){
		return 2300<=zoneid&&zoneid<=3000;
	}
}
