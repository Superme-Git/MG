

package fire.pb.circletask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CircTaskSchoolItemCollectConf {

	private int id;
	private int ctgroup;
	private int school;
	private int levelmin;
	private int levelmax;
	private int itemid;
	private int itemnum;
	private List<Integer> mapids = new ArrayList<Integer>();
	private List<Integer> mapProbs = new ArrayList<Integer>();
	private Map<Integer,List<Integer>> monsters = new HashMap<Integer, List<Integer>>();
	private Map<Integer,List<Integer>> monsterProbs = new HashMap<Integer, List<Integer>>();
	public CircTaskSchoolItemCollectConf(int id,int ctgroup,int school,
										int levelmin,int levelmax,int itemid,
										int itemnum) {

		super();
		this.id = id;
		this.ctgroup = ctgroup;
		this.school = school;
		this.levelmin = levelmin;
		this.levelmax = levelmax;
		this.itemid = itemid;
		this.itemnum = itemnum;
		
	}
	
	public int getId() {
		
		return id;
	}
	
	public int getGroup() {
		
		return ctgroup;
	}
	
	public int getSchool() {
		
		return school;
	}
	
	public int getLevelMin() {
		
		return levelmin;
	}
	
	public int getLevelMax() {
		
		return levelmax;
	}
	
	public int getItemid() {
	
		return itemid;
	}
	
	public int getItemnum() {
	
		return itemnum;
	}
	
	public List<Integer> getMapids() {
	
		return mapids;
	}
	
	public List<Integer> getMapProbs() {
	
		return mapProbs;
	}
	
	public Map<Integer,List<Integer>> getMonsters() {
	
		return monsters;
	}
	
	public Map<Integer,List<Integer>> getMonsterProbs() {
	
		return monsterProbs;
	}

}

