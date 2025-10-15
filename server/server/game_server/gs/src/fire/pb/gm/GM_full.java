package fire.pb.gm;

public class GM_full extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(
						getGmroleid());
				fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
				send.datas = (java.util.HashMap<Integer, Float>) role
						.recovery();
				if (!send.datas.isEmpty()) {
					mkdb.Procedure.psendWhileCommit(getGmroleid(), send);
					return true;
				} else {
					return false;
				}
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "满状态";
	}

}
