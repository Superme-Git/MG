package fire.pb.gm;

import fire.pb.PropRole;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.Module;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;

public class GM_addbuff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		try {
			if (args[0].equals("pet")) {
				final int buffid = Integer.valueOf(args[1]);
				long timetmp = 0;
				if (args.length > 2) {
					timetmp = Long.valueOf(args[2]) * 1000;
				}
				final long time = timetmp;
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						PropRole prole = new PropRole(getGmroleid(), true);
						Integer fightpet = prole.getFightpetkey();
						if (fightpet < 0) {
							sendToGM("没有参战宠物，不能学习技能");
							return true;
						}
						BuffAgent agent = new BuffPetImpl(getGmroleid(),
								fightpet);
						ConstantlyBuff cbuff = Module.getInstance()
								.createConstantlyBuff(buffid);
						cbuff.setTime(time);
						agent.addCBuffWithSP(cbuff);
						STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
						sSendChatMsg.roleid = getGmroleid();
						sSendChatMsg.rolename = "Server";
						sSendChatMsg.messagetype = ChannelType.CHANNEL_SYSTEM;
						sSendChatMsg.message = "参战宠物添加BUFF: " + buffid
								+ "(time: " + time / 1000 + "s)";
						psendWhileCommit(getGmroleid(), sSendChatMsg);
						return true;
					}
				}.submit().get();
			} else {

				final long roleid = (args.length > 1) ? Integer
						.valueOf(args[0]) : getGmroleid();
				final int buffid = (args.length > 1) ? Integer.valueOf(args[1])
						: Integer.valueOf(args[0]);
				final long time = 0;

				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						BuffAgent agent = new BuffRoleImpl(roleid);
						ConstantlyBuff cbuff = Module.getInstance()
								.createConstantlyBuff(buffid);
						cbuff.setTime(time);
						boolean success = agent.addCBuffWithSP(cbuff);
						if (success)
							sendToGM("给角色：" + roleid + " 添加buff ：" + buffid);
						return true;
					}
				}.submit().get();
			}
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
