package fire.pb.gm;

public class GM_lockscreen extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int block = Integer.parseInt(args[0]);
		if (0 != block && 1 != block)
			return false;

		new fire.pb.state.PRoleLockScreen(getGmroleid(),
				(block != 0) ? Boolean.TRUE : Boolean.FALSE).submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
