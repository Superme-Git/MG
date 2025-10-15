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
 * <code>XQLSelectStatement</code>用于执行一个XQL查询语句， 并返回该语句的执行结果。
 *
 * <p/>
 * 当你需要执行一个XQL语句时，那么你需要使用该XQL创建一个 
 * <code>XQLSelectStatement</code>对象，么调用
 * {@link #executeQuery()}得到该语句的执行结果。 <b>注意：</b><code>XQLStatement</code>
 * 对象目前还不能够 被复用，因此对于每一个XQL语句，都需要创建一个对象。
 *
 * <p/>
 * 当然你可以继承此对象，编写一个功能更加强大的对象。
 *
 */
public class XQLSelectStatement implements XQLStatement {

	private static Log logger = LogFactory.getLog(XQLSelectStatement.class);

	/** XQL语句的执行目标表，由语法解析器在语法解析成功时设置。 */
	protected String table;
	/** XQL语句where之后的部分，即所谓的条件语句，用于限定操作的记录集合。 */
	protected Condition condition;
	/** 排序语句 */
	protected OrderBy orderBy;
	/** 限定语句 */
	protected Limit limit;
	/** 查询结果 */
	@SuppressWarnings("rawtypes")
	List<Map.Entry> resultList;

	/**
	 * 包访问权限，在包外不允许构造。
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
	 * 语法分析器成功返回后，会设置条件对象和目标表，虽然语法正确， 但是还需要确定目标表是否存在，条件语句中的操作数之间类型是否 可进行比较等。
	 * <p>
	 * 如果通过语义检查，那么表示可以正确运行了，这样一旦执行过程中 抛出异常，表明发生了语义阶段不可预测的问题，这时可以直接终止 XQL语句的执行。
	 * 
	 * @return true如果没有语义错误，否则返回false。
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
		throw new UnsupportedOperationException("不要对查询语句执行该方法");
	}

	/**
	 * 设置查询语句的排序部分。
	 * 
	 * @param _orderBy
	 *            - 排序语句
	 */
	void setOrderBy(OrderBy _orderBy) {
		orderBy = _orderBy;
	}

	/**
	 * 设置查询语句的limit部分。
	 * 
	 * @param _limit
	 *            - 限定语句
	 */
	void setLimit(Limit _limit) {
		limit = _limit;
	}

	/**
	 * xdb表的遍历器。
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
