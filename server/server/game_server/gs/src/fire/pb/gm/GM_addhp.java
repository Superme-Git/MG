package fire.pb.gm;

public class GM_addhp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int hp = Integer.parseInt(args[0]);
		if (0 == hp)
			return false;
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;

		final fire.pb.PAddHpProc addhpproc = new fire.pb.PAddHpProc(
				getGmroleid(), hp);
		addhpproc.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
