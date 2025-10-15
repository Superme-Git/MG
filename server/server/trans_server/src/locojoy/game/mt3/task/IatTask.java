package locojoy.game.mt3.task;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import locojoy.game.mt3.sdk.ifly.Trans;
import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.Constans;

/**
 * 语音识别
 * 
 * @author liangyanpeng
 *
 */
public class IatTask extends DiskStorage {
	
	public static Logger LOG = Logger.getLogger("TRANS");

	private JSONObject msg;

	public IatTask(JSONObject msg, String fileName) {
		super();
		super.fileName = fileName;
		super.filePath = Conf.getIatVoiceDir();
		this.msg = msg;
	}

	@Override
	public void asyncMethod() {
		int sec = msg.getInt("sec");// 音频时长
		if (LOG.isDebugEnabled()) {
			LOG.debug("发送语音识别");
		}
		// 音频播放时长小于阀值才识别音频文字
		if (sec < Constans.IAT_MAX_VOICE_SEC) {
			String wordage = "吃葡萄不吐葡萄皮";
			long startTime = System.currentTimeMillis();
			if (!Conf.getIsDebug()) {
				wordage = Trans.speechToWordage(this.msg);
			}
			// 存储音频文件
			long endTime = System.currentTimeMillis();
			if (LOG.isInfoEnabled()) {
				long dealTime = endTime - startTime;
				StringBuilder logbuffer = new StringBuilder();
				logbuffer.append("[耗时:").append(dealTime).append("毫秒]语音识别内容=").append(wordage);
				LOG.info(logbuffer.toString());
			}
			storageDisk(wordage, Conf.getWordageExtension());
		}
	}

}
