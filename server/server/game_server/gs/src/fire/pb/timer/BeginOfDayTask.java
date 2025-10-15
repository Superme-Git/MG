package fire.pb.timer;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import fire.msp.IGetRolesCallBack;
import fire.pb.PLoginDayUpdatesProc;
import fire.pb.PLoginWeekUpdatesProc;
import fire.pb.SServerLevel;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.activity.reg.PQueryRegRec;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAutoAddCircleTask;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.circletask.catchit.PCatchItTaskClear;
import fire.pb.fushi.payday.PRefreshAllDayPay;
import fire.pb.hook.PAddDpointDailyProc;
import fire.pb.instancezone.bingfeng.SCanEnterBingFeng;
import fire.pb.map.RoleManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.mission.activelist.RoleLivenessManager;
import fire.pb.state.PMulDayLoginDayChange;
import fire.pb.tel.utils.GoodsSafeLocksUtils;

public class BeginOfDayTask extends TimerTask {

	class PRefreshTasks extends Procedure {

		private List<Long> roleIds;

		public PRefreshTasks(List<Long> roleIds) {
			this.roleIds = roleIds;
		}

		@Override
		protected boolean process() throws Exception {
			for (Long rid : roleIds) {
				// 重置个人活动数据,这个请放在最前面
				pexecute(new mkdb.Procedure() {

					protected boolean process() throws Exception {
						RoleLivenessManager.logger.info("每天开始,刷新角色[" + rid + "]日活动数据!");
						RoleLiveness actrole = RoleLiveness.getRoleLiveness(rid);
						boolean ret = actrole.resetActiveData();
						actrole.sendActivityData();
						new PAutoAddCircleTask(rid).call();
						/*
						 * SCanEnterBingFeng scanenter = new SCanEnterBingFeng();
						 * scanenter.finish = 0;
						 * Procedure.psendWhileCommit(rid, scanenter);
						 */
						return ret;
					};
				});
				pexecute(new fire.pb.circletask.PClearCircleTask(rid, true));
				// 清理捉鬼任务信息
				pexecute(new PCatchItTaskClear(rid));
				// 自动增加循环师门任务
				// pexecute(new PAutoAddCircTask(rid));
				int level = xtable.Properties.selectLevel(rid);
				java.util.List<Integer> circletasks = CircleTaskManager.getInstance().getCircleTypeByLevel(level);
				for (Integer curtype : circletasks) {
					if (curtype.intValue() == 1010000)
						continue;
					pexecute(new PAutoAddCircleTask(rid, curtype));
				}
				// 挂机增加可领取双倍点数
				pexecute(new PAddDpointDailyProc(rid));
				pexecute(new PMulDayLoginDayChange(rid));
				pexecute(new PQueryRegRec(rid));
				// 处理在线玩家的每天更新事件
				pexecute(new PLoginDayUpdatesProc(rid));
				// 处理玩家周更新逻辑
				pexecute(new PLoginWeekUpdatesProc(rid));
				// 重置道具安全锁开启模式
				pexecute(new mkdb.Procedure() {

					protected boolean process() throws Exception {
						GoodsSafeLocksUtils.autoOpenGoodLocks(rid);
						return true;
					};
				});
			}
			SServerLevel sLevel = fire.pb.timer.Module.getServerLvData();
			Procedure.psendWhileCommit(roleIds, sLevel);
			// 延迟5分钟执行的任务
			mkdb.Executor.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// TODO
				}
			}, 5, TimeUnit.MINUTES);
			// 延迟10分钟执行的任务
			mkdb.Executor.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					// TODO
					// 帮派凌晨处理
					new fire.pb.clan.PUpdateClansProc().submit();
					new fire.pb.battle.livedie.PLiveDieBattleClearDb().submit();// 清除过期的生死战记录
				}
			}, 10, TimeUnit.MINUTES);
			return true;
		}
	}

	public BeginOfDayTask() {
	}

	class PSetWeekInstanceInfo extends Procedure {

		@Override
		protected boolean process() throws Exception {
			fire.pb.mission.instance.InstanceManager.getInstance().setInstanceInfo(
					fire.pb.mission.instance.InstanceManager.TURN_WEEK, 1);
			return true;
		}
	}

	class PSetInstanceInfo extends Procedure {

		@Override
		protected boolean process() throws Exception {
			fire.pb.mission.instance.InstanceManager.getInstance().setInstanceInfo(
					fire.pb.mission.instance.InstanceManager.TURN_DAY, 1);
			return true;
		}
	}

	class PRefreshHeroWeekFreeTasks extends Procedure {

		private List<Long> roleIds;

		private int week;

		public PRefreshHeroWeekFreeTasks(List<Long> roleIds, int week) {
			this.roleIds = roleIds;
			this.week = week;
		}

		@Override
		protected boolean process() throws Exception {
			for (Long rid : roleIds) {
				pexecute(new fire.pb.huoban.PRefreshHeroWeekFree(rid, week));// 处理伙伴
				// 处理周刷新的活动次数
				pexecute(new mkdb.Procedure() {

					protected boolean process() throws Exception {
						RoleLivenessManager.logger.info("每周开始,刷新角色[" + rid + "]周活动数据!");
						RoleLiveness actrole = RoleLiveness.getRoleLiveness(rid);
						actrole.resetActiveWeekData();
						actrole.sendActivityData();
						return true;
					};
				});
				pexecute(new mkdb.Procedure() {

					protected boolean process() throws Exception {
						// 处理暗夜马戏团周刷新
						RoleLivenessManager.logger.info("每周开始,发送角色[" + rid + "]暗夜马戏团活动数据!");
						RoleAnYeTask.sendAllAnYeTask(rid);
						return true;
					};
				});
			}
			return true;
		}
	}

	class PWeekTaskDeal extends Procedure {

		private List<Long> roleIds;

		public PWeekTaskDeal(List<Long> roleIds) {
			this.roleIds = roleIds;
		}

		@Override
		protected boolean process() throws Exception {
			Calendar cal = Calendar.getInstance();
			int dayInWeek = cal.get(Calendar.DAY_OF_WEEK);
			int weekInYear = cal.get(Calendar.WEEK_OF_YEAR);
			int year = cal.get(Calendar.YEAR);
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			// int minute = cal.get(Calendar.MINUTE);
			// 周一才刷新
			if (dayInWeek == Calendar.MONDAY && hour == 0) {
				xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.get(1);
				if (serviceInfo == null) {
					serviceInfo = xbean.Pod.newServiceInfo();
					serviceInfo.setYear(year);
					xtable.Serviceinfos.insert(1, serviceInfo);
				} else if (year != serviceInfo.getYear()) {
					// 新的一年
					serviceInfo.setYear(year);
				}
				serviceInfo.setWeeks(weekInYear);
				pexecute(new mkdb.Procedure() {

					@Override
					protected boolean process() {
						// 设置伙伴周免费
						fire.pb.huoban.Module.getInstance().SetHeroWeekFree();
						// new PSetWeekInstanceInfo().call();
						new PRefreshHeroWeekFreeTasks(roleIds, weekInYear).call();
						return true;
					}
				});
				// 延迟5分钟执行的任务
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// TODO
					}
				}, 5, TimeUnit.MINUTES);
				// 延迟10分钟执行的任务
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// TODO
						// 清空3v3历史排行榜
						fire.pb.battle.pvp3.PvP3RankVisitor.clearHistoryRankList();
					}
				}, 10, TimeUnit.MINUTES);
				ActivityClanFightManager.getInstance().curweek.set(ActivityClanFightManager.getInstance().CalcCurWeekNum());
				int curweek = ActivityClanFightManager.getInstance().curweek.get();
				if (curweek % 24 == 0) // 每24周清理历史数据 by changhao
				{
					fire.pb.clan.fight.PClanFightClearHistroyRank p = new fire.pb.clan.fight.PClanFightClearHistroyRank();
					mkdb.Procedure.pexecuteWhileCommit(p);
				}
			}
			return true;
		}
	}

	class PRefreshMonthTasks extends Procedure {

		private List<Long> roleIds;

		public PRefreshMonthTasks(List<Long> roleIds) {
			this.roleIds = roleIds;
		}

		@Override
		protected boolean process() throws Exception {
			for (Long rid : roleIds) {
				pexecute(new fire.pb.fushi.PAddVipSuppRegNum(rid));// 处理补签次数
			}
			return true;
		}
	}

	class PMonthTaskDeal extends Procedure {

		private List<Long> roleIds;

		public PMonthTaskDeal(List<Long> roleIds) {
			this.roleIds = roleIds;
		}

		@Override
		protected boolean process() throws Exception {
			Calendar cal = Calendar.getInstance();
			int dayInMonth = cal.get(Calendar.DAY_OF_MONTH);
			// 每月1日才刷新
			if (dayInMonth == 1) {
				new PRefreshMonthTasks(roleIds).call();
				// 延迟5分钟执行的任务
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// TODO
					}
				}, 5, TimeUnit.MINUTES);
				// 延迟10分钟执行的任务
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						// TODO
					}
				}, 10, TimeUnit.MINUTES);
			}
			return true;
		}
	}

	@Override
	public void run() {
		RoleLivenessManager.updateRecommend();
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				xbean.ServiceInfo serviceInfo = xtable.Serviceinfos.get(1);
				if (serviceInfo == null) {
					serviceInfo = xbean.Pod.newServiceInfo();
					serviceInfo.setYear(year);
					xtable.Serviceinfos.insert(1, serviceInfo);
				}
				serviceInfo.setDays(serviceInfo.getDays() + 1);
				serviceInfo.setUpdatetime(System.currentTimeMillis());
				return true;
			}
		}.call();
		// 先注释，策划说不需要。
		// 进行跨天七日登录奖励刷新
		RoleManager.getRolesByConditions(-1, -1, -1, -1, -1, new IGetRolesCallBack() {

			@Override
			public void process(List<Long> roleIds) {
				new PRefreshTasks(roleIds).call();
				new PWeekTaskDeal(roleIds).call();
				new PMonthTaskDeal(roleIds).call();
			}
		});
		// 进行所有的投票结算
		try {
			new PSetInstanceInfo().submit();
			// new VoteResultAccountsExcutor().doVoteResultAccounts();
		} catch (Exception e) {
			e.printStackTrace();
			Module.logger.error("服务器结算投票时候出错！！===", e);
		}
		// 给本周结婚纪念日的发消息
		// try {
		// MarryTaskStepManager.sendDrawMarryReward();
		// } catch (Exception e) {
		// e.printStackTrace();
		// Module.logger.error("服务器群发结婚纪念日奖励时候出错！！===", e);
		// }
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) // 点卡服务器 by changhao
		{
			// 处理每日 日卡结算 by changhao
			// java.util.Set<Long> setroleids = new java.util.HashSet<Long>();
			// int rolieidsize = roleIds
			// setroleids.addAll(roleIds);
			new PRefreshAllDayPay().submit();
		} else if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			mkdb.Executor.getInstance().schedule(new fire.pb.fushi.monthcard.RefreshMonthCardTask(), 60, TimeUnit.SECONDS);
		}
	}
}
