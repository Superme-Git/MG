package fire.pb.pet;

import java.util.HashSet;
import java.util.Set;

import com.locojoy.base.Marshal.OctetsStream;

public class PRecoverPet extends mkdb.Procedure {

	private long roleId;
	private final long uniqueId;
	public String resultInfo;

	public PRecoverPet(long roleId, long uniqueId) {
		this.roleId = roleId;
		this.uniqueId = uniqueId;
	}

	@Override
	protected boolean process() throws Exception {

		// 宠物换主人时,则把原主人的宠物可回收key删除
		long oldOwnerId = xtable.Petrecyclebin.selectRoleid(uniqueId);
		if (roleId <= 0) {
			roleId = oldOwnerId;
		}

		Set<Long> roleIds = new HashSet<Long>();
		roleIds.add(roleId);
		if (roleId != oldOwnerId) {
			roleIds.add(oldOwnerId);
		}
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleIds));

		xbean.Petrecoverlist petRecoverList = xtable.Petrecover.get(oldOwnerId);
		if (petRecoverList == null) {
			resultInfo = "roleId:" + roleId + " Pet recover not found!";
			return false;
		}

		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null) {
			resultInfo = "roleId:" + roleId + " Properties not found!";
			return false;
		}

		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);

		// add to Uniquepets
		xbean.UniquePet upet = xbean.Pod.newUniquePet();
		upet.setRoleid(roleId);
		xtable.Uniquepets.add(uniqueId, upet);

		xbean.DiscardPet discardPet = xtable.Petrecyclebin.get(uniqueId);
		if (discardPet == null) {
			resultInfo = "uniqueId:" + uniqueId + " Recycle bin not found!";
			return false;
		}

		xbean.PetInfo petInfo = xbean.Pod.newPetInfo();
		petInfo.unmarshal(discardPet.getPet().marshal(new OctetsStream()));
		petInfo.setOwnerid(roleId);

		String petInfoString = "petId:" + petInfo.getId()
				+ " uniqId:" + petInfo.getUniqid()
				+ " petName:" + petInfo.getName()
				+ " petLevel:" + petInfo.getLevel();

		int petKey = petCol.add(petInfo, PetColumn.ADD_REASON_MOVE_BETWEEN_BAGS);
		if (petKey == -1) {
			resultInfo = petInfoString + " revert to roleId:" + roleId + " faild.";
			return false;
		}

		// recycle bin delete
		xtable.Petrecyclebin.remove(uniqueId);

		petRecoverList.getUniqids().remove(Long.valueOf(uniqueId));

		resultInfo = petInfoString + " old roleId:" + discardPet.getRoleid()
				+ " revert to new roleId:" + roleId + " success.";

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PRecoverPet] roleId:" + roleId
					+ " petInfo:" + Helper.toString(petInfo));
		}
		return true;
	}
}
