package com.pwrd.xql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *<code>OrderBy</code>是XQL中order by语法的实现，
 *目前order by仅支持对单个字段进行排序，该字段既可
 *以是key中的字段，也可以是value中的字段，除此之外
 *<code>OrderBy</code>还支持按照升序或者降序排序。
 * 
 * @author 王平（wangping101853）
 *
 */
public class OrderBy implements SemanticAnalyzer
{
	private static Log logger = LogFactory.getLog(OrderBy.class);
	private String table;
	/** 用于进行排序的字段 */
	private KVQualifiedField field;
	/** 是否按照升序排序 */
	private boolean isAsc;
	
	public OrderBy(KVQualifiedField _field, boolean _isAsc)
	{
		field = _field;
		isAsc = _isAsc;
	}
	
	/**
	 * 对给定的数据进行排序。
	 * 
	 * @param data - 用于进行排序的数据
	 * @return true 如果执行成功，false 如果执行失败
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(List<Map.Entry> data)
	{
		Collections.sort(data, new EntryComparator(table, field, isAsc));
		// if(!isAsc)
		// 	Collections.reverse(data);
		return true;
	}

	@Override
	public void setTable(String _table) 
	{
		this.table = _table;
	}

	/**
	 * 分析用于排序的字段是否是给定表的字段。
	 */
	@Override
	public boolean semanticAnalyse() 
	{
		assert table != null;
		assert field != null;
		
		return field.isValid(table);
	}
	
	/**
	 *<code>EntryComparator</code>专门用于对XDB中的
	 *记录进行比较。
	 *<p>
	 *这样通过改变用于比较的字段就可以按照给定的字段
	 *进行排序。
	 * 
	 * @author 王平（wangping101853）
	 *
	 */
	@SuppressWarnings("rawtypes")
	private static class EntryComparator implements Comparator
	{
		private String table;
		private KVQualifiedField field;
		private boolean isAsc;
		
		public EntryComparator(String _table, KVQualifiedField _field, boolean _isAsc)
		{
			table = _table;
			field = _field;
			isAsc = _isAsc;
		}
		
		@Override
		public int compare(Object o1, Object o2) 
		{
			Map.Entry entry1 = (Map.Entry) o1;
			Map.Entry entry2 = (Map.Entry) o2;
			DatabaseMetaData.Type type = field.getType(table);
			Object value1 = field.getValue(entry1);
			Object value2 = field.getValue(entry2);
			int result = 0;
			switch(type.getTypeId())
			{
			case SHORT:
			case INT:
			case LONG:
				long l1 = Long.parseLong(value1.toString());
				long l2 = Long.parseLong(value2.toString());
				if(l1 < l2)
					result = -1;
				else if(l1 == l2)
					result = 0;
				else
					result = 1;
				break;
			case FLOAT:
				float f1 = Float.parseFloat(value1.toString());
				float f2 = Float.parseFloat(value2.toString());
				if(f1 < f2)
					result = -1;
				else if(f1 == f2)
					result = 0;
				else
					result = 1;
				break;
			case STRING:
				result = value1.toString().compareTo(value2.toString());
				break;
			default:
				logger.error("xbrowse目前不支持对" + type + "进行排序");	
			}
			if(!isAsc)
				result = -result;
			return result;
		}
		
	}
}
