package com.pwrd.ui.text;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.StyledDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 查询界面中的文本是以语句为单位进行组织的，查询语句之间以';'进行
 * 分隔，当光标移动到某一语句中时，则突出显示该语句。
 * 
 * @author wp853
 *
 */
public class QueryStatementHighlighter implements CaretListener
{
	static Log logger = LogFactory.getLog(QueryStatementHighlighter.class);

	@Override
	public void caretUpdate(CaretEvent e) 
	{
		int pos = e.getDot();
		int mark = e.getMark();
		
		if(pos == mark)
		{
			JTextPane queryPane = (JTextPane)e.getSource();
			StyledDocument queryDoc = queryPane.getStyledDocument();
			Highlighter queryLighter = queryPane.getHighlighter();
			Highlighter.Highlight highlight = null;
			if(queryLighter.getHighlights().length == 1)
				highlight = queryLighter.getHighlights()[0];
			// queryLighter.removeAllHighlights();
			
			int sentinel = queryDoc.getLength();
			int stmtStart = pos - 1;
			int stmtEnd = pos;
			// 向后找（backward）直到找到分号或者直到文档起始位置
			try 
			{
				while(stmtStart >= 0 && getDocumentChar(queryDoc, stmtStart) != ';')
					-- stmtStart;
				/*
				 *  循环结束后，stmtStart可能为-1，这时表示到文档起始位置也没有找到';',
				 *  或者stmtStart所在的位置为';'。
				 */
				++ stmtStart; // 加1指向一个语句的起始位置
				
				while(stmtEnd < sentinel && getDocumentChar(queryDoc, stmtEnd) != ';')
					++ stmtEnd;
				/*
				 * 循环结束后，stmtEnd可能为sentinel，这是表示到文档的结尾也没有找到';'，
				 * 或者stmtStart所在的位置为';'。如果stmtStart所在的位置为';'，那么';'
				 * 也应该包含在该查询语句中。
				 */
				if(stmtEnd < sentinel)
					++ stmtEnd;
				if(highlight != null) // 判断下是否与当前的重合，如果重合，则使用现在的即可
				{
					if(highlight.getStartOffset() == stmtStart && highlight.getEndOffset() == stmtEnd)
					{	
						// logger.debug("Duplicate stme!");
						return;
					}
				}
				// logger.debug("not Duplicate stme!");
				queryLighter.removeAllHighlights();
				queryLighter.addHighlight(stmtStart, stmtEnd, new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN));
			} 
			catch (BadLocationException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
	
	private char getDocumentChar(StyledDocument doc, int offset) throws BadLocationException
	{
		return doc.getText(offset, 1).charAt(0);
	}
}
