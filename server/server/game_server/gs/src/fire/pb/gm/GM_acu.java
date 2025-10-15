package fire.pb.gm;

import fire.pb.skill.PInbornWithValidProc;

public class GM_acu extends GMCommand {

	@Override
	boolean exec(String[] args) {

		int acu = Integer.parseInt(args[0]);
		final long roleid = getGmroleid();

		new PInbornWithValidProc(roleid, acu).submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
