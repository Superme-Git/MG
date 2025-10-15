package fire.pb.battle;

import fire.pb.battle.ai.BattleAIAgent;
import fire.pb.buff.BuffMonsterImpl;
import fire.pb.skill.SkillMonster;
import xbean.BattleInfo;

public class BattleFieldFighter extends Fighter
{

	public BattleFieldFighter(BattleInfo battle, int fighterId)
	{
		
		this.battle = battle;
		this.fighterId = fighterId;
		fighterbean = xbean.Pod.newFighter();
		fighterbean.setBattleindex(0);
		xbean.Monster monsterbean = Monster.createMonster(25107, battle);
		if(monsterbean == null)
			throw new IllegalArgumentException();
		monsterbean.getSkills().clear();
		monsterbean.getEffects().clear();
		monsterbean.getFinalattrs().clear();
		buffagent = new BuffMonsterImpl(monsterbean);
		skillagent = new SkillMonster(monsterbean, 0);
		erole = buffagent.getERole();
		aiagent = new BattleAIAgent(this, battle);
	}

	@Override
	public boolean canAction(int operationType, int operationId)
	{
		return true;
	}
	
	@Override
	public void setDoubleSkill(boolean doubleSkill)
	{
		return;
	}
	@Override
	public boolean isDoubleSkill()
	{
		return false;
	}
}
