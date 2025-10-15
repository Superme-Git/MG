package fire.pb.gm;

public class GM_ws extends GMCommand {

	@Override
	boolean exec(String[] args) {
		sendToGM("无效的指令！"); // 这里以前是竞技场的指令,现在取消 [4/8/2016 XGM]
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
