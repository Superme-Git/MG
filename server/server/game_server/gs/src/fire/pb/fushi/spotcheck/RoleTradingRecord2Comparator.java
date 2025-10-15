package fire.pb.fushi.spotcheck;

import java.util.Comparator;


public class RoleTradingRecord2Comparator implements Comparator<RoleTradingRecord> {
	@Override
	public int compare(RoleTradingRecord o1, RoleTradingRecord o2) {
		if(o1.price>o2.price){
			return -1;
		}else if(o1.price==o2.price){
			if(o1.tradingtime>o2.tradingtime){
				return -1;
			}else if(o1.tradingtime==o2.tradingtime){
				return o1.tradingid.compareTo(o2.tradingid);
			}
		}
		return 1;
	}

}
