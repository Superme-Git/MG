package fire.pb.gm;

import java.util.HashMap;
import java.util.Map;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.PropRole;
import fire.pb.activity.festival.PGetFestivalRewardProc;
import fire.pb.friends.PGiveGift;
import fire.pb.friends.StepSpace;
import fire.pb.main.Gs;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.scene.Scene;
import fire.pb.talk.MessageMgr;
import gnet.DataBetweenAuAnyAndClient;
import gnet.DataBetweenAuAnyAndGS;
import gnet.DeliveryManager;

public class GM_test extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args[0].equals("au")) {
			PropRole prole = new PropRole(getGmroleid(), true);
			DataBetweenAuAnyAndClient ausnd = new DataBetweenAuAnyAndClient();
			ausnd.flag = DataBetweenAuAnyAndClient.CLIENT_TO_AuAny;
			ausnd.qtype = 9001;
			ausnd.userid = prole.getUserid();
			DeliveryManager.getInstance().send(ausnd);
			return true;
		} else if (args[0].equals("gs2au")) {
			PropRole prole = new PropRole(getGmroleid(), true);
			DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
			ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
			ausnd.qtype = 10001;
			ausnd.userid = prole.getUserid();
			String url = "http://127.0.0.1/test.php?params=88,1101961001,131235423523@locojoy,123456,12345678,13,123,1,123455667";
			ausnd.info = com.locojoy.base.Octets.wrap(url, "ISO-8859-1");
			DeliveryManager.getInstance().send(ausnd);
			return true;
		} else if (args[0].equals("zxc")) {
			MessageMgr.sendMsgToPop(getGmroleid(), "你当前已经在跨服PK服务器了啊");
		} else if (args[0].equals("ttt")) {
			Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
			temp.put(98, 123);
			temp.put(12, 123);
			temp.put(67, 123);
			temp.put(0, 123);
			java.util.Set<Integer> temp2 = new java.util.HashSet<Integer>();
			temp2.add(98);
			temp2.add(56);
			temp2.add(0);
			temp2.add(78);
			for (Map.Entry<Integer, Integer> entry : temp.entrySet()) {
				int a = entry.getKey();
				int b = entry.getValue();
				Scene.LOG.debug("a:" + a + ",b:" + b);
			}
			for (Integer entry : temp2) {
				Scene.LOG.debug("entry:" + entry);
			}
			return true;
		} else if (args[0].equals("msg")) {
			int msgid = Integer.parseInt(args[1]);
			long roleid = getGmroleid();
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, msgid, null);
			return true;
		} else if (args[0].equals("reqrefund")) {
			final long roleid = getGmroleid();
			PropRole prole = new PropRole(roleid, true);
			final int userid = prole.getUserid();	
			xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(userid);
			if(userinfo == null)
				return true;
			String username = userinfo.getUsername();
			DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
			ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
			ausnd.qtype = 9001;
			ausnd.userid = userid;
			OctetsStream temp = new OctetsStream();
			temp.marshal(username);
			temp.marshal(Gs.serverid);
			ausnd.info = temp;
			DeliveryManager.getInstance().send(ausnd);
			return true;
		} else if (args[0].equals("getrefund")) {
			final long roleid = getGmroleid();
			PropRole prole = new PropRole(roleid, true);
			final int userid = prole.getUserid();
			xbean.AUUserInfo userinfo = xtable.Auuserinfo.select(userid);
			if(userinfo == null)
				return true;
			String username = userinfo.getUsername();
			DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
			ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
			ausnd.qtype = 9003;
			ausnd.userid = userid;
			OctetsStream temp = new OctetsStream();
			temp.marshal(username);
			temp.marshal(Gs.serverid);
			ausnd.info = temp;
			DeliveryManager.getInstance().send(ausnd);
		} else if (args[0].equals("handact")) {
			final long roleid = getGmroleid();
			int actid = Integer.parseInt(args[1]);
			handact(roleid, actid);
		} else if (args[0].equals("stepspace")) {
			final long roleid = getGmroleid();
			long targetroleid = Long.valueOf(args[1]);
			new StepSpace(roleid, targetroleid).process();
		} else if (args[0].equals("givegift")) {
			long roleid = getGmroleid();
			long targetroleid = Long.valueOf(args[1]);
			PGiveGift proc = new PGiveGift(roleid, targetroleid, 339103, (byte)1, "123你好qwert哈哈@#$%!", (byte)1);//337942 337943
			proc.submit();
		} else if (args[0].equals("getfes")) {
			long roleid = getGmroleid();
			int rewardid = Integer.valueOf(args[1]);
			PGetFestivalRewardProc proc = new PGetFestivalRewardProc(roleid, rewardid);
			proc.submit();
		}

		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void handact(final long roleid, int actid) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(roleid, false);
			    actrole.handleActiveLivenessData(actid);
				return true;
			}
		}.submit();
	}

}
