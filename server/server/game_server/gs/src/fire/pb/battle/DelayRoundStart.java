package fire.pb.battle;

import java.util.List;

import com.locojoy.base.Runnable;


public class DelayRoundStart extends Runnable
{
	private final long battleID;
	private List<Long> liveRoleids;
	DelayRoundStart(final long battleID,List<Long> liveRoleids){
		this.battleID = battleID;
		this.liveRoleids = liveRoleids;
	}
	@Override
	public void run()
	{
		BattleField.logger.debug("战斗开始延时结束");
		if(xtable.Battle.selectBattleid(battleID) != null)
			new PPlayScript(battleID).submit();
//			new PRoundStart(battleID,liveRoleids).submit();
	}
}
