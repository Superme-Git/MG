package fire.pb.buff.single;

import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

/**鬼魂状态复活*/
public class GhostRelive extends Relive
{
	public GhostRelive(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	/**宠物鬼魂到回合后复活*/
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{

		if (aimfighter.getFighterBean().getFightertype() == xbean.Fighter.FIGHTER_PET ||aimfighter.getFighterBean().getFightertype() >= xbean.Fighter.FIGHTER_PARTNER)
		{//宠物或怪物
			ConstantlyBuff ghostbuff = aimfighter.getBuffAgent().getBuff(BuffConstant.CONTINUAL_GHOST);
			if(ghostbuff == null || ghostbuff.getRound()!=1)
				return null;//没有鬼魂buff或者鬼魂buff不是最后一回合
			init(battleInfo, opfighter, aimfighter, effects);

			// 鬼魂BUFF自己计数到后清除，这里不清除
			demoresult.targetid = aimfighter.getFighterId();
			demoresult.targetresult |= ResultType.RESULT_RELIVE;
			//满血满蓝复活
			aimfighter.getRoundBuffResult().getChangedAttrs().putAll(aimfighter.getEffectRole().fullHp());
			demoresult.hpchange = aimfighter.getEffectRole().getMaxHp();
			demoresult.targetresult |= ResultType.RESULT_HPCHANGE;
			
			aimfighter.getRoundBuffResult().getChangedAttrs().putAll(aimfighter.getEffectRole().fullMp());
			demoresult.mpchange = aimfighter.getEffectRole().getMaxMp();
			demoresult.targetresult |= ResultType.RESULT_MPCHANGE;

			// 复活特殊：需要清除先前的RoundResult，重新设置一下
			battleInfo.getRoundresult().put(aimfighter.getFighterId(), demoresult.targetresult);
			return demoresult;
		} else
			return null;

	}

}
