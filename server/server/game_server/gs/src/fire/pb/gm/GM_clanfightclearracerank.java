package fire.pb.gm;

/***
 * 公会清理本轮竞赛排行 by changhao
 */
public class GM_clanfightclearracerank extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		
		
		
		try {
			
		} catch (java.lang.NumberFormatException e) {
	
		}
		
		fire.pb.clan.fight.PClanFightClearRaceRank c = new fire.pb.clan.fight.PClanFightClearRaceRank();
		c.submit();
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
