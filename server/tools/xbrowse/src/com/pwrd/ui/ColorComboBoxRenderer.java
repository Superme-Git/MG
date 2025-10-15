package com.pwrd.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.pwrd.resources.ConstantIcons;

/**
 * 将颜色图标和颜色名称放在一起显示，能够使用户看到颜色的具体效果，
 * 当颜色<code>JComboBox</code>在绘制时，就会调用<code>ColorComboBoxRenderer</code>
 * 显示每一项。每一项均为一个<code>JLabel</code>实例，其文本为颜色名称，图标为
 * 一个使用该颜色填充的小正方形。
 * 
 * @author wp853
 *
 */
public class ColorComboBoxRenderer extends JLabel implements ListCellRenderer
{
	private static final long serialVersionUID = -755744068070799722L;

	/** 简单的几个颜色 */
	private static Map<String, Color> commonColors;
	
	static
	{
		commonColors = new HashMap<String, Color>();
		commonColors.put("Black", Color.BLACK);
		commonColors.put("Blue", Color.BLUE);
		commonColors.put("Cyan", Color.CYAN);
		commonColors.put("DarkGray", Color.DARK_GRAY);
		commonColors.put("Gray", Color.GRAY);
		commonColors.put("Green", Color.GREEN);
		commonColors.put("LightGray", Color.LIGHT_GRAY);
		commonColors.put("Magenta", Color.MAGENTA);
		commonColors.put("Orange", Color.ORANGE);
		commonColors.put("Pink", Color.PINK);
		commonColors.put("Red", Color.RED);
		commonColors.put("White", Color.WHITE);
		commonColors.put("Yellow", Color.YELLOW);
	}
	
	public ColorComboBoxRenderer()
	{
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) 
	{
		setText(value.toString());
		setIcon(new ColoredSquare(commonColors.get(value.toString())));
		
		if(isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		return this;
	}
	
	public static Map<String, Color> getCommonColors()
	{
		return commonColors;
	}
	
	/**
	 * 用于显示某一种颜色的小图标，该图标为系统默认大小。
	 * 
	 * @author wp853
	 *
	 */
	public static class ColoredSquare implements Icon 
	{
		private Color color;
		
		public ColoredSquare(Color c)
		{
		    this.color = c;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) 
		{
		    Color oldColor = g.getColor();
		    g.setColor(color);
		    g.fill3DRect(x,y,getIconWidth(), getIconHeight(), true);
		    g.setColor(oldColor);
		}
		
		public int getIconWidth() 
		{ 
			return ConstantIcons.DEFAULT_ICON_WIDTH; 
		}
		
		public int getIconHeight() 
		{ 
			return ConstantIcons.DEFAULT_ICON_HEIGHT; 
		}
	}
}