
package xbean;

public interface DiscardItem extends mkdb.Bean {
	public DiscardItem copy(); // deep clone
	public DiscardItem toData(); // a Data instance
	public DiscardItem toBean(); // a Bean instance
	public DiscardItem toDataIf(); // a Data instance If need. else return this
	public DiscardItem toBeanIf(); // a Bean instance If need. else return this

	public xbean.Item getItem(); // 物品固有属性
	public long getDeletedate(); // 删除日期

	public void setDeletedate(long _v_); // 删除日期
}
