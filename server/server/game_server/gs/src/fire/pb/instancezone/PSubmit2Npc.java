package fire.pb.instancezone;

import fire.pb.instancezone.event.SubmitItemEvent;
import fire.pb.npc.CSubmit2Npc;

public class PSubmit2Npc extends mkdb.Procedure
{
	private final CSubmit2Npc protocol;
	
	private final long roleId;
	

	public PSubmit2Npc(long roleId, CSubmit2Npc protocol)
	{
		this.roleId = roleId;
		this.protocol = protocol;
	}

	
	@Override
	protected boolean process() throws Exception
	{
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleId, false);
		if (instzone == null)
			return false;
//		instzone.lockRoles();
		SubmitItemEvent event = new SubmitItemEvent(roleId,protocol);
		return instzone.processEvent(event);
	}
}
