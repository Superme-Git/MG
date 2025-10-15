package cn.com.zc.db.util;


public class PetScanerUtil {
	
	/**
	 * 计算宠物的评分
	 * 
	 * @param roleid
	 * @param petkey 宠物栏中的宠物,如果不是，则返回-1
	 * @return
	 */

	/*public static int calcPetScore(xbean.PetInfo petInfo){
		double score = 0;
		
		List<PetScoreConfig> petConfig = DbxManager.getInstance().getPetScoreConfig();
		for(PetScoreConfig config : petConfig){
			double attrValue = getCurrentAttrValue(petInfo, config);
			score += attrValue * config.getWeight();
		}
		
		return (int) score;
	}*/



//	private static double getCurrentAttrValue(PetInfo petInfo, PetScoreConfig current) {
//		//Pet pet = Pet.getPet(petInfo);
//		switch (current.getAttrid()) {
//		case AttrType.PET_ATTACK_APT:
//			return petInfo.getAttackapt();
//
//		case AttrType.PET_DEFEND_APT:
//			return petInfo.getDefendapt();
//
//		case AttrType.PET_PHYFORCE_APT:
//			return petInfo.getPhyforceapt();
//
//		case AttrType.PET_SPEED_APT:
//			return petInfo.getSpeedapt();
//
//		case AttrType.PET_MAGIC_APT:
//			return petInfo.getMagicapt();
//
//		case AttrType.PET_DODGE_APT:
//			return petInfo.getDodgeapt();
//
//		case AttrType.PET_GROW_RATE:
//			return ((double)petInfo.getGrowrate())/1000;
//
//		case AttrType.PET_FIGHT_LEVEL:
//			PetAttr petattr = Module.getInstance().getPetManager().getAttr(petInfo.getId());
//			if(petattr == null)
//				return 0;
//			return petattr.getUselevel();
//
//		case AttrType.PET_XUEMAI:
//			return petInfo.getXuemai();
//
//		case AttrType.PET_XUEMAI_MAX:
//			return petInfo.getXuemai();
//
//		case AttrType.PET_LOW_SKILL:
//			return getSkillNumByType(petInfo, AttrType.PET_LOW_SKILL);
//
//		case AttrType.PET_HIGH_SKILL:
//			return getSkillNumByType(petInfo, AttrType.PET_HIGH_SKILL);
//
//		case AttrType.PET_SUPER_SKILL:
//			return getSkillNumByType(petInfo, AttrType.PET_SUPER_SKILL);
//
//
//		default:
//			break;
//		}
//		return 0;
//	}


//	private static int getSkillNumByType(PetInfo petInfo, int skillType) {
//		int skillNum = 0;
//		Pet pet = Pet.getPet(petInfo);
//		List<Integer> skills = pet.getBattleskillIds();
//		
//		for(int skill : skills){
//			int currentType = 0;
//			if(skill % 10 == 1){
//				currentType = AttrType.PET_LOW_SKILL;
//			}else if(skill % 10 == 2){
//				currentType = AttrType.PET_HIGH_SKILL;
//			}else if(skill % 10 == 3){
//				currentType = AttrType.PET_SUPER_SKILL;
//			}
//			
//			if(skillType == currentType)
//				skillNum ++;
//			
//		}
//		
//		return skillNum;
//	}

	
	
}
