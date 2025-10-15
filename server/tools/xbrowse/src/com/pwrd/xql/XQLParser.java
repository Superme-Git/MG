package com.pwrd.xql;

/****************************************************************************
 *                         A C A D E M I C   C O P Y
 * 
 * This file was produced by an ACADEMIC COPY of Parser Generator. It is for
 * use in non-commercial software only. An ACADEMIC COPY of Parser Generator
 * can only be used by a student, or a member of an academic community. If
 * however you want to use Parser Generator for commercial purposes then you
 * will need to purchase a license. For more information see the online help or
 * go to the Bumble-Bee Software homepage at:
 * 
 * http://www.bumblebeesoftware.com
 * 
 * This notice must remain present in the file. It cannot be removed.
 ****************************************************************************/

/****************************************************************************
 * myparser.java
 * Java source file generated from myparser.y.
 * 
 * Date: 05/14/12
 * Time: 16:51:26
 * 
 * AYACC Version: 2.07
 ****************************************************************************/

// line 1 ".\\myparser.y"

/****************************************************************************
 myparser.y
 ParserWizard generated YACC file.

 Date: 2011年8月11日
 ****************************************************************************/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// line 38 "myparser.java"
import yl.*;

/////////////////////////////////////////////////////////////////////////////
// XQLParser

public class XQLParser extends yyfparser
{
	// line 19 ".\\myparser.y"

	// place any extra class members here
	private static Log logger = LogFactory.getLog(XQLParser.class);
	private Tokenizer tokenizer;
	private XQLStatement xqlStmt;

	// line 52 "myparser.java"
	public XQLParser()
	{
		yytables();
		// line 27 ".\\myparser.y"

		// place any extra initialisation code here
		tokenizer = null;
		xqlStmt = null;
		// 默认构造函数无法满足要求，在生产的代码上添加

		// line 62 "myparser.java"
	}
	
	public XQLParser(Tokenizer _tokenizer)
	{
		this();
		setTokenizer(_tokenizer);
	}
	
	public class YYSTYPE extends yyattribute
	{
		// line 37 ".\\myparser.y"

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
			YYSTYPE yy = (YYSTYPE) source;
			// value = yy.value;
			// 目前只是简单的复制引用而已，以后可能需要支持数据拷贝
			token = yy.token;
			condition = yy.condition;
			field = yy.field;
			orderBy = yy.orderBy;
			limit = yy.limit;
			assign = yy.assign;
			assignList = yy.assignList;
		}

		// line 91 "myparser.java"
	}

	public static final int LP = 65537;
	public static final int RP = 65538;
	public static final int LB = 65539;
	public static final int RB = 65540;
	public static final int COMMA = 65541;
	public static final int ID = 65542;
	public static final int ASSIGN = 65543;
	public static final int NE = 65544;
	public static final int EQ = 65545;
	public static final int GT = 65546;
	public static final int GE = 65547;
	public static final int LT = 65548;
	public static final int LE = 65549;
	public static final int AND = 65550;
	public static final int OR = 65551;
	public static final int STRING = 65552;
	public static final int DOT = 65553;
	public static final int SELECT = 65554;
	public static final int UPDATE = 65555;
	public static final int SET = 65556;
	public static final int DELETE = 65557;
	public static final int FROM = 65558;
	public static final int WHERE = 65559;
	public static final int ORDER = 65560;
	public static final int BY = 65561;
	public static final int ASC = 65562;
	public static final int DESC = 65563;
	public static final int LIMIT = 65564;
	public static final int KEY = 65565;
	public static final int VALUE = 65566;
	public static final int LONG = 65567;
	public static final int FLOAT = 65568;

	protected final YYSTYPE yyattribute(int index)
	{
		YYSTYPE attribute = (YYSTYPE) yyattributestackref[yytop + index];
		return attribute;
	}

	protected final yyattribute yynewattribute()
	{
		return new YYSTYPE();
	}

	protected final YYSTYPE[] yyinitdebug(int count)
	{
		YYSTYPE a[] = new YYSTYPE[count];
		for (int i = 0; i < count; i++)
		{
			a[i] = (YYSTYPE) yyattributestackref[yytop + i - (count - 1)];
		}
		return a;
	}

	protected static yyftables yytables = null;

	@SuppressWarnings("unused")
	public final void yyaction(int action)
	{
		switch (action)
		{
		case 0:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 110 ".\\myparser.y"

			logger.info("The syntax of SELECT statement is correct.");

			// line 157 "myparser.java"
		}
			break;
		case 1:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 114 ".\\myparser.y"

			logger.info("The syntax of UPDATE statement is correct.");

			// line 170 "myparser.java"
		}
			break;
		case 2:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 118 ".\\myparser.y"

			logger.info("The syntax of DELETE statement is correct.");

			// line 183 "myparser.java"
		}
			break;
		case 3:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 122 ".\\myparser.y"

			logger.error("An syntax error has occured!");

			// line 196 "myparser.java"
		}
			break;
		case 4:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 131 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setTable(yyattribute(3 - 3).token.getText());
			xqlStmt = selectStmt;

			// line 211 "myparser.java"
		}
			break;
		case 5:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(6);
			}
			// line 137 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setCondition(yyattribute(5 - 5).condition);
			selectStmt.setTable(yyattribute(3 - 5).token.getText());
			xqlStmt = selectStmt;

			// line 227 "myparser.java"
		}
			break;
		case 6:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 144 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setOrderBy(yyattribute(4 - 4).orderBy);
			selectStmt.setTable(yyattribute(3 - 4).token.getText());
			xqlStmt = selectStmt;

			// line 243 "myparser.java"
		}
			break;
		case 7:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(7);
			}
			// line 151 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setOrderBy(yyattribute(6 - 6).orderBy);
			selectStmt.setCondition(yyattribute(5 - 6).condition);
			selectStmt.setTable(yyattribute(3 - 6).token.getText());
			xqlStmt = selectStmt;

			// line 260 "myparser.java"
		}
			break;
		case 8:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 159 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setLimit(yyattribute(4 - 4).limit);
			selectStmt.setTable(yyattribute(3 - 4).token.getText());
			xqlStmt = selectStmt;

			// line 276 "myparser.java"
		}
			break;
		case 9:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(7);
			}
			// line 166 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setLimit(yyattribute(6 - 6).limit);
			selectStmt.setCondition(yyattribute(5 - 6).condition);
			selectStmt.setTable(yyattribute(3 - 6).token.getText());
			xqlStmt = selectStmt;

			// line 293 "myparser.java"
		}
			break;
		case 10:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(6);
			}
			// line 174 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setLimit(yyattribute(5 - 5).limit);
			selectStmt.setOrderBy(yyattribute(4 - 5).orderBy);
			selectStmt.setTable(yyattribute(3 - 5).token.getText());
			xqlStmt = selectStmt;

			// line 310 "myparser.java"
		}
			break;
		case 11:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(8);
			}
			// line 182 ".\\myparser.y"

			XQLSelectStatement selectStmt = new XQLSelectStatement();
			selectStmt.setLimit(yyattribute(7 - 7).limit);
			selectStmt.setOrderBy(yyattribute(6 - 7).orderBy);
			selectStmt.setCondition(yyattribute(5 - 7).condition);
			selectStmt.setTable(yyattribute(3 - 7).token.getText());
			xqlStmt = selectStmt;

			// line 328 "myparser.java"
		}
			break;
		case 12:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 196 ".\\myparser.y"

			XQLUpdateStatement updateStmt = new XQLUpdateStatement();
			updateStmt.setAssignList(yyattribute(4 - 4).assignList);
			updateStmt.setTable(yyattribute(2 - 4).token.getText());
			xqlStmt = updateStmt;

			// line 344 "myparser.java"
		}
			break;
		case 13:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(7);
			}
			// line 203 ".\\myparser.y"

			XQLUpdateStatement updateStmt = new XQLUpdateStatement();
			updateStmt.setAssignList(yyattribute(4 - 6).assignList);
			updateStmt.setCondition(yyattribute(6 - 6).condition);
			updateStmt.setTable(yyattribute(2 - 6).token.getText());
			xqlStmt = updateStmt;

			// line 361 "myparser.java"
		}
			break;
		case 14:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 216 ".\\myparser.y"

			XQLDeleteStatement deleteStmt = new XQLDeleteStatement();
			deleteStmt.setTable(yyattribute(3 - 3).token.getText());
			xqlStmt = deleteStmt;

			// line 376 "myparser.java"
		}
			break;
		case 15:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(6);
			}
			// line 222 ".\\myparser.y"

			XQLDeleteStatement deleteStmt = new XQLDeleteStatement();
			deleteStmt.setCondition(yyattribute(5 - 5).condition);
			deleteStmt.setTable(yyattribute(3 - 5).token.getText());
			xqlStmt = deleteStmt;
			
			// line 391 "myparser.java"
		}
			break;
		case 16:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 239 ".\\myparser.y"

			((YYSTYPE) yyvalref).condition = yyattribute(1 - 1).condition;

			// line 404 "myparser.java"
		}
			break;
		case 17:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 243 ".\\myparser.y"

			CompositeCondition condition = new CompositeCondition();
			condition.setLeftCond(yyattribute(1 - 3).condition);
			condition.setOperator(TokenType.AND);
			condition.setRightCond(yyattribute(3 - 3).condition);
			((YYSTYPE) yyvalref).condition = condition;

			// line 421 "myparser.java"
		}
			break;
		case 18:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 251 ".\\myparser.y"

			CompositeCondition condition = new CompositeCondition();
			condition.setLeftCond(yyattribute(1 - 3).condition);
			condition.setOperator(TokenType.OR);
			condition.setRightCond(yyattribute(3 - 3).condition);
			((YYSTYPE) yyvalref).condition = condition;

			// line 438 "myparser.java"
		}
			break;
		case 19:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 270 ".\\myparser.y"

			((YYSTYPE) yyvalref).condition = yyattribute(2 - 3).condition;

			// line 451 "myparser.java"
		}
			break;
		case 20:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 274 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.NE);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 468 "myparser.java"
		}
			break;
		case 21:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 282 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.NE);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 485 "myparser.java"
		}
			break;
		case 22:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 291 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.EQ);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 502 "myparser.java"
		}
			break;
		case 23:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 299 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.EQ);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 519 "myparser.java"
		}
			break;
		case 24:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 307 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.GE);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 536 "myparser.java"
		}
			break;
		case 25:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 315 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.LE);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 553 "myparser.java"
		}
			break;
		case 26:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 324 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.GT);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 570 "myparser.java"
		}
			break;
		case 27:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 332 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.LT);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 587 "myparser.java"
		}
			break;
		case 28:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 340 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.LE);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 604 "myparser.java"
		}
			break;
		case 29:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 348 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.GE);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 621 "myparser.java"
		}
			break;
		case 30:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 356 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(1 - 3).field);
			cond.setOperator(TokenType.LT);
			cond.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 638 "myparser.java"
		}
			break;
		case 31:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 364 ".\\myparser.y"

			SimpleCondition cond = new SimpleCondition();
			cond.setLeftOperand(yyattribute(3 - 3).field);
			cond.setOperator(TokenType.GT);
			cond.setRightOperand(yyattribute(1 - 3).token);
			((YYSTYPE) yyvalref).condition = cond;

			// line 655 "myparser.java"
		}
			break;
		case 32:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 377 ".\\myparser.y"

			AssignList assignList = new AssignList();
			assignList.addAssign(yyattribute(1 - 1).assign);
			((YYSTYPE) yyvalref).assignList = assignList;

			// line 670 "myparser.java"
		}
			break;
		case 33:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 383 ".\\myparser.y"

			yyattribute(1 - 3).assignList.addAssign(yyattribute(3 - 3).assign);
			((YYSTYPE) yyvalref).assignList = yyattribute(1 - 3).assignList;

			// line 684 "myparser.java"
		}
			break;
		case 34:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 390 ".\\myparser.y"

			((YYSTYPE) yyvalref).assign = yyattribute(2 - 3).assign;

			// line 697 "myparser.java"
		}
			break;
		case 35:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 394 ".\\myparser.y"

			Assign assign = new Assign();
			assign.setLeftOperand(yyattribute(1 - 3).field);
			assign.setRightOperand(yyattribute(3 - 3).token);
			((YYSTYPE) yyvalref).assign = assign;

			// line 713 "myparser.java"
		}
			break;
		case 36:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 407 ".\\myparser.y"

			((YYSTYPE) yyvalref).orderBy = new OrderBy(
					yyattribute(3 - 3).field, true);

			// line 726 "myparser.java"
		}
			break;
		case 37:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 411 ".\\myparser.y"

			((YYSTYPE) yyvalref).orderBy = new OrderBy(
					yyattribute(3 - 4).field, true);

			// line 739 "myparser.java"
		}
			break;
		case 38:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 415 ".\\myparser.y"

			((YYSTYPE) yyvalref).orderBy = new OrderBy(
					yyattribute(3 - 4).field, false);

			// line 752 "myparser.java"
		}
			break;
		case 39:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 423 ".\\myparser.y"

			((YYSTYPE) yyvalref).field = yyattribute(2 - 3).field;

			// line 765 "myparser.java"
		}
			break;
		case 40:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 427 ".\\myparser.y"

			KVQualifiedField field = new KVQualifiedField();
			field.addQualification(yyattribute(1 - 1).token.getText());
			((YYSTYPE) yyvalref).field = field;

			// line 780 "myparser.java"
		}
			break;
		case 41:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 433 ".\\myparser.y"

			KVQualifiedField field = new KVQualifiedField();
			field.addQualification(yyattribute(1 - 1).token.getText());
			((YYSTYPE) yyvalref).field = field;

			// line 795 "myparser.java"
		}
			break;
		case 42:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 439 ".\\myparser.y"

			yyattribute(1 - 3).field.addQualification(yyattribute(3 - 3).token
					.getText());
			((YYSTYPE) yyvalref).field = yyattribute(1 - 3).field;

			// line 809 "myparser.java"
		}
			break;
		case 43:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(3);
			}
			// line 444 ".\\myparser.y"

			yyattribute(1 - 2).field.addQualification(yyattribute(2 - 2).token
					.getText());
			((YYSTYPE) yyvalref).field = yyattribute(1 - 2).field;

			// line 823 "myparser.java"
		}
			break;
		case 44:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 451 ".\\myparser.y"

			((YYSTYPE) yyvalref).token = yyattribute(2 - 3).token;

			// line 836 "myparser.java"
		}
			break;
		case 45:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(4);
			}
			// line 460 ".\\myparser.y"

			((YYSTYPE) yyvalref).token = yyattribute(2 - 3).token;

			// line 849 "myparser.java"
		}
			break;
		case 46:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 464 ".\\myparser.y"

			((YYSTYPE) yyvalref).token = yyattribute(1 - 1).token;

			// line 862 "myparser.java"
		}
			break;
		case 47:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 468 ".\\myparser.y"

			((YYSTYPE) yyvalref).token = yyattribute(1 - 1).token;

			// line 875 "myparser.java"
		}
			break;
		case 48:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(2);
			}
			// line 472 ".\\myparser.y"

			((YYSTYPE) yyvalref).token = yyattribute(1 - 1).token;

			// line 888 "myparser.java"
		}
			break;
		case 49:
		{
			YYSTYPE yya[];
			if (YYDEBUG)
			{
				yya = yyinitdebug(5);
			}
			// line 482 ".\\myparser.y"

			((YYSTYPE) yyvalref).limit = new Limit(
					Long.parseLong(yyattribute(2 - 4).token.getText()),
					Long.parseLong(yyattribute(4 - 4).token.getText()));

			// line 901 "myparser.java"
		}
			break;
		default:
			break;
		}
	}

	protected final void yytables()
	{
		yysstack_size = 100;
		yystack_max = 0;

		yycreatetables();
		yysymbol = yytables.yysymbol;
		yyrule = yytables.yyrule;
		yyreduction = yytables.yyreduction;
		yytokenaction = yytables.yytokenaction;
		yystateaction = yytables.yystateaction;
		yynontermgoto = yytables.yynontermgoto;
		yystategoto = yytables.yystategoto;
		yydestructorref = yytables.yydestructorref;
		yytokendestref = yytables.yytokendestref;
		yytokendestbaseref = yytables.yytokendestbaseref;
	}

	public static synchronized final void yycreatetables()
	{
		if (yytables == null)
		{
			yytables = new yyftables();

			if (YYDEBUG)
			{
				final yysymbol symbol[] = { new yysymbol("$end", 0),
						new yysymbol("error", 65536),
						new yysymbol("LP", 65537), new yysymbol("RP", 65538),
						new yysymbol("LB", 65539), new yysymbol("RB", 65540),
						new yysymbol("COMMA", 65541),
						new yysymbol("ID", 65542),
						new yysymbol("ASSIGN", 65543),
						new yysymbol("NE", 65544), new yysymbol("EQ", 65545),
						new yysymbol("GT", 65546), new yysymbol("GE", 65547),
						new yysymbol("LT", 65548), new yysymbol("LE", 65549),
						new yysymbol("AND", 65550), new yysymbol("OR", 65551),
						new yysymbol("STRING", 65552),
						new yysymbol("DOT", 65553),
						new yysymbol("SELECT", 65554),
						new yysymbol("UPDATE", 65555),
						new yysymbol("SET", 65556),
						new yysymbol("DELETE", 65557),
						new yysymbol("FROM", 65558),
						new yysymbol("WHERE", 65559),
						new yysymbol("ORDER", 65560),
						new yysymbol("BY", 65561), new yysymbol("ASC", 65562),
						new yysymbol("DESC", 65563),
						new yysymbol("LIMIT", 65564),
						new yysymbol("KEY", 65565),
						new yysymbol("VALUE", 65566),
						new yysymbol("LONG", 65567),
						new yysymbol("FLOAT", 65568), new yysymbol(null, 0) };
				yytables.yysymbol = symbol;

				final String rule[] = {
						"$accept: xql_stmt",
						"xql_stmt: select_stmt",
						"xql_stmt: update_stmt",
						"xql_stmt: delete_stmt",
						"xql_stmt: error",
						"select_stmt: SELECT FROM ID",
						"select_stmt: SELECT FROM ID WHERE condition_list",
						"select_stmt: SELECT FROM ID order_by",
						"select_stmt: SELECT FROM ID WHERE condition_list order_by",
						"select_stmt: SELECT FROM ID limit",
						"select_stmt: SELECT FROM ID WHERE condition_list limit",
						"select_stmt: SELECT FROM ID order_by limit",
						"select_stmt: SELECT FROM ID WHERE condition_list order_by limit",
						"update_stmt: UPDATE ID SET assign_stmt_list",
						"update_stmt: UPDATE ID SET assign_stmt_list WHERE condition_list",
						"delete_stmt: DELETE FROM ID",
						"delete_stmt: DELETE FROM ID WHERE condition_list",
						"condition_list: condition",
						"condition_list: condition_list AND condition",
						"condition_list: condition_list OR condition",
						"condition: LP condition RP",
						"condition: kv_field NE constant",
						"condition: constant NE kv_field",
						"condition: kv_field EQ constant",
						"condition: constant EQ kv_field",
						"condition: kv_field GE constant",
						"condition: constant GE kv_field",
						"condition: kv_field GT constant",
						"condition: constant GT kv_field",
						"condition: kv_field LE constant",
						"condition: constant LE kv_field",
						"condition: kv_field LT constant",
						"condition: constant LT kv_field",
						"assign_stmt_list: assign_stmt",
						"assign_stmt_list: assign_stmt_list COMMA assign_stmt",
						"assign_stmt: LP assign_stmt RP",
						"assign_stmt: kv_field ASSIGN constant",
						"order_by: ORDER BY kv_field",
						"order_by: ORDER BY kv_field ASC",
						"order_by: ORDER BY kv_field DESC",
						"kv_field: LP kv_field RP", "kv_field: KEY",
						"kv_field: VALUE", "kv_field: kv_field DOT ID",
						"kv_field: kv_field index_stmt",
						"index_stmt: LB constant RB",
						"constant: LP constant RP", "constant: LONG",
						"constant: FLOAT", "constant: STRING",
						"limit: LIMIT LONG COMMA LONG" };
				yytables.yyrule = rule;
			}

			final yyreduction reduction[] = { new yyreduction(0, 1, -1),
					new yyreduction(1, 1, 0), new yyreduction(1, 1, 1),
					new yyreduction(1, 1, 2), new yyreduction(1, 1, 3),
					new yyreduction(2, 3, 4), new yyreduction(2, 5, 5),
					new yyreduction(2, 4, 6), new yyreduction(2, 6, 7),
					new yyreduction(2, 4, 8), new yyreduction(2, 6, 9),
					new yyreduction(2, 5, 10), new yyreduction(2, 7, 11),
					new yyreduction(3, 4, 12), new yyreduction(3, 6, 13),
					new yyreduction(4, 3, 14), new yyreduction(4, 5, 15),
					new yyreduction(5, 1, 16), new yyreduction(5, 3, 17),
					new yyreduction(5, 3, 18), new yyreduction(6, 3, 19),
					new yyreduction(6, 3, 20), new yyreduction(6, 3, 21),
					new yyreduction(6, 3, 22), new yyreduction(6, 3, 23),
					new yyreduction(6, 3, 24), new yyreduction(6, 3, 25),
					new yyreduction(6, 3, 26), new yyreduction(6, 3, 27),
					new yyreduction(6, 3, 28), new yyreduction(6, 3, 29),
					new yyreduction(6, 3, 30), new yyreduction(6, 3, 31),
					new yyreduction(7, 1, 32), new yyreduction(7, 3, 33),
					new yyreduction(8, 3, 34), new yyreduction(8, 3, 35),
					new yyreduction(9, 3, 36), new yyreduction(9, 4, 37),
					new yyreduction(9, 4, 38), new yyreduction(10, 3, 39),
					new yyreduction(10, 1, 40), new yyreduction(10, 1, 41),
					new yyreduction(10, 3, 42), new yyreduction(10, 2, 43),
					new yyreduction(11, 3, 44), new yyreduction(12, 3, 45),
					new yyreduction(12, 1, 46), new yyreduction(12, 1, 47),
					new yyreduction(12, 1, 48), new yyreduction(13, 4, 49) };
			yytables.yyreduction = reduction;

			final yytokenaction tokenaction[] = {
					new yytokenaction(73, YYAT_SHIFT, 73),
					new yytokenaction(0, YYAT_SHIFT, 1),
					new yytokenaction(67, YYAT_SHIFT, 42),
					new yytokenaction(23, YYAT_SHIFT, 40),
					new yytokenaction(66, YYAT_SHIFT, 66),
					new yytokenaction(98, YYAT_SHIFT, 78),
					new yytokenaction(51, YYAT_SHIFT, 27),
					new yytokenaction(49, YYAT_SHIFT, 60),
					new yytokenaction(49, YYAT_SHIFT, 61),
					new yytokenaction(49, YYAT_SHIFT, 62),
					new yytokenaction(49, YYAT_SHIFT, 63),
					new yytokenaction(49, YYAT_SHIFT, 64),
					new yytokenaction(49, YYAT_SHIFT, 65),
					new yytokenaction(31, YYAT_SHIFT, 50),
					new yytokenaction(31, YYAT_SHIFT, 51),
					new yytokenaction(73, YYAT_SHIFT, 28),
					new yytokenaction(67, YYAT_SHIFT, 44),
					new yytokenaction(72, YYAT_SHIFT, 50),
					new yytokenaction(72, YYAT_SHIFT, 51),
					new yytokenaction(0, YYAT_SHIFT, 2),
					new yytokenaction(0, YYAT_SHIFT, 3),
					new yytokenaction(23, YYAT_SHIFT, 41),
					new yytokenaction(0, YYAT_SHIFT, 4),
					new yytokenaction(31, YYAT_SHIFT, 16),
					new yytokenaction(74, YYAT_SHIFT, 99),
					new yytokenaction(67, YYAT_SHIFT, 95),
					new yytokenaction(67, YYAT_SHIFT, 96),
					new yytokenaction(31, YYAT_SHIFT, 17),
					new yytokenaction(94, YYAT_SHIFT, 69),
					new yytokenaction(94, YYAT_SHIFT, 42),
					new yytokenaction(73, YYAT_SHIFT, 29),
					new yytokenaction(73, YYAT_SHIFT, 30),
					new yytokenaction(66, YYAT_SHIFT, 21),
					new yytokenaction(66, YYAT_SHIFT, 22),
					new yytokenaction(51, YYAT_SHIFT, 21),
					new yytokenaction(51, YYAT_SHIFT, 22),
					new yytokenaction(48, YYAT_SHIFT, 54),
					new yytokenaction(48, YYAT_SHIFT, 55),
					new yytokenaction(48, YYAT_SHIFT, 56),
					new yytokenaction(48, YYAT_SHIFT, 57),
					new yytokenaction(48, YYAT_SHIFT, 58),
					new yytokenaction(48, YYAT_SHIFT, 59),
					new yytokenaction(93, YYAT_SHIFT, 42),
					new yytokenaction(94, YYAT_SHIFT, 44),
					new yytokenaction(92, YYAT_SHIFT, 42),
					new yytokenaction(91, YYAT_SHIFT, 42),
					new yytokenaction(90, YYAT_SHIFT, 42),
					new yytokenaction(89, YYAT_SHIFT, 42),
					new yytokenaction(88, YYAT_SHIFT, 42),
					new yytokenaction(12, YYAT_SHIFT, 15),
					new yytokenaction(12, YYAT_SHIFT, 16),
					new yytokenaction(46, YYAT_SHIFT, 50),
					new yytokenaction(46, YYAT_SHIFT, 51),
					new yytokenaction(68, YYAT_SHIFT, 97),
					new yytokenaction(12, YYAT_SHIFT, 17),
					new yytokenaction(52, YYAT_SHIFT, 17),
					new yytokenaction(93, YYAT_SHIFT, 44),
					new yytokenaction(47, YYAT_SHIFT, 77),
					new yytokenaction(92, YYAT_SHIFT, 44),
					new yytokenaction(91, YYAT_SHIFT, 44),
					new yytokenaction(90, YYAT_SHIFT, 44),
					new yytokenaction(89, YYAT_SHIFT, 44),
					new yytokenaction(88, YYAT_SHIFT, 44),
					new yytokenaction(44, YYAT_SHIFT, 76),
					new yytokenaction(40, YYAT_SHIFT, 20),
					new yytokenaction(39, YYAT_SHIFT, 70),
					new yytokenaction(38, YYAT_SHIFT, 43),
					new yytokenaction(36, YYAT_SHIFT, 68),
					new yytokenaction(34, YYAT_ERROR, 0),
					new yytokenaction(33, YYAT_ERROR, 0),
					new yytokenaction(24, YYAT_ERROR, 0),
					new yytokenaction(18, YYAT_SHIFT, 17),
					new yytokenaction(17, YYAT_SHIFT, 36),
					new yytokenaction(16, YYAT_SHIFT, 35),
					new yytokenaction(14, YYAT_SHIFT, 26),
					new yytokenaction(11, YYAT_SHIFT, 14),
					new yytokenaction(10, YYAT_SHIFT, 13),
					new yytokenaction(9, YYAT_SHIFT, 12),
					new yytokenaction(5, YYAT_ACCEPT, 0),
					new yytokenaction(4, YYAT_SHIFT, 11),
					new yytokenaction(3, YYAT_SHIFT, 10),
					new yytokenaction(2, YYAT_SHIFT, 9) };
			yytables.yytokenaction = tokenaction;

			final yystateaction stateaction[] = {
					new yystateaction(-65535, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 4),
					new yystateaction(-65477, true, YYAT_ERROR, 0),
					new yystateaction(-65462, true, YYAT_ERROR, 0),
					new yystateaction(-65479, true, YYAT_ERROR, 0),
					new yystateaction(78, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 1),
					new yystateaction(0, false, YYAT_REDUCE, 2),
					new yystateaction(0, false, YYAT_REDUCE, 3),
					new yystateaction(-65465, true, YYAT_ERROR, 0),
					new yystateaction(-65480, true, YYAT_ERROR, 0),
					new yystateaction(-65467, true, YYAT_ERROR, 0),
					new yystateaction(-65510, true, YYAT_REDUCE, 5),
					new yystateaction(0, false, YYAT_DEFAULT, 40),
					new yystateaction(-65485, true, YYAT_REDUCE, 15),
					new yystateaction(0, false, YYAT_DEFAULT, 51),
					new yystateaction(-65488, true, YYAT_ERROR, 0),
					new yystateaction(-65495, true, YYAT_ERROR, 0),
					new yystateaction(-65493, true, YYAT_REDUCE, 7),
					new yystateaction(0, false, YYAT_REDUCE, 9),
					new yystateaction(0, false, YYAT_DEFAULT, 40),
					new yystateaction(0, false, YYAT_REDUCE, 41),
					new yystateaction(0, false, YYAT_REDUCE, 42),
					new yystateaction(-65538, true, YYAT_REDUCE, 13),
					new yystateaction(-65468, true, YYAT_DEFAULT, 38),
					new yystateaction(0, false, YYAT_REDUCE, 33),
					new yystateaction(0, false, YYAT_DEFAULT, 51),
					new yystateaction(0, false, YYAT_DEFAULT, 51),
					new yystateaction(0, false, YYAT_REDUCE, 49),
					new yystateaction(0, false, YYAT_REDUCE, 47),
					new yystateaction(0, false, YYAT_REDUCE, 48),
					new yystateaction(-65537, true, YYAT_REDUCE, 6),
					new yystateaction(0, false, YYAT_REDUCE, 17),
					new yystateaction(-65469, true, YYAT_DEFAULT, 48),
					new yystateaction(-65470, true, YYAT_DEFAULT, 49),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(-65474, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 11),
					new yystateaction(-65477, true, YYAT_DEFAULT, 94),
					new yystateaction(-65473, true, YYAT_ERROR, 0),
					new yystateaction(-65473, true, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 51),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(-65479, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 44),
					new yystateaction(-65499, true, YYAT_REDUCE, 16),
					new yystateaction(-65481, true, YYAT_ERROR, 0),
					new yystateaction(-65508, true, YYAT_DEFAULT, 94),
					new yystateaction(-65537, true, YYAT_DEFAULT, 98),
					new yystateaction(0, false, YYAT_DEFAULT, 51),
					new yystateaction(-65531, true, YYAT_DEFAULT, 73),
					new yystateaction(-65509, true, YYAT_REDUCE, 8),
					new yystateaction(0, false, YYAT_REDUCE, 10),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 73),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(0, false, YYAT_DEFAULT, 66),
					new yystateaction(-65533, true, YYAT_ERROR, 0),
					new yystateaction(-65537, true, YYAT_REDUCE, 37),
					new yystateaction(-65514, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 40),
					new yystateaction(0, false, YYAT_REDUCE, 35),
					new yystateaction(0, false, YYAT_REDUCE, 34),
					new yystateaction(-65533, true, YYAT_REDUCE, 14),
					new yystateaction(-65537, true, YYAT_ERROR, 0),
					new yystateaction(-65516, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 36),
					new yystateaction(0, false, YYAT_REDUCE, 43),
					new yystateaction(0, false, YYAT_REDUCE, 20),
					new yystateaction(0, false, YYAT_REDUCE, 46),
					new yystateaction(0, false, YYAT_REDUCE, 18),
					new yystateaction(0, false, YYAT_REDUCE, 19),
					new yystateaction(0, false, YYAT_REDUCE, 12),
					new yystateaction(0, false, YYAT_REDUCE, 21),
					new yystateaction(0, false, YYAT_REDUCE, 23),
					new yystateaction(0, false, YYAT_REDUCE, 27),
					new yystateaction(0, false, YYAT_REDUCE, 25),
					new yystateaction(0, false, YYAT_REDUCE, 31),
					new yystateaction(0, false, YYAT_REDUCE, 29),
					new yystateaction(-65491, true, YYAT_REDUCE, 22),
					new yystateaction(-65492, true, YYAT_REDUCE, 24),
					new yystateaction(-65493, true, YYAT_REDUCE, 28),
					new yystateaction(-65494, true, YYAT_REDUCE, 26),
					new yystateaction(-65495, true, YYAT_REDUCE, 32),
					new yystateaction(-65497, true, YYAT_REDUCE, 30),
					new yystateaction(-65510, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 38),
					new yystateaction(0, false, YYAT_REDUCE, 39),
					new yystateaction(0, false, YYAT_REDUCE, 50),
					new yystateaction(-65533, true, YYAT_ERROR, 0),
					new yystateaction(0, false, YYAT_REDUCE, 45) };
			yytables.yystateaction = stateaction;

			final yynontermgoto nontermgoto[] = { new yynontermgoto(0, 5),
					new yynontermgoto(0, 6), new yynontermgoto(0, 7),
					new yynontermgoto(0, 8), new yynontermgoto(51, 80),
					new yynontermgoto(27, 47), new yynontermgoto(41, 72),
					new yynontermgoto(41, 32), new yynontermgoto(51, 33),
					new yynontermgoto(27, 48), new yynontermgoto(51, 34),
					new yynontermgoto(27, 49), new yynontermgoto(31, 52),
					new yynontermgoto(12, 18), new yynontermgoto(13, 23),
					new yynontermgoto(13, 25), new yynontermgoto(31, 53),
					new yynontermgoto(12, 19), new yynontermgoto(40, 71),
					new yynontermgoto(20, 39), new yynontermgoto(40, 24),
					new yynontermgoto(20, 38), new yynontermgoto(94, 45),
					new yynontermgoto(73, 98), new yynontermgoto(66, 94),
					new yynontermgoto(65, 93), new yynontermgoto(64, 92),
					new yynontermgoto(63, 91), new yynontermgoto(62, 90),
					new yynontermgoto(61, 89), new yynontermgoto(60, 88),
					new yynontermgoto(59, 87), new yynontermgoto(58, 86),
					new yynontermgoto(57, 85), new yynontermgoto(56, 84),
					new yynontermgoto(55, 83), new yynontermgoto(54, 82),
					new yynontermgoto(52, 81), new yynontermgoto(50, 79),
					new yynontermgoto(43, 75), new yynontermgoto(42, 74),
					new yynontermgoto(35, 67), new yynontermgoto(26, 46),
					new yynontermgoto(18, 37), new yynontermgoto(15, 31) };
			yytables.yynontermgoto = nontermgoto;

			final yystategoto stategoto[] = { new yystategoto(-1, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(4, -1),
					new yystategoto(7, 40), new yystategoto(0, -1),
					new yystategoto(39, 41), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(30, -1),
					new yystategoto(0, -1), new yystategoto(11, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, 94),
					new yystategoto(0, -1), new yystategoto(37, 41),
					new yystategoto(-1, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(3, -1), new yystategoto(0, -1),
					new yystategoto(0, 94), new yystategoto(0, -1),
					new yystategoto(31, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, 94),
					new yystategoto(0, -1), new yystategoto(10, -1),
					new yystategoto(1, 51), new yystategoto(28, -1),
					new yystategoto(27, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, 94),
					new yystategoto(0, -1), new yystategoto(32, 51),
					new yystategoto(-2, -1), new yystategoto(24, -1),
					new yystategoto(0, -1), new yystategoto(24, -1),
					new yystategoto(23, -1), new yystategoto(22, -1),
					new yystategoto(21, -1), new yystategoto(20, -1),
					new yystategoto(19, -1), new yystategoto(20, -1),
					new yystategoto(19, -1), new yystategoto(18, -1),
					new yystategoto(17, -1), new yystategoto(16, -1),
					new yystategoto(15, -1), new yystategoto(14, -1),
					new yystategoto(0, 94), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(11, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, 94),
					new yystategoto(0, 94), new yystategoto(0, 94),
					new yystategoto(0, 94), new yystategoto(0, 94),
					new yystategoto(0, 94), new yystategoto(11, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1), new yystategoto(0, -1),
					new yystategoto(0, -1) };
			yytables.yystategoto = stategoto;

			yytables.yydestructorref = null;

			yytables.yytokendestref = null;

			yytables.yytokendestbaseref = null;
		}
	}

	// line 487 ".\\myparser.y"

	// ///////////////////////////////////////////////////////////////////////////
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
		if (parser.yycreate(null))
		{
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
