package fire.pb.gm;

import mkdb.Procedure;

public class GM_wb extends GMCommand {

	class wb extends Procedure {

		public wb(final long roleId, final int itemId) {
		}

		@Override
		protected boolean process() throws Exception {
			return true;
		}
	}

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int itemId = Integer.parseInt(args[0]);
		if (itemId != 331300 && itemId != 331301)
			return false;
		final long roleId = getGmroleid();
		new wb(roleId, itemId).submit();
		return false;
	}

	@Override
	String usage() {

		return null;
	}

}
