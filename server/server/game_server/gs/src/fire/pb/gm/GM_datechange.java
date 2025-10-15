package fire.pb.gm;

import fire.pb.timer.BeginOfDayTask;
import fire.pb.timer.EndOfDayTask;

public class GM_datechange extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			new EndOfDayTask().run();
		} else {
			new BeginOfDayTask().run();
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
