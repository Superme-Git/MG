package com.pwrd.xql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <code>Limit</code>用于获取在某一个区间的记录。
 * 
 * @author wp853
 *
 */
public class Limit implements SemanticAnalyzer
{
	private long startingPoint;
	private long count;
	
	public Limit(long _startingPoint, long _count)
	{
		startingPoint = _startingPoint;
		count = _count;
	}
	
	/**
	 * 
	 * 
	 * @param data - 数据
	 * @return 在该区间的记录
	 */
	@SuppressWarnings("rawtypes")
	public List<Map.Entry> execute(List<Map.Entry> data)
	{
		if(startingPoint >= data.size())
			return new ArrayList<Map.Entry>();
		int toIndex = (int) (startingPoint + count > data.size()? data.size() : startingPoint + count);
		return data.subList((int) startingPoint, toIndex);
	}
	
	@Override
	public void setTable(String _table)
	{		
	}

	@Override
	public boolean semanticAnalyse() 
	{
		if(startingPoint >= 0 && count >= 0)
			return true;
		return false;
	}

}
