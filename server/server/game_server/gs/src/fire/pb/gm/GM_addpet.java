package fire.pb.gm;

import java.util.concurrent.ExecutionException;

import fire.pb.DataInit;
import fire.pb.pet.PAddPetByPetID;
import fire.pb.pet.PetColour;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetTypeEnum;

public class GM_addpet extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		int index = 0;
		final Integer petid;
		final long roleid;
		Integer id2 = null;
		try {
			id2 = Integer.parseInt(args[index]);
		} catch (java.lang.NumberFormatException e) {
			id2 = getObjectIdByName(args[index]);
		}

		if (id2 == null)
			return false;

		petid = id2;

		int pettype = PetTypeEnum.BABY;
		int level = 0;
		++index;
		if (args.length > index) {
			final int ll = Integer.parseInt(args[index]);
			if (ll > 0 && ll <= DataInit.PET_LEVEL_MAX)
				level = ll;
		}

		++index;
		if (args.length > index)
			roleid = Long.valueOf(args[index]);
		else
			roleid = getGmroleid();

		int starId = -1;

		if (!fire.pb.StateCommon.isOnline(roleid))
			return false;

		final fire.pb.pet.Module petmodule = (fire.pb.pet.Module) fire.pb.main.ModuleManager
				.getInstance().getModuleByName("pet");
		if (null != petmodule) {
			fire.pb.pet.PetManager petm = petmodule.getPetManager();
			if (null != petm) {
				fire.pb.pet.PetAttr petattr = petm.getAttr(petid);
				if (null == petattr) {
					sendToGM("宠物配置表中 并没有此ID对应的宠物~");
					return false;
				}

				fire.pb.PropRole prole = new fire.pb.PropRole(roleid, true);
				if (petattr.takelevel > prole.getLevel()) {
					sendToGM("此宠物的携带等级为" + petattr.getTakelevel()
							+ ", 你的等级不足，不能获得此宠物");
					return false;
				}

				pettype = petattr.getKind();
				starId = PetColour.WHITE;
				final PAddPetByPetID addpetproc = new PAddPetByPetID(roleid,
						petid, level, PetColumnTypes.PET, pettype,
						PetColumn.ADD_REASON_GM, starId, false);
				try {
					addpetproc.submit().get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	String usage() {
		return "addpet petid level";
	}

}
