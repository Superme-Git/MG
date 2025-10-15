package fire.pb.gm;

import java.util.ArrayList;
import java.util.List;

import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import fire.pb.npc.SubmitType;

public class GM_submit extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		if (args.length == 0) {
			return false;
		} else {
			Integer subType = Integer.parseInt(args[0]);
			Integer npcid = Integer.parseInt(args[1]);
			List<fire.pb.npc.SubmitUnit> things = new ArrayList<fire.pb.npc.SubmitUnit>();
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					ItemMaps bag = fire.pb.item.Module.getInstance()
							.getItemMaps(roleId, BagTypes.BAG, false);
					if (bag == null) {
						return false;
					}

					int[] items = new int[] { 331201, 331202, 331203, 331204,
							331205 };
					for (int itemid : items) {
						ItemBase basicitem = Module.getInstance()
								.getItemManager().genItemBase(itemid, 1);
						if (basicitem == null) {
							return false;
						}
						if (bag.doAddItem(basicitem, -1,
								"add instanceZone item",
								fire.log.enums.YYLoggerTuJingEnum.GM, itemid) != AddItemResult.SUCC) {
							return false;
						}

						fire.pb.npc.SubmitUnit submitUnit = new fire.pb.npc.SubmitUnit(
								basicitem.getKey(), 1);
						things.add(submitUnit);
					}
					return true;
				}
			}.call();

			if (subType == 0) {
				new fire.pb.instancezone.PSubmit2NpcGm(roleId, npcid,
						SubmitType.INSTANCE_ZONE_ITEM, things).call();
			} else {
				new fire.pb.mission.instance.PSubmit2Npc(roleId, npcid,
						SubmitType.INSTANCE_ZONE_ITEM, things).call();
			}

		}
		return true;
	}

	@Override
	String usage() {
		return "submit [type] [npcid]";
	}

}
