package fire.pb.state;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class OfflineProtectState extends State{

	public OfflineProtectState(long roleId)
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
		if(trigger == State.TRIGGER_PROCESS_DONE && oldstate == State.PRE_OFFLINE_PROTECT_STATE)
			valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, getState());
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：").append(this.getClass()));
		
		return execute();
	}

	@Override
	protected boolean execute() {
		
		//TODO 下线保护中要做的事情
		//最后启动定时器，10分钟保护时间
		ScheduledFuture<?> fur = mkdb.Mkdb.executor().schedule(new fire.pb.timer.OfflineProtectTask(roleId) , 1, TimeUnit.SECONDS);
		xbean.OfflineFuture efur = xtable.Offlinefuture.get(roleId);
		if (efur != null && efur.getTimefuture() != null)
		{
			efur.getTimefuture().cancel(true);
		}
		if (efur == null)
		{
			efur = xbean.Pod.newOfflineFuture();
			xtable.Offlinefuture.add(roleId, efur);
		}
		efur.setTimefuture(fur);
		
		return true;
	}
	
//	private class CleanTempbagProc extends xdb.Procedure {
//		private final long roleid;
//		public CleanTempbagProc( final long roleid ) {
//			this.roleid = roleid;
//		}
//		@Override
//		protected boolean process() {
//			fire.pb.item.Module.getInstance().
//				getItemColumn( roleid, BagTypes.TEMP, false ).clear();
//			return true;
//		}	
//	}

//	@Override
//	public boolean exit() {
//		// TODO 退出此状态的一些处理
//		new PAddOfflineFuture(roleId, null, false).submit();
//		return false;
//		
//	}

	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_ONLINE)
		{
			removeOfflinefuture();
			return new BreakOfflineProtectState(roleId).enter(trigger);
		}
		if(trigger==State.TRIGGER_TIME_OUT)
		{
			removeOfflinefuture();
			return new EndOfflineProtectState(roleId).enter(trigger);
		}
		if(trigger!=State.TRIGGER_OFFLINE)
			triggerErrorLog(trigger);
		return false;
	}

	private void removeOfflinefuture()
	{
		xbean.OfflineFuture efur = xtable.Offlinefuture.get(roleId);
		if (efur != null)
		{
			if(efur.getTimefuture() != null)
				efur.getTimefuture().cancel(true);
			xtable.Offlinefuture.remove(roleId);
		}
	}

	@Override
	public int getState(){
		return State.OFFLINE_PROTECT_STATE;
	}
	
}
