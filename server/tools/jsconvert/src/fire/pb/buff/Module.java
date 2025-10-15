package fire.pb.buff;

import java.util.HashMap;

import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.instant.InstantBuffCfg;
import fire.pb.main.IModule;
import fire.pb.main.ModuleMgr;


public class Module implements IModule
{
	public static final String MODULE_NAME = "buff";
	
	java.util.Map<Integer, InstantBuffCfg> defaultIBuffConfigs = new HashMap<Integer, InstantBuffCfg>();
	java.util.Map<Integer, ConstantlyBuffConfig> defaultCBuffConfigs = new HashMap<Integer, ConstantlyBuffConfig>();
	
	public static Module getInstance()
	{
		//不再用以上的单例模式，从ModuleManager获得，由其管理
		return ((fire.pb.buff.Module)ModuleMgr.getInstance().getModuleByName(MODULE_NAME));
	}

	@Override
	public void exit()
	{

	}

	@Override
	public void init() throws Exception
	{
		defaultIBuffConfigs.clear();
		for (fire.pb.buff.SIBuffConfig buffconfig : fire.pb.main.ConfigMgr.getInstance().getConf(fire.pb.buff.SIBuffConfig.class).values())
		{
			String c = buffconfig.classname;
			if (c == null || c.isEmpty())
				c = "fire.pb.buff.continual.NullBuff";
			
			try
			{
				InstantBuffCfg ibuffConfig = new InstantBuffCfg(buffconfig);
				defaultIBuffConfigs.put(ibuffConfig.getBuffTypeId(), ibuffConfig);
			} catch (Exception e)
			{
				System.out.println("Initial InstantBuff Error, InstantBuff Id = " + buffconfig.getId());
			}
		}
		
		for (fire.pb.buff.SCBuffConfig buffconfig : fire.pb.main.ConfigMgr.getInstance().getConf(fire.pb.buff.SCBuffConfig.class).values())
		{
			String c = buffconfig.classname;
			if (c == null || c.isEmpty())
				c = "fire.pb.buff.continual.NullBuff";
			try
			{
				ConstantlyBuffConfig cbuffConfig = new ConstantlyBuffConfig(buffconfig);
				defaultCBuffConfigs.put(cbuffConfig.getBuffTypeId(), cbuffConfig);
			} catch (Exception e)
			{
				System.out.println("Initial ConstantlyBuff Error, ConstantlyBuff Id = " + buffconfig.getId());
			}
		}
	}

	public InstantBuffCfg getDefaultIBuffConfig(int buffId)
	{
		return defaultIBuffConfigs.get(buffId);
	}
	
	public  java.util.Map<Integer, ConstantlyBuffConfig> getCBuffConfig()
	{
		return defaultCBuffConfigs;
	}
}
