
package fire.pb.compensation;

import java.util.ArrayList;
import java.util.List;

public class SpecialCompensationConfig {

	private final int id;//serviceid
	private final long roleid;
	private final Integer userid;
	private final int awardid;
	private final long startTime;
	private final long endTime;
	private final int compensationMsgid;
	private final String serviceName;
	private final List<CompensationCondition>conditions;//奖励的条件,只有满足所有条件的人才给奖励
	private final List<Integer> zoneids;//奖励的服务器
	public SpecialCompensationConfig(int id,long roleid, Integer userid, int awardid, long startTime, long endTime, String conditions,int msgid,String serviceName,List<Integer> zoneids) {

		super();
		this.id = id;
		this.roleid = roleid;
		this.userid = userid;
		this.awardid = awardid;
		this.startTime = startTime;
		this.endTime = endTime;
		this.compensationMsgid = msgid;
		this.serviceName = serviceName;
		this.zoneids = zoneids;
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
	
	public long getRoleid() {
	
		return roleid;
	}
	
	public Integer getUserid() {
	
		return userid;
	}
	
	public int getAwardid() {
	
		return awardid;
	}
	
	public long getStartTime() {
	
		return startTime;
	}
	
	public long getEndTime() {
	
		return endTime;
	}
	
	public List<CompensationCondition> getCondtions() {
	
		return conditions;
	}

	
	public int getId() {
	
		return id;
	}
	
	public boolean isInTime(long time){
		return startTime < time && time < endTime;
	}

	
	public int getCompensationMsgid() {
	
		return compensationMsgid;
	}

	
	public String getServiceName() {
	
		return serviceName;
	}

	
	public List<Integer> getZoneids() {
	
		return zoneids;
	}
	
}

