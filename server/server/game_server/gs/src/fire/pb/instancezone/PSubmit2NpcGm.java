package fire.pb.instancezone;

import java.util.ArrayList;
import java.util.List;

import fire.pb.instancezone.event.SubmitItemEvent;
import mkdb.Procedure;

public class PSubmit2NpcGm extends Procedure {

	private final long roleid;
	private final int npcid; 	private final int submittype; 	private java.util.ArrayList<fire.pb.npc.SubmitUnit> things; 	
	public PSubmit2NpcGm(final long roleid, final int npcid, final int submittype, final List<fire.pb.npc.SubmitUnit> things) {
		this.roleid = roleid;
		this.npcid = npcid;
		this.submittype = submittype;
		this.things = new ArrayList<fire.pb.npc.SubmitUnit>();
		this.things.addAll(things);
	}
	
	@Override
	protected boolean process() throws Exception
	{
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleid, false);
		if (instzone == null)
			return false;
		
		SubmitItemEvent event = new SubmitItemEvent(roleid, npcid, submittype, things);
		return instzone.processEvent(event);
	}
}
