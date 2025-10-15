package fire.pb.gm;

import fire.pb.pet.PRecoverPet;

public class GM_recoverpet extends GMCommand {

	@Override
	boolean exec(String[] args) {

		Long uniqid = Long.valueOf(args[0]);
		xbean.DiscardPet discardpet = xtable.Petrecyclebin.select(uniqid);

		long roleId = 0;
		if (args.length > 1)
			roleId = Long.valueOf(args[1]);

		if (discardpet == null) {
			sendToGM("从回收站恢复宠物失败，回收站中已经没有该宠物，uniqid = " + uniqid);
			return false;
		}
		try {
			PRecoverPet p = new PRecoverPet(roleId, uniqid);
			p.submit().get();
			sendToGM(p.resultInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	String usage() {
		return "recoverpet [petuniqid]";
	}

}
