package snail.tools;

import java.lang.reflect.*;
import java.io.*;
import java.net.*;

/**
 * 动态加入 jar 文件到 classpath 中。
 * 
 * 不标准实现，在以后JDK版本中可能失效。
 */
public class Classpath {
	private static final Class<?>[] parameters = new Class[]{ URL.class };

	public static File getResourcePathFile(Class<?> cls) {
		String path = cls.getResource("Main.class").getPath();
		//System.out.println(path);
		if (false == path.startsWith("file:"))
			throw new RuntimeException("path is not a file." + path);
		int jarIndex = path.indexOf('!');
		if (jarIndex >= 0)
			path = path.substring(5, jarIndex);
		//System.out.println(path);
		return new File(path).getParentFile();
	}

	public static final void addFile(Class<?> pathHint, final String jarName) {
		addFile(new File(getResourcePathFile(pathHint), jarName));
	}

	public static final void addFile(final String file) {
		addFile( new File(file) );
	}

	public static final void addFile(final File file) {
		if (false == file.exists())
			throw new RuntimeException("file not exists. name=" + file);
		try {
			addURL(file.toURI().toURL());
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}
	 
	public static final void addURL(final URL u) {
		try {
			URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
			Method method = URLClassLoader.class.getDeclaredMethod("addURL",parameters);
			method.setAccessible(true);
			method.invoke(sysloader,new Object[]{ u });
		} catch (Throwable x) {
			throw new RuntimeException(x);
		}
	}
}
