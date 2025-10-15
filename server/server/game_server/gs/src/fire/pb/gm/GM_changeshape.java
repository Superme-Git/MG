package fire.pb.gm;

public class GM_changeshape extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		final Integer transshape = Integer.parseInt(args[0]);
		final long roleid = getGmroleid();
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (transshape > 0)
					fire.pb.mission.UtilHelper.transform(roleid, transshape,
							90, 0);
				else
					fire.pb.mission.UtilHelper.clearTransformID(roleid, 0);
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "请输入造型id";
	}

}
