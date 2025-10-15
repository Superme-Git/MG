package fire.pb.gm;

import fire.pb.mission.activelist.RoleLiveness;

public class GM_addactive extends GMCommand {
	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数错误：" + usage());
			return false;
		}

		final int activeId = Integer.valueOf(args[0]);
		int setcount = 0;
		if (args.length == 2)
			setcount = Integer.valueOf(args[1]);
		final int count = setcount;
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				RoleLiveness actrole = RoleLiveness
						.getRoleLiveness(getGmroleid());
				if (count == 0)
					actrole.addActiveNum(activeId);
				else
					actrole.setActiveNum(activeId, count);
				return true;
			};
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "//addactive [activeid] [第二个参数：如果有的话是设置的值，如果没有则原值+1]";
	}
}
