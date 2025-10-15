

package fire.pb.mission.fairyland;

import fire.pb.mission.Module;
import fire.pb.mission.SFairylandStatus;
import mkdb.Procedure;
import xbean.RolePos;


public class PRoleFairylandOffline extends Procedure {

	private long roleid;

	public PRoleFairylandOffline(long roleid) {
		super();
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {
		boolean hasTask = Module.getInstance().hasUnfinishedFairylandMission(roleid) > 0 ? true : false;
		xbean.RolePos rolePos = xtable.Roleposes.get(roleid);
		// 设状态
		SFairylandStatus sds = new SFairylandStatus();
		// 理论上,单个人暂离梦境的时候,hasdreamtask一定是true
		if (hasTask) {
			sds.status = RolePos.ABSENTDREAM;
			rolePos.setStatus(RolePos.ABSENTDREAM);
		} else {
			sds.status = RolePos.OUTDREAM;
			rolePos.setStatus(RolePos.OUTDREAM);
		}
	//	DreamLandManager.getInstance().absentDreamLand(roleid, hasTask);
		return true;
	}

}
