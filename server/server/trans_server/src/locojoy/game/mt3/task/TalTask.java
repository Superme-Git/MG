package locojoy.game.mt3.task;

import org.json.JSONObject;

import locojoy.game.mt3.utils.Conf;

/**
 * ��������
 * 
 * @author liangyanpeng
 *
 */
public class TalTask extends DiskStorage {
	
	protected JSONObject msg;
	
	public TalTask(JSONObject msg, String fileName) {
		super();
		super.fileName = fileName;
		super.filePath = Conf.getTalVoiceDir();
		this.msg = msg;
	}

	@Override
	public void asyncMethod() {
		String voice = msg.getString("speech");// ��Ƶ�ļ�
		storageDisk(voice, null);
	}

}
