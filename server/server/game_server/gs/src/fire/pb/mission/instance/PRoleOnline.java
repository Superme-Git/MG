package fire.pb.mission.instance;

import mkdb.Procedure;

public class PRoleOnline extends Procedure {
	final private long roleId;
	final private long sceneId;
	
	public PRoleOnline(final long roleid, final long sceneid) {
		this.roleId = roleid;
		this.sceneId = sceneid;
	}

	@Override
	protected boolean process() throws Exception {
		//出组队副本地图
		if (fire.pb.mission.instance.InstanceManager.getInstance().isInstanceCopy((int)sceneId)) {
			return true;
		}
		
		if (fire.pb.mission.instance.line.LineInstManager.getInstance().isInstanceCopy((int)sceneId)) {
			return true;
		}
//		xtable.Role2instancetask.remove(roleId);
		return true;
	}
}
