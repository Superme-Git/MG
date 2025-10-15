
package com.locojoy.IO.Protocol;

import com.locojoy.IO.PollIO;
import com.locojoy.base.ThreadPool;

public final class Task extends com.locojoy.base.Runnable
{
	private Manager manager;
	private Session session;
	private Protocol protocol;
	//private boolean	immediately = false;
	//TODO
	private long time_start = 0;

	private Task(int priority, Manager _manager, Session _session, Protocol _protocol)
	{
		super(priority);
		manager = _manager;
		session = _session;
		protocol = _protocol;
		time_start = System.currentTimeMillis();
	}

	public void run()
	{
		try
		{
			protocol.time_wait = System.currentTimeMillis() - time_start;
			protocol.Process(manager, session);
			if (Session.need_wakeup /*&& !immediately*/ ) 
			{
				PollIO.WakeUp();
				Session.need_wakeup = false;
			}
		}
		catch (ProtocolException e)
		{
			System.out.println("jio exception type=" + protocol.getType() + "," + e.toString());
			manager.Close(session);
		}
	}

	protected static void Dispatch(Manager manager, Session session, Protocol protocol)
	{
		int priority = manager.PriorPolicy(protocol.type);
		Task task = new Task(priority, manager, session, protocol);
		if (priority > 0)
		{
			ThreadPool.AddTask(task);
		}
		else
		{
		/*
			task.immediately = true;
			task.run();
		*/
			session.AddTask( (com.locojoy.base.Runnable)task );
		}
	}
}
