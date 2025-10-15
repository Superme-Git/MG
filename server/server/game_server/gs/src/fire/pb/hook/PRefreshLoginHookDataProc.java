package fire.pb.hook;


public class PRefreshLoginHookDataProc extends mkdb.Procedure{
	
	private final long roleId;
	private final long lastOnlineTime;

	public PRefreshLoginHookDataProc(long roleId, long lastOnlineTime){
		this.roleId = roleId;
		this.lastOnlineTime = lastOnlineTime;
	}

	@Override
	protected boolean process() throws Exception {
		
		RoleHookManager.getInstance().loginSetRoleDefaultSkill(roleId);
		RoleHookManager.getInstance().loginSetPetDefaultSkill(roleId);
		RoleHookManager.getInstance().refreshHookBattledata(roleId,true);
		
		RoleHookManager.getInstance().refreshOfflineExpByTime(roleId,lastOnlineTime);
		RoleHookManager.getInstance().loginAddDpoint(roleId,lastOnlineTime);
		RoleHookManager.getInstance().refreshHookExpdata(roleId,true);

		return true;
	}
}
