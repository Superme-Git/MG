package com.pwrd.xql;

/**
 *表示目前词法分析器支持的词法单元类型。
 * 
 * @author 王平(wangping101853)
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
	
	/** 标识符的正则表达式：[a-zA-z_][a-zA-z0-9_]* */
	ID("identifier", XQLParser.ID),			// 标识符
	
	/** 赋值操作符 */
	ASSIGN("assign", XQLParser.ASSIGN),		// =
	
	/** 关系运算符 */
	NE("not_equal", XQLParser.NE),			// != 
	EQ("equal", XQLParser.EQ),				// ==
	GE("greater_equal", XQLParser.GE),		// >=
	GT("greater_than", XQLParser.GT),		// >
	LE("less_equal", XQLParser.LE),			// <=
	LT("less_than", XQLParser.LT),			// <
	
	
	/** 逻辑运算符 */
	AND("and", XQLParser.AND),				// &&
	OR("or", XQLParser.OR),					// ||
	
	/** 
	 * 目前仅支持非常简单的字符串，分别以"和"作为字符串的开头和结尾，
	 * 并且字符串内容只能为[a-zA-Z0-9_]。
	 */
	STRING("string", XQLParser.STRING),		// string
	
	DOT(".", XQLParser.DOT),				// .
	
	/** 关键字 */
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
	
	/** 整数和浮点数 */
	LONG("long", XQLParser.LONG),		// long
	FLOAT("float", XQLParser.FLOAT),		// float
	
	SPACE("white_space", 0),				// 空白（空格、制表符和换行符等）
	END_OF_FILE("end_of_file", 0),			// 文件终止符
	ILLEGAL("illegal", 0),					// 非法词法单元
	;
	
	private String description;
	/** 
	 * 该TokenType在语法解析器中对应的值，如果该TokenType
	 * 在语法解析器中没有对应的值，则将该值置为0。
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
