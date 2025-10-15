package fire.pb.gm;

import java.util.HashMap;
import java.util.Map;

import fire.log.beans.RoleSkillUpBean;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.main.ConfigManager;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Module;
import fire.pb.skill.Result;
import fire.pb.skill.SkillRole;
import fire.pb.skill.SubSkillConfig;
import fire.pb.skill.fight.FightSkillConfig;
import fire.pb.skill.liveskill.LiveSkillManager;
import fire.pb.skill.particleskill.SUpdateLearnParticleSkill;
import fire.pb.talk.MessageMgr;
import fire.script.FightJSEngine;
import fire.script.JavaScript;

public class GM_learnxlskill extends GMCommand {

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

		boolean isCheckGuild = false;
		if (args.length == 4) {
			isCheckGuild = Integer.parseInt(args[3]) == 1 ? true : false;
		}

		new PLearnXLSkill(roleid, skillId, skillLv, isCheckGuild).submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

	public static class PLearnXLSkill extends mkdb.Procedure {
		private long roleid;
		private int id;
		private int lv;
		private boolean isCheckGuild;

		public PLearnXLSkill(long roleid, int skillid, int skillLv, boolean isCheckGuild) {
			this.roleid = roleid;
			this.id = skillid;
			this.lv = skillLv;
			this.isCheckGuild = isCheckGuild;
		}

		@Override
		protected boolean process() {
			fire.pb.skill.SLifeSkill config = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class).get(id);

			if (config == null) {
				return true;
			}

			SkillRole skillrole = new SkillRole(roleid);
			int size = skillrole.getPracticeSkills().size();
			if (size == 0) {
				java.util.Map<Integer, fire.pb.skill.SLifeSkill> lifeSkillconfig = ConfigManager.getInstance().getConf(fire.pb.skill.SLifeSkill.class);

				for (java.util.Map.Entry<Integer, fire.pb.skill.SLifeSkill> e1 : lifeSkillconfig.entrySet()) {
					if (e1.getValue().skillType == 3) {
						xbean.PracticeSkill practiceskill = xbean.Pod.newPracticeSkill();
						practiceskill.setExp(0);
						practiceskill.setLevel(0);

						skillrole.getPracticeSkills().put(e1.getKey(), practiceskill);
					}
				}
			}

			xbean.PracticeSkill practiceskill = skillrole.getPracticeSkill(id);
			if (practiceskill == null) {
				return true;
			}

			int curskilllevel = practiceskill.getLevel();
			if (curskilllevel > lv) {
				return true;
			}

			if (curskilllevel >= config.skillLevelMax) {
				MessageMgr.psendMsgNotify(roleid, 160107, null);
				return true;
			}

			if (isCheckGuild) {
				int factionlevel = 0;
				Long factionid = xtable.Roleidclan.select(roleid);
				if (factionid != null) {
					xbean.ClanInfo clanInfo = xtable.Clans.select(factionid);
					if (clanInfo != null) {
						factionlevel = clanInfo.getClanlevel();
					}
				}

				if (curskilllevel >= config.skillLevelMax) // 是否已经达到最大等级 by changhao
				{
					MessageMgr.psendMsgNotify(roleid, 160107, null);
					return true;
				}

				xbean.Properties prop = xtable.Properties.select(roleid);
				xbean.RoleClanPoint contribution = xtable.Roleclanpoint.select(roleid);

				int rolelevel = prop.getLevel();
				int contri = 0;
				if (contribution != null) {
					contri = contribution.getHistoryclanpoint();
				}

				// 得到能学的最大等级 by changhao
				int maxlevel = 0;

				java.util.Map<Integer, Integer> ret = LiveSkillManager.getInstance().GetPracticeSkillMaxLevel(rolelevel, factionlevel, contri, config.skillLevelMax);
				if (ret.size() == 0) {
					return true;
				}

				java.util.Iterator<java.util.Map.Entry<Integer, Integer>> it = ret.entrySet().iterator();
				java.util.Map.Entry<Integer, Integer> entry = it.next();
				maxlevel = entry.getValue();

				if (lv >= maxlevel) {
					lv = maxlevel;
				}
			}
			int maxexp = LiveSkillManager.getInstance().GetPracticleSkillNeedExp(id, curskilllevel);
			while (lv > curskilllevel) {
				curskilllevel++;
				maxexp = LiveSkillManager.getInstance().GetPracticleSkillNeedExp(id, curskilllevel);
				if (maxexp == -1) {
					practiceskill.setLevel(curskilllevel);
					practiceskill.setExp(0);
					break;
				}
			}

			if (curskilllevel > config.skillLevelMax) {
				curskilllevel = config.skillLevelMax;
			}

			practiceskill.setLevel(curskilllevel);
			practiceskill.setExp(0);

			SUpdateLearnParticleSkill msg = new SUpdateLearnParticleSkill();
			msg.skill.level = practiceskill.getLevel();
			msg.skill.id = id;
			msg.skill.exp = practiceskill.getExp();
			msg.skill.maxlevel = config.skillLevelMax;

			if (msg.skill.level > 0) {
				msg.skill.effects.putAll(CalcSkillEffect(config.skillId, msg.skill.level));
				msg.skill.nexteffect.putAll(CalcSkillEffect(config.skillId, msg.skill.level + 1));
				if (practiceskill.getLevel() >= curskilllevel) // 升级刷新属性 by
																// changhao
				{
					Result result = new Result(true);

					// 刷新BUFF by changhao
					Module.logger.info("玩家:" + roleid + "修炼专精技能后加载PracticleSkill技能");
					result.updateResult(skillrole.addPracticleSkillBuff());

					if (!result.getChangedAttrs().isEmpty())// 通知属性改动 by
															// changhao
					{
						mkdb.Procedure.psendWhileCommit(roleid, new SRefreshRoleData((HashMap<Integer, Float>) result.getChangedAttrs()));
					}
					fire.log.YYLogger.roleSkillUPLog(roleid, new RoleSkillUpBean(id, practiceskill.getLevel()));
				}
			}
			mkdb.Procedure.psendWhileCommit(roleid, msg);
			mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(roleid));
			return true;
		}

		public java.util.HashMap<Integer, Float> CalcSkillEffect(int skillid, int level) {
			java.util.HashMap<Integer, Float> effect = new java.util.HashMap<Integer, Float>();

			FightSkillConfig sconf = fire.pb.skill.Module.getInstance().getFightSkillConfig(skillid);
			if (sconf != null) {
				FightJSEngine engine = new FightJSEngine();
				engine.setSkillLevel(level);

				for (SubSkillConfig subskill : sconf.getSubSkills()) {
					for (BuffUnit buffarg : subskill.getBuffUnits()) {
						if (buffarg == null)
							continue;
						for (Map.Entry<Integer, JavaScript> entry : buffarg.effectJavascriptMap.entrySet()) {
							effect.put(entry.getKey(), entry.getValue().eval(engine, null, null).floatValue());
						}
					}
				}
			}

			return effect;
		}
	}
}
