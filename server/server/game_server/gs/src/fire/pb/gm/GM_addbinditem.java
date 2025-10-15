package fire.pb.gm;

import fire.pb.item.AddItemResult;
import fire.pb.item.Pack;

public class GM_addbinditem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		final Integer itemid;
		if (args[0].matches("\\d+")) {
			itemid = Integer.parseInt(args[0]);
		} else {
			itemid = getObjectIdByName(args[0]);
			if (itemid == null) {
				sendToGM("没有找到该物品");
				return false;
			}
		}
		final int num;
		if (args.length < 2) {
			num = 1;
		} else {
			if (args[1].matches("\\d+")) {
				num = Integer.parseInt(args[1]);
			} else {
				sendToGM("物品数量必须为数字");
				return false;
			}
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.item.ItemMgr im = fire.pb.item.Module.getInstance()
						.getItemManager();
				fire.pb.item.ItemShuXing attr = im.getAttr(itemid);
				int leftnum = num;
				Pack bag = new Pack(getGmroleid(), false);
				while (leftnum > 0) {
					int gennum = leftnum > attr.maxstack ? attr.maxstack
							: leftnum;
					fire.pb.item.ItemBase bi = im.genItemBase(itemid, gennum);
					bi.setFlag(fire.pb.Item.BIND);
					if (bag.doAddItem(bi, -1, "gm addbinditem",
							fire.log.enums.YYLoggerTuJingEnum.GM, itemid) != AddItemResult.SUCC) {
						break;
					}
					leftnum -= gennum;
				}
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "addbinditem itemid/name num";
	}

}
