
package xbean;

public interface RequestRuneInfoList extends mkdb.Bean {
	public RequestRuneInfoList copy(); // deep clone
	public RequestRuneInfoList toData(); // a Data instance
	public RequestRuneInfoList toBean(); // a Bean instance
	public RequestRuneInfoList toDataIf(); // a Data instance If need. else return this
	public RequestRuneInfoList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RequestRuneInfo> getRequestrunelists(); // 
	public java.util.List<xbean.RequestRuneInfo> getRequestrunelistsAsData(); // 

}
