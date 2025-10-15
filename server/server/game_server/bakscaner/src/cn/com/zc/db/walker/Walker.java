package cn.com.zc.db.walker;

import xbean.ActiveRoleInfo;
import mkdb.Storage.IWalk;
import mkdb.util.DatabaseMetaData.Table;
import mkdb.util.DatabaseMetaData.Type;
import cn.com.zc.db.Constant;
import cn.com.zc.db.bean.mbean.ActiveDetail;
import cn.com.zc.db.bean.mbean.ChargeUserDetail;

public abstract class Walker implements IWalk {

	private Table table;
	
	private final Type keyType;
	private final Type valueType;
	
	private int queryType;
	
	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public Walker(Table tableMetaData) {
		this.table = tableMetaData;
		this.keyType = tableMetaData.getKeyType();
		this.valueType = tableMetaData.getValueType();
	}
	
	public String getTableName(){
		return table.getName();
	}

	@Override
	public boolean onRecord(byte[] _key, byte[] _data) {
		Object key = this.keyType.unmarshal(_key);
		Object value = this.valueType.unmarshal(_data);
		
		return onRecordExecut(key, value);
	}
	

	public boolean isLast3Correct(int lasttime3, int type){
		boolean result = false;
		if(type == Constant.DAY){
			result = getDayBeforeYesterday()-1 == lasttime3? true:false;
		}else if(type == Constant.WEEK){
			result = getLast2Week()-1 == lasttime3? true:false;
		}
		return result;
	}
	
	public static int getLast2Week(){
		return getLastWeek() - 1;
	}
	
	public static int getLastWeek(){
		long currentTime = System.currentTimeMillis();
		int currentWeek = (int) (currentTime/(1000*60*60*24*7));
		return currentWeek - 1;
	}
	
	public static int getYesterday(){
		long currentTime = System.currentTimeMillis();
		int today = (int) (currentTime/(1000*60*60*24));
		return today - 1;
	}
	
	public static int getDayBeforeYesterday(){
		return getYesterday()-1;
	}

	public boolean isIncrease(int firsttimeenter) {
		int currentDay = getYesterday();
		if(firsttimeenter == currentDay){
			return true;
		}
		return false;
	}
	
	public ActiveDetail getActiveRoleDetail(ActiveRoleInfo activeRoleInfo, int type) {
		ActiveDetail detail = new ActiveDetail();
		if(type == Constant.DAY){
			boolean isYesterdayAct = activeRoleInfo.getLasttimeactiveday1() == getYesterday()? true:false;
			boolean isDayBeforYesAct = false;
			boolean isLastDay3Act = false;
			
			if(!isYesterdayAct){
				isDayBeforYesAct = activeRoleInfo.getLasttimeactiveday1() == getDayBeforeYesterday()? true:false;
				if(!isDayBeforYesAct){
					isLastDay3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveday1(), Constant.DAY);
				}else{
					isLastDay3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveday2(), Constant.DAY);
				}
			}else{
				isDayBeforYesAct = activeRoleInfo.getLasttimeactiveday2() == getDayBeforeYesterday() ? true:false;
				if(!isDayBeforYesAct){
					isLastDay3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveday2(), Constant.DAY);
				}
			}
			
			detail.setDayBeforYesterdayAct(isDayBeforYesAct);
			detail.setLastDay3Act(isLastDay3Act);
			detail.setYesterdayAct(isYesterdayAct);
			detail.setDayOnceActived(activeRoleInfo.getLasttimeactiveday2() != 0);
			
		}else if(type == Constant.WEEK){
			boolean isLastWeek1Act = activeRoleInfo.getLasttimeactiveweek1() == getLastWeek()? true:false;
			boolean isLastWeek2Act = false;
			boolean isLastWeek3Act = false;
			
			if(!isLastWeek1Act){
				isLastWeek2Act = activeRoleInfo.getLasttimeactiveweek1() == getLast2Week() ? true:false;
				if(!isLastWeek2Act){
					isLastWeek3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveweek1(), Constant.WEEK);
				}else{
					isLastWeek3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveweek2(), Constant.WEEK);
				}
			}else{
				isLastWeek2Act = activeRoleInfo.getLasttimeactiveweek2() == getLast2Week() ? true:false;
				if(!isLastWeek2Act){
					isLastWeek3Act = isLast3Correct(activeRoleInfo.getLasttimeactiveweek2(), Constant.WEEK);
				}
			}
			
			detail.setLastWeek1Act(isLastWeek1Act);
			detail.setLastWeek2Act(isLastWeek2Act);
			detail.setLastWeek3Act(isLastWeek3Act);
			detail.setWeekOnceActived(activeRoleInfo.getLasttimeactiveweek2() != 0);
			
		}
		
		return detail;
	}
	

	public ChargeUserDetail getChargeUserDetail(xbean.ActiveUserInfo activeUserInfo, int type){
		ChargeUserDetail detail = new ChargeUserDetail();
		if(type == Constant.DAY){
			boolean isYesterdayCharge = activeUserInfo.getLasttimechargeday1() == getYesterday()? true:false;
			boolean isDayBeforYesCharge = false;
			boolean isLastDay3Charge = false;
			
			if(!isYesterdayCharge){
				isDayBeforYesCharge = activeUserInfo.getLasttimechargeday1() == getDayBeforeYesterday()? true:false;
				if(!isDayBeforYesCharge){
					isLastDay3Charge = isLast3Correct(activeUserInfo.getLasttimechargeday1(), Constant.DAY);
				}else{
					isLastDay3Charge = isLast3Correct(activeUserInfo.getLasttimechargeday2(), Constant.DAY);
				}
			}else{
				isDayBeforYesCharge = activeUserInfo.getLasttimechargeday2() == getDayBeforeYesterday()? true:false;
				if(!isDayBeforYesCharge){
					isLastDay3Charge = isLast3Correct(activeUserInfo.getLasttimechargeday2(), Constant.DAY);
				}
			}
			
			detail.setDayBeforYesterdayTrade(isDayBeforYesCharge);
			detail.setLastDay3Trade(isLastDay3Charge);
			detail.setYesterdayTrade(isYesterdayCharge);
			detail.setDayOnceTrade(activeUserInfo.getLasttimechargeday2() != 0);
			
		}else if(type == Constant.WEEK){
			boolean isLastWeek1Charge = activeUserInfo.getLasttimechargeweek1() == getLastWeek()? true:false;
			boolean isLastWeek2Charge =false;
			boolean isLastWeek3Charge = false;
			if(!isLastWeek1Charge){
				isLastWeek2Charge = activeUserInfo.getLasttimechargeweek1() == getLast2Week()? true:false;
				if(!isLastWeek2Charge){
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek1(), Constant.WEEK);
				}else{
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek2(), Constant.WEEK);
				}
			}else{
				isLastWeek2Charge = activeUserInfo.getLasttimechargeweek2() == getLast2Week()? true:false;
				if(!isLastWeek2Charge){
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek2(), Constant.WEEK);
				}
			}
			
			detail.setLastWeek1Trade(isLastWeek1Charge);
			detail.setLastWeek2Trade(isLastWeek2Charge);
			detail.setLastWeek3Trade(isLastWeek3Charge);
			detail.setDayOnceTrade(activeUserInfo.getLasttimechargeweek2() != 0);
			
		}
		
		return detail;
	}
	
	
	public ActiveDetail getActiveUserDetail(xbean.ActiveUserInfo activeUserInfo, int type){
		ActiveDetail detail = new ActiveDetail();
		if(type == Constant.DAY){
			boolean isYesterdayOnline = activeUserInfo.getLasttimeactiveday1() == getYesterday()? true:false;
			boolean isYesterdayCharge = activeUserInfo.getLasttimechargeday1() == getYesterday() ? true:false;
			
			boolean isDayBeforYesterdayOnline = false;
			boolean isDayBeforYesterdayCharge = false;
			
			boolean isLastDay3Online = false;
			boolean isLastDay3Charge = false;
			
			if(!isYesterdayOnline){
				isDayBeforYesterdayOnline = activeUserInfo.getLasttimeactiveday1() == getDayBeforeYesterday()? true: false;
				if(!isDayBeforYesterdayOnline){
					isLastDay3Online = isLast3Correct(activeUserInfo.getLasttimeactiveday1(), Constant.DAY);
				}else{
					isLastDay3Online = isLast3Correct(activeUserInfo.getLasttimeactiveday2(), Constant.DAY);
				}
			}else{
				isDayBeforYesterdayOnline = activeUserInfo.getLasttimeactiveday2() == getDayBeforeYesterday()? true:false;
				if(!isDayBeforYesterdayOnline){
					isLastDay3Online = isLast3Correct(activeUserInfo.getLasttimeactiveday2(), Constant.DAY);
				}
			}
			
			if(!isYesterdayCharge){
				isDayBeforYesterdayCharge = activeUserInfo.getLasttimechargeday1() == getDayBeforeYesterday()? true:false;
				if(!isDayBeforYesterdayCharge){
					isLastDay3Charge = isLast3Correct(activeUserInfo.getLasttimechargeday1(), Constant.DAY);
				}else{
					isLastDay3Charge = isLast3Correct(activeUserInfo.getLasttimechargeday2(), Constant.DAY);
				}
			}
			
			detail.setDayBeforYesterdayAct(isDayBeforYesterdayOnline || isDayBeforYesterdayCharge);
			detail.setLastDay3Act(isLastDay3Online || isLastDay3Charge);
			detail.setYesterdayAct(isYesterdayOnline || isYesterdayCharge);
			boolean isDayOnceActived = activeUserInfo.getLasttimeactiveday2() != 0 || activeUserInfo.getLasttimechargeday2() != 0;
			detail.setDayOnceActived(isDayOnceActived);
			
		}else if(type == Constant.WEEK){
			boolean isLastWeek1Online = activeUserInfo.getLasttimeactiveweek1() == getLastWeek()? true:false;
			boolean isLastWeek1Charge = activeUserInfo.getLasttimechargeweek1() == getLastWeek()? true:false;
			
			boolean isLastWeek2Online = false;
			boolean isLastWeek2Charge = false;
			
			boolean isLastWeek3Online = false;
			boolean isLastWeek3Charge = false;
			
			if(!isLastWeek1Online){
				isLastWeek2Online = activeUserInfo.getLasttimeactiveweek1() == getLast2Week()? true:false;
				if(!isLastWeek2Online){
					isLastWeek3Online = isLast3Correct(activeUserInfo.getLasttimeactiveweek1(), Constant.WEEK);
				}else{
					isLastWeek3Online = isLast3Correct(activeUserInfo.getLasttimeactiveweek2(), Constant.WEEK);
				}
			}else{
				isLastWeek2Online = activeUserInfo.getLasttimeactiveweek2() == getLast2Week()? true:false;
				if(!isLastWeek2Online){
					isLastWeek3Online = isLast3Correct(activeUserInfo.getLasttimeactiveweek2(), Constant.WEEK);
				}
			}
			
			if(!isLastWeek1Charge){
				isLastWeek2Charge = activeUserInfo.getLasttimechargeweek1() == getLast2Week()? true:false;
				if(!isLastWeek2Charge){
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek1(), Constant.WEEK);
				}else{
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek2(), Constant.WEEK);
				}
			}else{
				isLastWeek2Charge = activeUserInfo.getLasttimechargeweek2() == getLast2Week()? true:false;
				if(!isLastWeek2Charge){
					isLastWeek3Charge = isLast3Correct(activeUserInfo.getLasttimechargeweek2(), Constant.WEEK);
				}
			}
			
			detail.setLastWeek1Act(isLastWeek1Charge || isLastWeek1Online);
			detail.setLastWeek2Act(isLastWeek2Charge || isLastWeek2Online);
			detail.setLastWeek3Act(isLastWeek3Charge || isLastWeek3Online);
			boolean isWeekOnceActived = activeUserInfo.getLasttimeactiveweek2() != 0 || activeUserInfo.getLasttimechargeweek2() != 0;
			detail.setWeekOnceActived(isWeekOnceActived);
			
		}
		
		return detail;
	}
	
	public abstract boolean onRecordExecut(Object key, Object value);

}
