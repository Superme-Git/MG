package fire.pb.gm;

import fire.pb.activity.winner.StartWinner;

public class GM_startwinner extends GMCommand {
	@Override
	boolean exec(String[] args) {
		new StartWinner(System.currentTimeMillis()).start();
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
