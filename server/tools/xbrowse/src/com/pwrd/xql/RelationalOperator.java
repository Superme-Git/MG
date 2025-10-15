package com.pwrd.xql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RelationalOperator 
{
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(RelationalOperator.class);
	
	public TokenType operator;
	
	public RelationalOperator(TokenType _operator)
	{
		operator = _operator;
	}
	
	public boolean execute(Object obj1, Token obj2)
	{
		switch(operator)
		{
			case NE:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 != int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 != float2;
				}
				else
					return !obj2.getText().equals(obj1.toString());
			}
			case EQ:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 == int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 == float2;
				}
				else
				{
					// logger.info("obj1:**" + obj1.toString() + "**" + obj2.getText() + "**");
					return obj2.getText().equals(obj1.toString());
				}
			}
			case GE:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 >= int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 >= float2;
				}
				else
				{
					return false;
				}
			}
			case GT:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 > int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 > float2;
				}
				else
					return false;
			}
			case LE:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 <= int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 <= float2;
				}
				else
					return false;
			}
			case LT:
			{
				if(obj2.getType() == TokenType.LONG)
				{
					long int1 = Long.parseLong(obj1.toString());
					long int2 = Long.parseLong(obj2.getText());
					return int1 < int2;
				}
				else if(obj2.getType() == TokenType.FLOAT)
				{
					float float1 = Float.parseFloat(obj1.toString());
					float float2 = Float.parseFloat(obj2.getText());
					return float1 < float2;
				}
				else
					return false;
			}
		}
		
		return false;
	}
}
