package fire.pb.gm;

import fire.pb.main.ModuleManager;
import fire.pb.main.MkdbModule;

public class GM_cache extends GMCommand {
	@Override
	boolean exec(String[] args) {
		int mb = 1024 * 1024;
		long totalMemory = Runtime.getRuntime().totalMemory() / mb;
		long freeMemory = Runtime.getRuntime().freeMemory() / mb;

		long useMemory = totalMemory - freeMemory;
		MkdbModule module = (MkdbModule) ModuleManager.getInstance()
				.getModuleByName("mkdb");
		module.printTableCacheInfo(useMemory, freeMemory);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
