package com.pwrd.xql;

/**
 *XQL语言的语义分析器。一个XQL语句顺利通过语法分析之后，
 *只能说明该语句是一个well-formed的XQL语句，但是并不能
 *说明这是一个合法的XQL语句，还需要对该语句进行上下文有关
 *性质的审查，进行类型审查。
 * 
 *<p/>在XQL中，语义分析包括：检查语句中的表是否存在、检查
 *条件语句中引用的字段是否存在、检查赋值语句中引用的字段是
 *否存在、检查关系运算符两侧的操作数类型是否能够进行该操作，
 *以及检查赋值运算符两侧的操作数能否进行该操作。
 * 
 *<p/>所有需要进行语义分析的类一定要根据需求实现该接口。
 *
 * @author 王平(wangping101853)
 *
 */
public interface SemanticAnalyzer 
{
	/**
	 *XQL语句执行的操作包括查询、插入、更新和删除，这些操作
	 *均以表为目标，因此在进行语义分析之前，一定要设置该对象
	 *的目标表。
	 * 
	 *<p/>在进行语义分析之前，一定要设置该XQL语句的目标表，
	 *如果一个XQL语句存在过滤条件、排序或者Limit，那么该操作应该
	 *在调用<code>setCondition</code>、<code>setOrderBy</code>
	 *和<code>setLimit</code>方法之后进行。
	 *
	 *<p/>该方法目前由语法分析器在语法分析成功时调用。
	 *
	 *<p/><b>注意：该方法必须在最后调用，否则会导致语义分析出错。</b>
	 * 
	 * @param _table - 目标表
	 */
	public void setTable(String _table);
	
	/**
	 *设置目标表后，该方法进行具体的语义分析操作。但是在进行
	 *语义分析操作之前，一定要确保已经调用了<code>setTable</code>
	 *方法。
	 * 
	 * @return true，如果语义正确；false，如果存在语义错误。
	 */
	public boolean semanticAnalyse();
}
