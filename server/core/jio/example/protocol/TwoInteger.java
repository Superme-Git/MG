package protocol;

import com.locojoy.base.*;
import com.locojoy.base.Marshal.*;
import com.locojoy.base.Security.*;
import com.locojoy.IO.Protocol.*;

public final class TwoInteger extends Rpc.Data
{
	public int	i1;
	public int	i2;

	public TwoInteger()
	{
	}

	public OctetsStream marshal(OctetsStream os)
	{
		os.marshal(i1);
		os.marshal(i2);
		return os;
	}

	public OctetsStream unmarshal(OctetsStream os) throws MarshalException
	{
		i1 = os.unmarshal_int();
		i2 = os.unmarshal_int();
		return os;
	}

	public Object clone()
	{
		try
		{
			TwoInteger o = (TwoInteger)super.clone();
			return o;
		}
		catch (Exception e) { }
		return null;
	}

}
