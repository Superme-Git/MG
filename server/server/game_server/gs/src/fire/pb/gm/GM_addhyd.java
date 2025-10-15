package fire.pb.gm;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.mission.activelist.RoleLiveness;

public class GM_addhyd extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数错误：");
			return false;
		}
		final int count = Integer.valueOf(args[0]);
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				RoleLiveness actrole = RoleLiveness
						.getRoleLiveness(getGmroleid());
				actrole.addActivenessAwardToRole(count, YYLoggerTuJingEnum.GM);
				return true;
			};
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
