package fire.pb.gm;

public class GM_title extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final long roleId = Long.parseLong(args[0]);
		final int titleId = Integer.parseInt(args[1]);

		xbean.Properties prop = xtable.Properties.select(roleId);

		if (prop.getTitles().get(titleId) == null) {
			sendToGM(prop.getRolename() + "没有title");
		} else {
			sendToGM(prop.getRolename() + "有title");
		}
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
