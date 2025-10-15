
package xbean;

public interface ApprenticelListRecord extends mkdb.Bean {
	public ApprenticelListRecord copy(); // deep clone
	public ApprenticelListRecord toData(); // a Data instance
	public ApprenticelListRecord toBean(); // a Bean instance
	public ApprenticelListRecord toDataIf(); // a Data instance If need. else return this
	public ApprenticelListRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 达到这个数量的时间
	public xbean.MarshalApprenticeRecord getMarshaldata(); // 

	public void setTime(long _v_); // 达到这个数量的时间
}
