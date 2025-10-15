package fire.pb.buff;

import fire.pb.buff.continual.BuffTimer;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.Role;
import fire.pb.skill.Result;

public class BuffMonsterImpl extends BuffAgent
{

	xbean.Monster monsterBean;
	
	public BuffMonsterImpl(xbean.Monster monsterBean)
	{
		this(monsterBean,false);
	}
	
	public BuffMonsterImpl(xbean.Monster monsterBean,boolean readonly)
	{
		this.readonly = readonly;
		this.monsterBean = monsterBean;
		this.agent = monsterBean.getBuffagent();
		this.agentType = BuffConstant.BuffAgentType.MONSTER;
	}
	
	@Override
	public boolean existState(int buffTypeId)
	{
		return false;
	}

	@Override
	public SBuffChangeResult getSBuffChangeResult(Result result)
	{
		SBuffChangeResult sBuffChangeResult = new SBuffChangeResult();
		sBuffChangeResult.agenttype = agentType;
		sBuffChangeResult.id = monsterBean.getMonsterid();
		sBuffChangeResult.petid = -1;
		sBuffChangeResult.addedbuffs.putAll(result.getAddedBuffMap());
		sBuffChangeResult.deletedbuffs.addAll(result.getDeletedBuffs());
		return sBuffChangeResult;
	}

	@Override
	public Role getERole()
	{
		return new fire.pb.effect.MonsterImpl(monsterBean);
	}

	@Override
	public BuffTimer getBuffTimer(int buffId, long time)
	{
		return null;
	}
	@Override
	public BuffTimer getBuffTimer(int buffId, long initialDelay, long time)
	{
		return null;
	}
	@Override
	public boolean psendSBuffChangeResult(Result result)
	{
		return false;
	}

	
	@Override
	public boolean removeBuffsWhileBattleEndWithSP()
	{
		return false;
	}
	
	@Override
	public Result removeBuffsWhileBattleEnd()
	{
		return new Result(false);
	}

	@Override
	public void startBuffTimer(ConstantlyBuff buff){}

	@Override
	public BuffTimer getBuffTimer(int buffId, long initialDelay, long period, int count)
	{
		return null;
	}

	@Override
	public void addSceneState(ConstantlyBuffConfig buffcfg){}

	@Override
	public void removeSceneState(ConstantlyBuffConfig buffcfg){}
}
