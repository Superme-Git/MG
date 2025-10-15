package fire.pb.fushi.spotcheck;

import java.util.Comparator;


public class RoleTradingHisRecordComparator implements Comparator<xbean.RoleTradingHisRecord> {
	@Override
	public int compare(xbean.RoleTradingHisRecord o1, xbean.RoleTradingHisRecord o2) {
		if(o1.getTradingtime()>o2.getTradingtime()){
			return -1;
		}
		if(o1.getTradingtime()==o2.getTradingtime()){
			return o1.getTradingid().compareTo(o2.getTradingid());
		}
		return 1;
	}

}
