package com.pwrd.xql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *XQL�������ڣ���ֻ��ͨ�������ִ��XQL��䡣 
 * 
 * @author ��ƽ(wangping101853)
 *
 */
public class XQLExecutor
{
	private static Log logger = LogFactory.getLog(XQLExecutor.class);
	private String statement;
	/** �����XQL������ */
	private XQLStatement xqlStmt;
	/** ��XQL��select���ʱ������ */
	@SuppressWarnings("rawtypes")
	private List<Map.Entry> resultList;
	/** ��XQL��update��delete���ʱ������ */
	int count;
	
	public XQLExecutor(String xql)
	{
		this.statement = xql;
	}
	
	public boolean execute()
	{
		if(!compile())				// �Ƚ��б���
			return false;
		if(!xqlStmt.execute())		// ִ��
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
	 * ��XQL�����дʷ����������Ǳ���ĵ�һ���׶Σ��ý׶�
	 * ��XQL�����ʶ����ʷ���Ԫ�����ж��Ƿ���ڷǷ��Ĵʷ�
	 * ��Ԫ��
	 * 
	 * @return true��������ڴʷ����󣬷��򷵻�false��
	 */
	protected boolean lexicalAnalyze()
	{
		assert statement != null; // ����ȷ��XQL��䲻Ϊ��
		
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
	 * ��XQL�������﷨���������Ǳ���ĵڶ����׶Σ��ý׶�
	 * �����ķ������ж�XQL����﷨�Ƿ���ȷ�������ȷ�Ļ���
	 * ���Ϊ��XQL�������һ��<code>XQLStatement</code>
	 * ����
	 * <p>
	 * �ڴˣ��﷨����������װ�ڶ����ڣ���˽���������Ϊ�﷨������
	 * �Ĳ���������XQL������﷨����ʱ������{@link #setCondition(Condition)}
	 * ��{@link #setTable(String)}�������������Ŀ���
	 * 
	 * @return true���û���﷨���󣬷��򷵻�false��
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
	 * ����XQL��䣬�����������׶���ɣ��ʷ��������﷨���������������
	 * 
	 * @return true�������ɹ���false������ֱ������
	 */
	protected boolean compile()
	{
		if(!lexicalAnalyze())
		{
			logger.error("�ʷ������׶γ��ִ���");
			return false;
		}
		if(!syntaxAnalyze())
		{
			logger.error("�﷨�����׶γ��ִ���");
			return false;
		}
		if(!xqlStmt.semanticAnalyse())
		{
			logger.error("��������׶γ��ִ���");
			return false;
		}
		return true;
	}
}
