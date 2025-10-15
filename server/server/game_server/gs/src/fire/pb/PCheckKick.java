package fire.pb;

import java.util.Calendar;

import fire.pb.map.RoleManager;
import fire.pb.util.DateValidate;


/*
 * 5分钟内踢人3次，不允许登陆 by changhao
 */
public class PCheckKick extends mkdb.Procedure {

	public long roleId;
	
	public PCheckKick(long roleid)
	{
		this.roleId = roleid;
	}
	
	public boolean process() {

		long cur = fire.pb.WorldTime.getInstance().getTimeInMillis();
		Integer kick = fire.pb.scene.movable.MoveUnit.NEED_KICK.get();
		if (kick != null && kick == 1)
		{
			xbean.KickInfo kickinfo = xtable.Roleid2kickinfo.get(roleId);
			if (kickinfo != null)
			{
				java.util.List<Long> timelist = new java.util.ArrayList<Long>();
				for (Long t : kickinfo.getKicktime())
				{
					if (!DateValidate.inTheSameDay(cur, t))
					{
						timelist.add(t);
					}
				}
				
				kickinfo.getKicktime().removeAll(timelist);
				
				int size = kickinfo.getKicktime().size();
				if (size >= 5)
				{
					if (cur >= kickinfo.getExpire())
					{
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.HOUR_OF_DAY, 23);
						cal.set(Calendar.MINUTE, 59);
						cal.set(Calendar.SECOND, 59);
						
						long expire = cur + 20 * 60 * 1000;
						if (expire > cal.getTimeInMillis())
						{
							expire = cal.getTimeInMillis();
						}
						
						kickinfo.setExpire(expire);
						while (kickinfo.getKicktime().size() > 4)
						{
							kickinfo.getKicktime().remove(kickinfo.getKicktime().size() - 1);
						}
					}
				}
				
				if (cur < kickinfo.getExpire())
				{
					SReturnLogin msg = new SReturnLogin();
					msg.reason = 2;
					msg.ext = kickinfo.getExpire() - cur;
					
					psend(roleId, msg);
					
					fire.log.Logger logger=fire.log.Logger.getLogger("SYSTEM");
					logger.info("PCheckKick: ["+ roleId +"]");					
				}
			}
		}

		return true;
	}
}
