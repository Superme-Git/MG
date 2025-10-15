package fire.pb.battle.pvp1;

import java.util.Collection;
import java.util.Map;

import fire.pb.battle.pvp.PvPHelper;
import fire.pb.circletask.SPVP1MapConfig;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;

/**
 * PvP1辅助类
 * @author XGM
 */
public abstract class PvP1Helper extends PvPHelper {

//	static class SingletonHolder {
//		static PvP1Helper singleton = new PvP1Helper();
//	}
//
//	public static PvP1Helper getInstance() {
//		return SingletonHolder.singleton;
//	}

	public static final int ENTER_MIN_LEVEL = 40; // 入场的最小等级

	public static final int RACE_MAX_BATTLE_NUM = 99; // 最大战斗次数

	public static final int SCORE_MAX_BATTLE_NUM = 10; // 给积分的战斗次数

	public static final long BATTLE_BETWEEN_MILLISECONDS = 1000 * 5; // 两场战斗之间的间隔

	public static final int CONTINUE_WIN_MIN_NUM = 5; // 连胜的最小次数

	// 奖励的ID
	public static final int FIRST_WIN_AWARD_ID = 6080;
	public static final int FIVE_FIGHT_AWARD_ID = 6081;

	/**
	 * 拉人进场
	 * @param roleId
	 * @return
	 */
	public static boolean summonByRole(long roleId, int roleMapId) {

		if (isPvPMap(roleMapId)) {
			return false;
		}

		int grade = getRaceGradeByRoleId(roleId);
		if (grade <= 0) {
			return false;
		}

		SGeneralSummonCommand snd = new SGeneralSummonCommand();
		snd.roleid = 0;
		snd.mapid = 0;
		snd.minimal = 0;
		snd.summontype = TransmitTypes.singlepvp;
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.psendWhileCommit(roleId, snd);
		} else {
			gnet.link.Onlines.getInstance().send(roleId, snd);
		}
		return true;
	}

	/**
	 * 获取所有赛场ID
	 * @return
	 */
	public static Collection<Integer> getAllRaceIds() {
		Map<Integer, SPVP1MapConfig> cfgs = ConfigManager.getInstance().getConf(SPVP1MapConfig.class);
		return cfgs.keySet();
	}

	/**
	 * 通过角色ID获取可进入赛场等级
	 * @param roleId
	 * @return
	 */
	public static int getRaceGradeByRoleId(long roleId) {

		int level = xtable.Properties.selectLevel(roleId);
		if (level < ENTER_MIN_LEVEL) {
			return -1;
		}
		return getRaceGradeByRoleLevel(level);
	}

	/**
	 * 通过角色等级获取可进入赛场等级
	 * @param level
	 * @return
	 */
	public static int getRaceGradeByRoleLevel(int level) {

		Map<Integer, SPVP1MapConfig> confs = ConfigManager.getInstance().getConf(SPVP1MapConfig.class);
		for (Map.Entry<Integer, SPVP1MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getLev1() <= level && level <= entry.getValue().getLev2()) {
				return entry.getKey();
			}
		}
		return 1;
	}

	/**
	 * 通过赛场等级获取地图ID
	 * @param grade
	 * @return
	 */
	public static int getMapIdByRaceGrade(int grade) {

		SPVP1MapConfig conf = ConfigManager.getInstance().getConf(SPVP1MapConfig.class).get(grade);
		if (conf == null) {
			return -1;
		}
		return conf.map;
	}

	/**
	 * 通过地图ID获取赛场等级
	 * @param mapId
	 * @return
	 */
	public static int getRaceGradeByMapId(int mapId) {

		Map<Integer, SPVP1MapConfig> confs = ConfigManager.getInstance().getConf(SPVP1MapConfig.class);
		for (Map.Entry<Integer, SPVP1MapConfig> entry : confs.entrySet()) {
			if (entry.getValue().getMap() == mapId)
				return entry.getKey();
		}
		return -1;
	}

	/**
	 * 是不是PvP1的地图
	 * @param mapId
	 * @return
	 */
	public static boolean isPvPMap(int mapId) {
		return getRaceGradeByMapId(mapId) != -1;
	}
}
