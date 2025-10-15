
package xbean;

public interface WinnerRoleRecord extends mkdb.Bean {
	public WinnerRoleRecord copy(); // deep clone
	public WinnerRoleRecord toData(); // a Data instance
	public WinnerRoleRecord toBean(); // a Bean instance
	public WinnerRoleRecord toDataIf(); // a Data instance If need. else return this
	public WinnerRoleRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public int getScore(); // 记录个人的成绩
	public long getTime(); // 记录成绩变化的时间
	public int getAwardflag(); // 是否领取过奖励的标记,0为没有领取   1为领取过了

	public void setRoleid(long _v_); // 
	public void setScore(int _v_); // 记录个人的成绩
	public void setTime(long _v_); // 记录成绩变化的时间
	public void setAwardflag(int _v_); // 是否领取过奖励的标记,0为没有领取   1为领取过了
}
