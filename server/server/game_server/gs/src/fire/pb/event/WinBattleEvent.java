package fire.pb.event;

import java.util.List;
import java.util.Map;

public class WinBattleEvent implements Event {
	private final long roleid;
	private final Map<fire.pb.battle.EBattleStatsticsType, fire.pb.battle.IBattleRoundStatistics<?,?> > statisticMap =
		new java.util.HashMap<fire.pb.battle.EBattleStatsticsType, fire.pb.battle.IBattleRoundStatistics<?,?> >();
	public WinBattleEvent( final long roleid, final List<fire.pb.battle.IBattleRoundStatistics<?,?> > statisticList ) {
		for ( fire.pb.battle.IBattleRoundStatistics<?,?> statistic :statisticList ) {
			statisticMap.put( statistic.getStatisticsType(), statistic );
		}
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() { return roleid; }
	
	public Map<fire.pb.battle.EBattleStatsticsType, fire.pb.battle.IBattleRoundStatistics<?,?> > getStatisticMap() {
		return statisticMap;
	}
}
