package fire.pb.gm;

import java.util.Map.Entry;

public class GM_awardlimit extends GMCommand {

	@Override
	boolean exec(final String[] args) {

		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {

				if (args.length == 1) {
					Integer awardid = Integer.parseInt(args[0]);
					xbean.ActivityItemLimit activityItemLimit = xtable.Awarditemlimit
							.select(awardid);
					StringBuilder sb = new StringBuilder();
					if (activityItemLimit != null) {
						for (Entry<Integer, xbean.LimitItemInfo> entry : activityItemLimit
								.getLimititemmap().entrySet()) {
							sb.append("itemid:" + entry.getKey())
									.append("maxnum:")
									.append(entry.getValue().getMaxnum())
									.append("num:")
									.append(entry.getValue().getNum());
						}
					}
					if (sb.length() > 1)
						sendToGM(sb.toString());
					else
						sendToGM("该awardid没有物品限制");
				} else if (args.length == 4) {
					Integer awardid = Integer.parseInt(args[0]);
					Integer itemid = Integer.parseInt(args[1]);
					Integer maxnum = Integer.parseInt(args[2]);
					Integer num = Integer.parseInt(args[3]);
					xbean.ActivityItemLimit activityItemLimit = xtable.Awarditemlimit
							.get(awardid);
					if (activityItemLimit == null) {
						activityItemLimit = xbean.Pod.newActivityItemLimit();
						xtable.Awarditemlimit
								.insert(awardid, activityItemLimit);
					}
					xbean.LimitItemInfo limitItemInfo = activityItemLimit
							.getLimititemmap().get(itemid);
					if (limitItemInfo == null) {
						limitItemInfo = xbean.Pod.newLimitItemInfo();
						activityItemLimit.getLimititemmap().put(itemid,
								limitItemInfo);
					}
					limitItemInfo.setMaxnum(maxnum);
					limitItemInfo.setNum(num);
					sendToGM("设置成功,可以用//awardlimit awardid验证");
				} else {
					sendToGM(usage());
				}
				return true;
			}

		}.submit();

		return true;
	}

	@Override
	String usage() {

		return "//awardlimit awardid显示该awardid的物品限制.//awardlimit awardid itemid maxnum num重置该awardid的itemid的数量";
	}

}
