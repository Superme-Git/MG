package fire.pb.pet;

/**
 * 收回展示宠物
 */
public class PShowPetOffProc extends mkdb.Procedure {

	private final long roleId;

	public PShowPetOffProc(final long roleId) {
		this.roleId = roleId;
	}

	public boolean process() {

		final xbean.Properties prop = xtable.Properties.get(roleId);
		if (null == prop || prop.getShowpetkey() < 0)
			return false;

		prop.setShowpetkey(-1);

		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		petCol.removeShowSkillBuff();

		// 通知地图模块
		fire.msp.move.GNotifyMapPetInfo send = new fire.msp.move.GNotifyMapPetInfo();
		send.roleid = roleId;
		fire.pb.GsClient.pSendWhileCommit(send);
		return true;
	}
}
