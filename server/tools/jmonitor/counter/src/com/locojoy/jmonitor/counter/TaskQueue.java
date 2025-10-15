package com.locojoy.jmonitor.counter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

	private final Lock 			lock = new ReentrantLock();
   private final Condition 	notEmpty = lock.newCondition(); 

   private LinkedList<CounterTask>	tasks = new LinkedList<CounterTask>();
   
	TaskQueue() {}

	public final void pushFirstTask( CounterTask t) {
		lock.lock();
		try {
			tasks.addFirst( t);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	public final void pushTask( CounterTask t) {
		lock.lock();
		try {
			tasks.add( t);
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}
	
	public final Collection<CounterTask> popTasks() {
		lock.lock();
		try {
			if( tasks.isEmpty())
				notEmpty.await();
			final Collection<CounterTask> result = tasks;
			tasks = new LinkedList<CounterTask>();
			return result;
		} catch (InterruptedException e) {
			return null;
		} finally {
			lock.unlock();
		}
	}
	
	
}
