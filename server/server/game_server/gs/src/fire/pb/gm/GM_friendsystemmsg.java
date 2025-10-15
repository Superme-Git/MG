package fire.pb.gm;

import java.util.LinkedList;
import java.util.List;

import fire.pb.talk.MessageMgr;

/**
 * 好友系统消息
 * @author XGM
 */
public class GM_friendsystemmsg extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM(usage());
			return true;
		}
		final long roleId = Long.valueOf(args[0]);
		final int msgId = Integer.valueOf(args[1]);

		final List<String> params = new LinkedList<String>();
		for (int i = 2; i < args.length; i++) {
			params.add(args[i]);
		}
		MessageMgr.sendSystemMessageToRole(roleId, msgId, params);
		return true;
	}

	@Override
	String usage() {
		return "//friendsystemmsg roleId msgId param...";
	}

}
