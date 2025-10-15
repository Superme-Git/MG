
package xbean;

public interface RoleTradingHisRecordList extends mkdb.Bean {
	public RoleTradingHisRecordList copy(); // deep clone
	public RoleTradingHisRecordList toData(); // a Data instance
	public RoleTradingHisRecordList toBean(); // a Bean instance
	public RoleTradingHisRecordList toDataIf(); // a Data instance If need. else return this
	public RoleTradingHisRecordList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlist(); // 
	public java.util.List<xbean.RoleTradingHisRecord> getRoletradinghisrecordlistAsData(); // 

}
