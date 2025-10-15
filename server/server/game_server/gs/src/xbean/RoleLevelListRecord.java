
package xbean;

public interface RoleLevelListRecord extends mkdb.Bean {
	public RoleLevelListRecord copy(); // deep clone
	public RoleLevelListRecord toData(); // a Data instance
	public RoleLevelListRecord toBean(); // a Bean instance
	public RoleLevelListRecord toDataIf(); // a Data instance If need. else return this
	public RoleLevelListRecord toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 达到这个数量的时间
	public xbean.MarshalRoleLevelRecord getMarshaldata(); // 

	public void setTime(long _v_); // 达到这个数量的时间
}
