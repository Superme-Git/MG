
package xbean;

public interface Petrecoverlist extends mkdb.Bean {
	public Petrecoverlist copy(); // deep clone
	public Petrecoverlist toData(); // a Data instance
	public Petrecoverlist toBean(); // a Bean instance
	public Petrecoverlist toDataIf(); // a Data instance If need. else return this
	public Petrecoverlist toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getUniqids(); // 宠物唯一id列表
	public java.util.List<Long> getUniqidsAsData(); // 宠物唯一id列表

}
