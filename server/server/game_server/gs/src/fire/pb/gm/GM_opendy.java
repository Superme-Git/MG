package fire.pb.gm;

import fire.pb.main.ConfigManager;
import fire.pb.state.StateManager;

public class GM_opendy extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length <= 0) {
			this.sendToGM("参数错误." + usage());
			return false;
		}
		int retvalue = Integer.parseInt(args[0]);
		if(retvalue > 0)
			ConfigManager.OPEN_DY = true;
		else
			ConfigManager.OPEN_DY = false;
		//MessageMgr.sendMsgToPop(getGmroleid(), "你已经更改当前服务器符石兑换点卡配置,配置=" + ConfigManager.FUSHI_2_DAYPAY);
		StateManager.logger.info("你已经更改当前服务器符石兑换点卡配置,配置=" + ConfigManager.FUSHI_2_DAYPAY);
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
