package fire.pb.gm;

import fire.pb.skill.scene.PSceneSkill;

public class GM_useskill extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2)
			return false;
		final long roleId = getGmroleid();
		final int skillId = Integer.valueOf(args[0]);
		final int aimtype = Integer.valueOf(args[1]);
		long aimId = 0;
		if (aimtype == PSceneSkill.GOAL_TEAM_ROLE) {
			aimId = Long.valueOf(args[2]);
		}

		new PSceneSkill(roleId, skillId, aimId, aimtype).submit();

		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
