package fire.pb.pet;

import java.util.Map;

import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;

/**
 * 宠物仓库扩容
 * @author XGM
 */
public class PPetDepotColumnAddCapacity extends mkdb.Procedure {

	private final long roleId;

	public PPetDepotColumnAddCapacity(final long roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean process() {

		fire.pb.pet.PetColumn petCol = new fire.pb.pet.PetColumn(roleId, PetColumnTypes.DEPOT, false);
		if (petCol.getCapacity() >= petCol.getPetColumnMaxCapacity()){
			return false;
		}

		// 消耗
		if (cost(petCol) == false) {
			return false;
		}

		// 增加容量
		petCol.incCapacity();
		// 通知客户端
		petCol.refreshCapacity();

		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[PPetDepotColumnAddCapacity] roleId:" + roleId
					+ " petColumnCapacity:" + petCol.getCapacity()
					+ " petColumnMaxCapacity:" + petCol.getPetColumnMaxCapacity());
		}
		return true;
	}

	/**
	 * 消耗
	 * @param petCol 宠物仓库
	 * @return 成功/失败
	 */
	boolean cost(fire.pb.pet.PetColumn petCol)
	{
		// 获得扩容所需的钱
		int money = getAddCapacityNeedMoney(petCol.getCapacity());
		if (money < 0) {
			return false;
		}

		// 扣钱
		if (money > 0) {
			Pack bag = new Pack(roleId, false);
			if (bag.subMoney(-money, "Pet depot add capacity", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_peiyang, 0) != -money)
				return false;
		}

		return true;
	}

	/**
	 * 获得扩容所需的钱
	 * @param curCapacity 当前容量
	 * @return 需要的钱
	 */
	int getAddCapacityNeedMoney(int curCapacity)
	{
		Map<Integer, SPetDepotPrice> confs = ConfigManager.getInstance().getConf(SPetDepotPrice.class);
		if (confs != null) {
			for (SPetDepotPrice conf : confs.values()) {
				if (conf.getNum() == curCapacity) {
					return conf.getNextneedmoney();
				}
			}
		}
		return -1;
	}
}