package robot.zhuogui;

import java.util.HashMap;
import java.util.Map;

import robot.ConfigMgr;
import fire.pb.instance.SBingFengCfg;
import fire.pb.instancezone.bingfeng.BingFengWangZuoConfig;

public class ZhuoGuiUtils {
	public static final Map<Integer, NpcData> npcId2NpcDataMap = new HashMap<Integer, NpcData>();

	public static ZhuoGuiUtils _instance = new ZhuoGuiUtils();

	public static ZhuoGuiUtils getInstance() {
		return _instance;
	}

	public void init() {
		npcId2NpcDataMap.put(180002, new NpcData(68,100172));
		npcId2NpcDataMap.put(180003, new NpcData(61,100173));
		npcId2NpcDataMap.put(180004, new NpcData(74,100174));
		npcId2NpcDataMap.put(180005, new NpcData(4,100175));
		npcId2NpcDataMap.put(180006, new NpcData(31,100176));
		npcId2NpcDataMap.put(180007, new NpcData(17,100177));
		npcId2NpcDataMap.put(180008, new NpcData(47,100178));
		npcId2NpcDataMap.put(180009, new NpcData(83,100179));
		npcId2NpcDataMap.put(180010, new NpcData(111,100180));
	}

	/**
	 * 得到配置数据
	 * 
	 * @param instzoneid
	 * @param state
	 * @return
	 */
	public NpcData getData(int npcid) {
		NpcData npcData = npcId2NpcDataMap.get(npcid);

		return npcData;

	}

}
