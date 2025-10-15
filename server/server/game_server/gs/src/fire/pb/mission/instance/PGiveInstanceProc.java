package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.PNewBattle;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.mission.SLandTask;
import fire.pb.mission.Squestions;
import fire.pb.npc.QuestionLib;
import fire.pb.npc.QuestionManager;
import fire.pb.npc.SAskQuestion;
import fire.pb.npc.SSubmit2Npc;
import fire.pb.npc.SubmitType;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;
import mkdb.Transaction;

public class PGiveInstanceProc extends Procedure{
	
	private final long roleid;

	private final long npckey;
	
	private final int serviceId;
	
	public PGiveInstanceProc(long roleid, long npckey, int serviceId){
		this.roleid = roleid;
		this.npckey = npckey;
		this.serviceId = serviceId;
	}
	
	public boolean GiveRoleAnswerQuestion(final ServiceConfig serviceConfig) {
		Object o = serviceConfig.params.get(ConfigParamName.QUESTIONS_ID);
		if (o == null)
			return false;
		Integer questionsId = (Integer)o;
		QuestionLib lib = QuestionManager.getInstance().getQuestionLibs().get(questionsId);
		if (lib == null)
			return false;
		if (lib.questions.isEmpty())
			return false;
		
		Object key = serviceConfig.params.get(ConfigParamName.QUESTION_ID);
		Squestions question = null;
		if (key != null)
			question = lib.questions.get(key);
		else
			question = lib.randomQuestion();
		if (question == null)
			return false;
		
		//让角色回答问题
		SAskQuestion sask = new SAskQuestion();
		sask.lastresult = 0;
		sask.questionid = question.getId();
		sask.questiontype = SAskQuestion.INSTANCE_ZONE;
		sask.npckey = npckey;
		sask.xiangguanid = 0;
		if (Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleid, sask);
		else
			mkdb.Procedure.psendWhileCommit(roleid, sask);
		return true;
	}
	
	public boolean GiveRoleSubMitItem(final ServiceConfig serviceConfig) {
		@SuppressWarnings("unchecked")
		List<Integer> itemids = (List<Integer>)serviceConfig.params.get(ConfigParamName.SUBMIT_ITEM_IDS);
		int posnum = (Integer)serviceConfig.params.get(ConfigParamName.COUNT);
		
		SSubmit2Npc snd = new SSubmit2Npc();
		snd.npckey = npckey;
		snd.questid = serviceConfig.servId;
		snd.submittype = SubmitType.INSTANCE_ZONE_ITEM;
		snd.availableids.addAll(itemids);
		snd.availablepos = posnum;
		if(Transaction.current() == null)
			gnet.link.Onlines.getInstance().send(roleid, snd);
		else
			mkdb.Procedure.psendWhileCommit(roleid, snd);
		return true;
	}

	@Override
	protected boolean process() throws Exception {
		final int instanceId = InstanceManager.getInstance().getInstanceIdByServiceId(serviceId);
		if (instanceId == -1)
			return false;
		
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleid);
		if (taskInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144838, null);
			return false;
		}
		
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
		if (info == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144838, null);
			return false;
		}
		
		final long taskId = info.getInstanceonlyid();
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(taskId);
		if (instInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144838, null);
			return false;
		}
		
		instInfo.setServiceid(serviceId);
		
		Role mapRole = RoleManager.getInstance().getRoleByID(roleid);
		if(null == mapRole){
			return false;
		}
		
		long currentScenceId = mapRole.getScene();
		if (!check(instInfo, taskId, currentScenceId, instanceId)) {
			return false;
		}
		
		ServiceConfig serviceConfig = InstanceManager.getInstance().getServiceConfig(serviceId);
		if (serviceConfig == null)
			return false;
		
		switch (serviceConfig.instzoneType) {
		case ServiceConfig.TYPE_BATTLE: {
			InstanceBattleEndHandler handler = new InstanceBattleEndHandler(serviceConfig, serviceId, npckey, info.getInstanceonlyid(), instanceId);
			PNewBattle battle = new PNewBattle(roleid, (Integer)serviceConfig.params.get(ConfigParamName.BATTLE_ID), handler);
			return battle.call();
		}
		case ServiceConfig.TYPE_ANSWER_QUESTION:
			if (GiveRoleAnswerQuestion(serviceConfig))
				return true;
			return false;
		case ServiceConfig.TYPE_SUBMIT_ITEM:
			if (GiveRoleSubMitItem(serviceConfig))
				return true;
			return false;
		default:
			break;
		}
		
		return true;
	}

	
	private boolean check(final xbean.InstanceInfo instInfo, final long instOnlyId, final long scenceId, final int instanceId) {
		SLandTask taskCofig = InstanceManager.getInstance().getInstanceTask(instanceId);
		if (taskCofig == null)
			return false;
		
		int tasktypeid = instInfo.getInstanceid();
		
		int mapId = InstanceManager.getInstance().getMapIdByTaskType(tasktypeid, instInfo.getCurrentstep());
		if (mapId == -1) {
			return false;
		}
		
		//不是自己的副本
		if (!InstanceManager.getInstance().isSameDynamicScene(scenceId, instOnlyId, mapId)){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144839, null);
			return false;
		}
		
		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144840, null);
			return false;
		} 
		
		if (team.getTeamLeaderId() != roleid) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144841, null);
			return false;
		}
		
		List<Long> allRoleIDs = new ArrayList<Long>();
		allRoleIDs.addAll(team.getNormalMemberIds());
		int count = 0;
		for (Long roleId : allRoleIDs) {
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleId);
			if (taskInfo == null) {
				continue;
			}
			
			xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
			
			if (info.getInstanceonlyid() == instOnlyId) {
				count ++;
			}
		}
		
		//和队长任务相同的人数小于3人
		if (count < taskCofig.getTeamnum()) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144840, null);
			return false;
		}
		
		return true;
	}
}
