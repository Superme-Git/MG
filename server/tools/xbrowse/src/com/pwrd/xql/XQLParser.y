%{
/****************************************************************************
myparser.y
ParserWizard generated YACC file.

Date: 2011��8��11��
****************************************************************************/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
%}

/////////////////////////////////////////////////////////////////////////////
// declarations section

// parser name
%name XQLParser

// class members
{
		// place any extra class members here
		private static Log logger = LogFactory.getLog(XQLParser.class);
		private Tokenizer tokenizer;
		private XQLStatement xqlStmt;
}

// constructor
{
		// place any extra initialisation code here
		tokenizer = null;
		xqlStmt = null;
		// Ĭ�Ϲ��캯���޷�����Ҫ���������Ĵ��������
}

%start xql_stmt

// attribute type
%union {
		// public int value;
		public Token token;
		public Condition condition;
		public KVQualifiedField field;
		public OrderBy orderBy;			
		public Limit limit;				
		public Assign assign;			
		public AssignList assignList;	

		public void yycopy(yyattribute source, boolean move) 
		{
			YYSTYPE yy = (YYSTYPE)source;
			// value = yy.value;
			// Ŀǰֻ�Ǽ򵥵ĸ������ö��ѣ��Ժ������Ҫ֧�����ݿ���
			token = yy.token;
			condition = yy.condition;
			field = yy.field;
			orderBy = yy.orderBy;
			limit = yy.limit;
			assign = yy.assign;
			assignList = yy.assignList;
		}
}

// place any declarations here
// %token SEMI		// ;
%token LP		// (
%token RP		// )
%token LB		// [		
%token RB		// ]		
%token COMMA	// ,		
%token ID		// identifier
%token ASSIGN	// =		
%token NE		// !=
%token EQ		// ==
%token GT		// >
%token GE		// >=
%token LT		// <
%token LE		// <=
%token AND		// &&
%token OR		// ||
%token STRING	// String
%token DOT		// .
%token SELECT	// select
%token UPDATE	// update	
%token SET		// set		
%token DELETE	// delete	
%token FROM		// from
%token WHERE	// where
%token ORDER	// order	
%token BY		// by		
%token ASC		// asc		
%token DESC		// desc		
%token LIMIT	// limit	
%token KEY		// key
%token VALUE	// value
// %token IN		// in
%token LONG	// int
%token FLOAT	// float or double

// �ڴ�ֻ���г���Щ���ŵ����ȼ����ѣ���Щ���ȼ��ͽ���Ͳ�û�����ڽ����ͻ��
%left AND OR
%left NE EQ GE GT LE LT

%%

/////////////////////////////////////////////////////////////////////////////
// rules section

// place your YACC rules here (there must be at least one)
// ����ֻ�ṩÿ��Action��ռλ���������ʵ��������Java����֮�����
xql_stmt		: select_stmt
				{
					logger.info("The syntax of SELECT statement is correct.");
				}
				| update_stmt
				{
					logger.info("The syntax of UPDATE statement is correct.");
				}
				| delete_stmt
				{
					logger.info("The syntax of DELETE statement is correct.");
				}
				| error
				{
					logger.error("An syntax error has occured!");
				}
				;

/*
 * setTable����һ���������е��������
 */
select_stmt		: SELECT FROM ID
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID WHERE condition_list
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setCondition($5.condition);
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID order_by // 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setOrderBy($4.orderBy);	 
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID WHERE condition_list order_by	// 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setOrderBy($6.orderBy);	
					selectStmt.setCondition($5.condition); 
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID limit // 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setLimit($4.limit);
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID WHERE condition_list limit		// 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setLimit($6.limit);
					selectStmt.setCondition($5.condition);
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				| SELECT FROM ID order_by limit					// 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setLimit($5.limit);
					selectStmt.setOrderBy($4.orderBy);
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				} 
				| SELECT FROM ID WHERE condition_list order_by limit // 
				{
					XQLSelectStatement selectStmt = new XQLSelectStatement();
					selectStmt.setLimit($7.limit);
					selectStmt.setOrderBy($6.orderBy);
					selectStmt.setCondition($5.condition);
					selectStmt.setTable($3.token.getText());
					xqlStmt = selectStmt;
				}
				;

/**
 * update syntax	#2012-5-14
 * setTable����һ���������е��������
 */				
update_stmt		: UPDATE ID	SET assign_stmt_list
				{
					XQLUpdateStatement updateStmt = new XQLUpdateStatement();
					updateStmt.setAssignList($4.assignList);
					updateStmt.setTable($2.token.getText());
					xqlStmt = updateStmt;
				}
				| UPDATE ID SET assign_stmt_list WHERE condition_list
				{
					XQLUpdateStatement updateStmt = new XQLUpdateStatement();
					updateStmt.setAssignList($4.assignList);
					updateStmt.setCondition($6.condition);
					updateStmt.setTable($2.token.getText());
					xqlStmt = updateStmt;
				}
				;
				
/**
 * delete syntax	#2012-5-14
 * setTable����һ���������е��������
 */				
delete_stmt		: DELETE FROM ID
				{
					XQLDeleteStatement deleteStmt = new XQLDeleteStatement();
					deleteStmt.setTable($3.token.getText());
					xqlStmt = deleteStmt;
				}
				| DELETE FROM ID WHERE condition_list
				{
					XQLDeleteStatement deleteStmt = new XQLDeleteStatement();
					deleteStmt.setCondition($5.condition);
					deleteStmt.setTable($3.token.getText());
					xqlStmt = deleteStmt;
				}
				;
			
/*
 * ���Ƕ����		
condition_list	: LP condition_list RP
				{
					System.out.println("condition_list : LP condition_list RP");
					$$.value = $1.value + $2.value + $3.value;
				}
				;
*/ 			
condition_list	: condition
				{
					$$.condition = $1.condition;
				}
				| condition_list AND condition
				{
					CompositeCondition condition = new CompositeCondition();
					condition.setLeftCond($1.condition);
					condition.setOperator(TokenType.AND);
					condition.setRightCond($3.condition);
					$$.condition = condition;
				}
				| condition_list OR condition
				{
					CompositeCondition condition = new CompositeCondition();
					condition.setLeftCond($1.condition);
					condition.setOperator(TokenType.OR);
					condition.setRightCond($3.condition);
					$$.condition = condition;
				}
				;

/*
 * �����ڱ��ʽ INTEGER NE VALUE����ʱyacc��֪���ǽ�INTEGER��Լ��constant
 * Ȼ��ִ��constant NE VALUE���������� INTEGER NE VALUE��Ȼ���ٽ��й�Լ��
 * ��������Լ����INTEGER NE KEY�ǶԵģ��ȹ�Լ���������INTEGER NE VALUE
 * ���Ǵ���ġ����ڳ���shift-reduce conflictʱ��yaccĬ�ϲ�����������Լ
 * �ķ�ʽ���������������ó�ͻ�����﷨�������޷�ʶ��INTEGER NE VALUE��
 * ͨ����INTEGER NE KEY�ĳ�constant NE KEY�Ϳ��Խ�������ͻ������KEY�Ƿ�
 * ����FLOAT��STRING�Ƚϣ������������ʱ���ټ��ɡ�
 */
condition		: LP condition RP
				{
					$$.condition = $2.condition;
				}
				| kv_field NE constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.NE);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant NE kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.NE);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				
				| kv_field EQ constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.EQ);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant EQ kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.EQ);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				| kv_field GE constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.GE);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant GE kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.LE);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				
				| kv_field GT constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.GT);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant GT kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.LT);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				| kv_field LE constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.LE);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant LE kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.GE);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				| kv_field LT constant
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($1.field);
					cond.setOperator(TokenType.LT);
					cond.setRightOperand($3.token);
					$$.condition = cond;
				}
				| constant LT kv_field
				{
					SimpleCondition cond = new SimpleCondition();
					cond.setLeftOperand($3.field);
					cond.setOperator(TokenType.GT);
					cond.setRightOperand($1.token);
					$$.condition = cond;
				}
				;

/**
 * set syntax	#2012-5-14
 */
assign_stmt_list: assign_stmt
				{
					AssignList assignList = new AssignList();
					assignList.addAssign($1.assign);
					$$.assignList = assignList;
				}
				| assign_stmt_list COMMA assign_stmt
				{
					$1.assignList.addAssign($3.assign);
					$$.assignList = $1.assignList;
				}
				;
				
assign_stmt		: LP assign_stmt RP
				{
					$$.assign = $2.assign;
				}
				| kv_field ASSIGN constant
				{
					Assign assign = new Assign();
					assign.setLeftOperand($1.field);
					assign.setRightOperand($3.token);
					$$.assign = assign;
				}
				; 


order_by		: ORDER BY kv_field
				{
					$$.orderBy = new OrderBy($3.field, true);
				}
				| ORDER BY kv_field ASC
				{
					$$.orderBy = new OrderBy($3.field, true);
				}
				| ORDER BY kv_field DESC
				{
					$$.orderBy = new OrderBy($3.field, false);
				}
				;
/*
 *
 */				
kv_field		: LP kv_field RP
				{
					$$.field = $2.field;
				}
				| KEY
				{
					KVQualifiedField field = new KVQualifiedField();
					field.addQualification($1.token.getText());
					$$.field = field;
				}
				| VALUE
				{
					KVQualifiedField field = new KVQualifiedField();
					field.addQualification(yyattribute(1 - 1).token.getText());
					$$.field = field;
				}
				| kv_field DOT ID
				{
					$1.field.addQualification($3.token.getText());
					$$.field = $1.field;
				}
				| kv_field index_stmt
				{
					$1.field.addQualification($2.token.getText());
					$$.field = $1.field;
				}
				;
				
index_stmt		: LB constant RB
				{
					$$.token = $2.token;
				}
				;

/*
 *
 */				
constant		: LP constant RP 
				{
					$$.token = $2.token;
				}
				| LONG
				{
					$$.token = $1.token;
				}
				| FLOAT
				{
					$$.token = $1.token;
				}
				| STRING
				{
					$$.token = $1.token;
				}
				;
				

limit			: LIMIT LONG COMMA LONG
				{
					$$.limit = new Limit(Long.parseLong($2.token.getText()), Long.parseLong($4.token.getText()));
				}
				;
				
%%

/////////////////////////////////////////////////////////////////////////////
// programs section

	public int yygettoken() 
	{
		// place your token retrieving code here
		YYSTYPE yylval = (YYSTYPE) yylvalref;
		while (tokenizer.hasNextToken()) 
		{
			Token token = tokenizer.getNextToken();
			if (token.getType() == TokenType.SPACE)
				continue;
			yylval.token = token;
			// yylval.value = token.getType().getValue();
			return token.getType().getValue();
		}
		
		return 0;
	}

	public static void main(String args[]) 
	{
		int n = 1;
		XQLParser parser = new XQLParser();
		if (parser.yycreate(null)) {
			n = parser.yyparse();
		}
		System.exit(n);
	}
	
	public XQLStatement getXqlStatement() 
	{
		return xqlStmt;
	}

	public void setTokenizer(Tokenizer _tokenizer) 
	{
		tokenizer = _tokenizer;
	}
	
	public boolean parse()
	{
		int n = 1;
		if (yycreate(null)) 
			n = yyparse();
		return n == 0;
	}

	public boolean parse(Tokenizer _tokenizer)
	{
		setTokenizer(_tokenizer);
		return parse();
	}
}

