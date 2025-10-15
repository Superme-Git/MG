package robot.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天道具管理器
 * 
 * @author yt
 */
public class ChatIteamMgr {

	public boolean isFinish = false;

	public static ChatIteamMgr _instance = new ChatIteamMgr();

	public static ChatIteamMgr getInstance() {
		return _instance;
	}

	Map<String, Map<String, String>> messageMap = new HashMap<String, Map<String, String>>();

	/**
	 * 聊天框中道具链接信息串处理
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月12日 上午2:53:52
	 * @version 1.0
	 * @param message
	 * @return
	 */
	public Map<String, String> processMessage(String message) {
		// if (messageMap.containsKey(message)) {
		// return messageMap.get(message);
		// }
		Map<String, String> dataMap = new HashMap<String, String>();
		String[] keyValueArr = message.split(" ");
		for (int i = 0; i < keyValueArr.length; i++) {
			String[] keyValue = keyValueArr[i].split("=\"");
			if (keyValue.length == 2) {
				String key = keyValue[0];
				String value = keyValue[1].replace("\"", "");
				//针对拍卖
				if (key.equals("arg")) {
					String[] valueArr = value.split(",");
					for (int j = 0; j < valueArr.length; j++) {
						String[] keyValue1 = valueArr[j].split("=");
						dataMap.put(keyValue1[0], keyValue1[1]);
					}
				} else {
					//针对背包
					dataMap.put(keyValue[0], value);
				}
			}
		}
		// messageMap.put(message, dataMap);
		return dataMap;
	}
	// /**
	// * 消息处理
	// *
	// * @author yangtao
	// * @dateTime 2016年6月12日 上午2:53:52
	// * @version 1.0
	// * @param message
	// * @return
	// */
	// public synchronized void processMessage1(Long roleid, String message) {
	// if (messageMap.containsKey(message)) {
	// return;
	// }
	// Map<String, String> dataMap = new HashMap<String, String>();
	// String[] keyValueArr = message.split(" ");
	// for (int i = 0; i < keyValueArr.length; i++) {
	// String[] keyValue = keyValueArr[i].split("=");
	// if (keyValue.length == 2) {
	// String value = keyValue[1].replace("\"", "");
	// dataMap.put(keyValue[0], value);
	// }
	// }
	// CChatItemTips cChatItemTips = new CChatItemTips();
	// fire.pb.talk.DisplayInfo displayinfo = new fire.pb.talk.DisplayInfo();
	// displayinfo.counterid = Integer.parseInt(dataMap.get("counter"));
	// displayinfo.displaytype = Integer.parseInt(dataMap.get("type"));
	// displayinfo.shopid = Long.parseLong(dataMap.get("shopid"));
	// displayinfo.uniqid = Integer.parseInt(dataMap.get("key"));
	// displayinfo.roleid = roleid;
	// displayinfo.teamid = 0l;
	// cChatItemTips.displayinfo = displayinfo;
	// // messageMap.put(message, dataMap);
	// return dataMap;
	// }
}
