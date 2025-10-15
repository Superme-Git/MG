package fire.pb.battle.pvp;

import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp3.PvP3Helper;
import fire.pb.battle.pvp5.PvP5Helper;

/**
 * PvP帮助类的管理
 * @author XGM
 */
public class PvPHelperManager {

	// 为了简化调用,帮助类只提供静态接口,所以帮助类不提供实例 [4/11/2016 XGM]
//	private static final Map<EPvPType, PvPHelper> manager = new HashMap<EPvPType, PvPHelper>();
//
//	static {
//		register(EPvPType.PVP1, PvP1Helper.getInstance());
//		register(EPvPType.PVP3, PvP3Helper.getInstance());
//		register(EPvPType.PVP5, PvP5Helper.getInstance());
//	}
//
//	/**
//	 * 注册
//	 * @param e
//	 * @param h
//	 */
//	public static void register(EPvPType e, PvPHelper h) {
//		manager.put(e, h);
//	}

	/**
	 * 是不是PvP的地图
	 * @param mapId
	 * @return
	 */
	public static boolean isPvPMap(int mapId) {
		if (PvP1Helper.isPvPMap(mapId) || PvP3Helper.isPvPMap(mapId) || PvP5Helper.isPvPMap(mapId)) {
			return true;
		}
		return false;
	}
}
