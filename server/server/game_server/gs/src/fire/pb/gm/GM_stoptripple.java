package fire.pb.gm;

import fire.pb.activity.award.RewardMgr;

public class GM_stoptripple extends GMCommand {
	@Override
	boolean exec(String[] args) {

		RewardMgr.setIwebMultiExp(0);
		sendToGM("全服三倍已经关闭!");
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
