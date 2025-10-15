package fire.pb;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class WorldEventTab implements Runnable {
	private static WorldEventTab instance;
	public static WorldEventTab getInstance() {
		return instance;
	}
	public WorldEventTab() {
		if (instance == null) {
			instance = this;
		}
	}
	public void start() {
		WorldTime now = WorldTime.getInstance();
		mkdb.Mkdb.executor().scheduleAtFixedRate(this, 60-now.get(Calendar.SECOND), 60, TimeUnit.SECONDS);
	}
	
	private AtomicLong eventidx = new AtomicLong(0);
	private Set<Long> running = new HashSet<Long>();
	private Set<Long> onetimerunning = new HashSet<Long>();
//	private List<CrontabTask> tasktab = new java.util.ArrayList<CrontabTask>();
//	private Map<CronTask, OnetimeTaskInfo> onetimetasktab = 
//			new java.util.concurrent.ConcurrentHashMap<CronTask, OnetimeTaskInfo>();

	public static class CrontabTask {
		CrontabTask(CronTask task, String schedule) {
			this.task = task;
			String[] schedulestrings = schedule.split("\\s+");
			if (schedulestrings.length == 5) {
				if (!init(minutes, schedulestrings[0], 0, 59)) {
					throw new IllegalArgumentException("错误的定时任务配置");
				}
				if (!init(hours, schedulestrings[1], 0, 23)) {
					throw new IllegalArgumentException("错误的定时任务配置");
				}
				if (!init(days, schedulestrings[2], 1, 31)) {
					throw new IllegalArgumentException("错误的定时任务配置");
				}
				if (!init(months, schedulestrings[3], 1, 12)) {
					throw new IllegalArgumentException("错误的定时任务配置");
				}
				if (!init(weekdays, schedulestrings[4], 1, 7)) {
					throw new IllegalArgumentException("错误的定时任务配置");
				}
			} else {
				throw new IllegalArgumentException("错误的定时任务配置");
			}
		}
		
		void run() {
			task.run();
		}
		
		 boolean match(WorldTime time) {
			 if (!match(time, time.get(Calendar.MINUTE), minutes)) {
				 return false;
			 }
			 if (!match(time, time.get(Calendar.HOUR_OF_DAY), hours)) {
				 return false;
			 } 
			 if (!match(time, time.get(Calendar.DAY_OF_MONTH), months)) {
				 return false;
			 } 
			 if (!match(time, time.get(Calendar.MONTH) + 1, months)) {
				 return false;
			 } 
			 if (!match(time, time.get(Calendar.DAY_OF_WEEK), weekdays)) {
				 return false;
			 }
			 return true;
		 }
		
		private boolean match(WorldTime time, int curval, Set<Integer> requires) {
			if (!requires.isEmpty()) {
				if (!requires.contains(curval)) {
					return false;
				}
			}
			return true;
		}
		
		private boolean init(Set<Integer> ret, String string, int min, int max) {
			for (String substring : string.split(",")) {
				if (substring.matches("\\d+")) {
					ret.add(Integer.parseInt(substring));
				} else if (substring.matches("\\d+-\\d+")){
					int idx = substring.indexOf("-");
					int a = Integer.parseInt(substring.substring(0, idx));
					int b = Integer.parseInt(substring.substring(idx+1, substring.length()));
					if (a>=b) {
						if (b < min || a > max) {
							return false;
						}
						for (int i = b; i <= a; i++) {
							ret.add(i);
						}
					} else {
						if (a < min || b > max) {
							return false;
						}
						for (int i = a; i <= b; i++) {
							ret.add(i);
						}
					}
				} else if (substring.matches("\\*")) {
				} else {
					return false;
				}
			}
			return true;
		}
		
		CronTask task;
		Set<Integer> minutes = new java.util.HashSet<Integer>();
		Set<Integer> hours = new java.util.HashSet<Integer>();
		Set<Integer> days = new java.util.HashSet<Integer>();
		Set<Integer> months = new java.util.HashSet<Integer>();
		Set<Integer> weekdays = new java.util.HashSet<Integer>();
	}
	
	public static class OnetimeTask {
		private final WorldTime runtime;
		private final CronTask task;
		OnetimeTask(final CronTask task, final int delay) {
			this.task = task;
			runtime = WorldTime.getInstance();
			runtime.add(Calendar.MINUTE, delay);
		}
		public void run() {
			task.run();
		}
		
		public boolean match(final WorldTime now) {
			return now.after(runtime);
		}
	}
	
	public long add(final CronTask task, final String schedule) {
		mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.EventInfo ei = xbean.Pod.newEventInfo();
				ei.setEvent(new CrontabTask(task, schedule));
				long key = eventidx.incrementAndGet();
				while (key == 0 || key == -1) {
					key = eventidx.incrementAndGet();
				}
				xtable.Worldeventtab.insert(key, ei);
				synchronized (running) {
					running.add(key);
				}
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			p.submit();
			return -1;
		} else {
			if (p.call())
				return eventidx.get();
			return 0;
		}
	}
	/**
	 * @param delay 单位分钟
	 */
	public long addDelay(final CronTask task, final int delay) {
		
		mkdb.Procedure p = new mkdb.Procedure() {
			
			@Override
			protected boolean process() {
				xbean.OnetimeEvent oe = xbean.Pod.newOnetimeEvent();
				oe.setOnetimeevent(new OnetimeTask(task, delay));
				long key = eventidx.incrementAndGet();
				while (key == 0 || key == -1) {
					key = eventidx.incrementAndGet();
				}
				xtable.Onetimeeventtab.insert(key, oe);
				synchronized (onetimerunning) {
					onetimerunning.add(key);
				}
				return true;
			}
		};
		if (mkdb.Transaction.current() == null) {
			p.submit();
			return -1;
		} else {
			if (p.call())
				return eventidx.get();
			return 0;
		}
	}
	
	public void remove(final long idx) {
		if (walking.get())
			return;
		mkdb.Procedure p = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				if (xtable.Worldeventtab.remove(idx)) {
					synchronized (running) {
						running.remove(idx);
					}
					return true;
				} 
				if (xtable.Onetimeeventtab.remove(idx)) {
					synchronized (onetimerunning) {
						onetimerunning.remove(idx);
					}
					return true;
				}
				return false;
			}
		};
		if (mkdb.Transaction.current() == null)
			p.submit();
		else 
			p.call();
	}
	
	public static interface CronTask {
		public void run();
	}
	
	private AtomicBoolean walking = new AtomicBoolean(false);
	
	@Override
	public final void run() {
		final WorldTime now = WorldTime.getInstance();
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				for (long run : running) {
					xbean.EventInfo ei = xtable.Worldeventtab.get(run);
					if (ei != null) {
						if (ei.getEvent().match(now))
							ei.getEvent().run();
					}
				}
				synchronized (onetimerunning) {
					walking.set(true);
					try {
						for (Iterator<Long> iter = onetimerunning.iterator(); iter.hasNext();) {
							long run = iter.next();
							xbean.OnetimeEvent oe = xtable.Onetimeeventtab.get(run);
							if (oe != null) {
								if (oe.getOnetimeevent().match(now)) {
									oe.getOnetimeevent().run();
									iter.remove();
									xtable.Onetimeeventtab.remove(run);
								}
							}
						}
					} catch (Throwable __) {} finally { walking.set(false);}
				}
				return true;
			}
		}.submit();
	}
	
	public CronTask getTask(final long eventidx) {
		xbean.EventInfo ei = xtable.Worldeventtab.get(eventidx);
		if (ei != null) return ei.getEvent().task;
		xbean.OnetimeEvent oe = xtable.Onetimeeventtab.get(eventidx);
		if (oe != null) return oe.getOnetimeevent().task;
		return null;
	}
}
