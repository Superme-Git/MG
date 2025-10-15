package fire.pb.battle.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * BattleAI
 * @author nobody
 *
 */
public class BattleAI
{
	int id;//AI id
	int triggerType;//触发时机
	Set<Integer> trigerSkillIds = null;//被技能使用或命中触发时，需要的技能id 
	List<Require> requires = new LinkedList<Require>();//条件ID
	List<AIOperation> actions = new LinkedList<AIOperation>();
	int count = -1;//执行次数，次数到后将移除。默认为-1时,没有次数限制
	List<Integer> enableAIs = new LinkedList<Integer>();//先启用
	List<Integer> disableAIs = new LinkedList<Integer>();//后停用
	
	public final static int AI_CAMP_FRIEND = 1;//已方
	public final static int AI_CAMP_ENEMY = 2; //敌方
	public final static int AI_CAMP_SLEF = 3;	 //自己
	public final static int AI_CAMP_ALL = 4;	 //敌我双方
	public final static int AI_CAMP_FRIEND_NOME = 5;	 //已方不包含自己
	
	public static int TRIGGER_BATTLE_START = 1;//战斗开始触发
	public static int TRIGGER_ROUND_START = 2;//回合开始出手触发
	public static int TRIGGER_BEFORE_OPERATION = 3;//行动开始触发 
	public static int TRIGGER_AFTER_OTHERS_OPERATION = 4;//别人行动后触发 
	public static int TRIGGER_BEFORE_ROUND_START = 5;//读秒触发
	public static int TRIGGER_BATTLE_END = 6;//战斗结束触发
	public static int TRIGGER_ESCAPE_SUCC = 7;//逃跑成功后触发
	public static int TRIGGER_ESCAPE_FAIL = 8;//逃跑失败后触发
	public static int TRIGGER_DEATH_SELF = 9;// 怪物死亡后触发
	public static int TRIGGER_DEATH_FRIEND = 10;//我方怪物死亡后触发
	public static int TRIGGER_DEATH_ENERMY = 11;//敌方怪物死亡后触发
	public static int TRIGGER_DECISION = 12;//回合开始触发-----------回合开始决定常规Decision时触发
	public static int TRIGGER_AFTER_SELF_OPERATION = 13;// 行动后触发
	public static int TRIGGER_USE_SKILL = 14;// 自己使用某个技能后触发
	public static int TRIGGER_SKILL_HIT = 15;// 自己被某个技能命中后触发
	public static int TRIGGER_ROUND_END = 16;// 回合结束触发
	

	BattleAI(int id)
	{
		this.id = id;
	}
	
}
