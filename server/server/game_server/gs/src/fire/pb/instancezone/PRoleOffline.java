package fire.pb.instancezone;

public class PRoleOffline extends mkdb.Procedure {
	final private long roleid;

	public PRoleOffline(long roleId) {
		this.roleid = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleid, false);
		if (instzone == null) {
			xtable.Role2instancezone.remove(roleid);
			return true;
		}

		return instzone.leaveWhileOffline(roleid);
	}
}
