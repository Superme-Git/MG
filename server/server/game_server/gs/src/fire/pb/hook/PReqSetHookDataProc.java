package fire.pb.hook;

import fire.pb.hook.HookDataUpdateEnum;


public class PReqSetHookDataProc extends mkdb.Procedure{
	
	private final long roleId;
	
	private short flag;
	private long data;

	public PReqSetHookDataProc(long roleId, short flag, long data ){
		this.roleId = roleId;
		this.flag = flag;
		this.data = data;
	}

	@Override
	protected boolean process() throws Exception {	
		if(flag == HookDataUpdateEnum.HOOKDATA_CANGETDPOINT){
			RoleHookManager.getInstance().setCanGetDpoint(roleId, (short)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_GETDPOINT){
			RoleHookManager.getInstance().setGetDpoint(roleId, (short)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_ISAUTOBATTLE){
			RoleHookManager.getInstance().setAutoBattle(roleId, (byte)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_CHAROPTYPE){
			RoleHookManager.getInstance().setCharOpType(roleId, (short)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_CHAROPID){
			RoleHookManager.getInstance().setCharOpId(roleId, (int)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_PETOPTYPE){
			RoleHookManager.getInstance().setPetOpType(roleId, (short)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_PETOPID){
			RoleHookManager.getInstance().setPetOpId(roleId, (int)data);
		}
		else if(flag == HookDataUpdateEnum.HOOKDATA_OFFLINEEXP){
			RoleHookManager.getInstance().setOfflineExp(roleId, (long)data);
		}

		return true;
	}
}
