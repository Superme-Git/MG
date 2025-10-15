package fire.pb.gm;

/***
 * 设置公会战行动力 by changhao
 * @author Administrator
 *
 */
public class GM_clanfightsetact extends GMCommand {

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
			new fire.pb.clan.fight.PClanFightSetAct(roleid, v).submit();
		}
		
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
