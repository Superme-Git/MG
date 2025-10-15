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
			 * ��ʵ�ļ���������"."��ͷ����Ȼ"."Ҳ��̫������ĩβ�� ������ʹ��ĩβ��Ҳ��ͨ����׺������Щ�Ƿ��ļ����˵���
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
