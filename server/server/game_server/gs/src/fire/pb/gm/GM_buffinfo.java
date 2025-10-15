package fire.pb.gm;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import fire.pb.PropRole;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffPetImpl;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.talk.ChannelType;
import fire.pb.talk.STransChatMessage2Client;
import fire.pb.timer.ScheduledFutureMap;

public class GM_buffinfo extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			try {
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						BuffAgent agent = new BuffRoleImpl(getGmroleid());
						String buffs = "";
						for (xbean.Buff buff : agent.getAllBuffBeans().values()) {
							buffs += buff.getIndex() + ";";
						}
						sendToGM("BUFFs: " + buffs);
						return true;
					}
				}.submit().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (args[0].equals("pet")) {
				try {
					if (args.length < 2) {
						new mkdb.Procedure() {
							@Override
							protected boolean process() {
								PropRole prole = new PropRole(getGmroleid(),
										true);
								BuffAgent agent = new BuffPetImpl(
										getGmroleid(), prole.getFightpetkey(),
										true);
								String buffs = "";
								for (xbean.Buff buff : agent.getAllBuffBeans()
										.values()) {
									buffs += buff.getIndex() + ";";
								}
								STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
								sSendChatMsg.roleid = getGmroleid();
								sSendChatMsg.rolename = "Server";
								sSendChatMsg.messagetype = ChannelType.CHANNEL_SYSTEM;
								sSendChatMsg.message = "Fight Pet Buffs: "
										+ buffs;
								psendWhileCommit(getGmroleid(), sSendChatMsg);
								return true;
							}
						}.submit().get();
					} else {
						final int buffId = Integer.valueOf(args[1]);
						new mkdb.Procedure() {
							@Override
							protected boolean process() {
								PropRole prole = new PropRole(getGmroleid(),
										true);
								BuffAgent agent = new BuffPetImpl(
										getGmroleid(), prole.getFightpetkey(),
										true);
								xbean.Buff buff = agent.getAllBuffBeans().get(
										buffId);
								if (buff == null) {
									STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
									sSendChatMsg.roleid = getGmroleid();
									sSendChatMsg.rolename = "Server";
									sSendChatMsg.messagetype = ChannelType.CHANNEL_CURRENT;
									sSendChatMsg.message = "参战宠物BUFF: "
											+ buffId + " not exist.";
									psendWhileCommit(getGmroleid(),
											sSendChatMsg);
									return true;
								}
								Long futureId = agent.getAgent()
										.getTimerfutures().get(buff.getIndex());
								long time = 0;
								if (futureId != null) {
									time = ScheduledFutureMap.getInstance()
											.get(futureId)
											.getDelay(TimeUnit.SECONDS);
								}
								STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
								sSendChatMsg.roleid = getGmroleid();
								sSendChatMsg.rolename = "Server";
								sSendChatMsg.messagetype = ChannelType.CHANNEL_CURRENT;
								sSendChatMsg.message = "参战宠物BUFF: " + buffId
										+ " ; buff time: " + time + "s. 效果有：\n";
								for (Map.Entry<Integer, Float> effect : buff
										.getEffects().entrySet()) {
									String name = fire.pb.effect.Module
											.getInstance().getEffectNameById(
													effect.getKey());
									sSendChatMsg.message += (name + "="
											+ effect.getValue() + ";");
								}
								psendWhileCommit(getGmroleid(), sSendChatMsg);
								return true;
							}
						}.submit().get();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (args[0].equals("role")) {
				try {
					final long roleid = Long.valueOf(args[1]);
					new mkdb.Procedure() {
						@Override
						protected boolean process() {
							BuffAgent agent = new BuffRoleImpl(roleid);
							String buffs = "";
							for (xbean.Buff buff : agent.getAllBuffBeans()
									.values()) {
								buffs += buff.getIndex() + ";";
							}
							sendToGM("BUFFs: " + buffs);
							return true;
						}
					}.submit().get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else

			{
				try {
					final int buffId = Integer.valueOf(args[0]);
					new mkdb.Procedure() {
						@Override
						protected boolean process() {
							BuffAgent agent = new BuffRoleImpl(getGmroleid());
							xbean.Buff buff = agent.getAllBuffBeans().get(
									buffId);
							if (buff == null) {
								STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
								sSendChatMsg.roleid = getGmroleid();
								sSendChatMsg.rolename = "Server";
								sSendChatMsg.messagetype = ChannelType.CHANNEL_CURRENT;
								sSendChatMsg.message = "BUFF: " + buffId
										+ " not exist.";
								psendWhileCommit(getGmroleid(), sSendChatMsg);
								return true;
							}
							Long futureId = agent.getAgent().getTimerfutures()
									.get(buff.getIndex());
							long time = 0;
							if (futureId != null) {
								time = ScheduledFutureMap.getInstance()
										.get(futureId)
										.getDelay(TimeUnit.SECONDS);
							}
							STransChatMessage2Client sSendChatMsg = new STransChatMessage2Client();
							sSendChatMsg.roleid = getGmroleid();
							sSendChatMsg.rolename = "Server";
							sSendChatMsg.messagetype = ChannelType.CHANNEL_CURRENT;
							sSendChatMsg.message = "BUFF: " + buffId
									+ " ; buff time: " + time + "s.效果有：\n";
							for (Map.Entry<Integer, Float> effect : buff
									.getEffects().entrySet()) {
								String name = fire.pb.effect.Module
										.getInstance().getEffectNameById(
												effect.getKey());
								sSendChatMsg.message += (name + "="
										+ effect.getValue() + ";");
							}
							psendWhileCommit(getGmroleid(), sSendChatMsg);
							return true;
						}
					}.submit().get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;

	}

	@Override
	String usage() {
		return null;
	}

}
