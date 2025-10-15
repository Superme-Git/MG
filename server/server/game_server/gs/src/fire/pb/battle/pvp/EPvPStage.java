package fire.pb.battle.pvp;

/**
 * PvP阶段的枚举
 * @author XGM
 */
public enum EPvPStage {
	NOTOPEN, // 未开启阶段
	START, // 开始阶段
	PREPARE, // 准备阶段
	FIGHT, // 战斗阶段(只有这个阶段进行匹配)
	END, // 结束阶段(结束匹配,传出玩家)
	AWARD, // 奖励阶段(可以场外领取奖励)
}
