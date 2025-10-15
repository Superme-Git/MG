package fire.pb.activity.winner;

import java.util.ArrayList;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.battle.BattleField;
import fire.pb.battle.PNewBattle;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.map.SceneNpcManager;
import fire.pb.mission.activelist.RoleLiveness;
import fire.pb.npc.NpcManager;
import fire.pb.npc.SNpcShare;
import fire.pb.npc.SWinnerChangeTask;
import fire.pb.school.School;
import fire.pb.school.shouxi.ProfessionLeaderEndHandler;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.MessageUtil;
import fire.pb.util.TaskDlgUtil;
import gnet.link.Onlines;

public class CStartWinnerBattle {
	private final long roleid;
	private final long npckey;

	public CStartWinnerBattle(long roleid, long npckey) {
		super();
		this.roleid = roleid;
		this.npckey = npckey;
	}

	public void process() {
		int npccode = SceneNpcManager.selectNpcByKey(npckey).getNpcID();
		//判断玩家是否有冠军试炼任务
		if (!TaskDlgUtil.existTask(roleid, WinnerManager.WINNER_TASK_ID)) {
			MessageMgr.sendMsgNotify(roleid, 141546, npccode, null);
			return;
		}
		xbean.TaskInfos taskInfos = xtable.Roletasks.select(roleid);
		if (taskInfos == null) {
			return ;
		}
		xbean.TaskDlgInfo taskDlgInfo = taskInfos.getTasksmap().get(WinnerManager.WINNER_TASK_ID);
		if (taskDlgInfo == null) {
			return ;
		}
		if(taskDlgInfo.getDstnpcid()!=npccode){
			MessageMgr.sendMsgNotify(roleid, 141546, npccode, null);
			return;
		}
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team == null){
			MessageMgr.sendMsgNotify(roleid, 140681, npccode, null);
			return;
		}
		if(team.isAbsentMember(roleid)){
			MessageMgr.sendMsgNotify(roleid, 140669, npccode, null);
			return ;
		}
		long teamid = team.teamId;
		WinnerRecord record = WinnerManager.getInstance().teams.get(teamid);
		if(record==null){
			MessageMgr.sendMsgNotify(roleid, 140681, npccode, null);
			return;
		}
		if (!WinnerManager.getInstance().isInWinnerActiveTime(record.getRoundStartTime())) {
			MessageMgr.sendMsgNotify(roleid, 140681, npccode, null);
			return;
		}
		List<Long> teamMembers = team.getNormalMemberIds();
		// 判断队伍人数要求
		int num = teamMembers.size();
		if (num < WinnerManager.TEAM_MIN_NUM) {
			for (Long mem : teamMembers) {
				MessageMgr.sendMsgNotify(mem,140669, npccode, null);
			}
			return;
		}
		//判断队伍中是否有小于等级要求的
		// 等级都大于等于40和不符合任务要求的
		List<Long> less_level = new ArrayList<Long>();
		List<Long> nothavetask = new ArrayList<Long>();
		//获得队长任务信息
		xbean.TaskDlgInfo leadertask=TaskDlgUtil.selecTaskDlgInfo(roleid,WinnerManager.WINNER_TASK_ID);
		for (long member : teamMembers) {
			PropRole role = new PropRole(member, true);
			if (role.getLevel() < WinnerManager.MIN_LEVEL) {
				less_level.add(member);
			}
			if(member==roleid){
				continue;
			}
			xbean.TaskDlgInfo task=TaskDlgUtil.selecTaskDlgInfo(member,WinnerManager.WINNER_TASK_ID);
			if(task==null){
				nothavetask.add(member);
			}else{
				if(task.getDstnpcid()!=leadertask.getDstnpcid()||task.getSumnum()!=leadertask.getSumnum()||task.getTaskstate()!=SpecialQuestState.UNDONE){
					nothavetask.add(member);
				}
			}
		}
		if (!less_level.isEmpty()) {
			String roleNames = MessageUtil.getRoleNames(less_level);
			List<String> paras = MessageUtil.getMsgParaList(roleNames);
			for (Long mem : teamMembers) {
				MessageMgr.sendMsgNotify(mem, 140670,npccode, paras);
			}
			return;
		}
		//判断任务是否一致，如果不一致需要通知其他玩家任务同步
		if (!nothavetask.isEmpty()) {
			//发送果不一致需要通知其他玩家任务同步
			for (Long mem : nothavetask) {
				SWinnerChangeTask sWinnerChangeTask = new SWinnerChangeTask();
				Onlines.getInstance().send(mem, sWinnerChangeTask);
			}
			//通知队长
			MessageMgr.sendMsgNotify(roleid, 140681,npccode, null);
			return;
		}
		if (!WinnerManager.getInstance().haveNpcBycode(npccode)) {
			MessageMgr.sendMsgNotify(roleid, 140684, npccode, null);
			return;
		}
		SNpcShare share = NpcManager.getInstance().getNpcShareByID(npccode);
		if(share==null){
			return ;
		}
		WinnerManager.getInstance().reloadTeamInfo(teamid,record);//重新加载队伍信息
		
		for (Long mem : teamMembers) {
			fire.pb.course.CourseManager.achieveCourse(mem, fire.pb.course.CourseType.ACTIVE_COURSE, RoleLiveness.WINNER, 0);
		}
				
		//替换成职业领袖 by changhao
		School s = School.getSchoolByFighterWinner(share.getId());
		if (s != null)
		{
			xbean.ProfessionLeaderInfo pl = xtable.Professionleader.select(s.getValue());
			if (pl != null)
			{
				xbean.Properties prop = xtable.Properties.select(pl.getRoleid());
				if (prop != null)
				{
					new PNewBattle(roleid, share.getBattleinfo(), 20, prop.getRolename(), new WinnerEndHandler(teamid, npccode)).submit();
					return;
				}	
			}
		}
		
		//创建战斗
		new PNewBattle(roleid, share.getBattleinfo(), new WinnerEndHandler(teamid, npccode)).submit();
	}
	
}
