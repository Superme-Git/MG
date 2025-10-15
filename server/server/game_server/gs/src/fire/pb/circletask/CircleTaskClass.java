package fire.pb.circletask;

import java.util.HashMap;
import java.util.Map;

public class CircleTaskClass {
	public int circletype;
	public Map<Integer, CircleTaskGroup> sqctGroup;
	
	public CircleTaskClass() {
		sqctGroup = new HashMap<Integer, CircleTaskGroup>();
	}
}
