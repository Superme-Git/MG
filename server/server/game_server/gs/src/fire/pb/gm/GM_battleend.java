package fire.pb.gm;

import fire.pb.battle.BattleField;
import fire.pb.util.FireProp;

public class GM_battleend extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long dstroleid = Long.parseLong(args[0]);
		final xbean.Properties prop = xtable.Properties.select(dstroleid);
		if (prop == null) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.checkroleid.unexist");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}

		final Long battleID = xtable.Roleid2battleid.select(dstroleid);
		if (null == battleID) {
			int msgid = fire.pb.util.FireProp.getIntValue(
					fire.pb.talk.MessageMgr.msgprop, "gm.battleend.nobattle");
			fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);
			return false;
		}
		int result = BattleField.BATTLE_DRAW;
		new fire.pb.battle.PBattleEnd(battleID, result).submit();

		int msgid = FireProp.getIntValue(fire.pb.talk.MessageMgr.msgprop,
				"gm.battleend.succ");
		fire.pb.talk.MessageMgr.sendMsgNotify(getGmroleid(), msgid, null);

		StringBuilder content = new StringBuilder("//battleend ");
		content.append(args[0]);
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return "//battleend  $Player$";
	}

}
