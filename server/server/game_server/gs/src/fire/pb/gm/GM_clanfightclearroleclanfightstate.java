package fire.pb.gm;

/***
 * 去除公会战状态 by changhao
 * @author Administrator
 *
 */
public class GM_clanfightclearroleclanfightstate extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Long roleid = null;
		try {
			roleid = Long.parseLong(args[0]);
		}
		catch (java.lang.NumberFormatException e)
		{
		}
		
		new fire.pb.clan.fight.PClanFightClearClanFightState(roleid).submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
