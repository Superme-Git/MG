package com.pwrd.log;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import com.pwrd.ui.ConsolePanel;
import com.pwrd.ui.XdbBrowserFrame;

/**
 * XdbBrowser使用Log4j来记录日志，Log4j的好处之一就是可以
 * 自己设置消息的存储位置，为了能将这些日志消息显示在系统的
 * 控制台上，使用此类作为Log4j和控制台之间的适配器。该类只
 * 是对<code>ConsolePanel</code>进行了一次简单的包装。<b>
 * 注意：</b>由于在Log4j进行初始化时，还没有<code>ConsolePanel
 * </code>实例，因此建议在代码中，确定已经生成<code>ConsolePanel
 * </code>实例的情况下为<code>Logger</code>设置该<code>Appender</code>。
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
