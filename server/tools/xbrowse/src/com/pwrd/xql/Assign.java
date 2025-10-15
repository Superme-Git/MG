package com.pwrd.xql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *��ֵ������ 
 *
 *<p/>һ����ֵ������һ������field=value�ı��ʽ��
 *����field��ʾ���ĳһ�������ֶΣ�value���ʾ����
 *ֵ�� 
 * 
 *
 */
public class Assign implements SemanticAnalyzer
{
	private static Log logger = LogFactory.getLog(Assign.class);
	private String table;
	private KVQualifiedField leftOperand;
	private Token rightOperand;
	
	@Override
	public void setTable(String _table)
	{
		table = _table;
	}

	@Override
	public boolean semanticAnalyse()
	{
		assert table != null;
		assert leftOperand != null;
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
				logger.error("�޷�������Ϊ" + rightOperand.getType().getDescription() + "��ֵ��ֵ������" + type);
				break;
			case FLOAT:
				if(rightOperand.getType() == TokenType.FLOAT)
					return true;
				logger.error("�޷�������Ϊ" + rightOperand.getType().getDescription() + "��ֵ��ֵ������" + type);
				break;
			case STRING:
				if(rightOperand.getType() == TokenType.STRING)
					return true;
				logger.error("�޷�������Ϊ" + rightOperand.getType().getDescription() + "��ֵ��ֵ������" + type);
				break;
			default:
				logger.error("xbrowseĿǰ��֧��" + type + "���͵ĸ�ֵ����");
				break;
			}
			/*
			 * ����ִ�е��������leftOperand��rightOperand����ִ�и�ֵ���㡣 
			 */
		}
		return false;
	}

	/**
	 * �Ը�����¼���ֶ�ִ�и�ֵ������
	 * 
	 * @param record - �������ֶ�
	 * @return true�����ִֵ�гɹ�������false
	 */
	@SuppressWarnings("rawtypes")
	public boolean execute(Map.Entry record)
	{
		return leftOperand.setValue(record, rightOperand);
	}
	
	public KVQualifiedField getLeftOperand()
	{
		return leftOperand;
	}

	public void setLeftOperand(KVQualifiedField leftOperand)
	{
		this.leftOperand = leftOperand;
	}

	public Token getRightOperand()
	{
		return rightOperand;
	}

	public void setRightOperand(Token rightOperand)
	{
		this.rightOperand = rightOperand;
	}
}
