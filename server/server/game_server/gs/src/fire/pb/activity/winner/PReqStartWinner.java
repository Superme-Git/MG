package fire.pb.activity.winner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.MessageUtil;
import fire.pb.util.TaskDlgUtil;
import mkdb.Procedure;


public class PReqStartWinner extends Procedure {

	private final long roleid;

	private final int npccode;
	
	public PReqStartWinner(long roleid, int npccode) {

		this.roleid = roleid;
		this.npccode = npccode;
	}

	@Override
	protected boolean process() throws Exception {

		Team team = TeamManager.selectTeamByRoleId(roleid);
		if(team==null||team.isAbsentMember(roleid)){
			MessageMgr.psendMsgNotify(roleid, 140669, npccode, null);
			return false;
		}
		List<Long> teamMembers = team.getNormalMemberIds();
		// 队员数量大于等于3人
		int teamSize=teamMembers.size();
		if ( teamSize < 3) {
			for (Long member : teamMembers) {
				MessageMgr.psendMsgNotify(member, 140669, npccode, null);
			}
			return false;
		}
		// 判断是否是队长
		if (!TeamManager.isTeamLeader(roleid)) {
            fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 141206,null);
			return false;
		}
		// 等级都大于等于40
		List<Long> levelLess40 = new ArrayList<Long>();
		for (Long mem : teamMembers) {
			PropRole role = new PropRole(mem, true);
			if (role.getLevel() < WinnerManager.MIN_LEVEL) {
				levelLess40.add(mem);
			}
		}
		if (!levelLess40.isEmpty()) {
			String roleNames = MessageUtil.getRoleNames(levelLess40);
			List<String> paras = MessageUtil.getMsgParaList(roleNames);
			for (Long mem : teamMembers) {
				MessageMgr.psendMsgNotify(mem, 140670, npccode, paras);
			}
			return false;
		}
		long teamid = team.teamId;
		if (teamid <= 0){
			return false;
		}
		WinnerRecord record = WinnerManager.getInstance().teams.get(teamid);
		if (record == null) {
			//添加到冠军试炼记录队伍信息中
			record=new WinnerRecord(teamid);
			WinnerManager.getInstance().teams.put(teamid, record);
		}
		
		if (TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID)) {
            fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 144923, npccode, null);
			return true;
		}
		//这个是清除玩家记录，需要放在所有过滤条件之后，要不会清除参加记录
		if(record.getTeamMembers().size()>0){
			//这里需要移除这些玩家在其他队伍的信息
			for(long rid:record.getTeamMembers().values()){
				WinnerManager.getInstance().removeLastTeamid(rid);
			}
		}
		record.getTeamMembers().clear();
		// 判断是否是上次活动的时间
		for (Long member : teamMembers) {
			record.getTeamMembers().put(member, member);
			WinnerManager.getInstance().winnerRoleidsTeamid.put(member, teamid);//记录玩家参加活动对应的队伍信息
		}
		boolean havafirsttask=TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID_ACCEPT);
		if (!WinnerManager.getInstance().isInWinnerActiveTime(record.getStartTime())||
				!havafirsttask) {
			record.setTeamScore(0);
			record.setStartTime(System.currentTimeMillis());
			record.setRoundCostTime(Long.MAX_VALUE);
			//设置当前完成的轮数
			record.setRound(0);
			record.setAllCostTime(0);
			record.setTaskstartTime(0);
			for (Long mem : teamMembers) {
				WinnerManager.clearActiveTeamWinnerScore(mem);//清除活动面板的队伍成绩
			}
			WinnerManager.logger.info("重新接取冠军试炼任务,队伍id\t"+record.getTeamid()+"\t当前队伍成员\t"+record.getTeamMembers().values()+"重新设置原因"+havafirsttask);
		}
		record.setLeaderID(roleid);
		record.setRoundStartTime(System.currentTimeMillis());
		record.setTaskstartTime(System.currentTimeMillis());//任务开始时间
		record.setStageSucc(0);

		// 生成冠军试炼的顺序
		List<Integer> stageList = record.getStageList();
		genStageList(stageList);

		// 发送消息
		int firstStage=stageList.get(0);
		for (Long mem : teamMembers) {
			//如果有冠军试炼传送的任务，需要清除
			if (TaskDlgUtil.existTask(mem, WinnerManager.WINNER_TASK_ID_ACCEPT)){
				mkdb.Procedure proc=new mkdb.Procedure(){
					@Override
					protected boolean process() throws Exception {
						TaskDlgUtil.refreshTaskState(mem, WinnerManager.WINNER_TASK_ID_ACCEPT, SpecialQuestState.ABANDONED);
						return true;
					}
				};
				mkdb.Procedure.pexecuteWhileCommit(proc);
			}
			//增加的任务栏提示
			long npckey = WinnerManager.npc_keys.get(firstStage);
			Npc npc = SceneNpcManager.selectNpcByKey(npckey);
			WinnerManager.getInstance().createWinnerTask(WinnerManager.WINNER_TASK_ID,mem,npc,SpecialQuestState.UNDONE,0,1);//创建任务
		    WinnerManager.getInstance().addWinnerRole(mem);//添加冠军试炼的玩家
		    MessageMgr.psendMsgNotifyWhileCommit(mem, 140666, npccode, Arrays.asList("1",npc.getName()));
		}
		WinnerManager.logger.info("接取冠军试炼任务,队伍id\t"+record.getTeamid()+"\t当前队伍成员\t"+record.getTeamMembers().values()+"\t当前队伍成绩\t"+record.getTeamScore());
		return true;
	}

	/**
	 * 生成冠军试炼关卡数据
	 * @param stageList
	 */
	private void genStageList(List<Integer> stageList) {
		if (!stageList.isEmpty())
			stageList.clear();
		//这里需要注意，第一个npc是领取任务的npc，所有需要排除在外
		for(int i=1;i<WinnerManager.npc_keys.size();i++){
			stageList.add(i);
		}
		//打乱排序
		Collections.shuffle(stageList);
	}

}
