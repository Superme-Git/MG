package fire.pb.battle;

import fire.pb.map.MapConfig;

public class PlayPKManage {

	public static final int MAZ_DISTANCE = 600; // 发起决斗或切磋的距离限制
	
	public static final int BuffDuelID=500019;//决斗buffid

	public static final int PK_PLAY_WIN = 1;

	public static final int PK_PLAY_LOSE = -1;


	public static MapConfig getMapConfig(int mapid){
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class).get(mapid);
	}
}
