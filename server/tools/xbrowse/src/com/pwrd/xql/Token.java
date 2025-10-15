package com.pwrd.xql;

/**
 * 一个<code>Token</code>代表一个词法单元。
 * 
 * @author 王平(wangping101853)
 *
 */
public class Token 
{	
	private TokenType type;
	private String text;
	
	/** 记录该Token在查询语句中的位置信息 */
	private int offset; 
	
	/** 
	 *当{@link #type}为<code>TokenType.ILLEGAL</code>时，
	 *记录的出错消息。
	 */
	private String errorMessage;
	
	public Token()
	{
	}
	
	public Token(TokenType _type, String _text)
	{
		type = _type;
		text = _text;
	}
	
	public Token(TokenType _type, String _text, int _offset)
	{
		this(_type, _text);
		offset = _offset;
	}
	
	public TokenType getType()
	{
		return type;
	}
	
	public void setType(TokenType _type)
	{
		type = _type;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String _text)
	{
		text = _text;
	}
	
	/**
	 * 
	 * @return 该<code>Token</code>在查询字符串的起始位置
	 */
	public int getOffset()
	{
		return offset;
	}
	
	public void setOffset(int _offset)
	{
		offset = _offset;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String msg) {
		errorMessage = msg;
	}
	
	public String toString()
	{
		return "TokenType is: " + type.name() + ", content is: " + text 
				+ ", offset is: " + offset + ", length is: " + text.length()
				+ (errorMessage != null ? "," + errorMessage : "");
	}
}
