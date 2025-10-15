package fire.pb.gm;

import java.util.List;

import fire.pb.gm.GM_addlevel.PGmAddLevel;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.PPutOnEquip;

public class GM_fuben extends GMCommand {

	public GM_fuben() {
	}

	@Override
	boolean exec(String[] args) {
		final long roleid = getGmroleid();
		new PGmAddLevel(getGmUserid(), roleid, 39, false).submit();
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				bag.addSysMoney(100000000L, "GM指令 加钱", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
				bag.addSysGold(100000000L, "GM指令 加金币", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
				
				Integer [] itemids = {41600, 41601, 41602, 41603, 41604, 41605};
				for (Integer itemid : itemids) {
					List<Integer> freepos = bag.getFreepos();
					if (freepos.size() == 0)
						break;

					EquipItem equipitem = (EquipItem) fire.pb.item.Module.getInstance().getItemManager().genItemBase(itemid, 1);
					if (equipitem == null)
						continue;
					
					equipitem.setEndure(equipitem.getCurMaxEndure());
					int pos = freepos.get(0);
					bag.doAddItem(equipitem, pos, "GM add", fire.log.enums.YYLoggerTuJingEnum.GM, 0);

					ItemBase item = bag.getItemByPos(pos);
					new PPutOnEquip(roleid, item.getKey(), equipitem.getEquipPos()).call();
				}
				
				String info = "添加神器到背包成功";
				sendToGM(info);
				return true;
			}
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//fuben";
	}

}
