package fire.pb.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.buff.continual.ConstantlyBuff;

public class Result
{
	private boolean success;
	private Map<Integer,Float> changedAttrs;	
	private Map<Integer,fire.pb.buff.Buff> addedBuffMap;
	private List<Integer> deletedBuffs;

	
	public Result(boolean success)
	{
		this.success = success;
		if(success)
		{
			changedAttrs = new HashMap<Integer, Float>();
			addedBuffMap = new HashMap<Integer, fire.pb.buff.Buff>();
			deletedBuffs = new ArrayList<Integer>();
		}
	}

	public void clear()
	{
		changedAttrs.clear();
		addedBuffMap.clear();
		deletedBuffs.clear();
	}
	
	public void updateResult(Result result)
	{
		if(result.isSuccess())
		{	
			if(result.getChangedAttrs().size() != 0)
				changedAttrs.putAll(result.getChangedAttrs());
			for(Map.Entry<Integer, fire.pb.buff.Buff> buff : result.getAddedBuffMap().entrySet())
			{
				this.addAddedBuff(buff.getKey(),buff.getValue());
			}
			for(Integer buffId : result.getDeletedBuffs())
			{
				this.addDeletedBuff(buffId);
			}
		}
	}
	
	public void updateChangedAttrs(Map<Integer, Float> newChangedAttrs)
	{
		this.changedAttrs.putAll(newChangedAttrs);
	}
	
	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public Map<Integer, Float> getChangedAttrs()
	{
		return changedAttrs;
	}


	public void setChangedAttrs(Map<Integer, Float> changedAttrs)
	{
		this.changedAttrs = changedAttrs;
	}
	
	public Map<Integer,fire.pb.buff.Buff> getAddedBuffMap()
	{
		return addedBuffMap;
	}
	
	public List<Integer> getDeletedBuffs()
	{
		return deletedBuffs;
	}


	public void addAddedBuff(ConstantlyBuff buff)
	{
		addAddedBuff(buff.getBuffBean());
	}
	
	public void addAddedBuff(xbean.Buff xbuff)
	{
		if(deletedBuffs.contains((Integer)xbuff.getIndex()))
		{
			deletedBuffs.remove(xbuff.getIndex());
			addedBuffMap.put(xbuff.getIndex(),getPrtlBuffFromXBuff(xbuff));
			return;
		}
		addedBuffMap.put(xbuff.getIndex(),getPrtlBuffFromXBuff(xbuff));
	}
	
	private void addAddedBuff(int buffId , fire.pb.buff.Buff buff)
	{
		if(deletedBuffs.contains(buffId))
		{
			deletedBuffs.remove((Integer)buffId);
			addedBuffMap.put(buffId,buff);
			return;
		}
		addedBuffMap.put(buffId,buff);
	}

	public void addDeletedBuff(int buffId)
	{
		if(addedBuffMap.containsKey(buffId))
		{
			addedBuffMap.remove(buffId);
			return;
		}
		if(deletedBuffs.contains((Integer)buffId))
			return;
		deletedBuffs.add(buffId);
	}
	
	public void addDeletedBuff(ConstantlyBuff buff)
	{
		if(addedBuffMap.containsKey(buff.getId()))
		{
			addedBuffMap.remove(buff.getId());
			return;
		}
		if(deletedBuffs.contains(buff.getId()))
			return;
		deletedBuffs.add(buff.getId());
	}

	
	private fire.pb.buff.Buff getPrtlBuffFromXBuff(xbean.Buff buff)
	{
		fire.pb.buff.Buff ptrlbuff = new fire.pb.buff.Buff();
		ptrlbuff.count = buff.getRound();
		if(buff.getRemaintime() <= 0)
			ptrlbuff.time = 0;
		else
		{
			ptrlbuff.time = (buff.getRemaintime() - (System.currentTimeMillis() - buff.getImpacttime())) / 1000;
			if (ptrlbuff.time < 0)
				ptrlbuff.time = 0;
		}
		return ptrlbuff;
	}
}
