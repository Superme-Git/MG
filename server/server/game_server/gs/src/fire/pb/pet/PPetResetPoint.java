package fire.pb.pet;

import java.util.Map;

import fire.pb.item.Pack;
import fire.pb.talk.MessageMgr;

/**
 * 宠物重置加点
 * @author XGM
 */
public class PPetResetPoint extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	
	public PPetResetPoint(final long roleId, final int petKey) {
		this.roleId = roleId;
		this.petKey = petKey;
	}

	@Override
	public boolean process() {

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet)
			return false;

		if(pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		// 消耗
		if (cost(pet.getPetInfo().getPointresetcount() + 1) == false)
			return false;

		// 增加重置次数
		pet.getPetInfo().setPointresetcount(pet.getPetInfo().getPointresetcount() + 1);

		// 重置
		final fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(roleId, petKey);
		java.util.Map<Integer, Float> res = epet.resetPoints();
		if (res != null) {
			// 发送重置加点后的宠物数据
			// 本来应该使用fire.pb.attr.SRefreshPetData协议,但是这个协议没有"宠物加点的重置次数"属性
			final fire.pb.pet.SRefreshPetInfo refresh = new fire.pb.pet.SRefreshPetInfo(pet.getProtocolPet());
			psendWhileCommit(roleId, refresh);

			// 提示重置成功
			MessageMgr.psendMsgNotify(roleId, 150044, null);
		}

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetResetPoint] roleId:" + roleId
					+ " petKey:" + petKey
					+ " uniqId:" + pet.getUniqueId()
					+ " petId:" + pet.getBaseId()
					+ " pointResetCount:" + pet.getPetInfo().getPointresetcount());
		}
		return true;
	}

	private boolean cost(int count) {

		int money = 0;
		Map<Integer, PetResetPointConfig> mapConfig = fire.pb.main.ConfigManager.getInstance().getConf(PetResetPointConfig.class);
		if (mapConfig != null) {
			for (PetResetPointConfig conf : mapConfig.values()) {
				money = conf.getCost();
				if (conf.getId() == count) {
					break;
				}
			}
		}

		if (money != 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.subMoney(-money, "Pet reset point", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0) != -money)
				return false;
		}

		return true;
	}

}