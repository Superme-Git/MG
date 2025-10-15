package fire.pb.battle;

public class BattleEndNotify
{
	private xbean.BattleInfo battleInfo;

	public BattleEndNotify(xbean.BattleInfo battleInfo)
	{
		this.battleInfo = battleInfo;
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
