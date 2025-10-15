package fire.pb.instancezone.conf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.pb.instance.SInstanceService;
import fire.pb.instancezone.Module;

public class NpcServiceConfig
{
	public final SInstanceService sFubennpcserv;

	public final int serviceId;
	public final int landId;

	
	public final int initact;
	public final static int STATE_HIDE = 0;
	Map<TriggerCondition, Integer> changestates = new HashMap<TriggerCondition, Integer>();
	public NpcServiceConfig(SInstanceService sFubennpcserv)
	{
		this.sFubennpcserv = sFubennpcserv;
		this.serviceId = sFubennpcserv.id;
		this.landId = sFubennpcserv.belongfuben;
		this.initact = sFubennpcserv.friststate;
		if (sFubennpcserv.changestate != null)
			for (String change : sFubennpcserv.changestate)
			{
				String[] strs = change.split("=");
				changestates.put(new TriggerCondition(strs[0]), Integer.valueOf(strs[1]));
			}
	}

	
	public Integer getSaveChange(SaveCondition condition)
	{
		Set<Integer> changes = new HashSet<Integer>();
		Integer state = null;
		for (Map.Entry<TriggerCondition, Integer> entry : changestates.entrySet())
		{
			if (condition.trigger(entry.getKey()))
			{
				state = entry.getValue();
				changes.add(entry.getValue());
			}
		}
		if (changes.size() > 1)
			Module.logger.error("在SaveCondition为 ：" + condition + "时，NPC服务：" + serviceId + "可能有多种状态变化");
		return state;
	}
}
