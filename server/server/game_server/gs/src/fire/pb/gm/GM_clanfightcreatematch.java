package fire.pb.gm;

/***
 * 创建公会战匹配 by changhao
 * @author Administrator
 *
 */
public class GM_clanfightcreatematch extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}

		try {
			
		} catch (java.lang.NumberFormatException e) {
	
		}

		new fire.pb.clan.fight.PClanFightCreateMatch(true).submit();
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
