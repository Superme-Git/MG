package fire.pb.fushi.redpack;

import java.util.Comparator;


public class RedPackReceiveTimeComparator implements Comparator<xbean.RoleReceiveRedPackRecord> {
	@Override
	public int compare(xbean.RoleReceiveRedPackRecord o1, xbean.RoleReceiveRedPackRecord o2) {
		if(o1.getReceivetime()>o2.getReceivetime()){
			return -1;
		}else if(o1.getReceivetime()==o2.getReceivetime()){
			return o1.getRedpackid().compareTo(o2.getRedpackid());
		}
		return 1;
	}

}
