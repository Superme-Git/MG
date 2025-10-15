package mkio;

import java.util.HashSet;
import java.util.Set;

/**
 * �����е����ӷ���һ�������й���������Ϊһ������ʵ�ֵĻ������á��������¹��ܣ�
 * <ol>
 * <li>����ر�ʱ���ر��������ӡ�
 * <li>���Ӷ���Ļص� onMkioRemoved onMkioAdded ���ڼ̳С�
 * </ol>
 */
public class ManagerSimple extends Manager {
	private Set<Mkio> mkios = new HashSet<Mkio>();
	private Object mutex = new Object();

	protected void onMkioRemoved(Mkio x, Throwable e) {
		mkdb.Trace.warn("onMkioRemoved=" + x, e);
	}

	protected void onMkioAdded(Mkio x) {
		if (mkdb.Trace.isWarnEnabled())
			mkdb.Trace.warn("onMkioAdded=" + x);
	}

	@Override
	protected void removeMkio(Mkio mkio, Throwable e) {
		boolean removed = false;
		synchronized (mutex) {
			removed = mkios.remove(mkio);
		}
		if (removed)
			try {
				onMkioRemoved(mkio, e);
			} catch (Throwable e_) {
				/* skip */ }
	}

	@Override
	protected void addMkio(Mkio mkio) {
		boolean added = false;
		if (Engine.isOpen()) {
			synchronized (mutex) {
				added = (super.getMaxSize() <= 0 || mkios.size() < super.getMaxSize()) && mkios.add(mkio);
			}
		}
		if (added)
			try {
				onMkioAdded(mkio);
			} catch (Throwable e_) {
				/* skip */ }
		else {
			mkdb.Trace.warn("Close in addMkio " + mkio + " size=" + size() + "/" + getMaxSize());
			mkio.close();
		}
	}

	@Override
	protected void close() {
		super.close();

		// ���ڴ������ӵ�Ӧ�ã����ر����Ӹ��á�Ŀǰ�����ɾ����˳���
		Set<Mkio> tmp;
		synchronized (mutex) {
			tmp = mkios;
			mkios = new HashSet<Mkio>();
		}
		for (Mkio io : tmp)
			io.close();
	}

	@Override
	public int size() {
		synchronized (mutex) {
			return mkios.size();
		}
	}

	@Override
	public Mkio get() {
		synchronized (mutex) {
			if (mkios.isEmpty())
				return null;
			return mkios.iterator().next();
		}
	}

//	// poll �ò������ӣ��͵ȴ�
//	public Mkio take() {
//		synchronized (mkios) {
//			while (mkios.isEmpty()) {
//				try {
//					mkios.wait();
//				} catch (InterruptedException e) {
//					throw new RuntimeException(e);
//				}
//			}
//			Mkio x = mkios.iterator().next();
//			mkios.remove(x);
//			return x;
//		}
//	}
//
//	public void put(Mkio mkio) {
//		boolean added = false;
//		if (Engine.isOpen()) {
//			synchronized (mkios) {
//				added = mkios.add(mkio);
//			}
//		}
//		if (!added)
//			mkio.close();
//	}

}
