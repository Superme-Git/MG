package fire.pb.state;

import fire.pb.hook.RoleHookManager;
import fire.pb.util.CGPlayUtil.PRemoveCGBuff;
import fire.pb.util.ReplayUtil.PRemoveReplayBuff;
import mkdb.Procedure;




public class BreakTrusteeShipState extends PreEntryState{

	public BreakTrusteeShipState(long roleId)
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
		if(trigger == State.TRIGGER_ONLINE && oldstate == State.TRUSTEESHIP_STATE)
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
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：").append(this.getClass()));
		
		return execute();
	}
	
	
	@Override
	public boolean beforeEnterWorld()
	{
		//TODO 中断战斗托管状态时做的处理
		//中断战斗托管时，buff模块不用重新初始化
		
		xbean.ERole xerole = xtable.Erole.get(roleId);
		if(xerole == null)
		{
			StateManager.logger.error(new StringBuilder("roleId=").append(roleId).append(" BreakTrusteeShipState Error: 角色xerole为空"));
			return false;
		}
		return true;
	}
	
	@Override
	public void sendEnterWorld()
	{
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
		return State.BREAK_TRUSTEESHIP_STATE;
	}

}
