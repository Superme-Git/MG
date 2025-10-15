package fire.pb.gm;

public class GM_addhb extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final Integer huobanid = Integer.parseInt(args[0]);
		if (huobanid == null) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final Integer timetype = Integer.parseInt(args[1]);
		if (timetype == null) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final Integer times = Integer.parseInt(args[2]);
		if (times == null) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		new fire.pb.huoban.PAddHuoBanTimes(getGmroleid(), times, timetype,
				huobanid).submit();

		return true;
	}

	@Override
	String usage() {
		return "addhb huobanid type times";
	}

}
