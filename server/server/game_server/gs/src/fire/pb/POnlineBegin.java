package fire.pb;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 角色上线或者下线后，做的一些工作,此处主要是开启或者注销一些计时任务
 *
 */
public class POnlineBegin  extends mkdb.Procedure{
	private final long roleid;
	private boolean begin;
	
	public POnlineBegin(final long roleid, boolean begin){
		this.roleid = roleid;
		this.begin = begin;
	}
	
	public boolean process(){
		xbean.OnlinerefreshFuture xfur = xtable.Onlinerefresh.get(roleid);
		if (begin){
			if(xfur != null && xfur.getFurvec()!= null)
				xfur.getFurvec().cancel(true);
			
			ScheduledFuture<?> fur = mkdb.Mkdb.executor().scheduleAtFixedRate(new fire.pb.timer.PerFiveMinutesTaskForRole(roleid), 5, 5, TimeUnit.MINUTES);
			if (null == xfur){
				xfur = xbean.Pod.newOnlinerefreshFuture();
				xtable.Onlinerefresh.insert(roleid, xfur);
			}
			xfur.setFurvec(fur);
		}
		else{
			if (null == xfur)
				return false;
			xfur.getFurvec().cancel(true);
			xtable.Onlinerefresh.remove(roleid);
		}
		return true;
		
	}
}
