package fire.pb.gm;

public class GM_timelonger extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int fighterplaytimetimes = Integer.parseInt(args[0]);
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final xbean.Properties prop = xtable.Properties
						.get(getGmroleid());
				if (null == prop)
					return false;
				fire.pb.PropRole role = new fire.pb.PropRole(getGmroleid(),
						false);
				role.setPlaytimetimes(fighterplaytimetimes);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "//timelonger times";
	}

}
