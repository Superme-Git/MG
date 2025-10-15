package fire.pb.gm;

import fire.pb.activity.award.RewardMgr;

public class GM_award extends GMCommand {

	@Override
	boolean exec(final String[] args) {
		if (args.length != 1)
			return false;
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				Integer awardid = Integer.parseInt(args[0]);
				long roleid = getGmroleid();
				RewardMgr.getInstance().distributeAllAward(roleid, awardid, null, fire.log.enums.YYLoggerTuJingEnum.GM, 0,
						0, "");
				return true;
			}

		}.submit();
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
