package fire.pb.state;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



public class TrusteeShipState extends State{

	protected int nTrusteeShipLen = 15;
	protected boolean bTimeout = false;
	
	public TrusteeShipState(long roleId)
	{		
		super(roleId);
	}
	
	public TrusteeShipState(long roleId, boolean blockscreen)
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
		if(trigger == State.TRIGGER_PROCESS_DONE && oldstate == State.PRE_TRUSTEESHIP_STATE)
			valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId,getState());
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：" ).append(this.getClass()));
		
		return execute();
	}

	@Override
	public boolean execute() {
		// TODO 进入战斗托管状态后的操作TrusteeShipTask
//		ScheduledFuture<?> fur = xdb.Xdb.executor().schedule(new fire.pb.timer.OfflineProtectTask(roleId) , 14, TimeUnit.MINUTES);
		ScheduledFuture<?> fur = mkdb.Mkdb.executor().schedule(new fire.pb.timer.TrusteeShipTask(roleId), nTrusteeShipLen, TimeUnit.MINUTES);
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

//	@Override
//	public boolean exit() {
//		return false;
//		
//		
//	}


	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_TIME_OUT)
		{
			Long battleId = xtable.Roleid2battleid.get(roleId);
			if(battleId==null)
			{
				removeOfflinefuture();
				return new EndTrusteeShipState(roleId).enter(trigger);
			}
			return true;
		}		
		if(trigger == State.TRIGGER_ONLINE)
		{
			removeOfflinefuture();
			return new BreakTrusteeShipState(roleId).enter(trigger);
		}
		if(trigger == State.TRIGGER_BATTLE_END)
		{
			xbean.OfflineFuture efur = xtable.Offlinefuture.get(roleId);
			if (efur != null&&!efur.getTimefuture().isDone())
			{
	 			return true;
			}
			removeOfflinefuture();
			return new EndTrusteeShipState(roleId).enter(State.TRIGGER_TIME_OUT);
		}
		if(trigger!=State.TRIGGER_OFFLINE_BATTLE&&trigger!=State.TRIGGER_OFFLINE)
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
		return State.TRUSTEESHIP_STATE;
	}
	
}
