package fire.pb.battle;

public class BattleStartNotify
{
	private xbean.BattleInfo battleInfo;

	public BattleStartNotify(long battleId)
	{
		
	}
	
	public xbean.BattleInfo getBattleInfo()
	{
		return battleInfo;
	}

	public void setBattleInfo(xbean.BattleInfo battleInfo)
	{
		this.battleInfo = battleInfo;
	}
}
