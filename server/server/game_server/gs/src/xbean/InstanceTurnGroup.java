
package xbean;

public interface InstanceTurnGroup extends mkdb.Bean {
	public InstanceTurnGroup copy(); // deep clone
	public InstanceTurnGroup toData(); // a Data instance
	public InstanceTurnGroup toBean(); // a Bean instance
	public InstanceTurnGroup toDataIf(); // a Data instance If need. else return this
	public InstanceTurnGroup toBeanIf(); // a Bean instance If need. else return this

	public int getTurngroup(); // 轮换组id
	public int getTurnindex(); // 轮换组当前id
	public int getTurntype(); // 轮换类型

	public void setTurngroup(int _v_); // 轮换组id
	public void setTurnindex(int _v_); // 轮换组当前id
	public void setTurntype(int _v_); // 轮换类型
}
