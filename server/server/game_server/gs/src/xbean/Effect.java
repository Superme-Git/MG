
package xbean;

public interface Effect extends mkdb.Bean {
	public Effect copy(); // deep clone
	public Effect toData(); // a Data instance
	public Effect toBean(); // a Bean instance
	public Effect toDataIf(); // a Data instance If need. else return this
	public Effect toBeanIf(); // a Bean instance If need. else return this

	public int getEffecttypeid(); // 目标加成属性类型Id
	public float getValue(); // 加成的值
	public boolean getEnable(); // 是否生效

	public void setEffecttypeid(int _v_); // 目标加成属性类型Id
	public void setValue(float _v_); // 加成的值
	public void setEnable(boolean _v_); // 是否生效
}
