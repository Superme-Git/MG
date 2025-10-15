package com.pwrd.xql;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mkdb.util.DatabaseMetaData;

/**
 *简单条件语句， 由左操作数、关系运算符和右操作数组成。
 * 
 */
public class SimpleCondition implements Condition
{
	private static Log logger = LogFactory.getLog(SimpleCondition.class);
	
	/** 目前左操作数只支持标识符。 */
	private KVQualifiedField leftOperand;
	/** 关系运算符 */
	private TokenType operator;
	/** 目前右操作只支持常量。 */
	private Token rightOperand;
	
	/** 语法分析器在语法分析成功返回时设置，语义分析时需要。  */
	private String table;
	/** 对目标记录应用条件时设置，进行条件运算需要。  */
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
				logger.error("类型" + type + "与类型" + rightOperand.getType().getDescription() + "无法进行比较。");
				break;
			case FLOAT:
				if(rightOperand.getType() == TokenType.FLOAT)
					return true;
				logger.error("类型" + type + "与类型" + rightOperand.getType().getDescription() + "无法进行比较。");
				break;
			case STRING:
				if(rightOperand.getType() == TokenType.STRING)
					return true;
				logger.error("类型" + type + "与类型" + rightOperand.getType().getDescription() + "无法进行比较。");
				break;
			default:
				logger.error("xbrowse目前不支持" + type + "类型的条件运算");
				break;
			}
			/*
			 * 代码执行到这里，表明leftOperand和rightOperand可以执行比较运算。
			 * 此时我们知道了右操作数的类型，因此在比较时，我们会试图将左操作数
			 * 转成右操作数的类型。 
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
		 * 代码执行到这表明语义分析是正确的，
		 * 首先需要得到leftOperand的值。
		 */
		Object leftValue = leftOperand.getValue(record);
		if(leftValue == null)
			return false;
		// logger.error(leftValue + " " + rightOperand.getText());
		/*
		 * 代码执行到这里，表明已经成功获取到了leftOperand的值，
		 * 接下来进行比较操作。 
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
