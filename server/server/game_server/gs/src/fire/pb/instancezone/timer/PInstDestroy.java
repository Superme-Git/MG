package fire.pb.instancezone.timer;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import mkdb.Procedure;

public class PInstDestroy extends Procedure {

	private final long landkey;
	public PInstDestroy(final long landkey) {
		this.landkey = landkey;
	}
	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, false);
		if (instzone == null)
			return false;
		
		instzone.destroyAfterBattle();
		InstanceZone.logger.info("副本[" + landkey + "延迟结束到期");
		return true;
	}
	
	


}
