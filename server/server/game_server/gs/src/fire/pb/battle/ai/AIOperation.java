package fire.pb.battle.ai;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import fire.pb.util.Parser.ID2Odds;

/**AI动作*/
public class AIOperation
{
	int index;	//AI action id
	int changeShape = -1;//shape id，没有为-1
	int operationType = -1;//操作类型，包括：普通攻击、使用技能、防御、保护、逃跑、休息，没有为-1
	int operationId = -1; //使用技能时的技能ID，没有为-1
	List<Integer> skillSoltId = new LinkedList<Integer>(); //调用伙伴几号槽里技能 ,key为槽位，value为技能ID
	List<Float> skillSoltOdds = new LinkedList<Float>(); //调用伙伴几号槽里技能 ,key为槽位，value为技能ID
	float countFactor = 0;//使用技能的系数
	float countConst = 1;//使用技能的常数
	int escOdds = -1;//逃跑几率，当optionType为逃跑时有效（0-100），没有为-1
	int goal = -1;//动作的targetfilter id, 只能有一个，没有为-1
	Map<Integer,ID2Odds> summonMaps = new HashMap<Integer, ID2Odds>();//召唤小怪，AI怪的召唤与普通玩家的召唤宠物不同,key为fighterid，value为怪物id
	int changeBG;
	boolean clientAction;
	boolean changeAim = false;
	AIOperation(int index)
	{
		this.index = index;
	}
	
	public AIOperation(AIOperation o)
	{
		this.index 						= o.index 		;
		this.changeShape 				= o.changeShape 	;
		this.operationType 				= o.operationType;
		this.operationId 				= o.operationId 	;
		this.skillSoltId.addAll(o.skillSoltId);
		this.skillSoltOdds.addAll(o.skillSoltOdds);
		this.countFactor 				= o.countFactor 	;
		this.countConst 				= o.countConst 	;
		this.escOdds 					= o.escOdds 		;
		this.goal 						= o.goal 		;
		this.summonMaps.putAll(o.summonMaps);
		this.changeBG					= o.changeBG		;
		this.clientAction				= o.clientAction	;
		this.changeAim					= o.changeAim	;
	}
	
	
	public int getIndex()
	{
		return index;
	}

	public int getOperationType()
	{
		return operationType;
	}

	public List<Integer> getSkillSoltID()
	{
		return skillSoltId;
	}
	
	public List<Float> getSkillSoltOdds()
	{
		return skillSoltOdds;
	}
	
	public int getOperationId()
	{
		return operationId;
	}

	public int getGoal()
	{
		return goal;
	}

	public Map<Integer, ID2Odds> getSummonMaps()
	{
		return summonMaps;
	}

	public int getEscOdds()
	{
		return escOdds;
	}

	public float getCountFactor()
	{
		return countFactor;
	}

	public float getCountConst()
	{
		return countConst;
	}

	public int getChangeShape()
	{
		return changeShape;
	}
	public boolean getChangeAim()
	{
		return changeAim;
	}	
	public boolean isClientAction()
	{
		return clientAction;
	}
	
}
