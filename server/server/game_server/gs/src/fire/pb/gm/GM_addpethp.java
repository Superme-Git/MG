package fire.pb.gm;

import fire.pb.pet.PAddPetHPProc;
import fire.pb.scene.Scene;

public class GM_addpethp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long hp = Long.parseLong(args[0]);
		if (hp == 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}

		final xbean.Properties prop = xtable.Properties.select(getGmroleid());
		if (prop.getFightpetkey() == -1) {
			sendToGM("您还没有参战宠物，请先设置参战宠物");
			return false;
		}

		PAddPetHPProc addPetHPProc = new PAddPetHPProc(getGmroleid(),
				prop.getFightpetkey(), hp);
		addPetHPProc.submit();
		Scene.LOG
				.info("GM 给参战宠物增加生命。" + "角色： " + getGmroleid() + "；加生命 ：" + hp);

		return true;
	}

	@Override
	String usage() {
		return "addhp addnumber, addnumber must larger than zero.";
	}

}
