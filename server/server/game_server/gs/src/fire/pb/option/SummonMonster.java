package fire.pb.option;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.battle.BattleField;
import fire.pb.battle.FighterInfo;
import fire.pb.battle.Monster;
import fire.pb.battle.OperationType;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.util.Parser.ID2Odds;

/**召唤怪物*/
public class SummonMonster extends BasicOperation {

	final private Map<Integer, ID2Odds> summons;
	final private boolean isInstant;
	List<FighterInfo> newfighters = new LinkedList<FighterInfo>();
	
	public SummonMonster(final xbean.BattleInfo battle, final int operator, final Map<Integer, ID2Odds> summons,boolean isInstant){
		super(battle, operator, operator);
		this.summons = summons;
		this.isInstant = isInstant;
	}
	
	
	public ResultItem getResultItem(){
		if(newfighters.size() != 0)
		{
			final ResultItem item = new ResultItem();
			if(BattleField.checkOutBattle(getBattle(), getOperator()))
				item.execute.attackerid = 0;
			else
				item.execute.attackerid = getOperator();
			item.execute.hpconsume = getHPconsume();
			item.execute.mpconsume = getMPconsume();
			item.execute.operationtype = getType();//表现为战场召唤还是怪物召唤
			item.execute.operationid = getOperateID();
			
			item.subresultlist = getSubResult();
			item.newfighter.addAll(newfighters);
			return item;
		}
		else
			return null;
		
	}
	
	public boolean checkOperator()
	{
		if(!isInstant && BattleField.checkOutBattle(getBattle(), getOperator()))
			return false;//非瞬时召唤，如果怪物已经不在战斗中了，不能召唤
		return true;
	}

	@Override
	public int getResultType() {return ResultType.RESULT_SUMMONPET;}

	@Override
	public int getType() {return (isInstant)? OperationType.ACTION_SUMMON_INSTANT:OperationType.ACTION_SUMMON;}

	@Override
	public void process(){
		
		if(opfighter == null)
			return;
		for(Map.Entry<Integer, ID2Odds> monsterEntry : summons.entrySet())
		{
			int fighterId = monsterEntry.getKey();
			boolean ishost = BattleField.isHost(fighterId);
			//验证该fighterId是否已经有战斗者占用
			fighterId = BattleField.getAvailableFighterId(getBattle(), fighterId, ishost);
			if(fighterId!=0)
			{
				int form = (ishost)?getBattle().getHostform():getBattle().getGuestform();
				int formLevel = ishost ? getBattle().getHostformlevel() : getBattle().getGuestformlevel();
				//有空闲位置，可以添加
				if(PNewBattle.enterBattleMonster(getBattle(), new Monster(monsterEntry.getValue().getRandomId()),fighterId,ishost,form,formLevel))
				{
					newfighters.add(BattleField.getFighterInfo(getBattle().getFighters().get(fighterId), getBattle()));
				}
			}
		}
		
	}
	
	public String toString(){
		return getOperator() + "\t召唤怪物\t" + summons;
	}
	
	
	@Override
	protected boolean checkAim()
	{
		return true;
	}


	@Override
	public boolean canChangeAim()
	{
		return false;
	}


	@Override
	public int getOperateID()
	{
		return 0;
	}
}
