package fire.pb.fushi.redpack;

import java.util.Comparator;


public class RedPackStateTimeComparator implements Comparator<xbean.RedPackBaseInfo> {
	@Override
	public int compare(xbean.RedPackBaseInfo o1, xbean.RedPackBaseInfo o2) {
		//先按红包状态排序
//		if(o1.getRedpackstate()<o2.getRedpackstate()){
//			return -1;
//		}else if(o1.getRedpackstate()==o2.getRedpackstate()){
			if(o1.getSendtime()>o2.getSendtime()){
				return -1;
			}else if(o1.getSendtime()==o2.getSendtime()){
				return o1.getRedpackid().compareTo(o2.getRedpackid());
			}
//		}
		return 1;
	}

}
