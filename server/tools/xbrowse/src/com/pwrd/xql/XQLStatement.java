package com.pwrd.xql;

import java.util.List;
import java.util.Map;

/**
 *<code>XQLStatement</code>是XQL语句的抽象。所有XQL语句
 *子类都需要实现该接口。
 * 
 * @author 王平(wangping101853)
 *
 */
public interface XQLStatement extends SemanticAnalyzer
{
	/**
	 *操作类型枚举，目前xbrowse仅仅支持查询、更新和删除
	 *三种操作。 
	 */
	public static enum XQLOperationType
	{
		XQL_OPERATION_SELECT,
		XQL_OPERATION_UPDATE,
		XQL_OPERATION_DELETE
	}
	
	/**
	 * 设置XQL语句的条件。条件是指where后面的部分，用于
	 * 指定操作集。目前该方法由语法分析器在语法分析成功
	 * 后调用，
	 * <p/>
	 * <b>注意: 在语法分析器中，一定要保证先调用本方法，然后再调用
	 * {@link #setTable(String)}。</b>
	 * 
	 * @param cond - 条件（XQL语句中where后面的部分）
	 */
	public void setCondition(Condition cond);
	
	/**
	 *在进行语法分析之前，我们无法知道XQL Statement的操作类型，
	 *语法分析完成之后，XQL Statement构造就完成了，这时候其操作
	 *类型也已经确定。在应用过程中，需要依赖操作类型进行后续的操
	 *作，e.g.如果是查询则需要在界面上显示结果，对于更新和删除则
	 *只需要知道更新了多少条记录即可。
	 * 
	 * @return XQL Statement的操作类型。
	 */
	public XQLOperationType getOperationType();
	
	/**
	 *执行XQL Statement。
	 *<p/>
	 *一定要在词法分析、语法分析和语义分析结束之后执行该操作，因
	 *为执行过程中已经对这个顺序进行了假定，所以省去了很多没必要
	 *的检查操作。如果没按照这个顺序进行操作，执行过程中会发生无
	 *法预料的错误。 
	 * 
	 * @return true if execute successfully, otherwise false.
	 */
	public boolean execute();
	
	/**
	 *得到XQL Statement的执行结果，只有查询语句需要实现该方法，
	 *对于更新和删除语句抛出{@link java.lang.UnsupportedOperationException}
	 *即可。
	 * 
	 * @return 查询语句的执行结果。
	 */
	@SuppressWarnings("rawtypes")
	public List<Map.Entry> getResultList();
	
	/**
	 *得到XQL Statement的执行结果，只有更新和删除语句需要实现该
	 *方法，对于查询语句抛出{@link java.lang.UnsupportedOperationException}
	 *即可。
	 * 
	 * @return 更新和删除的执行结果。
	 */
	public int getCount();
}
