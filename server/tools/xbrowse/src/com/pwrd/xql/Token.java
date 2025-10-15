package com.pwrd.xql;

/**
 * һ��<code>Token</code>����һ���ʷ���Ԫ��
 * 
 * @author ��ƽ(wangping101853)
 *
 */
public class Token 
{	
	private TokenType type;
	private String text;
	
	/** ��¼��Token�ڲ�ѯ����е�λ����Ϣ */
	private int offset; 
	
	/** 
	 *��{@link #type}Ϊ<code>TokenType.ILLEGAL</code>ʱ��
	 *��¼�ĳ�����Ϣ��
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
	 * @return ��<code>Token</code>�ڲ�ѯ�ַ�������ʼλ��
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
