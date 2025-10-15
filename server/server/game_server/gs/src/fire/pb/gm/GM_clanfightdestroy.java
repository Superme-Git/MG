package fire.pb.gm;

public class GM_clanfightdestroy extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		
		Long clanfightid = null;
		try
		{
			clanfightid = Long.parseLong(args[0]);
			
			new fire.pb.clan.fight.PClanFightDestroy(clanfightid).submit();
		}
		catch (java.lang.NumberFormatException e)
		{

		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
