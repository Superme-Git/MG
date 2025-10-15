package fire.pb.gm;

import fire.pb.ranklist.proc.RoleLevelSort;

public class GM_sortrose extends GMCommand {

	@Override
	boolean exec(String[] args) {

		new RoleLevelSort().submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
