package fire.pb.mission.instance;

import java.util.List;

import fire.log.Logger;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.DuplicateHelper;
import mkdb.Procedure;


public class PInstanceTimeOut extends Procedure {
	private final long instanceOnlyid;
	private final int instanceZoneId;
	static Logger logger = Logger.getLogger("TASK");

	public PInstanceTimeOut(final long instanceOnlyid, final int instanceZoneId) {

		super();
		this.instanceOnlyid = instanceOnlyid;
		this.instanceZoneId = instanceZoneId;
	}

	@Override
	protected boolean process() throws Exception {
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(instanceOnlyid);
		if (instInfo != null) {
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, instInfo.getRoleids()));
			lock(mkdb.Lockeys.get(xtable.Locks.ROLEINSTANCETASK,
					instInfo.getRoleids()));
			for (long rid : instInfo.getRoleids()) {
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask
						.get(rid);
				if (infoCol != null) {
					xbean.InstanceTaskInfo info = infoCol.getInstinfo().get(
							instanceZoneId);
					logger.info("副本任务超时   mission clear.roleid:" + rid
							+ "questid:" + instInfo.getCurrenttaskid());

					// 0=没有任务 1=任务成功 2=放弃任务 3=任务异常结束 4=任务领取 5=任务超时
					info.setInstanceflag(2);
					info.setInstanceonlyid(0);
					instInfo.setCurrentstate(SpecialQuestState.ABANDONED);
					InstanceManager.getInstance().refreshTaskState(rid,
							instInfo.getCurrenttaskid(),
							SpecialQuestState.ABANDONED);
				}
				
//				xtable.Role2instancetask.remove(rid);
			}

			// 删除副本
			List<Integer> mapList = InstanceManager.getInstance()
					.getInstanceMapId(instInfo.getInstanceid());
			for (Integer mapId : mapList) {
				DuplicateHelper.destroyDynamicSceneWhileCommit(mapId,
						instanceOnlyid);
			}

			// 删除任务
			xtable.Instancetask.remove(instanceOnlyid);
		}

		xbean.InstanceFutureInfo futureInfo = xtable.Instancefuture
				.get(instanceOnlyid);
		if (futureInfo != null) {
			if (futureInfo.getTimeoutfuture() != null) {
				futureInfo.getTimeoutfuture().cancel(true);
				futureInfo.setTimeoutfuture(null);
			}
			xtable.Instancefuture.remove(instanceOnlyid);
		}
		return true;
	}

}
