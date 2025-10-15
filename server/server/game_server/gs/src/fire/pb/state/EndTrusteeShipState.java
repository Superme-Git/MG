package fire.pb.state;

import fire.pb.hook.RoleHookManager;


public class EndTrusteeShipState extends State{

	public EndTrusteeShipState(long roleId)
	{
		super(roleId);
	}

	@Override
	public boolean enter(int trigger)
	{
		Integer oldstate = xtable.Roleonoffstate.get(roleId);
		if (oldstate == null)
			oldstate = State.UNENTRY_STATE;
		boolean valid = false;
		if(trigger == State.TRIGGER_TIME_OUT && oldstate == State.TRUSTEESHIP_STATE)
			valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, getState());
		if(RoleHookManager.getInstance().getLockScreenStateByRoleId(roleId) == true)
			RoleHookManager.getInstance().setLockScreenStateByRoleId(roleId, false);
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：").append(this.getClass()));
		
		return execute();
	}
	
	@Override
	public boolean execute() {
		// TODO 结束下线保护工作的处理放在这里，比如清除临时背包信息，删除临时任务等操作。
		return trigger(State.TRIGGER_PROCESS_DONE);
	}
	
	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_PROCESS_DONE)
			return new PreOfflineProtectState(roleId).enter(trigger);
		triggerErrorLog(trigger);
		return false;
	}

	@Override
	public int getState(){
		return State.END_TRUSTEESHIP_STATE;
	}
	
}
