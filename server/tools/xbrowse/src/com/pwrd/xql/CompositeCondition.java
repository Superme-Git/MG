package com.pwrd.xql;

import java.util.Map.Entry;

/**
 *组合条件语句。有多个条件语句通过逻辑运算符组合在一起。 
 * 
 * @author 王平(wangping101853)
 *
 */
public class CompositeCondition implements Condition
{
	private Condition leftCond;
	private TokenType operator;
	private Condition rightCond;
	
	public CompositeCondition()
	{
	}
	
	public CompositeCondition(Condition left, TokenType ope, Condition right)
	{
		leftCond = left;
		operator = ope;
		rightCond = right;
	}
	
	public Condition getLeftCond() 
	{
		return leftCond;
	}

	public void setLeftCond(Condition leftCond) 
	{
		this.leftCond = leftCond;
	}

	public TokenType getOperator() 
	{
		return operator;
	}

	public void setOperator(TokenType operator) 
	{
		this.operator = operator;
	}

	public Condition getRightCond() 
	{
		return rightCond;
	}

	public void setRightCond(Condition rightCond) 
	{
		this.rightCond = rightCond;
	}

	@Override
	public boolean semanticAnalyse() 
	{
		assert leftCond != null && operator != null && rightCond != null;
		return leftCond.semanticAnalyse() && rightCond.semanticAnalyse();
	}

	@Override
	public boolean isTrue() 
	{
		if(operator == TokenType.AND)
			return leftCond.isTrue() && rightCond.isTrue();
		else if(operator == TokenType.OR)
			return leftCond.isTrue() || rightCond.isTrue();
			
		return false;
	}

	@Override
	public void setTable(String _table) 
	{
		assert leftCond != null && rightCond != null;
		leftCond.setTable(_table);
		rightCond.setTable(_table);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setRecord(Entry _record) 
	{
		assert leftCond != null && rightCond != null;
		leftCond.setRecord(_record);
		rightCond.setRecord(_record);
	}

}
