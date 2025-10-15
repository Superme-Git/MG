package fire.pb.gm;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;

public class GM_removebuff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		try {
			if (args[0].equals("all")) {
			} else {
				final int buffId = Integer.valueOf(args[0]);
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						BuffAgent agent = new BuffRoleImpl(getGmroleid());
						agent.removeCBuff(buffId);
						sendToGM("BUFF: " + buffId + " has been removed");
						return true;
					}
				}.submit().get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
