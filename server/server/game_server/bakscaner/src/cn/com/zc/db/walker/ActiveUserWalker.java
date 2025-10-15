package cn.com.zc.db.walker;

import java.io.IOException;

import xbean.ActiveUserInfo;
import xbean.Properties;
import xbean.User;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.Dbx;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;
import cn.com.zc.db.Main;
import cn.com.zc.db.ScoreSnapShotHelper;
import cn.com.zc.db.StatisDateManager;
import cn.com.zc.db.bean.ScoreSnapShotBean;
import cn.com.zc.db.bean.mbean.ActiveDetail;
import cn.com.zc.db.bean.mbean.ChargeUserDetail;
import cn.com.zc.db.util.FileManager;
import cn.com.zc.db.util.ValueUtile;

import com.goldhuman.Common.Marshal.MarshalException;
import com.goldhuman.Common.Marshal.OctetsStream;

public class ActiveUserWalker extends Walker {

	public ActiveUserWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		StatisDateManager statisManager = StatisDateManager.getInstance();
		
		int userid = (Integer) key;
		xbean.ActiveUserInfo activeUserInfo = (ActiveUserInfo) value;
		
		//tmp
		//writeLine(userid, activeUserInfo);
		
		//历史账户数加一
		statisManager.addHistoryUserNum(1);
		
		//判断是否是新增用户
		if(isIncrease(activeUserInfo.getFirsttimeenter())){
			statisManager.addIncreaseUserNum(1);
		}
		
		//判断是活跃用户的哪一种（返回-1不是任何类型）
		//首先进行周统计
		int weekActiveType = getWeekActiveUserType(activeUserInfo);
		if(-1 != weekActiveType){
			statisManager.setActiveUser(weekActiveType, userid);
		}
		
		//然后进行日统计
		int dayActiveType = getDayActiveUserType(activeUserInfo);
		if(-1 != dayActiveType){
			statisManager.setActiveUser(dayActiveType, userid);
		}
		
		//判断是付费用户的哪一种（返回-1不是任何类型）
		//首次进行周统计
		int weekChargeType = getWeekChargeUserType(activeUserInfo);
		if(-1 != weekChargeType){
			int chargeNum = activeUserInfo.getLasttimechargeweektotalamount();
			int regionValue = getChargeRegionValue(chargeNum);
			if(-1 != regionValue){
				statisManager.setChargeUser(weekChargeType, regionValue, chargeNum);
			}
			
		}
		//然后进行日统计
		int dayChargeType = getDayChargeUserType(activeUserInfo);
		if(-1 != dayChargeType){
			int chargeNum = activeUserInfo.getLasttimechargedaytotalamount();
			int regionValue = getChargeRegionValue(chargeNum);
			if(-1 != regionValue){
				statisManager.setChargeUser(dayChargeType, regionValue, chargeNum);
			}
				
		}
		//充值总额
		recordChargeNum(userid, activeUserInfo);
		try {
			doQuickPhote(activeUserInfo, userid);
		} catch (MarshalException e) {
			e.printStackTrace();
		}
		
		
		
		return true;
	}

	/**
	 
	 */
	private void recordChargeNum(int userid, xbean.ActiveUserInfo activeUserInfo) {
		Dbx.Table userTable = DbxManager.getInstance().getTableByName(
				"user");

		 OctetsStream os = new OctetsStream();
		 OctetsStream result = userTable.find(os.marshal(userid));
		 if(null == result)
			 return;
		 
		 xbean.User user = xbean.Pod.newUser();
		 try {
			user.unmarshal(result);
		} catch (MarshalException e) {
			e.printStackTrace();
		}
		 ScoreSnapShotBean bean=null;
		for(long roleId :user.getIdlistAsData()){
			bean=ScoreSnapShotHelper.getScoreSnapShotBean(roleId);
			if (bean==null) {
				continue;
			}
			//所有角色的总充值额，玩家的roleId,目前只允许创建一角色，所以数据不会不会有问题，
			long chargetotalrmblong = activeUserInfo.getChargetotalrmblong();
			if (chargetotalrmblong > Integer.MAX_VALUE)
				chargetotalrmblong = Integer.MAX_VALUE;
			bean.setTotalCharge((int) chargetotalrmblong);
			bean.setYbNum(activeUserInfo.getCashfushi()+activeUserInfo.getBindfushi()+activeUserInfo.getSysfushi());
			
			//本日充值
			//玩家昨天以前冲过值，但是玩家之后没继续冲值，导致Chargetotalrmbuntillastday==0，所以，需要对Chargetotalrmbuntillastday进行刷新
			if (activeUserInfo.getLasttimechargeday()!=
					RMBItemScaner.getCurrentDay(System.currentTimeMillis()-RMBItemScaner.dayMills)&&
					activeUserInfo.getChargetotalrmbuntillastdaylong()==0) {
				//刷新
				activeUserInfo.setChargetotalrmbuntillastdaylong(chargetotalrmblong);
			}
			bean.setDayCharge((int) (chargetotalrmblong-activeUserInfo.getChargetotalrmbuntillastdaylong()));
		}
	}
	
	
	private void writeLine(int userid, ActiveUserInfo activeUserInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append(userid + ",");
		sb.append(activeUserInfo.getFirsttimeenter()+ ",");
		sb.append(activeUserInfo.getLasttimeenter()+ ",");
		sb.append(activeUserInfo.getLasttimeactiveday1()+ ",");
		sb.append(activeUserInfo.getLasttimeactiveday2()+ ",");
		sb.append(activeUserInfo.getLasttimeactiveweek1()+ ",");
		sb.append(activeUserInfo.getLasttimeactiveweek2()+ ",");
		sb.append(activeUserInfo.getFirsttimeactiveday()+ ",");
		sb.append(activeUserInfo.getFirsttimeactiveweek()+ ",");
		sb.append(activeUserInfo.getLasttimechargeday1()+ ",");
		sb.append(activeUserInfo.getLasttimechargeday2()+ ",");
		sb.append(activeUserInfo.getLasttimechargeweek1()+ ",");
		sb.append(activeUserInfo.getLasttimechargeweek2()+ ",");
		sb.append(activeUserInfo.getFirsttimechargeday()+ ",");
		sb.append(activeUserInfo.getFirsttimeactiveweek()+ ",");
		sb.append(activeUserInfo.getLasttimechargeday()+ ",");
		sb.append(activeUserInfo.getLasttimechargedaytotalamount()+ ",");
		sb.append(activeUserInfo.getLasttimechargeweek()+ ",");
		sb.append(activeUserInfo.getLasttimechargeweektotalamount()+ ",");
		sb.append(activeUserInfo.getChargetotalamountlong()+ ",");
		
		try {
			Main.userWriter.write(sb.toString() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private int getDayActiveUserType(ActiveUserInfo activeUserInfo) {
		int lastActiveDay = activeUserInfo.getLasttimeactiveday1();
		int firstEnterDay = activeUserInfo.getFirsttimeenter();
		//如果最后一次活跃就是第一次进入服务器的当前天
		//即为新增活跃用户
		if(lastActiveDay == firstEnterDay && lastActiveDay == getYesterday()){
			return Constant.DAY_INCREASE_ACTIVE_USER;
		}
		
		//昨天是否活跃，前天是否活跃，大前天是否活跃
		ActiveDetail detail = getActiveUserDetail(activeUserInfo, Constant.DAY);
		
		if(!detail.isYesterdayAct() && detail.isDayBeforYesterdayAct()){
			//如果昨天不曾活跃前天活跃即为流失
			return getDayLoseType(detail, activeUserInfo);
		}else if(detail.isYesterdayAct()){
			
			if(!detail.isDayBeforYesterdayAct() && detail.isDayOnceActived()){
				//如果前天不活跃，即为回流活跃
				return Constant.DAY_BACK_ACTIVE_USER;
			}else if(detail.isDayBeforYesterdayAct()){
				//如果昨天也活跃或者充值了，即为持续活跃
				return Constant.DAY_KEEP__ACTIVE;
			}
		}
		
		return -1;
	}


	private int getDayLoseType(ActiveDetail activeUserDetail, ActiveUserInfo activeUserInfo) {
		//如果前天为新增活跃用户，则为新增流失
		int lastDay2 = getDayBeforeYesterday();
		if(lastDay2 == activeUserInfo.getFirsttimeenter()){
			return Constant.DAY_INCREASE_ACTIVE_LOSE_USER;
		}
		
		//大前天是否充值
		if(!activeUserDetail.isLastDay3Act() && activeUserDetail.isDayOnceActived()){
			return Constant.DAY_BACK_ACTIVE_LOSE_USER;
		}
		
		if(activeUserDetail.isLastDay3Act()){
			return Constant.DAY_KEEP_ACTIVE_LOSE;
		}
		
		return -1;
	}

	/**
	 * 获得具体的周活跃类型
	 * 
	 * @param activeUserInfo  周活跃信息
	 * @return 具体周统计类型 -1不为任何类型
	 */
	private int getWeekActiveUserType(ActiveUserInfo activeUserInfo) {
		int lastActiveWeek = activeUserInfo.getLasttimeactiveweek1();
		int firstEnterWeek = activeUserInfo.getFirsttimeenter()/7;
		//如果最后一次活跃就是第一次进入服务器的当前周
		//即为新增活跃用户
		if(lastActiveWeek == firstEnterWeek && lastActiveWeek == getLastWeek()){
			return Constant.WEEK_INCREASE_ACTIVE_USER;
		}
		
		//上周是否活跃，上上周是否活跃，上上上周是否活跃
		ActiveDetail detail = getActiveUserDetail(activeUserInfo, Constant.WEEK);
		
		if(!detail.isLastWeek1Act() && detail.isLastWeek2Act()){
			//如果上周不曾活跃即为流失
			return getWeekLoseType(detail, activeUserInfo);
		}else if(detail.isLastWeek1Act()){
			
			if(!detail.isLastWeek2Act() && detail.isWeekOnceActived()){
				//如果上上周不活跃，即为回流活跃
				return Constant.WEEK_BACK_ACTIVE_USER;
			}else if(detail.isLastWeek2Act()){
				//如果上上周也活跃，即为持续活跃
				return Constant.WEEK_KEEP__ACTIVE;
			}
		}
		
		return -1;
	}

	private int getWeekLoseType(ActiveDetail detail, ActiveUserInfo activeUserInfo) {
		int lastActiveWeek = activeUserInfo.getLasttimeactiveweek1();
		int firstEnterWeek = activeUserInfo.getFirsttimeenter();
		
		//如果上上周为新增活跃用户，则为新增流失
		int lastWeek2 = getLast2Week();
		if(lastWeek2 == lastActiveWeek && lastWeek2 == firstEnterWeek){
			return Constant.WEEK_INCREASE_ACTIVE_LOSE_USER;
		}
		
		if(!detail.isLastWeek3Act() && detail.isWeekOnceActived()){
			return Constant.WEEK_BACK_ACTIVE_LOSE_USER;
		}
		
		if(detail.isLastWeek3Act()){
			return Constant.WEEK_KEEP_ACTIVE_LOSE;
		}
		
		return -1;
	}

	private int getDayChargeUserType(ActiveUserInfo activeUserInfo) {
		int lastCharge = activeUserInfo.getLasttimechargeday1();
		int firstEnter = activeUserInfo.getFirsttimeenter();
		if(lastCharge == getYesterday() && firstEnter == getYesterday()){
			return Constant.DAY_INCREASE_CHARGE_USER;
		}
		
		ChargeUserDetail detail = getChargeUserDetail(activeUserInfo, Constant.DAY);
		
		if(!detail.isYesterdayTrade() && detail.isDayBeforYesterdayTrade()){
			return getDayChargeUserLostType(detail, activeUserInfo);
		}else if(detail.isYesterdayTrade()){
			if(!detail.isDayBeforYesterdayTrade() && detail.isDayOnceTrade()){
				return Constant.DAY_BACK_CHARGE_USER;
			}else if(detail.isDayBeforYesterdayTrade()){
				return Constant.DAY_KEEP__CHARGE;
			}
		
		}
		
		return -1;
	}


	private int getDayChargeUserLostType(ChargeUserDetail detail, ActiveUserInfo activeUserInfo) {
		int dayBeforYesterday = getDayBeforeYesterday();
		if(dayBeforYesterday == activeUserInfo.getFirsttimeenter()){
			return Constant.DAY_INCREASE_CHARGE_LOSE_USER;
		}
		
		if(detail.isLastDay3Trade()){
			return Constant.DAY_KEEP_CHARGE_LOSE;
		}else if(detail.isLastDay3Trade() && detail.isDayOnceTrade()){
			return Constant.DAY_BACK_CHARGE_LOSE_USER;
		}
		
		return -1;
	}

	private int getWeekChargeUserType(ActiveUserInfo activeUserInfo) {
		int lastCharge = activeUserInfo.getLasttimechargeweek1();
		int firstEnter = activeUserInfo.getFirsttimeenter()/7;
		if(lastCharge == getLastWeek() && firstEnter == getLastWeek()){
			return Constant.WEEK_INCREASE_CHARGE_USER;
		}
		
		ChargeUserDetail detail = getChargeUserDetail(activeUserInfo, Constant.WEEK);
		
		if(!detail.isLastWeek1Trade() && detail.isLastWeek2Trade()){
			return getWeekChargeUserLostType(detail, activeUserInfo);
		}else if(detail.isLastWeek1Trade()){
			if(!detail.isLastWeek2Trade() && detail.isWeekOnceTrade()){
				return Constant.DAY_BACK_CHARGE_USER;
			}else if(detail.isLastWeek2Trade()){
				return Constant.DAY_KEEP__CHARGE;
			}
		
		}
		
		return -1;
	}

	private int getWeekChargeUserLostType(ChargeUserDetail detail, ActiveUserInfo activeUserInfo) {
		int lastWeek2 = getLast2Week();
		if(lastWeek2 == activeUserInfo.getFirsttimeenter()){
			return Constant.DAY_INCREASE_CHARGE_LOSE_USER;
		}
		
		if(detail.isLastWeek3Trade()){
			return Constant.DAY_KEEP_CHARGE_LOSE;
		}else if(!detail.isLastWeek3Trade() && detail.isWeekOnceTrade()){
			return Constant.DAY_BACK_CHARGE_LOSE_USER;
		}
	
		return -1;
	}

	private int getChargeRegionValue(int chargeNum) {
		
		return ValueUtile.searchValueSpace(Constant.TRADE_LIMIT, chargeNum);
		
	}
	
	private void doQuickPhote(xbean.ActiveUserInfo activeUserInfo, int userid) throws MarshalException {
		long consumeTotal = activeUserInfo.getCashfushiconsumetotalamount();
		long buyYBNum = activeUserInfo.getFushibuytotalamount();
		long sellYBNum = activeUserInfo.getFushiselltotalamount();
		
		//总使用符石
		//剩余流通符石
		//剩余RMB绑定符石
		//剩余系统绑定符石
		//交易平台挂单符石
		
		long usedTotal2 = activeUserInfo.getFushiconsumetotalamount();
		long fushi1 = activeUserInfo.getCashfushi();
		long fushi2 = activeUserInfo.getBindfushi();
		long fushi3 = activeUserInfo.getSysfushi();
		long fushi4 = activeUserInfo.getFushiinplatform();
		
		long chongZhiTotal = activeUserInfo.getChargetotalamountlong();
		
		long surplusNum = chongZhiTotal + buyYBNum - sellYBNum - consumeTotal;
		
		StringBuffer sb = new StringBuffer();
		String separator = ",";
		xbean.User user = getUserByID(userid);
		long lastLoginTime = 0;
		int _360Flag = 0;
		if(null == user){
			//TODO
		}else{
			long lastRoleid = user.getPrevloginroleid();
			xbean.Properties role = getRoleById(lastRoleid);
			if(0 == lastRoleid || null == role){
				lastLoginTime = user.getCreatetime();
				_360Flag = (System.currentTimeMillis()-lastLoginTime)>(365L*24*60*60*1000) ? 1 : 0;
			}else{
				lastLoginTime = role.getOnlinetime();
				_360Flag = ValueUtile.get360Flag(role.getOfflinetime(), role.getOnlinetime());
			}
		}	
		sb.append(Main.serverid + separator);
		sb.append(userid + separator);
		sb.append(lastLoginTime + separator);
		sb.append(_360Flag + separator);
		sb.append(chongZhiTotal + separator);
		sb.append(buyYBNum + separator);
		sb.append(sellYBNum + separator);
		sb.append(consumeTotal + separator);
		sb.append(surplusNum + separator);
		sb.append(usedTotal2 + separator);
		sb.append(fushi1 + separator);
		sb.append(fushi2 + separator);
		sb.append(fushi3 + separator);
		sb.append(fushi4);
		
		FileManager.getInstance().setQuickPhotoRecord(Constant.FU_SHI, sb.toString());
		
	}

	private Properties getRoleById(long lastRoleid) throws MarshalException {
		Dbx.Table roleTable = DbxManager.getInstance().getTableByName("properties");
		if(null != roleTable){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = roleTable.find(os.marshal(lastRoleid));
			 if(null != result){
				 xbean.Properties role = xbean.Pod.newProperties();
				 role.unmarshal(result);
				 return role;
			 }
		}
		return null;
	}

	private User getUserByID(int userid) throws MarshalException {
		Dbx.Table userTable = DbxManager.getInstance().getTableByName("user");
		if(null != userTable){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = userTable.find(os.marshal(userid));
			 if(null != result){
				 xbean.User user = xbean.Pod.newUser();
				 user.unmarshal(result);
				 return user;
			 }
		}
		return null;
	}
	
	
	

}
