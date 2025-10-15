package fire.pb.gm;

import java.util.LinkedList;
import java.util.List;

import fire.pb.talk.MessageMgr;

public class GM_sendmsg extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数不能小于2个  " + usage());
			return true;
		}

		long roleId = Long.valueOf(args[0]);

		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < args.length; i++)
			sb.append(args[i] + " ");
		final String content = sb.toString();

		List<String> param = new LinkedList<String>();
		param.add(content);
		MessageMgr.psendSystemMessageToRole(roleId, 141661, param);
		return true;
	}

	@Override
	String usage() {
		return "//roleid msg";
	}

}
