package fire.pb.gm;

public class GM_addmp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int mp = Integer.parseInt(args[0]);
		if (0 == mp)
			return false;
		final fire.pb.PAddMpProc proc = new fire.pb.PAddMpProc(getGmroleid(),
				mp);
		proc.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}
}
