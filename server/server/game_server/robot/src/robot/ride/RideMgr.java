package robot.ride;

import java.util.ArrayList;
import java.util.List;

import robot.Misc;

/**
 * 坐骑管理器
 * 
 * @author yt
 */
public class RideMgr {

	public static RideMgr _instance = new RideMgr();

	public static RideMgr getInstance() {
		return _instance;
	}

	public static List<Integer> rideItemIdList = new ArrayList<Integer>();

	public static List<Integer> mapIdList = new ArrayList<Integer>();
	static {
		rideItemIdList.add(331400);
		rideItemIdList.add(331401);
		rideItemIdList.add(331402);
		rideItemIdList.add(331403);
		rideItemIdList.add(331404);
		rideItemIdList.add(331405);
		rideItemIdList.add(331406);
		rideItemIdList.add(331407);
		rideItemIdList.add(331408);
		rideItemIdList.add(331409);
		mapIdList.add(1614);
		mapIdList.add(1615);
		mapIdList.add(1616);
		mapIdList.add(1620);
		mapIdList.add(1621);
		mapIdList.add(1622);
		mapIdList.add(1617);
		mapIdList.add(1618);
		mapIdList.add(1619);
	}

	/**
	 * 随机地图
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月24日 上午10:10:40
	 * @version 1.0
	 * @return
	 */
	public int randomInitMap() {
		int index = Misc.getRandomBetween(0, mapIdList.size() - 1);
		return mapIdList.get(index);
	}
}
