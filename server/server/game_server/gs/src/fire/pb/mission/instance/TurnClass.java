package fire.pb.mission.instance;

import java.util.ArrayList;
import java.util.List;

public class TurnClass {
	public int groupid;
	public int turntype;
	public List<TurnGroup> groupList;
	
	public TurnClass() {
		groupList = new ArrayList<TurnGroup>();
	}

}
