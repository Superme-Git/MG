package fire.pb.gm;

import fire.pb.shop.utils.MarketUtils;

/**
 * 拍卖道具在聊天频道玩家请求是否显示
 * 
 * @author yangtao
 * @dateTime 2016年6月14日 上午1:41:14
 * @version 1.0
 */
public class GM_mchat extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		if (!args[0].equals("0") && !args[0].equals("1")) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		int isMarketItemChatShow = Integer.valueOf(args[0]);
		// 请求显示
		if (isMarketItemChatShow == 1)
			MarketUtils.isMarketItemChatShow = true;
		else
			MarketUtils.isMarketItemChatShow = false;
		return true;
	}

	@Override
	String usage() {
		return "//mchat 必须加参数0或者1";
	}
}
