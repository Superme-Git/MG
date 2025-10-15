package com.pwrd.resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pwrd.io.SuffixFileFilter;

public class ConstantIcons 
{
	public static final int  DEFAULT_ICON_WIDTH = 16;
	public static final int DEFAULT_ICON_HEIGHT = 16;
	
	private static Log logger = LogFactory.getLog(ConstantIcons.class);
	
	private static Map<String, Icon> icons = new HashMap<String, Icon>();
	
	static
	{
		try 
		{
			File iconsDir = new File("resources/icons");
			if(iconsDir.isDirectory())
			{
				File[] iconFiles = iconsDir.listFiles(new SuffixFileFilter("png", "gif"));
				for(File iconFile : iconFiles)
				{
					String iconName = iconFile.getName();
					int lastDotPos = iconName.lastIndexOf(".");
					iconName = iconName.substring(0, lastDotPos);
					Image image = ImageIO.read(iconFile);
					Icon icon = new ImageIcon(image.getScaledInstance(DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT, Image.SCALE_SMOOTH));
					icons.put(iconName, icon);
					// logger.debug(iconName);
				}
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			logger.warn(e.getMessage());
		} 
	}
	
	/**
	 * 
	 * @param iconName - 图标的名字
	 * @return 返回名字对应的图标
	 */
	public static Icon getIcon(String iconName)
	{
		return icons.get(iconName);
	}
}
