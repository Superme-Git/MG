package fire.pb.instancezone.act;

import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;


public class CompleteSubsaveAct extends ServiceAct
{
	public int saveId;
	
	public int subsaveId;
	
	public int count;
	
	public CompleteSubsaveAct(int actId, Map<String, Object> params)
	{
		super(actId, params);
		this.saveId = (Integer)params.get(ConfigParamName.SAVE_ID);
		this.subsaveId = (Integer)params.get(ConfigParamName.SUB_SAVE_ID);
		this.count = (Integer)params.get(ConfigParamName.SUB_SAVE_COUNT);
	}

	@Override
	public int getType()
	{
		return ActionConfig.TYPE_COMPLETE_SUBSAVE;
	}
	
	@Override
	public boolean defaultprocess(InstanceZone instzone)
	{
		return instzone.updateSubSave(roleId, saveId, subsaveId, count);
	}
}
