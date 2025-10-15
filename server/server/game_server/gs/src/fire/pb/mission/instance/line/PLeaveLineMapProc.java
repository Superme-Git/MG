package fire.pb.mission.instance.line;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import fire.msp.IGetRolesCallBack;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;


public class PLeaveLineMapProc extends Procedure{
	
	private final long leaveRoleId;
	private final int gotoType; //0 正常退出; 1 跳转到其他地图
	
	public PLeaveLineMapProc(final long roleId, final int gotoType){
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

		//协助作战 或者有异常的直接传出去
		Role mapRole = RoleManager.getInstance().getRoleByID(leaveRoleId);
		if (mapRole == null) {
			return false;
		}
		
		long scenceId = mapRole.getScene();
//		int mapId = mapRole.getMapId();
		Scene scence = SceneManager.getInstance().getSceneByID(scenceId);
		if (scence == null) {
			return false;
		}
//		long ownerId = scence.getOwnerID();
		
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(leaveRoleId);
		if (taskInfo == null)
			return false;
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(taskInfo.getLastinstanceid());
		if (info == null) {
			return false;
		}
		
		RoleManager.getRolesByConditions(-1, -1, -1, scence.getSceneID(), -1, 
				new IGetRolesCallBack()
				{
					@Override
					public void process(List<Long> roleIds)
					{
						if (roleIds.size() == allRoleIDs.size()) {
							transout();
							
							ScheduledFuture<?> future = mkdb.Mkdb.executor().schedule(
									new LineTimeOut(info.getInstanceonlyid(), taskInfo.getLastinstanceid()),
									LineInstManager.DESTROY_INSTANCE, TimeUnit.SECONDS);
							xbean.InstanceFutureInfo futureInfo = xtable.Instancefuture
									.get(info.getInstanceonlyid());
							if (futureInfo == null) {
								futureInfo = xbean.Pod.newInstanceFutureInfo();
								xtable.Instancefuture.insert(info.getInstanceonlyid(), futureInfo);
							}
							futureInfo.setTimeoutfuture(future);
						}
						else {
							transout();
						}
					}
				});
		
		
		if (info.getInstanceflag() == 2) {//放弃了任务
			LineInstManager.getInstance().refreshTaskState(leaveRoleId,
					info.getCurrenttaskid(), SpecialQuestState.ABANDONED);
		}
		
		return true;
	}
	
	private boolean transout() {
		if (gotoType == 0)
			return Transfer.justGotoWhileCommit(leaveRoleId, LineInstManager.ZILUOLAN_MAP, LineInstManager.ZILUOLAN_POS_X, LineInstManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
		
		return true;
	}
}

