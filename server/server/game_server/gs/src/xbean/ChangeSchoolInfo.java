
package xbean;

public interface ChangeSchoolInfo extends mkdb.Bean {
	public ChangeSchoolInfo copy(); // deep clone
	public ChangeSchoolInfo toData(); // a Data instance
	public ChangeSchoolInfo toBean(); // a Bean instance
	public ChangeSchoolInfo toDataIf(); // a Data instance If need. else return this
	public ChangeSchoolInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.ChangeSchoolRecord> getRecords(); // 转职记录列表
	public java.util.List<xbean.ChangeSchoolRecord> getRecordsAsData(); // 转职记录列表
	public int getChangeweaponcount(); // 当前转职后,转换武器的次数
	public int getChangegemcount(); // 当前转职后,转换宝石的次数

	public void setChangeweaponcount(int _v_); // 当前转职后,转换武器的次数
	public void setChangegemcount(int _v_); // 当前转职后,转换宝石的次数
}
