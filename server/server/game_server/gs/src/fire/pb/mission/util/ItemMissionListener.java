package fire.pb.mission.util;

import fire.pb.item.Pack;
import fire.pb.team.TeamManager;


public class ItemMissionListener {
	public ItemMissionListener() {
	}

	public void onChanged( final long roleid, final int itemid ) {
		mkdb.Trace.debug("ItemTaskListener::onChanged");
		mkdb.Procedure.pexecuteWhileCommit( 
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					TeamManager.getTeamByRoleId(roleid);//强行锁队伍
					final Pack bag = new Pack( roleid, true );
					final int num = bag.getItemNum( itemid, 0 );
					new fire.pb.circletask.CircleTask( roleid, false ).onItemAdded( itemid, num );
					return true;
				}
		} );
	}
}
