package fire.pb.gm;

import com.locojoy.base.Octets;

import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import fire.pb.item.SGetItemTips;

public class GM_makeequipd extends GMCommand {

	@Override
	boolean exec(String[] args) {

		long gmroleid = getGmroleid();

		if (args.length < 1) {
			sendToGM("GM命令格式错误： " + usage());
			return false;
		}

		int skill = Integer.parseInt(args[0]);

		int effect = -1;
		if (args.length > 1) {
			effect = Integer.parseInt(args[1]);
		}

		String name = null;
		if (args.length > 2) {
			name = args[2];
		}

		setSkillAndEffect(gmroleid, skill, effect, name);

		return true;
	}

	private void setSkillAndEffect(final long gmroleid, final int skill,
			final int effect, final String name) {

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
				eItem.getEquipAttr().setSkill(skill);
				if (effect != -1) {
					eItem.getEquipAttr().setEffect(effect);
				}
				if (name != null) {
					eItem.getEquipAttr().setProducer(name);
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
		return "SKILL EFFECT PRODUCER";
	}

}
