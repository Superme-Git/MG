
package xbean;

public interface YaoQianShuInfo extends mkdb.Bean {
	public YaoQianShuInfo copy(); // deep clone
	public YaoQianShuInfo toData(); // a Data instance
	public YaoQianShuInfo toBean(); // a Bean instance
	public YaoQianShuInfo toDataIf(); // a Data instance If need. else return this
	public YaoQianShuInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 植树人
	public java.util.List<Long> getLookroleids(); // 照顾过的玩家id
	public java.util.List<Long> getLookroleidsAsData(); // 照顾过的玩家id
	public int getMapid(); // 
	public int getPosx(); // 
	public int getPosy(); // 

	public void setRoleid(long _v_); // 植树人
	public void setMapid(int _v_); // 
	public void setPosx(int _v_); // 
	public void setPosy(int _v_); // 
}
