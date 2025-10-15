package fire.pb.instancezone.event;

import java.util.Map;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.conf.EventConfig;

public class OtherEvent extends Event {

	public OtherEvent(Map<String, Object> params) {
		getParams().putAll(params);
	}

	@Override
	public int getType() {
		return EventConfig.TYPE_OTHER;
	}

	@Override
	public boolean process(InstanceZone instzone) {
		if (instzone.processOtherEvent(this))
			return super.process(instzone);
		else
			return false;
	}
}
