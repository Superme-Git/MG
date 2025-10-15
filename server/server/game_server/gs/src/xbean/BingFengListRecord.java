
package xbean;

public interface BingFengListRecord extends mkdb.Bean {
	public BingFengListRecord copy(); // deep clone
	public BingFengListRecord toData(); // a Data instance
	public BingFengListRecord toBean(); // a Bean instance
	public BingFengListRecord toDataIf(); // a Data instance If need. else return this
	public BingFengListRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 达到这个数量的时间
	public xbean.MarshalBingFengRecord getMarshaldata(); // 

	public void setTime(long _v_); // 达到这个数量的时间
}
