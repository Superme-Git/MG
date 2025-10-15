package com.pwrd.xql;

import java.util.Map;

/**
 *<code>Condition</code>用于表示一个条件，这个条件
 *既可以是一个简单的条件语句，也可以是由多个条件语句
 *组合而成的条件语句。
 *<p>
 *<code>Condition</code>对象由语法分析器在解析XQL语句
 *的时候自底至顶构造而成。
 *<p>
 *条件的应用由两个阶段组成，第一个阶段是语义分析，用于
 *确定条件中出现的标识符是否存在和标识符之间的类型之间
 *是否匹配 等；第二个阶段是应用条件，如果XQL执行的操作
 *是查询，那么对于返回的每一条记录应用条件判断，如果符合
 *条件则返回，否则丢弃（现在暂时不考虑Add, Update和remove
 *操作）。
 * 
 * @author 王平(wangping101853)
 *
 */
public interface Condition extends SemanticAnalyzer
{
	/**
	 * 设置该条件应用的目标记录。
	 * 
	 * @param _record
	 */
	@SuppressWarnings("rawtypes")
	public void setRecord(Map.Entry _record);
	
	/**
	 * 执行条件运算。
	 * 
	 * @return true如果记录满足条件，否则返回false
	 */
	public boolean isTrue();
}
