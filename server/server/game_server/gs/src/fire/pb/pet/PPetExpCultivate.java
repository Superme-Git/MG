package fire.pb.pet;

import fire.log.YYLogger;
import fire.pb.attr.AttrType;
import fire.pb.item.ItemIdToClassString;
import fire.pb.item.Pack;
import fire.pb.item.PetItemShuXing;
import fire.pb.item.pet.PetExpItem;

/**
 * 宠物经验培养
 * 
 * @author XGM
 */
public class PPetExpCultivate extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	private final int itemId;
	private final int itemNum;

	public PPetExpCultivate(final long roleId, final int petKey, final int itemId, final int itemNum) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.itemId = itemId;
		this.itemNum = itemNum;
	}

	@Override
	public boolean process() {

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet)
			return false;

		String itemClassName = ItemIdToClassString.getInstance().getItemClass(itemId);
		if (PetExpItem.class.getName().equals(itemClassName) == false) {
			return false; // 不是宠物经验药剂
		}

		// 获取可增加的经验
		int addExp = getItemAddExp(itemId) * itemNum;
		if (addExp <= 0) {
			return true;
		}

		// 检查道具数量
		Pack bag = new Pack(roleId, false);
		if (bag.getBagItemNum(itemId) < itemNum) {
			return false;
		}

		// 处理添加经验
		PAddPetExpProc addExpProc = new PAddPetExpProc(roleId, petKey, addExp, false, PAddPetExpProc.ADD_REASON_EXPCULTIVATE);
		if (addExpProc.call() == false) {
			return false;
		}

		// 删除道具
		int num = bag.removeItemById(itemId, itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0, "Pet exp cultivate cost");
		if (num != itemNum) {
			return false;
		}

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetExpCultivate] roleId:" + roleId + " itemId:" + itemId + " itemNum:" + itemNum + " addExp:" + addExp + " petInfo:" + Helper.toString(pet.getPetInfo()));
		}

		// 运营日志
		writeYYLogger();
		return true;
	}

	/**
	 * 运营日志
	 */
	private void writeYYLogger() {
		YYLogger.petTraLog(roleId, new fire.log.beans.OpPetTraBean(itemId, itemNum, AttrType.EXP));
	}

	/**
	 * 获取道具增加的经验
	 * 
	 * @param itemId
	 * @return 可增加的经验
	 */
	static public int getItemAddExp(int itemId) {
		PetItemShuXing conf = (PetItemShuXing) fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
		if (conf != null) {
			return conf.getAddExp();
		}
		return 0;
	}
}