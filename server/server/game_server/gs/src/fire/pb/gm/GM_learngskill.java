package fire.pb.gm;

import java.util.HashMap;

import fire.log.beans.RoleSkillUpBean;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffConstant;
import fire.pb.main.ConfigManager;
import fire.pb.skill.Result;
import fire.pb.skill.SSkillError;
import fire.pb.skill.SkillConstant;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.skill.liveskill.SUpdateLearnLiveSkill;

public class GM_learngskill extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			return false;
		}

		final int skillId = Integer.parseInt(args[0]);
		int skillLv = Integer.parseInt(args[1]);

		if (skillLv <= 0) {
			return true;
		}

		long roleid = getGmroleid();
		if (args.length == 3) {
			roleid = Long.parseLong(args[2]);
		}

		new PLearnGSkill(roleid, skillId, skillLv).submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

	public static class PLearnGSkill extends mkdb.Procedure {
		private long roleid;
		private int id;
		private int lv;

		public PLearnGSkill(long roleid, int skillid, int skillLv) {
			this.roleid = roleid;
			this.id = skillid;
			this.lv = skillLv;
		}

		@Override
		protected boolean process() {
			if (fire.pb.buff.Module.existState(roleid,
					BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid,
						142383, null);
				return true;
			}

			fire.pb.skill.SLifeSkill config = ConfigManager.getInstance()
					.getConf(fire.pb.skill.SLifeSkill.class).get(id);

			if (config == null) {
				LiveSkillManager.logger
						.error("CRequestLearnLiveSkill config error:" + id);
				psend(roleid, new SSkillError(
						SkillConstant.SkillError.LiveSkillIdError));
				return true;
			}

			fire.pb.skill.SkillRole skillrole = new fire.pb.skill.SkillRole(
					roleid);

			xbean.LiveSkill liveskill = skillrole.getLiveSkills().get(id);

			if (lv > config.skillLevelMax) {
				psend(roleid, new SSkillError(
						SkillConstant.SkillError.SkillMaxLimit));
				return false;
			}

			fire.pb.skill.SLifeSkillCost cost = ConfigManager.getInstance()
					.getConf(fire.pb.skill.SLifeSkillCost.class).get(lv);
			if (cost == null) {
				LiveSkillManager.logger
						.error("CRequestLearnLiveSkill cost error:" + lv);
				psend(roleid, new SSkillError(
						SkillConstant.SkillError.LiveSkillIdError));
				return false;
			}

			xbean.Properties roleproperty = xtable.Properties.get(roleid);

			int roleidlevel = roleproperty.getLevel();

			if (config.studyLevelRule > 0) {
				int needLevelLimit = cost.needLevelList
						.get(config.studyLevelRule - 1);
				if (roleidlevel < needLevelLimit) {
					psend(roleid, new SSkillError(
							SkillConstant.SkillError.MoneyNotEnough));
					return false;
				}
			}

			if (liveskill == null) {
				xbean.LiveSkill e = xbean.Pod.newLiveSkill();
				e.setLevel(lv);
				skillrole.getLiveSkills().put(id, e);

				liveskill = e;
			} else {
				liveskill.setLevel(lv);
			}

			if (config.skillId != 0) {
				Result result = new Result(true);

				result.updateResult(skillrole.addLiveSkillBuff());

				if (!result.getChangedAttrs().isEmpty()) {
					mkdb.Procedure.psendWhileCommit(
							roleid,
							new SRefreshRoleData(
									(HashMap<Integer, Float>) result
											.getChangedAttrs()));
				}
			}

			SUpdateLearnLiveSkill msg = new SUpdateLearnLiveSkill();
			msg.skill.level = liveskill.getLevel();
			msg.skill.id = id;

			mkdb.Procedure.psendWhileCommit(roleid, msg);
			mkdb.Procedure
					.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(
							roleid));
			fire.log.YYLogger.roleSkillUPLog(roleid, new RoleSkillUpBean(id,
					liveskill.getLevel()));
			return true;
		}
	};

}
