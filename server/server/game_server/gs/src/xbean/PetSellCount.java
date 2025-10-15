
package xbean;

public interface PetSellCount extends mkdb.Bean {
	public PetSellCount copy(); // deep clone
	public PetSellCount toData(); // a Data instance
	public PetSellCount toBean(); // a Bean instance
	public PetSellCount toDataIf(); // a Data instance If need. else return this
	public PetSellCount toBeanIf(); // a Bean instance If need. else return this

	public int getCount(); // 次数 *byte够了
	public long getResettime(); // 重置时间

	public void setCount(int _v_); // 次数 *byte够了
	public void setResettime(long _v_); // 重置时间
}
