package fire.pb.fushi.monthcard;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.GsClient;
import fire.pb.StateCommon;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.fushi.MonthCardManager;
import fire.pb.main.ConfigManager;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.team.teammelon.PEndMelonTask;
import fire.pb.timer.BeginOfDayTask;
import mkdb.Procedure;

/***
 * 刷新所有 by changhao
 */
public class PRefreshAllMonthCard extends Procedure {
		
	@Override
	protected boolean process() throws Exception
	{
		FushiManager.logger.info("PRefreshAllMonthCard 执行 开始 ");
		
		Map<Long,Role> maproleids = RoleManager.getInstance().getRoles();
		java.util.ArrayList<Long> arrayroleids = new java.util.ArrayList<Long>();
		
		for (Long r : maproleids.keySet())
		{
			if (StateCommon.isOnline(r))
			{
				arrayroleids.add(r);
			}
		}
		
		FushiManager.logger.info("PRefreshAllDayPay 执行 结束 ");
		
		for (Long r : arrayroleids)
		{
			if (r != null)
				MonthCardManager.getInstance().SendMonthCardInfo(r);			
		}
		
		return true;
	}
}
