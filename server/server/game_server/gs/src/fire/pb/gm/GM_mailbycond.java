package fire.pb.gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.compensation.CreateSingleCompensation;
import fire.pb.http.handler.GMHttpExecuteHandler;

public class GM_mailbycond extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 5) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		String strTitle = args[0]; // 标题
		String strContent = args[1]; // 内容
		int durationTime = Integer.parseInt(args[2]); // 持续分钟
		String strAwardList = args[3]; // 奖励列表
		String strCondList = args[4]; // 条件列表

		// 解析奖励
		String strAward[] = strAwardList.split(",");
		List<xbean.SingleCompensationAward> awards = new LinkedList<xbean.SingleCompensationAward>();
		for (int i = 0 ; i < strAward.length; ++i) {
			String array[] = strAward[i].split("\\|");
			if (array.length != 2) {
				continue;
			}
			int itemId = Integer.parseInt(array[0]);
			int itemNum = Integer.parseInt(array[1]);
			if (itemId <= 0 || itemNum <= 0) {
				sendToGM("参数格式错误："
						+ " 第" + (i + 1) + "个奖励"
						+ " itemId:" + itemId
						+ " itemNum:" + itemNum);
				return false;
			}

			xbean.SingleCompensationAward award = xbean.Pod.newSingleCompensationAwardData();
			switch (itemId) {
			case GMHttpExecuteHandler.GM_ITEM_CURRENCY_SILVER:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_MONEY);
				award.setNum(itemNum);
				break;
			case GMHttpExecuteHandler.GM_ITEM_CURRENCY_GOLD:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_GOLD);
				award.setNum(itemNum);
				break;
			case GMHttpExecuteHandler.GM_ITEM_CURRENCY_FUSHI:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_FUSHI);
				award.setNum(itemNum);
				break;
			case GMHttpExecuteHandler.GM_ITEM_CURRENCY_EXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_EXP);
				award.setNum(itemNum);
				break;
			case GMHttpExecuteHandler.GM_ITEM_CURRENCY_VIPEXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_VIPEXP);
				award.setNum(itemNum);
				break;
			default:
				{
					int itemFlag = fire.pb.compensation.Helper.AWARDFLAG_BIND;
					award.setType(fire.pb.compensation.Helper.AWARDTYPE_ITEM);
					award.setId(itemId);
					award.setNum(itemNum);
					award.setFlag(itemFlag);
				}
				break;
			}
			awards.add(award);
		}

		// 解析条件
		String strCond[] = strCondList.split(",");
		Map<Integer, List<String>> conds = new HashMap<Integer, List<String>>();
		for (int i = 0; i < strCond.length; i++) {
			String array[] = strCond[i].split("\\|");
			if (array.length != 3) {
				continue;
			}
			int condId = Integer.parseInt(array[0]);
			if (condId <= 0) {
				sendToGM("参数格式错误："
						+ " 第" + (i + 1) + "个条件"
						+ " condId:" + condId);
			}
			conds.put(condId, new ArrayList<String>());
			conds.get(condId).add(array[1]);
			conds.get(condId).add(array[2]);
		}

		long endTime = 0;
		if (durationTime > 0) {
			endTime = System.currentTimeMillis() + (durationTime * 60 * 1000);
		}
		// 发送
		CreateSingleCompensation.createFromYunYingCond(
				conds,
				awards,
				strTitle,
				strContent,
				endTime,
				"GMOPID",
				"GMSIGN").submit();
		return true;
	}

	@Override
	String usage() {
		return "//mailbycond title content duration awardContent:1|100,2|100 condContent:1|10|100,2|11|15";
	}

}
