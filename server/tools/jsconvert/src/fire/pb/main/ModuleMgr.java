package fire.pb.main;

import java.util.Map;

import fire.pb.main.ModuleInfo;

public class ModuleMgr {
	static private ModuleMgr instance;

	private Map<String, IModule> modules;
	private java.util.ArrayList<String> hasInitModule;

	ModuleMgr() {

	}

	private void initInstance() {
		if (!Main.isInMainThread())
			throw new RuntimeException("not in main thread");
		modules = new java.util.concurrent.ConcurrentHashMap<String, IModule>();

		final DAG<fire.pb.main.ModuleInfo> dag = new DAG<fire.pb.main.ModuleInfo>();
		final Map<Integer, fire.pb.main.ModuleInfo> mconfig = ConfigMgr
				.getInstance().getConf(fire.pb.main.ModuleInfo.class);
		for (final fire.pb.main.ModuleInfo ml : mconfig.values()) {
			final DAGNode<ModuleInfo> dg = dag.createNodeIfNotExist(ml);
			if (ml.dep != null)
				for (final String m : ml.dep) {
					for (final fire.pb.main.ModuleInfo mi : mconfig.values()) {
						if (mi.name.equals(m)) {
							dg.addPrev(mi);
						}
					}
				}
		}

		final String foundError[] = new String[1];
		foundError[0] = "";
		hasInitModule = new java.util.ArrayList<String>(mconfig.size());
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
					return;
				}
				if (!(obj instanceof IModule)) {
					return;
				}
				final IModule m = (IModule) obj;
				try {
					m.init();
				} catch (final Exception ex) {
					return;
				}
				modules.put(mi.name, m);
				hasInitModule.add(mi.name);
			}

		});
	}

	public static ModuleMgr getInstance() {
		return instance;
	}

	static void init() {
		instance = new ModuleMgr();
		instance.initInstance();
	}

	public IModule getModuleByName(String name) {
		return modules.get(name);
	}

	public IModule putModuleByName(String name, IModule m) {
		return modules.put(name, m);
	}

	void exit() {
		if (!Main.isInMainThread())
			throw new RuntimeException("not in main thread");
		for (int i = hasInitModule.size(); i != 0; i--) {
			try {
				modules.get(hasInitModule.get(i - 1)).exit();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
