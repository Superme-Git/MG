
package xbean;

public interface Itemrecoverlist extends mkdb.Bean {
	public Itemrecoverlist copy(); // deep clone
	public Itemrecoverlist toData(); // a Data instance
	public Itemrecoverlist toBean(); // a Bean instance
	public Itemrecoverlist toDataIf(); // a Data instance If need. else return this
	public Itemrecoverlist toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getUniqids(); // 物品唯一id列表
	public java.util.List<Long> getUniqidsAsData(); // 物品唯一id列表

}
