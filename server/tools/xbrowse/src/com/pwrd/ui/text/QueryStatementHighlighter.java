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
 * ��ѯ�����е��ı��������Ϊ��λ������֯�ģ���ѯ���֮����';'����
 * �ָ���������ƶ���ĳһ�����ʱ����ͻ����ʾ����䡣
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
			// ����ң�backward��ֱ���ҵ��ֺŻ���ֱ���ĵ���ʼλ��
			try 
			{
				while(stmtStart >= 0 && getDocumentChar(queryDoc, stmtStart) != ';')
					-- stmtStart;
				/*
				 *  ѭ��������stmtStart����Ϊ-1����ʱ��ʾ���ĵ���ʼλ��Ҳû���ҵ�';',
				 *  ����stmtStart���ڵ�λ��Ϊ';'��
				 */
				++ stmtStart; // ��1ָ��һ��������ʼλ��
				
				while(stmtEnd < sentinel && getDocumentChar(queryDoc, stmtEnd) != ';')
					++ stmtEnd;
				/*
				 * ѭ��������stmtEnd����Ϊsentinel�����Ǳ�ʾ���ĵ��Ľ�βҲû���ҵ�';'��
				 * ����stmtStart���ڵ�λ��Ϊ';'�����stmtStart���ڵ�λ��Ϊ';'����ô';'
				 * ҲӦ�ð����ڸò�ѯ����С�
				 */
				if(stmtEnd < sentinel)
					++ stmtEnd;
				if(highlight != null) // �ж����Ƿ��뵱ǰ���غϣ�����غϣ���ʹ�����ڵļ���
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
