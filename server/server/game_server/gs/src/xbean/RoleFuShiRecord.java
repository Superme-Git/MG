
package xbean;

public interface RoleFuShiRecord extends mkdb.Bean {
	public RoleFuShiRecord copy(); // deep clone
	public RoleFuShiRecord toData(); // a Data instance
	public RoleFuShiRecord toBean(); // a Bean instance
	public RoleFuShiRecord toDataIf(); // a Data instance If need. else return this
	public RoleFuShiRecord toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.FuShiRecord> getRecords(); // 
	public java.util.List<xbean.FuShiRecord> getRecordsAsData(); // 

}
