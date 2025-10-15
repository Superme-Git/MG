package com.pwrd;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaLibraryLoader 
{
	private static Method addURL;
	
	static
    {
    	addURL = null;
		try {
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		} 
		catch (SecurityException e1)
		{
			e1.printStackTrace();
		}
		catch (NoSuchMethodException e1)
		{
			e1.printStackTrace();
		}
		addURL.setAccessible(true);
    }
	
	public static boolean load(String libPath)
	{
		File jarFile = new File(libPath);
		return load(jarFile);
	}
	
	public static boolean load(File jarFile)
	{
		URLClassLoader cloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		try 
		{
			addURL.invoke(cloader, jarFile.toURI().toURL());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
