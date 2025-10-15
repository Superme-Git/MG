package fire.pb.gm;

import java.util.List;

public class GM_setroleai extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 0) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		List<Integer> ids = new java.util.ArrayList<Integer>();

		for (String arg : args)
			ids.add(Integer.parseInt(arg));

		new fire.pb.hook.PSetRoleFightAIProc(getGmroleid(), ids).submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
