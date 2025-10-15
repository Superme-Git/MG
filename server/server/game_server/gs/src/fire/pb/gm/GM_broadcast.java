package fire.pb.gm;

import fire.pb.talk.BroadcastSysMsg;
import fire.pb.talk.MessageMgr;

public class GM_broadcast extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			return true;
		}

		boolean isFriendMsg = (Integer.valueOf(args[0]) == 1);
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i] + " ");
		final String content = sb.toString();

		if (isFriendMsg) {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					new BroadcastSysMsg().broadcastSysMsg(content);
					return true;
				}
			}.submit();
		} else {
			MessageMgr.sendMsgToSysem(content);
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
