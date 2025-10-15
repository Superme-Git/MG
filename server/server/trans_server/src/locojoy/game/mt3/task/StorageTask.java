package locojoy.game.mt3.task;

import java.nio.charset.Charset;
import java.util.Base64;

import org.json.JSONObject;

/**
 * 磁盘写文件异步自行线程
 * 
 * @author liangyanpeng
 *
 */
public class StorageTask extends DiskStorage {
	
	protected JSONObject msg;
	
	public StorageTask(JSONObject msg, String filePath,String fileName) {
		super();
		super.fileName = fileName;
		super.filePath = filePath;
		this.msg = msg;
	}

	@Override
	public void asyncMethod() {
		String voice = msg.getString("speech"); // 音频文件
		if (voice == null || voice.isEmpty()) {
			return;
		}
		byte[] decVoice = voice.getBytes(); //Base64.getDecoder().decode(voice.getBytes(Charset.forName("utf-8")));
		storageDisk(decVoice, null);
	}

}
