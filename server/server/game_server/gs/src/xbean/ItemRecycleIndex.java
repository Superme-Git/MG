
package xbean;

public interface ItemRecycleIndex extends mkdb.Bean {
	public ItemRecycleIndex copy(); // deep clone
	public ItemRecycleIndex toData(); // a Data instance
	public ItemRecycleIndex toBean(); // a Bean instance
	public ItemRecycleIndex toDataIf(); // a Data instance If need. else return this
	public ItemRecycleIndex toBeanIf(); // a Bean instance If need. else return this

	public java.util.Set<Long> getDayrecycle(); // 
	public java.util.Set<Long> getDayrecycleAsData(); // 

}
