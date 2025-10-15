
package xbean;

public interface ClanIndex extends mkdb.Bean {
	public ClanIndex copy(); // deep clone
	public ClanIndex toData(); // a Data instance
	public ClanIndex toBean(); // a Bean instance
	public ClanIndex toDataIf(); // a Data instance If need. else return this
	public ClanIndex toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getIndex(); // 
	public java.util.Map<Integer, Long> getIndexAsData(); // 

}
