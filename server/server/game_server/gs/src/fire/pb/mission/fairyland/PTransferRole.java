

package fire.pb.mission.fairyland;

import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import mkdb.Procedure;

public class PTransferRole extends Procedure {

	private final long roleid;
	private int mapid;
	private int posx;
	private int posy;	
	
	public PTransferRole(long roleid, int mapid, int posx, int posy) {
        this.roleid = roleid;
        this.mapid = mapid;
        this.posx = posx;
        this.posy = posy;
	}
	@Override
	protected boolean process() throws Exception {
		Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.FORCE_GOTO);
		return true;
	}

}

