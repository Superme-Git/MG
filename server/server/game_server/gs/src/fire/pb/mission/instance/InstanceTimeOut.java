

package fire.pb.mission.instance;



public class InstanceTimeOut implements Runnable {

	private final long instOnlyId;
	private final int instanceZoneId;

	public InstanceTimeOut(final long instOnlyId, final int instanceZoneId) {

		super();
		this.instOnlyId = instOnlyId;
		this.instanceZoneId = instanceZoneId;
	}

	
	@Override
	public void run() {
       new PInstanceTimeOut(instOnlyId, instanceZoneId).submit();
	}

}
