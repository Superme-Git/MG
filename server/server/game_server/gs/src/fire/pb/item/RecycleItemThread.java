package fire.pb.item;

import java.util.Calendar;

import fire.pb.WorldTime;

public class RecycleItemThread implements fire.pb.WorldEventTab.CronTask {
//	public void start() {
//		Calendar now = Calendar.getInstance();
//		int curhour = now.get(Calendar.HOUR_OF_DAY);
//		int delay;
//		if (curhour == 4) {
//			delay = 0;
//		} else if (curhour > 4) {
//			delay = 29-curhour;
//		} else {
//			delay = 4-curhour;
//		}
//		future = xdb.Xdb.executor().scheduleAtFixedRate(this, delay, 24, TimeUnit.HOURS);
//	}
//	
//	ScheduledFuture<?> future = null;
//	
//	public void stop() {
//		if (future != null) {
//			future.cancel(true);
//			future = null;
//		}
//	}
	
	@Override
	public void run() {
		Module.logger.debug("Run clear item recycle at " + WorldTime.getInstance().getTime());
		cleanRecyclebin();
	}

	private void cleanRecyclebin() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final WorldTime date = WorldTime.getInstance();
				xbean.ItemRecycleDate id = 
						new xbean.ItemRecycleDate(date.get(Calendar.YEAR), date.get(Calendar.DAY_OF_YEAR));
				for (int i = 0; i < 10; i++) {
					removeItem(id);
					date.add(Calendar.DAY_OF_YEAR, -1);
					id = new xbean.ItemRecycleDate(date.get(Calendar.YEAR), date.get(Calendar.DAY_OF_YEAR));
				}
				return true;
			}
			
			private boolean removeItem(xbean.ItemRecycleDate id) {
				final xbean.ItemRecycleIndex ritems = xtable.Itemrecycleidx.get(id);
				if (ritems != null) {
					for (long idx : ritems.getDayrecycle()) {
						xbean.DiscardItem di = xtable.Itemrecyclebin.get(idx);
						if (di == null)
							continue;
						ItemBase bi = Module.getInstance().getItemManager().toItemBase(di.getItem());
						if (bi != null)
							bi.onDeleted();
						if (xtable.Itemrecyclebin.remove(idx)) {
							Module.logger.debug("delete item in recycle. id=" + idx);
						}
//						xtable.Itemuid2info.remove(idx);
					}
					xtable.Itemrecycleidx.delete(id);
					return true;
				}
				return false;
			}
		}.call();

	}
}
