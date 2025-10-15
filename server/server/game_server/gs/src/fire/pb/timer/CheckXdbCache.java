

package fire.pb.timer;


import fire.pb.main.ModuleManager;
import fire.pb.main.MkdbModule;
import fire.pb.util.FireProp;


public class CheckXdbCache implements Runnable {

	@Override
	public void run() {
		try {
			if (FireProp.getIntValue("sys", "sys.checkxdb")==1){
				int mb = 1024*1024;     
		           
		        // 可使用内存     
		        long totalMemory = Runtime.getRuntime().totalMemory() / mb;     
		        // 剩余内存     
		        long freeMemory = Runtime.getRuntime().freeMemory() / mb;     
		        // 最大可使用内存     
		        long maxMemory = Runtime.getRuntime().maxMemory() / mb;
		        
		        long useMemory = totalMemory - freeMemory;
		        
		        fire.log.Module.logger.info("TotalMemory:"+totalMemory+"FreeMemory:"+freeMemory+"MaxMemory:"+maxMemory);
		        if (freeMemory < 3072){
		        	MkdbModule module = (MkdbModule)ModuleManager.getInstance().getModuleByName("mkdb");
		    		module.printTableCacheInfo(useMemory,freeMemory);
		        }
			}
		} catch (Exception e) {
			fire.log.Module.logger.error("check xdb cache error.",e);
		}
		
	}

}

