package fire.pb.state;

import java.util.ArrayList;
import java.util.List;


public class PEndTrusteeShip extends mkdb.Procedure
{

	final private long roleId;
	
	public PEndTrusteeShip(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		List<Long> ids = new ArrayList<Long>();
		ids.add(roleId);
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, ids));//先锁rolelock
		IState state = StateManager.getStateByRoleId(roleId);
		state.trigger(State.TRIGGER_TIME_OUT);
		return true;
	}
}
