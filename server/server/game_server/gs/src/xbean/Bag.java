
package xbean;

public interface Bag extends mkdb.Bean {
	public Bag copy(); // deep clone
	public Bag toData(); // a Data instance
	public Bag toBean(); // a Bean instance
	public Bag toDataIf(); // a Data instance If need. else return this
	public Bag toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getCurrency(); // 货币, key 为枚举值, value为货币值
	public java.util.Map<Integer, Long> getCurrencyAsData(); // 货币, key 为枚举值, value为货币值
	public int getCapacity(); // 
	public int getNextid(); // 
	public java.util.Map<Integer, xbean.Item> getItems(); // 
	public java.util.Map<Integer, xbean.Item> getItemsAsData(); // 
	public int getLocked(); // 0 没有锁。1 有锁

	public void setCapacity(int _v_); // 
	public void setNextid(int _v_); // 
	public void setLocked(int _v_); // 0 没有锁。1 有锁
}
