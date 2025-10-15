package fire.pb.scene.movable;

import fire.msp.RoleMapInfo;
import fire.pb.map.MapConfig;
import fire.pb.redirect.Redirect;
import fire.pb.redirect.RedirectInfo;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.util.MapUtil;

/**
 * God 是一个工厂类，可以产出各种movable
 */
public class God {

	private static Role createRole(long rid, String nm, Scene s, Position p, int lv, int sch, int camp,
			int footLogoId) {
		return new Role(rid, nm, s, p, lv, sch, camp, footLogoId);
	}

	public static Role createRole(long rid, String name, int level, int school, RoleMapInfo mapinfo,
			RoleMapInfo laststaticmapinfo, boolean isProtectState, int camp, int footLogoId) {

		boolean needRedirect = false; // 用于检测是否进行场景重定向

		// 场景已经销毁,那么需要重定向
		Scene enterScene = SceneManager.getInstance().getSceneByID(mapinfo.sceneid);

		if (null == enterScene) {
			needRedirect = true;
		} else {
			if (enterScene instanceof DynamicScene) // 副本存在,那么检查角色是否处于离线保护状态
				needRedirect = !isProtectState; // 离线保护不需要重定向场景,只要进入副本即可
		}
		Position pos = new Position(mapinfo.posx, mapinfo.posy, mapinfo.posz);

		// 原场景不存在了，需要重定向
		if (needRedirect) {

			try {
				// 先查找最后一次的静态场景进行重定向
				if (laststaticmapinfo != null && laststaticmapinfo.sceneid > 0) {
					enterScene = SceneManager.getInstance().getSceneByID(laststaticmapinfo.sceneid);
					pos = new Position(laststaticmapinfo.posx, laststaticmapinfo.posy, laststaticmapinfo.posz);
				}
			} catch (Exception e) {
				e.printStackTrace();
				enterScene = null;
			}

			try {
				// 如果原场景是副本或者梦境地图 重定位
				if (enterScene == null) {
					int basemapid = MapUtil.getBaseMapIdBySceneId(mapinfo.sceneid);
					MapConfig conf = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class)
							.get(basemapid);
					// 当前地图是副本或者是梦境
					if (conf != null)
						if (conf.dynamic == 1) {
							enterScene = SceneManager.getInstance().getSceneByID(conf.getRemap());
							pos = new GridPos(conf.getRexjPos(), conf.getReyjPos()).toPosition();
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
				enterScene = null;
			}

			// 实在不行，重定向到各门派的安全区域
			if (enterScene == null) {
				final RedirectInfo redir = Redirect.getRedirectInfo(school);
				if (redir != null) {
					enterScene = SceneManager.getInstance().getSceneByID(redir.getMapID());
					pos = new GridPos(redir.getGPosx(), redir.getGPosy(), redir.getGPosz()).toPosition();
				}
			}
		}

		if (null == enterScene) {
			if (needRedirect)
				Scene.LOG.info("进行场景重定向时找不到目标场景");
			return null;
		}

		Position finalpos = enterScene.checkAndChangeGotoPos(pos);
		if (finalpos == null) {
			String str = "角色无法跳转到地图" + enterScene.getMapID() + "的指定坐标:" + pos;
			Scene.LOG.error(str);
			throw new IllegalArgumentException(str);
		}
		final Role cr = God.createRole(rid, name, enterScene, finalpos, level, school, camp, footLogoId);
		return cr;
	}

	/**
	 * 生成NPC
	 */
	public static NPC createNPC(long npckey, int npcid, String name, Scene scene, Position pos, int dir) {
		try {
			return new NPC(npckey, npcid, name, scene, pos, dir);
		} catch (IllegalArgumentException e) {
			e.fillInStackTrace();
			return null;
		}
	}

	/**
	 * 生成宠物
	 */
	public static ScenePet createPet(long uid, String name, int petid, int colour, int bodysize, int showeffect) {
		if (0 == uid)
			return null;
		if (0 == petid)
			return null;

		return new ScenePet(uid, name, petid, colour, bodysize, showeffect);
	}

	/**
	 * 生成拾取物
	 */
	public static Pickup createPickup(long key, int baseid, String name, Scene scene, Position pos, int pickupType) {
		return new Pickup(key, baseid, name, scene, pos, pickupType);
	}

	/**
	 * 生成红包
	 */
	public static HongBaoitem createHongBaoitem(long key, int baseid, Scene scene, Position pos, long money) {
		return new HongBaoitem(key, baseid, scene, pos, money);
	}

	/**
	 * 生成供采集的NPC
	 */
	public static GatherNpc createGatherNpc(long npckey, int npcid, String name, Scene scene, Position pos, int dir) {
		return new GatherNpc(npckey, npcid, name, scene, pos, dir);

	}

	public static Monstershow createMonstershow(long npckey, int baseID) {
		return new Monstershow(npckey, baseID);
	}

	public static SceneTeam createTeam(long teamid, long roleid) {
		return new SceneTeam(teamid, roleid);
	}
}
