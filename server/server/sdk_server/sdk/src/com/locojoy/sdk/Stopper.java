package com.locojoy.sdk;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Stopper implements StopperMXBean {
	static transient final ReentrantLock shutdownAlarmLock = new ReentrantLock();
	static transient final Condition shutdownAlarm = shutdownAlarmLock
			.newCondition();
	static long stopTime = -1L;
	

	public void doWait() {
		while (true) {
			try {
				shutdownAlarmLock.lockInterruptibly();
			}catch(final InterruptedException ex){
				break;
			}
			try {
				if (stopTime < 0)
					shutdownAlarm.await();
				else {
					final long now = System.currentTimeMillis();
					if (now >= stopTime)
						break;
					else
						shutdownAlarm.awaitUntil(new java.util.Date(
								stopTime));
				}
			} catch (final InterruptedException ex) {
				break;
			} finally {
				shutdownAlarmLock.unlock();
			}
		}

	}

	@Override
	public long getStopTime() {
		final long time; 
		shutdownAlarmLock.lock();
		try {
			time=stopTime;
		} finally {
			shutdownAlarmLock.unlock();
		}
		if(time<=0) return time;
		else return (time - System.currentTimeMillis())/1000;
	}

	@Override
	public void setStopTime(long seconds) {
		if (seconds < 0)
			return;
		shutdownAlarmLock.lock();
		try {
			stopTime = System.currentTimeMillis() + seconds * 1000L;
			shutdownAlarm.signalAll();
		} finally {
			shutdownAlarmLock.unlock();
		}

	}

}
