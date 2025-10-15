package fire.pb.master;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

public class Module implements ModuleInterface{
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Module.class);

	static org.apache.log4j.Logger getLogger() {
		return logger;
	}

	public static Module getInstance() {
		final Module instance = (Module) fire.pb.main.ModuleManager
				.getInstance().getModuleByName("master");
		return instance;
	}

	private MasterManager mm;

	@Override
	public void exit() {
	}

	public MasterManager getMasterManager() {
		return mm;
	}

	@Override
	public void init() throws Exception {
		logger.info("master模块初始化开始");
		try {
			
		} catch ( Exception e ) {
			e.printStackTrace();
			logger.error("master模块启动失败");
		}
		
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}


}
