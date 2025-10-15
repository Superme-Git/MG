

package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;


public class CircTaskSchoolPatrolConf {

	private int id;
	private int ctgroup;
	private int school;
	private int levelmin;
	private int levelmax;
	private List<Integer> mapids = new ArrayList<Integer>();
	private List<Integer> mapProbs = new ArrayList<Integer>();
	
	private List<Integer> battlecfgids = new ArrayList<Integer>();
	private List<Integer> battlecfgProbs = new ArrayList<Integer>();
	
	
	public CircTaskSchoolPatrolConf(int id,int ctgroup,int school,
										int levelmin,int levelmax) {

		super();
		this.id = id;
		this.ctgroup = ctgroup;
		this.school = school;
		this.levelmin = levelmin;
		this.levelmax = levelmax;
		
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
	
	public List<Integer> getMapids() {
	
		return mapids;
	}
	
	public List<Integer> getMapProbs() {
	
		return mapProbs;
	}
	
	public List<Integer> getBattleCFGs() {
		
		return battlecfgids;
	}
	
	public List<Integer> getBattleCFGProbs() {
	
		return battlecfgProbs;
	}

}

