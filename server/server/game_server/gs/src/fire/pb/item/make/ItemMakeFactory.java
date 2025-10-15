package fire.pb.item.make;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.item.EquipItem;
import fire.pb.item.EquipItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.SEquipAddattributelib;
import fire.pb.item.SEquipAddattributerandomlib;
import fire.pb.product.Commontext;
import fire.pb.util.Misc;

import org.apache.log4j.Logger;

public class ItemMakeFactory {

	private static ItemMakeFactory _Instance;

	private ItemMakeFactory() {
	}

	Logger logger = Logger.getLogger("ITEM");

	public synchronized static ItemMakeFactory getFactory() {
		if (_Instance == null)
			_Instance = new ItemMakeFactory();

		return _Instance;
	}

	public EquipItem genItemMakeByWay(int equipId, String producer) {
		EquipItem equipItem = (EquipItem) Module.getInstance().getItemManager()
				.genItemBase(equipId, 1, 0, null, false);

		if (null == equipItem) {
			logger.error("装备id:" + equipId + "装备配置为空");
			return null;
		}

		// 基本属性
		equipItem.getEquipAttr().setProducer(producer);
		equipItem.setEquipEndure();

		return equipItem;
	}

	public void genItem(EquipItem equipItem) {
		// 处理双加属性
		setEquipBaseInfo(equipItem);
		// 处理特技，特效
		setEquipSkillAndEffect(equipItem);
	}

	private void setEquipBaseInfo(EquipItem equip) {

		try {
			// 处理基础属性
			EquipItemShuXing attr = equip.getItemAttr();
			int BaseEffectId = attr.getBaseAttrId();
			// 从ItemMakeUtil.effectConfigs 中获取基础装备的属性
			ZhuangBeiShuXing equipAttrCnf = ItemMakeUtil.effectConfigs
					.get(BaseEffectId);
			if (equipAttrCnf == null) {
				return;
			}

			Map<Integer, Integer> baseAttrs = new HashMap<Integer, Integer>();
			Map<Integer, ShuXing> erandomMap = equipAttrCnf.GetERandom();
			for (Entry<Integer, ShuXing> shuxing : erandomMap.entrySet()) {
				ShuXing sx = shuxing.getValue();
				// int effectid =
				// fire.pb.effect.Module.getInstance().getIdByName(sx.GetEffectName().trim());
				int effectid = 0;
				try {
					effectid = fire.pb.effect.Module.getInstance()
							.getIdByName(sx.GetEffectName().trim());
				} finally {
				}
				int errectvalue = getBaseEffectByConfig(sx.GetBodongMap());
				baseAttrs.put(effectid, errectvalue);
			}

			equip.SetBaseAttr(baseAttrs);

			// 处理附加属性
			int randomval = Misc.getRandomBetween(1,
					Commontext.EQUIP_EFFECT_RATE_BASE);
			if (randomval < attr.addAttrRate) {// 随机到有附加属性
				if (attr.getAddAttrInfo() == null)
					return;
				// 随机加哪种附加属性 1双加 2单加 3一加一减
				int attIndex = Misc.getRandomBetween(0, 2);
				String[] AddEffectIds = attr.getAddAttrInfo().get(attIndex)
						.split(";");
				Map<Integer, Integer> addeffects = getAddEffectByConfig(AddEffectIds);

				equip.SetAddAttr(addeffects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setEquipSkillAndEffect(EquipItem equipItem) {
		try {
			// 处理技能和特效
			EquipItemShuXing attr = equipItem.getItemAttr();
			if (attr == null)
				return;

			int effectid = attr.getRandomEffectId();
			if (effectid > 0) {
				SEquipAddattributerandomlib addMap = ItemMakeUtil.EQUIPADDRANDOM_CFGS
						.get(effectid);
				if (addMap == null)
					return;
				int texiaoid = getSkillAndEffectByConfig(addMap);
				if (texiaoid > 0)
					equipItem.getEquipAttr().setEffect(texiaoid);
			}

			int skillid = attr.getRandomSkillId();
			if (skillid > 0) {
				SEquipAddattributerandomlib addMap = ItemMakeUtil.EQUIPADDRANDOM_CFGS
						.get(skillid);
				if (addMap == null)
					return;
				int jinengid = getSkillAndEffectByConfig(addMap);
				if (jinengid > 0)
					equipItem.getEquipAttr().setSkill(jinengid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getBaseEffectByConfig(Map<Integer, BoDongDuan> bodongMaps) {
		// 先随机在哪个波动段
		List<Integer> quanzhongList = new ArrayList<Integer>();
		for (Entry<Integer, BoDongDuan> bdMap : bodongMaps.entrySet()) {
			quanzhongList.add(bdMap.getValue().bodongduanbase);
		}

		int resultIndex = Misc.getProbability(quanzhongList);
		if (resultIndex >= bodongMaps.size())
			return 0;

		BoDongDuan bdduan = bodongMaps.get(resultIndex);

		// 随机出该波动段的值
		int value = Misc.getRandomBetween(bdduan.min, bdduan.max);
		if (value > 0)
			return value;

		return 0;
	}

	private Map<Integer, Integer> getAddEffectByConfig(String[] attrIds) {

		Map<Integer, Integer> addeffects = new HashMap<Integer, Integer>();
		ArrayList<String> addname = new ArrayList<String>();

		for (String attrid : attrIds) {
			ArrayList<Integer> addindex = new ArrayList<Integer>();
			ArrayList<Integer> addquanzhong = new ArrayList<Integer>();

			FujiaShuXingXinXi addShuxing = ItemMakeUtil.fujiaShuXingConfigs
					.get(Integer.parseInt(attrid));

			for (int i = 0; i < addShuxing.addtableId.size(); i++) {
				if (addShuxing.addtableId.get(i) > 0) {
					if (i > addShuxing.addname.size())
						continue;

					String name = addShuxing.addname.get(i);
					if (!addname.contains(name)) { // 控制不能随机到相同的属性ID
						addindex.add(addShuxing.addtableId.get(i));
						addquanzhong.add(addShuxing.addquanzhong.get(i));
					}
				}
			}

			int resultIndex = Misc.getProbability(addquanzhong);
			if (resultIndex >= addquanzhong.size())
				return null;

			// 得到属性的ID
			int value = addindex.get(resultIndex);
			if (value > 0) {
				SEquipAddattributelib equipAdd = ItemMakeUtil.EQUIPADDATTR_CFGS
						.get(value);
				String qujian[] = equipAdd.attributeidinterval.split(";");
				int effectid = fire.pb.effect.Module.getInstance()
						.getIdByName(equipAdd.attributename.trim());
				int effiectvalue = Misc.getRandomBetween(
						Integer.parseInt(qujian[0]),
						Integer.parseInt(qujian[1]));

				addeffects.put(effectid, effiectvalue);

				addname.add(equipAdd.attributename.trim());

			}

		}
		return addeffects;
	}

	private int getSkillAndEffectByConfig(SEquipAddattributerandomlib addMap) {

		int resultIndex = Misc.getProbabilityByBase(
				addMap.addattributerquanzhong, addMap.allquanzhong);
		// int resultIndex = Misc.getProbability(addMap.addattributerquanzhong,
		// addMap.allquanzhong); //TODO 临时测试特技生成
		if (resultIndex >= addMap.addattributer.size() || resultIndex == -1)
			return 0;

		int value = addMap.addattributer.get(resultIndex);
		if (value > 0) {
			SEquipAddattributelib equipAdd = ItemMakeUtil.EQUIPADDATTR_CFGS
					.get(value);
			if (equipAdd != null) {
				return equipAdd.getSkillid();
			}
			// switch(type)
			// {
			// case 0:
			// return equipAdd.getBuffid();
			// case 1:
			// return equipAdd.getSkillid();
			// }
		}

		return 0;
	}
}
