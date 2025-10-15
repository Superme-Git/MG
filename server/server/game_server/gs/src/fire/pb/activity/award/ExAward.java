

package fire.pb.activity.award;

import java.util.ArrayList;
import java.util.List;


public class ExAward {

	private final int awardid;
	private final List<Long> startTimes = new ArrayList<Long>();
	private final List<Long> endTimes = new ArrayList<Long>();
	private final List<String> zoneids = new ArrayList<String>();
	private final int exawardid;
	
	
	public ExAward(int awardid,Long startTime, Long endTimes, String zoneid, int exawardid) {

		super();
		this.awardid = awardid;
		this.startTimes.add(startTime);
		this.endTimes.add(endTimes);
		this.zoneids.add(zoneid);
		this.exawardid = exawardid;
	}

	public long getAwardid() {
	
		return awardid;
	}
	
	public List<Long> getStartTimes() {
	
		return startTimes;
	}
	
	public List<Long> getEndTimes() {
	
		return endTimes;
	}
	
	public List<String> getZoneids() {
	
		return zoneids;
	}
	
	public int getExawardid() {
	
		return exawardid;
	}
	
}

