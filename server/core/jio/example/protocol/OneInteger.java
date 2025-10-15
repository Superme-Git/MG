package protocol;

import com.locojoy.base.*;
import com.locojoy.base.Marshal.*;
import com.locojoy.base.Security.*;
import com.locojoy.IO.Protocol.*;

public final class OneInteger extends Rpc.Data
{
	public int	i1;

	public OneInteger()
	{
	}

	public OctetsStream marshal(OctetsStream os)
	{
		os.marshal(i1);
		return os;
	}

	public OctetsStream unmarshal(OctetsStream os) throws MarshalException
	{
		i1 = os.unmarshal_int();
		return os;
	}

	public Object clone()
	{
		try
		{
			OneInteger o = (OneInteger)super.clone();
			return o;
		}
		catch (Exception e) { }
		return null;
	}

}
