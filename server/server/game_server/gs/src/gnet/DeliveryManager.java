package gnet;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

import mkio.Manager;

public class DeliveryManager extends Manager {

	private static DeliveryManager instance = new DeliveryManager();

	public DeliveryManager() {

		synchronized (DeliveryManager.class) {
			instance = this;
		}
	}

	public static DeliveryManager getInstance() {

		synchronized (DeliveryManager.class) {
			return instance;
		}
	}

	private static Logger logger = Logger.getLogger("SYSTEM");

	private mkio.Mkio gdelivery = null;

	private final ReentrantLock lock = new ReentrantLock();

	@Override
	protected void addMkio(mkio.Mkio mkio) {

		lock.lock();
		try {
			// TODO: if gdelivery!=null ??
			gdelivery = mkio;
			logger.info("delivery connected");
		} finally {
			lock.unlock();
		}
	}

	@Override
	public mkio.Mkio get() {

		lock.lock();
		try {
			return gdelivery;
		} finally {
			lock.unlock();
		}
	}

	@Override
	protected void removeMkio(mkio.Mkio mkio, Throwable e) {

		if (e != null)
			logger.error("err", e);
		lock.lock();
		try {
			gdelivery = null;
		} finally {
			lock.unlock();
		}

	}

	@Override
	public int size() {

		lock.lock();
		try {
			return gdelivery == null ? 0 : 1;
		} finally {
			lock.unlock();
		}
	}

	public boolean send(mkio.Protocol p2) {
		return p2.send(gdelivery);
	}
}
