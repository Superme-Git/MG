package fire.pb.game;

import java.util.ArrayList;
import java.util.List;

public class WheelItem {
	public List<Integer> itemLst;
	public List<Integer> numLst;
	public List<Integer> rateLst;
	public List<Integer> notice;
	public List<Integer> msg;
	
	public WheelItem(final List<String> wheelitem) {
		if (wheelitem == null) 
			return;
		
		itemLst = new ArrayList<Integer>();
		numLst = new ArrayList<Integer>();
		rateLst = new ArrayList<Integer>();
		notice = new ArrayList<Integer>();
		msg = new ArrayList<Integer>();
		
		for (String itemsub : wheelitem) {
			String [] itemss = itemsub.split(";");
			itemLst.add(Integer.valueOf(itemss[0]));
			numLst.add(Integer.valueOf(itemss[1]));
			rateLst.add(Integer.valueOf(itemss[2]));
			notice.add(Integer.valueOf(itemss[3]));
			msg.add(Integer.valueOf(itemss[4]));
		}
	}

}
