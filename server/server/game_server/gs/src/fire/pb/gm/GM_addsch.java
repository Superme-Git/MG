package fire.pb.gm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GM_addsch extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		int count = Integer.valueOf(args[0]);
		List<Runnable> timers = new ArrayList<Runnable>();
		for (int i = 0; i < count; i++) {
			timers.add(new Runnable() {

				@Override
				public void run() {
				}
			});

		}
		for (Runnable runable : timers) {
			mkdb.Mkdb.executor().schedule(runable,
					(int) (Math.random() * (10000 - 1000) + 1000),
					TimeUnit.SECONDS);
		}
		return true;

	}

	@Override
	String usage() {
		return null;
	}
}
