
package xbean;

public interface LiveDieRoleInfo extends mkdb.Bean {
	public LiveDieRoleInfo copy(); // deep clone
	public LiveDieRoleInfo toData(); // a Data instance
	public LiveDieRoleInfo toBean(); // a Bean instance
	public LiveDieRoleInfo toDataIf(); // a Data instance If need. else return this
	public LiveDieRoleInfo toBeanIf(); // a Bean instance If need. else return this

	public long getObjectid(); // 下战书目标玩家id
	public long getInvitationtime(); // 下战书时间
	public int getSelecttype(); // 战书模式0单人   1组队
	public int getAcceptflag(); // 目标是否接受挑战 0初始状态   1接受战书

	public void setObjectid(long _v_); // 下战书目标玩家id
	public void setInvitationtime(long _v_); // 下战书时间
	public void setSelecttype(int _v_); // 战书模式0单人   1组队
	public void setAcceptflag(int _v_); // 目标是否接受挑战 0初始状态   1接受战书
}
