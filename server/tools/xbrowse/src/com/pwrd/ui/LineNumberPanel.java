package com.pwrd.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * �к����ؼ�������������ɣ����߽���Ҳ��к���������ʾЧ����eclipse�ı༭����
 * ���߽���������Ĭ����ɫ���к����򱳾�Ϊ��ɫ����ʾ���������ϵͳĬ�����壬����
 * ȷ���к���ʾ��������м䡣
 * 
 * @author wp853
 *
 */
public class LineNumberPanel extends JPanel
{
	private static final long serialVersionUID = 3496614667854219469L;

	private static final int DEFAULT_WIDTH = 40;
	private static final int DEFAULT_HEIGHT = 40;
	
	private Color _defaultColor;
	private JScrollPane _scrollPane;
	private JTextComponent _textCompo;
	private Document _doc;
	
	/**
	 * 
	 * @param scrollPane - �ı��������ڵĹ�����塣
	 * @param textCompo - ��Ҫ����кŵ��ı������
	 */
	public LineNumberPanel(JScrollPane scrollPane, JTextComponent textCompo)
	{
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		/*
		 * �к�������໹��һ����������ʾ�߽磬ʹ�������Ĭ�ϱ�����ɫ��
		 * ���ڽ��������޸ı�����ɫ����˽��䱣��������
		 */
		_defaultColor = getBackground(); 
		setBackground(Color.WHITE); // ��ʾ�кŵ�����Ϊ��ɫ
		
		_scrollPane = scrollPane;
		_textCompo = textCompo;
		_doc = textCompo.getDocument();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		/*
		 * ��ʾ�к��������ı߽磬�ñ߽�Ŀ��Ϊ�����к�����1/4��
		 * ����ɫΪĬ�Ͽؼ���ɫ��
		 */
		Rectangle rect = g.getClipBounds();
		int borderWidth = rect.width / 4;
		g.setColor(_defaultColor);
		g.fillRect(rect.x, rect.y, borderWidth, rect.height);
		
		Point startPos = _scrollPane.getViewport().getViewPosition();
		int startOffset = _textCompo.viewToModel(startPos);
		int endOffset = _textCompo.viewToModel(new Point(startPos.x + _textCompo.getWidth(), startPos.y + _textCompo.getHeight()));
		int startLine = _doc.getDefaultRootElement().getElementIndex(startOffset) + 1;
		int endLine = _doc.getDefaultRootElement().getElementIndex(endOffset) + 1;
		
		FontMetrics fm = g.getFontMetrics(_textCompo.getFont());
		int fontHeight = fm.getHeight();
		int fontDescent = fm.getDescent();
		int startY = -1;
		try 
		{
			startY = _textCompo.modelToView(startOffset).y - startPos.y + fontHeight - fontDescent;
		} 
		catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
		g.setColor(_textCompo.getForeground());
		/*
		 * ȷ���к���ʾ���к���������м䡣 
		 */
		int x = (getWidth() + borderWidth) / 2 + fm.stringWidth(Integer.toString(endLine))/2;
		for(int line = startLine, y = startY; line <= endLine; y += fontHeight, ++ line)
		{
			g.drawString(Integer.toString(line), x - fm.stringWidth(Integer.toString(line)), y);
		}
	}
}
