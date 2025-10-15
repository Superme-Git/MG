package protocol;

import com.locojoy.base.*;
import com.locojoy.base.Marshal.*;
import com.locojoy.base.Security.*;
import com.locojoy.IO.Protocol.*;

public final class RpcSum extends Rpc
{
	public void Server(Data argument, Data result) throws ProtocolException
	{
		TwoInteger arg = (TwoInteger)argument;
		OneInteger res = (OneInteger)result;
	}

	public void Client(Data argument, Data result) throws ProtocolException
	{
		TwoInteger arg = (TwoInteger)argument;
		OneInteger res = (OneInteger)result;
	}

	public void OnTimeout()
	{
	}

}
