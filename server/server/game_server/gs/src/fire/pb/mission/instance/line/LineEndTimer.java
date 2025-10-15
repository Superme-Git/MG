package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import fire.msp.IGetRolesCallBack;
import fire.pb.map.DuplicateHelper;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.SLineTask;
import fire.pb.move.SRoleEnterScene;
import fire.pb.scene.DynamicScene;
import fire.pb.scene.SceneManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import mkdb.Procedure;

public class LineEndTimer extends TimerTask {

	private final long instOnlyId;
	
	public LineEndTimer(final long instOnlyId) {
		this.instOnlyId = instOnlyId;
	}
	
	@Override
	public void run() {
		new Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.InstanceInfo instInfo = xtable.Instancetask.select(instOnlyId);
				if (instInfo == null)
					return false;
				
				List<Integer> mapList = LineInstManager.getInstance().getInstanceMapId(instInfo.getInstanceid());
				
				for (Integer mapId : mapList) {
					DynamicScene ds = SceneManager.getInstance().getSceneByMapAndOwner(mapId, instOnlyId);
					if (ds == null)
						continue;
					RoleManager.getRolesByConditions(-1, -1, -1, ds.getSceneID(), -1, 
							new IGetRolesCallBack()
							{
								@Override
								public void process(List<Long> roleIds)	{
									Procedure.pexecuteWhileCommit(
											new mkdb.Procedure() {
												@Override
												protected boolean process()	throws Exception {
													for (Long rid : roleIds) {
														final Team team = fire.pb.team.TeamManager.selectTeamByRoleId(rid);
														if (team != null) {
															if (team.isTeamLeader(rid)) {
																Transfer.justGotoWhileCommit(rid, LineInstManager.ZILUOLAN_MAP, LineInstManager.ZILUOLAN_POS_X, LineInstManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
															}
														}
														else {
															Transfer.justGotoWhileCommit(rid, LineInstManager.ZILUOLAN_MAP, LineInstManager.ZILUOLAN_POS_X, LineInstManager.ZILUOLAN_POS_Y, SRoleEnterScene.CHEFU);
														}
													}
													
													DuplicateHelper.destroyDynamicSceneWhileCommit(mapId, instOnlyId);
													return true;
												}
												
											}
									);
								}
							});
				}
				
				SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(instInfo.getInstanceid());
				if (taskCofig == null) {
					LineInstManager.logger.error("副本数据为空 " + instInfo.getInstanceid());
					return false;
				}
				
				for (Long roleid : instInfo.getRoleids()) {
					xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.select(roleid);
					if (infoCol == null) {
						continue;
					}
					
					xbean.InstanceTimeAwardInfo awardInfo = infoCol.getInstcount().get(instInfo.getInstanceid());
					if (awardInfo == null)
						continue;
					
					if (awardInfo.getStepawardtimes().size() == 0)
						continue;
					
					List<String> para = new ArrayList<String>(1);
					para.add(taskCofig.taskname);
					
//					MessageMgr.psendMsgNotifyWhileCommit(roleid, 166072, para);
					
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < 6; i ++) {
						Integer atom = awardInfo.getStepawardtimes().get(i);
						if (atom == null || atom < 1)
							sb.append("关卡").append(i).append(",");
					}
					
					if (sb.length() > 0) {
						sb.deleteCharAt(sb.lastIndexOf(","));
						para.add(sb.toString());
						MessageMgr.psendMsgNotifyWhileCommit(roleid, 166072, para);
					}
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

