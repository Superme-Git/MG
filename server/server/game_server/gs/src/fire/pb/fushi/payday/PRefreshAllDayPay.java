package fire.pb.fushi.payday;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.GsClient;
import fire.pb.StateCommon;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.team.teammelon.PEndMelonTask;
import fire.pb.timer.BeginOfDayTask;
import mkdb.Procedure;

/***
 * 刷新所有日卡消耗 by changhao
 */
public class PRefreshAllDayPay extends Procedure {
	
	public java.util.Set<Long> setroleids;
	public int id;
	
	@Override
	protected boolean process() throws Exception
	{
		FushiManager.logger.info("PRefreshAllDayPay 执行 开始 ");
		
		Map<Long,Role> maproleids = RoleManager.getInstance().getRoles();
		java.util.ArrayList<Long> arrayroleids = new java.util.ArrayList<Long>();
		
		for (Long r : maproleids.keySet())
		{
			if (StateCommon.isOnline(r))
			{
				arrayroleids.add(r);
			}
		}
		
		int size = arrayroleids.size();
		if (size > 10000)
		{
			FushiManager.logger.info("PRefreshAllDayPay 在线人数超过10000人 错误");			
		}
		
		int groundnum = 30; //30人一组 分散处理 by changhao
		int count = 0;
		int total = size;
		while (size > 0)
		{
			java.util.Set<Long> setroleids = new java.util.HashSet<Long>();
			for (int i = count * groundnum; i < count * groundnum + groundnum; i ++)
			{
				if (i < total)
					setroleids.add(arrayroleids.get(i));
			}
			
			size -= groundnum;
			count ++;
			
			if (setroleids.size() > 0)
				mkdb.Executor.getInstance().schedule(new RefreshDayPayTask(setroleids, count), count * 3, TimeUnit.SECONDS);
		}
		
		FushiManager.logger.info("PRefreshAllDayPay 执行 结束 ");	
		
	/*	
		xbean.EDayPay e = xtable.Roledaypay.get(1);
		if (e != null)
		{
			final java.util.ArrayList<Integer> userids = new java.util.ArrayList<Integer>();
			for (xbean.DayPay e1 : e.getRoleid2daypay().values())
			{
				long cur = System.currentTimeMillis();
				if (cur > e1.getExpiretime())
				{
					if (setroleids.contains(e1.getRoleid()))
					{
						userids.add(e1.getUserid());				
					}
				}			
			}
						
			lock(mkdb.Lockeys.get(xtable.Locks.USERLOCK, userids));

			for (xbean.DayPay e1 : e.getRoleid2daypay().values())
			{
				long cur = System.currentTimeMillis();
				if (cur > e1.getExpiretime())
				{
					if (setroleids.contains(e1.getRoleid()))
					{						
						xbean.DayPay daypay = e.getRoleid2daypay().get(e1.getRoleid());
						if (daypay != null)
						{
							fire.pb.fushi.SCommonDayPay c = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(1);

							boolean ok = FushiManager.subFushiFromUser(e1.getUserid(), e1.getRoleid(), c.serverdata, 0, 0, FushiConst.REASON_BUY_DAYPAY, YYLoggerTuJingEnum.tujing_Value_daypa, true);
							if (ok)
							{
								FushiManager.logger.info("PRefreshAllDayPay["+ e1.getUserid() +"]: 扣除点卡成功:["+ c.serverdata + "]");										
							}
							else
							{
								FushiManager.logger.info("PRefreshAllDayPay["+ e1.getUserid() +"]: 扣除点卡失败:["+ c.serverdata + "]");								
							}
							
							fire.pb.fushi.payday.SHaveDayPay msg = new fire.pb.fushi.payday.SHaveDayPay();
							
							if (ok)
							{
								Calendar cal = Calendar.getInstance();
								cal.set(Calendar.HOUR_OF_DAY, 23);
								cal.set(Calendar.MINUTE, 59);
								cal.set(Calendar.SECOND, 59);
								
								daypay.setExpiretime(cal.getTimeInMillis());
								msg.daypay = 1;
								GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(e1.getRoleid(),(byte)0));
								
								fire.pb.fushi.payday.SConsumeDayPay msg1 = new fire.pb.fushi.payday.SConsumeDayPay();
								mkdb.Procedure.psendWhileCommit(e1.getRoleid(), msg1);
							}
							else
							{
								msg.daypay = 0;
								GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(e1.getRoleid(),(byte)1));
								mkdb.Procedure.pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(e1.getRoleid()));
							}
							
							mkdb.Procedure.psendWhileCommit(e1.getRoleid(), msg);							
						}
					}
				}
			}
		}
		
		FushiManager.logger.info("PRefreshAllDayPay 执行 结束 ["+ id +"]");											
	*/
		return true;
	}
}
