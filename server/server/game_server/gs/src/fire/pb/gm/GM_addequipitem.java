package fire.pb.gm;

import fire.pb.item.AddItemResult;
import fire.pb.item.Pack;

public class GM_addequipitem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Integer itemid = null;
		try {
			itemid = Integer.parseInt(args[0]);

		} catch (java.lang.NumberFormatException e) {
			itemid = getObjectIdByName(args[0]);
		}
		if (itemid == null)
			return false;
		final int id = itemid.intValue();
		final mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.item.EquipItem equipitem = (fire.pb.item.EquipItem) fire.pb.item.Module
						.getInstance().getItemManager().genItemBase(id, 1);
				if (equipitem == null)
					return false;
				Pack bag = new Pack(getGmroleid(), false);
				return bag.doAddItem(equipitem, -1, "GM add", fire.log.enums.YYLoggerTuJingEnum.GM, 0) == AddItemResult.SUCC;
			}
		};
		p.submit();

		return true;
	}

	@Override
	String usage() {
		return "添加装备 addequipitem [itemid] [endure]";
	}

}
