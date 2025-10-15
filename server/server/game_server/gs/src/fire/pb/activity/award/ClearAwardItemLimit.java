
package fire.pb.activity.award;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;


public class ClearAwardItemLimit implements Runnable {

	@Override
	public void run() {

		Map<Integer, Set<Integer>> clearMap = RewardMgr.getInstance().getClearLimitMap();
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.HOUR_OF_DAY) == 2) {// 到了2点的时候判断清除
			Set<Integer> awardidsday = clearMap.get(1);// 每日清除
			if (awardidsday != null && awardidsday.size() > 0) {
                 new PClearAwardItemLimit(awardidsday).submit();
			}
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {// 每周一清除
				Set<Integer> awardidsWeek = clearMap.get(2);
				if (awardidsWeek != null && awardidsWeek.size() > 0) {
                  new PClearAwardItemLimit(awardidsWeek).submit();
				}
			}
			if (cal.get(Calendar.DAY_OF_MONTH) == 1) {// 每月的一号清除
				Set<Integer> awardidsMonth = clearMap.get(3);
				if (awardidsMonth != null && awardidsMonth.size() > 0) {
                  new PClearAwardItemLimit(awardidsMonth).submit();
				}
			}
		}
		// 每小时整清除
		Set<Integer> awardidsHour = clearMap.get(5);
		if (awardidsHour != null && awardidsHour.size() > 0)
			new PClearAwardItemLimit(awardidsHour).submit();
	}

}
