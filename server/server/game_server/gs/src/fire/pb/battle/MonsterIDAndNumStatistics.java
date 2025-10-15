package fire.pb.battle;

import java.util.HashMap;
import java.util.Map;

import xbean.BattleInfo;

public class MonsterIDAndNumStatistics implements IBattleRoundStatistics<Integer, Object> {
//	private int area;
	private final Map<Integer, Object> statisticsMap = new HashMap<Integer, Object>();
	@Override
	public Map<Integer, Object> getStatisticsResult() {
		return statisticsMap;
	}
	
	public final static int AREA = 0;
	public final static int MONSTERS = 1;
	
	@Override
	public EBattleStatsticsType getStatisticsType() {
		return EBattleStatsticsType.MonsterIDAndNum;
	}

	@Override
	public void statisticsProcess(BattleInfo battle) {
		statisticsMap.put( AREA, battle.getAreaconf() );
		Map<Integer, Integer> monsters = new HashMap<Integer, Integer>();
		for ( xbean.FailedMonster fm : battle.getFailedmonsters() ) {
			Integer num = monsters.get( (int)fm.getMonsterid() );
			if ( num == null )
				monsters.put( (int)fm.getMonsterid(), 1 );
			else
				monsters.put( (int)fm.getMonsterid(), num + 1 );
		}
		statisticsMap.put( MONSTERS, monsters );
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Integer> getMonsters() {
		Object monstersobj = statisticsMap.get( MONSTERS );
		if ( monstersobj == null )
			return null;
		return (Map<Integer, Integer>)monstersobj;
	}
	
	public int getArea() {
		Object areasobj = statisticsMap.get( AREA );
		if ( areasobj == null )
			return 0;
		return (Integer)areasobj;
	}

}
