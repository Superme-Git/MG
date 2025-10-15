package fire.pb.mbean;

import fire.log.Logger;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.mbean.beans.MBeanFactory;
import fire.pb.mbean.beans.Reload;


public class Module implements ModuleInterface
{
	private javax.management.MBeanServer mbs=java.lang.management.ManagementFactory.getPlatformMBeanServer();
	public static Logger logger = Logger.getLogger(Module.class);
	@Override
	public void init() throws Exception
	{
		logger.info(new StringBuilder("JMX SERVER START, rmiport = ").append(ConfigManager.getRmiPort()));
		
		//register all mbeans
		registerMbean(MBeanFactory.getInstance().getActiveRoleState(), "gs.counter:type=ActiveRoleState");
		registerMbean(MBeanFactory.getInstance().getActiveUserState(), "gs.counter:type=ActiveUserState");
		registerMbean(MBeanFactory.getInstance().getChargeUserState(), "gs.counter:type=ChargeUserState");
		registerMbean(MBeanFactory.getInstance().getConsumeRoleState(), "gs.counter:type=ConsumeRoleState");
		registerMbean(MBeanFactory.getInstance().getOnlinesNumState(), "gs.counter:type=OnlinesNumState");
		registerMbean(MBeanFactory.getInstance().getForceLogActiveState(), "gs.counter:type=ForceActiveLog");
		registerMbean(MBeanFactory.getInstance().getCacheInfoBean(), "gs.counter:type=CacheInfo");
		registerMbean(MBeanFactory.getInstance().getRankList(), "gs.counter:type=RankList");
		registerMbean(MBeanFactory.getInstance().getGMProcMXBeant(), "gs.counter:type=GMProcMXBeant");
		registerMbean(MBeanFactory.getInstance().getSearchMXBean(), "bean:name=SearchMXBean");
		
		
		registerMbean(new Reload(), "IWEB:type=Reload");
	}
	
	
	private void registerMbean(Object obj,String name) throws javax.management.NotCompliantMBeanException,javax.management.MBeanRegistrationException,javax.management.InstanceAlreadyExistsException,javax.management.MalformedObjectNameException{
		mbs.registerMBean(obj, new javax.management.ObjectName(name));
	}
	
	@Override
	public void exit()
	{
	}
	

	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
}
