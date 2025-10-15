package fire.pb.gm;

import fire.pb.DataInit;
import fire.pb.attr.AttrType;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumnTypes;
import fire.pb.scene.Scene;

public class GM_addlife extends GMCommand {
	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("为参战宠物增加或者减少寿命,参数格式错误：" + usage());
			return false;
		}
		try {
			final int addnum = Integer.parseInt(args[0]);
			if (addnum == 0) {
				sendToGM("为参战宠物增加或者减少寿命,参数格式错误" + usage());
				return false;
			}
			final xbean.Properties prop = xtable.Properties
					.select(getGmroleid());
			if (prop.getFightpetkey() == -1) {
				sendToGM("您还没有参战宠物，请先设置参战宠物,再使用此gm指令");
				return false;
			}
			new mkdb.Procedure() {
				@Override
				public boolean process() {
					final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(
							getGmroleid(), PetColumnTypes.PET, false);

					Pet pet = petcol.getPet(prop.getFightpetkey());
					if (pet == null)
						return false;
					int realadd = addnum;
					if (addnum > DataInit.PET_MAX_LIFE)
						realadd = DataInit.PET_MAX_LIFE;
					pet.addLife(realadd);
					final fire.pb.attr.SRefreshPetData send = new fire.pb.attr.SRefreshPetData();
					send.columnid = PetColumnTypes.PET;
					send.petkey = prop.getFightpetkey();
					send.datas.put(AttrType.PET_LIFE, (float) pet.getLife());
					mkdb.Procedure.psendWhileCommit(getGmroleid(), send);
					return true;
				}
			}.submit();
			Scene.LOG.info("GM 使用增加经验的GM指令给参战宠物"
					+ (addnum > 0 ? "增加寿命" + addnum : "减少寿命" + (-addnum))
					+ "角色 " + getGmroleid());
		} catch (Exception e) {
			sendToGM("为参战宠物增加或者减少寿命,参数格式错误" + usage());
		}
		return true;
	}

	@Override
	String usage() {
		return "//addlife addnum ";
	}
}
