
package xbean;

public interface RoleReceiveRedPackRecordList extends mkdb.Bean {
	public RoleReceiveRedPackRecordList copy(); // deep clone
	public RoleReceiveRedPackRecordList toData(); // a Data instance
	public RoleReceiveRedPackRecordList toBean(); // a Bean instance
	public RoleReceiveRedPackRecordList toDataIf(); // a Data instance If need. else return this
	public RoleReceiveRedPackRecordList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklist(); // 
	public java.util.Map<String, xbean.RoleReceiveRedPackRecord> getRolereceiveredpacklistAsData(); // 

}
