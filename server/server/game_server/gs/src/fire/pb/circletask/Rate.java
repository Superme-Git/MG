package fire.pb.circletask;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.GeneralFormat;


public class Rate {
	
	public int levelmin;
	public  int levelmax;
	public List< List<GeneralFormat> > rounds;

	public Rate() {
		rounds = new ArrayList<List<GeneralFormat>>();
	}
}
