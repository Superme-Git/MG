package com.locojoy.jmonitor.counter;

public abstract class GetTask implements CounterTask {

	static final private int GET_TASK_TIMEOUT = 10 * 1000;
	
	@Override
	public void doRunTask() {
		doGetTask();
		synchronized( this) {
			this.notify();
		}
	}

	public abstract void doGetTask();
	
	public void runTask() {
		TaskThread.getInstance().getTaskQueue().pushFirstTask( this);
		try {
			synchronized( this) {
				this.wait( GET_TASK_TIMEOUT);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
