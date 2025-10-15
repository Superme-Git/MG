package fire.pb.triggers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.log.Logger;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ModuleManager;
import fire.pb.main.ReloadResult;
import fire.pb.triggers.event.BattleAiAction;
import fire.pb.triggers.event.BattleDead;
import fire.pb.triggers.event.BattleWin;
import fire.pb.triggers.event.EnterRegion;
import fire.pb.triggers.event.TriggerEvent;
import fire.pb.triggers.event.TriggerNpc;
import fire.pb.triggers.event.UpLevel;
import fire.pb.triggers.event.UpLevelAndTaskCom;
import fire.pb.triggers.event.UseItem;
import fire.pb.triggers.event.UseSkill;

public class Module implements ModuleInterface
{
	public final static String MODULE_NAME = "triggers"; 
	
	public static Logger logger = Logger.getLogger(MODULE_NAME);
	
	private Map<Integer,TriggerEvent> triggerevents = new HashMap<Integer, TriggerEvent>();
	
	private Map<Integer, Set<Integer>> comquest2triggerid = new HashMap<Integer, Set<Integer>>();

	public static Module getInstance()
	{
		return ((fire.pb.triggers.Module)ModuleManager.getInstance().getModuleByName("triggers"));
	}
	
	@Override
	public void exit()
	{
		
	}

	@Override
	public void init() throws Exception
	{
		logger.info("开始初始化Triggers模块");
		Map<Integer,STriggerCondition> sconditions = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { //普通服
			logger.info("普通服Triggers模块,直接用...");
			sconditions = ConfigManager.getInstance().getConf(STriggerCondition.class);
		} else { //点卡服,拿点卡服的配置手工转换成原始配置
			logger.info("点卡服Triggers模块,点卡服数据转换...");
			java.util.TreeMap<Integer,STriggerCondition4D> sconditions2 = ConfigManager.getInstance().getConf(STriggerCondition4D.class);
			logger.info("点卡服Triggers模块,点卡服数据size=" + sconditions2.size());
			sconditions = new java.util.TreeMap<Integer, STriggerCondition>();
			for(java.util.Map.Entry<Integer, STriggerCondition4D> scondition4d : sconditions2.entrySet())
			{
				STriggerCondition tt = new STriggerCondition();
				tt.id=scondition4d.getValue().id ;
				tt.school=scondition4d.getValue().school ;
				tt.task=scondition4d.getValue().task ;
				tt.level=scondition4d.getValue().level ;
				tt.type=scondition4d.getValue().type ;
				tt.para=scondition4d.getValue().para ;
				tt.msg=scondition4d.getValue().msg ;
				tt.spot=scondition4d.getValue().spot ;
				tt.chengjiu=scondition4d.getValue().chengjiu ;
				tt.reward=scondition4d.getValue().reward ;
				tt.missiondel=scondition4d.getValue().missiondel ;
				tt.missionadd=scondition4d.getValue().missionadd ;
				sconditions.put(scondition4d.getKey(), tt);
			}
			logger.info("点卡服Triggers模块,点卡服数据转换后size=" + sconditions.size());
		}
		for(STriggerCondition scondition : sconditions.values())
		{
			TriggerEvent event = createTriggerEvent(scondition);
			//logger.info("Triggers模块id:" + event.id);
			if(event == null)
				continue;
			triggerevents.put(scondition.id, event);
			if(scondition.task > 0){
				Set<Integer> temp = comquest2triggerid.get(scondition.task);
				if(null != temp && temp.size() > 0){
					temp.add(scondition.task);
				} else {
					comquest2triggerid.remove(scondition.id);
					Set<Integer> ids = new HashSet<Integer>();
					ids.add(scondition.id);
					comquest2triggerid.put(scondition.task, ids);
				}
			}
		}
		logger.info("Triggers模块初始化完成");
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		return null;
	}
	
	public Set<Integer> getTriggerIdByTaskId(int taskid)
	{
		return comquest2triggerid.get(taskid);
	}

	public Map<Integer,TriggerEvent> getTriggerEvents()
	{
		return triggerevents;
	}
	
	public TriggerEvent createTriggerEvent(STriggerCondition scondition)
	{
		switch(scondition.type)
		{
		case TriggerEvent.TRIGGER_TYPE_BATTLE_AI:
			return new BattleAiAction(scondition);
		case TriggerEvent.TRIGGER_TYPE_BATTLE_WIN:
			return new BattleWin(scondition);
		case TriggerEvent.TRIGGER_TYPE_USE_ITEM:
			return new UseItem(scondition);
		case TriggerEvent.TRIGGER_TYPE_USE_SKILL:
			return new UseSkill(scondition);
		case TriggerEvent.TRIGGER_TYPE_DEATH:
			return new BattleDead(scondition);
		case TriggerEvent.TRIGGER_TYPE_UPLEVEL:
			return new UpLevel(scondition);
		case TriggerEvent.TRIGGER_TYPE_ENTER_REGION:
			return new EnterRegion(scondition);
		case TriggerEvent.TRIGGER_TYPE_NPC:
			return new TriggerNpc(scondition);
		case TriggerEvent.TRIGGER_TYPE_UPLEVEL_TASK:
			return new UpLevelAndTaskCom(scondition);
		}
		return null;
	}
	
}
