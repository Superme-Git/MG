package fire.pb.gm;

/***
 * 公会战结算 by changhao
 */
public class GM_clanfightendclanfight extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Long v = null;
		try {
			v = Long.parseLong(args[0]);
			
		} catch (java.lang.NumberFormatException e) {
	
		}
		
		if (v != null)
		{
			new fire.pb.clan.fight.PClanFightEndClanFight(v).submit();			
		}
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
