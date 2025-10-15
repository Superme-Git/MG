package fire.pb.gm;

import fire.pb.PropRole;
import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircleTask;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.circletask.PAutoAddCircleTask;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.SceneNpcManager;
import fire.pb.state.StateManager;

public class GM_shimen extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1)
			return false;
		long roleid = getGmroleid();
		final Integer circletype = Integer.parseInt(args[0]);
		int tid = 0;
		if (args.length > 1)
			tid = Integer.parseInt(args[1]);
		final Integer questid = tid;
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final CircleTask sq = new CircleTask(roleid, false);
				xbean.CircleTaskInfo sqinfo = sq
						.getSpecialQuestInfo(circletype);
				if (null == sqinfo)
					return false;
				sqinfo.setQueststate(SpecialQuestState.DONE);
				if (sqinfo.getQueststate() == SpecialQuestState.DONE) {
					final PropRole prole = new PropRole(roleid, false);
					long masterkey = CircleTaskManager.getInstance()
							.getRoleMastaerKey(roleid, prole.getSchool());
					fire.pb.map.Npc npc = SceneNpcManager
							.selectNpcByKey(masterkey);
					CircleTask.giveCTAward(roleid, prole.getLevel(),
							circletype, npc, sqinfo, null, false);
					sq.clearSpecialQuestInfo(sqinfo);
					CircleTaskManager.getInstance().refreshQuestState(roleid,
							circletype, SpecialQuestState.SUCCESS);
				}
				final CircTask ct = CircleTaskManager.getInstance()
						.getCircTask(roleid, circletype);
				if (null != ct && ct.gjhuanshu == sqinfo.getSumnum()) {
					return true;
				}
				try {
					new PAutoAddCircleTask(roleid, circletype, questid).call();
				} catch (Exception e) {
					StateManager.logger.error(new StringBuilder("roleId=")
							.append(roleid).append(" 上一环完成发送自动循环师门任务异常："), e);
					return false;
				}
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
