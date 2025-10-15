package fire.pb.instancezone;

import fire.pb.clan.ClanUtils;
import fire.pb.instancezone.faction.FactionInstZone;
import mkdb.Procedure;

public class PEnterInstZone extends Procedure {

	private final long teamleaderid;
	private final long roleid;
	public PEnterInstZone(final long teamleaderid, final long roleid) {
		this.teamleaderid = teamleaderid;
		this.roleid = roleid;
	}
	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(teamleaderid, false);
		if (instzone != null && instzone instanceof FactionInstZone) {
			//进公会副本的判断
			xbean.ClanInfo leaderclaninfo = ClanUtils.getClanInfoById(teamleaderid, true);
			if (leaderclaninfo == null) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160168 ,null);
				return false;
			}
			
			xbean.ClanInfo clan = ClanUtils.getClanInfoById(roleid, true); 
			if (clan == null || clan.getKey() != leaderclaninfo.getKey()) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160168, null);
				return false;
			}
			
			if (!instzone.enter(roleid, true)) {
				return false;
			}
		}
		return true;
	}

	
}
