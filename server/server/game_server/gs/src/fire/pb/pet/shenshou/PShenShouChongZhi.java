package fire.pb.pet.shenshou;

import java.util.Arrays;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.RoleConfigManager;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemMaps;
import fire.pb.item.ItemShuXing;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetManager;
import fire.pb.pet.PetTypeEnum;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;

/**
 * 神兽重置 author yebin @ 2016年3月10日
 */
public class PShenShouChongZhi extends Procedure {
	public static final Logger logger = Logger.getLogger("SYSTEM");

	private final long roleId;
	private final int petKey;
	private final int needPetId;

	public PShenShouChongZhi(long roleId, int petKey, int needPetId) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.needPetId = needPetId;
	}

	@Override
	protected boolean process() throws Exception {
		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		fire.pb.pet.Pet pet = petCol.getPet(petKey);

		// 宠物不存在或者不是神兽
		if (pet == null || pet.getKind() != PetTypeEnum.SACREDANIMAL) {
			MessageMgr.sendMsgNotify(roleId, 162104, null);
			logger.error("神兽重置,petkey对应的宠物不符合要求或不存在");
			return false;
		}

		final PetManager petManager = fire.pb.pet.Module.getInstance().getPetManager();
		if (null == petManager) {
			return false;
		}

		PetAttr petAttr = petManager.getAttr(needPetId);
		if (null == petAttr || petAttr.getKind() != PetTypeEnum.SACREDANIMAL || petAttr.getId() == pet.getPetAttr().getId()) {
			MessageMgr.sendMsgNotify(roleId, 162104, null);
			logger.error("神兽重置,needPetId对应的宠物不符合要求或不存在");
			return false;
		}

		fire.pb.PropRole pRole = new fire.pb.PropRole(roleId, true);
		if (pRole.getFightpetkey() == petKey) {
			MessageMgr.sendMsgNotify(roleId, 162108, null);
			logger.error("神兽重置,出战宠物不能重置");
			return false;
		}

		if (pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		if (petAttr.takelevel > pRole.getLevel()) {
			logger.error("神兽重置,兑换的目标神兽携带等级比人物高");
			return false;
		}

		int itemId = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(289).getValue());
		int needItemNum = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(287).getValue());
		ItemMaps bagContainer = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		if (bagContainer == null) {
			logger.error("角色id " + roleId + "神兽重置" + "\t背包错误");
			return false;
		}

		ItemShuXing itemShuXing = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
		if (itemShuXing == null) {
			logger.error("角色id " + roleId + "兑换神兽" + "\t道具错误");
			return false;
		}

		int curNum = bagContainer.getItemNum(itemId, 0);
		if (curNum < needItemNum) {
			MessageMgr.sendMsgNotify(roleId, 162110, Arrays.asList(itemShuXing.getName()));
			return false;
		}

		// 扣除道具
		int usedNum = bagContainer.removeItemById(itemId, needItemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenshoucost, itemId,
				"重置神兽");
		if (usedNum != needItemNum) {
			return false;
		}

		// 返还道具(养成过的所用的所有道具100%返还)
		if (pet.getPetInfo().getShenshouinccount() != 0) {
			int ycCostItemNum = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(288).getValue());
			int retItemNum = ycCostItemNum * pet.getPetInfo().getShenshouinccount();
			if (BagUtil.addItem(roleId, itemId, retItemNum, "重置神兽返还物品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenshou, itemId) != retItemNum) {
				logger.error("神兽重置,返还道具数量不正确");
				return false;
			}
			MessageMgr.sendMsgNotify(roleId, 162098,
					Arrays.asList(pet.getPetAttr().getName(), itemShuXing.getName(), Integer.toString(retItemNum)));
		}

		logger.error("角色id " + roleId + "重置神兽" + "\t扣除道具，物品id" + itemId + "数量" + needItemNum);

		if (petCol.removePet(pet.getPetkey(), PetColumn.REMOVE_REASON_SHENSHOUCHONGZHI) != 0) {
			return false;
		}

		if (petCol.addpet(petAttr.getId(), petAttr.getInitlevel(), petAttr.getKind(), null,
				PetColumn.ADD_REASON_SHENSHOUDUIHUAN, -1, false, null) < 0) {
			return false;
		}
		
		MessageMgr.sendMsgNotify(roleId, 162097, Arrays.asList(petAttr.getName()));
		MessageMgr.sendMsgNotify(roleId, 162109, Arrays.asList(petAttr.getName()));
		return true;
	}
}
