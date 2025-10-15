
package xbean;

public interface TransfromByItemData extends mkdb.Bean {
	public TransfromByItemData copy(); // deep clone
	public TransfromByItemData toData(); // a Data instance
	public TransfromByItemData toBean(); // a Bean instance
	public TransfromByItemData toDataIf(); // a Data instance If need. else return this
	public TransfromByItemData toBeanIf(); // a Bean instance If need. else return this

	public int getUseitemid(); // 
	public int getTransformid(); // 
	public long getValiddate(); // 

	public void setUseitemid(int _v_); // 
	public void setTransformid(int _v_); // 
	public void setValiddate(long _v_); // 
}
