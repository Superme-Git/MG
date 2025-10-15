
package xbean;

public interface WelfareBean extends mkdb.Bean {
	public WelfareBean copy(); // deep clone
	public WelfareBean toData(); // a Data instance
	public WelfareBean toBean(); // a Bean instance
	public WelfareBean toDataIf(); // a Data instance If need. else return this
	public WelfareBean toBeanIf(); // a Bean instance If need. else return this

	public int getBuytimes(); // 购买次数
	public long getLastbuytime(); // 

	public void setBuytimes(int _v_); // 购买次数
	public void setLastbuytime(long _v_); // 
}
