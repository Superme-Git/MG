
package fire.pb.activity.award;

import java.util.Set;

import mkdb.Procedure;

public class PClearAwardItemLimit extends Procedure {

	private final Set<Integer> awardids;

	public PClearAwardItemLimit(Set<Integer> awardids) {

		this.awardids = awardids;
	}

	@Override
	protected boolean process() throws Exception {

		for (Integer awardid : awardids) {

			xbean.ActivityItemLimit activityItemLimit = xtable.Awarditemlimit.get(awardid);
			if (activityItemLimit!=null) {
				for (xbean.LimitItemInfo limitItemInfo : activityItemLimit.getLimititemmap().values()) {
					limitItemInfo.setNum(0);
				}
			}
		}
		return true;
	}

}
