package cn.com.zc.db.walker;

import xbean.ActiveRoleInfo;
import xbean.ActiveUserInfo;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.Dbx;
import cn.com.zc.db.Constant;
import cn.com.zc.db.DbxManager;
import cn.com.zc.db.StatisDateManager;
import cn.com.zc.db.bean.mbean.ActiveDetail;
import cn.com.zc.db.util.FileManager;
import cn.com.zc.db.util.ValueUtile;

import com.goldhuman.Common.Marshal.MarshalException;
import com.goldhuman.Common.Marshal.OctetsStream;

public class ActiveRoleWalker extends Walker {

	public ActiveRoleWalker(Table tableMetaData) {
		super(tableMetaData);
	}

	@Override
	public boolean onRecordExecut(Object key, Object value) {
		StatisDateManager statisManager = StatisDateManager.getInstance();
		Long roleid = (Long) key;
		xbean.ActiveRoleInfo activeRoleInfo = (xbean.ActiveRoleInfo) value;
		
		//tmp
		//writeLine(roleid, activeRoleInfo);
		
		//历史角色数加一
		statisManager.addHistoryRoleNum(1);
		
		//判断是否是新增角色
		if(isIncrease(activeRoleInfo.getCreatetime())){
			statisManager.addIncreaseRoleNum(1);
		}
		
		//判断是周活跃角色的哪一种（返回-1不是任何类型）
		int weekActiveType = getWeekActiveRoleType(activeRoleInfo);
		if(-1 != weekActiveType){
			long combinationKey = createActiveRoleKey(roleid, activeRoleInfo, Constant.WEEK);
			statisManager.setActiveRoleMap(weekActiveType, combinationKey);
		}
		
		//判断是日活跃角色的哪一种（返回-1不是任何类型）
		int dayActiveType = getDayActiveRoleType(activeRoleInfo);
		if(-1 != dayActiveType){
			long combinationKey = createActiveRoleKey(roleid, activeRoleInfo, Constant.DAY);
			statisManager.setActiveRoleMap(dayActiveType, combinationKey);
		}
		
		xbean.ActiveUserInfo activeUserInfo = null;
		try {
			activeUserInfo = getActiveUserinfo(roleid);
			//doQuickPhoto(roleid, activeRoleInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null == activeUserInfo)
			return true;
		
		//统计角色日符石消耗量
		if(activeUserInfo.getLasttimefushiconsumeday() == getYesterday()){
			long combinationKey = createConsumeRoleKey(roleid, activeRoleInfo, activeUserInfo, Constant.DAY);
			int consumeNum = activeUserInfo.getLasttimefushiconsumedaytotalamount();
			statisManager.setConsumeRole(combinationKey, consumeNum, Constant.DAY);
			
		}
		
		//统计角色周符石消耗量
		if(activeUserInfo.getLasttimefushiconsumeweek() == getLastWeek()){
			long combinationKey = createConsumeRoleKey(roleid, activeRoleInfo,activeUserInfo, Constant.WEEK);
			int consumeNum = activeUserInfo.getLasttimefushiconsumeweektotalamount();
			statisManager.setConsumeRole(combinationKey, consumeNum, Constant.WEEK);
		}
		
		return true;
	}


	private ActiveUserInfo getActiveUserinfo(Long roleid) throws MarshalException {
		Dbx.Table properties = DbxManager.getInstance().getTableByName(
				"properties");
		Integer userid = 0;
		if (properties != null) {
			OctetsStream os = new OctetsStream();
			OctetsStream result = properties.find(os.marshal(roleid));
			if (null != result) {
				xbean.Properties xProperties = xbean.Pod.newProperties();
				xProperties.unmarshal(result);
				userid = xProperties.getUserid();
			}
			xbean.ActiveUserInfo xActiveUser = null;
			Dbx.Table activeRole = DbxManager.getInstance().getTableByName(
					"activeusertable");
			if (activeRole != null) {
				OctetsStream user = new OctetsStream();
				OctetsStream activeUser = activeRole.find(user.marshal(userid));
				if (null != activeUser) {
					xActiveUser = xbean.Pod.newActiveUserInfo();
					xActiveUser.unmarshal(activeUser);
					return xActiveUser;
				}
			}
		}
		return null;
	}

	private xbean.ActiveUserInfo doQuickPhoto(Long roleid, ActiveRoleInfo activeRoleInfo) throws Exception {
		Dbx.Table properties = DbxManager.getInstance().getTableByName("properties");
		long offlinetime = 0;
		long lastonline = 0;
		int userid = 0;
		long lastOnlineReal = 0;
		if(properties != null){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = properties.find(os.marshal(roleid));
			 if(null != result){
				 xbean.Properties xProperties = xbean.Pod.newProperties();
				 xProperties.unmarshal(result);
				 offlinetime = xProperties.getOfflinetime();
				 lastonline = xProperties.getOnlinetime();
				 lastOnlineReal = lastonline == -1 ? xProperties.getCreatetime() : lastonline;
				 userid = xProperties.getUserid();
			 }
		}
		
		int _360Flag = ValueUtile.get360Flag(offlinetime, lastonline);
		long consumeTotal = 0;
		long buyYBNum = 0;
		long sellYBNum = 0;
		
		//总使用符石
		//剩余流通符石
		//剩余RMB绑定符石
		//剩余系统绑定符石
		//交易平台挂单符石
		
		long usedTotal2 = 0;
		long fushi1 = 0;
		long fushi2 = 0;
		long fushi3 = 0;
		long fushi4 = 0;
		
		xbean.ActiveUserInfo xActiveUser = null;
		Dbx.Table activeRole = DbxManager.getInstance().getTableByName("activeusertable");
		long chongZhiTotal = 0;
		if(activeRole != null){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = activeRole.find(os.marshal(userid));
			 if(null != result){
				 xActiveUser = xbean.Pod.newActiveUserInfo();
				 xActiveUser.unmarshal(result);
				 chongZhiTotal = xActiveUser.getChargetotalamountlong();
				 consumeTotal = xActiveUser.getFushiconsumetotalamount();
				 buyYBNum = xActiveUser.getFushibuytotalamount();
				 sellYBNum = xActiveUser.getFushiselltotalamount();
				 
				 usedTotal2 = xActiveUser.getFushiconsumetotalamount();
				 fushi1 = xActiveUser.getCashfushi();
				 fushi2 = xActiveUser.getBindfushi();
				 fushi3 = xActiveUser.getSysfushi();
				 fushi4 = xActiveUser.getFushiinplatform();
			 }
		}
		
		long surplusNum = chongZhiTotal + buyYBNum - sellYBNum - consumeTotal;
		
		StringBuffer sb = new StringBuffer();
		String separator = ",";
		sb.append(userid + separator);
		sb.append(roleid + separator);
		sb.append(lastOnlineReal + separator);
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
		
		return xActiveUser;
		
	}

	private int getDayActiveRoleType(ActiveRoleInfo activeRoleInfo) {
		int lastActiveDay = activeRoleInfo.getLasttimeactiveday1();
		int createDay = activeRoleInfo.getCreatetime();
		//如果最后一次活跃就是第一次进入服务器的当前天
		//即为新增活跃角色
		if(lastActiveDay == createDay && lastActiveDay == getYesterday()){
			return Constant.DAY_INCREASE_ACTIVE_ROLE;
		}
		
		//昨天是否活跃，前天是否活跃，大前天是否活跃
		ActiveDetail detail = getActiveRoleDetail(activeRoleInfo, Constant.DAY);
		
		if(!detail.isYesterdayAct() && detail.isDayBeforYesterdayAct()){
			//如果昨天不曾活跃前天活跃即为流失
			return getDayLoseType(detail, activeRoleInfo);
		}else if(lastActiveDay == getYesterday()){
			
			if(!detail.isDayBeforYesterdayAct() && detail.isDayOnceActived()){
				//如果前天不活跃，而且没充值，即为回流活跃
				return Constant.DAY_BACK_ACTIVE_ROLE;
			}else if(detail.isDayBeforYesterdayAct()){
				//如果昨天也活跃或者充值了，即为持续活跃
				return Constant.DAY_KEEP_ACTIVE_ROLE;
			}
		}
		
		return -1;
	}
	

	private int getDayLoseType(ActiveDetail detail,	ActiveRoleInfo activeRoleInfo) {
		//如果前天为新增活跃用户，则为新增流失
		int lastDay2 = getDayBeforeYesterday();
		if(lastDay2 == activeRoleInfo.getCreatetime()){
			return Constant.DAY_INCREASE_ACTIVE_LOSE_ROLE;
		}
		
		if(!detail.isLastDay3Act() && detail.isDayOnceActived()){
			return Constant.DAY_BACK_ACTIVE_LOSE_ROLE;
		}
		
		if(detail.isLastDay3Act()){
			return Constant.DAY_KEEP_ACTIVE_ROLE;
		}
		
		return -1;
	}

	private int getWeekActiveRoleType(ActiveRoleInfo activeRoleInfo) {
		int lastActiveWeek = activeRoleInfo.getLasttimeactiveweek1();
		int firstEnterWeek = activeRoleInfo.getCreatetime()/7;
		//如果最后一次活跃就是第一次进入服务器的当前周
		//即为新增活跃用户
		if(lastActiveWeek == firstEnterWeek && lastActiveWeek == getLastWeek()){
			return Constant.WEEK_INCREASE_ACTIVE_ROLE;
		}
		
		//上周是否活跃，上上周是否活跃，上上上周是否活跃
		ActiveDetail detail = getActiveRoleDetail(activeRoleInfo, Constant.WEEK);
		
		if(!detail.isLastWeek1Act() && detail.isLastWeek2Act()){
			//如果上周不曾活跃即为流失
			return getWeekLoseType(detail, activeRoleInfo);
		}else if(detail.isLastWeek1Act()){
			
			if(!detail.isLastWeek2Act() && detail.isWeekOnceActived()){
				//如果上上周不活跃，即为回流活跃
				return Constant.WEEK_BACK_ACTIVE_ROLE;
			}else if(detail.isLastWeek2Act()){
				//如果上上周也活跃，即为持续活跃
				return Constant.WEEK_KEEP__ACTIVE_ROLE;
			}
		}
		
		return -1;
	}
	
	private int getWeekLoseType(ActiveDetail detail, ActiveRoleInfo activeRoleInfo) {
		int lastActiveWeek = activeRoleInfo.getLasttimeactiveweek1();
		int firstEnterWeek = activeRoleInfo.getCreatetime();
		
		//如果上上周为新增活跃用户，则为新增流失
		int lastWeek2 = getLast2Week();
		if(lastWeek2 == lastActiveWeek && lastWeek2 == firstEnterWeek){
			return Constant.WEEK_INCREASE_ACTIVE_LOSE_ROLE;
		}
		
		if(!detail.isLastWeek3Act() && detail.isWeekOnceActived()){
			return Constant.WEEK_BACK_ACTIVE_LOSE_ROLE;
		}
		
		if(detail.isLastWeek3Act()){
			return Constant.WEEK_KEEP_ACTIVE_LOSE_ROLE;
		}
		
		return -1;
	}

	/**
	 * 生成一个活跃角色的组合key
	 * Long型的低32位是key的有效位
	 * 每8位标示一个字段：等级段+门派+主角+在线时长
	 * 
	 * @param roleid
	 * @param activeRoleInfo
	 * @return
	 */
	private long createActiveRoleKey(Long roleid, ActiveRoleInfo activeRoleInfo, int type) {
		xbean.Properties propr = null;
		try {
			propr = getRoleProperties(roleid);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		if(null == propr)
			return -1;
		
		int level = propr.getLevel();
		int school = propr.getSchool();
		int shape = propr.getShape();
		
		int onlineTime = 0;
		if(type == Constant.DAY){
			onlineTime = activeRoleInfo.getDayonlinetime();
		}else if(type == Constant.WEEK){
			onlineTime = activeRoleInfo.getWeekonlinetime();
		}
		
		int levelIndex = ValueUtile.binSearchValueSpace(Constant.ROLE_LEVEL_INTERAL, level, Constant.ROLE_LEVEL_SPACE) + 1;
		int onlineSpace = ValueUtile.searchValueSpace(Constant.ONLINE_TIME_SPACE, onlineTime) + 1;
		
		levelIndex <<= 24;
		school <<= 16;
		shape <<= 8;
		
		return levelIndex + school + shape + onlineSpace;
	}
	
	private xbean.Properties getRoleProperties(long roleid) throws Exception{
		Dbx.Table properties = DbxManager.getInstance().getTableByName("properties");
		xbean.Properties xProperties = null;
		if(properties != null){
			 OctetsStream os = new OctetsStream();
			 OctetsStream result = properties.find(os.marshal(roleid));
			 if(null != result){
				 xProperties = xbean.Pod.newProperties();
				 xProperties.unmarshal(result);
			 }
		}
		
		return xProperties;
	}

	/**
	 * 生成一个符石消耗的组合key
	 * Long型的低32位是key的有效位
	 *  8位                6位            6位     6位          6位
	 * 等级段+消耗额度段+门派+主角+在线时长
	 *  
	 * @param roleid
	 * @param activeRoleInfo
	 * @return
	 */
	private long createConsumeRoleKey(Long roleid, ActiveRoleInfo activeRoleInfo, ActiveUserInfo activeUserInfo, int type) {
		xbean.Properties propr = null;
		try {
			propr = getRoleProperties(roleid);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		if(null == propr)
			return -1;
		
		int level = propr.getLevel();
		int school = propr.getSchool();
		int shape = propr.getShape();
		
		int onlineTime = 0;
		if(type == Constant.DAY){
			onlineTime = activeRoleInfo.getDayonlinetime();
		}else if(type == Constant.WEEK){
			onlineTime = activeRoleInfo.getWeekonlinetime();
		}
		
		int consumeNum = 0;
		if(type == Constant.DAY){
			consumeNum = activeUserInfo.getLasttimefushiconsumedaytotalamount();
		}else if(type == Constant.WEEK){
			consumeNum = activeUserInfo.getLasttimefushiconsumeweektotalamount();
		}
		
		int levelIndex = ValueUtile.binSearchValueSpace(Constant.ROLE_LEVEL_INTERAL, level, Constant.ROLE_LEVEL_SPACE) + 1;
		int onlineSpace = ValueUtile.searchValueSpace(Constant.ONLINE_TIME_SPACE, onlineTime) + 1;
		int consumeSpace = ValueUtile.searchValueSpace(Constant.TRADE_LIMIT, consumeNum);
		
		levelIndex <<= 24;
		consumeSpace <<= 18;
		school <<= 12;
		shape <<= 6;
		
		return levelIndex + school + shape + onlineSpace;
	}



}
