package fire.pb.mission.activelist;

import java.util.concurrent.atomic.AtomicInteger;

import fire.log.Logger;
import fire.pb.activity.ActivityConfNew;
import fire.pb.main.ConfigManager;
import fire.pb.mission.SActiveChestConfig;
import fire.pb.mission.SActiveChestConfig4D;

public class RoleLivenessManager
{
	public final static Logger logger = Logger.getLogger("ACTIVE");
	
	public static int MAXRECOMMEND = 1; //最大推荐度,根据读表数据产生
	
	private static AtomicInteger currentRecommend = new AtomicInteger(1);
	
	public static void updateRecommend() {
		if(currentRecommend.incrementAndGet() > MAXRECOMMEND)
			currentRecommend.set(1);
	}
	
	public static int getCurrentRecommend() {
		return currentRecommend.get();
	}
	
	public static void setCurrentRecommend(int recommend) {
		currentRecommend.set(recommend);
	}
	
	public static void serverShutdown()
	{
		try
		{
			new mkdb.Procedure()
			{
				protected boolean process() throws Exception {
					xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.get(1);
					if (serviceInfo == null) {
						serviceInfo = xbean.Pod.newServiceInfo();
						serviceInfo.setActiverecommend(RoleLivenessManager.getCurrentRecommend());
						xtable.Serviceinfos.insert(1, serviceInfo);
					} else {
						serviceInfo.setActiverecommend(RoleLivenessManager.getCurrentRecommend());
					}
					return true;
				};
			}.submit().get();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static final RoleLivenessManager instance = new RoleLivenessManager();
	private RoleLivenessManager(){}
	
	public static RoleLivenessManager getInstance()
	{
		return instance;
	}
	
	public static int YingFuMax;
	
	public static int YingFuZheSunXishu;
	
	public static int YingFuXishu;
	
	public void init()
	{
		fire.pb.common.SCommon common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(218);
		YingFuMax = Integer.parseInt(common.getValue());
		
		common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(216);
		YingFuZheSunXishu = Integer.parseInt(common.getValue());
		
		common = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(217);
		YingFuXishu = Integer.parseInt(common.getValue());
	}
	
	public ActivityConfNew getActivityConfNew(int id) {
		return RoleLiveness.getConfigActivity(new Integer(id));
	}

	public SActiveChestConfig getSActiveChestConfigById(int id) {
		SActiveChestConfig conf = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			conf = ConfigManager.getInstance().getConf(SActiveChestConfig.class).get(new Integer(id));
		} else { //点卡服
			SActiveChestConfig4D conf4d = ConfigManager.getInstance().getConf(SActiveChestConfig4D.class).get(new Integer(id));
			if(conf4d == null)
				return null;
			conf = new SActiveChestConfig();
			conf.id=conf4d.id ;
			conf.activeness=conf4d.activeness ;
			conf.itemid=conf4d.itemid ;
		}
		return conf;
	}

	public int getChestItemByAct(int id, float activeness) {
		SActiveChestConfig conf = getSActiveChestConfigById(id);
		if(null != conf && activeness >= conf.getActiveness())
			return conf.getItemid();
		return 0;
	}
}
