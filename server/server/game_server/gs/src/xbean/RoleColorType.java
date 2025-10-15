
package xbean;

public interface RoleColorType extends mkdb.Bean {
	public RoleColorType copy(); // deep clone
	public RoleColorType toData(); // a Data instance
	public RoleColorType toBean(); // a Bean instance
	public RoleColorType toDataIf(); // a Data instance If need. else return this
	public RoleColorType toBeanIf(); // a Bean instance If need. else return this

	public int getColorpos1(); // 染色部位1
	public int getColorpos2(); // 染色部位2

	public void setColorpos1(int _v_); // 染色部位1
	public void setColorpos2(int _v_); // 染色部位2
}
