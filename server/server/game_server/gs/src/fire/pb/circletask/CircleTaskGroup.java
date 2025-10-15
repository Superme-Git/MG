package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

public class CircleTaskGroup {
//	public int id;
	public int circletype;
	public int groupid;
	public int levelmin;
	public int levelmax;
	public List<Integer> levelRate;
	public List<CircleTaskRate> listSQCT;
	
	public CircleTaskGroup() {
		levelRate = new ArrayList<Integer>();
		listSQCT = new ArrayList<CircleTaskRate>();
	}

}
