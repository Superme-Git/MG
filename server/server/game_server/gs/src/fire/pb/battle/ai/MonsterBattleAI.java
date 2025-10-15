package fire.pb.battle.ai;

import java.util.LinkedList;
import java.util.List;

import fire.pb.util.Parser;

public class MonsterBattleAI
{
	private final int monsterId;
	
	private List<Integer> battleAIs = new LinkedList<Integer>();//此类型怪物必出的AI
	
	private List<Parser.ID2Odds> battleAIOdds = new LinkedList<Parser.ID2Odds>();//怪物按几率出现的AI

	public MonsterBattleAI(int monsterId)
	{
		this.monsterId = monsterId;
	}

	public int getMonsterId()
	{
		return monsterId;
	}

	public List<Integer> getBattleAIs()
	{
		return battleAIs;
	}

	public List<Parser.ID2Odds> getBattleAIOdds()
	{
		return battleAIOdds;
	}
	
}