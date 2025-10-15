
package fire.pb.fushi;

import java.util.Calendar;
import mkdb.Procedure;
import mkdb.Transaction;
import org.apache.log4j.Logger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.GsClient;
import fire.pb.talk.MessageMgr;
import fire.pb.main.ConfigManager;

/***
 * 点卡管理器 by changhao
 * 还他妈管理订阅
 * @author Administrator
 *
 */
public class DayPayManager {

	public static final Logger logger = Logger.getLogger("DayPay");

	private static DayPayManager instance = new DayPayManager();
	
	public static int daypayLevel = 0;
	
	public static int promptLevel = 30; //提醒等级 by changhao
	
	public static long adddaypatime = 20 * 60 * 1000; //点卡服多15分钟过期时间 by changhao
	
	public static DayPayManager getInstance() {
		return instance;
	}

	/**
	 * 处理日付异步调 by changhao
	 */	
	public void ProcessDayPay(int userId, long roleid)
	{
		mkdb.Procedure proc = new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				xbean.Properties prop = xtable.Properties.select(roleid);
				if (prop != null)
				{
					if (prop.getLevel() >= daypayLevel)
					{
						boolean ok = DayPayManager.getInstance().CheckDayPay(roleid, 0);
						if (ok == false) //没付点卡 by changhao
						{
							boolean suc = DayPayManager.getInstance().CostDayPay(prop.getUserid(), roleid);
							if (suc)
							{
								MonthCardManager.getInstance().ModifyDayPayMonthCard(prop.getUserid(), roleid);
							}
						}
					}
				}
				
				return true;
			}
		};
		if(Transaction.current() != null){
			Procedure.pexecuteWhileCommit(proc);
		} else {
			proc.submit();
		}
	}
	
	/***
	 * 是否已经付了日卡 by changhao
	 * delay 延长一定的时间(只用于扣符石不涉及订阅) by changhao
	 * @param roleid
	 * @return
	 */
	public boolean CheckDayPay(long roleid, long delay)
	{
		long cur = System.currentTimeMillis();
		xbean.subscription sub = xtable.Subscriptions.select(roleid);
		if(sub != null && sub.getExpiretime() > 0 && cur < sub.getExpiretime()) {
			//如果玩家有订阅记录，且订阅过期时间>0，且当前时间在过期时间内，说明订阅有效，不再做符石点卡的判断
			return true;
		} else if(sub != null && sub.getExpiretime() > 0 && cur >= sub.getExpiretime()) {
			if(fire.pb.util.DateValidate.inTheSameDay(sub.getExpiretime(), cur)) {
				//虽然玩家订阅今天过期了，但今天还是看成订阅成功状态，也不再扣今天的符石了
				return true;
			}
			else if (delay != 0) //跨天节骨眼上需要延迟到期时间 by changhao
			{				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(sub.getExpiretime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.MILLISECOND, 999);
				
				long time = cal.getTimeInMillis();
				if (cur < time + delay)
				{
					return true;
				}
			}
		}
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop == null)
		{
			return false;
		}
		
		if (cur > prop.getExpiretime() + delay) //跨天扣符石比较慢 给玩家多15分钟到期时间 by changhao
		{
			return false;
		}
		return true;
	}
		
	/***
	 * 检查是否支付日卡,也检查登记 by changhao
	 * @param roleid
	 * @return
	 */
	public boolean CheckDayPayWithLevel(long roleid)
	{		
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null)
		{
			if (prop.getLevel() >= daypayLevel)
			{
				return CheckDayPay(roleid, DayPayManager.adddaypatime);
			}
			return true;
		}	
		return false;
	}
	
	/***
	 * 检查是否是在免费级别  by changhao     
	 */
	public boolean CheckFree(int level)
	{
		if (level >= daypayLevel)
		{
			return false;
		}

		return true;
	}
	
	/***
	 * 检查是否能拍卖 by changhao
	 * @param level
	 * @return
	 */
	public boolean CheckTrade(long roleid, int level)
	{
		boolean free = CheckFree(level);
		if (free == true)
		{
			return false;
		}
		else
		{
			return CheckDayPayWithLevel(roleid);
		}
	}
	
	/***
	 * 扣日卡 by changhao
	 * @return
	 */	
	public boolean CostDayPay(int userid, long roleid)
	{
		final xbean.User user = xtable.User.get(userid);
		if (user == null)
		{
			return false;
		}
		
		xbean.Properties prop = xtable.Properties.get(roleid);
		if (prop == null)
		{
			return false;
		}
		
		long cur = System.currentTimeMillis();
		xbean.subscription sub = xtable.Subscriptions.get(roleid);
		if(sub != null && sub.getExpiretime() > 0 && cur < sub.getExpiretime()) {
			//如果玩家有订阅记录，且订阅过期时间>0，且当前时间在过期时间内，说明订阅有效，不再做符石点卡的扣除逻辑
			FushiManager.logger.info("CostDayPay["+ roleid +"]已经订阅游戏,到期时间:["+ sub.getExpiretime() + "].");
			return false;
		} else if(sub != null && sub.getExpiretime() > 0 && cur >= sub.getExpiretime()) {
			if(fire.pb.util.DateValidate.inTheSameDay(sub.getExpiretime(), cur)) {
				//虽然玩家订阅今天过期了，但今天还是看成订阅成功状态，也不再扣今天的符石了
				FushiManager.logger.info("CostDayPay["+ roleid +"]订阅游戏今天已经到期,但今天不扣符石.");
				return false;
			}
		}

		if (cur <= prop.getExpiretime())
		{
			return false;
		}
				
		fire.pb.fushi.SCommonDayPay c = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(1);

		boolean ok = false;
		boolean first = false;
		if (prop.getFirstprompt() == 0) //第一次提示 by changhao
		{
			fire.pb.fushi.payday.SQueryConsumeDayPay msg1 = new fire.pb.fushi.payday.SQueryConsumeDayPay();
			mkdb.Procedure.psendWhileCommit(roleid, msg1);
			prop.setFirstprompt(1);
			ok = false;
			first = true;
		}
		else
		{
			if(!ConfigManager.FUSHI_2_DAYPAY){
				FushiManager.logger.info("CostDayPay["+ roleid +"],本服务器关闭了符石买点卡.");
			} else {
				ok = FushiManager.subFushiFromUser(userid, roleid, c.serverdata, 0, 0, FushiConst.REASON_BUY_DAYPAY, YYLoggerTuJingEnum.tujing_Value_daypa, true);
			}
		}
		
		fire.pb.fushi.payday.SHaveDayPay msg = new fire.pb.fushi.payday.SHaveDayPay();
		
		if (ok)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			
			prop.setExpiretime(cal.getTimeInMillis());
			
			msg.daypay = 1;
			GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid,(byte)0));
		}
		else
		{
			msg.daypay = 0;
			GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid,(byte)1));
			
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(roleid));
			
			fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleid);
		}
		
		if (first == false)
		{
			mkdb.Procedure.psendWhileCommit(roleid, msg);
		}
		
		
		if (ok)
		{
			fire.pb.fushi.payday.SConsumeDayPay msg1 = new fire.pb.fushi.payday.SConsumeDayPay();
			mkdb.Procedure.psendWhileCommit(roleid, msg1);
			
			FushiManager.logger.info("CostDayPay["+ roleid +"]: 扣除点卡成功:["+ c.serverdata + "]");
		}	
		else
		{
			FushiManager.logger.info("CostDayPay["+ roleid +"]: 扣除点卡失败:["+ c.serverdata + "]");					
		}
		
		return ok;
	}
		
	/**
	 * 有订阅并且没有到期 by changhao
	 * @param roleid
	 * @return
	 */
	public boolean haveSubscribeAndNoExpire(long roleid)
	{
		xbean.subscription sub = xtable.Subscriptions.select(roleid);
		if (sub != null)
		{
			long cur = System.currentTimeMillis();
			if (cur < sub.getExpiretime())
			{
				return true;
			}
			
			if(fire.pb.util.DateValidate.inTheSameDay(sub.getExpiretime(), cur)) //如果同一天也没过期 by changhao
			{
				return true;
			}
		}
		
		return false;
	}
	
	/***
	 * 扣点提前提醒 by changhao
	 */
	public void Prompt(long roleid)
	{
		mkdb.Procedure proc = new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				xbean.Properties prop = xtable.Properties.select(roleid);
				if (prop != null)
				{
					if (prop.getLevel() >= promptLevel && prop.getLevel() < daypayLevel)
					{
						MessageMgr.sendMsgNotify(roleid, 162172, null);					
					}
				}
				
				return true;
			}
		};
		
		if(Transaction.current() != null)
		{
			Procedure.pexecuteWhileCommit(proc);
		}
		else
		{
			proc.submit();
		}		
	}
	
	/***
	 * 弹出扣除点卡确认提示  by changhao
	 */
	public void CostDayPayPrompt(long roleid)
	{
		mkdb.Procedure proc = new mkdb.Procedure()
		{
			@Override
			protected boolean process() throws Exception
			{
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null)
				{
					if (prop.getLevel() == promptLevel)
					{	
						if (prop.getFirstprompt() == 0)
						{
							fire.pb.fushi.payday.SQueryConsumeDayPay msg1 = new fire.pb.fushi.payday.SQueryConsumeDayPay();
							mkdb.Procedure.psendWhileCommit(roleid, msg1);
							prop.setFirstprompt(1);
						}
					}
				}
				
				return true;
			}
		};
		
		if(Transaction.current() != null)
		{
			Procedure.pexecuteWhileCommit(proc);
		}
		else
		{
			proc.submit();
		}		
	}
}
