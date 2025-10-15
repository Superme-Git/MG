package fire.pb.buff.single;

import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.battle.ResultType;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;

public class SummonBack extends SingleBuff {
	public SummonBack(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}

	
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		super.init(battleInfo, opfighter, aimfighter, effects);
		
		if (aimfighter.getFightertype() == xbean.Fighter.FIGHTER_PET){
			final int roleIndex = aimfighter.getFighterId() -5;
			if (battleInfo.getFighters().containsKey(roleIndex))
			{
				xbean.Fighter owner = battleInfo.getFighters().get(roleIndex);
				final long roleid = owner.getUniqueid();
				Integer result = battleInfo.getRoundresult().get(aimfighter.getFighterId());
				result = (result == null)? 0 : result;
				battleInfo.getRoundresult().put(aimfighter.getFighterId(), result | ResultType.RESULT_SUMMONBACK);
				int petkey = xtable.Properties.selectFightpetkey(roleid);
				owner.getFightedpets().put(petkey, ResultType.RESULT_SUMMONBACK);
			}
			// 不需要修改战斗宠物设定,把fighter删除就可以了,fighter删除是在handleOutBattle中完成的
		}else if(aimfighter.getFightertype() >= xbean.Fighter.FIGHTER_PARTNER){
			Integer result = battleInfo.getRoundresult().get(aimfighter.getFighterId());
			result = (result == null)? 0 : result;
			battleInfo.getRoundresult().put(aimfighter.getFighterId(), result | ResultType.RESULT_SUMMONBACK);
		}
		demoresult.targetresult |= ResultType.RESULT_SUMMONBACK;
		return demoresult;
	}
	
}
