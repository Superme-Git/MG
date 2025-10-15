package com.pwrd.xql;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *一个非常简单的XQL词法分析器实现。该词法分析器仅支持
 *<code>TokenType</code>所声明的类型。
 * @see TokenType
 * 
 * @author 王平（wangping101853）
 *
 */
public class Tokenizer 
{
	private static Log logger = LogFactory.getLog(Tokenizer.class);
	private static Set<String> keywords;
	private static Set<Character> idChars;
	
	/** 待分词的XQL语句 */
	private String _query;

	/** 下一个词法单位开始位置 */
	private int _startIndex;

	static
	{
		keywords = new HashSet<String>();
		keywords.add("select");
		keywords.add("update");
		keywords.add("set");
		keywords.add("delete");
		keywords.add("from");
		keywords.add("where");
		keywords.add("key");
		keywords.add("value");
		keywords.add("order");
		keywords.add("by");
		keywords.add("asc");
		keywords.add("desc");
		keywords.add("limit");
		keywords.add("in");
		idChars = new HashSet<Character>();
		for(char ch = 'a'; ch <= 'z'; ++ ch)
			idChars.add(ch);
		for(char ch = 'A'; ch <= 'Z'; ++ ch)
			idChars.add(ch);
		for(char ch = '0'; ch <= '9'; ++ ch)
			idChars.add(ch);
		idChars.add('_');
	}
	
	public static Set<String> getKeywords()
	{
		return keywords;
	}
	
	public static boolean isKeyword(String keyword)
	{
		return keywords.contains(keyword);
	}
	
	public static boolean isIdChar(char ch)
	{
		return idChars.contains(ch);
	}
	
	public Tokenizer(String query) 
	{
		_startIndex = 0;
		_query = query;
	}
	
	/**
	 * 对<code>charAt</code>方法的一个简单包装。
	 * @param index - the index of the <code>char</code> value
	 * @return the char value at the specified index of <code>_query</code>.
	 */
	private char charAt(int index)
	{
		return _query.charAt(index);
	}

	/**
	 * 得到下一个词法单元。
	 * 
	 * @return 下一个词法单元。
	 * @exception IllegalStateException - 通过{@link #hasNextToken()}来判断是否还存在
	 * 下一个词法单元，如果存在再调用该方法，否则抛出该异常。
	 */
	public Token getNextToken() throws IllegalStateException
	{
		if(!hasNextToken())
		{
			logger.error("Please invoke hasNextToken each time before getNextToken");
			throw new IllegalStateException("Please invoke hasNextToken each time before getNextToken");
		}
		int sentinel = _query.length(); // 设置哨兵，进行词法解析时不要超过此位置
		Token token = new Token();
		TokenType tokenType = TokenType.ILLEGAL;
		String msg = null;
		// 指向下一个待处理的字符
		int offset = _startIndex;
		
		// 处理结束后，一定要保证offset指向下一个词法单元的起始位置。
		// 一定要保证offset小于sentinel。
		switch (charAt(offset ++)) 
		{
			case ' ':
			case '\t':
			case '\n':
			case '\f':
			case '\r': /** 处理空白 */
			{
				for( ; offset < sentinel && Character.isWhitespace(charAt(offset)); ++ offset)
					;
				tokenType = TokenType.SPACE;
				break;
			}
			case '(': /** 处理标点符号 */
			{
				tokenType = TokenType.LP;
				break;
			}
			case ')':
			{
				tokenType = TokenType.RP;
				break;
			}
			case '[':
			{
				tokenType = TokenType.LB;
				break;
			}
			case ']':
			{
				tokenType = TokenType.RB;
				break;
			}
			case ';':
			{
				tokenType = TokenType.SEMI;
				break;
			}
			case ',':
			{
				tokenType = TokenType.COMMA;
				break;
			}
			case '=': /** 处理关系运算符 */
			{
				if(offset < sentinel && charAt(offset) == '=')
				{
					++ offset;
					tokenType = TokenType.EQ;
					break;
				}
				else if(offset < sentinel && charAt(offset) != '=')
				{
					tokenType = TokenType.ASSIGN;
					break;
				}
				tokenType = TokenType.ILLEGAL;
				msg = "unrecognized \'=\' at position " + _startIndex;
				break;
			}
			case '<':
			{
				if(offset < sentinel && charAt(offset) == '=')
				{
					++ offset;
					tokenType = TokenType.LE;
					break;
				}
				else
				{
					tokenType = TokenType.LT;
					break;
				}
			}
			case '>':
			{
				if(offset < sentinel && charAt(offset) == '=')
				{
					++ offset;
					tokenType = TokenType.GE;
					break;
				}
				else
				{
					tokenType = TokenType.GT;
					break;
				}
			}
			case '!':
			{
				if(offset >= sentinel || charAt(offset) != '=')
				{
					tokenType = TokenType.ILLEGAL;
					msg = "unrecognized \'!\' at position " + _startIndex;
					break;
				}
				else
				{
					++ offset;
					tokenType = TokenType.NE;
					break;
				}
			}
			case '|': /** 处理逻辑运算符 */
			{
				if(offset >= sentinel || charAt(offset) != '|')
				{
					tokenType = TokenType.ILLEGAL;
					msg = "unrecognized '|' at position " + _startIndex;
					break;
				}
				else
				{
					++ offset;
					tokenType = TokenType.OR;
					break;
				}
			}
			case '&':
			{
				if(offset >= sentinel || charAt(offset) != '&')
				{
					tokenType = TokenType.ILLEGAL;
					msg = "unrecognized '&' at position " + _startIndex;
					break;
				}
				else
				{
					++ offset;
					tokenType = TokenType.AND;
					break;
				}
			}
			case '\"': /** 处理字符串 */
			{
				char delim = charAt(offset - 1); // "
				for(; offset < sentinel && charAt(offset) != delim; ++ offset)
					;
				if(offset >= sentinel)
				{
					tokenType = TokenType.ILLEGAL;
					msg = "There isn't matched \" for \"";
				}
				else
				{
					++ offset;
					tokenType = TokenType.STRING;
				}
				break;
			}
			case '.':
			{
				// if(!Character.isDigit(_query.charAt(offset)))
				// {
				tokenType = TokenType.DOT;
				break;
				// }
				/* If the next character is a digit, this is a floating point
				** number that begins with ".".  Fall thru into the next case */
			}
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			{
				tokenType = TokenType.LONG;
				for(; offset < sentinel && Character.isDigit(charAt(offset)); ++ offset)
					;
				if(offset >= sentinel)
					break;
				if(charAt(offset) == '.')
				{
					++ offset;
					tokenType = TokenType.FLOAT;
					while(offset < sentinel && Character.isDigit(charAt(offset)))
						++ offset;
				}
				/* 
				 * 此时跳出的唯一原因是碰到了非数字字符，如果这些
				 * 非数字字符是[a-zA-Z_]，那么这个数字非法。
				 */
				while(offset < sentinel && isIdChar(charAt(offset)))
				{
					++ offset;
					tokenType = TokenType.ILLEGAL;
					msg = "illegal number at position " + _startIndex;
				}
				break;
			}
			default: /** 处理标识符和关键字，到这该字符应该是[a-zA-Z_] */
			{
				if(!isIdChar(charAt(offset - 1)))
				{
					msg = "Illegal id char: " + charAt(offset - 1);
					break;
				}
				for(; offset < sentinel && isIdChar(_query.charAt(offset)); ++ offset)
					;
				String text = _query.substring(_startIndex, offset);
				if(keywords.contains(text))
					tokenType = TokenType.valueOf(text.toUpperCase());
				else
					tokenType = TokenType.ID;
			}
			
		}
		token.setType(tokenType);
		if(tokenType != TokenType.STRING)
			token.setText(_query.substring(_startIndex, offset));
		else
			token.setText(_query.substring(_startIndex + 1, offset - 1));
		token.setOffset(_startIndex);
		token.setErrorMessage(msg);
		_startIndex = offset;
		return token;
	}

	/**
	 * 判断XQL语句中是否还存在未分析的部分。
	 * 
	 * @return true如果存在下一个词法单元，否则返回false。
	 */
	public boolean hasNextToken() 
	{
		return _startIndex < _query.length();
	}
	
	/**
	 * 重置词法分析器，以使其可以从头开始分析XQL语句。
	 */
	public void reset()
	{
		_startIndex = 0;
	}
}
