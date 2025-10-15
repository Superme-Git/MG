
package xbean;

public interface RecoveryAttr extends mkdb.Bean {
	public RecoveryAttr copy(); // deep clone
	public RecoveryAttr toData(); // a Data instance
	public RecoveryAttr toBean(); // a Bean instance
	public RecoveryAttr toDataIf(); // a Data instance If need. else return this
	public RecoveryAttr toBeanIf(); // a Bean instance If need. else return this

	public int getQuality(); // 

	public void setQuality(int _v_); // 
}
