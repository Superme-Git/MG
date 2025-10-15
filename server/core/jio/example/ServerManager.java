import protocol.*;
import com.locojoy.IO.*;
import com.locojoy.base.*;
import com.locojoy.IO.Protocol.*;
import com.locojoy.base.Security.*;

public final class ServerManager extends Manager
{
	protected void OnAddSession(Session session)
	{
		System.out.println("OnAddSession " + session + " " + session.getPeerAddress());
	}

	protected void OnDelSession(Session session)
	{
		System.out.println("OnDelSession " + session + " " + session.getPeerAddress());
	}

	protected State GetInitState()
	{
		return State.Get("rpc");
	}

	protected String Identification()
	{
		return "Server";
	}

}
