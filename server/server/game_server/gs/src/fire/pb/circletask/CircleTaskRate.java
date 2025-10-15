package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.GeneralFormat;


public class CircleTaskRate {
	public int id;
	public int groupid;
	public int levelmin;
	public int levelmax;
	public int maxnum;
	public int type;
	public List< List<GeneralFormat> > rounds;

	public CircleTaskRate() {
		rounds = new ArrayList<List<GeneralFormat>>();
	}
}
