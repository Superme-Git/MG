package fire.pb.gm;

import fire.pb.XdbSelect;

public class GM_s extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("错误的参数:" + usage());
			return false;
		}

		XdbSelect select = new XdbSelect();
		sendToGM(select.select(args[0], args[1]));

		return select.isSuccess();
	}

	@Override
	String usage() {
		return "//key[keytype] tableName[.varName]";
	}

}
