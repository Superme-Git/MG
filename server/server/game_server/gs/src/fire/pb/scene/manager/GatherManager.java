package fire.pb.scene.manager;

import java.util.HashMap;
import java.util.Map;

import fire.msp.npc.MStartGather;
import fire.pb.scene.movable.GatherNpc;
import fire.pb.scene.movable.God;

/**
 * 可采集的npc管理
 * 
 */

public class GatherManager {
	private static final GatherManager instance = new GatherManager();

	public static GatherManager getInstance() {
		return instance;
	}

	private final Map<Integer, Map<Long, GatherNpc>> gatherMap = new HashMap<Integer, Map<Long, GatherNpc>>();

	public GatherNpc genGathernpc(long npckey, int npcid, String name, int dir) {
		return God.createGatherNpc(npckey, npcid, name, null, null, dir);
	}

	public void removeGathernpc(int mapID, long npckey) {
		Map<Long, GatherNpc> mapgathers = gatherMap.get(mapID);
		if (mapgathers != null) {
			mapgathers.remove(npckey);
		}
		String trace = fire.pb.util.Parser.convertfireStackTrace2String(Thread.currentThread().getStackTrace());
		fire.pb.scene.manager.SceneNpcManager.getInstance().removeNpcFromScene(npckey, trace);
	}

	public boolean tryGather(final long roleid, final long gatherkey) {
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return false;
		GatherNpc gn = getGatherNpc(role.getScene().getMapID(), gatherkey);
		if (gn == null)
			return false;
		if (gn.canGather(roleid)) {
			MStartGather send = new MStartGather();
			send.gatherid = gn.getNpcID();
			send.gatherkey = gn.getUniqueID();
			send.roleid = roleid;
			fire.pb.scene.SceneClient.pSend(send);
			return true;
		}
		return false;
	}

	public void onGatherEnd(final long roleid, final long gatherkey, final int result) {
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID(roleid);
		if (role == null)
			return;
		GatherNpc gn = getGatherNpc(role.getScene().getMapID(), gatherkey);
		if (gn == null)
			return;
		gn.onGatherEnd(roleid, result);
	}

	private GatherNpc getGatherNpc(final int mapid, final long gatherkey) {
		Map<Long, GatherNpc> mapgathers = gatherMap.get(mapid);
		if (mapgathers == null)
			return null;
		return mapgathers.get(gatherkey);
	}

	public Map<Long, GatherNpc> getGathersByMapID(int mapID) {
		return gatherMap.get(mapID);
	}

	public Map<Integer, Map<Long, GatherNpc>> getGatherMap() {
		return gatherMap;
	}

}
