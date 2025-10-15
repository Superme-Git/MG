package fire.pb.state;

import fire.pb.hook.RoleHookManager;
import fire.pb.util.CGPlayUtil.PRemoveCGBuff;
import fire.pb.util.ReplayUtil.PRemoveReplayBuff;
import mkdb.Procedure;



public class BreakOfflineProtectState extends PreEntryState{

	public BreakOfflineProtectState(long roleId)
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
		if(trigger == State.TRIGGER_ONLINE && oldstate == State.OFFLINE_PROTECT_STATE)
			valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		Procedure.pexecuteWhileCommit(new PRemoveCGBuff(roleId));
		Procedure.pexecuteWhileCommit(new PRemoveReplayBuff(roleId));
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, getState());
		if(RoleHookManager.getInstance().getLockScreenStateByRoleId(roleId) == true)
			RoleHookManager.getInstance().setLockScreenStateByRoleId(roleId, false);
		StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" 进入状态：").append(this.getClass()));
		return execute();
	}

	
	@Override
	public boolean beforeEnterWorld()
	{
		//TODO 中断下线保护状态时做的处理
		return super.beforeEnterWorld();
	}
	
	@Override
	public void sendEnterWorld()
	{
		//TODO 中断下线保护状态时需要发的协议
		super.sendEnterWorld();
	}


	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_PROCESS_DONE)
			return new EntryState(roleId).enter(trigger);
		triggerErrorLog(trigger);
		return false;
	}



	@Override
	public int getState(){
		return State.BREAK_OFFLINE_PROTECT_STATE;
	}



}
