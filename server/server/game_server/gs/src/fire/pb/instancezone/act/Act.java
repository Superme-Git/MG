package fire.pb.instancezone.act;

import java.util.Map;

import fire.pb.instancezone.InstanceZone;

public abstract class Act
{
	
	public enum Result
	{
		NULL,		SUCCESS,		FAIL,	}
	
	final private int actId;	
	final private Map<String,Object> params;
	
	public Act(int actId, Map<String, Object> params)
	{
		this.actId = actId;
		this.params = params;
	}

	public int getActId()
	{
		return actId;
	}
	public Map<String, Object> getParams()
	{
		return params;
	}

	public abstract int getType();
	
	public boolean process(InstanceZone instzone)
	{
		Result result = instzone.act(this);
		if(result == Result.NULL)
			return defaultprocess(instzone);
		else if(result == Result.SUCCESS)
			return true;
		else
			return false;
	}
	public abstract boolean defaultprocess(InstanceZone instzone);
}
