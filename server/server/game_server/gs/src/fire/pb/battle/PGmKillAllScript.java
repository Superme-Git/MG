package fire.pb.battle;
import java.util.List;

public class PGmKillAllScript extends PPlayScript {
	private final long roleId;
	
	public PGmKillAllScript(final long battleID,final long roleId){
		super(battleID);
		this.roleId = roleId;
	}
	
	/**获取本回合剧本*/
	@Override
	final java.util.List<ResultItem> getScript(final xbean.BattleInfo battle, final List<fire.pb.battle.AIOperation> clientAIActions)
	{
		final java.util.List<ResultItem> script = new java.util.LinkedList<ResultItem>();
		
		int rolefighterId = battle.getRoleids().get(roleId);
		
		xbean.Fighter rolexfighter = battle.getFighters().get(rolefighterId);
		
		final ResultItem item = new ResultItem();
		item.execute.attackerid = rolefighterId;
		item.execute.hpconsume = 0;
		item.execute.mpconsume = 0;
		item.execute.operationtype = OperationType.ACTION_SKILL;
		item.execute.operationid = 15;
		
		for (xbean.Fighter xfighter  : battle.getFighters().values())
		{
			if(xfighter.getIshost() != rolexfighter.getIshost())
			{//是敌人
				if(xfighter.getFightertype() == xbean.Fighter.FIGHTER_ROLE)
				{
					int result = ResultType.RESULT_DEATH | ResultType.RESULT_HPCHANGE | ResultType.RESULT_CRITIC;
					battle.getRoundresult().put(xfighter.getBattleindex(), result);
					SubResultItem subitem = new SubResultItem();
					DemoResult dr = new DemoResult();
					dr.targetid = xfighter.getBattleindex();
					dr.targetresult = result;
					dr.hpchange = -12345;
					subitem.resultlist.add(dr);
					item.subresultlist.add(subitem);
				}
				else if(xfighter.getFightertype() == xbean.Fighter.FIGHTER_PET)
				{
					int result = ResultType.RESULT_KICKOUT | ResultType.RESULT_HPCHANGE | ResultType.RESULT_CRITIC;
					battle.getRoundresult().put(xfighter.getBattleindex(), result);
					SubResultItem subitem = new SubResultItem();
					DemoResult dr = new DemoResult();
					dr.targetid = xfighter.getBattleindex();
					dr.targetresult = result;
					dr.hpchange = -12345;
					subitem.resultlist.add(dr);
					item.subresultlist.add(subitem);
				}
				else if(xfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
				{
					int result = ResultType.RESULT_KICKOUT + ResultType.RESULT_HPCHANGE + ResultType.RESULT_CRITIC;
					battle.getRoundresult().put(xfighter.getBattleindex(), result);
					SubResultItem subitem = new SubResultItem();
					DemoResult dr = new DemoResult();
					dr.targetid = xfighter.getBattleindex();
					dr.targetresult = result;
					dr.hpchange = -12345;
					subitem.resultlist.add(dr);
					item.subresultlist.add(subitem);
				}
			}
		}
		script.add(item);
		if(rolexfighter.getIshost())
			battle.setBattleresult(BattleField.BATTLE_WIN);
		else
			battle.setBattleresult(BattleField.BATTLE_LOSE);
		return script;
	}
	
}
