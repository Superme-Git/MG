package fire.pb.gm;

import java.util.concurrent.TimeUnit;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/***
 * 公会清理历史排行 by changhao
 */
public class GM_clanfightclearhistroyrank extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		
		
		
		try {
			
		} catch (java.lang.NumberFormatException e) {
	
		}
		
		fire.pb.clan.fight.PClanFightClearHistroyRank c = new fire.pb.clan.fight.PClanFightClearHistroyRank();
		c.submit();
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
