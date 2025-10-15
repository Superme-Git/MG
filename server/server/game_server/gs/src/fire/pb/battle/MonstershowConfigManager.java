
package fire.pb.battle;

import java.util.Map;

/**明雷怪触发怪物信息*/
public class MonstershowConfigManager {
	private final Map<Integer, fire.pb.npc.SMonstershowConfig> configMap;
	MonstershowConfigManager( final fire.pb.main.ConfigManager cm ) {
		configMap = cm.getConf( fire.pb.npc.SMonstershowConfig.class);
	}
	public fire.pb.npc.SMonstershowConfig getConfig( int monsterkey ) {
		return configMap.get( monsterkey );
	}
	
}
