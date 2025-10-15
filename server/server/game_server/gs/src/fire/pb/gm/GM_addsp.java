package fire.pb.gm;

public class GM_addsp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long roleid = getGmroleid();
		final int sp = Integer.parseInt(args[0]);
		if (0 == sp)
			return false;
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(
						roleid);

				erole.addSp(sp, 0);

				final fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
				data.datas
						.put(fire.pb.attr.AttrType.SP, (float) erole.getSp());
				psendWhileCommit(roleid, data);

				return true;
			}
		};

		proc.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}
}
