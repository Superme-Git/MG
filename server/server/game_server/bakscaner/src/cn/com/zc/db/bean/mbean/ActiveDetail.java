package cn.com.zc.db.bean.mbean;

public class ActiveDetail {
	
	private boolean isYesterdayAct;
	private boolean isDayBeforYesterdayAct;
	private boolean isLastDay3Act;
	private boolean isDayOnceActived;
	
	private boolean isLastWeek1Act;
	private boolean isLastWeek2Act;
	private boolean isLastWeek3Act;
	private boolean isWeekOnceActived;
	
	
	public boolean isDayOnceActived() {
		return isDayOnceActived;
	}
	public void setDayOnceActived(boolean isDayOnceActived) {
		this.isDayOnceActived = isDayOnceActived;
	}
	public boolean isWeekOnceActived() {
		return isWeekOnceActived;
	}
	public void setWeekOnceActived(boolean isWeekOnceActived) {
		this.isWeekOnceActived = isWeekOnceActived;
	}
	
	public boolean isYesterdayAct() {
		return isYesterdayAct;
	}
	public void setYesterdayAct(boolean isYesterdayAct) {
		this.isYesterdayAct = isYesterdayAct;
	}
	public boolean isDayBeforYesterdayAct() {
		return isDayBeforYesterdayAct;
	}
	public void setDayBeforYesterdayAct(boolean isDayBeforYesterdayAct) {
		this.isDayBeforYesterdayAct = isDayBeforYesterdayAct;
	}
	public boolean isLastDay3Act() {
		return isLastDay3Act;
	}
	public void setLastDay3Act(boolean isLastDay3Act) {
		this.isLastDay3Act = isLastDay3Act;
	}
	public boolean isLastWeek1Act() {
		return isLastWeek1Act;
	}
	public void setLastWeek1Act(boolean isLastWeek1Act) {
		this.isLastWeek1Act = isLastWeek1Act;
	}
	public boolean isLastWeek2Act() {
		return isLastWeek2Act;
	}
	public void setLastWeek2Act(boolean isLastWeek2Act) {
		this.isLastWeek2Act = isLastWeek2Act;
	}
	public boolean isLastWeek3Act() {
		return isLastWeek3Act;
	}
	public void setLastWeek3Act(boolean isLastWeek3Act) {
		this.isLastWeek3Act = isLastWeek3Act;
	}


}
