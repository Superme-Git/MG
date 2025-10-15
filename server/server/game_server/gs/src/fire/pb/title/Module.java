package fire.pb.title;

import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;


public class Module implements ModuleInterface {
	static private final Logger logger = Logger.getLogger(Module.class);
	
	private TitleManager titleManager;
	
	public TitleManager getTitleManager(){
		return titleManager;
	}
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		logger.info("称谓   模块初始化开始");
		final fire.pb.main.ConfigManager cm = fire.pb.main.ConfigManager.getInstance();
		titleManager = new TitleManager(cm);
		logger.info("称谓   模块初始化完成 ");
		
	}
	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
}
