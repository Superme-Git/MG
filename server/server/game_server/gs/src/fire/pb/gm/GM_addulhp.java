package fire.pb.gm;

import java.util.HashMap;

import fire.pb.attr.SRefreshRoleData;

public class GM_addulhp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int wound = -Integer.parseInt(args[0]);
		if (0 == wound)
			return false;
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(
						getGmroleid());
				SRefreshRoleData sRefreshRoleData = new SRefreshRoleData(
						(HashMap<Integer, Float>) erole.addWound(wound));
				psendWhileCommit(getGmroleid(), sRefreshRoleData);
				return true;
			}

		}.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
