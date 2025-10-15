package mkio;

import java.util.*;

/**
 * 根据创建者的名字管理连接。 每个创建者只允许创建一个连接。
 */
public class ManagerByCreator extends Manager {
	private Map<Creator, Mkio> mkios = new HashMap<Creator, Mkio>();
	private Object mutex = new Object();

	protected void onXioRemoved(Mkio x, Throwable e) {
		mkdb.Trace.warn("onXioRemoved=" + x, e);
	}

	protected void onXioAdded(Mkio x) {
		if (mkdb.Trace.isWarnEnabled())
			mkdb.Trace.warn("onXioAdded=" + x);
	}

	@Override
	protected void addMkio(Mkio mkio) {
		boolean added = false;
		synchronized (mutex) {
			if (mkios.containsKey(mkio.getCreator())) {
				mkdb.Trace.warn("Close(duplicate creator) in addXio " + mkio);
				mkio.close();
			} else {
				mkios.put(mkio.getCreator(), mkio);
				added = true;
			}
		}
		if (added)
			try {
				onXioAdded(mkio);
			} catch (Throwable e_) {
				/* skip */ }
	}

	@Override
	public Mkio get() {
		synchronized (mutex) {
			return mkios.values().iterator().next();
		}
	}

	@Override
	protected void removeMkio(Mkio mkio, Throwable e) {
		boolean removed = false;
		synchronized (mutex) {
			if (mkios.get(mkio.getCreator()) == mkio) {
				mkios.remove(mkio.getCreator());
				removed = true;
			}
		}
		if (removed)
			try {
				onXioRemoved(mkio, e);
			} catch (Throwable e_) {
				/* skip */ }
	}

	@Override
	public int size() {
		synchronized (mutex) {
			return mkios.size();
		}
	}

	@Override
	protected void close() {
		super.close();
		Map<Creator, Mkio> tmp;
		synchronized (mutex) {
			tmp = mkios;
			mkios = new HashMap<Creator, Mkio>();
		}
		for (Mkio io : tmp.values())
			io.close();
	}

	public Mkio get(Creator creator) {
		synchronized (mutex) {
			return mkios.get(creator);
		}
	}
	
}
