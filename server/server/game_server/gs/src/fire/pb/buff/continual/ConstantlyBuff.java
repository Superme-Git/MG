package fire.pb.buff.continual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import fire.pb.attr.EffectType;
import fire.pb.battle.DemoResult;
import fire.pb.battle.Fighter;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffPetImpl;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.IBuff;
import fire.pb.buff.Module;
import fire.pb.buff.RootBuff;
import fire.pb.buff.single.EPFixedInjure;
import fire.pb.skill.Result;
import fire.pb.skill.fight.FightSkill;
import fire.pb.timer.ScheduledFutureMap;
import fire.script.JavaScript;

/***/
public class ConstantlyBuff extends RootBuff
{
	protected static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("BATTLE");
	protected xbean.Buff buffBean;
	final protected ConstantlyBuffConfig buffConfig;
	protected int buffId;
	Map<Integer, JavaScript> effects ;
	
	public List<Integer> parseUsedAttrTypes()
	{
		List<Integer> usedattrtypea = new ArrayList<Integer>();
		List<Integer> usedattrtypeb = new ArrayList<Integer>();

		for (Map.Entry<Integer, JavaScript> entry : effects.entrySet())
			entry.getValue().parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		return usedattrtypea;
	}
	/**用于已经存在的Buff,和buffcopy时*/
	public ConstantlyBuff(xbean.Buff buffBean)
	{
		this.buffId = buffBean.getIndex();
		this.buffBean = buffBean;
		buffConfig = fire.pb.buff.Module.getInstance().getDefaultCBuffConfig(buffId);
		if(buffConfig.getRoundBuffEffectJavaScript() != null)
		{
			try {
				effects = fire.pb.util.Parser.parseFightJsEffects(buffConfig.getRoundBuffEffectJavaScript());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**新建一个Buff*/
	public ConstantlyBuff(ConstantlyBuffConfig buffConfig)
	{
		this.buffConfig = buffConfig;
		this.buffId = buffConfig.getBuffTypeId();
		initialBuffBean();
	}
	
	
	protected void initialBuffBean()
	{
		buffBean = xbean.Pod.newBuff();
		
		buffBean.setIndex(buffConfig.getBuffTypeId());
		buffBean.setValue(buffConfig.getInitValue());
		buffBean.setRound(buffConfig.getInitRound());
		buffBean.setRemaintime(buffConfig.getInitTime());
		for(int effectTypeId : buffConfig.getEffects().keySet())
		{//初始化表中的效果值
			buffBean.getEffects().put(effectTypeId, buffConfig.getEffects().get(effectTypeId));
		}
		if(buffConfig.getRoundBuffEffectJavaScript() != null)
		{
			try {
				effects = fire.pb.util.Parser.parseFightJsEffects(buffConfig.getRoundBuffEffectJavaScript());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	

	/**附加buff，由BuffAgent调用add类方法，不要直接使用*/
	public Result impact(BuffAgent buffAgent)
	{
		List<Integer> rmids = new ArrayList<Integer>(); 
		for(Map.Entry<Integer, Float> entry :buffBean.getEffects().entrySet())
		{
			if(entry.getValue() == 0)
				rmids.add(entry.getKey());
		}
		for(Integer effectid :rmids)
			buffBean.getEffects().remove(effectid);
		fire.pb.effect.Role erole = buffAgent.getERole();
		xbean.Buff oldbuff = buffAgent.addBuffBean(buffBean);
		if(oldbuff != null)
		{
			if(buffAgent instanceof BuffRoleImpl)
			{
				long rid = ((BuffRoleImpl)buffAgent).getRoleId();
				fire.pb.buff.Module.logger.error("角色"+rid+"的Buff:" + oldbuff.getIndex() + "移除的时候返回不为null");
			}
			else if(buffAgent instanceof BuffPetImpl)
			{
				long rid = ((BuffPetImpl)buffAgent).getRoleId();
				fire.pb.buff.Module.logger.error("角色"+rid+"的宠物Buff:" + oldbuff.getIndex() + "移除的时候返回为null");
			}
			erole.detachEffects(oldbuff.getEffects());
		}
		erole.attachEffects(buffBean.getEffects());
		//定时器启动
		buffAgent.startBuffTimer(this);		
		Result result = new Result(true);
		result.addAddedBuff(this);
		// 属性更新
		result.updateChangedAttrs(erole.updateAllFinalAttrs());
		
		return result;
	}
	
	
	/**执行方法*/
	public Result process(BuffAgent buffAgent)
	{
		return new Result(false);
	}
	
	/**默认closure*/
	public Result closure(BuffAgent buffAgent)
	{
		xbean.Buff xbuff = buffAgent.removeBuffBean(buffBean.getIndex());
		if(xbuff == null)
		{
			if(buffAgent instanceof BuffRoleImpl)
			{
				long rid = ((BuffRoleImpl)buffAgent).getRoleId();
				fire.pb.buff.Module.logger.error("角色"+rid+"的Buff:" + buffBean.getIndex() + "移除的时候返回为null");
			}
			else if(buffAgent instanceof BuffPetImpl)
			{
				long rid = ((BuffPetImpl)buffAgent).getRoleId();
				fire.pb.buff.Module.logger.error("角色"+rid+"的宠物Buff:" + buffBean.getIndex() + "移除的时候返回为null");
			}
		}
		fire.pb.effect.Role erole = buffAgent.getERole();
		erole.detachEffects(buffBean.getEffects());
		//定时器停止
		Long futureId = buffAgent.getAgent().getTimerfutures().get(buffBean.getIndex());
		if(futureId!=null)
		{
			ScheduledFuture<?> future = ScheduledFutureMap.getInstance().get(futureId);
			if(future!=null)
			{
				future.cancel(true);
				ScheduledFutureMap.getInstance().remove(futureId);
			}
			buffAgent.getAgent().getTimerfutures().remove(buffBean.getIndex());
		}
		Result result = new Result(true); 
		result.addDeletedBuff(this);
		result.setChangedAttrs(erole.updateAllFinalAttrs());
		return result;
	}
	
	/**结束时closure*/
	public Result closure(BuffAgent buffAgent,boolean timeout)
	{		
		return closure(buffAgent);
		
	}
	

	/**战斗中impact*/
	@Override
	public DemoResult impact(xbean.BattleInfo battleInfo,Fighter opfighter, Fighter aimfighter, FightSkill battleskill, Map<Integer, JavaScript> effects)
	{
		DemoResult demoResult = new DemoResult();
		demoResult.targetid = aimfighter.getFighterId();
		if(effects!=null&&effects.size() > 0)
		{
			for(Map.Entry<Integer, JavaScript> entry: effects.entrySet())
			{
				try {
					buffBean.getEffects().put(entry.getKey(), entry.getValue().eval(battleInfo.getEngine(),opfighter,aimfighter).floatValue());
				} catch (Exception e) {
					
					logger.error("buffid:"+buffId+"entry.getValue:"+entry.getValue().toString()+"entry.getkey()"+entry.getKey(), e);
					throw new RuntimeException(e);
				}
			}
		}
		else
		{
			if(this.effects!=null)
			{
				for(Map.Entry<Integer, JavaScript> entry: this.effects.entrySet())
				{
					try {
						buffBean.getEffects().put(entry.getKey(), entry.getValue().eval(battleInfo.getEngine(),opfighter,aimfighter).floatValue());
					} 
					catch (Exception e) {						
						logger.error("buffid:"+buffId+"entry.getValue:"+entry.getValue().toString()+"entry.getkey()"+entry.getKey(), e);
						throw new RuntimeException(e);
					}
				}
			}			
		}
		buffBean.setFighterkey(opfighter.getFighterBean().getFighterkey());
		Result result = aimfighter.getBuffAgent().addCBuff(this);
		fire.pb.buff.Module.updateDemoResultFromResult(demoResult, result,aimfighter);
		return demoResult;
	}

	

	/**战斗结束时结算*/
	public DemoResult onRoundEnd(xbean.BattleInfo battleInfo,Fighter fighter)
	{
		DemoResult demo = new DemoResult();
		demo.targetid = fighter.getFighterId();
		boolean changed = false;
		//如果有回合末结算的一次性BUF
		if((buffConfig.getRoundBuffId() > 0 /*||(buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_HIDDEN_FIRST
				||buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_HIDDEN_SECOND
				||buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_ROLE_HIDDEN && buffBean.getRound() == 1)*/)&& 
				(!(fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_DEATH)|| fighter.getBuffAgent().existBuff(BuffConstant.CONTINUAL_GHOST))
				||getId()==BuffConstant.CONTINUAL_GHOST||getId()==BuffConstant.CONTINUAL_ELEMENT_AWKEN_RELIVE))
		{
			
			Map<Integer,JavaScript> effects = new HashMap<Integer, JavaScript>();
			for (Map.Entry<Integer, Float> entry : buffBean.getEffects().entrySet())
			{
				JavaScript script = new JavaScript(entry.getValue().toString());
				effects.put(entry.getKey(), script);
			}
			if(effects.size() < 1 && buffConfig.getRoundBuffEffectJavaScript() != null)
			{
				try {
					effects = fire.pb.util.Parser.parseFightJsEffects(buffConfig.getRoundBuffEffectJavaScript());
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
			IBuff instantbuff = fire.pb.buff.Module.getInstance().createBuff(buffConfig.getRoundBuffId());
			if(instantbuff instanceof EPFixedInjure &&(buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_HIDDEN_FIRST
							||buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_HIDDEN_SECOND
							||buffConfig.getBuffTypeId() == BuffConstant.CONTINUAL_ROLE_HIDDEN)
							/*&& buffBean.getRound() == 1*/)
			{//隐身buff
				//String ep_value = Math.random()>0.5f?"1":"2";
				effects.put(EffectType.EFFECT_POINT_ABL, new JavaScript("1"));
			}
			if (fighter.getBuffAgent().canAddBuff(instantbuff.getId()))
			{
				DemoResult tmpdemo = instantbuff.impact(battleInfo, fighter, fighter, null, effects);
				if (tmpdemo != null)
				{
					demo = tmpdemo;
					changed = true;
				}
			}
			
		}
		//扣回合数
		Result result = roundCountdown(fighter.getBuffAgent());
		if(result.isSuccess())
		{
			fire.pb.buff.Module.updateDemoResultFromResult(demo, result, fighter);
			changed = true;
		}
		if(changed)
			return demo;
		else
			return null;
	}

	protected Result roundCountdown(BuffAgent agent)
	{
		if(buffBean.getRound() <= 0)//永久buff
			return new Result(false);
		
		int count = buffBean.getRound();
		count--;
		buffBean.setRound(count);
		if(count == 0)//计数0-移除
			return agent.removeCBuff(buffId);
		else
		{
			Result result = new Result(true);
			result.addAddedBuff(this);
			return result;
		}
			
	}
	
	public ConstantlyBuff copy()
	{
		return Module.getInstance().createConstantlyBuff(buffBean.copy());
	}
	
	public ConstantlyBuff copyToData()
	{
		return Module.getInstance().createConstantlyBuff(buffBean.toData());
	}
	
	public ConstantlyBuffConfig getBuffConfig()
	{
		return buffConfig;
	}
	public xbean.Buff getBuffBean()
	{
		return buffBean;
	}

	/**剩余时间毫秒计时*/
	public long getTime(){return buffBean.getRemaintime();}
	/**剩余时间毫秒计时*/
	public void setTime(long time){buffBean.setRemaintime(time);}
	/**剩余回合*/
	public int getRound(){return buffBean.getRound();}
	/**剩余回合*/
	public void setRound(int count)	{buffBean.setRound(count);}
	/**值*/
	public long getAmount()	{return buffBean.getValue();}
	/**值*/
	public void setAmount(long amount){buffBean.setValue(amount);}
	/**效果*/
	public Map<Integer,Float> getEffects(){return buffBean.getEffects();}
	
	/**把默认的效果清除，只留下刚传入的效果*/
	public void setEffects(Map<Integer,Float> effects)
	{
		buffBean.getEffects().clear();
		buffBean.getEffects().putAll(effects);
	}

	@Override
	public int getId(){return buffId;}
	
	public void setImpactTime(long attachTime){buffBean.setImpacttime(attachTime);}
	
	public long getImpactTime(){return buffBean.getImpacttime();}
	
	/**取剩余时间*/
	public long getRemianingTime(BuffAgent agent)
	{
		Long futureId = agent.getAgent().getTimerfutures().get(buffId);
		if(futureId == null)
			return 0;
		ScheduledFuture<?> timerfuture = ScheduledFutureMap.getInstance().get(futureId);
		if(timerfuture == null)
			return 0;
		long remaintime = timerfuture.getDelay(java.util.concurrent.TimeUnit.MILLISECONDS);
		return remaintime;
	}
	
	@Override
	public String toString()
	{
		return buffBean.toString();
	}
	
	/**取剩余时间*/
	public long getRemainTime(){
		if(getTime() <= 0)
			return -1;
		if(getImpactTime() <= 0)
			return getTime();//下线停止计时buff在下线中
		return Math.max(0, getTime() - (System.currentTimeMillis() - getImpactTime())); 
	}	
	
	/**更新时间*/
	public long updateCBuffTiming(long totalTime){
		long currTime = buffBean.getRemaintime() + totalTime;
		buffBean.setRemaintime(currTime);
		return currTime;
	}
}
