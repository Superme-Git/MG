package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.List;

public class TimerNpcGroupData {
	public final int groupId;
	public List<Integer> NpcList;
	
	public TimerNpcGroupData(final int groupId, final String npclist) {
		NpcList = new ArrayList<Integer>();
		this.groupId = groupId;
		
		String [] strs = npclist.split(",");
		for (String str : strs) {
			if (str != null)
				NpcList.add(Integer.parseInt(str));
		}
	}

}
