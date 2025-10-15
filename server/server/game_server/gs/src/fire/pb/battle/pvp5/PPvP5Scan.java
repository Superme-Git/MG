package fire.pb.battle.pvp5;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import mkdb.Procedure;

/**
 * 5v5定时扫描可进入匹配玩家
 * @author XGM
 */
public class PPvP5Scan extends Procedure {

	private final int grade;
	
	public PPvP5Scan(int grade) {
		this.grade = grade;
	}

	@Override
	protected boolean process() {

		PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
		if (race.getXRace() == null) {
			return false;
		}
		if (race.isOpening() == false) {
			return false;
		}

		// 预测结果
		PvP5Judge judge = new PvP5Judge(race);
		PvP5Judge.EResult result = judge.predict();
		if (result != PvP5Judge.EResult.NULL) {
			int camp = (result == PvP5Judge.EResult.AWIN ? 0 : 1);
			PvP5CampCountdown.getInstance().enter(grade, camp);
			int retRemainMinute = PvP5CampCountdown.getInstance().tick(grade, new Function<Integer, Void>() {
				@Override
				public Void apply(Integer remainMinute) {
					if (remainMinute > 0) {
						PvP5CampCountdown.broadcast(grade, camp, remainMinute);
					} else {
						judge.award(result);
						race.end();
					}
					return null;
				}
			});
			// 结束后不再往下走
			if (retRemainMinute == 0) {
				return true;
			}
		} else {
			PvP5CampCountdown.getInstance().leave(grade);
		}

		// 添加可匹配玩家
		List<Long> roleIds = new LinkedList<Long>();
		scan(roleIds, race.getXRace().getAllrolesidcampa());
		scan(roleIds, race.getXRace().getAllrolesidcampb());

		// 已经在队列中的角色
		Set<Long> existRoleIds = new HashSet<Long>();
		for (xbean.PvP5QueueRole queueRole : race.getXRace().getWaitingqueue()) {
			existRoleIds.add(queueRole.getRoleid());
		}

		// 加入等待队列
		long now = System.currentTimeMillis();
		for (Long roleId : roleIds) {
			if (existRoleIds.contains(roleId) == false
					&& isCanEnterWait(race, roleId, now)) {
				xbean.PvP5QueueRole newQueueRole = xbean.Pod.newPvP5QueueRole();
				newQueueRole.setRoleid(roleId);
				newQueueRole.setEnterqueuetime(now);
				race.getXRace().getWaitingqueue().add(newQueueRole);
				// 通知客户端倒计时
				PvP5RoleProxy.notifyTeamProtocol(roleId, new SPvP5ReadyFight());
				// 打印日志
				if (PvP5Control.getLogger().isDebugEnabled()) {
					PvP5Control.getLogger().debug("PVP5::[PPvP5Scan] "
							+ " grade:" + grade
							+ " roleId:" + roleId);
				}
			}
		}

		return true;
	}

	/**
	 * 扫描并获取有效角色
	 */
	private static void scan(List<Long> outRoleIds, List<Long> inRoleIds) {
		for (long roleId : inRoleIds) {
			// 排除队员
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_TEAM_MEMBER))
				continue;
			// 排除战斗者
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))
				continue;
			// 排除观战者
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_WATCHER))
				continue;
			// 排除观看录像者
			if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_REPLAY))
				continue;
			// 添加
			outRoleIds.add(roleId);
		}
	}

	/**
	 * 是否能进入等待队列
	 */
	private static boolean isCanEnterWait(PvP5RaceProxy race, long roleId, long now) {
		if (PvP5RoleStateManager.getInstance().checkPvPState(roleId, PvP5RoleStateManager.PVPSTATE_DEFAULT) == false) {
			return false;
		}
		if (race.roleIsCanReady(roleId, true) == false) {
			return false;
		}
		return true;
	}
}
