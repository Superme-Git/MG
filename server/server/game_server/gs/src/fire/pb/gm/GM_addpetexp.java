package fire.pb.gm;

import fire.pb.pet.PAddPetExpProc;
import fire.pb.scene.Scene;

public class GM_addpetexp extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final long exp = Long.parseLong(args[0]);
		if (exp == 0) {
			sendToGM("参数格式错误:" + usage());
			return false;
		}
		final long roleid;
		if (args.length >= 2)
			roleid = Long.valueOf(args[1]);
		else
			roleid = getGmroleid();

		if (!fire.pb.StateCommon.isOnline(roleid))
			return false;

		final xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop.getFightpetkey() == -1) {
			sendToGM("您还没有参战宠物，请先设置参战宠物");
			return false;
		}

		PAddPetExpProc addexpproc = new PAddPetExpProc(roleid,
				prop.getFightpetkey(), exp);
		addexpproc.submit();
		Scene.LOG.info("GM 使用增加经验的GM指令给参战宠物增加经验" + "角色 " + roleid + "加经验 "
				+ exp);
		return true;
	}

	@Override
	String usage() {
		return "addpetexp num,  num != 0";
	}

}
