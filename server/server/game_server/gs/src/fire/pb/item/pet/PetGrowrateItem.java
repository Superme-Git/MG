package fire.pb.item.pet;

import java.util.Map;

import fire.log.YYLogger;
import fire.pb.common.SCommon;
import fire.pb.item.Commontext;
import fire.pb.item.ItemMgr;
import fire.pb.item.PetItem;
import fire.pb.item.Commontext.UseResult;
import fire.pb.main.ConfigManager;
import fire.pb.pet.Helper;
import fire.pb.pet.Pet;
import fire.pb.pet.PetAttr;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.util.Misc;


public class PetGrowrateItem extends PetItem {
	public PetGrowrateItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}

	public PetGrowrateItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}

	@Override
	public UseResult appendToPet(int petkey, int num) {

		if (Helper.isPetInBattle(getOwnerid(), petkey))
			return Commontext.UseResult.FAIL;

		PetColumn petcol = new PetColumn(getOwnerid(), PetColumnTypes.PET, false);
		Pet pet = petcol.getPet(petkey);
		if(pet == null)
			return Commontext.UseResult.FAIL;

		// 使用次数限制
		int countLimit = getGrowrateAddCountLimit();
		if (pet.getPetInfo().getGrowrateaddcount() >= countLimit) {
			return Commontext.UseResult.FAIL;
		}

		// 添加成长
		int curValue = pet.getPetInfo().getGrowrate();
		int maxValue = getGrowrateMaxValue(pet.getBaseId());
		int addValue = getAddGrowrateValue(curValue, maxValue);
		int finalValue = curValue + addValue;
		if (finalValue > maxValue) {
			finalValue = maxValue;
		}

		if (curValue >= finalValue) {
			return Commontext.UseResult.FAIL; // 达到最大值
		}

		pet.getPetInfo().setGrowrate(finalValue);

		// 次数+1
		pet.getPetInfo().setGrowrateaddcount(pet.getPetInfo().getGrowrateaddcount() + 1);

		fire.pb.pet.SRefreshPetInfo send = new fire.pb.pet.SRefreshPetInfo(pet.getProtocolPet());
		mkdb.Procedure.psendWhileCommit(roleid, send);

		// 更新评分
		pet.updatePetScoreWhileChange();
		fire.pb.course.CourseManager.checkAchieveCourse(roleid, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());

		// 记录日志
		if (fire.pb.pet.Module.logger.isInfoEnabled()) {
			fire.pb.pet.Module.logger.info("[PetGrowrateItem] roleId:" + roleid
					+ " petKey:" + petkey
					+ " uniqId:" + pet.getUniqueId()
					+ " petId:" + pet.getBaseId()
					+ " itemId:" + getItemId()
					+ " ItemName:" + getName()
					+ " itemNum:" + num
					+ " curValue:" + curValue
					+ " maxValue:" + maxValue
					+ " addValue:" + addValue
					+ " finalValue:" + finalValue
					+ " petInfo:" + fire.pb.pet.Helper.toString(pet.getPetInfo()));
		}
		
		// 运营日志
		writeYYLogger(num);
		return Commontext.UseResult.SUCC;
	}

	private void writeYYLogger(int num){
		YYLogger.petTraLog(roleid, new fire.log.beans.OpPetTraBean(getItemId(), num, fire.pb.attr.AttrType.PET_GROW_RATE));
	}
	
	static int getAddGrowrateValue(int curValue, int maxValue)
	{
		int v1 = (int)((maxValue - curValue) * 0.05 + 0.5); // 四舍五入
		int v2 = (int)((maxValue - curValue) * 0.10 + 0.5);
		int r = Misc.getRandomBetween(v1, v2);
		return r > 0 ? r : 1;
	}

	
	static int getGrowrateMaxValue(int petId)
	{
		int maxValue = 0;
		Map<Integer, PetAttr> confs = ConfigManager.getInstance().getConf(PetAttr.class);
		if (confs != null) {
			PetAttr conf = confs.get(petId);
			if (conf != null) {
				for (Integer v : conf.getGrowrate()) {
					if (v > maxValue) {
						maxValue = v;
					}
				}
			}
		}
		return maxValue;
	}

	
	static int getGrowrateAddCountLimit()
	{
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(121);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 0;
	}

}