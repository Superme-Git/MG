package fire.pb.gm;

public class GM_addqian extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int moneyType;
		final long money;
		moneyType = Integer.parseInt(args[0]);
		money = Long.parseLong(args[1]);
		if (money == 0 || money > 1L << 60 || money < (1L << 60) * -1) {
			return false;
		}

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(getGmroleid(),
						false);
				bag.addSysCurrency(money, moneyType, "GM指令 加货币", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addqian moneytype money";
	}
}
