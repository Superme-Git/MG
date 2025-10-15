package fire.pb.scene.manager;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import fire.log.Logger;
import fire.pb.move.SAddPickupScreen;
import fire.pb.move.SRemovePickupScreen;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.movable.God;
import fire.pb.scene.movable.HongBaoitem;
import fire.pb.scene.movable.Pickup;
import fire.pb.scene.sPos.GridPos;

/**
 * 场景中拾取物品管理
 *
 */
public class PickUpManager {
	private static PickUpManager instance;
	public static final int PICK_UP_TYPE_TNSH = 1;
	public static final int PICK_UP_TYPE_JFJP = 2;
	public static final int PICK_UP_TYPE_HONGBAO = 3;
	public static final int PICK_UP_TYPE_BOATPARTYFOOD = 4;
	public static final int PICK_UP_TYPE_COMMONITEM = 5;

	public static long lastPickupTime = 0;

	Logger logger = Logger.getLogger("MAPMAIN");

	private PickUpManager(final fire.pb.main.ConfigManager cm) {
	}

	public static PickUpManager getInstance() {
		if (null == instance) {
			instance = new PickUpManager(fire.pb.main.ConfigManager.getInstance());
		}
		return instance;
	}

	public Pickup getPickup(Long uniqueID) {
		return pickups.get(uniqueID);
	}

	public Set<Long> getPickupKeyset() {
		return pickups.keySet();
	}

	public java.util.List<Pickup> getPickupsInScreens(Scene scene, java.util.Set<Integer> indexs) {
		final java.util.List<Pickup> list = new java.util.LinkedList<Pickup>();

		for (Pickup p : pickups.values()) {
			// 同场景
			if (p.getScene().equals(scene) && indexs.contains(p.getMyScreenIndex()))
				list.add(p);
		}

		return list;
	}

	public java.util.List<Pickup> getPickupsInScene(Scene scene) {
		final java.util.List<Pickup> list = new java.util.LinkedList<Pickup>();

		for (Pickup p : pickups.values()) {
			// 同场景
			if (p.getScene().equals(scene))
				list.add(p);
		}

		return list;
	}

	private void add(final Pickup p) {
		if (pickups.containsKey(p.getUniqueID())) {
			logger.info("pickups contains key.key:" + p.getUniqueID() + "pickups.size:" + pickups.size());
			return;
		}

		pickups.put(p.getUniqueID(), p);
		final SAddPickupScreen snd = new SAddPickupScreen();
		snd.pickuplist.add(p.getPickupBasic());
		logger.info("send pickup to client.size:" + snd.pickuplist.size());
		p.sendAround(snd);
	}

	/**
	 * 从场景中删除拾取物品
	 */
	public void remove(final long id) {
		final Pickup p = pickups.get(id);
		if (null == p)
			return;

		final Scene ps = p.getScene();
		if (null == ps)
			return;

		final SRemovePickupScreen snd = new SRemovePickupScreen();
		snd.pickupids.add(id);
		p.sendAround(snd);

		pickups.remove(id);
	}

	/**
	 * 在场景中播洒拾取物品
	 * 
	 * @param baseID
	 *            拾取物的模板ID
	 * @param mapID
	 *            地图
	 * @param pos
	 *            位置
	 * @return 生成的拾取物
	 */
	public Pickup plant(final int baseID, final long mapID, final GridPos pos, int pickupType) {

		final long id = nextkey.incrementAndGet();
		if (id > Long.MAX_VALUE)
			return null;
		final Scene scene = SceneManager.getInstance().getSceneByID(mapID);
		if (null == scene)
			return null;

		final Pickup pickup = God.createPickup(id, baseID, null, scene, pos.toPosition(), pickupType);

		add(pickup);
		return pickup;
	}

	public HongBaoitem plantHongBao(final int baseID, final long mapID, final GridPos pos, long moneyNum) {
		final long id = nextkey.incrementAndGet();
		if (id > Long.MAX_VALUE)
			return null;
		final Scene scene = SceneManager.getInstance().getSceneByID(mapID);
		if (null == scene)
			return null;

		HongBaoitem HongBaoitem = God.createHongBaoitem(id, baseID, scene, pos.toPosition(), moneyNum);
		add(HongBaoitem);
		return HongBaoitem;
	}

	/**
	 * 获取某个地图内拾取物的总数量
	 * 
	 * @param mapID
	 *            地图ID
	 */
	public int getPickupNum(final long sceneid, int pickupType) {
		int val = 0;
		synchronized (pickups) {
			for (Pickup p : pickups.values())
				if (p.getScene().getMapID() == sceneid && p.getPickupType() == pickupType)
					val++;
		}
		return val;
	}

	/**
	 * 获取某个地图内拾取物的总数量
	 * 
	 * @param mapID
	 *            地图ID
	 */
	public int getPickupBaseNum(final long sceneid, int baseId) {
		int val = 0;
		synchronized (pickups) {
			for (Pickup p : pickups.values())
				if (p.getScene().getMapID() == sceneid && p.getBaseID() == baseId)
					val++;
		}
		return val;
	}

	public Map<Long, GridPos> hongbaoList = new ConcurrentHashMap<Long, GridPos>();

	private final Map<Long, Pickup> pickups = new ConcurrentHashMap<Long, Pickup>();
	private java.util.concurrent.atomic.AtomicLong nextkey = new java.util.concurrent.atomic.AtomicLong(0);
}
