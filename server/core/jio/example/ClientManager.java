import protocol.*;

import com.locojoy.IO.*;
import com.locojoy.base.*;
import com.locojoy.IO.Protocol.*;
import com.locojoy.base.Security.*;

public final class ClientManager extends Manager
{
	protected void OnAddSession(Session session)
	{
		System.out.println("OnAddSession " + session + " " + session.getPeerAddress());
		TwoInteger arg = new TwoInteger();
		arg.i1 = 10;
		arg.i2 = 20;
		Send(session, Rpc.Call("RPCSUM", arg));
	}

	protected void OnDelSession(Session session)
	{
		System.out.println("OnDelSession " + session + " " + session.getPeerAddress());
	}

	protected void OnAbortSession(Session session)
	{
		System.out.println("OnAbortSession " + session + " " + session.getPeerAddress());
	}

	protected State GetInitState()
	{
		return State.Get("rpc");
	}

	protected String Identification()
	{
		return "Client";
	}

	public String identity()
	{
		return "zengpan";
	}

	public String password()
	{
		return "hello";
	}
}
