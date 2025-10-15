package fire.pb.gm;

import fire.pb.item.DPGiveItem;

public class GM_abpitem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM(usage());
			return false;
		}
		Integer itemid = null;
		Integer itemnum = null;
		try {
			itemid = Integer.parseInt(args[0]);
			itemnum = Integer.parseInt(args[1]);
		} catch (java.lang.NumberFormatException e) {
			itemid = getObjectIdByName(args[0]);
		}

		if (itemid == null)
			return false;

		if (itemnum == null)
			itemnum = 0;

		DPGiveItem p = new DPGiveItem(getGmroleid(), itemid, itemnum);
		p.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
