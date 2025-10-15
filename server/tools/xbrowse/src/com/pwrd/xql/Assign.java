package com.pwrd.xql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *赋值操作。 
 *
 *<p/>一个赋值操作是一个形如field=value的表达式，
 *其中field表示表的某一个子孙字段，value则表示常量
 *值。 
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
			 * 首先检查leftOperand是否存在。
			 * 如果语法正确的话，leftOperand的长度肯定不小于1，
			 * 当其长度为1时，leftOperand只能是key或者value，
			 * 因此不需要检查其有效性，因此只考虑leftOperand长度大于1时
			 * 的有效性。
			 */
			/*
			 * 代码执行到这里，表明leftOperand存在，并且type是其类型，
			 * 现在需要检验leftOperand和rightOperand的类型是否匹配。
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
				logger.error("无法将类型为" + rightOperand.getType().getDescription() + "的值赋值给类型" + type);
				break;
			case FLOAT:
				if(rightOperand.getType() == TokenType.FLOAT)
					return true;
				logger.error("无法将类型为" + rightOperand.getType().getDescription() + "的值赋值给类型" + type);
				break;
			case STRING:
				if(rightOperand.getType() == TokenType.STRING)
					return true;
				logger.error("无法将类型为" + rightOperand.getType().getDescription() + "的值赋值给类型" + type);
				break;
			default:
				logger.error("xbrowse目前不支持" + type + "类型的赋值操作");
				break;
			}
			/*
			 * 代码执行到这里，表明leftOperand和rightOperand可以执行赋值运算。 
			 */
		}
		return false;
	}

	/**
	 * 对给定记录的字段执行赋值操作。
	 * 
	 * @param record - 给定的字段
	 * @return true如果赋值执行成功，否则false
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
