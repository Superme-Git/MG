package fire.pb.gm;

import java.util.concurrent.TimeUnit;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/***
 * 公会战重新匹配 by changhao
 */
public class GM_clanfightmatch extends GMCommand {

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
