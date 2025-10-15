package fire.pb.redirect;

import fire.pb.map.MapConfig;
import fire.pb.scene.BlockInfo;
import fire.pb.scene.Scene;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;


/**
 * 副本的重定位操作
 */
public class Redirect {

	public static void getoutFromDynamicScene(Role role, Scene curScene, int gototype) {
		if (role == null)
			return;
		final RedirectInfo info = Redirect.getRedirectInfo(role, curScene);
		if (info == null)
			return;
		role.justGoto(info.getMapID(), info.getGPosx(), info.getGPosy(), BlockInfo.FLOOR_0_Z, gototype);
	}

	/**
	 * 复本或者梦境中出来 找上次进入地图的位置,如果找不到,找配置文件的的重定位地图坐标以及地图,
	 * 如果这个也没有， 找门派地图
	 */
	public static RedirectInfo getRedirectInfo(Role role, Scene curScene) {

		int mapId = -1;
		int gposx = -1;
		int gposy = -1;
		int gposz = -1;

		// 检查前以前的地图
		if (role.getLastStaticMapInfo() != null && role.getLastStaticMapInfo().sceneid > 0) {
			mapId = (int) role.getLastStaticMapInfo().sceneid;
			GridPos roleGrid = new fire.pb.scene.sPos.Position(role.getLastStaticMapInfo().posx,
					role.getLastStaticMapInfo().posy, role.getLastStaticMapInfo().posz).toGridPos();
			gposx = roleGrid.getX();
			gposy = roleGrid.getY();
			gposz = roleGrid.getZ();
		}

		// 检测重定位地图
		if (mapId == -1 && curScene != null) {
			MapConfig conf = curScene.getMapConfig();

			// 当前地图是副本
			if (conf.dynamic == 1) {
				mapId = conf.getRemap();
				gposx = conf.getRexjPos();
				gposy = conf.getReyjPos();
				gposz = BlockInfo.FLOOR_0_Z;
			}
		}

		// 重定位到门派地图
		if (mapId == -1) {
			final fire.pb.role.Redirect config = fire.pb.main.ConfigManager.getInstance()
					.getConf(fire.pb.role.Redirect.class).get(role.getSchoolId());
			if (config != null) {
				mapId = config.remapid;
				gposx = config.reposx;
				gposy = config.reposy;
				gposz = BlockInfo.FLOOR_0_Z;
			}
		}

		if (mapId != -1) {
			return new RedirectInfo(mapId, gposx, gposy, gposz);
		} else {
			Scene.LOG.info("进行场景重定向时找不到目标场景");
		}

		return null;
	}

	public static RedirectInfo getRedirectInfo(int school) {
		final fire.pb.role.Redirect config = fire.pb.main.ConfigManager.getInstance()
				.getConf(fire.pb.role.Redirect.class).get(school);

		return null != config ? new RedirectInfo(config.remapid, config.reposx, config.reposy, BlockInfo.FLOOR_0_Z)
				: null;
	}

}
