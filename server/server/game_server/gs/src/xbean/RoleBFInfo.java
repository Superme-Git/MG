
package xbean;

public interface RoleBFInfo extends mkdb.Bean {
	public RoleBFInfo copy(); // deep clone
	public RoleBFInfo toData(); // a Data instance
	public RoleBFInfo toBean(); // a Bean instance
	public RoleBFInfo toDataIf(); // a Data instance If need. else return this
	public RoleBFInfo toBeanIf(); // a Bean instance If need. else return this

	public int getInstzoneid(); // 
	public int getLosetimes(); // 
	public int getReceivelastaward(); // 
	public int getLastrank(); // 
	public int getStage(); // 
	public int getLastenterstage(); // 
	public java.util.Map<Integer, Long> getStagetime(); // 
	public java.util.Map<Integer, Long> getStagetimeAsData(); // 
	public java.util.Map<Integer, Integer> getStageround(); // 每关的所用的回合数
	public java.util.Map<Integer, Integer> getStageroundAsData(); // 每关的所用的回合数
	public int getYesterstage(); // 
	public long getLastchangetime(); // 
	public java.util.Map<Integer, Integer> getStagefanpai(); // 记录每个关卡翻牌的次数
	public java.util.Map<Integer, Integer> getStagefanpaiAsData(); // 记录每个关卡翻牌的次数
	public int getAutogo(); // 是否自动寻路

	public void setInstzoneid(int _v_); // 
	public void setLosetimes(int _v_); // 
	public void setReceivelastaward(int _v_); // 
	public void setLastrank(int _v_); // 
	public void setStage(int _v_); // 
	public void setLastenterstage(int _v_); // 
	public void setYesterstage(int _v_); // 
	public void setLastchangetime(long _v_); // 
	public void setAutogo(int _v_); // 是否自动寻路
}
