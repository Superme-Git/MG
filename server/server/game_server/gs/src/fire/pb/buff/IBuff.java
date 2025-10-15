package fire.pb.buff;

import java.util.Map;

import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import xbean.BattleInfo;

public interface IBuff
{
	public int getId();
	public IBuff copy();
	public Result impact(BuffAgent agent);
	public DemoResult impact(BattleInfo battleInfo, Fighter opfighter, Fighter aimfighter, FightSkill battleskill, Map<Integer, fire.script.JavaScript> effects);
}
