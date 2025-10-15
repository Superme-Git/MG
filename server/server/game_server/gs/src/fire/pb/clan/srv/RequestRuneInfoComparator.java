package fire.pb.clan.srv;

import java.util.Comparator;


public class RequestRuneInfoComparator implements Comparator<xbean.RequestRuneInfo> {
	@Override
	public int compare(xbean.RequestRuneInfo o1, xbean.RequestRuneInfo o2) {
		long v = o2.getActiontype()-o1.getActiontype();
		if(v==0){
			v = o1.getTargetroleid() - o2.getTargetroleid();
		}
		if (v == 0) {
			v = o1.getRequesttime() - o2.getRequesttime();
		}
		if (v == 0) {
			v = o1.getRoleid() - o2.getRoleid();
		}
		return (int) v;
	}

}
