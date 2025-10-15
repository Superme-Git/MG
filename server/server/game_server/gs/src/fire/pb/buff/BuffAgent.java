package fire.pb.buff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import fire.pb.attr.EffectType;
import fire.pb.attr.FightAttrType;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.continual.BuffTimer;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.buff.continual.PeriodBuff;
import fire.pb.main.Gs;
import fire.pb.skill.Result;
import fire.pb.timer.ScheduledFutureMap;

public abstract class BuffAgent
{
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("BATTLE");
	protected xbean.BuffAgent agent;
	protected int agentType;// 1=role;2=pet;3=monster
	protected boolean readonly;

	
	/**buff冲突检查*/
	public boolean canAddBuff(int buffId)
	{
		return checkCanAddBuff(buffId) == 0;
	}
	
	/**buff冲突检查*/
	public int checkCanAddBuff(int buffId)
	{
		int conflictId = checkBuffClash(buffId);
		if(conflictId != 0)
			return conflictId;
		
		return checkBuffValid(buffId);
	}
	
	/**冲突检查*/
	public int checkBuffClash(int buffOpId)
	{
		BuffClashs buffconflicts = Module.getInstance().getBuffClashs(buffOpId);
		if(buffconflicts == null)
			return 0;
		Set<Integer> existIds = getAllBuffBeans().keySet();
		int conflict = buffconflicts.checkBuffClash(existIds);
		if(conflict > 0)
			return conflict;
		int bufftype = 0;
		if(Module.isConstantlyBuff(buffOpId))
		{
			ConstantlyBuffConfig cfg = Module.getInstance().getDefaultCBuffConfig(buffOpId);
			if(cfg == null) return 0;
			bufftype = cfg.getBuffType();
		}
		else
		{
			return 0;
		}
		
		buffconflicts = Module.getInstance().getBuffClashs(bufftype);
		if(buffconflicts == null)
			return 0;
		conflict = buffconflicts.checkBuffClash(existIds);
		return conflict;
	}
	
	
	/**检查buff是否起效*/
	public boolean canEffectBuff(int buffId)
	{	
		return checkBuffValid(buffId) == 0;
	}
	
	/**检查buff是否起效*/
	public int checkBuffValid(int buffId)
	{
		BuffClashs buffconflicts = Module.getInstance().getBuffClashs(buffId);
		if(buffconflicts == null)
			return 0;
		Set<Integer> existIds = getAllBuffBeans().keySet();
		int invalid = buffconflicts.checkBuffInvalid(existIds);
		if(invalid > 0)
			return invalid;
		int bufftype = 0;
		if(Module.isConstantlyBuff(buffId))
		{
			ConstantlyBuffConfig cfg = Module.getInstance().getDefaultCBuffConfig(buffId);
			if(cfg == null) return 0;
			bufftype = cfg.getBuffType();
		}
		else
			return 0;
		
		buffconflicts = Module.getInstance().getBuffClashs(bufftype);
		if(buffconflicts == null)
			return 0;
		invalid = buffconflicts.checkBuffInvalid(existIds);
		return invalid;
	}
	/**获取被覆盖的buff*/
	public List<Integer> getOverridedBuffIds(int buffId)
	{
		BuffClashs buffClashs = Module.getInstance().getBuffClashs(buffId);
		if(buffClashs == null)
			return new ArrayList<Integer>();
		return buffClashs.getOverridedBuffIds(getAllBuffBeans().keySet());
	}

	/**检查是否有同类存在*/
	public boolean existBuff(int buffId)
	{
		ConstantlyBuffConfig conf = Module.getInstance().getDefaultCBuffConfig(buffId);
		if(conf == null || conf.getClassName() == null) return false;
		if(conf.getClassName().equals("fire.pb.buff.continual.NullBuff"))
			return existState(buffId);
		return existBuffBean(buffId);
	}
	
	/**判断是否存在某一类型的buff*/
	public boolean existBuffByType(int type)
	{
		if(type<1 || type >= 1000)
			return false;
		for(Integer buffId : agent.getBuffs().keySet())
		{
			ConstantlyBuffConfig cfg = Module.getInstance().getDefaultCBuffConfig(buffId);
			if(type >= 100 )//小类符合
			{
				if(type == cfg.getBuffType())
					return true;
			}
			else if(type >= 10)//中类符合
			{
				if(type == cfg.getBuffType() / 10)
					return true;
			}
			else if(type >= 1)//大类符合
			{
				if(type == cfg.getBuffType() / 100)
					return true;
			}
		}
		return false;
	}

	public abstract boolean existState(int buffTypeId);
	
	/**根据buffId查找agent中的buff*/
	public ConstantlyBuff getBuff(int buffId)
	{
		xbean.Buff buffbean = getBuffBean(buffId);
		if(buffbean == null)
			return null;
		ConstantlyBuff cbuff = Module.getInstance().createConstantlyBuff(buffbean);
		return cbuff;
	}
	
	/**获取xbean.buff*/
	public xbean.Buff getBuffBean(int buffId)
	{
		return agent.getBuffs().get(buffId);
	}
	
	public xbean.Buff addBuffBean(xbean.Buff buffbean)
	{
		return agent.getBuffs().put(buffbean.getIndex(), buffbean);
	}
	
	public xbean.Buff removeBuffBean(int buffId)
	{
		return agent.getBuffs().remove(buffId);
	}
	
	public boolean existBuffBean(int buffId)
	{
		return agent.getBuffs().containsKey(buffId);
	}
	
	/**获得所有BuffBeans */
	public Map<Integer,xbean.Buff> getAllBuffBeans()
	{
		Map<Integer,xbean.Buff> buffbeans = new HashMap<Integer, xbean.Buff>();
		buffbeans.putAll(agent.getBuffs());
		return buffbeans;
	}
	/**获取回合结束时结算的buff*/
	public List<ConstantlyBuff> getRoundBuffs()
	{
		List<ConstantlyBuff> buffs = new ArrayList<ConstantlyBuff>();
		for(Map.Entry<Integer, xbean.Buff> buffentry : getAllBuffBeans().entrySet())
		{//必须要有回合计数
			if(buffentry.getValue().getRound()>0)
			{
				ConstantlyBuff cbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffentry.getValue());
				buffs.add(cbuff);
			}
			else
			{
				BuffConfig cfg = Module.getInstance().getDefaultCBuffConfig(buffentry.getKey());
				if(cfg == null)
					continue;
				//如果是RoundBuff则无视回合数
				try
				{
					if(fire.pb.buff.Module.ROUND_BUFF_CLASS.isAssignableFrom(Class.forName(cfg.getClassName())))
					{
						ConstantlyBuff cbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffentry.getValue());
						buffs.add(cbuff);
					}
				} catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		return buffs;
	}
	
	public Result countDownWhileRoundEnd(List<ConstantlyBuff> buffs)
	{
		Result result = new Result(true);
		
		for(ConstantlyBuff buff : buffs)
		{
			buff.setRound(buff.getRound() - 1);
			if(buff.getRound() <= 0)
				result.updateResult(removeCBuff(buff.getId()));
			else
				result.addAddedBuff(buff);
		}
		
		return result;
	}
	
	
	/**添加持续性Buff，使用Buff表中数据构造Buff*/
	public Result addCBuff(int buffTypeId)
	{
		ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffTypeId);
		if(buff == null )
		{
			logger.error("Buff(ID: " + buffTypeId +") is not exist.");
			return new Result(false);
		}
		return addCBuff(buff);
	}
	/**使用传入参数添加buff*/
	public Result addCBuff(int buffId , int round , long time)
	{
		ConstantlyBuff cbuff = Module.getInstance().createConstantlyBuff(buffId);
		if(cbuff == null )
		{
			logger.error("Buff(ID: " + buffId +") is not exist.");
			return new Result(false);
		}
		cbuff.setRound(round);
		cbuff.setTime(time);
		return addCBuff(cbuff);
	}
	
	public Result addCBuff(ConstantlyBuff buff)
	{	//检查能不能添加
/*		if((buff.getId()==509127||buff.getId()==509123)&&(
				buff.getEffects().containsKey(FightAttrType.PHYSIC_DAMGE_PIERCE_RATE+1) &&
				buff.getEffects().get(FightAttrType.PHYSIC_DAMGE_PIERCE_RATE+1) > 486))
		{
			logger.error(Gs.ThrowableLog());
		}*/
		if(!canAddBuff(buff.getId()))
			return new Result(false);
		Result result  = new Result(true);
		if(existBuff(buff.getId()))
		{
			//叠加规则检查
			result = processOverrideSelf(buff);
			if(!result.isSuccess())
				return result;
		}
		//覆盖检查
		List<Integer> rmbuffIds = getOverridedBuffIds(buff.getId());
		
		for(int rmbuffId : rmbuffIds)
		{
			result.updateResult(removeCBuff(rmbuffId));
		}
		result.updateResult(buff.impact(this));
		if(result.isSuccess())
			addSceneState(buff.getBuffConfig());
		return result;
	}
	
	/**覆盖处理*/
	public Result processOverrideSelf(ConstantlyBuff newbuff)
	{	
		if(newbuff.getBuffConfig().getOverrideSelfType() == ConstantlyBuffConfig.OVERRIDE_TYPE_ALL)
		{
			return removeCBuff(newbuff.getId());
		}
		else if(newbuff.getBuffConfig().getOverrideSelfType() == ConstantlyBuffConfig.OVERRIDE_TYPE_ATTR_ADD && newbuff.getBuffConfig().getOverrideAttrs() != null)
		{
			for(int attrid: newbuff.getBuffConfig().getOverrideAttrs())
			{
				xbean.Buff existedBuff =  getBuffBean(newbuff.getId());
				Float existedAttr = existedBuff.getEffects().get(attrid);
				if(existedAttr == null)
					existedAttr = 0f;
				Float newAttr = newbuff.getEffects().get(attrid);
				if(newAttr == null)
					newAttr = 0f;
				newAttr = existedAttr + newAttr;
				if(newAttr != 0f)
					newbuff.getEffects().put(attrid, newAttr);
			}
			return  removeCBuff(newbuff.getId());
		}
		else if(newbuff.getBuffConfig().getOverrideSelfType() == ConstantlyBuffConfig.OVERRIDE_TYPE_ATTR_HIGHER)
		{
			for(int attrid: newbuff.getBuffConfig().getOverrideAttrs())
			{
				xbean.Buff existedBuff =  getBuffBean(newbuff.getId());
				Float existedAttr = existedBuff.getEffects().get(attrid);
				if(existedAttr == null) existedAttr = 0f;
				Float newAttr = newbuff.getEffects().get(attrid);
				if(newAttr == null) newAttr = 0f;
				if(Math.abs(existedAttr) > Math.abs(newAttr))
					newbuff.getEffects().put(attrid, existedAttr);
			}
			return removeCBuff(newbuff.getId());
		}
		return new Result(false);
		
	}
	
	public abstract void startBuffTimer(ConstantlyBuff buff);
	
	/**添加持续性Buff，使用用Buff表中数据构造Buff*/
	public boolean addCBuffWithSP(int buffTypeId)
	{	
		ConstantlyBuff buff = fire.pb.buff.Module.getInstance().createConstantlyBuff(buffTypeId);
		if(buff == null )
		{
			logger.error("Buff(ID: " + buffTypeId +") is not exist.");
			return false;
		}
		return addCBuffWithSP(buff);
	}

	/**添加持续性Buff，直接传入持续性Buff*/
	public boolean addCBuffWithSP(ConstantlyBuff buff)
	{
		Result result = addCBuff(buff);
		if(result == null || !result.isSuccess())
			return false;
		psendSBuffChangeResult(result);
		return true;
	}
	
	/**用buffID去除指定战斗者所上的持续性Buff*/
	public Result removeFighterCBuff(int buffId,int nfighterid)
	{
		if(readonly)
			return new Result(false);
		
		if (existBuff(buffId) && getBuffBean(buffId).getFighterkey() == nfighterid)
		{			
			ConstantlyBuff buff = getBuff(buffId);
			Result result = buff.closure(this);
			if(result.isSuccess())
				removeSceneState(buff.getBuffConfig());
			return result;
		}
		else
			return new Result(false);
	}	
	
	/**用buffID去除一个持续性Buff*/
	public Result removeCBuff(int buffId)
	{
		if(readonly)
			return new Result(false);
		
		if (existBuff(buffId))
		{
			//TODO 验证是否是不可叠加的buff类型
			ConstantlyBuff buff = getBuff(buffId);
			Result result = buff.closure(this);
			if(result.isSuccess())
				removeSceneState(buff.getBuffConfig());
			return result;
		}
		else
			return new Result(false);
	}
	
	/**用buffID去除一个持续性Buff*/
	public Result removeTimeoutCBuff(int buffId)
	{
		if(readonly)
			return new Result(false);
		if (existBuff(buffId))
		{
			ConstantlyBuff buff = getBuff(buffId);
			Result result = buff.closure(this,true);
			if(result.isSuccess())
				removeSceneState(buff.getBuffConfig());
			return result;
		}
		else
			return new Result(false);
	}

	
	/**去除指定持续性Buff的部分效果*/
	public Result removeCBuff(int buffId, Map<Integer,Float> effects)
	{
		if(readonly)
			return new Result(false);
		if (existBuff(buffId))
		{
			ConstantlyBuff buff = getBuff(buffId);
			//计算叠加后的效果
			for(Entry<Integer,Float> effect:effects.entrySet())
			{
				Float oldvalue = buff.getEffects().get(effect.getKey());
				if(oldvalue == null) continue;
				float newvalue = oldvalue + effect.getValue();
				if(newvalue < 0)
				{
					buff.getEffects().remove(effect.getKey());
					continue;
				}
			}
			if(buff.getEffects().size() == 0)
				return buff.closure(this);
			else
			{
				Result result = buff.impact(this);
				return result;
			}
			
		} else
			return new Result(false);

	}
	
	/**用buffID去除一个持续性Buff*/
	public boolean removeTimeoutCBuffWithSP(int buffId)
	{
		Result result = removeTimeoutCBuff(buffId);
		if(result == null || !result.isSuccess())
			return false;
		psendSBuffChangeResult(result);
		return true;
	}
	
	/**用buffID去除一个持续性Buff*/
	public boolean removeCBuffWithSP(int buffId)
	{
		Result result = removeCBuff(buffId);
		if(result == null || !result.isSuccess())
			return false;
		psendSBuffChangeResult(result);
		return true;
	}
		
	public boolean removeCBuffWithSP(int buffId, Map<Integer,Float> effects)
	{
		Result result = removeCBuff(buffId, effects);
		if(result == null || !result.isSuccess())
			return false;
		psendSBuffChangeResult(result);
		return true;
	}
	
	public boolean removeCBuffWhileBattleEnd(int buffId)
	{
		agent.getBattleendclear().put(buffId,xbean.Buff.BATTLE_END_IMPACT);
		return true;
	}
	
	/**离开战斗时清除战斗内存在buff*/
	public Result removeBuffsWhileBattleEnd()
	{
		if(readonly)
			return new Result(false);
		Result result = new Result(true);
		List<Integer> rmBuffIds = new ArrayList<Integer>();
		for(int buffId : getAllBuffBeans().keySet())
		{
			try{	
				ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
				if(cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_OUT_BATTLE
						|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_DEATH
						|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_IN_BATTLE_HURT
						|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL
						|| cbuffconf.getClearType() == BuffConstant.CLEAR_TYPE_USE_SKILL_ATTACK)
					rmBuffIds.add(buffId);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		for(int rmBuffId : rmBuffIds)
		{
			try{
				result.updateResult(removeCBuff(rmBuffId));
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return result;
	}
	
	
	/**离开战斗时清除战斗内存在buff*/
	public boolean removeBuffsWhileBattleEndWithSP()
	{
		try{
			if (readonly)
				return false;
			Result result = removeBuffsWhileBattleEnd();
			if (result.isSuccess())
			{
				psendSBuffChangeResult(result);
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**获取buff的剩余时间*/
	public long getCBuffTime(int buffId)
	{
		if(readonly)
			return -1;
		Long futureId = agent.getTimerfutures().get(buffId);
		if(futureId == null)
			return -1;
		ScheduledFuture<?> timerfuture = ScheduledFutureMap.getInstance().get(futureId);
		if(timerfuture == null)
			return -1;
		long remaintime = timerfuture.getDelay(java.util.concurrent.TimeUnit.MILLISECONDS);
		return remaintime;
	}

	/**刷新buff时间*/
	public long updateCBuffTimingWithSP(int buffId, long addTime)
	{
		if(readonly)
			return -1;
		ConstantlyBuff cbuff = this.getBuff(buffId);
		if(cbuff == null)
			return -1;
		Long futureId = agent.getTimerfutures().get(buffId);
		if(futureId == null)
			return -1;
		ScheduledFuture<?> timerfuture = ScheduledFutureMap.getInstance().get(futureId);
		if(timerfuture == null)
			return -1;
		long remaintime = timerfuture.getDelay(java.util.concurrent.TimeUnit.MILLISECONDS);
		long newtime = remaintime+addTime;
		if(newtime < 0) newtime = 0;
		cbuff.setImpactTime(System.currentTimeMillis());;
		cbuff.getBuffBean().setRemaintime(newtime);
		BuffTimer newtimer = getBuffTimer(buffId,newtime);
		if(newtimer.schedule())
		{
			timerfuture.cancel(true);
			ScheduledFutureMap.getInstance().remove(futureId);
			agent.getTimerfutures().put(buffId,ScheduledFutureMap.getInstance().insert(newtimer.getFuture()));
			Result result = new Result(true);
			result.addAddedBuff(cbuff);
			psendSBuffChangeResult(result);
		}
		else
		{
			removeCBuffWithSP(buffId);
		}
		return newtime;
	}
	
	/**处理buff的调用*/
	public Result processCBuff(int buffId)
	{
		ConstantlyBuff buff = this.getBuff(buffId);
		if(buff == null)
			return new Result(false);
		return buff.process(this);
	}
	/**处理buff的调用 */
	public boolean processCBuffWithSP(int buffId)
	{
		ConstantlyBuff buff = this.getBuff(buffId);
		Result result =  buff.process(this);
		if(result == null || !result.isSuccess())
			return false;
		psendSBuffChangeResult(result);
		return true;
	}
	/**PeriodBuff的周期性处理延后到战斗结束执行*/
	public boolean processCBuffWhileBattleEnd(int buffId)
	{
		agent.getBattleendclear().put(buffId,xbean.Buff.BATTLE_END_PROCESS);
		return true;
	}
	
	public List<String> getBuffTipArgs(int buffId)
	{
		List<String> args = new ArrayList<String>();
		return args;
	}
	
	public static boolean processBuffTime(ConstantlyBuff cbuff,long now)
	{
		if(cbuff.getTime() <= 0)
			return true;
		
		if(cbuff.getImpactTime() != 0)
		{
			if(cbuff instanceof PeriodBuff)
			{
				PeriodBuff pbuff = (PeriodBuff)cbuff;
				if(pbuff.getPeriodCount() > 0)
				{
					long resumeTime = pbuff.getPeriodCount() *pbuff.getPeriod() + pbuff.getInitialDelay() -  (now - pbuff.getImpactTime());
					if (resumeTime > 0)
					{
						pbuff.setImpactTime(0);
						pbuff.setInitialDelay(resumeTime % pbuff.getPeriod());
						pbuff.setPeriodCount((int)(resumeTime / pbuff.getPeriod()) + 1);
					} else
						return false;
				}
				else
				{
					long pastTime = now - pbuff.getImpactTime();
					if(pastTime <= pbuff.getInitialDelay())
					{
						pbuff.setInitialDelay(pbuff.getInitialDelay() - pastTime);
					}
					else
					{
						pbuff.setInitialDelay(pbuff.getPeriod() - (pastTime - pbuff.getInitialDelay()) % pbuff.getPeriod());
					}
					pbuff.setImpactTime(0);
				}
			}
			else
			{
				long resumeTime = cbuff.getTime() - (now - cbuff.getImpactTime());
				if (resumeTime > 0)
				{
					cbuff.setImpactTime(0);
					cbuff.setTime(resumeTime);
				}
				else
					return false;
			}
		}
		
		return true;
	}
	
	public abstract BuffTimer getBuffTimer(int buffId, long time);
	public abstract BuffTimer getBuffTimer(int buffId, long intinalDelay, long period);
	public abstract BuffTimer getBuffTimer(int buffId, long initialDelay, long period,int count);
	public abstract boolean psendSBuffChangeResult(Result result);
	public abstract void addSceneState(ConstantlyBuffConfig buffcfg);
	public abstract void removeSceneState(ConstantlyBuffConfig buffcfg);
	protected abstract SBuffChangeResult getSBuffChangeResult(Result result);
	public xbean.BuffAgent getAgent()
	{
		return agent;
	}

	public void setAgent(xbean.BuffAgent agent)
	{
		this.agent = agent;
	}

	public boolean isReadonly()
	{
		return readonly;
	}

	public void setReadonly(boolean readonly)
	{
		this.readonly = readonly;
	}

	public int getAgentType()
	{
		return agentType;
	}

	public void setAgentType(int agentType)
	{
		this.agentType = agentType;
	}

	public abstract fire.pb.effect.Role getERole();
	
	public boolean examBuffsEffects()
	{
		if(!(this instanceof BuffRoleImpl))
			return true;
			
		Map<Integer,Float> buffeffects = new HashMap<Integer, Float>(); 
		for(xbean.Buff xbuff : getAllBuffBeans().values())
		{
			for(Map.Entry<Integer,Float> entry : xbuff.getEffects().entrySet())
			{
				if(!fire.pb.effect.Module.isFightAttr((entry.getKey() / 10)*10))
					continue;
				Float value = buffeffects.get(entry.getKey());
				if(value == null)
					buffeffects.put(entry.getKey(), entry.getValue());
				else
					buffeffects.put(entry.getKey(), value + entry.getValue());
			}
		}
		Map<Integer, Float> changedattrs = getERole().correctEffects(buffeffects);
		if(changedattrs != null && !changedattrs.isEmpty())
		{
			Map<Integer,Float> clientAttrs = fire.pb.effect.Module.getClientAttrs(changedattrs);
			if(clientAttrs.size() != 0)
				mkdb.Procedure.psendWhileCommit(((BuffRoleImpl)this).getRoleId(),new SRefreshRoleData((HashMap<Integer, Float>)clientAttrs));
			return false;
		}
		return true;
	}
}
