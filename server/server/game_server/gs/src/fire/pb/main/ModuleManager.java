package fire.pb.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

//TODO:要不要提供一个静态的instance?
public class ModuleManager {
	static private final Logger logger = Logger.getLogger("SYSTEM");
	static private ModuleManager instance;

	// key:modulename,value:类名
	private Map<String, ModuleInterface> modules;
	private java.util.ArrayList<String> 已初始化的模块;

	/**
	 * 只能在主线程中调用
	 */
	ModuleManager() {

	}
	/**
	 * 只能在主线程调用
	 * edit by lc 
	 * 把初始化模块的代码从构造函数中移出来，原因是：
	 * 模块初始化时会互相依赖（例如A依赖B），而A初始化时怎么获得B，是通过ModuleManager.getInsance().getModuleByName。而此时instance还没构造好，会空指针
	 */
	private void initInstance()
	{
		if(!Gs.isInMainThread()) throw new RuntimeException("不在主线程中");
		modules = new java.util.concurrent.ConcurrentHashMap<String, ModuleInterface>();
		// 根据依赖性插入排序
		// XXX：这段代码是有问题滴
		//final java.util.ArrayList<fire.pb.main.ModuleInfo> al = new java.util.ArrayList<fire.pb.main.ModuleInfo>();
		final DAG<fire.pb.main.ModuleInfo> dag = new DAG<fire.pb.main.ModuleInfo>();
		final Map<Integer, fire.pb.main.ModuleInfo> mconfig=ConfigManager
				.getInstance().getConf(fire.pb.main.ModuleInfo.class);
		for (final fire.pb.main.ModuleInfo ml : mconfig.values()) {
			final DAGNode<ModuleInfo> dg=dag.createNodeIfNotExist(ml);
			if(ml.dep!=null)
				for(final String m:ml.dep){
					for(final fire.pb.main.ModuleInfo mi:mconfig.values()){
						if(mi.name.equals(m)) {
							dg.addPrev(mi);
						}
					}					
				}
		}

		final String foundError[]=new String[1];
		foundError[0]="";
		已初始化的模块=new java.util.ArrayList<String>(mconfig.size());
		dag.walk(new DAG.IWalk<ModuleInfo>() {

			@Override
			public void onNode(DAGNode<ModuleInfo> n) {

				final fire.pb.main.ModuleInfo mi = n.getName();
				final String classname;
				if (mi.classname == null || mi.classname.isEmpty()) {
					classname = "fire.pb." + mi.name + ".Module";
				} else
					classname = mi.classname;
				Object obj;
				try {
					obj = Class.forName(classname).newInstance();
				} catch (final Exception ex) {
					logger.error(String.format("构造模块%s失败", mi.name), ex);
					foundError[0] = mi.name;
					return;
				}
				if (!(obj instanceof ModuleInterface)) {
					logger.error(String.format("模块%s配置错误", mi.name));
					foundError[0] = mi.name;
					return;
				}
				final ModuleInterface m = (ModuleInterface) obj;
				try {
					m.init();
				} catch (final Exception ex) {
					logger.error(String.format("模块%s启动失败", mi.name), ex);
					foundError[0] = mi.name;
				}
				modules.put(mi.name, m);
				已初始化的模块.add(mi.name);
			}

		});

		if (!foundError[0].isEmpty())
		{
			exit();//退出流程
			throw new RuntimeException("模块"+foundError[0]+"初始化失败");
		}
	}

	public static ModuleManager getInstance() {
		return instance;
	}

	static void init() {
		instance = new ModuleManager();
		instance.initInstance();
	}
	
	public synchronized ReloadResult reload()
	{
		List<String> reloadmodules = getReloadModules();
		ReloadResult result = new ReloadResult(true);
		for(String modulename : reloadmodules)
		{
			try
			{
				ModuleInterface module = ModuleManager.getInstance().getModuleByName(modulename);
				if (module == null){
					logger.error("Module: " + modulename + " is invalid. \n");
					return new ReloadResult(false, "module " + modulename + " is invalid. \n");
				}

				ReloadResult moduleresult = module.reload();
				
				if(moduleresult == null)
					return result;
				result.appendResult(moduleresult);
				if (result.isSuccess())
					result.appendMsg("Module: ").appendMsg(module.getClass().getName()).appendMsg("  reload success. \n");
				else
					return result;
			}catch(Exception e)
			{
				String errormsg = "Reload Module " + modulename +" Error. \n";
				logger.error(errormsg, e);
				result.setSuccess(false);
				result.appendMsg(errormsg);
				return result;
			}
		}
		
		return result;
	}
	
	private List<String> getReloadModules()
	{
		Map<Integer,HotfixConfig> hotfixs = ConfigManager.getInstance().getConf(HotfixConfig.class);
		List<String> reloadmodules = new LinkedList<String>();
		Set<String> reloadcfgs = new HashSet<String>();
		for(HotfixConfig hotfix : hotfixs.values())
		{
			if(hotfix.type != 2)//文件
				reloadcfgs.add(hotfix.name);
		}
		
		for(HotfixXml2ModuleConfig xml2module : ConfigManager.getInstance().getConf(HotfixXml2ModuleConfig.class).values())
		{
			if(!reloadcfgs.contains(xml2module.filename))
				continue;
			if(!xml2module.canreload)
				continue;
			if(xml2module.module == null || xml2module.module.equals(""))
				continue;//该文件不需要reload模块
			if(reloadmodules.contains(xml2module.module))
				continue;
			reloadmodules.add(xml2module.module);
		}
		return reloadmodules;
	}
	

	/**
	 * 也许会返回null
	 * 
	 * @param name
	 * @return
	 */
	public ModuleInterface getModuleByName(String name) {
		return modules.get(name);
	}
	/**
	 * @param name
	 * @return
	 */
	public ModuleInterface putModuleByName(String name, ModuleInterface m) {
		return modules.put(name,m);
	}
	
	/**
	 * 只能在主线程中调用
	 */
	void exit() {
		if(!Gs.isInMainThread()) throw new RuntimeException("不在主线程中");
		for(int i=已初始化的模块.size();i!=0;i--){	
			try {
				modules.get(已初始化的模块.get(i-1)).exit();
			} catch (final Exception ex) {
				logger.error("err", ex);
			}
		}
	}
}
