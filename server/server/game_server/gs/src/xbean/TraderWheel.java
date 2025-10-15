
package xbean;

public interface TraderWheel extends mkdb.Bean {
	public TraderWheel copy(); // deep clone
	public TraderWheel toData(); // a Data instance
	public TraderWheel toBean(); // a Bean instance
	public TraderWheel toDataIf(); // a Data instance If need. else return this
	public TraderWheel toBeanIf(); // a Bean instance If need. else return this

	public int getBoxtype(); // 宝箱类型
	public int getItemindex(); // 物品列的索引

	public void setBoxtype(int _v_); // 宝箱类型
	public void setItemindex(int _v_); // 物品列的索引
}
