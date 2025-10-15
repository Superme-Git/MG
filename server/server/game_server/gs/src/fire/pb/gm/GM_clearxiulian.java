package fire.pb.gm;

import java.util.ArrayList;

import fire.pb.skill.SUpdateAssistSkill;
import fire.pb.skill.SkillRole;

public class GM_clearxiulian extends GMCommand {

	@Override
	boolean exec(String[] args) {

		mkdb.Procedure addContribute = new mkdb.Procedure() {
			@Override
			public boolean process() {

				long memberid = getGmroleid();
				SkillRole srole = new SkillRole(memberid);
				ArrayList<Integer> skillsId = new ArrayList<Integer>();
				skillsId.addAll(srole.getAssistSkills().keySet());
				for (Integer skillId : skillsId) {
					SUpdateAssistSkill updateSkill = new SUpdateAssistSkill();
					updateSkill.assistskill.id = skillId;
					updateSkill.assistskill.exp = 0;
					updateSkill.assistskill.level = 0;
					psend(memberid, updateSkill);
				}
				srole.getAssistSkills().clear();
				return true;
			}
		};

		addContribute.submit();

		return true;
	}

	@Override
	String usage() {
		return "//clearxiulian";
	}

}
