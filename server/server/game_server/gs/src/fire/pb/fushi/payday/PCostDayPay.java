package fire.pb.fushi.payday;

import java.util.List;
import java.util.Map;

import fire.pb.StateCommon;
import fire.pb.fushi.DayPayManager;
import fire.pb.master.MasterManager;
import fire.pb.statistics.StatisticUtil;
import mkdb.Procedure;

/***
 * 日卡扣除符石 by changhao
 *
 */
public class PCostDayPay extends Procedure {

	PCostDayPay(int userid, long roleid)
	{
		this.roleid = roleid;
		this.userid = userid;
	}
	
	@Override
	protected boolean process() throws Exception {
		
		DayPayManager.getInstance().CostDayPay(userid, roleid);
		return true;
	}
	
	int userid;
	long roleid;

}
