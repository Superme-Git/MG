package com.pwrd.xql;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.locojoy.base.Marshal.OctetsStream;
import com.pwrd.dbx.DbxManager;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

/**
 *XQLɾ������ʵ�֡� 
 * 
 */
public class XQLDeleteStatement implements XQLStatement
{
	private static Log logger = LogFactory.getLog(XQLDeleteStatement.class);
	/** XQL����ִ��Ŀ������﷨���������﷨�����ɹ�ʱ���á� */
	protected String table;
	/** XQL���where֮��Ĳ��֣�����ν��������䣬�����޶������ļ�¼���ϡ� */
	protected Condition condition;
	/** ��ɾ���ļ�¼�� */
	protected int count;
	
	XQLDeleteStatement()
	{
		count = 0;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute()
	{
		// �������ͨ��֮��Ż���ø÷���
		XQLSelectStatement selectStmt = new XQLSelectStatement();
		selectStmt.setCondition(condition);
		selectStmt.setTable(table);
		if(selectStmt.execute())
		{
			Dbx.Table ttable = DbxManager.getInstance().getCurrentDbx().openTable(table);
			DatabaseMetaData.Table tableMeta = DatabaseMetaData.getInstance().getTable(table);
			List<Map.Entry> resultList = selectStmt.getResultList();
			for(Map.Entry entry : resultList)
			{
				OctetsStream key = tableMeta.getKeyType().marshal(entry.getKey());
				if(ttable.remove(key))
					++ count;
			}
			ttable.save();
			logger.info(count + " records were deleted.");
		}
		return false;
	}
	
	@Override
	public void setCondition(Condition cond)
	{
		condition = cond;
	}

	@Override
	public XQLOperationType getOperationType()
	{
		return XQLOperationType.XQL_OPERATION_DELETE;
	}

	@Override
	public void setTable(String _table)
	{
		table = _table;
	}

	@Override
	public boolean semanticAnalyse()
	{
		assert table != null;
		if(DbxManager.getInstance().getCurrentDbx() == null)
		{
			logger.error("Please select a valid xdb");
			return false;
		}
		if(DbxManager.getInstance().getCurrentDbx().openTable(table) == null)
		{
			logger.error("The table " + table + " doesn't exist");
			return false;
		}
		boolean result = true;
		result = result && (condition == null ? true : condition.semanticAnalyse());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Entry> getResultList()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int getCount()
	{
		return count;
	}
}
