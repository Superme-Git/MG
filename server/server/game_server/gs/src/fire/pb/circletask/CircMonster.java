package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

public class CircMonster {
	
	public List<Integer> mapId;
	public List<Integer> mapProb;
	
	public List<Integer> battleId;
	public List<Integer> battleProb;
	
	public CircMonster() {
		mapId = new ArrayList<Integer>();
		mapProb = new ArrayList<Integer>();
		
		battleId = new ArrayList<Integer>();
		battleProb = new ArrayList<Integer>();
	}
	

}
