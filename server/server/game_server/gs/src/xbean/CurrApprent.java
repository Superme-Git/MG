
package xbean;

public interface CurrApprent extends mkdb.Bean {
	public CurrApprent copy(); // deep clone
	public CurrApprent toData(); // a Data instance
	public CurrApprent toBean(); // a Bean instance
	public CurrApprent toDataIf(); // a Data instance If need. else return this
	public CurrApprent toBeanIf(); // a Bean instance If need. else return this

	public int getTitleid(); // 徒弟的当前称谓
	public int getLevel(); // 拜师时候的等级
	public xbean.HasApprent getApprentinfo(); // 徒弟的基本信息
	public java.util.Map<Integer, xbean.ApprentceChieve> getAchievement(); // 徒弟的各项成就
	public java.util.Map<Integer, xbean.ApprentceChieve> getAchievementAsData(); // 徒弟的各项成就
	public int getMastercomment(); // 师傅对徒弟的评价
	public int getApprentcomment(); // 徒弟对师傅的评价
	public long getBaishitime(); // 

	public void setTitleid(int _v_); // 徒弟的当前称谓
	public void setLevel(int _v_); // 拜师时候的等级
	public void setMastercomment(int _v_); // 师傅对徒弟的评价
	public void setApprentcomment(int _v_); // 徒弟对师傅的评价
	public void setBaishitime(long _v_); // 
}
