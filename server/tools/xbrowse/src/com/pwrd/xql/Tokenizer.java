package com.pwrd.xql;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *һ���ǳ��򵥵�XQL�ʷ�������ʵ�֡��ôʷ���������֧��
 *<code>TokenType</code>�����������͡�
 * @see TokenType
 * 
 * @author ��ƽ��wangping101853��
 *
 */
public class Tokenizer 
{
	private static Log logger = LogFactory.getLog(Tokenizer.class);
	private static Set<String> keywords;
	private static Set<Character> idChars;
	
	/** ���ִʵ�XQL��� */
	private String _query;

	/** ��һ���ʷ���λ��ʼλ�� */
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
	 * ��<code>charAt</code>������һ���򵥰�װ��
	 * @param index - the index of the <code>char</code> value
	 * @return the char value at the specified index of <code>_query</code>.
	 */
	private char charAt(int index)
	{
		return _query.charAt(index);
	}

	/**
	 * �õ���һ���ʷ���Ԫ��
	 * 
	 * @return ��һ���ʷ���Ԫ��
	 * @exception IllegalStateException - ͨ��{@link #hasNextToken()}���ж��Ƿ񻹴���
	 * ��һ���ʷ���Ԫ����������ٵ��ø÷����������׳����쳣��
	 */
	public Token getNextToken() throws IllegalStateException
	{
		if(!hasNextToken())
		{
			logger.error("Please invoke hasNextToken each time before getNextToken");
			throw new IllegalStateException("Please invoke hasNextToken each time before getNextToken");
		}
		int sentinel = _query.length(); // �����ڱ������дʷ�����ʱ��Ҫ������λ��
		Token token = new Token();
		TokenType tokenType = TokenType.ILLEGAL;
		String msg = null;
		// ָ����һ����������ַ�
		int offset = _startIndex;
		
		// ���������һ��Ҫ��֤offsetָ����һ���ʷ���Ԫ����ʼλ�á�
		// һ��Ҫ��֤offsetС��sentinel��
		switch (charAt(offset ++)) 
		{
			case ' ':
			case '\t':
			case '\n':
			case '\f':
			case '\r': /** ����հ� */
			{
				for( ; offset < sentinel && Character.isWhitespace(charAt(offset)); ++ offset)
					;
				tokenType = TokenType.SPACE;
				break;
			}
			case '(': /** ��������� */
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
			case '=': /** �����ϵ����� */
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
			case '|': /** �����߼������ */
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
			case '\"': /** �����ַ��� */
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
				 * ��ʱ������Ψһԭ���������˷������ַ��������Щ
				 * �������ַ���[a-zA-Z_]����ô������ַǷ���
				 */
				while(offset < sentinel && isIdChar(charAt(offset)))
				{
					++ offset;
					tokenType = TokenType.ILLEGAL;
					msg = "illegal number at position " + _startIndex;
				}
				break;
			}
			default: /** �����ʶ���͹ؼ��֣�������ַ�Ӧ����[a-zA-Z_] */
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
	 * �ж�XQL������Ƿ񻹴���δ�����Ĳ��֡�
	 * 
	 * @return true���������һ���ʷ���Ԫ�����򷵻�false��
	 */
	public boolean hasNextToken() 
	{
		return _startIndex < _query.length();
	}
	
	/**
	 * ���ôʷ�����������ʹ����Դ�ͷ��ʼ����XQL��䡣
	 */
	public void reset()
	{
		_startIndex = 0;
	}
}
