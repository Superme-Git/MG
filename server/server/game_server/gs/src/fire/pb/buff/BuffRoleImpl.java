package fire.pb.buff;



import java.util.HashMap;
import java.util.Map;

import fire.msp.role.GAddSceneState;
import fire.msp.role.GRemoveSceneState;
import fire.pb.GsClient;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.continual.BuffTimer;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.continual.PeriodBuff;
import fire.pb.buff.continual.PeriodBuffTimer;
import fire.pb.effect.Role;
import fire.pb.map.RoleManager;
import fire.pb.skill.Result;
import fire.pb.timer.ScheduledFutureMap;

/***/

public class BuffRoleImpl extends BuffAgent
{
	private long roleId;
	private xbean.BuffRole buffRoleBean;
	private xbean.StoredBuffRole storedBuffRole;

	
	public BuffRoleImpl(long roleId)
	{
		this(roleId,false);
	}
	
	
	public BuffRoleImpl(long roleId,boolean readonly)
	{
		this.readonly = readonly;
		this.roleId = roleId;
		this.agentType = BuffConstant.BuffAgentType.Role;
		if(readonly)
		{
			buffRoleBean = xtable.Buffroles.select(roleId);
			storedBuffRole = xtable.Buffrolestodisk.select(roleId);
		}
		else
		{
			buffRoleBean = xtable.Buffroles.get(roleId);
			storedBuffRole = xtable.Buffrolestodisk.get(roleId);
		}
		if(buffRoleBean == null)
		{
			if(readonly)
			{
				buffRoleBean = xbean.Pod.newBuffRoleData();
			}
			else
			{
				buffRoleBean = xbean.Pod.newBuffRole();
				xtable.Buffroles.insert(roleId,buffRoleBean);
			}
		}
		
		if(storedBuffRole == null)
		{
			if(readonly)
			{
				storedBuffRole = xbean.Pod.newStoredBuffRole();
			}
			else
			{
				storedBuffRole = xbean.Pod.newStoredBuffRole();
				xtable.Buffrolestodisk.insert(roleId,storedBuffRole);
			}
		}
		
		this.agent = buffRoleBean.getBuffagent();
	}
	
	/**获取xbean.buff*/
	@Override
	public xbean.Buff getBuffBean(int buffId)
	{
		ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
		if(cbuffconf.isStoreToDisk())
			return storedBuffRole.getBuffs().get(buffId);
		else
			return agent.getBuffs().get(buffId);
	}
	/**添加某个xbean.buff*/
	@Override
	public xbean.Buff addBuffBean(xbean.Buff buffbean)
	{
		ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffbean.getIndex());
		if(cbuffconf.isStoreToDisk())
			return storedBuffRole.getBuffs().put(buffbean.getIndex(), buffbean);
		else
			return agent.getBuffs().put(buffbean.getIndex(), buffbean);
	}
	
	/**移除某个xbean.buff*/
	@Override
	public xbean.Buff removeBuffBean(int buffId)
	{
		ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
		if(cbuffconf.isStoreToDisk())
			return storedBuffRole.getBuffs().remove(buffId);
		else
			return agent.getBuffs().remove(buffId);
	}
	
	/**判断是否存在 */
	@Override
	public boolean existBuffBean(int buffId)
	{
		ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
		if(cbuffconf.isStoreToDisk())
			return storedBuffRole.getBuffs().containsKey(buffId);
		else
			return agent.getBuffs().containsKey(buffId);
		
	}
	
	/***/
	@Override
	public Map<Integer,xbean.Buff> getAllBuffBeans()
	{
		Map<Integer,xbean.Buff> buffbeans = new HashMap<Integer, xbean.Buff>();
		buffbeans.putAll(agent.getBuffs());
		buffbeans.putAll(storedBuffRole.getBuffs());
		return buffbeans;
	}
	
	@Override
	public BuffTimer getBuffTimer(int buffId, long time)
	{
		return new BuffTimer(roleId,buffId,time);
	}
	/**获取PeriodBuffTimer，没有次数执行限制*/
	@Override
	public BuffTimer getBuffTimer(int buffId, long initialDelay, long period)
	{
		return getBuffTimer(buffId,initialDelay,period,-1);
	}
	
	/**获取PeriodBuffTimer，次数执行限制*/
	@Override
	public BuffTimer getBuffTimer(int buffId, long initialDelay, long period,int count)
	{
		return new PeriodBuffTimer(roleId, buffId,initialDelay,period,-1);
	}
	@Override
	public boolean existState(int buffTypeId)
	{
		return Module.existState(roleId, buffTypeId);
	}
	
	@Override
	public boolean psendSBuffChangeResult(Result result)
	{
		if(result == null)
			return false;
		Map<Integer,Float> clientAttrs = fire.pb.effect.Module.getClientAttrs(result.getChangedAttrs());
		if(clientAttrs.size() != 0)
			mkdb.Procedure.psendWhileCommit(roleId,new SRefreshRoleData((HashMap<Integer, Float>)clientAttrs));
		SBuffChangeResult sBuffChangeResult = getSBuffChangeResult(result);
		if(sBuffChangeResult.addedbuffs.size() == 0 && sBuffChangeResult.deletedbuffs.size() == 0)
			return false;
		mkdb.Procedure.psendWhileCommit(roleId, sBuffChangeResult);
		return true;
	}
	
	public int getAllSceneState()
	{
		int scenestate = 0;
		for(Integer buffId :getAllBuffBeans().keySet())
		{
			ConstantlyBuffConfig buffcfg = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
			if(buffcfg.getSceneId() > 0)
				scenestate = scenestate | buffcfg.getSceneId();
		}
		return scenestate;
	}
	
	public void psendCBuffWhileOnline()
	{
		Result result = new Result(true);
		for(xbean.Buff xbuff :getAllBuffBeans().values()) {
			result.addAddedBuff(xbuff);
		}
		psendSBuffChangeResult(result);
	}
	
	@Override
	public SBuffChangeResult getSBuffChangeResult(Result result)
	{
		SBuffChangeResult sBuffChangeResult = new SBuffChangeResult();
		sBuffChangeResult.agenttype = agentType;
		sBuffChangeResult.id = roleId;
		sBuffChangeResult.petid = -1;
		
		for(Integer buffId : result.getDeletedBuffs())
		{
			ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
			if(!cbuffconf.isSendClient())
				continue;
			
			sBuffChangeResult.deletedbuffs.add(buffId);
		}
		
		for(Map.Entry<Integer, fire.pb.buff.Buff> entry : result.getAddedBuffMap().entrySet())
		{
			ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(entry.getKey());
			if(!cbuffconf.isSendClient())
				continue;
			if(!cbuffconf.isShowTime())
				entry.getValue().time = 0;
			sBuffChangeResult.addedbuffs.put(entry.getKey(),entry.getValue());
		}
		return sBuffChangeResult;
	}

	public xbean.StoredBuffRole getStoredBuffRole()
	{
		return storedBuffRole;
	}
	

	public long getRoleId()
	{
		return roleId;
	}


	public void setRoleId(long roleId)
	{
		this.roleId = roleId;
	}


	public xbean.BuffRole getBuffRoleBean()
	{
		return buffRoleBean;
	}



	@Override
	public Role getERole()
	{
		return new fire.pb.effect.RoleImpl(roleId,readonly);
	}

	/**删除所有只能在战斗中存在的buff和战斗结束时应该被清除的buff，离开战斗时使用*/
	@Override
	public Result removeBuffsWhileBattleEnd()
	{
		Result result = super.removeBuffsWhileBattleEnd();
		if(result.isSuccess())
		{
			//执行战斗中到时清除或处理的，延时到战斗结束执行的buff
			for(Map.Entry<Integer,Integer> entry: agent.getBattleendclear().entrySet())
			{
				switch(entry.getValue())
				{
					case xbean.Buff.BATTLE_END_IMPACT:
						result.updateResult(removeTimeoutCBuff(entry.getKey()));
						break;
					case xbean.Buff.BATTLE_END_PROCESS:
						result.updateResult(processCBuff(entry.getKey()));
						break;
				}
			}
			agent.getBattleendclear().clear();
		}
		return result;
	}


	@Override
	public void startBuffTimer(ConstantlyBuff buff)
	{
		if(buff.getTime() > 0)
		{
			Long futureId = getAgent().getTimerfutures().get(buff.getId());
			if(futureId!=null)
			{
				java.util.concurrent.ScheduledFuture<?> future = ScheduledFutureMap.getInstance().get(futureId);
				if(future!=null)
					future.cancel(true);
				fire.pb.buff.Module.logger.error("Buff"+buff.getId()+"startTimer时，已经有了旧的。");
			}
			BuffTimer newtimer = null;
			if(buff instanceof PeriodBuff)
				newtimer = getBuffTimer(buff.getId(), ((PeriodBuff)buff).getInitialDelay(), ((PeriodBuff)buff).getPeriod(), ((PeriodBuff)buff).getPeriodCount());
			else
				newtimer = getBuffTimer(buff.getId(), buff.getTime());
			if(newtimer == null)
			{
				logger.error("!!!Buff Timer is null.");
				throw new NullPointerException("没有buff timer!!");
			}
			if(newtimer.schedule())
			{
				long now = System.currentTimeMillis();
				buff.setImpactTime(now);
				long futurekey = ScheduledFutureMap.getInstance().insert(newtimer.getFuture());
				getAgent().getTimerfutures().put(buff.getId(),futurekey);
			}
		}
	}
	
	/***检查buff冲突，是否能添加该buff或执行动作*/
	@Override
	public int checkBuffClash(int buffOpId)
	{
		int clashId = super.checkBuffClash(buffOpId);
		if(clashId > 0)
		{
			sendBuffClashMsg(buffOpId, clashId);
			return clashId;
		}
		
		//检查非实体buff（state）的冲突
		BuffClashs buffclashs = Module.getInstance().getBuffClashs(buffOpId);
		if(buffclashs == null)
			return 0;
		clashId = buffclashs.checkStateClash(roleId);
		if(clashId > 0)
		{
			sendBuffClashMsg(buffclashs, clashId);
			return clashId;
		}
		
		//检查跟地图的冲突
		fire.pb.map.Role maprole = RoleManager.getInstance().getRoleByID(roleId);
		if(maprole == null)
			return clashId;
		if(buffclashs.checkMapClash((int)maprole.getScene()))
		{
			sendBuffClashMsg(buffclashs, (int)maprole.getScene());
			return (int)maprole.getScene();
		}
		
		return 0;
		
	}
	
	/**发送默认的冲突消息*/
	public void sendBuffClashMsg(int buffOpId,int clashId)
	{
		if(clashId == 0)
			return;
		sendBuffClashMsg(Module.getInstance().getBuffClashs(buffOpId), clashId);
	}
	/**发送默认的冲突消息*/
	public void sendBuffClashMsg(BuffClashs buffclashs ,int clashId)
	{
		if(buffclashs == null)
			return;
		Integer msgId = buffclashs.getClashMsgIds().get(clashId);
		if(msgId != null)
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, msgId, null);//这里直接发送，防止外面有回滚等
		else if(buffclashs.getDefaultClashMsgId() != 0)
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, buffclashs.getDefaultClashMsgId(), null);//这里直接发送，防止外面有回滚等
	}

	@Override
	public void addSceneState(ConstantlyBuffConfig buffcfg)
	{
		if(buffcfg.getSceneId() > 0)//TODO 还需要验证场景ID是 2的整数次幂
			GsClient.pSendWhileCommit(new GAddSceneState(roleId,buffcfg.getSceneId()));
	}


	@Override
	public void removeSceneState(ConstantlyBuffConfig buffcfg)
	{
		if(buffcfg.getSceneId() > 0)//TODO 还需要验证场景ID是 2的整数次幂
			GsClient.pSendWhileCommit(new GRemoveSceneState(roleId,buffcfg.getSceneId()));			
	}
	
}
