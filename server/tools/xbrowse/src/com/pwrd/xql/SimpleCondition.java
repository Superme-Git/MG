package com.pwrd.xql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *��������䣬 �������������ϵ��������Ҳ�������ɡ�
 * 
 */
public class SimpleCondition implements Condition
{
	private static Log logger = LogFactory.getLog(SimpleCondition.class);
	
	/** Ŀǰ�������ֻ֧�ֱ�ʶ���� */
	private KVQualifiedField leftOperand;
	/** ��ϵ����� */
	private TokenType operator;
	/** Ŀǰ�Ҳ���ֻ֧�ֳ����� */
	private Token rightOperand;
	
	/** �﷨���������﷨�����ɹ�����ʱ���ã��������ʱ��Ҫ��  */
	private String table;
	/** ��Ŀ���¼Ӧ������ʱ���ã���������������Ҫ��  */
	@SuppressWarnings("rawtypes")
	private Map.Entry record;
	
	public  SimpleCondition()
	{
	}
	
	public SimpleCondition(KVQualifiedField left, TokenType ope, Token right)
	{
		setLeftOperand(left);
		setOperator(ope);
		setRightOperand(right);
	}
	
	@Override
	public void setTable(String table) 
	{
		this.table = table;
	}
	
	@Override
	public boolean semanticAnalyse() 
	{
		assert table != null;
		assert leftOperand != null;
		assert operator != null;
		assert rightOperand != null;
		
		if(DatabaseMetaData.getInstance().getTable(table) != null)
		{
			/*
			 * ���ȼ��leftOperand�Ƿ���ڡ�
			 * ����﷨��ȷ�Ļ���leftOperand�ĳ��ȿ϶���С��1��
			 * ���䳤��Ϊ1ʱ��leftOperandֻ����key����value��
			 * ��˲���Ҫ�������Ч�ԣ����ֻ����leftOperand���ȴ���1ʱ
			 * ����Ч�ԡ�
			 */
			/*
			 * ����ִ�е��������leftOperand���ڣ�����type�������ͣ�
			 * ������Ҫ����leftOperand��rightOperand�������Ƿ�ƥ�䡣
			 */
			if(!leftOperand.isValid(table))
				return false;
			DatabaseMetaData.Type type = leftOperand.getType(table);
			switch(type.getTypeId())
			{
			case SHORT:
			case INT:
			case LONG:
				if(rightOperand.getType() == TokenType.LONG)
					return true;
				logger.error("����" + type + "������" + rightOperand.getType().getDescription() + "�޷����бȽϡ�");
				break;
			case FLOAT:
				if(rightOperand.getType() == TokenType.FLOAT)
					return true;
				logger.error("����" + type + "������" + rightOperand.getType().getDescription() + "�޷����бȽϡ�");
				break;
			case STRING:
				if(rightOperand.getType() == TokenType.STRING)
					return true;
				logger.error("����" + type + "������" + rightOperand.getType().getDescription() + "�޷����бȽϡ�");
				break;
			default:
				logger.error("xbrowseĿǰ��֧��" + type + "���͵���������");
				break;
			}
			/*
			 * ����ִ�е��������leftOperand��rightOperand����ִ�бȽ����㡣
			 * ��ʱ����֪�����Ҳ����������ͣ�����ڱȽ�ʱ�����ǻ���ͼ���������
			 * ת���Ҳ����������͡� 
			 */
		}
		
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setRecord(Map.Entry record) 
	{
		this.record = record;
	}
	
	@Override
	public boolean isTrue() 
	{
		assert record != null;
		/*
		 * ����ִ�е�����������������ȷ�ģ�
		 * ������Ҫ�õ�leftOperand��ֵ��
		 */
		Object leftValue = leftOperand.getValue(record);
		if(leftValue == null)
			return false;
		// logger.error(leftValue + " " + rightOperand.getText());
		/*
		 * ����ִ�е���������Ѿ��ɹ���ȡ����leftOperand��ֵ��
		 * ���������бȽϲ����� 
		 */
		return new RelationalOperator(operator).execute(leftValue, rightOperand);
	}
	
	public void setLeftOperand(KVQualifiedField leftOperand) 
	{
		this.leftOperand = leftOperand;
	}

	public KVQualifiedField getLeftOperand() 
	{
		return leftOperand;
	}

	public void setOperator(TokenType operator) 
	{
		this.operator = operator;
	}

	public TokenType getOperator() 
	{
		return operator;
	}

	public void setRightOperand(Token rightOperand) 
	{
		this.rightOperand = rightOperand;
	}

	public Token getRightOperand() 
	{
		return rightOperand;
	}

}
