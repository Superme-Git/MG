package com.pwrd.xql;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *<code>OrderBy</code>��XQL��order by�﷨��ʵ�֣�
 *Ŀǰorder by��֧�ֶԵ����ֶν������򣬸��ֶμȿ�
 *����key�е��ֶΣ�Ҳ������value�е��ֶΣ�����֮��
 *<code>OrderBy</code>��֧�ְ���������߽�������
 * 
 * @author ��ƽ��wangping101853��
 *
 */
public class OrderBy implements SemanticAnalyzer
{
	private static Log logger = LogFactory.getLog(OrderBy.class);
	private String table;
	/** ���ڽ���������ֶ� */
	private KVQualifiedField field;
	/** �Ƿ����������� */
	private boolean isAsc;
	
	public OrderBy(KVQualifiedField _field, boolean _isAsc)
	{
		field = _field;
		isAsc = _isAsc;
	}
	
	/**
	 * �Ը��������ݽ�������
	 * 
	 * @param data - ���ڽ������������
	 * @return true ���ִ�гɹ���false ���ִ��ʧ��
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
	 * ��������������ֶ��Ƿ��Ǹ�������ֶΡ�
	 */
	@Override
	public boolean semanticAnalyse() 
	{
		assert table != null;
		assert field != null;
		
		return field.isValid(table);
	}
	
	/**
	 *<code>EntryComparator</code>ר�����ڶ�XDB�е�
	 *��¼���бȽϡ�
	 *<p>
	 *����ͨ���ı����ڱȽϵ��ֶξͿ��԰��ո������ֶ�
	 *��������
	 * 
	 * @author ��ƽ��wangping101853��
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
				logger.error("xbrowseĿǰ��֧�ֶ�" + type + "��������");	
			}
			if(!isAsc)
				result = -result;
			return result;
		}
		
	}
}
