package fire.pb.instancezone.act;

import java.util.Map;

import fire.pb.instancezone.conf.ConfigParamName;


public abstract class ServiceAct extends Act
{
	final public long npckey;
	final public int serviceId;
	final public long roleId;
	final public int npcbaseId;
	public ServiceAct(int actId, Map<String, Object> params)
	{
		super(actId, params);
		this.roleId = (Long)params.get(ConfigParamName.ROLE_ID);
		this.npckey = (Long)params.get(ConfigParamName.NPC_KEY);
		this.serviceId = (Integer)params.get(ConfigParamName.NPC_SERVICE_ID);
		this.npcbaseId = (Integer)params.get(ConfigParamName.NPC_ID);
	}
	public long getNpckey()
	{
		return npckey;
	}
	public int getServiceId()
	{
		return serviceId;
	}
	public long getRoleId()
	{
		return roleId;
	}
	public int getNpcBaseId()
	{
		return npcbaseId;
	}
	
}
