
package xbean;

public interface ChangeSchoolRecord extends mkdb.Bean {
	public ChangeSchoolRecord copy(); // deep clone
	public ChangeSchoolRecord toData(); // a Data instance
	public ChangeSchoolRecord toBean(); // a Bean instance
	public ChangeSchoolRecord toDataIf(); // a Data instance If need. else return this
	public ChangeSchoolRecord toBeanIf(); // a Bean instance If need. else return this

	public int getShape(); // 造型
	public int getSchool(); // 职业
	public long getTime(); // 转职时间

	public void setShape(int _v_); // 造型
	public void setSchool(int _v_); // 职业
	public void setTime(long _v_); // 转职时间
}
