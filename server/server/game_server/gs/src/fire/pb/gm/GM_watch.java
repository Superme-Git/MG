package fire.pb.gm;

import fire.pb.battle.watch.PSendWatchBattle;

public class GM_watch extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		long roleid = 0;
		if (args[0].matches("\\d+")) {
			roleid = Integer.valueOf(args[0]);
		}
		if (roleid <= 0) {
			final String name = args[0];
			final Long rid = xtable.Rolename2key.select(name);
			if (rid != null)
				roleid = rid;
		}
		if (roleid <= 0) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		new PSendWatchBattle(getGmroleid(), roleid).submit();
		return true;
	}

	@Override
	String usage() {
		return "//watch [角色ID或者名称]";
	}

}
