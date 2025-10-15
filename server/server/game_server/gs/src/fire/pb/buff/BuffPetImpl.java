package fire.pb.buff;

import java.util.HashMap;
import java.util.Map;

import fire.pb.attr.SRefreshPetData;
import fire.pb.buff.continual.BuffTimer;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.effect.Role;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.Result;

/**只有人物身上背包栏里的宠物才能获取此buff，其他时候（）*/
public class BuffPetImpl extends BuffAgent
{
	private long roleId;

	private int petKey;
	private xbean.PetInfo petInfo;
	
	/**非只读模式，此种方式构造时，只能操作人身上宠物栏里的宠物buff*/
	public BuffPetImpl(long roleId,int petKey)
	{
		this(roleId,petKey,false);
	}
	
	/**只读或非只读模式，此种方式构造时，只能操作人身上宠物栏里的宠物buff*/
	public BuffPetImpl(long roleId,int petKey, boolean readonly)
	{
		this.readonly = readonly;
		this.roleId = roleId;
		this.petKey = petKey;
		this.agentType = BuffConstant.BuffAgentType.PET;
		PetColumn petColumn = new PetColumn(roleId,PetColumnTypes.PET , readonly);
		petInfo = petColumn.getPetInfo(petKey);
		if(petInfo == null)
		{
			throw new IllegalArgumentException("人身上宠物栏Id错误：" + petKey);
		}
		agent = petInfo.getBuffpet();
	}
	
	
	/**以此方式可以使用petinfo来构造buff宠物，但是这种方式构造的宠物没有关联的roleid和key，无法发送协议*/
	public BuffPetImpl(xbean.PetInfo petInfo)
	{
		this.readonly = false;
		this.roleId = -1;
		this.petKey = -1;
		this.petInfo = petInfo;
		this.agentType = BuffConstant.BuffAgentType.PET;
		agent = petInfo.getBuffpet();
	}
	
	@Override
	public boolean existState(int buffTypeId)
	{
		return false;
	}

	@Override
	public Role getERole()
	{
		if(roleId <= 0)
			return new fire.pb.effect.PetImpl(petInfo);
		else
			return new fire.pb.effect.PetImpl(roleId,petKey,readonly);
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
		if(roleId <= 0 || petKey == -1)
		{
			Module.logger.error("ERROR：roleId <= 0 || petKey == -1");
			return false;
		}
		if(result == null)
			return false;
		Map<Integer,Float> clientAttrs = fire.pb.effect.Module.getClientAttrs(result.getChangedAttrs());
		if(clientAttrs.size() != 0)
			mkdb.Procedure.psendWhileCommit(roleId,new SRefreshPetData(PetColumnTypes.PET,petKey,(HashMap<Integer, Float>)clientAttrs));
		SBuffChangeResult sBuffChangeResult = getSBuffChangeResult(result);
		if(sBuffChangeResult.addedbuffs.size() == 0 && sBuffChangeResult.deletedbuffs.size() == 0)
			return false;
		mkdb.Procedure.psendWhileCommit(roleId, sBuffChangeResult);
		return true;
	}
	@Override
	public SBuffChangeResult getSBuffChangeResult(Result result)
	{
		if(roleId <= 0 || petKey == -1)
			return null;
		SBuffChangeResult sBuffChangeResult = new SBuffChangeResult();
		sBuffChangeResult.agenttype = agentType;
		sBuffChangeResult.id = roleId;
		sBuffChangeResult.petid = petKey;
		
		for(Integer buffId : result.getDeletedBuffs())
		{
			ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(buffId);
			if(!cbuffconf.isSendClient())
				continue;
			
			sBuffChangeResult.deletedbuffs.add(buffId);
		}
		//完善tips
		for(Map.Entry<Integer, fire.pb.buff.Buff> entry : result.getAddedBuffMap().entrySet())
		{
			ConstantlyBuffConfig cbuffconf = Module.getInstance().getDefaultCBuffConfig(entry.getKey());
			if(!cbuffconf.isSendClient())
				continue;
			sBuffChangeResult.addedbuffs.put(entry.getKey(),entry.getValue());
		}
		return sBuffChangeResult;
	}

	public xbean.PetInfo getPetInfo()
	{
		return petInfo;
	}

	@Override
	public void startBuffTimer(ConstantlyBuff buff)
	{
		
	}

	@Override
	public BuffTimer getBuffTimer(int buffId, long initialDelay, long period, int count)
	{
		return null;
	}
	public long getRoleId()
	{
		return roleId;
	}
	@Override
	public void addSceneState(ConstantlyBuffConfig buffcfg){}

	@Override
	public void removeSceneState(ConstantlyBuffConfig buffcfg){}
}
