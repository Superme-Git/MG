package fire.pb.item;

import fire.msp.move.GRoleEquipChange;
import fire.pb.GsClient;
import mkdb.Procedure;

/***
 * 上下坐骑 by changhao
 * @author Administrator
 *
 */
public class PEquipRideProc extends Procedure
{
	public long roleId;
	public int rideitemid;
	public int itemkey;
	
	public PEquipRideProc( long roleId, int rideitmeid, int itemkey)
	{
		this.roleId = roleId;
		this.rideitemid = rideitmeid;
		this.itemkey = itemkey;
	}
	
	@Override
	protected boolean process() throws Exception
	{	
		xbean.Properties prop = xtable.Properties.get(roleId);
		
		int ride = prop.getRide();
		
		if (this.rideitemid != ride)
		{
			ride = this.rideitemid;
		}
		else
		{
			ride = 0;
		}
		
		prop.setRide(ride);
		
		fire.pb.npc.SRideItem rideitem = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRideItem.class).get(ride);
		if (rideitem != null)
		{
			fire.pb.npc.SRide sride = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRide.class).get(rideitem.getRideid());
			if (sride != null)
			{				
				refreshRide(roleId, sride.ridemodel, sride.id, itemkey, rideitemid);
				fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.QI_ZUO_QI);
				return true;
			}
		}
		
		refreshRide(roleId, 0, 0, itemkey, 0);			
		
		return true;
	}
	
	/***
	 * 如果玩家有坐骑上线要通知下，刷新道具 by changhao
	 * @param roleid
	 */
	public static void updateRideMsg(long roleid)
	{
		xbean.Properties prop = xtable.Properties.select(roleid);
		int ride = prop.getRide();
		fire.pb.npc.SRideItem srideitem = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRideItem.class).get(ride);			
		fire.pb.item.SRideUpdate msg = new fire.pb.item.SRideUpdate();
		if (srideitem != null)
		{
			msg.rideid = srideitem.rideid;
			msg.itemid = ride;
			msg.itemkey = -1;
			mkdb.Procedure.psendWhileCommit(roleid, msg);	
		}
	}
	
	public static void refreshRide(final long roleId, int ride, int rideid, int itemkey, int itemid)
	{
		GRoleEquipChange notifymap = new GRoleEquipChange();
		notifymap.roleid = roleId;
		notifymap.pos = -1;
		notifymap.itemid = 0;
		notifymap.ride = ride;
		notifymap.effect = -1;
		GsClient.pSendWhileCommit(notifymap);
		
		if (itemkey != -1)
		{
			fire.pb.item.SRideUpdate msg = new fire.pb.item.SRideUpdate();
			msg.rideid = rideid;
			msg.itemid = itemid;
			msg.itemkey = itemkey;
			mkdb.Procedure.psendWhileCommit(roleId, msg);
		}
		
		Long teamId = xtable.Roleid2teamid.get(roleId);
		if (teamId != null)
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure()
			{
				protected boolean process() throws Exception
				{
					final fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
					if (team != null){
						team.updateTeamMemberComponents2Others(roleId);
					}
					return true;
				};
			});
	}
}
