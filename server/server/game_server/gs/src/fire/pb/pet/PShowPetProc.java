package fire.pb.pet;

/**
 * 展示宠物
 */
public class PShowPetProc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;

	PShowPetProc(final long roleId, final int petKey) {
		this.roleId = roleId;
		this.petKey = petKey;
	}

	public boolean process() {

		final xbean.Properties prop = xtable.Properties.get(roleId);
		if (null == prop)
			return false;

		if (prop.getShowpetkey() == petKey)
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		Pet pet = petCol.getPet(petKey);
		if (pet == null)
			return false;

		petCol.removeShowSkillBuff(); // delete
		prop.setShowpetkey(petKey);
		petCol.addShowSkillBuff(); // add

		// 通知地图模块展示宠物
		fire.msp.move.GNotifyMapPetInfo send = new fire.msp.move.GNotifyMapPetInfo(roleId, pet.getShowPetInfo());
		fire.pb.GsClient.pSendWhileCommit(send);
		return true;
	}
}
