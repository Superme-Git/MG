package com.pwrd.log;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import com.pwrd.ui.ConsolePanel;
import com.pwrd.ui.XdbBrowserFrame;

/**
 * XdbBrowserʹ��Log4j����¼��־��Log4j�ĺô�֮һ���ǿ���
 * �Լ�������Ϣ�Ĵ洢λ�ã�Ϊ���ܽ���Щ��־��Ϣ��ʾ��ϵͳ��
 * ����̨�ϣ�ʹ�ô�����ΪLog4j�Ϳ���̨֮���������������ֻ
 * �Ƕ�<code>ConsolePanel</code>������һ�μ򵥵İ�װ��<b>
 * ע�⣺</b>������Log4j���г�ʼ��ʱ����û��<code>ConsolePanel
 * </code>ʵ������˽����ڴ����У�ȷ���Ѿ�����<code>ConsolePanel
 * </code>ʵ���������Ϊ<code>Logger</code>���ø�<code>Appender</code>��
 * 
 * @author wp853
 *
 */
public class ConsolePanelAppender extends AppenderSkeleton
{	
	private ConsolePanel consolePanel;
	
	public ConsolePanelAppender()
	{
		consolePanel = XdbBrowserFrame.getConsolePanel();
	}
	
	public ConsolePanelAppender(Layout layout)
	{
		this();
		setLayout(layout);
	}
	
	@Override
	public void close() 
	{
		if(closed)
			return;
		closed = true;
	}

	@Override
	public boolean requiresLayout() 
	{
		return true;
	}

	@Override
	protected void append(LoggingEvent event) 
	{
		AttributeSet as = XLogger.getLevelAS(event.getLevel());
		try 
		{
			consolePanel.append(this.layout.format(event), as);
			if (layout.ignoresThrowable()) 
			{
				String[] s = event.getThrowableStrRep();
				if (s != null) 
				{
					int len = s.length;
					for (int i = 0; i < len; i++) {
						consolePanel.append(s[i], as);
						consolePanel.append(Layout.LINE_SEP, as);
					}
				}
			}
		} catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
	}
}
