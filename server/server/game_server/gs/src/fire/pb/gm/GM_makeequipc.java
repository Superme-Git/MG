package fire.pb.gm;

import com.locojoy.base.Octets;

import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.item.SGetItemTips;

public class GM_makeequipc extends GMCommand {

	@Override
	boolean exec(String[] args) {

		long gmroleid = getGmroleid();

		if (args.length < 1) {
			sendToGM("GM命令格式错误： " + usage());
			return false;
		}

		addGemToEquip(gmroleid, args);

		return true;
	}

	private void addGemToEquip(final long gmroleid, final String[] args) {
		new mkdb.Procedure() {
			@Override
			public boolean process() {
				Pack bag = (Pack) fire.pb.item.Module.getInstance()
						.getItemMaps(gmroleid, fire.pb.item.BagTypes.BAG,
								false);

				ItemBase item = bag.getItemByPos(0);
				if (null == item)
					return false;

				boolean isEquip = false;
				if (item instanceof EquipItem) {
					isEquip = true;
				}

				if (!isEquip) {
					return false;
				}

				EquipItem eItem = (EquipItem) item;
				for (int i = 0; i < args.length; i++) {
					int id = Integer.parseInt(args[i]);
					if (id < 0)
						continue;

					eItem.getEquipAttr().getDiamonds().add(id);

				}

				Octets tips = eItem.getTips();
				SGetItemTips send = new SGetItemTips(BagTypes.BAG,
						eItem.getKey(), tips);
				psendWhileCommit(gmroleid, send);

				return true;
			}
		}.submit();

	}

	@Override
	String usage() {
		return "gemid";
	}

}
