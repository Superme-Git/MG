package fire.pb.instancezone.faction;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.Module;
import mkdb.Procedure;

public class PCheckMapNpcs extends Procedure {

	private final long ownerid;
	private final int npccount;
	private final int saveid;
	public PCheckMapNpcs(final long ownerid, final int npccount, final int saveid) {
		this.ownerid = ownerid;
		this.npccount = npccount;
		this.saveid = saveid;
	}
	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(ownerid, false);
		if (instzone == null) {
			return false;
		}
		
		if (!(instzone instanceof FactionInstZone))
			return false;
		
		if (npccount == 0) {
			if (((FactionInstZone)instzone).setSaveCompleted(saveid)) {
				((FactionInstZone)instzone).onSaveCompleted(saveid);
			}
			
			Module.logger.info(new StringBuilder().append("landkey:").append(ownerid)
					.append(",saveid:").append(saveid).append(",副本进度完成"));
		}
		
		return true;
	}
	
	
}
