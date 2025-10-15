package fire.pb.pet;

import java.text.SimpleDateFormat;
import java.util.Map;

import fire.pb.buff.BuffConstant;
import fire.pb.item.Pack;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import fire.pb.util.DateValidate;

/**
 * 宠物出售
 * @author XGM
 */
public class PPetSell extends mkdb.Procedure {

	private final long roleId;
	private final int petKey;
	
	public PPetSell(final long roleId, final int petKey) {
		this.roleId = roleId;
		this.petKey = petKey;
	}

	@Override
	public boolean process() {

		if (fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;

		Integer fightPetKey = xtable.Properties.selectFightpetkey(roleId);
		if (fightPetKey == petKey) {
			return false;
		}

		final PetColumn petCol = new PetColumn(roleId, PetColumnTypes.PET, false);
		final Pet pet = petCol.getPet(petKey);
		if (null == pet) {
			return false;
		}
		//道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(roleId, pet.getPetInfo())) {
			return false;
		}
		if (pet.isLocked() != -1) {
			MessageMgr.psendMsgNotify(roleId, Pet.PET_LOCK_ERROR_MSG, null);
			return true;
		}

		// 只能操作野宠
		if (pet.getKind() != PetTypeEnum.WILD) {
			return false;
		}

		// 获取每日限制的存储数据
		xbean.PetSellCount petSellCount = xtable.Rolepetsellcount.get(roleId);
		if (petSellCount == null) {
			petSellCount = xbean.Pod.newPetSellCount();
			xtable.Rolepetsellcount.insert(roleId, petSellCount);
		}

		// 检测每日限制次数
		if (checkCount(petSellCount) == false) {
			MessageMgr.psendMsgNotify(roleId, 160405, null);
			return false;
		}
		
		// 奖励
		if (award(pet.getBaseId()) == false) {
			return false;
		}

		// 增加计数
		addCount(petSellCount);

		if (petCol.removePet(pet.getPetkey(), PetColumn.REMOVE_REASON_SELL) != 0) {
			return false;
		}

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			long resetTime = petSellCount.getResettime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTime = (resetTime > 0 ? dateFormat.format(resetTime) : "0");
			Module.logger.info("[PPetSell] roleId:" + roleId
					+ " petKey:" + petKey
					+ " uniqId:" + pet.getUniqueId()
					+ " petId:" + pet.getBaseId()
					+ " petSellCount:" + petSellCount.getCount()
					+ " resetTime:" + strTime);
		}
		//清除数据
     	GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
		return true;
	}

	/**
	 * 检测每日限制次数
	 * @param petSellCount
	 * @return
	 */
	private boolean checkCount(xbean.PetSellCount petSellCount) {

		// 隔天重置
		long now = System.currentTimeMillis();
		if (DateValidate.inTheSameDay(now, petSellCount.getResettime()) == false) {
			petSellCount.setCount(0);
			petSellCount.setResettime(now);
		}

		// 每日限制10次
		if (petSellCount.getCount() >= 10) {
			return false;
		}
		return true;
	}

	/**
	 * 增加每日次数 
	 * @param petSellCount
	 */
	private void addCount(xbean.PetSellCount petSellCount) {
		petSellCount.setCount(petSellCount.getCount() + 1);
	}

	private boolean award(int petId) {

		int money = 0;
		Map<Integer, PetAttr> mapConfig = fire.pb.main.ConfigManager.getInstance().getConf(PetAttr.class);
		if (mapConfig != null) {
			for (PetAttr conf : mapConfig.values()) {
				if (conf.getId() == petId) {
					if (conf.getSellPrice() > 0) {
						money = conf.getSellPrice();
					}
					break;
				}
			}
		}

		if (money != 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.addSysMoney(money, "Pet sell", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0) != money)
				return false;
		}

		MessageMgr.psendMsgNotify(roleId, 160049, MessageMgr.getStringList(money));

		return true;
	}
}