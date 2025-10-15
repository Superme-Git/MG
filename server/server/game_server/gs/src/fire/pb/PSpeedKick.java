package fire.pb;

import fire.pb.map.RoleManager;


/*
 * 加速踢人 by changhao
 */
public class PSpeedKick extends mkdb.Procedure {

	public long roleId;
	public long clienttime;
	public long servertime;
	
	public PSpeedKick(long roleid, long clienttime, long servertime)
	{
		this.roleId = roleid;
		this.clienttime = clienttime;
		this.servertime = servertime;
	}
	
	public boolean process() {

		Integer kick = fire.pb.scene.movable.MoveUnit.NEED_KICK.get();
		if (kick != null && kick == 1)
		{
			xbean.KickInfo kickinfo = xtable.Roleid2kickinfo.get(roleId);
			if (kickinfo == null)
			{
				kickinfo = xbean.Pod.newKickInfo();
				xtable.Roleid2kickinfo.insert(roleId, kickinfo);
			}
			
			long cur = fire.pb.WorldTime.getInstance().getTimeInMillis();
			kickinfo.getKicktime().add(cur);
			
			SReturnLogin msg = new SReturnLogin();
			msg.reason = 1;
			psend(roleId, msg);
		}
		
		fire.log.Logger logger=fire.log.Logger.getLogger("SYSTEM");
		logger.info("CKick: ["+ roleId +"]servertime:["+ this.servertime +"] +clienttime:["+ this.clienttime +"]");
		
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(null == teamId){
			teamId = 0L;
		}
		
		// 运营日志
		fire.pb.map.Role role = RoleManager.getInstance().getRoleByID(roleId);
		if(null != role){
			fire.log.YYLogger.waiGuaLog(roleId, role.getMapId(), teamId, kick);
		}else{
			fire.log.YYLogger.waiGuaLog(roleId, 0, teamId, kick);
		}

		return true;
	}
}
