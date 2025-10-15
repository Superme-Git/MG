package fire.pb.buff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.buff.continual.ConstantlyBuffConfig;

public class BuffClashs
{
	final private int buffId;
	final private String buffName;
	private int defaultClashMsgId = 0; //将实体buff和state分开存放，是为了检查的时候提高效率
	final private List<Integer> clashStates = new LinkedList<Integer>();//冲突状态
	final private List<Integer> clashBuffs = new LinkedList<Integer>();//冲突buff，战斗内：这些buff的存在，使此buff不能选之为目标
	final private List<Integer> coverBuffs = new LinkedList<Integer>();//此buff加载后，要覆盖的buff
	final private List<Integer> invalidBuffs = new LinkedList<Integer>();//无效buff，战斗内：这些buff的存在，使此buff无效
	final private Map<Integer,Integer> clashMsgIds = new HashMap<Integer, Integer>();
	final private Set<Integer> clashMapIds = new HashSet<Integer>();
	
	public BuffClashs(int buffId, String name)
	{
		this.buffId = buffId;
		this.buffName = name;
	}
	
	public int getBuffId()
	{
		return buffId;
	}
	public void addClashBuff(ConstantlyBuffConfig conf)
	{
		//将实体buff和state分开存放，是为了检查的时候提高效率
		if(conf == null || conf.getClassName() == null)
		{
			Module.logger.error("error：添加冲突buff时找不到该buff配置或者buff类名为空. BuffID = " + buffId);
			return;
		}
		if(conf.getClassName().equals("fire.pb.buff.continual.NullBuff"))
			clashStates.add(conf.getBuffTypeId());
		else
			clashBuffs.add(conf.getBuffTypeId());
	}
	
	public List<Integer> getClashStates()
	{
		return clashStates;
	}
	public List<Integer> getClashBuffs()
	{
		return clashBuffs;
	}
	public List<Integer> getOverrideBuffs()
	{
		return coverBuffs;
	}

	public int getDefaultClashMsgId()
	{
		return defaultClashMsgId;
	}

	public void setDefaultClashMsgId(int defaultClashMsgId)
	{
		this.defaultClashMsgId = defaultClashMsgId;
	}

	public Map<Integer, Integer> getClashMsgIds()
	{
		return clashMsgIds;
	}

	/**检查状态冲突，只有角色才有状态buff*/
	public int checkStateClash(long roleId)
	{
		for(int buffId : clashStates)
		{
			if(Module.existState(roleId,buffId))
				return buffId;
		}
		return 0;
	}
	
	
	/**检查参数中的buffId冲突*/
	public int checkBuffClash(Set<Integer> buffIds)
	{
		
		for(int id : buffIds)
		{
			int bufftype = Module.getInstance().getDefaultCBuffConfig(id).getBuffType();
			for(int clash :clashBuffs)
			{
				if (clash >= 1000)// buffId符合
				{
					if(clash == id)
						return id;
				}
				else if (clash >= 100)// 小类符合
				{
					if(clash == bufftype)
						return id;
				}
				else if (clash >= 10)// 中类符合
				{
					if(clash == bufftype / 10)
						return id;
				}
				else if (clash >= 1 )// 大类符合
				{
					if(clash == bufftype / 100)
						return id;
				}
				
			}
		}
		return 0;
	}
	
	/**检查buffId冲突*/
	public int checkBuffInvalid(Set<Integer> buffIds)
	{
		
		for(int id : buffIds)
		{
			int bufftype = Module.getInstance().getDefaultCBuffConfig(id).getBuffType();
			for(int invalid :invalidBuffs)
			{
				if (invalid >= 1000)// buffId符合
				{
					if(invalid == id)
						return id;
				}
				else if (invalid >= 100)// 小类符合
				{
					if(invalid == bufftype)
						return id;
				}
				else if (invalid >= 10)// 中类符合
				{
					if(invalid == bufftype / 10)
						return id;
				}
				else if (invalid >= 1 )// 大类符合
				{
					if(invalid == bufftype / 100)
						return id;
				}
				
			}
		}
		return 0;
	}
	
	/**筛选需要覆盖的buffId*/
	public List<Integer> getOverridedBuffIds(Set<Integer> buffIds)
	{
		List<Integer> rmbuffs = new ArrayList<Integer>();
		for(int id : buffIds)
		{
			int bufftype = Module.getInstance().getDefaultCBuffConfig(id).getBuffType();
			for (int override : coverBuffs)
			{
				if (override >= 1000)// buffId符合
				{
					if(override == id)
						rmbuffs.add(id);
				}
				else if (override >= 100)// 小类符合
				{
					if(override == bufftype)
						rmbuffs.add(id);
				}
				else if (override >= 10)// 中类符合
				{
					if(override == bufftype / 10)
						rmbuffs.add(id);
				}
				else if (override >= 1 )// 大类符合
				{
					if(override == bufftype / 100)
						rmbuffs.add(id);
				}
			}
		}
		return rmbuffs;
	}

	public String getName() {
		return buffName;
	}

	public List<Integer> getInvalidBuffs()
	{
		return invalidBuffs;
	}

	public Set<Integer> getClashMapIds()
	{
		return clashMapIds;
	}

	public boolean checkMapClash(int mapId)
	{
		return clashMapIds.contains(mapId);
	}
	
	
}
