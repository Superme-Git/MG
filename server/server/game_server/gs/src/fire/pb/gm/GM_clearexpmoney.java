package fire.pb.gm;

public class GM_clearexpmoney extends GMCommand {
	@Override
	boolean exec(String[] args) {

		if (args.length != 2) {
			sendToGM(usage());
			return true;
		}
		final Long roleid = Long.parseLong(args[0]);
		final Integer type = Integer.parseInt(args[1]);
		if (type != 1 && type != 2 && type != 3) {
			sendToGM(usage());
			return true;
		}
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				xbean.RoleQuitStatistics statistics = xtable.Rolequitstatistics
						.get(roleid);
				if (statistics == null)
					return true;
				if (type == 1) {
					statistics.setExp(0);
					sendToGM("角色" + roleid + "的今日获得经验已经清空!");
				}
				if (type == 2) {
					statistics.setMoney(0);
					sendToGM("角色" + roleid + "的今日获得金钱已经清空!");
				}
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {

		return "//clearexpmoney roleid 1/2/3 1清经验,2清金钱,3清储备金";
	}

}
