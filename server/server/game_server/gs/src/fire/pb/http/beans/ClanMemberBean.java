package fire.pb.http.beans;

public class ClanMemberBean {
	private long roleid;
	private String name;//角色名称
	private int level;//角色等级
	private int school;//角色职业
	private int position;//公会职位
	private int currentContribution;//现有贡献
	private int historyContribution;//历史贡献
	private int weekContribution;//本周贡献
	private String addtime;//加入公会时间
	
	public long getRoleid() {
		return roleid;
	}
	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSchool() {
		return school;
	}
	public void setSchool(int school) {
		this.school = school;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getCurrentContribution() {
		return currentContribution;
	}
	public void setCurrentContribution(int currentContribution) {
		this.currentContribution = currentContribution;
	}
	public int getHistoryContribution() {
		return historyContribution;
	}
	public void setHistoryContribution(int historyContribution) {
		this.historyContribution = historyContribution;
	}
	public int getWeekContribution() {
		return weekContribution;
	}
	public void setWeekContribution(int weekContribution) {
		this.weekContribution = weekContribution;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
	


}
