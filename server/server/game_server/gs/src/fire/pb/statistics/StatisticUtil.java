
package fire.pb.statistics;

import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import fire.pb.fushi.FushiConst;
import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;
import mkdb.Procedure;

public class StatisticUtil {

	public static final long dayMills = 1000 * 60 * 60 * 24;

	public static final long weekMills = 1000 * 60 * 60 * 24 * 7;

	public static final long addMills = 1000 * 60 * 60 * 24 * 3;

	static final Properties prop = ConfigManager.getInstance().getPropConf("sys");

	static final int ROLE_ACTIVE_DAY_MINUTES = FireProp.getIntValue(prop, "sys.roleActiveDayMinute");

	static final int ROLE_ACTIVE_WEEK_MINUTES = FireProp.getIntValue(prop, "sys.roleActiveWeekMinute");

	static final int USER_ACTIVE_DAY_MINUTES = FireProp.getIntValue(prop, "sys.userActiveDayMinute");

	static final int USER_ACTIVE_WEEK_MINUTES = FireProp.getIntValue(prop, "sys.userActiveWeekMinute");

	public static final long rawTimeOffset = TimeZone.getDefault().getRawOffset();


	public static void setUserFirsttimeIntoWorld(int userid, long time) {

		xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
		if (userInfo == null) {
			userInfo = xbean.Pod.newActiveUserInfo();
			xtable.Activeusertable.insert(userid, userInfo);
		}
		int currentday = getCurrentDay(time);
		userInfo.setFirsttimeenter(currentday);
	}

	public static int getCurrentDay(long time) {

		return (int) ((time + rawTimeOffset) / dayMills);
	}

	public static int getCurrentWeek(long time) {

		return (int) ((time + rawTimeOffset + addMills) / weekMills) + 1;
	}


	public static void setUserLasttimeIntoWorld(int userid, long currentTimeMillis) {

		xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
		if (userInfo == null) {
			userInfo = xbean.Pod.newActiveUserInfo();
			xtable.Activeusertable.insert(userid, userInfo);
		}
		int currentday = getCurrentDay(currentTimeMillis);
		userInfo.setLasttimeenter(currentday);
	}

	public static void setRoleLasttimeIntoWorld(long roleid, long currentTimeMillis) {

		xbean.ActiveRoleInfo roleInfo = xtable.Activeroletable.get(roleid);
		if (roleInfo == null) {
			roleInfo = xbean.Pod.newActiveRoleInfo();
			xtable.Activeroletable.insert(roleid, roleInfo);
		}
		roleInfo.setEnterworldtime(currentTimeMillis);
	}

	public static void updateChargeDayStats(int userid, long time, int chongzhiValue,int rmb) {

		xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
		if (userInfo == null) {
			userInfo = xbean.Pod.newActiveUserInfo();
			xtable.Activeusertable.insert(userid, userInfo);
		}
		int currentDay = getCurrentDay(time);
		if (userInfo.getFirsttimechargeday() == 0)// 为0说明是第一次充值
			userInfo.setFirsttimechargeday(currentDay);

		int chargeDay1 = userInfo.getLasttimechargeday1();
		if (currentDay != chargeDay1) {// 跟数据库里上一次的记录不在同一天,则更新
			userInfo.setLasttimechargeday2(chargeDay1);
			userInfo.setLasttimechargeday1(currentDay);
			
		}

		if (currentDay != userInfo.getLasttimechargeday()) {// 不是同一天则重新计数
			userInfo.setLasttimechargeday(currentDay);
			userInfo.setLasttimechargedaytotalamount(chongzhiValue);
			//截止昨天的充值总额
			userInfo.setChargetotalrmbuntillastdaylong(userInfo.getChargetotalrmblong());
		} else {// 否则只要修改最近日充值总额就可以了
			userInfo.setLasttimechargedaytotalamount(chongzhiValue + userInfo.getLasttimechargedaytotalamount());
		}

		// 最后更新一下总额
		userInfo.setChargetotalamountlong(chongzhiValue + userInfo.getChargetotalamountlong());
		userInfo.setChargetotalrmblong(rmb * 100 + userInfo.getChargetotalrmblong());

	}

	//updateChargeWeekStats:更新所有跟充值有关的当前周数据
	public static void updateChargeWeekStats(int userid, long time, int chongzhiValue) {

		xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
		if (userInfo == null) {
			userInfo = xbean.Pod.newActiveUserInfo();
			xtable.Activeusertable.insert(userid, userInfo);
		}
		int currentWeek = getCurrentWeek(time);
		if (userInfo.getFirsttimechargeweek() == 0)// 为0说明是第一次充值
			userInfo.setFirsttimechargeweek(currentWeek);

		int chargeWeek1 = userInfo.getLasttimechargeweek1();
		if (currentWeek != chargeWeek1) {// 跟数据库里上一次的记录不在同一周,则更新
			userInfo.setLasttimechargeweek2(chargeWeek1);
			userInfo.setLasttimechargeweek1(currentWeek);
		}

		if (currentWeek != userInfo.getLasttimechargeweek()) {// 不是同一天则重新计数
			userInfo.setLasttimechargeweek(currentWeek);
			userInfo.setLasttimechargeweektotalamount(chongzhiValue);
		} else {// 否则只要修改最近日充值总额就可以了
			userInfo.setLasttimechargeweektotalamount(chongzhiValue + userInfo.getLasttimechargeweektotalamount());
		}
	}

	public static void setRoleCreateTime(long roleid, long time) {

		xbean.ActiveRoleInfo roleInfo = xtable.Activeroletable.get(roleid);
		if (roleInfo == null) {
			roleInfo = xbean.Pod.newActiveRoleInfo();
			xtable.Activeroletable.insert(roleid, roleInfo);
		}
		roleInfo.setCreatetime(getCurrentDay(time));
	}

	private static xbean.ActiveUserInfo getActiveUserInfo(long roleid) {
		
		if (roleid == FushiConst.SYS_FUSHI_ROLEID) {
			xbean.ActiveUserInfo activeUserInfo = xtable.Activeusertable.get(Integer.MAX_VALUE);
			if (activeUserInfo == null) {
				activeUserInfo = xbean.Pod.newActiveUserInfo();
				xtable.Activeusertable.insert(Integer.MAX_VALUE, activeUserInfo);
			}
			return activeUserInfo;
		}

		xbean.Properties properties = xtable.Properties.select(roleid);
		if (properties != null) {
			xbean.ActiveUserInfo activeUserInfo = xtable.Activeusertable.get(properties.getUserid());
			if (activeUserInfo == null) {
				activeUserInfo = xbean.Pod.newActiveUserInfo();
				xtable.Activeusertable.insert(properties.getUserid(), activeUserInfo);
			}
			return activeUserInfo;
		}
		return null;
	}

	 private static xbean.ActiveRoleInfo getActiveRoleInfo(long roleid){
	 xbean.ActiveRoleInfo activeRoleInfo = xtable.Activeroletable.get(roleid);
	 if (activeRoleInfo==null){
	 activeRoleInfo = xbean.Pod.newActiveRoleInfo();
	 xtable.Activeroletable.insert(roleid, activeRoleInfo);
	 }
	 return activeRoleInfo;
	 }
	public static void updateFushiConsumeStats(long roleid, long time, int num,int cashnum) {

		if (num <= 0)
			return;

		xbean.ActiveUserInfo userInfo = getActiveUserInfo(roleid);
		if (userInfo == null)
			return;
		userInfo.setFushiconsumetotalamount(num + userInfo.getFushiconsumetotalamount());
		if (cashnum>0)
			userInfo.setCashfushiconsumetotalamount(cashnum + userInfo.getCashfushiconsumetotalamount());

		int currentDay = getCurrentDay(time);
		int currentWeek = getCurrentWeek(time);
		if (currentDay != userInfo.getLasttimefushiconsumeday()) {
			userInfo.setLasttimefushiconsumeday(currentDay);
			userInfo.setLasttimefushiconsumedaytotalamount(num);
		} else {
			userInfo.setLasttimefushiconsumedaytotalamount(num + userInfo.getLasttimefushiconsumedaytotalamount());
		}

		if (currentWeek != userInfo.getLasttimefushiconsumeweek()) {
			userInfo.setLasttimefushiconsumeweek(currentWeek);
			userInfo.setLasttimefushiconsumeweektotalamount(num);
		} else {
			userInfo.setLasttimefushiconsumeweektotalamount(num + userInfo.getLasttimefushiconsumeweektotalamount());
		}
	}

	public static void updateFushiNumStats(long roleid, xbean.YbNum ybNum) {

		if (ybNum == null)
			return;

		xbean.ActiveUserInfo userInfo = getActiveUserInfo(roleid);
		if (userInfo != null) {
			userInfo.setBindfushi(0);
			userInfo.setCashfushi(ybNum.getNum());
			userInfo.setSysfushi(ybNum.getSysnum());
		}
	}
	
	/**
	 * 应用宝的相关处理
	 * @param roleid
	 * @param banlance
	 * @param genbalance
	 */
	public static void updateFushiNumStats(long roleid, int banlance, int genbalance) {
		xbean.ActiveUserInfo userInfo = getActiveUserInfo(roleid);
		if (userInfo != null) {
			userInfo.setBindfushi(0);
			userInfo.setCashfushi(banlance);
			userInfo.setSysfushi(genbalance);
		}
	}

	public static void updateFushiNumInPlatform(long roleid, int num) {
		if (num == 0)
			return;

		xbean.ActiveUserInfo userInfo = getActiveUserInfo(roleid);
		if (userInfo != null) {
			userInfo.setFushiinplatform(userInfo.getFushiinplatform() + num);
			if (userInfo.getFushiinplatform() < 0)
				throw new RuntimeException("fushi num in platform less than zero!");
		}
	}

	public static void updateDealMoneyInPlatform(long roleid, long num) {

		if (num == 0)
			return;

		xbean.ActiveRoleInfo roleInfo = getActiveRoleInfo(roleid);
		if (roleInfo != null) {
			roleInfo.setDealmoneyinplatform(roleInfo.getDealmoneyinplatform() + num);
			if (roleInfo.getDealmoneyinplatform() < 0)
				throw new RuntimeException("deal money in platform less than zero!");
		}
	}

	public static void updateTmpMoneyInPlatform(long roleid, long num) {

		if (num == 0)
			return;

		xbean.ActiveRoleInfo roleInfo = getActiveRoleInfo(roleid);
		if (roleInfo != null) {
			roleInfo.setTmpmoneyinplatform(roleInfo.getTmpmoneyinplatform() + num);
			if (roleInfo.getTmpmoneyinplatform() < 0)
				throw new RuntimeException("tmp money in platform less than zero!");
		}
	}

	public static void updateFushiTradeStats(long buyerid, long sellerid, int num) {

		if (num <= 0)
			return;

		xbean.ActiveUserInfo buyerUserInfo = getActiveUserInfo(buyerid);
		xbean.ActiveUserInfo sellerUserInfo = getActiveUserInfo(sellerid);

		if (buyerUserInfo != null)
			buyerUserInfo.setFushibuytotalamount(buyerUserInfo.getFushibuytotalamount() + num);

		if (sellerUserInfo != null)
			sellerUserInfo.setFushiselltotalamount(sellerUserInfo.getFushiselltotalamount() + num);

	}

 
	//updateRoleActiveStats:更新玩家的在线时长,活跃度信息
	public static void updateRoleActiveStats(long roleid, int time) {

		xbean.ActiveRoleInfo roleInfo = xtable.Activeroletable.get(roleid);
		if (roleInfo == null) {
			roleInfo = xbean.Pod.newActiveRoleInfo();
			xtable.Activeroletable.insert(roleid, roleInfo);
		}
		long currentTime = System.currentTimeMillis();
		int currentDay = getCurrentDay(currentTime);
		int currentWeek = getCurrentWeek(currentTime);
		if (roleInfo.getDayonline() != currentDay) {
			roleInfo.setDayonline(currentDay);
			roleInfo.setDayonlinetime(time);
		} else {
			roleInfo.setDayonlinetime(time + roleInfo.getDayonlinetime());
		}

		int activeDay1 = roleInfo.getLasttimeactiveday1();
		if (currentDay != activeDay1) {// 如果最近活跃的当前天不是currentDay,则进行是否要更新的判断,否则说明已经更新过了,更新时长即可
			if (roleInfo.getDayonlinetime() > ROLE_ACTIVE_DAY_MINUTES) {
				if (roleInfo.getFirsttimeactiveday() == 0) // 如果是第一次活跃
					roleInfo.setFirsttimeactiveday(currentDay);
				roleInfo.setLasttimeactiveday2(activeDay1);
				roleInfo.setLasttimeactiveday1(currentDay);
				roleInfo.setLasttimeactiveday1onlinetime(roleInfo.getDayonlinetime());
			}
		} else {
			roleInfo.setLasttimeactiveday1onlinetime(roleInfo.getDayonlinetime());
		}
		// 刷新role周的数据
		if (roleInfo.getWeekonline() != currentWeek) {
			roleInfo.setWeekonline(currentWeek);
			roleInfo.setWeekonlinetime(time);
		} else {
			roleInfo.setWeekonlinetime(time + roleInfo.getWeekonlinetime());
		}
		int activeWeek1 = roleInfo.getLasttimeactiveweek1();
		if (currentWeek != activeWeek1) {
			if (roleInfo.getWeekonlinetime() > ROLE_ACTIVE_WEEK_MINUTES) {
				if (roleInfo.getFirsttimeactiveweek() == 0)
					roleInfo.setFirsttimeactiveweek(currentWeek);
				roleInfo.setLasttimeactiveweek2(activeWeek1);
				roleInfo.setLasttimeactiveweek1(currentWeek);
				roleInfo.setLasttimeactiveweek1onlinetime(roleInfo.getWeekonlinetime());
			}
		} else {
			roleInfo.setLasttimeactiveweek1onlinetime(roleInfo.getWeekonlinetime());
		}

		// 刷新user日和周的数据
		Integer userid = xtable.Roleid2userid.select(roleid);
		if (userid != null) {
			int onlineTimeDay = 0; // user下面所有id的当前天在线时长之和
			int onlineTimeWeek = 0;// user下面所有id的当前周在线时长之和
			int maxOnlineDay = 0;// user下面有多个id,可能每个id的当前天是不一样的,我们要以最大的当前天为准,如果所有角色当前天相等,则时间相加
			int maxOnlineWeek = 0;// 同上
			List<Long> ids = xtable.User.selectIdlist(userid);
			if (ids != null && !ids.isEmpty()) {
				for (Long rid : ids) {
					xbean.ActiveRoleInfo rInfo = null;
					if (rid != roleid)
						rInfo = xtable.Activeroletable.select(rid);
					else
						rInfo = roleInfo;
					if (rInfo == null)
						continue;
					// 计算user的当前天在线时长,属于user的所有roleid相加.
					if (rInfo.getDayonline() > maxOnlineDay) {
						maxOnlineDay = rInfo.getDayonline();
						onlineTimeDay = rInfo.getDayonlinetime();
					} else if (rInfo.getDayonline() == maxOnlineDay) {
						onlineTimeDay += rInfo.getDayonlinetime();
					}
					// 计算user的当前周在线时长
					if (rInfo.getWeekonline() > maxOnlineWeek) {
						maxOnlineWeek = rInfo.getWeekonline();
						onlineTimeWeek = rInfo.getWeekonlinetime();
					} else if (rInfo.getWeekonline() == maxOnlineWeek) {
						onlineTimeWeek += rInfo.getWeekonlinetime();
					}
				}
			}
			xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
			if (userInfo == null) {
				userInfo = xbean.Pod.newActiveUserInfo();
				xtable.Activeusertable.insert(userid, userInfo);
			}
			if (onlineTimeDay > USER_ACTIVE_DAY_MINUTES) {// 如果达到了日活跃的标准
				int userActiveDay1 = userInfo.getLasttimeactiveday1();
				if (maxOnlineDay != userActiveDay1) {// 如果最近活跃的当前天不是currentDay,则进行是否要更新的判断,否则说明已经更新过了
					if (userActiveDay1 == 0) // 如果是第一次活跃
						userInfo.setFirsttimeactiveday(maxOnlineDay);
					userInfo.setLasttimeactiveday2(userActiveDay1);
					userInfo.setLasttimeactiveday1(maxOnlineDay);
				}
			}
			if (onlineTimeWeek > USER_ACTIVE_WEEK_MINUTES) {// 如果达到了周活跃的标准
				int userActiveWeek1 = userInfo.getLasttimeactiveweek1();
				if (maxOnlineWeek != userActiveWeek1) {
					if (userActiveWeek1 == 0)
						userInfo.setFirsttimeactiveweek(maxOnlineWeek);
					userInfo.setLasttimeactiveweek2(userActiveWeek1);
					userInfo.setLasttimeactiveweek1(maxOnlineWeek);
				}
			}
		}
	}

	public static void updateCofcMoney(long roleid) {

	}

	public static void updateOnTheEndOfDay(final List<Long> roleIds, final long currentTimeMillis) {
		
		for (final Long rid : roleIds) {
			Procedure.pexecute(new mkdb.Procedure() {

				@Override
				protected boolean process() throws Exception {

						xbean.Properties roleProp = xtable.Properties.select(rid);
						if (roleProp != null) {
							long time = currentTimeMillis - roleProp.getOnlinetime();
							updateRoleActiveStats(rid, (int) time / 60000);
							
							//设置加点方案切换次数
							roleProp.setSchemechanges(0);
						}
					return true;
				}
			});
		}


	}	
}
