package fire.pb.activity.timernpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.util.Misc;

/**
 * 基础的活动信息
 * @author nobody
 *
 */
public class ActivityInfo {

	/** 活动id */
	private int id;
	/** 活动基础id*/
	private int baseId;
	/** 活动开始时间 */
	private long startTime;
	/** 活动结束时间 */
	private long endTime;
	
	/** 随机到的地图ID*/
	private int mapId;
	
	/** 活动的怪物和地图信息 key为mapId, value 为NpcId List*/
	private Map<Integer, List<Integer>> npcMap = new HashMap<Integer, List<Integer>>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBaseId() {
		return baseId;
	}
	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public void setMapInfo(Map<Integer, List<Integer>> mapInfo) {
		this.npcMap.putAll(mapInfo);
	}
	public Map<Integer, List<Integer>> getMapInfo() {
		return npcMap;
	}
	
	public int getRndMapId() {
		Integer [] mapArray = new Integer[npcMap.keySet().size()];
		mapArray = npcMap.keySet().toArray(new Integer[0]);
		
		if (mapArray.length == 0)
			return 0;
		
		int nIndex = Misc.getRandomBetween(0, mapArray.length - 1);
		int nMapId = mapArray[nIndex];
		if (nMapId > 0)
			return nMapId;
		
		return mapId;
	}
	/**
	 * 获取一定数量的npcid
	 * @param nCnt
	 * @return
	 */
	public List<Integer> getRndNpcId(int nCnt) {
		if (nCnt <= 0)
			return null;
		Integer [] mapArray = new Integer[npcMap.keySet().size()];
		mapArray = npcMap.keySet().toArray(new Integer[0]);
		
		int nIndex = Misc.getRandomBetween(0, mapArray.length - 1);
		mapId = mapArray[nIndex];
		
		List<Integer> retLst = new ArrayList<Integer>();
		
		while (nCnt > retLst.size()) {
			List<Integer> npcList = Misc.getRandomList(npcMap.get(mapId), nCnt - retLst.size());
			if (npcList.size() > 0) {
				retLst.addAll(npcList);
			}
			else {
				//保护不至于表填错
				nCnt --;
			}
		}
		
		return retLst;
	}
	
}
