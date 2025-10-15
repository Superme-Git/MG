package fire.pb.state;

import fire.pb.event.LeaveTeamSpecialQuestEvent;
import fire.pb.hook.RoleHookManager;


/**结束下线保护状态*/
public class EndOfflineProtectState extends State{

	public EndOfflineProtectState(long roleId)
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
		if(trigger == State.TRIGGER_TIME_OUT && oldstate == State.OFFLINE_PROTECT_STATE)
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
//		final fire.pb.item.OfflineProcessMoney proc = new fire.pb.item.OfflineProcessMoney(roleId);
//		proc.call();
		new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				fire.pb.event.Poster.getPoster().dispatchEvent(new LeaveTeamSpecialQuestEvent(roleId));
//				//清除副本状态
//				xtable.Role2instancetask.remove(roleId);
				return true;
			}
		}.call();

		// 清除临时背包栏
		new CleanTempbagProc( roleId ).call();
		
		new PEndOfflineProtect(roleId).call();//踢出队伍
		/*Team team = TeamManager.getTeamByRoleId(roleId);
		if(team!=null)
			team.removeTeamMemberWithSP(roleId);*/
		
		return trigger(State.TRIGGER_PROCESS_DONE);
	}
	
	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_PROCESS_DONE)
			return new UnEntryState(roleId).enter(trigger);
		triggerErrorLog(trigger);
		return false;
	}

	@Override
	public int getState(){
		return State.END_OFFLINE_PROTECT_STATE;
	}
	
	private class CleanTempbagProc extends mkdb.Procedure {

		public CleanTempbagProc( final long roleid ) {
			
		}
		@Override
		protected boolean process() {
			return true;
		}	
	}
}
