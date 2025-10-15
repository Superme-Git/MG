package fire.pb.gm;

import fire.pb.PropRole;

public class GM_delfrd extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：请输入角色的id");
			return false;
		}

		final long roleId = Long.valueOf(args[0]);
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				PropRole prole = new PropRole(roleId, true);
				xbean.FriendGroups groups = xtable.Friends.get(roleId);
				if (groups == null) {
					sendToGM("该玩家没有好友");
					return true;
				}
				xtable.Friends.remove(roleId);
				sendToGM(prole.getName() + "   的好友都被清空了");
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
