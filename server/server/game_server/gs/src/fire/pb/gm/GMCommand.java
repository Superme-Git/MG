package fire.pb.gm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import fire.pb.item.ItemShuXing;
import fire.pb.map.MapConfig;
import fire.pb.npc.SNpcShare;
import fire.pb.pet.PetAttr;
import fire.pb.talk.MessageMgr;

import org.apache.log4j.Logger;

public abstract class GMCommand {
	static class FormatError extends RuntimeException {
		private static final long serialVersionUID = 1L;

	}

	protected final static String Prefix = "<T t=\"";
	protected final static String Suffix = "\"></T><B></B>";
	protected static Logger logger = Logger.getLogger("GMCOMMAND");

	private long gmroleid;

	private int gmUserid;
	private int localsid;

	public void setGmroleid(long gmroleid) {
		this.gmroleid = gmroleid;
	}

	public long getGmroleid() {
		return gmroleid;
	}

	abstract boolean exec(String[] args);

	abstract String usage();

	public boolean sendToGM(String str) {
		if (gmroleid <= 0)
			return false;
		MessageMgr.sendMsgToPop(gmroleid, str);
		if (fire.pb.main.Gs.isDebug())
			logger.debug("gmroleid=" + gmroleid + str);
		return true;
	}

	public int getGmUserid() {

		return gmUserid;
	}

	public void setGmUserid(int gmUserid) {

		this.gmUserid = gmUserid;
	}

	public void setGmLocalsid(int localsid) {
		this.localsid = localsid;
	}

	public int getGmLocalsid() {
		return localsid;
	}

	public static Map<String, Integer> taskname2id = new HashMap<String, Integer>();
	static {
		for (fire.pb.mission.MissionConfig conf : fire.pb.mission.MissionManager
				.getInstance().missionConfMap.values()) {
			taskname2id.put(conf.missionName, conf.missionId);
		}
	}
	public static Map<String, Integer> objectName2Id = new HashMap<String, Integer>();

	static {
		NavigableMap<Integer, ItemShuXing> itemAttrMap = fire.pb.item.Module
				.getInstance().getItemManager().getAttrMap();
		Iterator<Entry<Integer, ItemShuXing>> attrIterator = itemAttrMap
				.entrySet().iterator();
		while (attrIterator.hasNext()) {
			Entry<Integer, ItemShuXing> current = attrIterator.next();
			objectName2Id.put(current.getValue().getName(), current.getKey());
		}

		java.util.Map<Integer, MapConfig> mapConfig = fire.pb.main.ConfigManager
				.getInstance().getConf(fire.pb.map.MapConfig.class);
		Iterator<Entry<Integer, MapConfig>> mapIterator = mapConfig.entrySet()
				.iterator();
		while (mapIterator.hasNext()) {
			Entry<Integer, MapConfig> current = mapIterator.next();
			objectName2Id
					.put(current.getValue().getMapName(), current.getKey());
		}

		java.util.NavigableMap<Integer, SNpcShare> npcShareMap = fire.pb.main.ConfigManager
				.getInstance().getConf(fire.pb.npc.SNpcShare.class);
		Iterator<Entry<Integer, SNpcShare>> npcMap = npcShareMap.entrySet()
				.iterator();
		while (npcMap.hasNext()) {
			Entry<Integer, SNpcShare> current = npcMap.next();
			objectName2Id.put(current.getValue().getName(), current.getKey());
		}

		java.util.NavigableMap<Integer, PetAttr> petMap = fire.pb.main.ConfigManager
				.getInstance().getConf(fire.pb.pet.PetAttr.class);
		Iterator<Entry<Integer, PetAttr>> petIterator = petMap.entrySet()
				.iterator();
		while (petIterator.hasNext()) {
			Entry<Integer, PetAttr> current = petIterator.next();
			objectName2Id.put(current.getValue().getName(), current.getKey());
		}

	}

	public final Integer getObjectIdByName(String name) {
		return objectName2Id.get(name);
	}

}
