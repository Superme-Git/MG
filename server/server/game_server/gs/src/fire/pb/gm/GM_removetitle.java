package fire.pb.gm;

import fire.pb.title.Title;

public class GM_removetitle extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
		}
		long roleid = getGmroleid();
		int titleid = 0;
		if (args.length == 2) {
			roleid = Long.parseLong(args[0]);
			titleid = Integer.parseInt(args[1]);
		} else
			titleid = Integer.parseInt(args[0]);
		Title.removeTitle(roleid, titleid);
		return false;
	}

	@Override
	String usage() {
		return "//removetitle [titleid] or //removetitle [roleid] [titleid]";
	}

}
