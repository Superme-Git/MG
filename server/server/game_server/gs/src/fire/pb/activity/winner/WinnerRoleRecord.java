package fire.pb.activity.winner;

public class WinnerRoleRecord {
	private long roleid;//
	private int score=0;//记录个人的成绩
	private long time;//记录成绩变化的时间
	private int awardflag=0;// 是否领取过奖励的标记,0为没有领取   1为领取过了
	
	
	public WinnerRoleRecord(long roleid){
		this.roleid=roleid;
	}
	
	
	public long getRoleid() {
		return roleid;
	}
	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getAwardflag() {
		return awardflag;
	}
	public void setAwardflag(int awardflag) {
		this.awardflag = awardflag;
	}
	
}
