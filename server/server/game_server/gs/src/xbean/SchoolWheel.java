
package xbean;

public interface SchoolWheel extends mkdb.Bean {
	public SchoolWheel copy(); // deep clone
	public SchoolWheel toData(); // a Data instance
	public SchoolWheel toBean(); // a Bean instance
	public SchoolWheel toDataIf(); // a Data instance If need. else return this
	public SchoolWheel toBeanIf(); // a Bean instance If need. else return this

	public int getAwardid(); // 奖励id
	public int getItemindex(); // 物品列的索引

	public void setAwardid(int _v_); // 奖励id
	public void setItemindex(int _v_); // 物品列的索引
}
