package fire.pb.gm;

import java.util.concurrent.TimeUnit;

import fire.pb.activity.award.RewardMgr;

public class GM_starttripple extends GMCommand {

	@Override
	boolean exec(String[] args) {

		RewardMgr.setIwebMultiExp(2);
		sendToGM("全服三倍已经开启!");
		mkdb.Executor.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				GMCommand command = new GM_stoptripple();
				command.setGmLocalsid(getGmLocalsid());
				command.setGmroleid(getGmroleid());
				command.setGmUserid(getGmUserid());
				command.exec(null);
			}
		}, 2, TimeUnit.HOURS);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
