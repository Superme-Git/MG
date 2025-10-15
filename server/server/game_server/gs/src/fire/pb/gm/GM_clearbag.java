package fire.pb.gm;

import fire.pb.item.BagTypes;
import fire.pb.item.Module;
import fire.pb.item.Pack;

public class GM_clearbag extends GMCommand {

	@Override
	boolean exec(String[] args) {

		final long roleid;
		if (args.length >= 1)
			roleid = Long.valueOf(args[0]);
		else
			roleid = getGmroleid();

		if (!fire.pb.StateCommon.isOnline(roleid))
			return false;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				Pack bag = (Pack) Module.getInstance().getItemMaps(roleid,
						BagTypes.BAG, false);
				bag.clear();
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "清空背包栏";
	}

}
