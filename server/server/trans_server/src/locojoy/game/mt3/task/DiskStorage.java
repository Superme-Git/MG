package locojoy.game.mt3.task;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import locojoy.game.mt3.async.AsyncTask;
import locojoy.game.mt3.utils.FileUtils;

/**
 * ���̴洢
 * 
 * @author liangyanpeng
 *
 */
public abstract class DiskStorage extends AsyncTask {
	
	public static final Logger TRANS_LOG = Logger.getLogger("TRANS");
	
	public static final Logger BATTLE_LOG = Logger.getLogger("BATTLE");

	/**
	 * �洢·��
	 */
	protected String filePath = null;
	/**
	 * �洢�ļ���
	 */
	protected String fileName = null;

	/**
	 * �����ļ�
	 * @param content ��Ƶ���ı�
	 * @param extension ��չ��(���û����չ������null)
	 */
	public void storageDisk(String content, String extension) {
		long startTime = System.currentTimeMillis();
		StringBuilder savePath = new StringBuilder();
		savePath.append(filePath);
		savePath.append(fileName);
		if (extension != null) {
			savePath.append(extension);
		}
		if (TRANS_LOG.isDebugEnabled()) {
			TRANS_LOG.debug("����·����" + savePath);
		}
		FileUtils.writeFile(content.getBytes(Charset.forName("utf-8")), savePath.toString());
		long endTime = System.currentTimeMillis();
		if (TRANS_LOG.isInfoEnabled()) {
			long dealTime = endTime - startTime;
			StringBuilder logbuffer = new StringBuilder();
			logbuffer.append("[��ʱ:").append(dealTime).append("����]д�����ļ�").append(fileName);
			if (extension != null) {
				logbuffer.append(extension);
			}
			TRANS_LOG.info(logbuffer.toString());
		}
	}
	/**
	 * �����ļ�
	 * @param content ս��¼���ֽ�����
	 * @param extension ��չ��(���û����չ������null)
	 */
	public void storageDisk(byte content[], String extension) {
		long startTime = System.currentTimeMillis();
		StringBuilder savePath = new StringBuilder();
		savePath.append(filePath);
		savePath.append(fileName);
		if (extension != null) {
			savePath.append(extension);
		}
		if (BATTLE_LOG.isDebugEnabled()) {
			BATTLE_LOG.debug("����·����" + savePath);
		}
		FileUtils.writeFile(content, savePath.toString());
		long endTime = System.currentTimeMillis();
		if (BATTLE_LOG.isInfoEnabled()) {
			long dealTime = endTime - startTime;
			StringBuilder logbuffer = new StringBuilder();
			logbuffer.append("[��ʱ:").append(dealTime).append("����]д�����ļ�").append(fileName);
			if (extension != null) {
				logbuffer.append(extension);
			}
			BATTLE_LOG.info(logbuffer.toString());
		}
	}
}
