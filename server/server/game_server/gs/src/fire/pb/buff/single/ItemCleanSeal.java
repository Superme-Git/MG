package fire.pb.buff.single;

import java.util.HashMap;
import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;
import xbean.BattleInfo;

public class ItemCleanSeal extends CleanBuff
{
	public ItemCleanSeal(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	@Override
	public DemoResult impact(BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		DemoResult demo = super.impact(battleInfo, opfighter, aimfighter, battleskill, effects);
		
		for(fire.pb.battle.DemoBuff demobuff : demo.demobuffs)
		{
			if(demobuff.round == -1)
				return demo;
		}
		
		if(aimfighter == null)
			return demo;
		
		ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(BuffConstant.CONTINUAL_SLEEP);
		if(buff!=null)
		{
			buff.setRound(2);
			DemoResult sleepdemo = buff.impact(battleInfo, opfighter, aimfighter, battleskill, new HashMap<Integer, JavaScript>());
			demo.demobuffs.addAll(sleepdemo.demobuffs);
		}		
		return demo;
	}
}
