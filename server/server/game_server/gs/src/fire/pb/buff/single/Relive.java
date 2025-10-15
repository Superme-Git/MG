package fire.pb.buff.single;

import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.Module;
import fire.pb.skill.Result;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

/**复活*/
public class Relive extends Cure
{
	public Relive(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, FightSkill battleskill,Map<Integer, JavaScript> effects)
	{
		//复活的条件：倒地死亡
		if (aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH))
		{
			init(battleInfo, opfighter, aimfighter, effects);
			
			if(aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DIE_FORBID))
			{
				return null;//如果有灵魂禁锢，需要消耗
			}
			//驱散死亡BUFF
			Result result = aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_DEATH);
			if(!result.isSuccess())
				return null;
			demoresult.targetid = aimfighter.getFighterId();
			demoresult.targetresult |= ResultType.RESULT_RELIVE;
			if(opfighter.getFighterId() != aimfighter.getFighterId())//为妙手仁心成就而存
				opfighter.setSaveRoleNum(opfighter.getSaveRoleNum() + 1 );
			//治疗
			if(effects != null)
				cure();
/*			int nsp = aimfighter.getEffectRole().getSp();
			aimfighter.getEffectRole().setSp(0);
			demoresult.spchange = -nsp;*/
			//转换
			fire.pb.buff.Module.updateDemoResultFromResult(demoresult, result, aimfighter);
			//验证复活目标的血，至少为1
			if(aimfighter.getEffectRole().getHp() == 0)
			{
				aimfighter.attachHpChange(1);
				demoresult.hpchange = 1;
			}
			end();
			//复活特殊：需要清除先前的RoundResult，重新设置一下
			battleInfo.getRoundresult().put(aimfighter.getFighterId(), demoresult.targetresult);

			if(aimfighter.isPartner() && aimfighter.getSkillAgent().hasSkill(SkillConstant.BATTLE_HUOBAN_FERITY_INSTINCT_SKILL)
					&&!aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_FERITY_INSTINCT_FLAG))
			{
				//	野兽本能(预留）标记用
				result = aimfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_FERITY_INSTINCT_FLAG, -1, 0);
				Module.updateDemoResultFromResult(demoresult, result, aimfighter);// 死亡
	
				//	野兽本能(预留）免疫用 
				result = aimfighter.getBuffAgent().addCBuff(BuffConstant.CONTINUAL_FERITY_INSTINCT_IMMUNE, 1, 0);
				Module.updateDemoResultFromResult(demoresult, result, aimfighter);// 死亡
			}
			if(aimfighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_RELIVE)&&aimfighter!=opfighter)
			{//强力母牛技能还没起效。去除那次复活机会
				aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_FLAG);
				aimfighter.getBuffAgent().removeCBuff(BuffConstant.CONTINUAL_ELEMENT_AWKEN_RELIVE);
			}


			return demoresult;
		}
		else
			return null;
		
	}
	
}
