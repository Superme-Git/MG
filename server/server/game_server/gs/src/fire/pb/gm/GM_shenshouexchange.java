package fire.pb.gm;

import fire.pb.pet.shenshou.PShenShouDuiHuan;

public class GM_shenshouexchange extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new PShenShouDuiHuan(getGmroleid()).submit();
		sendToGM("兑换神兽成功");
		return true;
	}

	@Override
	String usage() {
		return "addpet petid level";
	}

}
