package fire.pb.gm;

import fire.pb.clan.PUpdateClansProc;

public class GM_bpgx extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new PUpdateClansProc().submit();
		return true;
	}

	@Override
	String usage() {
		return "//bpgx";
	}
}
