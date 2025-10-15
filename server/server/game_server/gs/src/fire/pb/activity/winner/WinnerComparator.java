package fire.pb.activity.winner;

import java.util.Comparator;


public class WinnerComparator implements Comparator<WinnerRecord> {
	@Override
	public int compare(WinnerRecord o1, WinnerRecord o2) {
		if(o1.getTeamScore()>o2.getTeamScore()){
			return -1;
		}else if(o1.getTeamScore()==o2.getTeamScore()){
			if(o1.getAllCostTime()<o2.getAllCostTime()){
				return -1;
			}
		}
		return 1;
	}

}
