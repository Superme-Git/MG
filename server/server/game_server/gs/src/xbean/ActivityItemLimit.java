
package xbean;

public interface ActivityItemLimit extends mkdb.Bean {
	public ActivityItemLimit copy(); // deep clone
	public ActivityItemLimit toData(); // a Data instance
	public ActivityItemLimit toBean(); // a Bean instance
	public ActivityItemLimit toDataIf(); // a Data instance If need. else return this
	public ActivityItemLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmap(); // key为itemid
	public java.util.Map<Integer, xbean.LimitItemInfo> getLimititemmapAsData(); // key为itemid

}
