package fire.pb.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.YYLogger;
import fire.pb.common.SCommon;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

/**
 * 宠物洗练
 * @author XGM
 */
public class PPetWashPorc extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	
	public PPetWashPorc(final long roleId, final int petKey) {
		this.roleId = roleId;
		this.petKey = petKey;
	}

	@Override
	public boolean process() {

		// 参战的宠物不能洗练
		Integer fightPetKey = xtable.Properties.selectFightpetkey(roleId);
		if (fightPetKey == petKey) {
			return false;
		}

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet)
			return false;

		if(pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		// 神兽不能洗练
		if (pet.getKind() == PetTypeEnum.SACREDANIMAL)
			return false;

		// 获取洗练后的新宠ID
		int newPetId = randomWashPetId(pet.getPetAttr());
		if (newPetId <= 0) {
			return false;
		}
		PetAttr newPetAttr = PetManager.getInstance().getAttr(newPetId);
		if (newPetAttr == null)
			return false; // 宠物配置表中没有这个宠物

		int itemId = pet.getPetAttr().getWashitemid();
		int itemNum = pet.getPetAttr().getWashitemnum();
		
		// 检查道具数量
		if (itemId > 0 && itemNum > 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.getBagItemNum(itemId) < itemNum)
				return false;

			// 删除洗练道具
			int num = bag.removeItemById(itemId, itemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_xilian, 0, "Pet wash cost");
			if (num != itemNum) {
				return false;
			}
		}

		// 洗练
		Map<Integer,Object> initAttrs = new HashMap<Integer, Object>();
		if (pet.getPetInfo().getName() != pet.getPetAttr().getName()) {
			initAttrs.put(CalcPetAttr.PET_NAME, pet.getPetInfo().getName());
		} else {
			initAttrs.put(CalcPetAttr.PET_NAME, newPetAttr.getName());
		}

		int color = PetColour.WHITE; 

		int oldKind = pet.getPetAttr().getKind();
		int newKind = newPetAttr.getKind();
		xbean.PetInfo newPetInfo = PetColumn.createPet(roleId, newPetId, 0, null, newKind, color, pet.isBind(), initAttrs, pet.getUniqueId());
		newPetInfo.setKey(pet.getPetInfo().getKey());

		petCol.getPetsMap().put(newPetInfo.getKey(), newPetInfo);
		Pet newPet = petCol.getPet(newPetInfo.getKey());
		if (newPet == null)
			return false;

		// 资质规则:
		final CalcPetAttr cattr = new CalcPetAttr(newPetInfo);
		cattr.setBornAttackApt(randomAptitude(cattr.getPetAttr().getAttackaptmin(), cattr.getPetAttr().getAttackaptmax()));
		cattr.setBornDefendApt(randomAptitude(cattr.getPetAttr().getDefendaptmin(), cattr.getPetAttr().getDefendaptmax()));
		cattr.setBornMagicApt(randomAptitude(cattr.getPetAttr().getMagicaptmin(), cattr.getPetAttr().getMagicaptmax()));
		cattr.setBornPhyforceApt(randomAptitude(cattr.getPetAttr().getPhyforceaptmin(), cattr.getPetAttr().getPhyforceaptmax()));
		cattr.setBornDodgeApt(randomAptitude(0, 0));
		cattr.setBornSpeedApt(randomAptitude(cattr.getPetAttr().getSpeedaptmin(), cattr.getPetAttr().getSpeedaptmax()));

		// 清空加点方案
		newPetInfo.setAutoaddcons(0);
		newPetInfo.setAutoaddiq(0);
		newPetInfo.setAutoaddstr(0);
		newPetInfo.setAutoaddendu(0);
		newPetInfo.setAutoaddagi(0);

		// 洗练后的宠物如果有染色方案保留
		if (pet.getPetInfo().getPetdye1() > 0) {
			newPet.getPetInfo().setPetdye1(pet.getPetInfo().getPetdye1());
			newPet.getPetInfo().setPetdye2(pet.getPetInfo().getPetdye2());
		}

		// 洗练的宠物是宝宝或者变异时
		// 洗练次数+1
		if (oldKind == PetTypeEnum.BABY || oldKind == PetTypeEnum.VARIATION) {
			// 同类型的洗练次数才累计,不同类型洗练次数清空
			if (oldKind == newKind) {
				newPetInfo.setWashcount(pet.getPetInfo().getWashcount() + 1);
			}
			// 洗练达指定次数给满技能
			int maxWashCount = newPet.getPetAttr().getWashcount();
			if (maxWashCount > 0 && newPetInfo.getWashcount() >= maxWashCount) {
				newPetInfo.getSkills().clear();
				for (SkillRate sr : newPet.getPetAttr().getSkills()) {
					if (sr.skillid > 0) {
						newPet.addSkill(sr.skillid, -1, 0, 0);
					}
				}
				// 洗满后重置次数
				newPetInfo.setWashcount(0);
			}
		}
		
		newPet.updatePetScoreWhileChange();
		
		if (newPet.getPetInfo().getTreasure() == 1) {
			fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.XI_LIAN_PET);
		}
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, newPet.getPetInfo().getPetscore());

		fire.pb.pet.SRefreshPetInfo send = new fire.pb.pet.SRefreshPetInfo(newPet.getProtocolPet());
		mkdb.Procedure.psendWhileCommit(roleId, send);

		mkdb.Procedure.psendWhileCommit(roleId, new SPetWash(newPet.getPetkey()));

		// 公告洗出变异宠
		if (oldKind != newKind && newKind == PetTypeEnum.VARIATION) {
			String roleName = xtable.Properties.selectRolename(roleId);
			MessageMgr.sendSystemMsg(160484, MessageMgr.getStringList(roleName, newPetAttr.getName()));
			
			fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.WASH_PET);
		}

		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.PetColumnChange(roleId, newPetInfo.getId()));
		// 运营日志
		onLog(roleId, newPetInfo, newPetAttr, pet, itemId);
		
		return true;
	}

	/**
	 * 获取随机的宠物ID
	 * @param petAttr 宠物配置
	 * @return 随机的宠物ID
	 */
	public int randomWashPetId(PetAttr petAttr)
	{
		// 500220@980;500261@20
		// ID@几率
		String[] petList = petAttr.getWashnewpetid().split(";");
		if (petList.length > 0) {
			// ID和几率
			List<Integer> idList = new ArrayList<Integer>();
			List<Integer> rateList = new ArrayList<Integer>();
			// 总几率
			int sumValue = 0;
			for (int i = 0; i < petList.length; i++) {
				String[] pet = petList[i].split("@");
				if (pet.length == 2) {
					// ID和几率
					int id = Integer.parseInt(pet[0]);
					int rate = Integer.parseInt(pet[1]);
					idList.add(id);
					rateList.add(rate);
					// 总几率
					sumValue += rate;
				} else {
					return 0;
				}
			}

			// 随机一个ID
			int r = fire.pb.util.Misc.getRandomBetween(1, sumValue);
			int v = 0;
			for (int i = 0; i < idList.size() && i < rateList.size(); i++) {
				v += rateList.get(i);
				if (v >= r) {
					return idList.get(i);
				}
			}
		}

		return 0;
	}

	static Double getCommonConf(int id)
	{
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(id);
			if (conf != null) {
				return Double.parseDouble(conf.getValue());
			}
		}
		return null;
	}

	/**
	 * 随机资质
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public int randomAptitude(int minValue, int maxValue)
	{
		if (minValue > maxValue)
			return 0;

		Double p1 = getCommonConf(268);
		if (p1 == null)
			p1 = 0.4;

		// 最小资质放大一些
		int value =  (int) (minValue + (maxValue - minValue) * p1);
		return fire.pb.util.Misc.getRandomBetween(value, maxValue);
	}

	public void onLog(long roleId, xbean.PetInfo petInfo, PetAttr petAttr, Pet pet, int itemId)
	{
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetWashPorc] roleId:" + roleId
					+ " petInfo:" + Helper.toString(petInfo));
		}
		
		// 运营日志
		writeYYLogger(roleId, petInfo, petAttr, pet, itemId);
	}

	/**
	 * 运营日志
	 * @param roleId
	 * @param petInfo
	 * @param petAttr
	 * @param pet
	 * @param itemId
	 */
	private void writeYYLogger(long roleId, xbean.PetInfo petInfo, PetAttr petAttr, Pet pet, int itemId) {
		if(petInfo == null || petAttr == null || pet == null) return;
		
		int isTrea = petAttr.getTreasureScore() > petInfo.getPetscore() ? 0 : 1;
		YYLogger.petRefLog(roleId, petAttr.getId(), isTrea, pet.getUniqueId(), itemId);
	}
}
