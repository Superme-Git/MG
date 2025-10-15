package cn.com.zc.db.bean.mbean;

public class ChargeUserDetail {
	
	private boolean isYesterdayTrade;
	private boolean isDayBeforYesterdayTrade;
	private boolean isLastDay3Trade;
	private boolean isDayOnceTrade;
	
	private boolean isLastWeek1Trade;
	private boolean isLastWeek2Trade;
	private boolean isLastWeek3Trade;
	private boolean isWeekOnceTrade;
	
	public boolean isDayOnceTrade() {
		return isDayOnceTrade;
	}
	public void setDayOnceTrade(boolean isDayOnceTrade) {
		this.isDayOnceTrade = isDayOnceTrade;
	}
	public boolean isWeekOnceTrade() {
		return isWeekOnceTrade;
	}
	public void setWeekOnceTrade(boolean isWeekOnceTrade) {
		this.isWeekOnceTrade = isWeekOnceTrade;
	}
	
	public boolean isYesterdayTrade() {
		return isYesterdayTrade;
	}
	public void setYesterdayTrade(boolean isYesterdayCharge) {
		this.isYesterdayTrade = isYesterdayCharge;
	}
	public boolean isDayBeforYesterdayTrade() {
		return isDayBeforYesterdayTrade;
	}
	public void setDayBeforYesterdayTrade(boolean isDayBeforYesterdayCharge) {
		this.isDayBeforYesterdayTrade = isDayBeforYesterdayCharge;
	}
	public boolean isLastDay3Trade() {
		return isLastDay3Trade;
	}
	public void setLastDay3Trade(boolean isLastDay3Charge) {
		this.isLastDay3Trade = isLastDay3Charge;
	}
	public boolean isLastWeek1Trade() {
		return isLastWeek1Trade;
	}
	public void setLastWeek1Trade(boolean isLastWeek1Charge) {
		this.isLastWeek1Trade = isLastWeek1Charge;
	}
	public boolean isLastWeek2Trade() {
		return isLastWeek2Trade;
	}
	public void setLastWeek2Trade(boolean isLastWeek2Charge) {
		this.isLastWeek2Trade = isLastWeek2Charge;
	}
	public boolean isLastWeek3Trade() {
		return isLastWeek3Trade;
	}
	public void setLastWeek3Trade(boolean isLastWeek3Charge) {
		this.isLastWeek3Trade = isLastWeek3Charge;
	}

}
