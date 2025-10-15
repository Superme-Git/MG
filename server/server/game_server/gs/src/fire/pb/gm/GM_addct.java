package fire.pb.gm;

import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;

public class GM_addct extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2)
			return false;
		long roleid = getGmroleid();
		int circletype = Integer.parseInt(args[0]);
		int addnum = Integer.parseInt(args[1]);
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final CircleTask sq = new CircleTask(roleid, false);
				xbean.CircleTaskInfo sqinfo = sq.getSpecialQuestInfo(circletype);
				if (null == sqinfo)
					return false;
				sqinfo.setRound(sqinfo.getRound() + addnum);
				sqinfo.setSumnum(sqinfo.getSumnum() + addnum);
				CircleTaskManager.sendAllSpecialQuest(roleid);
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//addct taskid sumnums";
	}

}
