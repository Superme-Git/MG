package fire.pb.instancezone.timer;

import fire.pb.PAddExpProc;
import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import mkdb.Procedure;

public class PInstMapTimer extends Procedure {
	private final long ownerid;
	private final int saveid;
	private final int fubenid;

	public PInstMapTimer(final long ownerid, final int saveid, final int fubenid) {
		this.ownerid = ownerid;
		this.saveid = saveid;
		this.fubenid = fubenid;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(ownerid, true);
		if (instzone == null) {
			return false;
		}
		
		for (Long rid : instzone.getRoleIds()) {
			Integer curlevel = xtable.Properties.selectLevel(rid);
			if (curlevel == null)
				return false;
			//400*min(RoleLv,FuBenId*10-1051)*0.0392*(0.4+0.1*Saveid)
			Double expAward = 400 * Math.min(curlevel, fubenid * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * saveid);
			pexecuteWhileCommit(new PAddExpProc(rid, expAward.longValue(), PAddExpProc.INST_MAP,
					"公会地图加经验"));
		}

		return true;
	}

}
