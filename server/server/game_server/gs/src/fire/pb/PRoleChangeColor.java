package fire.pb;

import fire.msp.role.GRoleChangeShape;
import fire.pb.util.RolePropConf;
import mkdb.Procedure;

public class PRoleChangeColor extends Procedure {
	protected final long roleid;
	protected final int newshapeid;
	PRoleChangeColor(long roleid, int newshapeid) {
		this.roleid = roleid;
		this.newshapeid = newshapeid;
	}

	@Override
	protected boolean process() {
		xbean.Properties prop = xtable.Properties.get(roleid);
		if (prop == null)
			return false;
		final int oldval = prop.getShape();
		if (RolePropConf.getShapeidByXshapeid(newshapeid) != 
				RolePropConf.getShapeidByXshapeid(oldval)) {
			return false;
		}
		GRoleChangeShape send = new GRoleChangeShape();
		send.roleid = roleid;
		send.reason = GRoleChangeShape.HUANSE;
		send.shape = newshapeid;
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
		if (team != null){
			send.teammembers.addAll(team.getAllMemberIds());
		}
		fire.pb.GsClient.pSendWhileCommit(send);
		prop.setShape(newshapeid);
		return true;
	}
}
