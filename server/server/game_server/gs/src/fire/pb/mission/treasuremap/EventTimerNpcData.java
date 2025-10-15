package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;

import fire.pb.map.EventTimerNpc;

public class EventTimerNpcData {
	public final int id;
	public final int refreshGroup;
	
	public List<EventTimerGroupData> groupData;
	
	public EventTimerNpcData(final EventTimerNpc npcConfig) {
		this.id = npcConfig.getId();
		this.refreshGroup = npcConfig.getRefreshGroup();
		
		groupData = new ArrayList<EventTimerGroupData>();
	}
	
	public void AddTimerGroupData(final EventTimerNpc npcConfig) {
		EventTimerGroupData data = new EventTimerGroupData(npcConfig);
		groupData.add(data);
	}
	
	
	public EventTimerGroupData getGroupData(final int roleLevel) {
		for (EventTimerGroupData data : groupData) {
			if (roleLevel > data.levelMin && roleLevel < data.levelMax) {
				return data;
			}
		}
		
		return null;
	}
	
	public EventTimerGroupData getGroupDataById(int id) {
		for (EventTimerGroupData data : groupData) {
			if (data.id == id) {
				return data;
			}
		}
		
		return null;
	}

}
