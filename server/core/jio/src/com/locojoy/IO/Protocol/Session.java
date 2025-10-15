
package com.locojoy.IO.Protocol;

import java.net.SocketAddress;
import java.util.LinkedList;

import com.locojoy.IO.NetIO.NetSession;
import com.locojoy.base.Octets;
import com.locojoy.base.TimerObserver;
import com.locojoy.base.Marshal.OctetsStream;

public final class Session extends NetSession
{
	protected Manager manager;
	protected static boolean need_wakeup = false;
	private State state;
	private Stream is;
	private LinkedList<OctetsStream> os;
	private TimerObserver.WatchDog timer;
	private long timestamp;

	private LinkedList<com.locojoy.base.Runnable> private_tasks;

	public void AddTask( com.locojoy.base.Runnable r )
	{
		synchronized( private_tasks )
		{
			if ( private_tasks.isEmpty() )
			{
				private_tasks.addFirst(r);
				new Thread(new com.locojoy.base.Runnable()
				{
					public void run()
					{
						while ( true )
						{
							com.locojoy.base.Runnable r = null;
							synchronized( private_tasks )
							{
								r = (com.locojoy.base.Runnable) private_tasks.getLast();
							}
							r.run();
							synchronized( private_tasks )
							{
								private_tasks.removeLast();
								if ( private_tasks.isEmpty() )
									return;
							}

						}
					}
				}
				).start();
			}
			else
			{
				private_tasks.addFirst(r);
			}
			private_tasks.notify();
		}
	}

	public Object clone()
	{
		try
		{
			Session s = (Session)super.clone();
			s.state = new State(state);
			s.is = new Stream(s);
			s.os = new LinkedList<OctetsStream>();
			s.private_tasks = new LinkedList<com.locojoy.base.Runnable>();
			s.timer = new TimerObserver.WatchDog();
			return s;
		}
		catch (Exception e) { }
		return null;
	}

	public String Identification()
	{
		return manager.Identification();
	}

	public SocketAddress OnCheckAddress(SocketAddress sa)
	{
		return manager.OnCheckAddress(sa);
	}

	protected void OnOpen()
	{
		timer.Reset();
		manager.AddSession(this);
	}

	protected void OnClose()
	{
		manager.DelSession(this);
	}

	public void OnAbort()
	{
		manager.AbortSession(this);
	}

	public long getTimestamp() { return timestamp; }

	protected void OnRecv()
	{
		timestamp = System.currentTimeMillis();
		timer.Reset();
		Octets input = Input();
		is.insert(is.size(), input);
		input.clear();
		try
		{
			for (Protocol p; (p = Protocol.Decode(is)) != null; Task.Dispatch(manager, this, p));
		}
		catch (ProtocolException e)
		{
			Close();
		}
	}

	protected void OnSend()
	{
		if (state.TimePolicy(timer.Elapse()))
		{
			if (os.size() != 0)
			{
				do
				{
					OctetsStream o = (OctetsStream)os.getFirst();
					if (!Output(o))
						break;
					os.removeFirst();
				}
				while(os.size() != 0);
				timer.Reset();
			}
		}
		else
			Close();
	}

	protected boolean Send(Protocol protocol)
	{
		synchronized(this)
		{
			OctetsStream o = new OctetsStream();
			protocol.Encode(o);
			if (protocol.SizePolicy(o.size()))
			{
				os.addLast(o);
				need_wakeup = true;
				return true;
			}
		}
		return false;
	}

	protected boolean StatePolicy(int type) { 
			return state.TypePolicy(type); 

	}

	protected void Close() { closing = true; }
	protected void ChangeState(String name)
	{
		synchronized(this)
		{
			state = State.Get(name);
		}
	}

	public Session(Manager m)
	{
		manager = m;
		state = manager.GetInitState();
	}
}
