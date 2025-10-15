package robot.manager;

import java.util.NavigableMap;
import java.util.TreeMap;

import robot.ConfigMgr;
import fire.pb.npc.SNpcServiceConfig;
import fire.pb.npc.SNpcShare;

public class NPCMgr {
	private static NPCMgr instance = new NPCMgr();

	public static NPCMgr getInstance() {
		return instance;
	};

	private NavigableMap<Integer, SNpcShare> mapNPCInfo = new TreeMap<Integer, SNpcShare>();
	private java.util.NavigableMap<Integer, SNpcServiceConfig> npcServiceMap = new java.util.TreeMap<Integer, SNpcServiceConfig>();

	private NPCMgr() {
	}

	/**
	 * 通过ID获取NPC信息
	 * 
	 * @param npcId
	 * @return
	 */
	public SNpcShare getNpcInfoByID(int npcId) {
		return mapNPCInfo.get(npcId);
	}

	/**
	 * 通过npcID获取NPC名字
	 * 
	 * @param npcId
	 * @return
	 */
	public String getNPCNameByID(int npcId) {
		SNpcShare result = mapNPCInfo.get(npcId);
		if (result == null)
			return "";
		return result.getName();
	}

	/**
	 * 初始化NPC信息
	 */
	public void init() {
		ConfigMgr cm = ConfigMgr.getInstance();
		mapNPCInfo = cm.getConfig(SNpcShare.class);

		npcServiceMap = cm.getConfig(SNpcServiceConfig.class);
	}

	public final SNpcServiceConfig getServiceConfigByNpcID(int npcid) {
		return npcServiceMap.get(npcid);
	}
}
