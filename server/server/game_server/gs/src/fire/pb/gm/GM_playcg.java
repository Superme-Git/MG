package fire.pb.gm;

public class GM_playcg extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length != 1) {
			sendToGM("playcg指令需要一个动画的id!");
			return false;
		}
		int cgid = Integer.parseInt(args[0]);
		fire.pb.util.CGPlayUtil.sendStartCGProtocal(getGmroleid(), cgid);
		return true;
	}

	@Override
	String usage() {

		return null;
	}

}
