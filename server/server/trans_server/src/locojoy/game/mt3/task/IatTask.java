package locojoy.game.mt3.task;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import locojoy.game.mt3.sdk.ifly.Trans;
import locojoy.game.mt3.utils.Conf;
import locojoy.game.mt3.utils.Constans;

/**
 * ����ʶ��
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
		int sec = msg.getInt("sec");// ��Ƶʱ��
		if (LOG.isDebugEnabled()) {
			LOG.debug("��������ʶ��");
		}
		// ��Ƶ����ʱ��С�ڷ�ֵ��ʶ����Ƶ����
		if (sec < Constans.IAT_MAX_VOICE_SEC) {
			String wordage = "�����Ѳ�������Ƥ";
			long startTime = System.currentTimeMillis();
			if (!Conf.getIsDebug()) {
				wordage = Trans.speechToWordage(this.msg);
			}
			// �洢��Ƶ�ļ�
			long endTime = System.currentTimeMillis();
			if (LOG.isInfoEnabled()) {
				long dealTime = endTime - startTime;
				StringBuilder logbuffer = new StringBuilder();
				logbuffer.append("[��ʱ:").append(dealTime).append("����]����ʶ������=").append(wordage);
				LOG.info(logbuffer.toString());
			}
			storageDisk(wordage, Conf.getWordageExtension());
		}
	}

}
