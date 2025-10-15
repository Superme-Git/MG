package fire.pb.gm;

import fire.pb.item.BagTypes;
import fire.pb.item.ItemMaps;
import fire.pb.util.BagUtil;

public class GM_additem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args[0].matches("\\d+L")) {
			final Long uid = Long.parseLong(args[0].substring(0,
					args[0].length() - 1));
			mkdb.Procedure.execute(new PRecycleItem(getGmroleid(), uid),
					new mkdb.Procedure.Done<PRecycleItem>() {
						@Override
						public void doDone(PRecycleItem p) {
							sendToGM(p.getResult());
						}
					});
			return true;
		}
		final Integer idmin, idmax, number, bagid;
		final long roleid;
		if (args[0].matches("\\d+-\\d+")) {
			String[] ids = args[0].split("-");
			idmin = Integer.parseInt(ids[0]);
			idmax = Integer.parseInt(ids[1]);
		} else if (args[0].matches("\\d+")) {
			idmin = Integer.parseInt(args[0]);
			idmax = idmin;
		} else {
			idmin = getObjectIdByName(args[0]);
			idmax = idmin;
		}

		if (idmin == null)
			return false;
		if (args.length >= 2)
			number = Integer.valueOf(args[1]);
		else
			number = 1;
		if (args.length >= 3)
			roleid = Long.valueOf(args[2]);
		else
			roleid = getGmroleid();
		if (args.length >= 4) {
			if (args[3].matches("\\d+")) {
				bagid = Integer.valueOf(args[3]);
			} else {
				if (args[3].equals("背包")) {
					bagid = BagTypes.BAG;
				} else if (args[3].equals("仓库")) {
					bagid = BagTypes.DEPOT;
				} else {
					bagid = BagTypes.BAG;
				}
			}
		} else {
			bagid = BagTypes.BAG;
		}

		final fire.pb.item.Module itemmodule = (fire.pb.item.Module) fire.pb.main.ModuleManager
				.getInstance().getModuleByName("item");
		if (itemmodule != null) {
			for (int i = idmin; i <= idmax; i++) {
				new PAddItem(roleid, itemmodule, i, number, bagid).submit();
			}
		}
		return true;
	}

	private class PAddItem extends mkdb.Procedure {
		private final fire.pb.item.Module itemmodule;
		private final int id;
		private final int number;
		private final int bagid;
		private final long roleid;

		PAddItem(long roleid, final fire.pb.item.Module itemmodule,
				final int id, final int number, final int bagid) {
			this.itemmodule = itemmodule;
			this.roleid = roleid;
			this.id = id;
			this.bagid = bagid;
			this.number = number;
		}

		@Override
		protected boolean process() {
			ItemMaps ic = fire.pb.item.Module.getInstance().getItemMaps(
					roleid, bagid, false);
			boolean isSucc = false;
			if (ic == null)
				isSucc = false;
			else {
				isSucc = BagUtil.addItem(roleid, id, number, "GM添加物品", fire.log.enums.YYLoggerTuJingEnum.GM, id) > 0;
			}
			fire.pb.item.ItemShuXing itemAttr = itemmodule.getItemManager()
					.getAttr(id);
			String name = "unknown name";
			if (itemAttr != null)
				name = itemAttr.getName();
			if (isSucc) {
				String info = "OK. Succeeded in adding " + name + " (id=" + id
						+ ",number=" + number + ") to " + roleid + ".";
				sendToGM(info);
			} else
				sendToGM("NO. Failed to add " + name + " (id=" + id
						+ ",number=" + number + ") to " + roleid + " failed.");
			return isSucc;
		}
	}

	@Override
	String usage() {
		return "additem id number roleid,bagtype 后三个参数为可选";
	}

}
