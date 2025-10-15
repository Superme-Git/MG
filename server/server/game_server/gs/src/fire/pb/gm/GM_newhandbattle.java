package fire.pb.gm;

import fire.pb.PropRole;
import fire.pb.battle.PNewBattle;
import fire.pb.battle.newhand.CReqNewHandBattle;

public class GM_newhandbattle extends GMCommand {

	@Override
	boolean exec(String[] args) {

		new mkdb.Procedure() {
			private PropRole role;

			@Override
			protected boolean process() {
				final long roleId = getGmroleid();
				if (roleId > 0) {
					role = new PropRole(roleId, true);
					if (role != null) {
						new PNewBattle(roleId, CReqNewHandBattle.BASE_BATTLEID
								+ role.getSchool() - 11).call();
					}
				}
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "newhandbattle";
	}

}
