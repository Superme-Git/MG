package fire.pb.battle.pvp5;

import java.util.LinkedList;
import java.util.List;

import fire.pb.PAddExpProc;
import fire.pb.battle.pvp.EPvPStage;

/**
 * 5v5定时奖励
 * @author XGM
 */
public class PvP5ScheduleAward implements Runnable {

	private final EPvPStage ePvPStage;

	public PvP5ScheduleAward(EPvPStage ePvPStage) {
		this.ePvPStage = ePvPStage;
	}

	@Override
	public void run() {

		for (int grade : PvP5Helper.getAllRaceIds()) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {

					PvP5RaceProxy race = PvP5RaceProxy.getPvP5RaceProxy(grade);
					if (race.getXRace() == null) {
						return false; // 如果这里执行的比创建赛场早,就暂不执行
					}
					if (race.isOpening() == false) {
						return false;
					}

					// 获得优胜场角色
					List<PvP5RoleProxy> roleProxyList = new LinkedList<PvP5RoleProxy>();
					roleProxyList.addAll(getExcellentRole(race.getXRace().getAllrolesidcampa()));
					roleProxyList.addAll(getExcellentRole(race.getXRace().getAllrolesidcampb()));
					// 定时奖励
					for (PvP5RoleProxy roleProxy : roleProxyList) {
						// 奖励经验
						awardExp(roleProxy.getRoleId());
						// 奖励积分
						if (ePvPStage == EPvPStage.FIGHT) {
							awardScore(roleProxy);
						}
					}
					return true;
				}
			}.submit();
		}
	}

	/**
	 * 奖励经验
	 * @param roleId
	 */
	public void awardExp(long roleId) {
		int lv = xtable.Properties.selectLevel(roleId);
		int exp = (int) (400 * lv * 0.04);
		PAddExpProc proc = new PAddExpProc(roleId, exp, true, PAddExpProc.OTHER, "5v5挂机奖励");
		mkdb.Procedure.pexecuteWhileCommit(proc);
	}

	/**
	 * 奖励积分
	 * @param roleProxy
	 */
	public void awardScore(PvP5RoleProxy roleProxy) {
		
	}

	/**
	 * 获得优胜场角色
	 * @param roleIds
	 * @return
	 */
	public static List<PvP5RoleProxy> getExcellentRole(List<Long> roleIds) {
		List<PvP5RoleProxy> roleProxyList = new LinkedList<PvP5RoleProxy>();
		for (Long roleId : roleIds) {
			PvP5RoleProxy roleProxy = PvP5RoleProxy.getPvP5RoleProxy(roleId, true);
			if (roleProxy.getExcellent() == 1) {
				roleProxyList.add(roleProxy);
			}
		}
		return roleProxyList;
	}
}
