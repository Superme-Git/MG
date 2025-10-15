package com.pwrd.xql;

/**
 *��ʾĿǰ�ʷ�������֧�ֵĴʷ���Ԫ���͡�
 * 
 * @author ��ƽ(wangping101853)
 *
 */
public enum TokenType 
{
	SEMI("semicolon", 0),					// ;
	LP("left_parenthesis", XQLParser.LP),	// (
	RP("right_parenthesis", XQLParser.RP),	// )
	LB("left_bracket", XQLParser.LB),		// [
	RB("right_bracket", XQLParser.RB),		// ]
	COMMA("comma", XQLParser.COMMA),		// ,
	
	/** ��ʶ����������ʽ��[a-zA-z_][a-zA-z0-9_]* */
	ID("identifier", XQLParser.ID),			// ��ʶ��
	
	/** ��ֵ������ */
	ASSIGN("assign", XQLParser.ASSIGN),		// =
	
	/** ��ϵ����� */
	NE("not_equal", XQLParser.NE),			// != 
	EQ("equal", XQLParser.EQ),				// ==
	GE("greater_equal", XQLParser.GE),		// >=
	GT("greater_than", XQLParser.GT),		// >
	LE("less_equal", XQLParser.LE),			// <=
	LT("less_than", XQLParser.LT),			// <
	
	
	/** �߼������ */
	AND("and", XQLParser.AND),				// &&
	OR("or", XQLParser.OR),					// ||
	
	/** 
	 * Ŀǰ��֧�ַǳ��򵥵��ַ������ֱ���"��"��Ϊ�ַ����Ŀ�ͷ�ͽ�β��
	 * �����ַ�������ֻ��Ϊ[a-zA-Z0-9_]��
	 */
	STRING("string", XQLParser.STRING),		// string
	
	DOT(".", XQLParser.DOT),				// .
	
	/** �ؼ��� */
	SELECT("select", XQLParser.SELECT),		// select
	UPDATE("update", XQLParser.UPDATE),		// update
	SET("set", XQLParser.SET),				// set
	DELETE("delete", XQLParser.DELETE),		// delete
	FROM("from", XQLParser.FROM),			// from
	WHERE("where", XQLParser.WHERE),		// where
	ORDER("order", XQLParser.ORDER),		// order
	BY("by", XQLParser.BY),					// by
	ASC("asc", XQLParser.ASC),				// asc
	DESC("desc", XQLParser.DESC),			// desc
	LIMIT("limit", XQLParser.LIMIT),		// limit
	KEY("key", XQLParser.KEY),				// key
	VALUE("value", XQLParser.VALUE),		// value
	IN("in", 0),							// in
	
	/** �����͸����� */
	LONG("long", XQLParser.LONG),		// long
	FLOAT("float", XQLParser.FLOAT),		// float
	
	SPACE("white_space", 0),				// �հף��ո��Ʊ���ͻ��з��ȣ�
	END_OF_FILE("end_of_file", 0),			// �ļ���ֹ��
	ILLEGAL("illegal", 0),					// �Ƿ��ʷ���Ԫ
	;
	
	private String description;
	/** 
	 * ��TokenType���﷨�������ж�Ӧ��ֵ�������TokenType
	 * ���﷨��������û�ж�Ӧ��ֵ���򽫸�ֵ��Ϊ0��
	 */
	private int value;
	
	TokenType(String desc)
	{
		description = desc;
	}
	
	TokenType(String desc, int _value)
	{
		this(desc);
		value = _value;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public int getValue()
	{
		return value;
	}
}
