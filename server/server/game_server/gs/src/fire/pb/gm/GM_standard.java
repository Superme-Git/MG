package fire.pb.gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import fire.pb.PropRole;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.clan.srv.ClanBaseManager;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.PPutOnEquip;
import fire.pb.main.ConfigManager;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.skill.PInbornWithValidProc;
import fire.pb.skill.Result;
import fire.pb.skill.SkillRole;
import fire.pb.util.Parser;
import fire.pb.util.Parser.ID2Odds;

public class GM_standard extends GMCommand {
	class GMStandardGemInfo {
		public int gemTypeId;
		public int gemNum;
	}

	static class GMStandardInfo {
		public int id;
		public String schoolname;
		public String about;
		public int rolelv;
		public List<Integer> equipinfo;
		public ID2Odds skillinfo;
		public ID2Odds xiulianinfo;
		public ID2Odds guildskillinfo;
		public ID2Odds gemInfo;
		public ID2Odds petInfo;
	}

	static private Map<Integer, GMStandardInfo> standardInfoMap = new HashMap<Integer, GMStandardInfo>();

	static {
		try {
			Map<Integer, SGMStandard> standardInfoOrgMap = ConfigManager.getInstance().getConf(SGMStandard.class);
			for (Entry<Integer, SGMStandard> element : standardInfoOrgMap.entrySet()) {
				GMStandardInfo info = new GMStandardInfo();
				info.id = element.getValue().id;
				info.schoolname = element.getValue().schoolname;
				info.about = element.getValue().about;
				info.rolelv = element.getValue().rolelv;
				info.equipinfo = new ArrayList<Integer>();
				if (element.getValue().equipinfo != null && element.getValue().equipinfo.length() != 0 && !element.getValue().equipinfo.equals("0")) {
					String[] strEquipIds = element.getValue().equipinfo.split(";");
					if (strEquipIds.length > 0) {
						for (int i = 0; i < strEquipIds.length; i++) {
							int equipId = -1;
							equipId = Integer.parseInt(strEquipIds[i]);
							info.equipinfo.add(equipId);
						}
					}
				}

				if (element.getValue().skillinfo != null && !element.getValue().skillinfo.equals("") && !element.getValue().skillinfo.equals("0")) {
					info.skillinfo = Parser.parseIdAndOdds(element.getValue().skillinfo);
				}

				if (element.getValue().xiulianinfo != null && !element.getValue().xiulianinfo.equals("") && !element.getValue().xiulianinfo.equals("0")) {
					info.xiulianinfo = Parser.parseIdAndOdds(element.getValue().xiulianinfo);
				}

				if (element.getValue().guildskillinfo != null && !element.getValue().guildskillinfo.equals("") && !element.getValue().guildskillinfo.equals("0")) {
					info.guildskillinfo = Parser.parseIdAndOdds(element.getValue().guildskillinfo);
				}

				if (element.getValue().geminfo != null && !element.getValue().geminfo.equals("") && !element.getValue().geminfo.equals("0")) {
					info.gemInfo = Parser.parseIdAndOdds(element.getValue().geminfo);
				}

				if (element.getValue().petinfo != null && !element.getValue().petinfo.equals("") && !element.getValue().petinfo.equals("0")) {
					info.petInfo = Parser.parseIdAndOdds(element.getValue().petinfo);
				}

				standardInfoMap.put(element.getKey(), info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int guildLv;
		if (args.length == 2) {
			guildLv = Integer.parseInt(args[1]);
		} else {
			guildLv = 3;
		}
		int standardNo = Integer.valueOf(args[0]);
		if (standardNo <= 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}

		final long roleId = getGmroleid();
		if (!fire.pb.StateCommon.isOnline(roleId))
			return false;

		GMStandardInfo gmConfigInfo = standardInfoMap.get(standardNo);
		if (gmConfigInfo == null) {
			return false;
		}

		final PropRole pRole = new PropRole(roleId, true);
		final int orgRoleLv = pRole.getLevel(); // GM命令前的等级

		// 加银币
		GMInteface.execCommand(-1, roleId, -1, "//addmoney 1000000000", false);

		// 加金币
		GMInteface.execCommand(-1, roleId, -1, "//addgold 1000000000", false);

		// 加符石
		GMInteface.execCommand(getGmUserid(), roleId, -1, "//addfushi 2 10000", false);

		final int targetRoleLv = gmConfigInfo.rolelv; // 目标等级

		// 升级
		if (orgRoleLv < targetRoleLv) {
			GMInteface.execCommand(-1, roleId, -1, "//addlevel " + (targetRoleLv - orgRoleLv), false);
		}

		// 创建公会
		try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					ClanBaseManager.getInstance().GMCreateClan(roleId, pRole.getName(), pRole.getName(), guildLv);
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			sendToGM("执行standard出错失败");
			e.printStackTrace();
			return false;
		}

		// 添加装备
		for (Integer equipId : gmConfigInfo.equipinfo) {
			GMInteface.execCommand(-1, roleId, -1, "//additem " + equipId + " 1", false);
		}

		// 添加宝石
		if (gmConfigInfo.gemInfo != null) {
			for (int i = 0; i < gmConfigInfo.gemInfo.ids.length; i++) {
				int gemId = gmConfigInfo.gemInfo.ids[i];
				int gemNum = gmConfigInfo.gemInfo.odds[i];
				GMInteface.execCommand(-1, roleId, -1, "//additem " + gemId + " " + gemNum, false);
			}
		}

		// 添加修炼技能
		if (gmConfigInfo.xiulianinfo != null) {
			for (int i = 0; i < gmConfigInfo.xiulianinfo.ids.length; i++) {
				int skillId = gmConfigInfo.xiulianinfo.ids[i];
				int skillLv = gmConfigInfo.xiulianinfo.odds[i];
				GMInteface.execCommand(-1, roleId, -1, "//learnxlskill " + skillId + " " + skillLv + " " + roleId + " 1", false);
			}
		}

		// 添加公会生活技能
		if (gmConfigInfo.guildskillinfo != null) {
			for (int i = 0; i < gmConfigInfo.guildskillinfo.ids.length; i++) {
				int skillId = gmConfigInfo.guildskillinfo.ids[i];
				int skillLv = gmConfigInfo.guildskillinfo.odds[i];
				GMInteface.execCommand(-1, roleId, -1, "//learngskill " + skillId + " " + skillLv, false);
			}
		}

		// 添加宠物
		if (gmConfigInfo.petInfo != null) {
			for (int i = 0; i < gmConfigInfo.petInfo.ids.length; i++) {
				int petId = gmConfigInfo.petInfo.ids[i];
				int petLv = gmConfigInfo.petInfo.odds[i];
				GMInteface.execCommand(-1, roleId, -1, "//addpet " + petId + " " + petLv, false);
			}
		}

		new PGmStandard(roleId).submit();
		return true;
	}

	@Override
	String usage() {
		return "addexp addnumber, addnumber != 0";
	}

	public static class PGmStandard extends mkdb.Procedure {
		final private long roleId;

		public PGmStandard(long roleId) {
			this.roleId = roleId;
		}

		@Override
		protected boolean process() throws Exception {
			final PropRole pRole = new PropRole(roleId, true);
			SkillRole sRole = new SkillRole(roleId);
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, true);
			Iterator<ItemBase> it = bag.iterator();
			while (it.hasNext()) {
				final ItemBase item = it.next();
				if (item instanceof EquipItem) {
					new PPutOnEquip(roleId, item.getKey(), ((EquipItem) item).getEquipPos()).call();
				}
			}

			// 技能升级
			new PInbornWithValidProc(roleId, -1).call();

			// 重新加载被动技能buff
			Result result = sRole.addSkillBaseBuff();
			if (result.isSuccess())// 通知属性改动
			{
				mkdb.Procedure.psendWhileCommit(roleId, new SRefreshRoleData((HashMap<Integer, Float>) result.getChangedAttrs()));
			}

			// 更新玩家综合实力排行榜
			mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(roleId));
			return true;
		}
	}
}