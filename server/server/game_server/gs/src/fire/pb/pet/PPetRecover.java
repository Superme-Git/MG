package fire.pb.pet;

import mkdb.Procedure;

import com.locojoy.base.Marshal.OctetsStream;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.Pack;

/**
 * 宠物找回
 * @author XGM
 */
public class PPetRecover extends Procedure {

	public final long roleId;
	public final long uniqId; // 唯一id

	public PPetRecover(long roleId, long uniqId) {
		this.roleId = roleId;
		this.uniqId = uniqId;
	}

	@Override
	protected boolean process() throws Exception {

		// 获得可回收uniqId列表
		xbean.Petrecoverlist petRecoverList = xtable.Petrecover.get(roleId);
		if (petRecoverList == null) {
			return false;
		}

		// 是否存在可回收的uniqId
		if (petRecoverList.getUniqids().contains(uniqId) == false) {
			return false;
		}

		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);

		// add to Uniquepets
		xbean.UniquePet upet = xbean.Pod.newUniquePet();
		upet.setRoleid(roleId);
		xtable.Uniquepets.add(uniqId, upet);

		// 获得丢弃的宠物
		xbean.DiscardPet discardPet = xtable.Petrecyclebin.get(uniqId);
		if (discardPet == null) {
			return false;
		}

		// 获得宠物配置
		PetAttr petAttrConf = Module.getInstance().getPetManager().getAttr(discardPet.getPet().getId());
		if (petAttrConf == null) {
			return false;
		}

		// 金币消耗
		int cost = petAttrConf.getRecovercost() * -1;
		Pack bag = new Pack(roleId, false);
		if (cost != bag.subGold(cost, "宠物找回", YYLoggerTuJingEnum.tujing_Value_petrecovercost, 0)) {
			return false;
		}

		xbean.PetInfo petInfo = xbean.Pod.newPetInfo();
		petInfo.unmarshal(discardPet.getPet().marshal(new OctetsStream()));
		petInfo.setOwnerid(roleId);

		int petKey = petCol.add(petInfo, PetColumn.ADD_REASON_MOVE_BETWEEN_BAGS);
		if (petKey == -1) {
			return false;
		}

		// recycle bin delete
		xtable.Petrecyclebin.remove(uniqId);

		// remove uniqId
		petRecoverList.getUniqids().remove(Long.valueOf(uniqId));

		// 通知客户端
		SPetRecover send = new SPetRecover();
		send.petid = petInfo.getId();
		send.uniqid = uniqId;
		mkdb.Procedure.psendWhileCommit(roleId, send);

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetRecover] roleId:" + roleId
					+ " petInfo:" + Helper.toString(petInfo));
		}
		return true;
	}
}
