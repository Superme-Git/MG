package com.pwrd.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.resources.ConstantIcons;

/**
 * XdbBrowser的控制台，用于显示在使用过程中所产生的消息。
 * 
 * 
 * @author wp853
 *
 */
public class ConsolePanel extends JPanel
{
	private static final long serialVersionUID = 3687061056337024709L;
	private static Log logger = LogFactory.getLog(ConsolePanel.class);
	private static final int DEFAULT_MAX_BUFFER_SIZE = 8000;
	
	private JTextPane console;
	// LineNumberPanel lnPanel;
	private StyledDocument consoleDoc;
	
	/** 
	 * 控制台能显示的最大字符个数，一定要保证控制台显示的字符数少于它，
	 * 其大小可以调节。 
	 */
	private int maxBufferSize;
	
	/** 控制台当前显示的字符数。 */
	private int curBufferSize;
	
	public ConsolePanel()
	{
		super(new BorderLayout());
		console = new JTextPane();
		/*{
			private static final long serialVersionUID = -5346234418612732566L;

			public void paint(Graphics g)
			{
				super.paint(g);
				lnPanel.repaint();
			}
		};*/
		console.setEditable(false);
		consoleDoc = console.getStyledDocument();
		JScrollPane scrollPane = new JScrollPane(console);
		// lnPanel = new LineNumberPanel(scrollPane, console);
		JToolBar toolbar = createToolBar();
		// add(lnPanel, BorderLayout.LINE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(toolbar, BorderLayout.LINE_END);
		
		maxBufferSize = DEFAULT_MAX_BUFFER_SIZE;
		curBufferSize = 0;
	}
	
	public void setMaxBufferSize(int _bufferSize)
	{
		maxBufferSize = _bufferSize;
	}
	
	public int getMaxBufferSize()
	{
		return maxBufferSize;
	}
	
	/**
	 * 在文档末尾位置追加一个默认风格的字符串。
	 * 
	 * @param str - 待追加的字符串
	 * @throws BadLocationException
	 */
	public void append(String str) throws BadLocationException
	{
		append(str, null);
	}
	
	/**
	 * 在文档末尾位置追加一个附加显示属性的字符串。
	 * 
	 * @param str - 待追加的字符串
	 * @param as - 该字符串的显示属性
	 * @throws BadLocationException
	 */
	public void append(String str, AttributeSet as) throws BadLocationException
	{
		assert str != null;
		if(curBufferSize + str.length() > maxBufferSize)
		{
			int deletedChs = removeFirst(curBufferSize + str.length() - maxBufferSize);
			curBufferSize -= deletedChs;
		}
		int offset = consoleDoc.getLength();
		consoleDoc.insertString(offset, str, as);
		curBufferSize += str.length();
	}
	
	/**
	 * 从文档的开头删除掉给定数目的字符。为了保持控制台输出的
	 * 美观，采用整行的删除办法，因此实际上删除掉的是包含给定
	 * 字符数的行。
	 * 
	 * @param chs - 需要删除的字符数。
	 * @return 实际上删除的字符数，这个数大于或者等于<code>chs</code>。
	 * @throws BadLocationException
	 */
	private int removeFirst(int chs) throws BadLocationException
	{
		// 如果chs个字符刚好是一完整的行。
		int offset = chs - 1;
		/*
		 * 循环结束后，offset要么指向文档的末尾位置，要么指向换行符
		 * 的下一个位置。其刚好等于要删除的字符数。
		 */
		while(offset < consoleDoc.getLength())
		{
			if("\n".equals(consoleDoc.getText(offset, 1)))
			{
				++ offset;
				break;
			}
			++ offset;
		}
		consoleDoc.remove(0, offset);
		// System.out.println(offset);
		
		return offset;
	}
	
	/**
	 * 清空控制台输出。
	 * 
	 * @throws BadLocationException
	 */
	public void clear() throws BadLocationException
	{
		removeFirst(curBufferSize);
		curBufferSize = 0;
	}
	
	public void addCaretListener(CaretListener listener)
	{
		console.addCaretListener(listener);
	}
	
	private JToolBar createToolBar()
	{
		JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
		JButton clearBtn = toolbar.add(new ClearAction("Clear", ConstantIcons.getIcon("clear")));
		clearBtn.setHideActionText(true);
		clearBtn.setToolTipText("Clear all messages");
		
		return toolbar;
	}
	
	private class ClearAction extends AbstractAction
	{
		private static final long serialVersionUID = -2756786192863908330L;

		@SuppressWarnings("unused")
		public ClearAction()
		{
		}
		
		@SuppressWarnings("unused")
		public ClearAction(String text)
		{
			super(text);
		}
		
		public ClearAction(String text, Icon icon)
		{
			super(text, icon);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			try 
			{
				clear();
			} catch (BadLocationException e1) 
			{
				e1.printStackTrace();
				logger.info(e1.getMessage());
			}
		}
		
	}
}
