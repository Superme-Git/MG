package fire.pb.battle;

import java.util.HashMap;
import java.util.Map;

import xbean.BattleInfo;

public class MonsterLevelAndNumStatistics implements IBattleRoundStatistics<Integer, Object> {
	Map<Integer, Object> statisticsMap = new HashMap<Integer, Object>();

	@Override
	public Map<Integer, Object> getStatisticsResult() {
		return statisticsMap;
	}

	@Override
	public EBattleStatsticsType getStatisticsType() {
		return EBattleStatsticsType.MonsterLevelAndNum;
	}
	
	public static final int PLAYERAVGLEVEL = 0;
	public static final int MONSTERLEVEL = 1;
	public static final int BATTLETYPE = 2;
	public static final int BATTLEAREATYPE = 3;

	@Override
	public void statisticsProcess(BattleInfo battle) {
		//statisticsMap.put( PLAYERAVGLEVEL, battle.getHostaveragelevel() );
		statisticsMap.put( PLAYERAVGLEVEL, battle.getBattledatas().get(xbean.BattleInfo.DATA_HOST_ROLE_AVERAGE_LEVEL).intValue());
		Map<Integer, Integer> killed = new HashMap<Integer, Integer>();
		for ( xbean.FailedMonster fm : battle.getFailedmonsters() ) {
			Integer num = killed.get( (int)fm.getLevel() );
			if ( num == null )
				killed.put( (int)fm.getLevel(), 1 );
			else
				killed.put( (int)fm.getLevel(), num + 1 );
		}
		statisticsMap.put(MONSTERLEVEL, killed);
		statisticsMap.put( BATTLETYPE, battle.getBattletype() );
		statisticsMap.put( BATTLEAREATYPE, battle.getAreatype() );
	}

}
