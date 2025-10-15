package fire.pb.gm;

import fire.pb.school.shouxi.PRefreshAllShouxis;
import fire.pb.school.shouxi.ProfessionLeaderManager;

public class GM_refreshpl extends GMCommand {

	@Override
	boolean exec(String[] args) {
		try
		{
			new PRefreshAllShouxis().submit().get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		ProfessionLeaderManager.getInstance().setVoteTime();
		ProfessionLeaderManager.getInstance().setChallengeTime();
		
		return true;
	}

	@Override
	String usage() {

		return "refreshsx";
	}

}
