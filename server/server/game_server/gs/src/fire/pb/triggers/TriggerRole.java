package fire.pb.triggers;

import java.util.List;
import java.util.Set;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.map.RoleManager;
import fire.pb.mission.PAcceptMajorMission;
import fire.pb.talk.MessageMgr;
import fire.pb.team.TeamManager;
import fire.pb.triggers.event.TriggerEvent;

public class TriggerRole
{
	private long roleId;
	private xbean.TriggerRole triggerbean;
	
	public long getRoleid(){
		return roleId;
	}
	
	public TriggerRole(long roleId, boolean readonly)
	{
		this.roleId = roleId;
		if(readonly)
			triggerbean = xtable.Triggerroles.select(roleId);
		else
			triggerbean = xtable.Triggerroles.get(roleId);
		if(triggerbean == null)
		{
			if(readonly)
				triggerbean = xbean.Pod.newTriggerRoleData();
			else
			{
				triggerbean = xbean.Pod.newTriggerRole();
				xtable.Triggerroles.insert(roleId, triggerbean);
			}
		}
	}
	
	public boolean isTriggered(int triggerId)
	{
		return triggerbean.getTriggeredids().contains(triggerId);
	}
	
	public void trigger4taskcom(int taskid)
	{
		Set<Integer> temp = Module.getInstance().getTriggerIdByTaskId(taskid);
		if(temp != null && temp.size() > 0){
			for(Integer triggerid : temp)
			{
				mkdb.Trace.debug("taskid:" + taskid + "完成！手动触发:" + triggerid);
				triggerbean.getTriggeredids().add(triggerid);
			}
		}
	}
	
	/**直接通过id触发*/
	public boolean trigger(int triggerId)
	{
		if(isTriggered(triggerId))
			return false;
		
		TriggerEvent triggerevent = Module.getInstance().getTriggerEvents().get(triggerId);
		if(triggerevent == null)
			return false;
		if(!valid(triggerevent))
			return false;
		
		sendMsg(triggerevent);
		reward(triggerevent);
		removeQuest(triggerevent);
		addQuest(triggerevent);
		
		triggerbean.getTriggeredids().add(triggerId);
		return true;
	}
	
	/**根据类型和参数获取触发的id*/
	public int getTriggerByType(int triggerType, List<Integer> args)
	{
		int triggered = 0;
		for(TriggerEvent triggerevent : Module.getInstance().getTriggerEvents().values())
		{
			if(triggerevent.type != triggerType)
				continue;
			if(isTriggered(triggerevent.id))
				continue;
			if(!valid(triggerevent))
				continue;
			if(triggerevent.triggered(this, args))
			{
				triggered = triggerevent.id;
				break;
			}
		}
		return triggered;
	}
	/**通过类型和参数来检测是否触发*/
	public boolean triggerByType(int triggerType, List<Integer> args)
	{
		int triggered = getTriggerByType(triggerType, args);
		if(triggered == 0)
			return false;
		
		return trigger(triggered);
	}
	
	public void triggersByType(int triggerType, List<Integer> args)
	{
		for(TriggerEvent triggerevent : Module.getInstance().getTriggerEvents().values())
		{
			if(triggerevent == null)
				continue;
			if(triggerevent.type != triggerType)
				continue;
			if(isTriggered(triggerevent.id))
				continue;
			if(!valid(triggerevent))
				continue;
			if(triggerevent.triggered(this, args))
			{
				mkdb.Trace.debug("triggered:" + triggerevent.id);
				sendMsg(triggerevent);
				reward(triggerevent);
				removeQuest(triggerevent);
				addQuest(triggerevent);	
				triggerbean.getTriggeredids().add(triggerevent.id);
			}
		}
	}
	
	/**检查某个角色是否在某个区域内*/
	public boolean inRegion(int regionId)
	{
		fire.pb.map.Role role = RoleManager.getInstance().getRoleByID(roleId);
		return role.isInRegion(regionId);
	}
	
	/**
	 * 检查前置条件
	 */
	public boolean valid(TriggerEvent triggerevent)
	{
		int level = xtable.Properties.selectLevel(roleId);
		if(level < triggerevent.needlevel)
			return false;
		if(triggerevent.needquest != 0)
		{
			//TODO 检验有没有任务
			//检测是否完成过任务
		}
		if(triggerevent.needskill != 0)
		{
			//TODO 检验有没有技能
		}
		return true;
	}
	
	/**发送消息*/
	private void sendMsg(TriggerEvent event)
	{
		if(event.msgId == 0)
			return;
		MessageMgr.sendMsgNotify(roleId, event.msgId, null);
	}
	

	/**直接发奖励*/
	private void reward(TriggerEvent event)
	{
		if(event.rewardId == 0)
			return;
		RewardMgr.getInstance().distributeAllAward(roleId, event.rewardId, null, fire.log.enums.YYLoggerTuJingEnum.GENERAL, event.rewardId,PAddExpProc.OTHER,"trigger");
	}
	
	/**删除任务*/
	private void removeQuest(TriggerEvent event)
	{
		if(event.removequest == 0)
			return;
	}
	
	/**添加任务*/
	private void addQuest(TriggerEvent event)
	{
		if(event.addquest == 0)
			return;
		mkdb.Procedure.pexecuteWhileCommit(
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					TeamManager.getTeamByRoleId(roleId);//强行锁队伍
					new PAcceptMajorMission(roleId, event.addquest, true,true).call();
					return true;
				}
			} );
	}
	
}
