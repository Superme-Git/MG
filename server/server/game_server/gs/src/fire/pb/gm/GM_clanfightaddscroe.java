package fire.pb.gm;

/***
 * 增加公会战积分 by changhao
 * @author Administrator
 *
 */
public class GM_clanfightaddscroe extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Long roleid = null;
		Integer v = null;
		try {
			roleid = Long.parseLong(args[0]);
			v = Integer.parseInt(args[1]);
			
		} catch (java.lang.NumberFormatException e) {
	
		}
		
		if (roleid != null)
		{
			new fire.pb.clan.fight.PClanFightAddScroeToRole(roleid, v, true).submit();
		}
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
