
package fire.pb.fushi;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.GsClient;
import fire.pb.talk.MessageMgr;
import fire.pb.team.PQuitTeamProc;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.FireProp;
import fire.pb.main.ConfigManager;

/***
 * 月卡管理器 by changhao
 * @author Administrator
 *
 */
public class MonthCardManager {

	private static MonthCardManager instance = new MonthCardManager();
	
	public static MonthCardManager getInstance() {
		return instance;
	}

	public void init() throws Exception {

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
			
				return true;
			}

		}.submit();
	}
	
	/**
	 * 检查是否超过月卡购买上限 by changhao
	 * @return 
	 */
	public boolean CheckMonthCardLimit(long roleid)
	{
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.select(roleid);
		if (monthcard == null)
		{
			return false;
		}
		
		long cur = System.currentTimeMillis();
		long end = monthcard.getEndtime();
		long delta = end - cur;
		
		long day1 = 24 * 60 * 60 * 1000;
		
		fire.pb.common.SCommon limit = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(358);
		long day300 = day1 * Integer.parseInt(limit.getValue());
		if (delta >= day300)
		{
			return true;
		}
		
		return false;
	}
	
	/***
	 * 检查是否已经支付月卡 by changhao
	 * @param roleid
	 * @return
	 */
	public boolean CheckMonthCard(long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return false;			
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.select(roleid);
		if (monthcard == null)
		{
			return false;
		}
		
		long cur = System.currentTimeMillis();
		
		if (cur <= monthcard.getEndtime())
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 处理月卡异步调用 by changhao
	 */	
	public void ProcessMonthCard(int userId, long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return;			
		Procedure.pexecuteWhileCommit(new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{				
				boolean ok = MonthCardManager.getInstance().CostMonthCard(userId, roleid);
				
				return ok;
			}
		});
	}
	
	/***
	 * 发送月卡信息 by changhao
	 */
	public void SendMonthCardInfo(long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return;			
		fire.pb.fushi.monthcard.SMonthCard msg1 = new fire.pb.fushi.monthcard.SMonthCard();
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.select(roleid);
		if (monthcard == null)
		{
			mkdb.Procedure.psendWhileCommit(roleid, msg1);			
			return;
		}		
		
		boolean grab = MonthCardManager.getInstance().IsGrabReward(roleid, monthcard.getGrabtime());
		if (grab == true)
		{
			msg1.grab = 0;			
		}
		else
		{
			msg1.grab = 1;
		}

		msg1.endtime = monthcard.getEndtime();
		
		mkdb.Procedure.psendWhileCommit(roleid, msg1);	
	}
	
	/***
	 * 修改点卡服的 月卡数据 by changhao
	 */
	public void ModifyDayPayMonthCard(int userid, long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return;			
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.get(roleid);
		if (monthcard == null)
		{
			monthcard = xbean.Pod.newEMonthCard();
			xtable.Roleid2monthcard.insert(roleid, monthcard);
		}
		
		fire.pb.fushi.monthcard.SMonthCard msg1 = new fire.pb.fushi.monthcard.SMonthCard();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		monthcard.setEndtime(cal.getTimeInMillis());		
		msg1.endtime = monthcard.getEndtime();
		
		boolean grab = MonthCardManager.getInstance().IsGrabReward(roleid, monthcard.getGrabtime());
		if (grab == true)
		{
			msg1.grab = 0;			
		}
		else
		{
			msg1.grab = 1;
		}
		
		mkdb.Procedure.psendWhileCommit(roleid, msg1);
	}
	
	/***
	 * 扣月卡  by changhao
	 * @return
	 */	
	public boolean CostMonthCard(int userid, long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return false;			
		final xbean.User user = xtable.User.get(userid);
		if (user == null)
		{
			return false;
		}
		
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.get(roleid);
		if (monthcard == null)
		{
			monthcard = xbean.Pod.newEMonthCard();
			xtable.Roleid2monthcard.insert(roleid, monthcard);
		}
			
		long cur = System.currentTimeMillis();
				
		fire.pb.common.SCommon price = ConfigManager.getInstance().getConf(fire.pb.common.SCommon.class).get(360);
		boolean ok = FushiManager.subFushiFromUser(userid, roleid, Integer.parseInt(price.getValue()), 0, 0, FushiConst.REASON_BUY_MONTHCARD, YYLoggerTuJingEnum.tujing_Value_monthcard, true);
		
		fire.pb.fushi.monthcard.SMonthCard msg1 = new fire.pb.fushi.monthcard.SMonthCard();
		
		//购买成功 by changhao
		if (ok)
		{				
			long endtime = monthcard.getEndtime();
			if (cur < endtime)
			{
				long day1 = 24 * 60 * 60 * 1000;
				long t = endtime + day1 * 30; 
				monthcard.setEndtime(t);
			}
			else
			{
				Calendar end = Calendar.getInstance();
				end.set(Calendar.HOUR_OF_DAY, 23);
				end.set(Calendar.MINUTE, 59);
				end.set(Calendar.SECOND, 59);
				
				long day1 = 24 * 60 * 60 * 1000;
				long t = end.getTimeInMillis() + day1 * 29; 
				monthcard.setEndtime(t);				
			}
			
			msg1.endtime = monthcard.getEndtime();
			
			boolean grab = MonthCardManager.getInstance().IsGrabReward(roleid, monthcard.getGrabtime());
			if (grab == true)
			{
				msg1.grab = 0;			
			}
			else
			{
				msg1.grab = 1;
			}
			
			FushiManager.logger.info("CostMonthCard["+ roleid +"]: 购买月卡成功:["+ Integer.parseInt(price.getValue()) + "]");
		}
		else
		{
			FushiManager.logger.info("CostMonthCard["+ roleid +"]: 购买月卡失败:["+ Integer.parseInt(price.getValue()) + "]");					
		}
		
		mkdb.Procedure.psendWhileCommit(roleid, msg1);
		
		if (ok == true)
		{
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 178006, null);				
		}
		
		return ok;
	}
	
	/***
	 * 是否已经领取过奖励了 by changhao
	 */
	public boolean IsGrabReward(long roleid, long grabtime)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return false;			
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.select(roleid);
		if (monthcard != null)
		{
			long cur = System.currentTimeMillis();
			if (cur > monthcard.getEndtime())
			{
				return true;
			}
		}
		else
		{
			return true;
		}
		
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);					
		
		Calendar end = Calendar.getInstance();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		
		//已经领取过奖励了 by changhao
		if (grabtime >= start.getTimeInMillis() && grabtime <= end.getTimeInMillis())
		{
			return true;
		}
		
		return false;
	}
	
	public void ProcessLess3Day(long roleid)
	{
		if (fire.pb.fushi.Module.GetPayServiceType() == 1)
			return;			
		xbean.EMonthCard monthcard = xtable.Roleid2monthcard.get(roleid);
		if (monthcard == null)
		{
			return;
		}		
		
		long cur = System.currentTimeMillis();
		long delta = monthcard.getEndtime() - cur;
		long day1 = 24 * 60 * 60 * 1000;
		long day4 = 4 * day1;
		
		if (delta > 0 && delta < day4)
		{
			Calendar start = Calendar.getInstance();
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);					
			
			Calendar end = Calendar.getInstance();
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			
			if (monthcard.getFirstprompttime() >= start.getTimeInMillis() && monthcard.getFirstprompttime() <= end.getTimeInMillis())
			{
				return;
			}
			
			monthcard.setFirstprompttime(cur);
			
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190068, null);	
		}
	}
}
