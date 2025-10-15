package com.locojoy.jmonitor.counter;

public abstract class SetTask implements CounterTask {

	public void runTask() {
		TaskThread.getInstance().getTaskQueue().pushTask( this);
	}

}
