
package xbean;

public interface FormBean extends mkdb.Bean {
	public FormBean copy(); // deep clone
	public FormBean toData(); // a Data instance
	public FormBean toBean(); // a Bean instance
	public FormBean toDataIf(); // a Data instance If need. else return this
	public FormBean toBeanIf(); // a Bean instance If need. else return this

	public int getActivetimes(); // 
	public int getLevel(); // 等级
	public int getExp(); // 经验 by changhao

	public void setActivetimes(int _v_); // 
	public void setLevel(int _v_); // 等级
	public void setExp(int _v_); // 经验 by changhao
}
