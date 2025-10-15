package fire.pb.pet.shenshou;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.RoleConfigManager;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemMaps;
import fire.pb.pet.Module;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.pet.PetManager;
import fire.pb.pet.PetTypeEnum;
import fire.pb.pet.SRefreshPetInfo;
import fire.pb.pet.SShenShouInc;
import fire.pb.talk.MessageMgr;

/**
 * 神兽养成 
 * author yebin @ 2016年3月10日
 */
public class PShenShouYangCheng extends Procedure {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	private final long roleId;
	private final int petKey;

	public PShenShouYangCheng(long roleId, int petKey) {
		this.roleId = roleId;
		this.petKey = petKey;
	}

	@Override
	protected boolean process() throws Exception {
		PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		fire.pb.pet.Pet pet = petCol.getPet(petKey);
		
		// 宠物不存在或者不是神兽
		if (pet == null || pet.getKind() != PetTypeEnum.SACREDANIMAL) {
			MessageMgr.sendMsgNotify(roleId, 162105, null);
			logger.error("神兽养成,petkey对应的宠物不符合要求或不存在");
			return false;
		}

		if(pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}
		
		final PetManager petManager = Module.getInstance().getPetManager();
		if (null == petManager) {
			return false;
		}

		int itemId = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(289).getValue());
		int needItemNum = Integer.parseInt(RoleConfigManager.getRoleCommonConfig(288).getValue());
		ItemMaps bagContainer = fire.pb.item.Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		if (bagContainer == null) {
			logger.error("角色id " + roleId + "神兽养成" + "\t背包错误");
			return false;
		}

		int curNum = bagContainer.getItemNum(itemId, 0);
		if (curNum < needItemNum) {
			MessageMgr.sendMsgNotify(roleId, 162094, null);
			return false;
		}

		xbean.PetInfo petInfo = petCol.getPetInfo(petKey);
		if(petInfo == null){
			return false;
		}
		
		int shenshouinccount = petInfo.getShenshouinccount();
		if(shenshouinccount >= 3){
			MessageMgr.sendMsgNotify(roleId, 162096, null);
			return false;
		}
		
		SShenShouInc attConf = petManager.getShenShouIncConfig(petInfo.getId(), shenshouinccount);
		if(attConf == null){
			MessageMgr.sendMsgNotify(roleId, 162107, null);
			logger.error("角色id " + roleId + "错误:神兽养成次数为" + shenshouinccount);
			return false;
		}
		
		if (petInfo.getLevel() < attConf.getInclv()) {
			return false;
		}
		
		// 保留自动加点方案
		petInfo.setGrowrate(petInfo.getGrowrate() + attConf.getAttinc()); // 成长率 		*short够了
		petInfo.setBornattackapt(petInfo.getBornattackapt() + attConf.getAtkinc()); // 出生时攻击资质	*short够了
		petInfo.setBorndefendapt(petInfo.getBorndefendapt() + attConf.getDefinc()); // 出生时防御资质	*short够了
		petInfo.setBornphyforceapt(petInfo.getBornphyforceapt() + attConf.getHpinc()); // 出生时体力资质	*short够了
		petInfo.setBornmagicapt(petInfo.getBornmagicapt() + attConf.getMpinc()); // 出生时法力资质	*short够了
		petInfo.setBornspeedapt(petInfo.getBornspeedapt() + attConf.getSpdinc()); // 出生时速度资质	*short够了
			
		// 次数增加
		petInfo.setShenshouinccount(shenshouinccount + 1);
		
		//增加评分计算
		pet.updatePetScoreWhileChange();
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());
		// 扣除道具
		int usedNum = bagContainer.removeItemById(itemId, needItemNum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shenshou, itemId,
				"重置神兽");
		if (usedNum != needItemNum) {
			return false;
		}
				
		// 刷新宠物信息
		final SRefreshPetInfo refresh = new SRefreshPetInfo(pet.getProtocolPet());
		psendWhileCommit(roleId, refresh);
		MessageMgr.sendMsgNotify(roleId, 162095, null);

		// 记录日志
		logger.error("角色id " + roleId + "养成神兽" + "\t扣除道具，物品id" + itemId + "数量" + needItemNum);
		return true;
	}
}
