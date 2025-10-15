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
 * XdbBrowser�Ŀ���̨��������ʾ��ʹ�ù���������������Ϣ��
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
	 * ����̨����ʾ������ַ�������һ��Ҫ��֤����̨��ʾ���ַ�����������
	 * ���С���Ե��ڡ� 
	 */
	private int maxBufferSize;
	
	/** ����̨��ǰ��ʾ���ַ����� */
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
	 * ���ĵ�ĩβλ��׷��һ��Ĭ�Ϸ����ַ�����
	 * 
	 * @param str - ��׷�ӵ��ַ���
	 * @throws BadLocationException
	 */
	public void append(String str) throws BadLocationException
	{
		append(str, null);
	}
	
	/**
	 * ���ĵ�ĩβλ��׷��һ��������ʾ���Ե��ַ�����
	 * 
	 * @param str - ��׷�ӵ��ַ���
	 * @param as - ���ַ�������ʾ����
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
	 * ���ĵ��Ŀ�ͷɾ����������Ŀ���ַ���Ϊ�˱��ֿ���̨�����
	 * ���ۣ��������е�ɾ���취�����ʵ����ɾ�������ǰ�������
	 * �ַ������С�
	 * 
	 * @param chs - ��Ҫɾ�����ַ�����
	 * @return ʵ����ɾ�����ַ�������������ڻ��ߵ���<code>chs</code>��
	 * @throws BadLocationException
	 */
	private int removeFirst(int chs) throws BadLocationException
	{
		// ���chs���ַ��պ���һ�������С�
		int offset = chs - 1;
		/*
		 * ѭ��������offsetҪôָ���ĵ���ĩβλ�ã�Ҫôָ���з�
		 * ����һ��λ�á���պõ���Ҫɾ�����ַ�����
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
	 * ��տ���̨�����
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
