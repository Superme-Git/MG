package fire.pb.triggers.event;

import java.util.LinkedList;
import java.util.List;

import fire.pb.triggers.STriggerCondition;
import fire.pb.triggers.TriggerRole;

public abstract class TriggerEvent
{
	public final static int TRIGGER_TYPE_BATTLE_AI = 1;
	public final static int TRIGGER_TYPE_BATTLE_WIN = 2;
	public final static int TRIGGER_TYPE_USE_ITEM = 4;
	public final static int TRIGGER_TYPE_USE_SKILL = 5;
	public final static int TRIGGER_TYPE_DEATH = 6;
	public final static int TRIGGER_TYPE_UPLEVEL = 7;
	public final static int TRIGGER_TYPE_ENTER_REGION = 8;
	public final static int TRIGGER_TYPE_NPC = 9;
	public final static int TRIGGER_TYPE_UPLEVEL_TASK = 10;
	
	public final int id;
	
	public final int type;
	
	public final int needlevel;
	
	public final int needquest;
	
	public final int needskill = 0;
	
	public final int needschool;
	
	protected final List<Integer> args = new LinkedList<Integer>();
	
	public final int msgId;
	
	public final int famoussiteId;	
	
	public final int rewardId;
	
	public final int achievementId;
	
	public final int removequest;
	
	public final int addquest;
	
	public TriggerEvent(STriggerCondition scondition)
	{
		id = scondition.id;
		type = scondition.type;
		needlevel = scondition.level;
		needquest = scondition.task;
		needschool = scondition.school;
		if(scondition.para != null)
			for(String str : scondition.para)
				args.add(Float.valueOf(str).intValue());
		msgId = scondition.msg;
		famoussiteId = scondition.spot;
		rewardId = scondition.reward;
		achievementId = scondition.chengjiu;
		removequest = scondition.missiondel;
		addquest = scondition.missionadd;
	}
	
	public abstract boolean triggered(TriggerRole triggerRole, List<Integer> args);
	
	public abstract int getType();
	
}
