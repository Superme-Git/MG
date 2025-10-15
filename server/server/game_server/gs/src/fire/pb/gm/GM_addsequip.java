package fire.pb.gm;

import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;

public class GM_addsequip extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}
		final int id = Integer.parseInt(args[0]);
		final int skillid = Integer.parseInt(args[1]);
		final fire.pb.item.Module itemmodule = (fire.pb.item.Module) fire.pb.main.ModuleManager
				.getInstance().getModuleByName("item");
		if (itemmodule != null) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					ItemBase item = itemmodule.getItemManager().genItemBase(id,
							1);
					if (!(item instanceof fire.pb.item.EquipItem)) {
						sendToGM("参数格式错误：物品不是装备." + usage());
						return false;
					}
					fire.pb.item.EquipItem equip = (fire.pb.item.EquipItem) item;
					if (skillid / 10000 == 40)
						equip.getExtInfo().setSkill(skillid);
					else if (skillid / 10000 == 41)
						equip.getExtInfo().setEffect(skillid);
					fire.pb.item.ItemMaps bag = itemmodule.getItemMaps(
							getGmroleid(), BagTypes.BAG, false);
					return bag.doAddItem(equip, -1, "gm", fire.log.enums.YYLoggerTuJingEnum.GM, 0) == AddItemResult.SUCC;
				}
			}.submit();
		}
		return true;
	}

	@Override
	String usage() {
		return "添加带有特技的装备";
	}

}
