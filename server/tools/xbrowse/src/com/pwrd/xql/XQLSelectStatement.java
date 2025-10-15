package com.pwrd.xql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;
import com.pwrd.dbx.DbxManager;

import mkdb.util.DatabaseMetaData;
import mkdb.util.Dbx;

/**
 * <code>XQLSelectStatement</code>����ִ��һ��XQL��ѯ��䣬 �����ظ�����ִ�н����
 *
 * <p/>
 * ������Ҫִ��һ��XQL���ʱ����ô����Ҫʹ�ø�XQL����һ�� 
 * <code>XQLSelectStatement</code>����ô����
 * {@link #executeQuery()}�õ�������ִ�н���� <b>ע�⣺</b><code>XQLStatement</code>
 * ����Ŀǰ�����ܹ� �����ã���˶���ÿһ��XQL��䣬����Ҫ����һ������
 *
 * <p/>
 * ��Ȼ����Լ̳д˶��󣬱�дһ�����ܸ���ǿ��Ķ���
 *
 */
public class XQLSelectStatement implements XQLStatement {

	private static Log logger = LogFactory.getLog(XQLSelectStatement.class);

	/** XQL����ִ��Ŀ������﷨���������﷨�����ɹ�ʱ���á� */
	protected String table;
	/** XQL���where֮��Ĳ��֣�����ν��������䣬�����޶������ļ�¼���ϡ� */
	protected Condition condition;
	/** ������� */
	protected OrderBy orderBy;
	/** �޶���� */
	protected Limit limit;
	/** ��ѯ��� */
	@SuppressWarnings("rawtypes")
	List<Map.Entry> resultList;

	/**
	 * ������Ȩ�ޣ��ڰ��ⲻ�����졣
	 */
	XQLSelectStatement() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean execute() {
		Map resultSet = new HashMap();
		Dbx.Table ttable = DbxManager.getInstance().getCurrentDbx().openTable(table);
		DbxIWalkImpl walkImpl = new DbxIWalkImpl(table);
		ttable.walk(walkImpl);
		Map data = walkImpl.getData();
		if (condition != null) {
			for (Iterator it = data.entrySet().iterator(); it.hasNext();) {
				Map.Entry record = (Map.Entry) it.next();
				condition.setRecord(record);
				if (condition.isTrue())
					resultSet.put(record.getKey(), record.getValue());
			}
		} else
			resultSet = data;

		resultList = new ArrayList<Map.Entry>();
		for (Object obj : resultSet.entrySet()) {
			Map.Entry entry = (Map.Entry) obj;
			resultList.add(entry);
		}

		if (orderBy != null)
			orderBy.execute(resultList);

		if (limit != null)
			resultList = limit.execute(resultList);

		return true;
	}

	/**
	 * �﷨�������ɹ����غ󣬻��������������Ŀ�����Ȼ�﷨��ȷ�� ���ǻ���Ҫȷ��Ŀ����Ƿ���ڣ���������еĲ�����֮�������Ƿ� �ɽ��бȽϵȡ�
	 * <p>
	 * ���ͨ�������飬��ô��ʾ������ȷ�����ˣ�����һ��ִ�й����� �׳��쳣����������������׶β���Ԥ������⣬��ʱ����ֱ����ֹ XQL����ִ�С�
	 * 
	 * @return true���û��������󣬷��򷵻�false��
	 */
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
		result = result && (condition == null ? true : condition.semanticAnalyse());
		result = result && (orderBy == null ? true : orderBy.semanticAnalyse());
		result = result && (limit == null ? true : limit.semanticAnalyse());

		return result;
	}

	@Override
	public XQLOperationType getOperationType() {
		return XQLOperationType.XQL_OPERATION_SELECT;
	}

	@Override
	public void setTable(String _table) {
		table = _table;
		if (condition != null)
			condition.setTable(_table);
		if (orderBy != null)
			orderBy.setTable(_table);
		if (limit != null)
			limit.setTable(_table);
	}

	@Override
	public void setCondition(Condition cond) {
		condition = cond;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map.Entry> getResultList() {
		return resultList;
	}

	@Override
	public int getCount() {
		throw new UnsupportedOperationException("��Ҫ�Բ�ѯ���ִ�и÷���");
	}

	/**
	 * ���ò�ѯ�������򲿷֡�
	 * 
	 * @param _orderBy
	 *            - �������
	 */
	void setOrderBy(OrderBy _orderBy) {
		orderBy = _orderBy;
	}

	/**
	 * ���ò�ѯ����limit���֡�
	 * 
	 * @param _limit
	 *            - �޶����
	 */
	void setLimit(Limit _limit) {
		limit = _limit;
	}

	/**
	 * xdb��ı�������
	 */
	public static class DbxIWalkImpl implements mkdb.Storage.IWalk {
		@SuppressWarnings("rawtypes")
		private Map _data = new HashMap();
		private String table;

		public DbxIWalkImpl(String table) {
			this.table = table;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean onRecord(byte[] key, byte[] data) {
			OctetsStream keyStream = new OctetsStream(Octets.wrap(key));
			OctetsStream valueStream = new OctetsStream(Octets.wrap(data));
			DatabaseMetaData.Table tableMeta = DatabaseMetaData.getInstance().getTable(table);
			Object _key = tableMeta.getKeyType().unmarshal(keyStream);
			Object _value = tableMeta.getValueType().unmarshal(valueStream);
			// logger.info(_value.getClass().getName());
			_data.put(_key, _value);

			return true;
		}

		@SuppressWarnings("rawtypes")
		public Map getData() {
			return _data;
		}
	}
}
