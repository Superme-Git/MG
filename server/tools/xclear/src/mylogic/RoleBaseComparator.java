package mylogic;

import java.util.Comparator;


public class RoleBaseComparator implements Comparator<Rolebean> {
	@Override
	public int compare(Rolebean o1, Rolebean o2) {
		if(o1.getLevel()>o2.getLevel()){
			return -1;
		}else if(o1.getLevel()==o2.getLevel()){
			if(o1.getServerid()<o2.getServerid()){
				return -1;
			}else if(o1.getServerid()==o2.getServerid()){
				if(o1.getCreatetime()<o2.getCreatetime()){
					return -1;
				}
			}
		}
		return 1;
	}

}
