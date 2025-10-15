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
 * XQL更新语句的实现。
 * 
 */
public class XQLUpdateStatement implements XQLStatement {
	private static Log logger = LogFactory.getLog(XQLUpdateStatement.class);
	/** XQL语句的执行目标表，由语法解析器在语法解析成功时设置。 */
	protected String table;
	/** 赋值语句列表 */
	protected AssignList assignList;
	/** XQL语句where之后的部分，即所谓的条件语句，用于限定操作的记录集合。 */
	protected Condition condition;
	/** 受影响的语句条数 */
	protected int count = 0;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean execute() {
		// 由于语义分析通过之后才会调用该方法
		// 这样通过table和condition构造一个查询语句时
		// 就不需要再执行语义分析了
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
		// 根据XQL的语法，assignList不可能为空，如果为空，语法分析阶段就会出错
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
