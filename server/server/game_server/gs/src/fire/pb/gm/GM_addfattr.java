package fire.pb.gm;

import java.util.Map;

import fire.pb.battle.Fighter;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.Module;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.talk.ChannelType;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessage2Client;

public class GM_addfattr extends GMCommand {

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

	public static class PAddFighterAttr extends mkdb.Procedure {
		private long gmroleId;
		private int fighterId;
		private String effectstr;

		public PAddFighterAttr(long gmroleId, int fighterId, String effectstr) {
			this.gmroleId = gmroleId;
			this.fighterId = fighterId;
			this.effectstr = effectstr;
		}

		@Override
		protected boolean process() {
			try {
				Long battleId = xtable.Roleid2battleid.select(gmroleId);

				if (battleId == null) {
					MessageMgr.sendMsgToPop(gmroleId, "玩家不在战斗中。");
					return true;
				}

				xbean.BattleInfo battle = xtable.Battle.get(battleId);
				lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids()
						.keySet()));
				Fighter fighter = battle.getFighterobjects().get(fighterId);
				if (fighter == null) {
					MessageMgr.sendMsgToPop(gmroleId, "战斗者不存在。");
					return true;
				}
				Map<Integer, Float> effects = fire.pb.util.Parser
						.parseEffects(effectstr);
				ConstantlyBuff cbuff = Module.getInstance()
						.createConstantlyBuff(507001);
				cbuff.setEffects(effects);
				fighter.getBuffAgent().addCBuffWithSP(cbuff);
				MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId
						+ " 添加GM属性BUFF:    " + effectstr);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	@Override
	String usage() {
		return null;
	}

}
