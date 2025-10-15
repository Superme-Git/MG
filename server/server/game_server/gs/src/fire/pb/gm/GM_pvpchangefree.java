package fire.pb.gm;

public class GM_pvpchangefree extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		return true;
	}

	@Override
	String usage() {
		return "pvpchangefree baseFuShi addFushi";
	}

}
