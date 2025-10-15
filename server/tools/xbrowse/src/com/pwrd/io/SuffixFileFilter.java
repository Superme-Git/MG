package com.pwrd.io;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

public class SuffixFileFilter implements FileFilter {
	private Set<String> acceptSuffix = new HashSet<String>();

	public SuffixFileFilter(String... args) {
		for (String arg : args) {
			// logger.info(arg);
			if (arg != null)
				acceptSuffix.add(arg.toLowerCase());
		}
	}

	@Override
	public boolean accept(File pathname) {
		assert pathname != null;
		String fileName = pathname.getName();
		if (fileName.length() > 0) {
			int lastDotPos = fileName.lastIndexOf(".");
			/**
			 * 其实文件名不能以"."开头，当然"."也不太可能在末尾， 不过即使在末尾，也能通过后缀名将这些非法文件过滤掉。
			 */
			if (lastDotPos >= 0 && lastDotPos < (fileName.length() - 1)) {
				String suffix = fileName.substring(lastDotPos + 1);
				if (acceptSuffix.contains(suffix.toLowerCase()))
					return true;
			}
		}

		return false;
	}
}
