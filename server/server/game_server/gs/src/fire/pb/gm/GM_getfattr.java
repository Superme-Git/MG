package fire.pb.gm;

import fire.pb.battle.Fighter;
import fire.pb.talk.MessageMgr;

public class GM_getfattr extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		int fighterId = Integer.valueOf(args[0]);
		String attrname = args[1];

		new PGetFighterAttr(getGmroleid(), fighterId, attrname).submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

	public static class PGetFighterAttr extends mkdb.Procedure {
		private long gmroleId;
		private int fighterId;
		private String attrname;

		public PGetFighterAttr(long gmroleId, int fighterId, String attrname) {
			this.gmroleId = gmroleId;
			this.fighterId = fighterId;
			this.attrname = attrname;
		}

		@Override
		protected boolean process() {
			Long battleId = xtable.Roleid2battleid.select(gmroleId);
			if (battleId == null) {
				battleId = xtable.Watcherid2battleid.select(gmroleId);
				if (battleId == null) {
					MessageMgr.sendMsgToPop(gmroleId, "玩家不在战斗中。");
					return true;
				}
			}

			xbean.BattleInfo battle = xtable.Battle.get(battleId);
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, battle.getRoleids()
					.keySet()));

			Fighter fighter = battle.getFighterobjects().get(fighterId);
			if (fighter == null) {
				MessageMgr.sendMsgToPop(gmroleId, "战斗者不存在。");
				return true;
			}
			if (attrname.equals("")) {
				MessageMgr.sendMsgToPop(gmroleId, "属性名称错误。");
				return true;
			}
			if (attrname.equals("hp")) {
				MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId
						+ " HP为    " + fighter.getEffectRole().getHp());
				return true;
			}
			if (attrname.equals("mp")) {
				MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId
						+ " MP为    " + fighter.getEffectRole().getMp());
				return true;
			}

			if (attrname.equals("sp")) {
				MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId
						+ " SP为    " + fighter.getEffectRole().getSp());
				return true;
			}

			if (attrname.equals("level")) {
				MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId
						+ " 等级为    " + fighter.getEffectRole().getLevel());
				return true;
			}

			int attrId = fire.pb.effect.Module.getInstance().getAttrIdByName(
					attrname);
			if (attrId == 0)
				MessageMgr.sendMsgToPop(gmroleId, "错误的属性名称：" + attrname);
			float value = fighter.getEffectRole().getAttrById(attrId);
			MessageMgr.sendMsgToPop(gmroleId, "战斗者    " + fighterId + " 属性（"
					+ attrname + "）的值为    " + value);

			return true;
		}
	}

}
