package com.pwrd.log;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Level;

/**
 * 系统日志服务的入口
 * 
 * @author wp853
 *
 */
public class XLogger 
{
	private static Map<Level, SimpleAttributeSet> levelAS;
	
	static
	{
		levelAS = new HashMap<Level, SimpleAttributeSet>();
		levelAS.put(Level.DEBUG, null);
		SimpleAttributeSet magentaAS = new SimpleAttributeSet();
		magentaAS.addAttribute(StyleConstants.Foreground, Color.MAGENTA);
		levelAS.put(Level.INFO, magentaAS);
		SimpleAttributeSet redAS = new SimpleAttributeSet();
		redAS.addAttribute(StyleConstants.Foreground, Color.RED);
		levelAS.put(Level.WARN, redAS);
	}
	
	public static void startup()
	{
		Layout log4jLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %C %L %p: %m\n");
		Appender consolePanelAppender = new ConsolePanelAppender(log4jLayout);
		Logger.getRootLogger().addAppender(consolePanelAppender);
	}
	
	public static AttributeSet getLevelAS(Level level)
	{
		if(Level.DEBUG.isGreaterOrEqual(level))
			return levelAS.get(Level.DEBUG);
		else if(Level.INFO.equals(level))
			return levelAS.get(Level.INFO);
		else if(level.isGreaterOrEqual(Level.WARN))
			return levelAS.get(Level.WARN);
		
		return null;
	}
}
