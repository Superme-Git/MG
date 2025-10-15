package fire.pb.instancezone.act;

import java.util.List;
import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.ActionConfig;
import fire.pb.instancezone.conf.ConfigParamName;

public class TaskAct extends ServiceAct {

	public final List<Integer> taskid;

	@SuppressWarnings("unchecked")
	public TaskAct(int actId, Map<String, Object> params) {
		super(actId, params);
		this.taskid = (List<Integer>) params.get(ConfigParamName.TASK_ID);
	}

	@Override
	public int getType() {
		return ActionConfig.TYPE_TASK;
	}

	@Override
	public boolean defaultprocess(InstanceZone instzone) {
		return true;
	}

	public List<Integer> getTaskIds() {
		return taskid;
	}
}
