package fire.pb.state;

import fire.pb.PropRole;
import fire.pb.hook.RoleHookManager;
import fire.pb.talk.MessageMgr;



public class EntryState extends State {

	public EntryState(long roleId)
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
		if(trigger == State.TRIGGER_PROCESS_DONE)
			if(oldstate == State.PRE_ENTRY_STATE || oldstate == State.BREAK_OFFLINE_PROTECT_STATE ||oldstate == State.BREAK_TRUSTEESHIP_STATE)
				valid = true;
		if(!valid)
		{
			enterErrorLog(oldstate, trigger);
			return false;
		}
		if(oldstate == State.BREAK_TRUSTEESHIP_STATE)
		{
			PropRole prole = new PropRole(roleId, false); 
			long spase =System.currentTimeMillis() - prole.getLastOfflineTime();
			if(spase > 60000)		
			{
				MessageMgr.psendMsgNotify(roleId, 162031, null);
			}
		}
		xtable.Roleonoffstate.remove(roleId);
		xtable.Roleonoffstate.add(roleId, State.ENTRY_STATE);
		if(RoleHookManager.getInstance().getLockScreenStateByRoleId(roleId) == true)
			RoleHookManager.getInstance().setLockScreenStateByRoleId(roleId, false);
		StateManager.logger.info(new StringBuilder("roleId=").append(roleId).append(" 角色进入状态：").append(this.getClass()));
		
		return execute();
	}
	
	
	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public boolean trigger(int trigger)
	{
		if(trigger==State.TRIGGER_OFFLINE_BATTLE||trigger==State.TRIGGER_OFFLINE)
			return new PreTrusteeShipState(roleId).enter(trigger);
		if(trigger!=State.TRIGGER_BATTLE_END)
			triggerErrorLog(trigger);
		return false;
	}

	@Override
	public int getState(){
		return State.ENTRY_STATE;
	}
}
