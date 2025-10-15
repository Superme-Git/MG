
package xbean;

public interface RoleYaoQianShuInfo extends mkdb.Bean {
	public RoleYaoQianShuInfo copy(); // deep clone
	public RoleYaoQianShuInfo toData(); // a Data instance
	public RoleYaoQianShuInfo toBean(); // a Bean instance
	public RoleYaoQianShuInfo toDataIf(); // a Data instance If need. else return this
	public RoleYaoQianShuInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getLookroleids(); // 照顾过的玩家id
	public java.util.List<Long> getLookroleidsAsData(); // 照顾过的玩家id
	public int getMapid(); // 
	public int getPosx(); // 
	public int getPosy(); // 

	public void setMapid(int _v_); // 
	public void setPosx(int _v_); // 
	public void setPosy(int _v_); // 
}
