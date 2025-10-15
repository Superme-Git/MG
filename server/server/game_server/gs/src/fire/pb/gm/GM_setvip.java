package fire.pb.gm;

import fire.pb.fushi.PAddVipExp;
import fire.pb.fushi.SVipInfoConfig;

public class GM_setvip extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		
		int viplevel = Integer.parseInt(args[0]);
		if (viplevel == 0)
			return true;
		
		SVipInfoConfig cfg = fire.pb.fushi.Module.getVipInfoConfig(viplevel);
		new fire.pb.fushi.PAddVipExp(getGmroleid(), cfg.exp, PAddVipExp.REASON_ADD_VIP_EXP_GM).submit();
		return true;
	}

	@Override
	String usage() {
		return "//setvip level";
	}

}
