package fire.pb.school.change;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.common.SCommon;
import fire.pb.item.ItemShuXing;
import fire.pb.item.gemItemShuXing;
import fire.pb.main.ConfigManager;
import fire.pb.shop.SGoods;

public class ChangeSchoolUtils {
	// 一级宝石表
	private static Map<Integer, Integer> changeGemSrcGemMap = new HashMap<Integer, Integer>();
	
	static {
		for (Entry<Integer, SGoods> entry : fire.pb.shop.Module.sGoodsMap.entrySet()) {
			SGoods goodInfo = entry.getValue();
			ItemShuXing itemAttr = fire.pb.item.Module.getInstance().getItemManager().getAttr(goodInfo.getItemId());
			if (itemAttr == null || !(itemAttr instanceof gemItemShuXing)) {
				continue;
			}

			gemItemShuXing gemItemAttr = (gemItemShuXing) itemAttr;
			if (itemAttr.getLevel() == 1) {
				if (!changeGemSrcGemMap.containsKey(gemItemAttr.getGemType())) {
					changeGemSrcGemMap.put(gemItemAttr.getGemType(), goodInfo.getId());
				}
			}
		}
	}

	public static int getSrcGem(int gemType) {
		int ret = 0;
		if (changeGemSrcGemMap.containsKey(gemType))
			ret = changeGemSrcGemMap.get(gemType);
		return ret;
	}

	/**
	 * 获取配置属性
	 */
	public static Integer getCommonConf(int id) {
		java.util.Map<Integer, SCommon> confs = ConfigManager.getInstance().getConf(SCommon.class);
		if (confs != null) {
			SCommon conf = confs.get(id);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return null;
	}

	/**
	 * 转职后可转换武器次数
	 */
	public static int getMaxChangeWeaponCount() {
		Integer value = getCommonConf(431);
		if (value == null) {
			value = 0;
		}
		return value;
	}

	/**
	 * 转职后可转换宝石次数
	 */
	public static int getMaxChangeGemCount() {
		Integer value = getCommonConf(435);
		if (value == null) {
			value = 0;
		}
		return value;
	}
}
