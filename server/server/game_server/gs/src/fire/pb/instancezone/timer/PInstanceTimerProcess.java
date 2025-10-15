package fire.pb.instancezone.timer;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;


public class PInstanceTimerProcess extends mkdb.Procedure
{
	final private long landkey;
	@SuppressWarnings("unused")
	final private int timerId;
	
	
	public PInstanceTimerProcess(long landkey, int timerId)
	{
		this.landkey = landkey;
		this.timerId = timerId;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, false);
		if (instzone == null)
			return false;
		instzone.destroy(true, false);
		InstanceZone.logger.debug("副本[" + instzone.getInstZoneId() + "]时间到,结束副本！");
		return true;
	}
}

