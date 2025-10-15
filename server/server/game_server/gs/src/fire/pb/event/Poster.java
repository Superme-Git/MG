package fire.pb.event;
/**
 * 简单的消息处理,还应该做个消息队列
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Poster {
	public static Poster getPoster() {return poster;}
	static Poster poster = new Poster();
	private Poster() {
//		xdb.Xdb.executor().scheduleAtFixedRate(new Run(), 1000, 1, TimeUnit.MILLISECONDS);
	}
	
//	private java.util.Queue<Event> eventqueue = new LinkedBlockingQueue<Event>();
	private static ThreadLocal<Poster> threadlocal = new ThreadLocal<Poster>();
	private Map<Class<? extends Event>, Set<EventHandler> > listenMap = new HashMap<Class<? extends Event>, Set<EventHandler> >();
	public static Poster current() {
		return threadlocal.get();
	}
	private Set<EventHandler> getListeners(Event e) {
		return listenMap.get(e.getClass());
	}
	
	public <T extends Event> boolean listenEvent(EventHandler eh, Class<T> c) {
		Set<EventHandler> ehs = listenMap.get(c);
		if (ehs == null) {
			ehs = new HashSet<EventHandler>();
			listenMap.put(c, ehs);
		}
		return ehs.add(eh);
	}
	
//	private class Run implements Runnable {
//		@Override
//		public void run() {
//		//	threadlocal.set(poster);
//			synchronized(eventqueue) {
//				Event e;
//				if ((e=eventqueue.poll()) != null) {
//					final Set<EventHandler> ehs = getListeners(e);
//					if (ehs != null) {
//						for (EventHandler eh : ehs) {
//							eh.onEvent(e);
//						}
//					}
//				}
//			}
//		//	threadlocal.set(null);
//		}
//	}

	/**
	 * @param e
	 */
	public void dispatchEvent(Event e) {
		final Set<EventHandler> ehs = getListeners(e);
		if (ehs != null) {
			for (EventHandler eh : ehs) {
				eh.onEvent(e);
			}
		}
	}
}
