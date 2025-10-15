package locojoy.game.mt3.task;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import locojoy.game.mt3.async.AsyncTask;
import locojoy.game.mt3.utils.FileUtils;

/**
 * 磁盘存储
 * 
 * @author liangyanpeng
 *
 */
public abstract class DiskStorage extends AsyncTask {
	
	public static final Logger TRANS_LOG = Logger.getLogger("TRANS");
	
	public static final Logger BATTLE_LOG = Logger.getLogger("BATTLE");

	/**
	 * 存储路径
	 */
	protected String filePath = null;
	/**
	 * 存储文件名
	 */
	protected String fileName = null;

	/**
	 * 保存文件
	 * @param content 音频或文本
	 * @param extension 扩展名(如果没有扩展名设置null)
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
			TRANS_LOG.debug("保存路径：" + savePath);
		}
		FileUtils.writeFile(content.getBytes(Charset.forName("utf-8")), savePath.toString());
		long endTime = System.currentTimeMillis();
		if (TRANS_LOG.isInfoEnabled()) {
			long dealTime = endTime - startTime;
			StringBuilder logbuffer = new StringBuilder();
			logbuffer.append("[耗时:").append(dealTime).append("毫秒]写磁盘文件").append(fileName);
			if (extension != null) {
				logbuffer.append(extension);
			}
			TRANS_LOG.info(logbuffer.toString());
		}
	}
	/**
	 * 保存文件
	 * @param content 战斗录像字节数组
	 * @param extension 扩展名(如果没有扩展名设置null)
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
			BATTLE_LOG.debug("保存路径：" + savePath);
		}
		FileUtils.writeFile(content, savePath.toString());
		long endTime = System.currentTimeMillis();
		if (BATTLE_LOG.isInfoEnabled()) {
			long dealTime = endTime - startTime;
			StringBuilder logbuffer = new StringBuilder();
			logbuffer.append("[耗时:").append(dealTime).append("毫秒]写磁盘文件").append(fileName);
			if (extension != null) {
				logbuffer.append(extension);
			}
			BATTLE_LOG.info(logbuffer.toString());
		}
	}
}
