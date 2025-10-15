package fire.pb.gm;

import fire.pb.role.PModifyRoleName;

public class GM_modname extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		try {
			new PModifyRoleName(getGmroleid(), args[0], -1, true).submit();
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}

	@Override
	String usage() {

		return "modname roleid name";
	}

}
