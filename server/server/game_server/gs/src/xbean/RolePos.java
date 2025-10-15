
package xbean;

public interface RolePos extends mkdb.Bean {
	public RolePos copy(); // deep clone
	public RolePos toData(); // a Data instance
	public RolePos toBean(); // a Bean instance
	public RolePos toDataIf(); // a Data instance If need. else return this
	public RolePos toBeanIf(); // a Bean instance If need. else return this

	public final static int OUTDREAM = 1; // 不在幻境中
	public final static int INDREAM = 2; // 在幻境中
	public final static int ABSENTDREAM = 3; // 暂离幻境

	public int getMapid(); // 
	public int getPosx(); // 
	public int getPosy(); // 
	public long getOwnerid(); // 这个幻境是属于哪个玩家的
	public int getDynamicmap(); // 
	public int getDynamicposx(); // 
	public int getDynamicposy(); // 
	public int getStatus(); // 
	public int getHastask(); // 玩家是否有幻境任务 0没有,1有

	public void setMapid(int _v_); // 
	public void setPosx(int _v_); // 
	public void setPosy(int _v_); // 
	public void setOwnerid(long _v_); // 这个幻境是属于哪个玩家的
	public void setDynamicmap(int _v_); // 
	public void setDynamicposx(int _v_); // 
	public void setDynamicposy(int _v_); // 
	public void setStatus(int _v_); // 
	public void setHastask(int _v_); // 玩家是否有幻境任务 0没有,1有
}
