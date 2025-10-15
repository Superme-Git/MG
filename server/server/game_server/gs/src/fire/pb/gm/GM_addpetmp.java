package fire.pb.gm;

import fire.pb.pet.PAddPetMPProc;
import fire.pb.scene.Scene;

public class GM_addpetmp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final long mp = Long.parseLong(args[0]);
		if (mp == 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}

		final xbean.Properties prop = xtable.Properties.select(getGmroleid());
		if (prop.getFightpetkey() == -1) {
			sendToGM("您还没有参战宠物，请先设置参战宠物");
			return false;
		}

		PAddPetMPProc addPetMPProc = new PAddPetMPProc(getGmroleid(),
				prop.getFightpetkey(), mp);
		addPetMPProc.submit();
		Scene.LOG.info("GM 使用增加经验的GM指令给参战宠物增加法力" + "角色 " + getGmroleid()
				+ "加法力 " + mp);
		return true;
	}

	@Override
	String usage() {
		return "addemp addnumber, addnumber must larger than zero.";
	}

}
