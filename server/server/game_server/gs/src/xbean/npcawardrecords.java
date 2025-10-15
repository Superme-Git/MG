
package xbean;

public interface npcawardrecords extends mkdb.Bean {
	public npcawardrecords copy(); // deep clone
	public npcawardrecords toData(); // a Data instance
	public npcawardrecords toBean(); // a Bean instance
	public npcawardrecords toDataIf(); // a Data instance If need. else return this
	public npcawardrecords toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.npcaward> getRecords(); // 
	public java.util.Map<Integer, xbean.npcaward> getRecordsAsData(); // 

}
