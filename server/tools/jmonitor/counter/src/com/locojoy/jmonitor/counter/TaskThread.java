package com.locojoy.jmonitor.counter;

import java.util.Collection;

public class TaskThread implements Runnable {

	final private TaskQueue		queue = new TaskQueue();
	final private Thread			thread;
	
	TaskThread() {
		thread = new Thread( this, "counter task thread");
		thread.start();
	}
	
	@Override
	public void run() {
		while( true)
		{
			Collection<CounterTask>	tasks = queue.popTasks();
			if( null == tasks)
				break;
			for( final CounterTask task : tasks)
				task.doRunTask();
			}
	}
	
	static final private TaskThread	instance = new TaskThread();
	static public TaskThread getInstance() {
		return instance;
	}

	public TaskQueue getTaskQueue() {
		return queue;
	}
	
}
