package fire.pb.fushi.payday;

import java.util.Calendar;
import java.util.Map;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.GsClient;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.fushi.MonthCardManager;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import mkdb.Procedure;

/***
 * 刷新所有日卡消耗 by changhao
 */
public class PRefreshAllDayPayByRoleIds extends Procedure {
	
	public java.util.Set<Long> setroleids;
	public int id;
	
	PRefreshAllDayPayByRoleIds(java.util.Set<Long> setroleids, int id)
	{
		this.setroleids = setroleids;
		this.id = id;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		FushiManager.logger.info("PRefreshAllDayPay 执行 开始 ["+ id +"]");

		final java.util.ArrayList<Integer> userids = new java.util.ArrayList<Integer>();
		final java.util.ArrayList<Long> roleids = new java.util.ArrayList<Long>();
		
		long cur = System.currentTimeMillis();
		for (Long roleid : setroleids)
		{
			xbean.subscription sub = xtable.Subscriptions.select(roleid);
			if(sub != null && sub.getExpiretime() > 0 && cur < sub.getExpiretime()) {
				//如果玩家有订阅记录，且订阅过期时间>0，且当前时间在过期时间内，说明订阅有效，不再做符石点卡的判断
				continue;
			}
			
			xbean.Properties prop = xtable.Properties.select(roleid);
			if (prop != null)
			{
				if (cur > prop.getExpiretime())
				{
					fire.pb.fushi.SCommonDayPay daypay = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(3);

					if (prop.getLevel() >= daypay.serverdata)
					{
						userids.add(prop.getUserid());
						roleids.add(roleid);						
					}
				}						
			}
		}
					
		lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userids));
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids));

		for (Long roleid : roleids)
		{				
			fire.pb.fushi.SCommonDayPay c = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(1);
			
			xbean.Properties prop = xtable.Properties.get(roleid);
			
			if (prop != null)
			{
				boolean ok = false;		
				if(!ConfigManager.FUSHI_2_DAYPAY){
					//FushiManager.logger.info("CostDayPay["+ roleid +"],本服务器关闭了符石买点卡.");
				} else {
					ok = FushiManager.subFushiFromUser(prop.getUserid(), roleid, c.serverdata, 0, 0, FushiConst.REASON_BUY_DAYPAY, YYLoggerTuJingEnum.tujing_Value_daypa, true);
				}
				
				if (ok)
				{
					FushiManager.logger.info("PRefreshAllDayPay["+ prop.getUserid() +"]: 扣除点卡成功:["+ c.serverdata + "]");										
				}
				else
				{
					FushiManager.logger.info("PRefreshAllDayPay["+ prop.getUserid() +"]: 扣除点卡失败:["+ c.serverdata + "]");								
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
					
					fire.pb.fushi.payday.SConsumeDayPay msg1 = new fire.pb.fushi.payday.SConsumeDayPay();
					mkdb.Procedure.psendWhileCommit(roleid, msg1);
					
					MonthCardManager.getInstance().ModifyDayPayMonthCard(prop.getUserid(), roleid);
				}
				else
				{
					msg.daypay = 0;
					GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid,(byte)1));
					mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(roleid));
					fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleid);
				}
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);									
			}						
		}
		
		FushiManager.logger.info("PRefreshAllDayPay 执行 结束 ["+ id +"]");											
		
		return true;
	}
}
