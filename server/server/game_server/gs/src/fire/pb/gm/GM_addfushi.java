package fire.pb.gm;

import java.util.concurrent.atomic.AtomicLong;

import fire.log.enums.YYLoggerTuJingEnum;

public class GM_addfushi extends GMCommand {

	private static AtomicLong totalNum1 = new AtomicLong();
	private static AtomicLong totalNum2 = new AtomicLong();
	private static AtomicLong totalNum3 = new AtomicLong();

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		if (!fire.pb.StateCommon.isOnline(getGmroleid()))
			return false;
		final int fushiType = Integer.parseInt(args[0]);
		final int fushi = Integer.parseInt(args[1]);

		if (args.length == 3 && Integer.parseInt(args[2]) == 1) {
			sendToGM("type1:" + totalNum1 + "type2:" + totalNum2 + "type3:" + totalNum3);
			return true;
		}
		if (fushi <= 0 || fushiType <= 0 || fushiType >= 4) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}
		fire.pb.fushi.PAddFuShi pAddFuShi = new fire.pb.fushi.PAddFuShi(getGmUserid(), getGmroleid(), fushi, fushiType - 1,
				YYLoggerTuJingEnum.GM);
		pAddFuShi.submit();
		if (fushiType == 1)
			totalNum1.addAndGet(fushi);
		else if (fushiType == 2) {
			totalNum2.addAndGet(fushi);
		} else if (fushiType == 3) {
			totalNum3.addAndGet(fushi);
		}
		return true;
	}

	@Override
	String usage() {

		return "必须有两个参数,第一个指定符石类型(1，非绑定.2。绑定.3。系统赠送),第二个指定符石数量";
	}

}
