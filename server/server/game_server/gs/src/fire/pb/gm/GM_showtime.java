package fire.pb.gm;

import fire.pb.WorldTime;

public class GM_showtime extends GMCommand {

	@Override
	boolean exec(String[] args) {
		sendToGM("当前时间为:" + WorldTime.getInstance().getTime());
		return true;
	}

	@Override
	String usage() {
		return "打印当前时间";
	}

}
