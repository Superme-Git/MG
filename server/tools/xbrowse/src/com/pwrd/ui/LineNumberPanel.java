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
 * 行号面板控件。由两部分组成：左侧边界和右侧行号区域，其显示效果类eclipse的编辑器，
 * 左侧边界采用组件的默认颜色，行号区域背景为白色，显示数字则采用系统默认字体，并且
 * 确保行号显示在区域的中间。
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
	 * @param scrollPane - 文本区域所在的滚动面板。
	 * @param textCompo - 需要添加行号的文本组件。
	 */
	public LineNumberPanel(JScrollPane scrollPane, JTextComponent textCompo)
	{
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		/*
		 * 行号区域左侧还有一部分用于显示边界，使用组件的默认背景颜色，
		 * 由于接下来会修改背景颜色，因此将其保存下来。
		 */
		_defaultColor = getBackground(); 
		setBackground(Color.WHITE); // 显示行号的区域为白色
		
		_scrollPane = scrollPane;
		_textCompo = textCompo;
		_doc = textCompo.getDocument();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		/*
		 * 显示行号区域左侧的边界，该边界的宽度为整个行号面板的1/4，
		 * 其颜色为默认控件颜色。
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
		 * 确保行号显示在行号区域的正中间。 
		 */
		int x = (getWidth() + borderWidth) / 2 + fm.stringWidth(Integer.toString(endLine))/2;
		for(int line = startLine, y = startY; line <= endLine; y += fontHeight, ++ line)
		{
			g.drawString(Integer.toString(line), x - fm.stringWidth(Integer.toString(line)), y);
		}
	}
}
