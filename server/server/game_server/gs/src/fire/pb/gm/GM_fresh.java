package fire.pb.gm;

import gnet.DeliveryManager;
import gnet.Game2AU;
import xbean.AUUserInfo;

import com.locojoy.base.Marshal.OctetsStream;

public class GM_fresh extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length != 1)
			return false;
		final long roleid = getGmroleid();
		final int userid = getGmUserid();
		String cardnumber = args[0];
		if (cardnumber.length() != 20) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 142465, 0, null);
			return false;
		}
		Game2AU game2au = new Game2AU();
		game2au.qtype = 3;
		game2au.userid = userid;
		game2au.reserved = 0;
		AUUserInfo userInfo = xtable.Auuserinfo.select(userid);
		int ip = 0;
		if (userInfo != null)
			ip = userInfo.getLoginip();
		OctetsStream os = new OctetsStream();
		try {
			os.marshal(roleid);
			os.marshal(cardnumber);
			os.marshal(ip);
			game2au.info = os;
		} catch (Exception e) {
			e.printStackTrace();
		}
		DeliveryManager.getInstance().send(game2au);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
