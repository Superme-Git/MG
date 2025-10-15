package fire.pb.gm;

import fire.pb.school.shouxi.PRefreshAllShouxis;
import fire.pb.school.shouxi.ProfessionLeaderManager;

public class GM_refreshdaypay extends GMCommand {

	@Override
	boolean exec(String[] args) {
		try
		{
			new fire.pb.fushi.payday.PRefreshAllDayPay().submit().get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	String usage() {

		return "refreshsx";
	}

}
