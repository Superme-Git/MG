package locojoy.game.mt3.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

/**
 * �ļ�����
 * 
 * @author liangyanpeng
 *
 */
public class FileUtils {
	
	protected static final Logger LOG = Logger.getLogger(FileUtils.class);
	
	/**
	 * �ļ����ֳ�
	 */
	public static LinkedBlockingDeque<String> nameQueue = new LinkedBlockingDeque<>();

	/**
	 * ��ȡ�ļ�
	 * @param file �ļ���ַ
	 * @return �ļ�����
	 */
	public static byte[] loadFile(File file) {
		byte[] bytes = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			long length = file.length();
			bytes = new byte[(int) length];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				is.close();
				throw new IOException("Could not completely read file " + file.getName());
			}
		} catch (Exception e) {
			LOG.error("��ȡ�ļ�", e);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("��ȡ�ļ��ر���", e);
				}
			}
		}
		return bytes;
	}

	/**
	 * �����ļ�
	 * @param fileBytes  �ļ�����
	 * @param savePath   ����·��
	 */
	public static void writeFile(byte[] fileBytes, final String savePath) {
		BufferedOutputStream bos = null;
		if (fileBytes.length == 0)
			return;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(savePath));
			bos.write(fileBytes, 0, fileBytes.length);
			bos.flush();
		} catch (Exception e) {
			LOG.error("�����ļ�", e);
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					LOG.error("�����ļ��ر���", e);
				}
			}
		}
		
	}
	
	/**
	 * @return �ļ���
	 */
	public static String generateFileName() {
		String name = java.util.UUID.randomUUID().toString();
		return name.replaceAll("-", "");
	}
	
	/**
	 * @return classpath·��
	 */
	public static String getClassPath() {
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
		if (null != classpath)
			return classpath.getPath();
		return null;
	}
	
	/**
	 * @param aue ��������
	 * @return �ļ���չ��
	 */
	public static String getExtension(final String aue) {
		if (aue.equals(AueEnum.PCM.value())) {
			return AueEnum.PCM.value();
		} else if (aue.equals(AueEnum.WAV.value())) {
			return AueEnum.WAV.value();
		} else {
			return AueEnum.SPEEX.value();
		}
	}
	
}
