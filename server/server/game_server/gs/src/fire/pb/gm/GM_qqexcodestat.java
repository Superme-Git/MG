package fire.pb.gm;

import fire.pb.activity.exchangecode.ExchangeCodeMgr;


public class GM_qqexcodestat extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if(args.length == 0){
			boolean isOpen = ExchangeCodeMgr.isQQExchangeCodeOpen();
			sendToGM("QQ会员兑换码状态:" + (isOpen ? "打开" : "关闭"));
			return true;
		}
		
		if (args.length != 1)
			return false;
		
		if(!args[0].equals("0") && !args[0].equals("1")){
			usage();
			return true;
		}

		boolean isOpen = args[0].equals("1");
		ExchangeCodeMgr.setQQExchangeCodeOpen(isOpen);
		sendToGM("QQ会员兑换码设置成功,状态:" + (isOpen ? "打开" : "关闭"));
		return true;
	}

	@Override
	String usage() {
		return "//qqcodestat [0,1]";
	}

}
