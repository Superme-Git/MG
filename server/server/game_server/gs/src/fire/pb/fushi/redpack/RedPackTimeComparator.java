package fire.pb.fushi.redpack;

import java.util.Comparator;


public class RedPackTimeComparator implements Comparator<xbean.RedPackInfo> {
	@Override
	public int compare(xbean.RedPackInfo o1, xbean.RedPackInfo o2) {
		if(o1.getSendtime()>o2.getSendtime()){
			return -1;
		}else if(o1.getSendtime()==o2.getSendtime()){
			return o1.getRedpackid().compareTo(o2.getRedpackid());
		}
		return 1;
	}

}
