package fire.pb.gm;

import fire.pb.mission.SLineTask;
import fire.pb.mission.instance.line.LineInstManager;

public class GM_activeline extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数错误：" + usage());
			return false;
		}

		final int instid = Integer.valueOf(args[0]);
		final long roleid = getGmroleid();

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask
						.get(roleid);
				if (taskInfo == null) {
					taskInfo = xbean.Pod.newInstanceInfoCol();
					xtable.Roleinstancetask.insert(roleid, taskInfo);
				}

				SLineTask taskCofig = LineInstManager.getInstance()
						.getInstanceTask(instid);
				if (taskCofig == null) {
					return false;
				}

				int afterid = instid;

				if (taskCofig.afterid != 0) {
					while (true) {
						taskCofig = LineInstManager.getInstance()
								.getInstanceTask(afterid);
						if (taskCofig == null) {
							return false;
						}
						if (taskCofig.afterid == 0)
							break;

						xbean.InstanceTaskInfo instanceInfo = taskInfo
								.getInstinfo().get(afterid);
						if (instanceInfo == null) {
							instanceInfo = xbean.Pod.newInstanceTaskInfo();
							taskInfo.getInstinfo().put(afterid, instanceInfo);
						}

						instanceInfo.setState(2);
						instanceInfo.setCounts(0);
						afterid = taskCofig.getAfterid();
					}
				}

				xbean.InstanceTaskInfo instanceInfo = taskInfo.getInstinfo()
						.get(afterid);
				if (instanceInfo == null) {
					instanceInfo = xbean.Pod.newInstanceTaskInfo();
					taskInfo.getInstinfo().put(afterid, instanceInfo);
				}

				instanceInfo.setState(2);
				instanceInfo.setCounts(0);
				sendToGM("角色 " + roleid + " 的精英副本 " + instid + " 激活成功");
				return true;
			}

		}.submit();

		return false;
	}

	@Override
	String usage() {
		return "//activeline [副本id]";
	}

}
