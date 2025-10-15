package fire.pb.gm;

import fire.log.Module;

public class GM_addword extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1)
			return false;
		for (String str : args) {
			Module.addNormalValidWord(str);
		}
		sendToGM("共添加了" + args.length + "个屏蔽字");
		return true;
	}

	@Override
	String usage() {

		return null;
	}

	public static void main(String[] args) {

	}

}
