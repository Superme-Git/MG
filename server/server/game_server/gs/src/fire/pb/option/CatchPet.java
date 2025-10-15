package fire.pb.option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fire.pb.battle.BattleField;
import fire.pb.battle.Fighter;
import fire.pb.battle.Monster;
import fire.pb.battle.OperationType;
import fire.pb.battle.ResultItem;
import fire.pb.battle.ResultType;
import fire.pb.battle.SCatchRate;
import fire.pb.main.ConfigManager;
import fire.pb.npc.MonsterConfig;
import fire.pb.pet.Module;
import fire.pb.pet.PAddPetByPetID;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetTypeEnum;
import fire.pb.skill.SkillConstant;
import fire.pb.talk.MessageMgr;

/**捕捉*/
public class CatchPet extends BasicOperation {
	
	public CatchPet(final xbean.BattleInfo battle, final int operator, final int aim){
		super(battle, operator, aim);
	}
	
	@Override
	public int getType() {return (isFailed)? OperationType.ACTION_FAILURE_NO_WONDER : OperationType.ACTION_CATHCH;}
	
	@Override
	public int getOperateID() {return (isFailed)? failreason :0;}

	
	private int seizeResult = 0;
	@Override
	public int getResultType() { return seizeResult;}

	protected int mpconsume = 0;

	@Override
	public void process(){
		final long roleID = BattleField.getRoleIDByIndex(getBattle(), getOperator());
		if(0 == roleID) 
			return;
		
		
		final Fighter aimfighter = getBattle().getFighterobjects().get(getAim());
		if(null == aimfighter)
		{
			isFailed = true;
			failreason = 142937;
			return;
		}
		
		if(aimfighter.getFightertype() < xbean.Fighter.FIGHTER_PARTNER)
		{
			isFailed = true;
			failreason = 142937;
			return;
		}
		

		
		
		final int monsterBaseID = (int)aimfighter.getFighterBean().getUniqueid();

		/*float catchRate = 0;
		
		int petID = 0;
		int colorRateId = -1;
		boolean isbind = false;*/
		
		MonsterConfig cfg = Monster.getMonsterConfig(monsterBaseID);
		if (cfg == null || cfg.canCatch == 0)
		{
			isFailed = true;
			failreason = 142937;
			return;
		}
		int petID = cfg.getPet();
		boolean isbind = (cfg.orbinding == 1);
		float catchRate = ((float) cfg.catchRate) / 1000f;
		if(0 == petID)
		{
			isFailed = true;
			failreason = 142937;
			return;
		}
		//判断携带等级
		int takelevel = fire.pb.pet.Module.getInstance().getPetTakeLevel(petID);
		if(takelevel > opfighter.getEffectRole().getLevel())
		{
			//参战等级不足提示
			isFailed = true;
			failreason = 141685;
			fire.pb.talk.MessageMgr.psendMsgNotify(roleID, 150079, Arrays.asList(cfg.getName(),takelevel+""));
			return;
		}
		
		PetColumn petcol = new PetColumn(opfighter.getFighterBean().getUniqueid(), PetColumnTypes.PET, false);
		if(petcol.size() >= petcol.getCapacity())
		{
			//宠物栏满了
			isFailed = true;
			failreason = 160198;
			MessageMgr.sendMsgNotify(roleID, 160198, null);
			return;
		}
		
/*		//消耗蓝   策划要求不在耗蓝了liuxinhua edit 2015-12-3
		mpconsume = -(aimfighter.getEffectRole().getLevel() *3 +10);
		if(opfighter.getEffectRole().getMp() < -mpconsume)
		{
			//蓝不足提示
			isFailed = true;
			failreason = 141633;
			return;
		}
		opfighter.attachMpChange(mpconsume);*/
		//计算捕捉几率
		SCatchRate scatchrate = null;
		float curperc = aimfighter.getEffectRole().getHpPercent() * 100;
		for(SCatchRate rate : getSCatchRate().values())
		{
			if(curperc > rate.getLowerlimit() && curperc <= rate.getUpperlimit())
			{
				scatchrate = rate;
				break;
			}
		}
		if(scatchrate != null)
		{
			catchRate = (float)(catchRate * scatchrate.getCatchfactor() + scatchrate.getCatchconst());
		}
		if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_HUNTER_EXPERT))
			catchRate+=0.2;
		//判断捕捉是否成功
		float frate = (float) Math.random();
//		BattleField.logger.debug(" frate（随机值） "+ frate + "  catchRate（捕捉几率）: "+ catchRate);		
		if(frate > catchRate)
		{
			return;//捕捉不成功
		}
		xbean.Monster xmonster = getBattle().getMonsters().get(getAim());
		List<Integer> skillids = new ArrayList<Integer>();
		for(xbean.MonsterSkill mskill : xmonster.getSkills())
			skillids.add(mskill.getId());
		
		int lv = 0;
//		if(xmonster.getType() != Monster.MONSTER_BABY)
//		{//野宠等级要下浮
//			lv = Math.max(1, (int)( xmonster.getLevel() *(0.7 + Math.random() * 0.1) ) );
//		}
		PetAttr petAttr = Module.getInstance().getPetManager().getAttr(petID);
		if (petAttr != null) {
			lv = petAttr.getInitlevel();
		}

//		final int type = (xmonster.getType() == Monster.MONSTER_BABY)? PetTypeEnum.BABY : PetTypeEnum.WILD;
		int type = PetTypeEnum.BABY;
		if (petAttr != null) {
			type = petAttr.getKind();
		}
		
		//计算颜色
		int starId = PetColour.WHITE; // 现在这个属性统一白色 [1/16/2016 XGM]
		boolean success = new PAddPetByPetID(roleID, petID, lv,  PetColumnTypes.PET, type, skillids, PetColumn.ADD_REASON_CATCH, starId, isbind).call();
		if(!success)
		{
			isFailed = true;
			return;
		}
		
		seizeResult = ResultType.RESULT_SEIZE;
		int aimresult = getBattle().getRoundresult().get(getAim());
		aimresult |= ResultType.RESULT_SEIZE;
		getBattle().getRoundresult().put(getAim(), aimresult);
	}
	
	@Override
	public ResultItem getResultItem(){
		
		if(resultItem != null&&isFailed == true)
		{
			resultItem.execute.msgid = failreason;	
		}
		return super.getResultItem();
	}
		
	int getMonserBaseID(){
		final xbean.Fighter f = getBattle().getFighters().get(getAim());
		if(null == f)
			return 0;
		
		if(f.getFightertype() < xbean.Fighter.FIGHTER_PARTNER)
			return 0;
		
		return (int)f.getUniqueid();
	}
	
	public String toString(){
		return getOperator() + "\t捕捉\t" + getAim();
	}
	
	@Override
	public int getMPconsume()
	{
		return mpconsume;
	}
	
	private static Map<Integer,SCatchRate> getSCatchRate()
	{
		
		return 	ConfigManager.getInstance().getConf(SCatchRate.class);
	}
	
	@Override
	public boolean canChangeAim(){return true;}	// 捕捉宠物换对象
}
