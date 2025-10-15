package fire.pb.battle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BattleConfigManager {
	public static BattleConfigManager getInstance() {
		return instance;
	}
	static BattleConfigManager instance = new BattleConfigManager();
	private BattleConfigManager() {
		
	}

	void init( fire.pb.main.ConfigManager cm ) {
		monsterNumConfigMap.clear();
		Map<Integer, SBattleMonsterConfig> bmcMap = 
			cm.getConf(fire.pb.battle.SBattleMonsterConfig.class );
		for ( SBattleMonsterConfig config : bmcMap.values() ) {
			List<TeamMonsterNumConfig> list = monsterNumConfigMap.get( config.teamsize );
			if ( list == null ) {
				list = new ArrayList<TeamMonsterNumConfig>();
				monsterNumConfigMap.put( config.teamsize, list );
			}
			list.add( new TeamMonsterNumConfig( 
					new MonsterNumConfig( config.普通怪数目, config.头领数目 ), config.出现概率 ) );
		}
	}
	
	private Map<Integer, List<TeamMonsterNumConfig> > monsterNumConfigMap =
		new HashMap<Integer, List<TeamMonsterNumConfig> >();
	
	private class TeamMonsterNumConfig {
		TeamMonsterNumConfig( MonsterNumConfig conf, int prop ) {
			this.conf = conf;
			this.prop = prop;
		}
		public MonsterNumConfig conf;
		public int prop;
	}
	
	public MonsterNumConfig getMonsterNumConfig( final int teamsize ) {
		List<TeamMonsterNumConfig> confs = monsterNumConfigMap.get( teamsize );
		if ( confs == null ) 
			return null;
		int r = fire.pb.util.Misc.getRandomBetween( 1, 1000 );
		for ( TeamMonsterNumConfig conf : confs ) {
			if ( conf.prop == 0 )
				continue;
			if ( r <= conf.prop ) {
				return conf.conf;
			} else {
				r -= conf.prop;
			}
		}
		return null;
	}
	
	public class MonsterNumConfig {
		MonsterNumConfig( final int normalNum, final int headNum ) {
			this.headNum = headNum;
			this.normalNum = normalNum;
		}
		public int getMonsternum() {
			return normalNum + headNum;
		}
		public int getNormalnum() {
			return normalNum;
		}
		public int getHeadnum() {
			return headNum;
		}
		public int normalNum;
		public int headNum;
	}

}
