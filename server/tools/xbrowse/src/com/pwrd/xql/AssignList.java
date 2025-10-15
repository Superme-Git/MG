package com.pwrd.xql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *赋值操作列表，有多个赋值操作组成。 
 * 
 * @author 王平(wangping101853)
 *
 */
public class AssignList implements SemanticAnalyzer
{
	private List<Assign> assignList;
	
	public AssignList()
	{
		assignList = new ArrayList<Assign>();
	}
	
	@Override
	public void setTable(String _table)
	{
		for(int i = 0; i < assignList.size(); ++ i)
			assignList.get(i).setTable(_table);
	}

	@Override
	public boolean semanticAnalyse()
	{
		for(int i = 0; i < assignList.size(); ++ i)
		{
			if(!assignList.get(i).semanticAnalyse())
				return false;
		}
		return true;
	}

	/**
	 * 执行赋值操作。
	 */
	@SuppressWarnings("rawtypes")
	public boolean execute(Map.Entry record)
	{
		for(int i = 0; i < assignList.size(); ++ i)
		{
			if(!assignList.get(i).execute(record))
				return false;
		}
		return true;
	}
	
	/**
	 * 添加赋值操作。
	 * 
	 * @param assign - 赋值操作
	 */
	public void addAssign(Assign assign)
	{
		assignList.add(assign);
	}
}
