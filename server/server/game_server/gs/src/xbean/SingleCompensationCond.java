
package xbean;

public interface SingleCompensationCond extends mkdb.Bean {
	public SingleCompensationCond copy(); // deep clone
	public SingleCompensationCond toData(); // a Data instance
	public SingleCompensationCond toBean(); // a Bean instance
	public SingleCompensationCond toDataIf(); // a Data instance If need. else return this
	public SingleCompensationCond toBeanIf(); // a Bean instance If need. else return this

	public int getCondid(); // 条件的id
	public java.util.List<String> getCondparams(); // 条件的参数
	public java.util.List<String> getCondparamsAsData(); // 条件的参数

	public void setCondid(int _v_); // 条件的id
}
