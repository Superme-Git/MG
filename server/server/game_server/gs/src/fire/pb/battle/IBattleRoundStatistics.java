package fire.pb.battle;
/**战斗回合统计接口*/
public interface IBattleRoundStatistics<K, V> {
	public EBattleStatsticsType getStatisticsType();
	public void statisticsProcess( final xbean.BattleInfo battleinfo );
	public java.util.Map<K, V> getStatisticsResult();
}
