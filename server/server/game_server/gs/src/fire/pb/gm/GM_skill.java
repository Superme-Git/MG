package fire.pb.gm;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import xbean.BattleInfo;
import fire.pb.battle.BattleField;
import fire.pb.battle.OperationType;
import fire.pb.battle.PPlayScript;
import fire.pb.skill.Module;
import fire.pb.skill.SAssistSkill;
import fire.pb.skill.SkillRole;

public class GM_skill extends GMCommand {

	@Override
	boolean exec(String[] args) {
		String action = "";
		if (args.length > 1) {
			action = args[1];

		}

		final Long battleID = xtable.Roleid2battleid.select(getGmroleid());
		if (null == battleID) {
			try {
				if (action.equals("addexp")) {
					final int skillId = Integer.valueOf(args[0]);
					if (args.length < 3)
						return false;
					final long exp = Long.valueOf(args[2]);
					new mkdb.Procedure() {

						@Override
						protected boolean process() {
							SkillRole srole = new SkillRole(getGmroleid());
							srole.addExp(skillId, exp);
							sendToGM("add exp success.");
							return true;
						}
					}.submit();
				} else if (args[0].equals("showexp")) {
					SkillRole srole = new SkillRole(getGmroleid(), true);
					showExp(srole);
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			final int skillId = Integer.valueOf(args[0]);
			final int aim = Integer.valueOf(args[1]);
			try {
				int phase = xtable.Battle.selectPhase(battleID);
				if (phase != BattleInfo.PHASE_WAITOPERATION) {
					sendToGM("战斗不在等待操作阶段，不能使用此GM指令");
					return false;
				}
				new mkdb.Procedure() {

					@Override
					protected boolean process() {
						xbean.BattleInfo battle = xtable.Battle.get(battleID);
						BattleField.cancelTimeOutCheck(battle);
						int fighterId = battle.getRoleids().get(getGmroleid());
						xbean.Decision decision = xbean.Pod.newDecision();
						decision.setOperator(fighterId);
						if (BattleField.checkOutBattle(battle, aim)) {
							return false;
						} else
							decision.setAim(aim);
						decision.setOperatetype(OperationType.ACTION_SKILL);
						decision.setOperateid(skillId);
						battle.getDecisions().put(fighterId, decision);
						return true;
					}
				}.submit().get();
				new PPlayScript(battleID).submit();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		return true;

	}

	private static final int WEAPON_SKILL_ID = 300101;
	private static final int PROTECTION_SKILL_ID = 300301;
	private static final int ORNAMENT_SKILL_ID = 300201;

	private void showExp(SkillRole srole) {
		Map<Integer, xbean.AssistSkill> skills = srole.getAssistSkills();
		Iterator<Entry<Integer, xbean.AssistSkill>> iterator = skills
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Integer, xbean.AssistSkill> current = iterator.next();
			int skillId = current.getKey();
			xbean.AssistSkill skill = current.getValue();
			SAssistSkill asc = Module.getSAssistSkill(skillId);
			if (skillId == WEAPON_SKILL_ID || skillId == PROTECTION_SKILL_ID
					|| skillId == ORNAMENT_SKILL_ID) {

				sendToGM("辅助技能： " + asc.getSkillName() + " 等级为：   "
						+ skill.getLevel() + " 熟练度为 ： " + skill.getExp());

			}
		}

	}

	@Override
	String usage() {
		return "战斗外可使用，升级生活（辅助）技能：//skill [skillid] addlevel 或者 //skill [skillid] addexp [exp] 或者//skill [skillid] showexp";
	}
}
