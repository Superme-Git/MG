package fire.pb.event;

public class BattleEndEvent implements Event {
	
	private final xbean.BattleResult br = xbean.Pod.newBattleResultData();
	private final long roleid;
	public BattleEndEvent( final long roleid, final xbean.BattleInfo bi ) {
		br.setAreaconf( bi.getAreaconf() );
		br.setAreatype( bi.getAreatype() );
		br.setBattleid( bi.getBattleid() );
		br.setBattleresult( bi.getBattleresult() );
		br.setBattletype( bi.getBattletype() );
		br.setConfigid( bi.getConfigid() );
		br.setEnvironment( bi.getEnvironment() );
		br.setGuestform( bi.getGuestform() );
		br.setHostform( bi.getHostform() );
		br.setMonsterid( bi.getMonsterid() );
		br.setMonstertype( bi.getMonstertype() );
		br.setPhase( bi.getPhase() );
		br.setRound( bi.getRound() );
		br.setRoundtime( bi.getRoundtime() );
		br.setScriptplaytime( bi.getScriptplaytime() );
		br.getAudience().putAll( bi.getAudienceAsData() );
		//br.getBattleais().putAll( bi.getBattleaisAsData() ); 先注掉，以后有用再说
		br.getBattledatas().putAll( bi.getBattledatasAsData() );
		br.getDecisions().putAll( bi.getDecisionsAsData() );
		br.getFailedmonsters().addAll( bi.getFailedmonstersAsData() );
		br.getFeedbacks().addAll( bi.getFeedbacksAsData() );
		br.getFighters().putAll( bi.getFightersAsData() );
		br.getInifighters().putAll( bi.getInifightersAsData() );
		br.getMonsters().putAll( bi.getMonstersAsData() );
		br.getProtectormap().putAll( bi.getProtectormapAsData() );
		br.getRoleids().putAll( bi.getRoleidsAsData() );
		br.getRoundresult().putAll( bi.getRoundresultAsData() );
		br.getSkipeddeadfighters().addAll( bi.getSkipeddeadfightersAsData() );
		br.getWatchers().putAll( bi.getWatchersAsData() );
		
		this.roleid = roleid;
	}
	
	public xbean.BattleResult getBattleResult() { return br; }

	@Override
	public long getRoleid() {
		return roleid;
	}
	
//	public Map<fire.pb.battle.EBattleStatsticsType, fire.pb.battle.IBattleRoundStatistics<?,?> > getStatisticMap() {
//		return statisticMap;
//	}
}
