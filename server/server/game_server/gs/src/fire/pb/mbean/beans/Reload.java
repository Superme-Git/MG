package fire.pb.mbean.beans;

import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.mbean.Module;

public class Reload implements ReloadMXBean
{

	@Override
	public String reload()
	{
		ReloadResult result = new ReloadResult(true);
		try
		{
			result.appendResult(ConfigManager.getInstance().reload());
			if(!result.isSuccess())
				return result.getMsg();
		}
		catch(Exception e)
		{
			Module.logger.error("Reload unknown exception",e);
			return "Reload unknown exception, refer to log";
		}
		result.appendResult(ModuleManager.getInstance().reload());
		if(!result.isSuccess())
			return result.getMsg();
		
		result.appendMsg("Reload success. \n");
		Module.logger.info(result.getMsg());
		return result.getMsg();
	}
	
	
	
	
}
