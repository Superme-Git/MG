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
 * XQL��������ʵ�֡�
 * 
 */
public class XQLUpdateStatement implements XQLStatement {
	private static Log logger = LogFactory.getLog(XQLUpdateStatement.class);
	/** XQL����ִ��Ŀ������﷨���������﷨�����ɹ�ʱ���á� */
	protected String table;
	/** ��ֵ����б� */
	protected AssignList assignList;
	/** XQL���where֮��Ĳ��֣�����ν��������䣬�����޶������ļ�¼���ϡ� */
	protected Condition condition;
	/** ��Ӱ���������� */
	protected int count = 0;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute() {
		// �����������ͨ��֮��Ż���ø÷���
		// ����ͨ��table��condition����һ����ѯ���ʱ
		// �Ͳ���Ҫ��ִ�����������
		XQLSelectStatement selectStmt = new XQLSelectStatement();
		selectStmt.setCondition(condition);
		selectStmt.setTable(table);
		if (selectStmt.execute()) {
			Dbx.Table ttable = DbxManager.getInstance().getCurrentDbx().openTable(table);
			DatabaseMetaData.Table tableMeta = DatabaseMetaData.getInstance().getTable(table);
			List<Map.Entry> resultList = selectStmt.getResultList();
			for (Map.Entry entry : resultList) {
				if (assignList.execute(entry)) {
					OctetsStream keyS = tableMeta.getKeyType().marshal(entry.getKey());
					OctetsStream valueS = tableMeta.getValueType().marshal(entry.getValue());
					if (ttable.replace(keyS, valueS))
						++count;
				}
			}
			ttable.save();
			logger.info(count + " records were updated.");
		}
		return true;
	}

	public void setAssignList(AssignList al) {
		assignList = al;
	}

	@Override
	public void setCondition(Condition cond) {
		condition = cond;
	}

	@Override
	public XQLOperationType getOperationType() {
		return XQLOperationType.XQL_OPERATION_UPDATE;
	}

	@Override
	public void setTable(String _table) {
		table = _table;
		if (assignList != null)
			assignList.setTable(table);
		if (condition != null)
			condition.setTable(table);
	}

	@Override
	public boolean semanticAnalyse() {
		assert table != null;
		if (DbxManager.getInstance().getCurrentDbx() == null) {
			logger.error("Please select a valid xdb");
			return false;
		}
		if (DbxManager.getInstance().getCurrentDbx().openTable(table) == null) {
			logger.error("The table " + table + " doesn't exist");
			return false;
		}
		boolean result = true;
		// ����XQL���﷨��assignList������Ϊ�գ����Ϊ�գ��﷨�����׶ξͻ����
		result = result && assignList.semanticAnalyse();
		result = result && (condition == null ? true : condition.semanticAnalyse());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Entry> getResultList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getCount() {
		return count;
	}
}
