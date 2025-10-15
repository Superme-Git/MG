package fire.pb.gm;

import fire.msp.role.GSetRoleHideOrShow;
import fire.pb.GsClient;
import fire.pb.item.SpecialItem;
import fire.pb.util.FireProp;

public class GM_hideme extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleid = getGmroleid();
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				fire.pb.buff.BuffAgent agent = new fire.pb.buff.BuffRoleImpl(
						roleid);
				agent.addCBuffWithSP(SpecialItem.STUPOR_BUFF_ID);
				GSetRoleHideOrShow gSetRoleHideOrShow = new GSetRoleHideOrShow(
						roleid, 1);
				GsClient.pSendWhileCommit(gSetRoleHideOrShow);

				int msgid = FireProp.getIntValue(
						fire.pb.talk.MessageMgr.msgprop, "gm.hideme.succ");
				fire.pb.talk.MessageMgr.psendMsgNotify(getGmroleid(), msgid,
						null);
				return true;
			}

		}.submit();

		return true;
	}

	@Override
	String usage() {

		return "hideme";
	}

}
