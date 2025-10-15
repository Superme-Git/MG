package fire.pb.gm;

public class GM_addtitle extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
		}

		final int titleid = Integer.parseInt(args[0]);
		String name = new String("");
		long availtime = -1;
		if (args.length > 1) {
			name = args[1];
			if (name.equals("null"))
				name = new String("");
		}
		if (args.length > 2)
			availtime = Long.parseLong(args[2]);
		if (!fire.pb.title.TitleManager.existTitle(titleid)) {
			sendToGM("称谓表中不存在此称谓ID，请参看策划svn/data/xlsdir/titleconfig.xlsx");
			return false;
		}
		long realtime = 0;
		if (availtime < 0) {
			realtime = -1;
		} else
			realtime = availtime * 1000;
		long roleid = getGmroleid();
		if (args.length >= 4)
			roleid = Long.parseLong(args[3]);
		fire.pb.title.Title.addTitle(roleid, titleid, name, realtime);
		return true;
	}

	@Override
	String usage() {
		return "//addtitle [id] [name] [availtime] [roleid]";
	}

}
