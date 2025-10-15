package fire.pb.map;

import java.util.LinkedList;
import java.util.List;

import fire.pb.triggers.TriggerRole;
import fire.pb.triggers.event.TriggerEvent;

/**
 * 
 * 进入区域触发
 * 
 * @author nobody
 *
 */
public class PEnterRegion extends mkdb.Procedure
{
	private final long roleId;
	private final int regionId;
	public PEnterRegion(long roleId, int regionId)
	{
		this.roleId = roleId;
		this.regionId = regionId;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		TriggerRole trole = new TriggerRole(roleId, true);
		List<Integer> args =  new LinkedList<Integer>();
		args.add(regionId);
		int triggered = trole.getTriggerByType(TriggerEvent.TRIGGER_TYPE_ENTER_REGION, args);
		if(triggered <= 0)
			return false;
		
		trole = new TriggerRole(roleId, false);
		return trole.trigger(triggered);
	}
}
