

package fire.pb.mbean.beans;

import fire.pb.main.ModuleManager;
import fire.pb.main.MkdbModule;


public interface CacheInfoMXBean {

	public void getCacheInfo();
}
class CacheInfo implements CacheInfoMXBean{


	@Override
	public void getCacheInfo() {
		int mb = 1024*1024;   
		// 可使用内存     
	    long totalMemory = Runtime.getRuntime().totalMemory() / mb;     
	    // 剩余内存     
	    long freeMemory = Runtime.getRuntime().freeMemory() / mb;     
	      
	    long useMemory = totalMemory - freeMemory;
	    MkdbModule module = (MkdbModule)ModuleManager.getInstance().getModuleByName("mkdb");
		module.printTableCacheInfo(useMemory,freeMemory);
	}
	
}
