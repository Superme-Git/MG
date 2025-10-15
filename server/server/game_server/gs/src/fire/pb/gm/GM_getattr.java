package fire.pb.gm;

import fire.pb.PropRole;

public class GM_getattr extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		String ispet = args[0];
		if (ispet.equals("pet")) {
			if (args.length < 2) {
				sendToGM("参数格式错误：" + usage());
				return false;
			}
			int attrId = Integer.valueOf(args[1]);
			PropRole prole = new PropRole(getGmroleid(), true);
			int petId = prole.getFightpetkey();
			if (petId < 0)
				sendToGM("当前没有参战宠物");
			fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(
					getGmroleid(), petId, true);
			float attr = epet.getAttrById(attrId);
			sendToGM("当前宠物属性（id:" + attrId + ")的值为：" + attr);
		} else {
			int attrId = Integer.valueOf(args[0]);
			fire.pb.effect.Role erole = new fire.pb.effect.RoleImpl(
					getGmroleid(), true);
			float attr = erole.getAttrById(attrId);
			sendToGM("当前人物属性（id:" + attrId + ")的值为：" + attr);
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
