
package xbean;

public interface ClanInstBestLevel extends mkdb.Bean {
	public ClanInstBestLevel copy(); // deep clone
	public ClanInstBestLevel toData(); // a Data instance
	public ClanInstBestLevel toBean(); // a Bean instance
	public ClanInstBestLevel toDataIf(); // a Data instance If need. else return this
	public ClanInstBestLevel toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getInstsave(); // 最先通过公会副本某一层的公会,key 层数, value 公会key
	public java.util.Map<Integer, Long> getInstsaveAsData(); // 最先通过公会副本某一层的公会,key 层数, value 公会key

}
