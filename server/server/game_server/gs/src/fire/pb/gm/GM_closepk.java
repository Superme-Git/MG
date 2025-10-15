package fire.pb.gm;

import fire.pb.battle.PPlayPKFightView;

public class GM_closepk extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int value=Integer.parseInt(args[0]);//0开启   1关闭功能
		if(value!=0&&value!=1){
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		PPlayPKFightView.openflag=value;
		return true;
	}

	@Override
	String usage() {
		return "openfun funtype value";
	}

}
