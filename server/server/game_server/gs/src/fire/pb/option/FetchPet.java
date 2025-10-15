package fire.pb.option;

import java.util.LinkedList;

import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultType;
import fire.pb.pet.PSetFightPetProc;

/**回收宠物*/
public class FetchPet extends BasicOperation {
	
	
	public 	FetchPet(final xbean.BattleInfo battle, final int operator, final int aim){
		super(battle, operator, 0);
	}

	@Override
	public int getType() {return isFailed? OperationType.ACTION_FAILURE : OperationType.ACTION_WITHDRAW;}

	@Override
	public int getOperateID() {return isFailed? BasicOperation.FAIL_FETCH_PET  : 0;}

	@Override
	public int getResultType() {return ResultType.RESULT_SUMMONBACK;}

	@Override
	public LinkedList<DemoResult> getDemoResult(){
		final LinkedList<DemoResult> list = new LinkedList<DemoResult>();
		final DemoResult r = new DemoResult();
		r.targetid = getOperator() + 5;
		r.targetresult = getResultType();
		
		list.add(r);
		return list;
	}
	
	@Override
	public void process(){
		final long roleID = BattleField.getRoleIDByIndex(getBattle(), getOperator());
		if (0 == roleID)
			return;
		final xbean.Properties pro = xtable.Properties.select(roleID);
		if (null == pro)
			return;
		final int petKey = pro.getFightpetkey();
		if (petKey < 1)
			return;
		int petfid = getOperator() + 5;
		if(!BattleField.checkLiveInBattle(getBattle(), petfid))
		{
			isFailed = true;
			return;
		}
		final xbean.Fighter pet = getBattle().getFighters().get(petfid);
		if (null == pet)
			return;

		if (pet.getFightertype() != xbean.Fighter.FIGHTER_PET)
			return;
		xbean.Fighter xfighter = getBattle().getFighters().get(getOperator());

		getBattle().getDecisions().remove(petfid);
		int petresult = getBattle().getRoundresult().get(petfid);
		xfighter.getFightedpets().put(petKey, petresult |ResultType.RESULT_SUMMONBACK);
		getBattle().getRoundresult().put(petfid, petresult | ResultType.RESULT_SUMMONBACK);
		for (BattleEndHandler handler : getBattle().getBattleendhandlers())
		{
			try
			{
				handler.handleOutBattle(petfid,petKey); 
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		new PSetFightPetProc(roleID, -1, false, true).call();
	}
	
	public boolean checkEffective(){
		return checkOperator();//收回宠物只检查操作者即可
	}
	
	public String toString(){return getOperator() + "\t召回战斗宠物";}
	
	@Override
	public boolean canChangeAim(){return false;}
	
}
