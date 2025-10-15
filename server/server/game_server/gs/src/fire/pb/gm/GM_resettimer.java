package fire.pb.gm;

import java.util.concurrent.TimeUnit;

import fire.pb.timer.GameTimeTask;

public class GM_resettimer extends GMCommand {

	@Override
	boolean exec(String[] args) {

		mkdb.Mkdb.executor().scheduleAtFixedRate(new GameTimeTask(), 1, 150,
				TimeUnit.SECONDS);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
