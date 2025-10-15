
package xbean;

public interface ItemUse extends mkdb.Bean {
	public ItemUse copy(); // deep clone
	public ItemUse toData(); // a Data instance
	public ItemUse toBean(); // a Bean instance
	public ItemUse toDataIf(); // a Data instance If need. else return this
	public ItemUse toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.ItemUseCount> getIteminfo(); // 每个道具的使用次数
	public java.util.Map<Integer, xbean.ItemUseCount> getIteminfoAsData(); // 每个道具的使用次数

}
