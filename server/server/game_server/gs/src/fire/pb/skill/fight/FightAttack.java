package fire.pb.skill.fight;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.attr.FightAttrType;
import fire.pb.battle.BattleEnviroment;
import fire.pb.battle.BattleField;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.OperationType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.IBuff;
import fire.pb.buff.single.SingleBuff;
import fire.pb.util.Misc;
import xbean.BattleInfo;

public class FightAttack extends FightSkill
{

	private int maxComboNum = 1;
	private int maxExtraNum = 2;
	
	private int comboNum = 0;
	private int extraNum = 0;

	public FightAttack(final xbean.BattleInfo battle, final int operator, final int aim, int skillId,int type)
	{
		super(battle, operator, aim , skillId , type);
	}
	
	@Override
	protected boolean checkSkill()
	{
		return true;
	}
	
	@Override
	protected List<DemoResult> processBuff(IBuff buff,BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, Map<Integer, fire.script.JavaScript> effects)
	{
		if(buff.getClass().getName() == "fire.pb.buff.instant.EPFixedInjure")
		{
			List<DemoResult> demos = new LinkedList<DemoResult>();			
			DemoResult lastdemo = buff.impact(getBattle(),opfighter, aimfighter, this,effects);
			demos.add(lastdemo);			
			return demos; 
		}
		if (opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS)||opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
		{
			if(opfighter == aimfighter|| aimfighter.getFighterId() == 0)
			{
				int fighterid = 0;
				if(opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_CHAOS_ADV))
				{
					fighterid = BattleField.getRandLiveFriend(battle, opfighter.getFighterId());
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandLiveFriend()找到的目标是figterid为:" + fighterid + " 的战斗者");}
				}
				else
				{
					fighterid = BattleField.getRandTargetByType(battle, opfighter.getFighterId(),4606,true,true, null);
					if(opfighter.getFighterId() == fighterid)
						fighterid = BattleField.getRandTargetByType(battle, opfighter.getFighterId(),4606,true,true, null);
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击getRandTargetByType()找到的目标是figterid为:" + fighterid + " 的战斗者");}
				}

				if (fighterid != 0)
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击装换目标。目标是figterid为:" + fighterid + " 的战斗者");}
					return ChaosPhyAttack(buff, battleInfo, opfighter, battleInfo.getFighterobjects().get(fighterid), effects);
				}
				else
				{
					if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击找不到目标失败。 ");}
					return null;
				}
			}
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("混乱攻击目标是figterid为:" + aimfighter.getFighterId() + " 的战斗者");}
			return ChaosPhyAttack(buff, battleInfo, opfighter, aimfighter, effects);
		}
		else
			return nomarlPhyAttack(buff, battleInfo, opfighter, aimfighter, effects);
	}
	
	//混攻
	private List<DemoResult> ChaosPhyAttack(IBuff buff,BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, Map<Integer, fire.script.JavaScript> effects)
	{
		List<DemoResult> demos = new LinkedList<DemoResult>();
		
		//第一次攻击
		DemoResult lastdemo = buff.impact(getBattle(),opfighter, aimfighter, this,effects);
		demos.add(lastdemo);
		if(lastdemo.assisterid != 0)
		{//合击，流程结束
			return demos;
		}
		//处理溅射
		List<DemoResult> diffusedemos = ((fire.pb.buff.single.PhysicalInjure)buff).handleDiffuse();
		if(diffusedemos.size() > 0)
			demos.addAll(diffusedemos);		
		//反击
		if(skillConfig.canAtackBack() && !opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW))
		{
			float attackbackrate= aimfighter.getEffectRole().getAttrById(FightAttrType.ATTACK_BACK_RATE)-opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE);
			if(BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid))
			{
				float frate = (float) Math.random();
				//BattleField.logger.debug(" frate（随机值） "+ frate + "  attackbackrate（反击几率）: "+ attackbackrate);		
				if(frate <attackbackrate)
				{
					lastdemo = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE).impact(getBattle(), aimfighter,opfighter, null,effects);
					lastdemo.targetid = aimfighter.getFighterId();
					demos.add(lastdemo);
				}
			}
		}
		//如果被反击或反震，流程结束
		return demos;
	}
	
	
	
	// 普攻
	private List<DemoResult> nomarlPhyAttack(IBuff buff,BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, Map<Integer, fire.script.JavaScript> effects)
	{
		maxExtraNum = (int)opfighter.getEffectRole().getAttrById(FightAttrType.EXTRA_ATTACK_COUNT);
		maxComboNum = (int)opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_COUNT);
		
		List<DemoResult> demos = new LinkedList<DemoResult>();
		
		DemoResult lastdemo = buff.impact(getBattle(),opfighter, aimfighter, this,effects);
		demos.add(lastdemo);
		if(lastdemo.assisterid != 0)
		{
			return demos;
		}
		//处理溅射
		List<DemoResult> diffusedemos = ((fire.pb.buff.single.PhysicalInjure)buff).handleDiffuse();
		if(diffusedemos.size() > 0)
			demos.addAll(diffusedemos);		
		//反击
		float attackbackrate= aimfighter.getEffectRole().getAttrById(FightAttrType.ATTACK_BACK_RATE)-opfighter.getEffectRole().getAttrById(FightAttrType.ANTI_ATTACK_BACK_RATE);
		if(skillConfig.canAtackBack() && !opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW))
		{
			if(BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid))
			{
				float frate = (float) Math.random();
				//BattleField.logger.debug(" frate（随机值） "+ frate + "  attackbackrate（反击几率）: "+ attackbackrate);		
				if(frate <attackbackrate)
				{
					SingleBuff instantBuff= fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE);
					lastdemo = instantBuff.impact(getBattle(),aimfighter, opfighter,null,effects);
					lastdemo.targetid = aimfighter.getFighterId();
					demos.add(lastdemo);
				}
			}
			
		}
		//连击
		while(BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid)&&BattleField.checkLiveInBattle(battleInfo, opfighter.getFighterId()))
		{
			if(triggerComboAttack() && comboNum < maxComboNum)
			{
				SingleBuff instantBuff= fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_COMBO_PHY_DAMAGE);
				lastdemo = instantBuff.impact(getBattle(),opfighter, aimfighter, null,effects);
				comboNum++;
				demos.add(lastdemo);
				if(!BattleField.checkLiveInBattle(battleInfo, getOperator()))
					return demos;
				
				if(skillConfig.canAtackBack() && !opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW))
				{
					if(BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid))
					{
						float frate = (float) Math.random();
						//BattleField.logger.debug(" frate（随机值） "+ frate + "  attackbackrate（反击几率）: "+ attackbackrate);		
						if(frate <attackbackrate)
						{
							lastdemo = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE).impact(getBattle(),aimfighter, opfighter,  null,effects);
							lastdemo.targetid = aimfighter.getFighterId();
							demos.add(lastdemo);
						}
					}
				}
				
				if(comboNum >= maxComboNum)
					break;
				if(lastdemo.attackback !=0 || lastdemo.returnhurt !=0)
					return demos;
			}
			else
				return demos;
		}
		//追击
		while(!BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid))
		{
			if(triggerExtraAttack() && extraNum < maxExtraNum)
			{
				final java.util.List<Integer> enemies = BattleField.getEnemies(battle, getOperator());
				if (enemies.size() == 0)
					return demos;
				Fighter fighter = null;
				Misc.randomlizeList(enemies);
				for(int enemy : enemies)
				{
					fighter = battle.getFighterobjects().get(enemy);
					List<Integer> buffIDs = new ArrayList<Integer>();
					buffIDs.add(BuffConstant.INSTANT_EXTRA_PHY_DAMAGE);					
					if(checkAimFighter(fighter, buffIDs, true,true))
						break;
					else
						fighter = null;
				}
				if(fighter == null)
					return demos;
				battle.getEngine().setAimFighter(fighter,battle.getEnvironment() == BattleEnviroment.NIGHT);// 设置被追击者相关参数
				lastdemo = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_EXTRA_PHY_DAMAGE).impact(getBattle(),opfighter, fighter,null, effects);
				extraNum++;
				demos.add(lastdemo);
				if(!BattleField.checkLiveInBattle(battleInfo, getOperator()))
					return demos;
				
				if(skillConfig.canAtackBack() && !opfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_BEHUNTER_BOW))
				{
					if(BattleField.checkLiveInBattle(battleInfo, lastdemo.targetid))
					{
						float frate = (float) Math.random();
						if(frate <attackbackrate)
						{
							lastdemo = fire.pb.buff.Module.getInstance().createInstantBuff(BuffConstant.INSTANT_ATACK_BACK_PHY_DAMAGE).impact(getBattle(), fighter,opfighter, null,effects);
							lastdemo.targetid = aimfighter.getFighterId();
							demos.add(lastdemo);
						}
					}				
				}		
				if(extraNum >= maxExtraNum)
					return demos;
			}
			else
				return demos;
		}
		return demos;
	}
	
	private boolean triggerComboAttack()
	{
		float comboRate = opfighter.getEffectRole().getAttrById(FightAttrType.PHYSIC_COMBO_ATTACK_RATE);
		if(Math.random() < comboRate)
			return true;
		else
			return false;
	}
	
	private boolean triggerExtraAttack()
	{
		float extraRate = opfighter.getEffectRole().getAttrById(FightAttrType.EXTRA_ATTACK_RATE);
		if(Math.random() < extraRate)
			return true;
		else
			return false;
	}
	
	// 命中检查
	@Override
	protected boolean isHit(Double hitRate,Fighter curAim, int buffId)
	{
		if(opfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_ROLE)
			return true;
		boolean ishit = (hitRate >= Math.random());
		sendHitRateForDebug(hitRate, curAim, buffId, ishit);
		return ishit;
	}
	
	@Override
	public int getType() {return OperationType.ACTION_ATTACK;}

	public String toString(){return getOperator() + "\t攻击\t" + getAim();}
}
