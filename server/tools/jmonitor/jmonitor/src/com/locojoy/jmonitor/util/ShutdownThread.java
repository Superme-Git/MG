package com.locojoy.jmonitor.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


//from jetty8

public class ShutdownThread extends Thread {
	private static final ShutdownThread instance = new ShutdownThread();

	private boolean hooked;
	private final List<Runnable> tasks = new CopyOnWriteArrayList<Runnable>();

	private ShutdownThread() {
	}

	private synchronized void hook() {
		try {
			if (!hooked)
				Runtime.getRuntime().addShutdownHook(this);
			hooked = true;
		} catch (Exception e) {
		}
	}

	private synchronized void unhook() {
		try {
			hooked = false;
			Runtime.getRuntime().removeShutdownHook(this);
		} catch (Exception e) {
		}
	}

	public synchronized static void register(Runnable task) {
		instance.tasks.add(task);
		if (instance.tasks.size() > 0)
			instance.hook();
	}

	public synchronized static void deregister(Runnable task) {
		instance.tasks.remove(task);
		if (instance.tasks.size() == 0)
			instance.unhook();
	}

	public void run() {
		for (Runnable t : tasks)
			t.run();
	}
}