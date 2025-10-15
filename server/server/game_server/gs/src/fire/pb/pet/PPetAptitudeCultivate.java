package fire.pb.pet;

import java.util.ArrayList;
import java.util.List;

import fire.log.YYLogger;
import fire.pb.attr.AttrType;
import fire.pb.common.SCommon;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.item.pet.PetAptItem;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.talk.STransChatMessageNotify2Client;
import fire.pb.util.Misc;

/**
 * 宠物资质培养
 * @author XGM
 */
public class PPetAptitudeCultivate extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	private final int aptId;
	private final int itemKey;

	public PPetAptitudeCultivate(final long roleId, final int petKey, final int aptId, final int itemKey) {
		this.roleId = roleId;
		this.petKey = petKey;
		this.aptId = aptId;
		this.itemKey = itemKey;
	}

	@Override
	public boolean process() {

		if (Helper.isPetInBattle(roleId, petKey))
			return false;

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet)
			return false;

		if (pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		// 使用次数限制
		int countLimit = getCountLimit();
		if (pet.getPetInfo().getAptaddcount() >= countLimit) {
			return true;
		}

		// 检查道具
		Pack bag = new Pack(roleId, false);
		ItemBase bi = bag.getItem(itemKey);
		if (bi == null) {
			return false;
		}
		if ((bi instanceof PetAptItem) == false) {
			return false;
		}

		int curValue = 0;
		int maxValue = 0;
		int addValue = 0;
		int finalValue = 0;
		switch (aptId) {
		case AttrType.PET_ATTACK_APT:
			curValue = pet.getPetInfo().getBornattackapt();
			maxValue = pet.getPetAttr().getAttackaptmax();
			break;
		case AttrType.PET_DEFEND_APT:
			curValue = pet.getPetInfo().getBorndefendapt();
			maxValue = pet.getPetAttr().getDefendaptmax();
			break;
		case AttrType.PET_MAGIC_APT:
			curValue = pet.getPetInfo().getBornmagicapt();
			maxValue = pet.getPetAttr().getMagicaptmax();
			break;
		case AttrType.PET_PHYFORCE_APT:
			curValue = pet.getPetInfo().getBornphyforceapt();
			maxValue = pet.getPetAttr().getPhyforceaptmax();
			break;
		case AttrType.PET_SPEED_APT:
			curValue = pet.getPetInfo().getBornspeedapt();
			maxValue = pet.getPetAttr().getSpeedaptmax();
			break;
		default:
			return false;
		}

		addValue = getAddAptValue(curValue, maxValue);
		finalValue = curValue + addValue;
		if (finalValue > maxValue) {
			finalValue = maxValue;
		}

		if (curValue >= finalValue) {
			return true;	// 达到上限
		}

		switch (aptId) {
		case AttrType.PET_ATTACK_APT:
			pet.getPetInfo().setBornattackapt(finalValue);
			break;
		case AttrType.PET_DEFEND_APT:
			pet.getPetInfo().setBorndefendapt(finalValue);
			break;
		case AttrType.PET_MAGIC_APT:
			pet.getPetInfo().setBornmagicapt(finalValue);
			break;
		case AttrType.PET_PHYFORCE_APT:
			pet.getPetInfo().setBornphyforceapt(finalValue);
			break;
		case AttrType.PET_SPEED_APT:
			pet.getPetInfo().setBornspeedapt(finalValue);
			break;
		default:
			return false;
		}

		// 次数+1
		pet.getPetInfo().setAptaddcount(pet.getPetInfo().getAptaddcount() + 1);
		//增加评分计算
		pet.updatePetScoreWhileChange();
		
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());

		// 删除道具
		int itemNum = 1;
		int num = bag.removeItemWithKey(itemKey, itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, bi.getItemId(), "Pet aptitude cultivate cost");
		if (num != itemNum) {
			return false;
		}

		// 刷新宠物信息
		final SRefreshPetInfo refresh = new SRefreshPetInfo(pet.getProtocolPet());
		psendWhileCommit(roleId, refresh);

		// 成功消息
		psendWhileCommit(roleId, new SPetAptitudeCultivate(petKey, aptId, finalValue));

		// 提示资质增加
		List<String> params = new ArrayList<String>();
		params.add(pet.getName());
		params.add(pet.getAptitudeStringByAttrType(aptId, false));
		params.add(((Integer)(addValue)).toString());
		final STransChatMessageNotify2Client msg = MessageMgr.getMsgNotify(150074, 0, params);
		psendWhileCommit(roleId, msg);

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetAptitudeCultivate] roleId:" + roleId
					+ " itemKey:" + itemKey
					+ " itemId:" + bi.getItemId()
					+ " itemName:" + bi.getName()
					+ " aptId:" + aptId
					+ " curValue:" + curValue
					+ " maxValue:" + maxValue
					+ " addValue:" + addValue
					+ " finalValue:" + finalValue
					+ " petInfo:" + Helper.toString(pet.getPetInfo()));
		}
		
		// 运营日志
		writeYYLogger(bi);
		return true;
	}

	/**
	 * 运营日志
	 * @param bi
	 */
	private void writeYYLogger(ItemBase bi) {
		if(bi == null) return;
		YYLogger.petTraLog(roleId, new fire.log.beans.OpPetTraBean(bi.getItemId(), 1, aptId));
	}

	/**
	 * 获取增加的资质值
	 * @param curValue 资质当前值
	 * @param maxValue 资质最大值
	 * @return 增加资质
	 */
	int getAddAptValue(int curValue, int maxValue)
	{
		int v1 = (int)((maxValue - curValue) * 0.04 + 0.5); // 四舍五入
		int v2 = (int)((maxValue - curValue) * 0.06 + 0.5);
		int r = Misc.getRandomBetween(v1, v2);
		return r > 0 ? r : 1;
	}

	/**
	 * 获取资质道具使用次数限制
	 * @return 次数限制
	 */
	int getCountLimit()
	{
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(120);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}
}