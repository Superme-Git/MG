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
 * ����ɫͼ�����ɫ���Ʒ���һ����ʾ���ܹ�ʹ�û�������ɫ�ľ���Ч����
 * ����ɫ<code>JComboBox</code>�ڻ���ʱ���ͻ����<code>ColorComboBoxRenderer</code>
 * ��ʾÿһ�ÿһ���Ϊһ��<code>JLabel</code>ʵ�������ı�Ϊ��ɫ���ƣ�ͼ��Ϊ
 * һ��ʹ�ø���ɫ����С�����Ρ�
 * 
 * @author wp853
 *
 */
public class ColorComboBoxRenderer extends JLabel implements ListCellRenderer
{
	private static final long serialVersionUID = -755744068070799722L;

	/** �򵥵ļ�����ɫ */
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
	 * ������ʾĳһ����ɫ��Сͼ�꣬��ͼ��ΪϵͳĬ�ϴ�С��
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