package fire.pb.instancezone;

import fire.pb.instancezone.faction.FactionInstZone;
import mkdb.Procedure;

public class PEnterInstZoneByNpc extends Procedure {

	private final long landkey;
	private final long roleid;
	private final int instid;
	
	public PEnterInstZoneByNpc(final int instid, final long landkey, final long roleid) {
		this.instid = instid;
		this.landkey = landkey;
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone oldzone = InstanceZoneFactory.getInstanceZone(landkey, false);
		if (null == oldzone)
			return false;
		
		if (!(oldzone instanceof FactionInstZone)) {
			return false;
		}
		
		if (((FactionInstZone)oldzone).getClanInstData().getInstreset().containsKey(instid)) {
			if (!fire.pb.instancezone.Module.getInstance().getInstInit(landkey)) {
				oldzone.init(((FactionInstZone)oldzone).getCurrentStep(), roleid, false);
				fire.pb.instancezone.Module.getInstance().setInstInit(landkey);
			}
		}
		
		return oldzone.enter(roleid, true);
	}
	
	
}
