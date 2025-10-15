package fire.pb.mission.instance;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;


public class PLeaveInstanceMapProc extends Procedure{
	
	private final long leaveRoleId;
	private final int gotoType; //0 正常退出; 1 跳转到其他地图
	
	public PLeaveInstanceMapProc(final long roleId, final int gotoType){
		this.leaveRoleId = roleId;
		this.gotoType = gotoType;
	}
	
	@Override
	protected boolean process() throws Exception {

		Team team = TeamManager.selectTeamByRoleId(leaveRoleId);
		List<Long> allRoleIDs = new LinkedList<Long>();
		if (team == null) {
			allRoleIDs.add(leaveRoleId);
		} else if (team.getTeamLeaderId() == leaveRoleId) {
			allRoleIDs.addAll(team.getNormalMemberIds());
		}

		Role mapRole = RoleManager.getInstance().getRoleByID(leaveRoleId);
		if (mapRole == null) {
			return false;
		}
		
		long scenceId = mapRole.getScene();
		Scene scence = SceneManager.getInstance().getSceneByID(scenceId);
		if (scence == null) {
			return false;
		}
		
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(leaveRoleId);
		if (taskInfo == null)
			return false;
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(taskInfo.getLastinstanceid());
		if (info == null) {
			return false;
		}
		
		if (info.getInstanceflag() == 2) {//放弃了任务
			InstanceManager.getInstance().refreshTaskState(leaveRoleId,
					info.getCurrenttaskid(), SpecialQuestState.ABANDONED);
		}
		
		boolean destory = true;
		List<Integer> mapList = InstanceManager.getInstance().getInstanceMapId(taskInfo.getLastinstanceid());
		for (Integer mapId : mapList) {
			DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(mapId, info.getInstanceonlyid());
			if (ds != null) {
				if (ds.getSceneRoles().size() > 0) {
					destory = false;
					break;
				}
			}
		}
		
		if (destory) {
			// 启动一个future,10分钟后如果没有玩家再进入,销毁该副本
			ScheduledFuture<?> future = mkdb.Mkdb.executor().schedule(
					new InstanceTimeOut(info.getInstanceonlyid(), taskInfo.getLastinstanceid()),
					InstanceManager.DESTROY_INSTANCE, TimeUnit.SECONDS);
			xbean.InstanceFutureInfo futureInfo = xtable.Instancefuture
					.get(info.getInstanceonlyid());
			if (futureInfo == null) {
				futureInfo = xbean.Pod.newInstanceFutureInfo();
				xtable.Instancefuture.insert(info.getInstanceonlyid(), futureInfo);
			}
			futureInfo.setTimeoutfuture(future);
		}
		
//		for (Long rid : allRoleIDs) {
//			xtable.Role2instancetask.remove(rid);
//		}
		
		if (gotoType == 0) {
			return Transfer.justGotoWhileCommit(leaveRoleId, InstanceManager.ZILUOLAN_MAP, InstanceManager.ZILUOLAN_POS_X, InstanceManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
		}
		else
			return true;
	}
}
