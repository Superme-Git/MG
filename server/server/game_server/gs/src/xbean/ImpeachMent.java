
package xbean;

public interface ImpeachMent extends mkdb.Bean {
	public ImpeachMent copy(); // deep clone
	public ImpeachMent toData(); // a Data instance
	public ImpeachMent toBean(); // a Bean instance
	public ImpeachMent toDataIf(); // a Data instance If need. else return this
	public ImpeachMent toBeanIf(); // a Bean instance If need. else return this

	public long getImpeachroleid(); // 发起弹劾的人
	public long getImpeachtime(); // 发起弹劾的时间
	public short getImpeachallnum(); // 需要相应弹劾的总人数
	public java.util.List<Long> getAcceptimpeachroleids(); // 相应弹劾的角色ID
	public java.util.List<Long> getAcceptimpeachroleidsAsData(); // 相应弹劾的角色ID

	public void setImpeachroleid(long _v_); // 发起弹劾的人
	public void setImpeachtime(long _v_); // 发起弹劾的时间
	public void setImpeachallnum(short _v_); // 需要相应弹劾的总人数
}
