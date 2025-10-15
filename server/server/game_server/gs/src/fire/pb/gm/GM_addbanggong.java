package fire.pb.gm;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PAddClanPointProc;

public class GM_addbanggong extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int contri;
		contri = Integer.parseInt(args[0]);
		if (contri == 0 || contri > 1L << 60 || contri < (1L << 60) * -1) {
			return false;
		}
		new PAddClanPointProc(getGmroleid(), contri, YYLoggerTuJingEnum.GM,false, "gm添加").submit();

		return true;
	}

	@Override
	String usage() {
		return "factionContri contri";
	}

}
