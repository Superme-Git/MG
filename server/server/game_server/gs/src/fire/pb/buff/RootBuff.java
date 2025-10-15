package fire.pb.buff;

import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.script.JavaScript;
import xbean.BattleInfo;

public class RootBuff implements IBuff
{
	@Override
	public int getId(){return -1;}	
	@Override
	public IBuff copy()	{return null;}
	@Override
	public Result impact(BuffAgent agent){return null;}
	@Override
	public DemoResult impact(BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter,FightSkill battleskill, Map<Integer, JavaScript> effects)	{return null;}
}
