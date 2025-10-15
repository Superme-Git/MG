package fire.pb.gm;

import java.util.HashMap;

import fire.log.beans.OpLvlBean;
import fire.pb.DataInit;
import fire.pb.PAddExpProc;
import fire.pb.PLevelUpProc;
import fire.pb.PropRole;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.main.ConfigManager;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.role.NextExpAndResMoney;
import fire.pb.skill.Result;
import fire.pb.skill.SkillRole;

public class GM_addlevel extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		int addlevel = Integer.valueOf(args[0]);
		if (addlevel <= 0 || addlevel >= DataInit.ROLE_LEVEL_MAX) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}
		final long roleid;
		if (args.length >= 3)
			roleid = Long.valueOf(args[2]);
		else
			roleid = getGmroleid();

		if (!fire.pb.StateCommon.isOnline(roleid))
			return false;

		PropRole prole = new PropRole(roleid, true);

		final int curlevel = prole.getLevel();
		if (addlevel + curlevel > DataInit.ROLE_LEVEL_MAX) {
			sendToGM("加的等级超过等级上限，最高只能升到155级" + usage());
			addlevel = DataInit.ROLE_LEVEL_MAX - curlevel;
		}
		new PGmAddLevel(getGmUserid(), roleid, addlevel, (args.length >= 2 && args[1].equals("equip"))).submit();
		return true;
	}

	@Override
	String usage() {
		return "addexp addnumber, addnumber != 0";
	}

	public static class PGmAddLevel extends mkdb.Procedure {
		final private int userid;
		final private long roleid;
		final private int addlevel;
		final private boolean equipbest;

		public PGmAddLevel(int userid, long roleid, int addlevel, boolean equipbest) {
			this.userid = userid;
			this.roleid = roleid;
			this.addlevel = addlevel;
			this.equipbest = equipbest;
		}

		@Override
		protected boolean process() throws Exception {
			final PropRole prole = new PropRole(roleid, false);
			final int curlevel = prole.getLevel();
			final int finallevel = addlevel + curlevel;

			NextExpAndResMoney needcfg = ConfigManager.getInstance().getConf(NextExpAndResMoney.class).get(finallevel);
			if (needcfg == null)
				return true;

			SkillRole srole = new SkillRole(roleid);
			for (int i = 0; i < addlevel; i++) {
				int level = prole.getLevel();
				long addexp = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.NextExpAndResMoney.class).get(level).exp;
				final fire.pb.PAddExpProc addexpProc = new fire.pb.PAddExpProc(roleid, addexp, PAddExpProc.GM, "GM_addlevel添加");
				addexpProc.call();
				final PLevelUpProc levelup = new PLevelUpProc(userid, roleid);
				levelup.call();
				int newlevel = xtable.Properties.selectLevel(roleid);
				if (newlevel == curlevel + addlevel)
					break;
			}
			Result result = srole.addSkillBaseBuff();
			if (result.isSuccess()) {
				mkdb.Procedure.psendWhileCommit(roleid, new SRefreshRoleData((HashMap<Integer, Float>) result.getChangedAttrs()));
			}

			if (equipbest) {
				final fire.pb.item.ItemMgrImp im = (fire.pb.item.ItemMgrImp) fire.pb.item.Module.getInstance().getItemManager();
				im.equipAllBest(roleid);
			}

			mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleid));
			return true;
		}

	}

}