package fire.pb.gm;

import fire.pb.activity.winner.StopWinner;

public class GM_stopwinner extends GMCommand {
	@Override
	boolean exec(String[] args) {
		new StopWinner().run();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
