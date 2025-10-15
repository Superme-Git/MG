package fire.pb.item;
import fire.log.enums.YYLoggerTuJingEnum;

public class Temp extends ItemMaps {
//	final xbean.TempTime tt;

	public Temp(long roleId, boolean readonly) {
		super(roleId, readonly);
		/*xbean.TempTime mytt;
		if (!readonly) {
			mytt = xtable.Temptime.get(roleId);
			if (mytt == null) {
				mytt = xbean.Pod.newTempTime();
				xtable.Temptime.insert(roleId, mytt);
			}
		} else {
			mytt = xtable.Temptime.select(roleId);
			if (mytt == null) {
				mytt = xbean.Pod.newTempTimeData();
			}
		}
		tt = mytt;*/
	}

	@Override
	public void clear() {
		mkdb.Procedure.psendWhileCommit(roleId, new SCleanTempPack());
//		tt.getItems().clear();
		super.clear();
	}

	@Override
	public int getPackid() {
		return BagTypes.TEMP;
	}

	@Override
	public int removeItemWithKey(int key, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
//		tt.getItems().remove(key);
		return super.removeItemWithKey(key, num,
				fire.log.enums.YYLoggerTuJingEnum.TEMPFULL, 0,
				"临时背包栏满，添加物品时删除第一个物品");
	}

	/*private void removeOldItem() {
		long oldest = Long.MAX_VALUE;
		int oldkey = 0;
		if (tt != null) {
			for (Map.Entry<Integer, xbean.Item> xi : pack.getItems().entrySet()) {
				long gettime = tt.getItems().get(xi.getKey());
				if (gettime < oldest) {
					oldest = gettime;
					oldkey = xi.getKey();
				}
			}
		}
		removeItemWithKey(oldkey, -1, fire.log.enums.YYLoggerTuJingEnum.TEMPFULL,
				0, "临时背包栏满，添加物品时删除第一个物品");
	}*/

	@Override
	public int doAddItem(final int itemid, final int num, final int numtype,
			final int initflag, String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
			final int xiangguanid) {
		if (isFull()) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162063, null);
			return 0;
//			removeOldItem();
		}
		int realAdd = super.doAddItem(itemid, num, numtype, initflag, reason,
				countertype, xiangguanid);
		return realAdd;
	}

	@Override
	protected int doPushItem(xbean.Item xi) {
		int key = super.doPushItem(xi);
//		tt.getItems().put(key, WorldTime.getInstance().getTimeInMillis());
		return key;
	}

	@Override
	public ItemBase TransOut(final int key, final int number,
			final String reason) {
//		if (tt != null) {
//			tt.getItems().remove(key);
//		}
		ItemBase ret = super.TransOut(key, number, reason);
		if (ret != null) {
			fire.pb.event.Poster.getPoster()
					.dispatchEvent(
							new fire.pb.event.UnequipItemEvent(roleId, ret
									.getItemId()));
		}
		return ret;
	}

	@Override
	public AddItemResult doAddItem(ItemBase item, int pos, String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (isFull()) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 162063, null);
			return AddItemResult.FULL;
		}
		return super.doAddItem(item, pos, reason, countertype, xiangguanid);
	}
}
