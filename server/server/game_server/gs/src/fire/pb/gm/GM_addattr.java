package fire.pb.gm;

import java.util.Map;

import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.Module;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;

public class GM_addattr extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		try {
			final String effectstr = args[0];
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					try {
						Map<Integer, Float> effects = fire.pb.util.Parser
								.parseEffects(effectstr);
						BuffAgent agent = new BuffRoleImpl(getGmroleid());
						ConstantlyBuff cbuff = Module.getInstance()
								.createConstantlyBuff(507099);
						cbuff.setEffects(effects);
						agent.addCBuffWithSP(cbuff);
						STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
						sSendChatMsg.roleid = getGmroleid();
						sSendChatMsg.rolename = "Server";
						sSendChatMsg.messagetype = ChannelType.CHANNEL_SYSTEM;
						sSendChatMsg.message = "添加GM属性BUFF:" + effectstr;
						psendWhileCommit(getGmroleid(), sSendChatMsg);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return true;
				}
			}.submit().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
