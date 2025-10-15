package fire.pb.mission.instance.line;

public class LineTimeOut implements Runnable {

	private final long instOnlyId;
	private final int instanceZoneId;

	public LineTimeOut(final long instOnlyId, final int instanceZoneId) {
		super();
		this.instOnlyId = instOnlyId;
		this.instanceZoneId = instanceZoneId;
	}

	@Override
	public void run() {
       new PLineTimeOut(instOnlyId, instanceZoneId).submit();
	}

}

