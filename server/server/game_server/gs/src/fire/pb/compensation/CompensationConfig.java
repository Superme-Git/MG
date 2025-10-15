package fire.pb.compensation;

import java.util.ArrayList;
import java.util.List;

public class CompensationConfig {
	private long startTime;

	private long endTime;

	private int itemid;

	private final List<CompensationCondition>conditions;

	private int compensationMsg;
	
	private int compensationid;

	private String serviceMsg;
	
	String plats;
	
	String nonPlats;
	
	private final List<Integer> zoneids;//奖励的服务器

	
	public CompensationConfig(long startTime, long endTime, int itemid, int compensationid, int compensationMsg, String serviceMsg,String conditions,List<Integer> zoneids,String plats,String noPlats) {

		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.itemid = itemid;
		this.compensationid = compensationid;
		this.compensationMsg = compensationMsg;
		this.serviceMsg = serviceMsg;
		this.zoneids = zoneids;
		this.plats = plats;
		this.nonPlats = noPlats;
		this.conditions = new ArrayList<CompensationCondition>();
		if (conditions!=null && conditions.length()>1){
			String[] cons = conditions.split(";");
			if (cons.length>0){
				for (String con : cons) {
					if (con.equals("0"))
						continue;
					String[] conIDAndArgs = con.split(",");
					this.conditions.add(new CompensationCondition(conIDAndArgs));
				}
			}
		}
	}


	public long getStartTime() {
	
		return startTime;
	}

	
	public void setStartTime(long startTime) {
	
		this.startTime = startTime;
	}

	
	public long getEndTime() {
	
		return endTime;
	}

	
	public void setEndTime(long endTime) {
	
		this.endTime = endTime;
	}

	
	public int getItemid() {
	
		return itemid;
	}

	
	public void setItemid(int itemid) {
	
		this.itemid = itemid;
	}

	
	public int getCompensationid() {
	
		return compensationid;
	}

	
	public void setCompensationid(int compensationid) {
	
		this.compensationid = compensationid;
	}

	
	public int getCompensationMsg() {
	
		return compensationMsg;
	}

	
	public void setCompensationMsg(int compensationMsg) {
	
		this.compensationMsg = compensationMsg;
	}

	
	public String getServiceMsg() {
	
		return serviceMsg;
	}

	
	public void setServiceMsg(String serviceMsg) {
	
		this.serviceMsg = serviceMsg;
	}
	public boolean isInCompensationTime(long time) {

		return startTime < time && time < endTime;
	}
	public List<CompensationCondition> getCondtions() {
		
		return conditions;
	}


	
	public List<Integer> getZoneids() {
	
		return zoneids;
	}


	
	public String getPlats() {
	
		return plats;
	}


	
	public String getNonPlats() {
	
		return nonPlats;
	}

}

