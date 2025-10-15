package fire.pb.gm;

import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAcceptCircTask;
import fire.pb.circletask.SpecialQuestState;

public class GM_ctset extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 2)
			return false;
		long roleid = getGmroleid();
		final Integer circletype = Integer.parseInt(args[0]);
		final Integer circlequest = Integer.parseInt(args[1]);
		//Integer round = 0;
		if (args.length == 3){
		//	round = Integer.parseInt(args[2]);
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask
						.get(roleid);
				if (null == quest_map) {
					quest_map = xbean.Pod.newCircleTaskMap();
					xtable.Rolecircletask.insert(roleid, quest_map);
				}
				xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(
						circletype);
				if (null == questinfo) {
					questinfo = xbean.Pod.newCircleTaskInfo();
					quest_map.getTaskmap().put(circletype, questinfo);
				} else {
					final CircleTask sq = new CircleTask(roleid, false);
					sq.clearSpecialQuestInfo(questinfo);
					questinfo.setQueststate(SpecialQuestState.SUCCESS);
					CircleTaskManager.getInstance().refreshQuestState(roleid,
							circletype, SpecialQuestState.SUCCESS);
				}
				pexecuteWhileCommit(new PAcceptCircTask(roleid, 0, 0,
						circletype, circlequest));
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {

		return "//shimen taskid or //shimen taskid roleid";
	}

}
