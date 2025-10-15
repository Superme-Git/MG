
package com.locojoy.IO.Protocol;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.locojoy.IO.ActiveIO;
import com.locojoy.IO.PassiveIO;
import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

public abstract class Protocol implements Marshal, Cloneable
{
	private static final Map<String,Protocol> map = new HashMap<String,Protocol>();
	private static final com.locojoy.base.Counter counter = new com.locojoy.base.Counter("Protocol");

	protected int type;
	protected int size_policy;
	protected int prior_policy;
	protected long time_wait = 0;

	static
	{
		try
		{
			Parser.ParseFlag(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			Parser.ParseProtocol(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			Parser.ParseRpc(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}

	public static Protocol GetStub(String name)
	{
		return (Protocol)map.get(name.toUpperCase());
	}

	public static Protocol GetStub(int type)
	{
		return GetStub(Integer.toString(type));
	}

	public static Protocol Create(String name)
	{
		Protocol stub = GetStub(name);
		return stub == null ? null : (Protocol)stub.clone();
	}

	public static Protocol Create(int type)
	{
		return Create(Integer.toString(type));
	}

	public int getType()
	{
		return type;
	}

	protected void Encode(OctetsStream os)
	{
		os.compact_uint32(type).marshal(new OctetsStream().marshal(this));
	}

	protected static Protocol Decode(Stream is) throws ProtocolException
	{
		if (is.eos()) return null;
		Protocol protocol = null;
		int type = 0, size = 0;
		try
		{
			if (is.check_policy)
			{
				is.Begin();
				type = is.uncompact_uint32();
				size = is.uncompact_uint32();
				is.Rollback();
				if (!is.session.StatePolicy(type) || !is.session.manager.InputPolicy(type, size))
				{
					System.out.println(new Date(System.currentTimeMillis()) +  "Protocol Decode CheckPolicy Error:type=" + type + ",size="+size + ",peer=" + is.session.getPeerAddress() );
					throw new ProtocolException();
				}
				is.check_policy = false;
				is.checked_size = size;
			}

			Stream data = new Stream(is.session, is.checked_size);
			is.Begin();
			type = is.uncompact_uint32();
			is.unmarshal(data);
			is.Commit();
			if ((protocol = Create(type)) != null)
			{
				protocol.unmarshal(data);
				counter.increment(protocol.getClass().getName());
			}
			else {
				System.out.println("type=" + type + " Not Found");
			}
			is.check_policy = true;
		}
		catch (MarshalException e)
		{
			is.Rollback();
			if (protocol != null)
			{
				System.out.println(new Date(System.currentTimeMillis()) +  "Protocol Decode Unmarshal Error:type=" + type + ",size="+size + ",peer=" + is.session.getPeerAddress() );
				throw new ProtocolException();
			}
			else
			{
		//		System.out.println(new Date(System.currentTimeMillis()) +  "Protocol Decode Warning:uncomplete data,protocol type=" + type + ",size="+size + ",peer=" + is.session.getPeerAddress());
			}
		}
		return protocol;
	}
	public static PassiveIO Server(Manager manager) { return PassiveIO.Open(new Session(manager)); }
	public static ActiveIO  Client(Manager manager) { return ActiveIO.Open (new Session(manager)); }

	protected int PriorPolicy() { return prior_policy; }
	protected boolean SizePolicy(int x) { return x <=0 || x < size_policy; }
	public abstract void Process(Manager manager, Session session) throws ProtocolException;

	public static void main(String argv[]) 
	{
		com.locojoy.base.TimerObserver.GetInstance().StopTimer();
	}

}
