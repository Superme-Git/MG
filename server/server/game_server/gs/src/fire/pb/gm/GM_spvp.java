package fire.pb.gm;

import fire.pb.battle.pvp1.PPvP1NewBattle;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.battle.pvp1.PvP1RaceProxy;
import fire.pb.battle.pvp1.PvP1RoleProxy;

public class GM_spvp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2)
			return false;
		if (args[0].equalsIgnoreCase("battle")) {
			new PPvP1NewBattle(getGmroleid(), Long.valueOf(args[1]), 1).submit();
		} else if (args[0].equalsIgnoreCase("score")) {
			final int addvalue = Integer.valueOf(args[1]);
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					int gradeid = PvP1Helper.getRaceGradeByRoleId(getGmroleid());
					PvP1RaceProxy race = PvP1RaceProxy.getPvP1RaceProxy(gradeid);

					PvP1RoleProxy pvpRole = PvP1RoleProxy.getPvP1RoleProxy(getGmroleid(), false);
					pvpRole.addScore(addvalue, false);
					pvpRole.sendSPvP1MyInfo(race.isReady(getGmroleid()));
					return true;
				};

			}.submit();
		}

		return true;
	}

	@Override
	String usage() {
		return "//spvp battle [otherroleid] || //spvp score [addvalue]";
	}

}
