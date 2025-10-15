package fire.pb.npc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.event.Event;
import fire.pb.event.LevelupEvent;
import fire.pb.game.Sdongtaifuwu;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

import org.apache.log4j.Logger;


public class Module implements ModuleInterface, fire.pb.event.EventHandler {
	static public final Logger logger = Logger.getLogger("MAPMAIN");
	public java.util.Properties npcprop = fire.pb.main.ConfigManager.getInstance().getPropConf("npc");
	
	public static Map<Integer, Integer >festivalGiftConfig = new HashMap<Integer, Integer>();
	
	public static Module getInstance(){
		return ((fire.pb.npc.Module)ModuleManager.getInstance().getModuleByName("npc"));
	}
	
	@Override
	public void exit() {
	}
	
	@Override
	public void init() throws Exception{
		logger.info("npc模块初始化开始");
		NpcServiceManager.getInstance().init();
		NpcManager.getInstance().init();
		// 初始化活动奖励表
		//AwardManager.getInstance().initData();
		QuestionManager.getInstance().init();
		ChestManager.getInstance().init();
		fire.pb.event.Poster.getPoster().listenEvent(this, fire.pb.event.EnterWorldEvent.class);
		fire.pb.event.Poster.getPoster().listenEvent(this, LevelupEvent.class);
		SpecialVisitProcessCreator.getInstance().transFestivalGift(festivalGiftConfig);
		initDynamicServiceCfg();
		logger.info("npc模块初始化完成");
	}
	
	public static final Map<Integer, Map<Integer, List<DynamicTime>>> npcDynamicServiceTime = new HashMap<Integer, Map<Integer, List<DynamicTime>>>();
	
	class DynamicTime{
		private long startTime;
		private long endTime;
		DynamicTime(long s, long e){
			this.startTime = s;
			this.endTime = e;
		}
		public long getStartTime() {
			return startTime;
		}
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
		public long getEndTime() {
			return endTime;
		}
		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}
		
	}
	
	private void initDynamicServiceCfg(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Sdongtaifuwu cfg : ConfigManager.getInstance().getConf(Sdongtaifuwu.class).values()){
			Map<Integer, List<DynamicTime>> services = null;
			services = npcDynamicServiceTime.get(cfg.npcid);
			if(services == null){
				services = new HashMap<Integer, List<DynamicTime>>();
				npcDynamicServiceTime.put(cfg.npcid, services);
			}
			List<DynamicTime> times = null;
			times = services.get(cfg.fuwuid);
			if(times == null){
				times = new ArrayList<DynamicTime>();
				services.put(cfg.fuwuid, times);
			}
			try {
				times.add(new DynamicTime(sdf.parse(cfg.kaishitime).getTime(), sdf.parse(cfg.jeishutime).getTime()) );
			} catch (Exception e) {
			}
		}
	}
	
	public static void addDynamicServices(int npcid, ArrayList<Integer> result){
		long now = System.currentTimeMillis();
		if(result == null) return;
		Map<Integer, List<DynamicTime>> services = npcDynamicServiceTime.get(npcid);
		if(services == null) return;
		for(Map.Entry<Integer, List<DynamicTime>> pair : services.entrySet()){
			for(DynamicTime time : pair.getValue()){
				if(time.getStartTime()<=now && time.getEndTime()>=now){
					result.add(pair.getKey());
					break;
				}
			}
		}
	}
	
	
	
	public static boolean isDynamicServiceWorkingById(int npcId,int serviceId){
		long now = System.currentTimeMillis();
		Map<Integer, List<DynamicTime>> services = npcDynamicServiceTime.get(npcId);
		if(services == null){
			return false;
		} 
		for(Map.Entry<Integer, List<DynamicTime>> pair : services.entrySet()){
			for(DynamicTime time : pair.getValue()){
				if(time.getStartTime()<=now && time.getEndTime()>=now){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public static boolean isDynamicServiceWorkingByKey(long npcKey,int serviceId){
		Npc npc = SceneNpcManager.getNpcByKey(npcKey);
		if (npc == null) {
			logger.info("npc  is null " + npcKey);
			return false;
		}
		return isDynamicServiceWorkingById(npc.getNpcID(), serviceId);
	}
	
	
	@Override
	public ReloadResult reload() throws Exception
	{
		NpcManager.getInstance().reload();
		NpcServiceManager.reload();
		PReqFortuneWheel.reload();
		return new ReloadResult(true);
	}

	@Override
	public void onEvent(final Event e) {
		
	}
}
