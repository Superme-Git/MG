package com.pwrd.ui;

import javax.swing.LookAndFeel;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * A look and feel for an application.
 * 
 * @author Heidi Rakels.
 */
public class LAF
{
	
	public static final String THEME_GOLD 	= "gold";
	public static final String THEME_OCEAN 	= "blue";
	public static final String THEME_DEAULT = "default";

	// Fields.

	private String 			className;
	private String 			title;
	private boolean 		supported	= false;
	private boolean 		selected	= false;
	private LookAndFeel 	laf;
	private String 			theme;
	
	// Constructors.

	public LAF(String title, String className, String themeString)
	{
		this.title = title;
		this.className = className;
		this.theme = themeString;
		
		// Is this look and feel supported?
		try 
		{
            @SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className);
            laf = (LookAndFeel) clazz.newInstance();
            supported = laf.isSupportedLookAndFeel();
        } 
		catch (Exception e) 
		{
			// e.printStackTrace();
		}

	}

	// Getters / Setters.

	public String getClassName()
	{
		return className;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public boolean isSupported()
	{
		return supported;
	}

	public String getTitle()
	{
		return title;
	}

	public LookAndFeel getLaf()
	{
		return laf;
	}
	
	public String getTheme()
	{
		return theme;
	}

	public static void setTheme(LookAndFeel laf, String themeString)
	{
        if (laf instanceof MetalLookAndFeel)
        {
        	MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }
	}
}
