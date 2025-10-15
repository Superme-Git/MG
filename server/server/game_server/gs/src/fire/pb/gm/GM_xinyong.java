package fire.pb.gm;

import fire.pb.game.MoneyType;

public class GM_xinyong extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleid = getGmroleid();
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, true);
		long v = bag.getCurrency(MoneyType.MoneyType_EreditPoint);
		String info = "角色当前的信用值为:" + v;
		sendToGM(info);
		return true;
	}


	@Override
	String usage() {
		return null;
	}

}
