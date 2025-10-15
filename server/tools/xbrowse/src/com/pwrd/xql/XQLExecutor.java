package com.pwrd.xql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *XQL引擎的入口，你只能通过该入口执行XQL语句。 
 * 
 * @author 王平(wangping101853)
 *
 */
public class XQLExecutor
{
	private static Log logger = LogFactory.getLog(XQLExecutor.class);
	private String statement;
	/** 构造的XQL语句对象 */
	private XQLStatement xqlStmt;
	/** 当XQL是select语句时，有用 */
	@SuppressWarnings("rawtypes")
	private List<Map.Entry> resultList;
	/** 当XQL是update或delete语句时，有用 */
	int count;
	
	public XQLExecutor(String xql)
	{
		this.statement = xql;
	}
	
	public boolean execute()
	{
		if(!compile())				// 先进行编译
			return false;
		if(!xqlStmt.execute())		// 执行
			return false;			
		switch(getOperationType())
		{
		case XQL_OPERATION_SELECT:
			resultList = xqlStmt.getResultList();
			break;
		case XQL_OPERATION_UPDATE:
		case XQL_OPERATION_DELETE:
			count = xqlStmt.getCount();
			break;
		}
		return true;
	}
	
	/**
	 * @see XQLStatement#getOperationType()
	 */
	public XQLStatement.XQLOperationType getOperationType()
	{
		if(xqlStmt == null)
		{
			logger.error("You can't invoke this method before execute!");
			throw new IllegalStateException();
		}
		return xqlStmt.getOperationType();
	}
	
	/**
	 * @see XQLStatement#getResultList()
	 */
	@SuppressWarnings("rawtypes")
	public List<Map.Entry> getResultList()
	{
		if(resultList == null)
			resultList = new ArrayList<Map.Entry>();
		return resultList;
	}
	
	/**
	 * @see XQLStatement#getCount()
	 */
	public int getCount()
	{
		return count;
	}
	
	/**
	 * 对XQL语句进行词法分析，这是编译的第一个阶段，该阶段
	 * 从XQL语句中识别出词法单元，并判断是否存在非法的词法
	 * 单元。
	 * 
	 * @return true如果不存在词法错误，否则返回false。
	 */
	protected boolean lexicalAnalyze()
	{
		assert statement != null; // 必须确保XQL语句不为空
		
		boolean result = true;
		Tokenizer tokenizer = new Tokenizer(statement);
		while(tokenizer.hasNextToken())
		{
			Token token = tokenizer.getNextToken();
			if(token.getType() == TokenType.ILLEGAL)
			{
				result = result && false;
				logger.error(token.getErrorMessage());
			}
		}
		return result;
	}
	
	/**
	 * 对XQL语句进行语法分析，这是编译的第二个阶段，该阶段
	 * 根据文法规则判断XQL语句语法是否正确，如果正确的话，
	 * 则会为该XQL语句生成一个<code>XQLStatement</code>
	 * 对象。
	 * <p>
	 * 在此，语法分析器被包装在对象内，因此将本对象作为语法分析器
	 * 的参数，并在XQL语句无语法错误时，调用{@link #setCondition(Condition)}
	 * 和{@link #setTable(String)}设置条件对象和目标表。
	 * 
	 * @return true如果没有语法错误，否则返回false。
	 */
	protected boolean syntaxAnalyze()
	{
		Tokenizer tokenizer = new Tokenizer(statement);
		XQLParser xqlParser = new XQLParser(tokenizer);
		boolean result = xqlParser.parse();
		xqlStmt = xqlParser.getXqlStatement();
		return result;
	}
	
	/**
	 * 编译XQL语句，编译由三个阶段组成：词法分析、语法分析和语义分析。
	 * 
	 * @return true如果编译成功，false如果出现编译错误。
	 */
	protected boolean compile()
	{
		if(!lexicalAnalyze())
		{
			logger.error("词法分析阶段出现错误");
			return false;
		}
		if(!syntaxAnalyze())
		{
			logger.error("语法分析阶段出现错误");
			return false;
		}
		if(!xqlStmt.semanticAnalyse())
		{
			logger.error("语义分析阶段出现错误");
			return false;
		}
		return true;
	}
}
