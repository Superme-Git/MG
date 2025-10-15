package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.Misc;

public class CircleTaskEventGroup {
	
	private static class EventGroup {
		public int eventid;
		public int eventrate;
		
		public EventGroup(int eventid, int eventrate) {
			this.eventid = eventid;
			this.eventrate = eventrate;
		}
	}
	
	public List<List<EventGroup>> eGroupList = new ArrayList<List<EventGroup>>();

	public CircleTaskEventGroup(final String eventStr) {
		String [] strFen = eventStr.split(";");
		for (String strFenFen : strFen) {
			String [] strDou = strFenFen.split(",");
			List<EventGroup> eventGroupList = new ArrayList<EventGroup>();
			for (String strDouDou : strDou) {
				String [] strAt = strDouDou.split("@");
				EventGroup egroup = new EventGroup(Integer.parseInt(strAt[0]), Integer.parseInt(strAt[1]));
				eventGroupList.add(egroup);
			}
			eGroupList.add(eventGroupList);
		}
	}
	
	public List<Integer> getEventList() {
		List<Integer> retList = new ArrayList<Integer>();
		
		int nIndex1 = Misc.getRandomBetween(0, eGroupList.size() - 1);
		if (nIndex1 > eGroupList.size())
			return null;
		
		List<EventGroup> group = eGroupList.get(nIndex1);
		
		for (EventGroup eventData : group) {
			if (eventData.eventrate > Misc.getRatePercent())
				retList.add(eventData.eventid);
		}
		
		return retList;
	}
	
	public List<Integer> getEventListByGm() {
		List<Integer> retList = new ArrayList<Integer>();
		
		int nIndex = Misc.getRandomBetween(0, eGroupList.size() - 1);
		if (nIndex > eGroupList.size())
			return null;
		
		List<EventGroup> group = eGroupList.get(nIndex);
		
		for (EventGroup eventData : group) {
			retList.add(eventData.eventid);
		}
		
		return retList;
	}
	
}
