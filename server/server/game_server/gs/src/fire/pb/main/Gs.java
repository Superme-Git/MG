package fire.pb.main;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import mkdb.util.JMXServer;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.log4j.Logger;

import com.sun.management.OperatingSystemMXBean;

import fire.TestSignal;
import fire.pb.PropConf;
import fire.pb.callback.SocketServer;
import fire.pb.http.LocalHttpSession;
import fire.pb.mission.activelist.RoleLivenessManager;
import fire.pb.state.PRoleOffline;
import fire.pb.util.FireProp;
import fire.script.ParamMapBindThreadManager;
import fire.script.ScriptEngineBindThreadManager;
import gnet.link.Role;

public class Gs {

	private static Logger logger = Logger.getLogger("SYSTEM");

	private static GsInfo info = new GsInfo();

	public static boolean isDebug() {
		return config.CompileArg.isdebug;
	}

	public static AtomicBoolean isShutDown = new AtomicBoolean(false);

	private static javax.management.MBeanServer mbs = java.lang.management.ManagementFactory.getPlatformMBeanServer();

	private static JMXServer jmxserver;

	public static int platType;

	public static String serverid = "0"; // 先初始化为0,正常运营时会通过deliver设置正确的serverid

	public static boolean isYingyongbao = false;

	private static LocalHttpSession localHttpService;

	private static ExecutorService exes = null; // 线程池

	public static ExecutorService getExecService() {
		return exes;
	}

	private static CloseableHttpAsyncClient httpclient;
	public static CloseableHttpAsyncClient getHttpClient() {
		if (httpclient == null) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(60000)
					.setConnectTimeout(60000)
					.build();
			
			int maxConnTotal = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.plat.yyb.conntotal");
			int maxConnPerRoute = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.plat.yyb.connperroute");
			
			httpclient = HttpAsyncClients.custom()
					.setDefaultRequestConfig(requestConfig)
					.setMaxConnTotal(maxConnTotal).setMaxConnPerRoute(maxConnPerRoute)
					.build();
		}
		return httpclient;
	}

	static void registerMbean(Object obj, String name) throws javax.management.NotCompliantMBeanException,
			javax.management.MBeanRegistrationException, javax.management.InstanceAlreadyExistsException,
			javax.management.MalformedObjectNameException {
		mbs.registerMBean(obj, new javax.management.ObjectName("bean:name=" + name));
	}

	public static long getStartTime() {
		return info.getStartTime();
	}

	public static void init() throws Exception {
		mainThreadid = Thread.currentThread().getId();
		// PropertyConfigurator.configure("log4j.properties");
		ConfigManager.init("gamedata/xml/auto", new XStreamUnmarshaller(), "properties");
		Gs.serverid = String.valueOf(fire.pb.PropConf.ServerId.ServerId);

		fire.script.JsFunManager.InitFunMap();
		ModuleManager.init();
		info.onServerStart();
		jmxserver = new JMXServer(ConfigManager.getRmiPort(), ConfigManager.getRmiPort() + 2000 + ConfigManager.getGsZoneId(),
				null, null, null);
		jmxserver.start();
		getHttpClient();
		try {
			httpclient.start();
			localHttpService = new LocalHttpSession(PropConf.Sys.HTTP_PORT);
			localHttpService.start();
		} catch (Throwable e) {
			logger.error("start http server failed", e);
		}
		if (ConfigManager.getChargeDirect()) {
			new Thread(new SocketServer("0.0.0.0", ConfigManager.getCallBackPort()), "ChargeCBServer").start();
			logger.info("Charge Callback Server Thread Start!Listen at 0.0.0.0:" + ConfigManager.getCallBackPort());
		}
		exes = new ThreadPoolExecutor(ConfigManager.initpoolsize, ConfigManager.maxpoolsize, 30, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(ConfigManager.maxqueuesize), new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "tpool-" + r.hashCode());
					}
				}, new RejectedExecHandlerImpl());
		logger.info("ThreadPool init!initsize:" + ConfigManager.initpoolsize + ",maxsize:" + ConfigManager.maxpoolsize
				+ ",queue:" + ConfigManager.maxqueuesize);
	}

	public static void docleanup() {
		ModuleManager.getInstance().exit();
	}

	// 注意：在main函数执行之前或返回之后不得调用！
	public static boolean isInMainThread() {
		return Thread.currentThread().getId() == mainThreadid;
	}

	private static long mainThreadid = -1;

	public static String[] args;

	public static void main(String[] args) throws Exception {
		logger.info("SERVER STARTING...");
		Gs.args = args;
		init();
		try {
			TestSignal.getInstance();// 加载linux监听关服
			// 必须在所有模块都初始化完成之后，再开启网络
			mkio.Engine.getInstance().open();
			// 脚本引擎初始化,放在网络开启之前会影响GS网络打不开的情况。
			// 怀疑可能是初始化未完毕
			if (PropConf.Battle.DEBUG_FORMULA_MODULE == 1)
				ScriptEngineBindThreadManager.initialization();
			else
				ParamMapBindThreadManager.initialization();
			final mkdb.util.UniqNameConf unc = mkdb.Mkdb.getInstance().getConf().getUniqNameConf();
			logger.info("SERVER OPENED! Current Local Id:" + unc.getLocalId());
			logger.info("Config -usemysql:" + ConfigManager.getUseMysql());
			logger.info("是否和合过服的服务器 is merger server = " +isMergeServer());
			// printMemoryLogThread();//输出日志
			roleNumLogThread();// 输出在线统计人数日志
			final Stopper stopper = new Stopper();
			registerMbean(stopper, "stopper");
			stopper.doWait();
			logger.info("SERVER SHUTDOWN...");
			if (!isShutDown.get()) {
				// 不再接受任何的协议
				isShutDown.set(true);
				stopLocalHttpServer();
				httpclient.close();
				onShutdown();
				// 日志关闭，把缓存的日志写文件
				org.apache.logging.log4j.LogManager.shutdown();
			} else {
				logger.info("Exception:shutdowing   请等待");
			}
			logger.info("BYE");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(1);
		}
	}

	private static void onShutdown() throws Exception {
		// 关服时，需要有顺序的处理：
		// 1.踢所有人下线
		List<Role> roles = gnet.link.Onlines.getInstance().getSafeRoles();
		for (Role r : roles) {
			try {
				new fire.pb.state.PRoleOffline(r.getRoleid(), PRoleOffline.TYPE_LINK_BROKEN).submit().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gnet.link.Onlines.getInstance().kick(r.getRoleid(), fire.pb.KickErrConst.ERR_SERVER_SHUTDOWN);
		}
		// 2.停止战斗
		fire.pb.battle.Module.serverShutdown();
		// 3.转换状态为UnEntryState（结束所有人的下线保护状态）
		fire.pb.state.StateManager.serverShutdown();
		// 4.让link停止监听。不接受新的登录请求，现有的依然处理,之所以挪到这里来是因为如果放在前面的话,因为要踢所有人下线,link又会被trigger
		// listen
		final gnet.link.LinkServerControl linkcontrol = new gnet.link.LinkServerControl();
		linkcontrol.ptype = gnet.link.LinkServerControl.E_STOP_LISTEN;
		for (final gnet.link.Link link : gnet.link.Onlines.getInstance().links()) {
			linkcontrol.send(link.getXio());
		}
		// //因为结婚系统的信息时内存管理，所以需要在关服务器的时候做一些清理
		RoleLivenessManager.serverShutdown();
		docleanup();// 执行不用按顺序处理的模块退出
		stopJMXServer();
	}

	private static void stopJMXServer() {
		try {
			Stopper.shutdownCompletedLock.lockInterruptibly();
		} catch (final InterruptedException ex) {
			ex.printStackTrace();
			return;
		}
		Stopper.shutdownCompleted.signalAll();
		Stopper.shutdownCompletedLock.unlock();
		jmxserver.stop();
		logger.info("JMX SERVER STOPED");
	}

	private static void stopLocalHttpServer() {
		localHttpService.stop();
		logger.info("LOCALHTTP SERVER STOPED");
	}

	/**
	 * 输出当前内存日志信息
	 */
	private static void printMemoryLogThread() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					Runtime runtime = Runtime.getRuntime();
					long free = runtime.freeMemory();
					long max = runtime.maxMemory();
					long total = runtime.totalMemory();
					long useable = max + total - free;
					logger.info("JMX最大内存" + max / 1024 / 1024 + "M\t已分配内存" + total / 1024 / 1024 + "M\t已分配内存中的空闲内存" + free / 1000
							/ 1000 + "M\t最大可以内存" + useable / 1024 / 1024 + "M");
					OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
					long sfree = osm.getFreePhysicalMemorySize();
					long stotal = osm.getTotalPhysicalMemorySize();
					logger.info("系统物理内存总计" + stotal / 1024 / 1024 + "M\t系统物理可用内存" + sfree / 1024 / 1024 + "M");
					try {
						Thread.sleep(10 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	// 输出当前在线玩家人数 5分钟输出一次
	private static void roleNumLogThread() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						final int val = fire.pb.map.RoleManager.getInstance().getRoles().size();
						fire.log.YYLogger.onlineLog(val);
						Thread.sleep(5 * 60 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	// 通过堆栈方式输出日志，这个方法适合查找bug用，比如一个addmoney的方法，有好多地方调用，不知道是那块调用了他
	// 就在这个方法中添加这个日志输出，就能很明确的知道那块调用了这个方法，那处先调用这个方法
	public static String ThrowableLog() {
		Throwable ex = new Throwable();
		StackTraceElement[] stackTraceElement = ex.getStackTrace();
		StringBuffer sb = new StringBuffer();
		if (stackTraceElement != null) {
			for (int i = 0; i < stackTraceElement.length; i++) {
				sb.append(stackTraceElement[i] + "\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 判断是否是合过服的服务器 true表示合过的服务器
	 * @return
	 */
	public static boolean isMergeServer(){
		String str = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.merge.localids");
		if(str==null){
			return false;
		}
		String[] tmpStrs = str.split(";");
		if(tmpStrs==null){
			return false;
		}
		if(tmpStrs.length>=2){
			return true;
		}
		return false;
	}
}
