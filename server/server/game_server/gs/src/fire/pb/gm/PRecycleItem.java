package fire.pb.gm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fire.pb.WorldTime;
import fire.pb.item.AddItemResult;
import fire.pb.item.ItemBase;
import fire.pb.item.Pack;
import mkdb.Procedure;

public class PRecycleItem extends Procedure {
	private final long uid;
	private final long roleid;
	private String result = "找回物品中 ...";

	public PRecycleItem(final long roleid, final long uid) {
		this.roleid = roleid;
		this.uid = uid;
	}

	public String getResult() {
		return result;
	}

	@Override
	protected boolean process() {
		xbean.DiscardItem di = xtable.Itemrecyclebin.get(uid);
		if (di == null) {
			result = "无法找到物品";
			return false;
		}
		xbean.Item xitem = di.getItem().copy();
		ItemBase bi = fire.pb.item.Module.getInstance().getItemManager()
				.toItemBase(xitem);
		Pack bag = new Pack(roleid, false);
		if (bi != null) {
			bi.getDataItem().getNumbermap().clear();
			for (Map.Entry<Integer, Integer> e : xitem.getNumbermap()
					.entrySet()) {
				bi.getDataItem().getNumbermap().put(e.getKey(), e.getValue());
			}
			bi.getDataItem().setNumber(xitem.getNumber());
			if (bag.doAddItem(bi, -1, "找回物品",
					fire.log.enums.YYLoggerTuJingEnum.GM, xitem.getId()) != AddItemResult.SUCC) {
				result = "背包没有足够的空间";
				return false;
			}
			xtable.Itemrecyclebin.delete(uid);
			result = "找回物品成功";
			WorldTime now = WorldTime.getInstance();

			List<String> parameters = new ArrayList<String>();
			parameters.add(String.valueOf(now.get(Calendar.YEAR)));
			parameters.add(String.valueOf(now.get(Calendar.MONTH) + 1));
			parameters.add(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
			parameters.add(String.valueOf(now.get(Calendar.HOUR_OF_DAY)));
			parameters.add(String.valueOf(now.get(Calendar.MINUTE)));
			fire.pb.talk.MessageMgr.psendSystemMessageToRole(roleid, 143928,
					parameters);
			return true;
		} else {
			return false;
		}

	}

}
