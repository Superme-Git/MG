package fire.pb.option;

import fire.pb.DataInit;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SSendPetInitAttrs;
import fire.pb.pet.PSetFightPetProc;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;

/**召宠*/
public class SummonPet extends BasicOperation {

	public SummonPet(final xbean.BattleInfo battle, final int operator, final int petID){
		super(battle, operator, operator);
		this.petKey = petID;
	}
	
	@Override
	public ResultItem getResultItem(){
		resultItem.execute.attackerid = getOperator();
		resultItem.execute.hpconsume = getHPconsume();
		resultItem.execute.mpconsume = getMPconsume();
		resultItem.execute.operationtype = getType();
		resultItem.execute.operationid = getOperateID();
		resultItem.subresultlist = getSubResult();
		processActiveHidden();
		
		final xbean.Fighter f = getBattle().getFighters().get(getOperator() + 5);
		if(null != f)
		{
			//SubResultItem subitem = resultItem.subresultlist.get(0); 
			resultItem.newfighter.add(BattleField.getFighterInfo(f,getBattle()));
		}
		return resultItem;
	}

	@Override
	public int getType() {return isFailed? OperationType.ACTION_FAILURE : OperationType.ACTION_SUMMON;}
	
	@Override
	public int getOperateID() {return isFailed? BasicOperation.FAIL_SUMMON_PET : petKey;}

	@Override
	public int getResultType() {return ResultType.RESULT_SUMMONPET;}



	@Override
	public void process(){
		final long roleID = BattleField.getRoleIDByIndex(getBattle(), getOperator());
		if(0 == roleID)
		{
			isFailed = true;
			return;
		}
		final int petIndex = getOperator() + 5;
		
		xbean.Fighter xfighter = getBattle().getFighters().get(getOperator());
		if(xfighter == null)
		{
			isFailed = true;
			return;
		}
		if(xfighter.getFightedpets().containsKey(petKey))
		{
			isFailed = true;
			return;//宠物已经在这场战斗出战过了
		}
		
		final PetColumn col = new PetColumn(roleID, PetColumnTypes.PET,false);
		final Pet pet = col.getPet(petKey);
		if(null == pet)
		{
			isFailed = true;
			return;
		}
		
		boolean isForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(pet.getPetAttr().getId());
		if(pet.getLife() < DataInit.PET_FIGHT_LIFE_LIMIT && !isForever)
		{
			isFailed = true;
			return;//宠物生命小于50，不出战
		}
		
		if(opfighter.getEffectRole().getLevel() < pet.getUseLevel())
		{
			isFailed = true;
			return;//没达到使用等级，不出战
		}
		
		final xbean.Properties pro = xtable.Properties.select(roleID);
		if(null == pro)
		{
			isFailed = true;
			return;
		}
		
		final int oldpetKey = pro.getFightpetkey();
		
		// 设置宠物的出战状态
		boolean setresult = new PSetFightPetProc(roleID, this.petKey, true, true).call();
		if(!setresult)
		{
			isFailed = true;
			return;
		}
		
		if(this.getBattle().getFighters().containsKey(petIndex))//原来有宠物在
		{
			Fighter oldpetfighter = this.getBattle().getFighterobjects().get(petIndex);
			if(oldpetfighter== null)
			{
				isFailed = true;
				return;
			}
			/*Integer oldpetresult = getBattle().getRoundresult().get(petIndex);
			if(oldpetresult!=null && (oldpetresult & ResultType.RESULT_GHOST) != 0)
			{
				isFailed = true;
				return;
			}*/
			
			xfighter.getFightedpets().put(oldpetKey, getBattle().getRoundresult().get(petIndex)|ResultType.RESULT_SUMMONBACK);
			for (BattleEndHandler handler : getBattle().getBattleendhandlers())
			{
				try
				{
					handler.handleOutBattle(petIndex,oldpetKey); 
					oldpetfighter.getBuffAgent().removeBuffsWhileBattleEndWithSP();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
//			if (getBattle().getBattletype() == BattleType.BATTLE_PVP)
//				if (this.getBattle().getFighters().containsKey(petIndex))
//					QiecuoEndHandler.recoverPetHpMPWithSP(getBattle(), petIndex,oldpetKey);
		}
		
		if(!PNewBattle.enterBattlePet(getBattle(), roleID, getOperator() + 5, opfighter.getFighterBean().getIshost()))
			new PSetFightPetProc(roleID, -1, false, true).call();
		else
		{//成功的话发送宠物的初始二级属性
			xbean.Fighter petfighter = getBattle().getFighters().get(getOperator() + 5);
			if(petfighter != null)
			{
				SSendPetInitAttrs spetattr = new SSendPetInitAttrs();
				spetattr.petinitattrs.putAll(petfighter.getInitattrs());
				mkdb.Procedure.psendWhileCommit(roleID, spetattr);
			}
		}
		//new PSummonPet(getBattle().getBattleid(), roleID, petID, getOperator()+5 ).call();
	}
	
	public String toString(){
		return getOperator() + "\t召唤宠物\t" + petKey;
	}
	
	
	private int petKey;

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
}
