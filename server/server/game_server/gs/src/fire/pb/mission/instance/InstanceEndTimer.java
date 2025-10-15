package fire.pb.mission.instance;

import java.util.List;
import java.util.TimerTask;

import fire.pb.map.DuplicateHelper;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.SceneManager;
import fire.pb.team.Team;
import mkdb.Procedure;

public class InstanceEndTimer extends TimerTask {

	private final long instOnlyId;
	
	public InstanceEndTimer(final long instOnlyId) {
		this.instOnlyId = instOnlyId;
	}
	
	@Override
	public void run() {
		new Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.InstanceInfo instInfo = xtable.Instancetask.get(instOnlyId);
				if (instInfo == null)
					return false;
				
				List<Integer> mapList = InstanceManager.getInstance().getInstanceMapId(instInfo.getInstanceid());
				
				for (Integer mapId : mapList) {
					DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(mapId, instOnlyId);
					for (long rid : ds.getSceneRoles().keySet()) {
//						xtable.Role2instancetask.remove(rid);
						
						final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(rid);
						if (team != null) {
							if (team.isTeamLeader(rid)) {
								Transfer.justGotoWhileCommit(rid, InstanceManager.ZILUOLAN_MAP, InstanceManager.ZILUOLAN_POS_X, InstanceManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
							}
						}
						else {
							Transfer.justGotoWhileCommit(rid, InstanceManager.ZILUOLAN_MAP, InstanceManager.ZILUOLAN_POS_X, InstanceManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
						}
					}
				}
				
				for (Integer mapId : mapList) {
					DuplicateHelper.destroyDynamicSceneWhileCommit(mapId, instOnlyId);
				}

				// 删除任务
				xtable.Instancetask.remove(instOnlyId);

				xbean.InstanceFutureInfo futureInfo = xtable.Instancefuture.get(instOnlyId);
				if (futureInfo != null) {
					if (futureInfo.getTimeoutfuture() != null) {
						futureInfo.getTimeoutfuture().cancel(true);
						futureInfo.setTimeoutfuture(null);
					}
					xtable.Instancefuture.remove(instOnlyId);
				}
				
				return true;
			}
		}.submit();
	}
}
