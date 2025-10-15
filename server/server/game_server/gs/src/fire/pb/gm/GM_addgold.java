package fire.pb.gm;

public class GM_addgold extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final long money;
		final long roleid;
		money = Long.parseLong(args[0]);
		if (money == 0 || money > 1L << 60 || money < (1L << 60) * -1) {
			return false;
		}

		if (args.length >= 2)
			roleid = Long.valueOf(args[1]);
		else
			roleid = getGmroleid();

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				bag.addSysGold(money, "GM指令 加金币", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addmoney gold";
	}

}
