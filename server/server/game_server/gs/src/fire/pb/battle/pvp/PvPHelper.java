package fire.pb.battle.pvp;

import java.util.Map;

import fire.pb.circletask.PVPAwardConfig;
import fire.pb.circletask.PVPScoreAwardConfig;
import fire.pb.main.ConfigManager;

/**
 * PvP帮助类
 * @author XGM
 */
public abstract class PvPHelper {

	/**
	 * 获得NPC坐标
	 * @param configId
	 * @return
	 */
	public static fire.pb.npc.SNpcPos getNpcPosByConfigId(int configId) {
		fire.pb.npc.SActivityNpcPos activityNpcPosConf = fire.pb.timer.ActivityManager
				.getActivityNpcPosMap().get(configId);
		if (activityNpcPosConf != null) {
			if (activityNpcPosConf.getNpcPoses().size() > 0) {
				return activityNpcPosConf.getNpcPoses().get(0);
			}
		}
		return null;
	}

	/**
	 * 通过服务ID获得奖励类型
	 * @param serviceId
	 * @return
	 */
	public static int getBoxTypeByServiceId(int serviceId) {
		switch (serviceId) {
		// 1v1
		case 910006: return 1; // 首胜奖励
		case 910007: return 2; // 五战奖励
		// 3v3
		case 910016: return 1; // 首胜奖励
		case 910017: return 2; // 十战奖励
		case 910018: return 3; // 五胜奖励
		// 5v5
		case 910026: return 1; // 首胜奖励
		case 910027: return 2; // 五战奖励
		}
		return -1;
	}

	/**
	 * 根据积分取得等级
	 * @param score
	 * @return
	 */
	public static int getScoreLevel(int score) {
		Map<Integer, PVPScoreAwardConfig> cfgs = ConfigManager.getInstance().getConf(PVPScoreAwardConfig.class);
		for (Map.Entry<Integer, PVPScoreAwardConfig> entry : cfgs.entrySet()) {
			if (entry.getValue().score <= score && score <= entry.getValue().score2)
				return entry.getKey();
		}
		return cfgs.size();
	}

	/**
	 * 获取奖励ID
	 * @param pvpType
	 * @param level
	 * @param isWin
	 * @param flag 优胜场标记(只有5V5用到,1=优胜场,0=普通场)
	 * @return
	 */
	public static int getAwardIdByRoleLevel(EPvPType pvpType, int level, boolean isWin, int flag) {

		int type = -1;
		if (pvpType == EPvPType.PVP1) {
			type = 1;
		} else if (pvpType == EPvPType.PVP3) {
			type = 2;
		} else if (pvpType == EPvPType.PVP5) {
			type = 3;
		}

		Map<Integer, PVPAwardConfig> confs = ConfigManager.getInstance().getConf(PVPAwardConfig.class);
		for (PVPAwardConfig conf : confs.values()) {
			if (conf.type == type && conf.flag == flag) {
				if (conf.level1 <= level && level <= conf.level2) {
					if (isWin) {
						return conf.winawardid;
					} else {
						return conf.loseawardid;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 获取积分奖励配置
	 * @param scoreLevel
	 * @return
	 */
	public static PVPScoreAwardConfig getScoreAwardConfig(int scoreLevel) {
		return ConfigManager.getInstance().getConf(PVPScoreAwardConfig.class).get(scoreLevel);
	}

}
