package fire.pb.gm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.locojoy.base.Octets;

import fire.pb.friends.SSendSystemMessageToRole;
import fire.pb.map.SceneManager;
import fire.pb.talk.MessageMgr;

public class GM_systemmsg extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			this.sendToGM(usage());
			return true;
		}
		final int msgId = Integer.valueOf(args[0]);
		final int type = Integer.valueOf(args[1]);

		final List<String> param = new LinkedList<String>();
		for (int i = 2; i <= args.length - 1; i++) {
			param.add(args[i]);
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {

				if (type == 0) {
					MessageMgr.psendSystemMessageToRole(getGmroleid(), msgId, param);
				} else {
					ArrayList<Octets> octetsList = new ArrayList<Octets>();
					for (String s : param) {
						octetsList.add(convertString2Octets(s));
					}
					SSendSystemMessageToRole msg = new SSendSystemMessageToRole();
					msg.systemroleid = 0;
					msg.contentid = msgId;
					msg.contentparam = octetsList;
					SceneManager.sendAll(msg);
				}
				return true;
			}
		}.submit();
		return true;
	}

	public static com.locojoy.base.Octets convertString2Octets(String str) {
		try {
			return new Octets(str.getBytes("UTF-16LE"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	String usage() {
		return "//systemmsg msgId type parm...   type=0���Լ�����Ϣ  type������0  �������˷���Ϣ    ���������������2��";
	}

}
