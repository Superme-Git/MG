package fire.pb.battle.livedie;

import java.util.Comparator;


public class LiveDieComparator implements Comparator<xbean.LDVideoRoleInfoDes> {
	@Override
	public int compare(xbean.LDVideoRoleInfoDes o1, xbean.LDVideoRoleInfoDes o2) {
		if(o1.getRosenum()>o2.getRosenum()){
			return -1;
		}else if(o1.getRosenum()==o2.getRosenum()){
			if(o1.getFighttime()>o2.getFighttime()){
				return -1;
			}
		}
		return 1;
	}

}
