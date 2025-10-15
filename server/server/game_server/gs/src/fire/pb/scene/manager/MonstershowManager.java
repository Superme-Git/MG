package fire.pb.scene.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.map.SShowAreainfo;
import fire.pb.move.SceneState;
import fire.pb.scene.BlockInfo;
import fire.pb.scene.MonsterInfo;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneManager;
import fire.pb.scene.StaticScene;
import fire.pb.scene.movable.God;
import fire.pb.scene.movable.Monstershow;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;

import org.apache.log4j.Logger;

public class MonstershowManager {
	
	private MonstershowManager() {
	}

	private static MonstershowManager instance = new MonstershowManager();

	public static MonstershowManager getInstance() {
		if (instance == null) {
			instance = new MonstershowManager();
		}
		return instance;
	}

	private static final int NEAR = 3;
	private final java.util.NavigableMap<Integer, fire.pb.map.SShowAreainfo> monstershowareaMap = fire.pb.main.ConfigManager
			.getInstance().getConf(fire.pb.map.SShowAreainfo.class);
	// 按地图,区域类型分类
	private volatile Map<Long, Map<Integer, Set<Long>>> monsters = new HashMap<Long, Map<Integer, Set<Long>>>();

	private boolean isNear(GridPos pos1, GridPos pos2) {
		// 附近3范围内
		return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY()) <= NEAR;
	}

	/**
	 * 获得将要战斗的怪物
	 */
	public NPC getMonsterFightInFuture(Role role) {
		if (role.checkSceneState(SceneState.BATTLE_FIGHTER))
			return null;

		final Scene rs = role.getScene();
		if (null == rs)
			return null;

		final Set<Long> monstersInMap = new HashSet<Long>();
		final Map<Integer, Set<Long>> allmonstershows = monsters.get(rs.getSceneID());
		if (allmonstershows != null) {
			for (final Set<Long> monstershows : allmonstershows.values()) {
				monstersInMap.addAll(monstershows);
			}
			;
		}
		if (monstersInMap.isEmpty())
			return null;
		for (Long mkey : monstersInMap) {
			NPC npc = SceneNpcManager.getInstance().getNpcByKey(mkey);
			if (npc == null) {
				remove(mkey);
				logger.error("不存在的明雷怪key=" + mkey);
				continue;
			}
			if (!(npc instanceof Monstershow)) {
				remove(mkey);
				logger.error("明雷怪key=" + mkey + "不是明雷怪");
				continue;
			}
			Monstershow m = (Monstershow) npc;
			if (role.getScene() == m.getScene() && isNear(role.getPos().toGridPos(), m.getPos().toGridPos())) {

				if (m.getInBattle() == NPC.NOT_INBATTLE && !m.isLoser(role.getRoleID()))
					return SceneNpcManager.getInstance().getNpcByKey(m.getUniqueID());
			}
		}
		return null;
	}

	private int getMonsterNum(final long sceneID, final int areatype) {
		final Map<Integer, Set<Long>> monstersInMap = monsters.get(sceneID);
		if (monstersInMap == null) {
			return 0;
		}
		final Set<Long> monsters = monstersInMap.get(areatype);
		if (monsters == null)
			return 0;
		return monsters.size();
	}

	private static final Logger logger = Logger.getLogger("MAPMAIN");

	private boolean genMonstershow(final int baseID, final Scene scene, final GridPos pos) {

		// 随机一种
		final long npckey = SceneNpcManager.getInstance().getNextId();
		Monstershow monster = God.createMonstershow(npckey, baseID);
		SceneNpcManager.getInstance().summonNpcAndUpdateGS(scene, pos.toPosition(), monster);
		final int areaconfig = scene.getMapInfo().getMonsterInfo().getMonsterConfig(pos.getX(), pos.getY());
		if (areaconfig >= 0) {
			return false;
		}
		Map<Integer, Set<Long>> monstersInMap = monsters.get(scene.getSceneID());
		if (monstersInMap == null) {
			monstersInMap = new HashMap<Integer, Set<Long>>();
			monsters.put(scene.getSceneID(), monstersInMap);
		}
		Set<Long> areamonsters = monstersInMap.get(Math.abs(areaconfig));
		if (areamonsters == null) {
			areamonsters = java.util.Collections.synchronizedSet(new HashSet<Long>());
			monstersInMap.put(Math.abs(areaconfig), areamonsters);
		}
		if (areamonsters.add(monster.getUniqueID())) {
			logger.debug("在地图id=" + scene.getMapID() + "位置(" + pos.getX() + ", " + pos.getY() + ")生成怪" + baseID
					+ ", 怪物id=" + monster.getUniqueID());
			return true;
		}
		return false;
	}

	public boolean remove(long key) {
		for (Map.Entry<Long, Map<Integer, Set<Long>>> e : monsters.entrySet()) {
			Map<Integer, Set<Long>> monstersInMap = e.getValue();
			for (final Set<Long> areamonsters : monstersInMap.values())
				if (areamonsters.remove(key)) {
					if (monsters.put(e.getKey(), monstersInMap) != null) {
						return SceneNpcManager.getInstance().removeNpcFromScene(key, "MonstershowManager.remove");
					}
				}
		}
		return false;
	}

	public void genMonstershow() {
		// 所有地图都刷
		for (StaticScene scene : SceneManager.getInstance().getAllStaticScenes().values()) {

			for (Map.Entry<Integer, Set<MonsterInfo.Pos>> posE : scene.getMapInfo().getMonsterInfo()
					.getMonstershowArea().entrySet()) {
				List<MonsterInfo.Pos> poslist = new ArrayList<MonsterInfo.Pos>();
				poslist.addAll(posE.getValue());
				final int maxNum = monstershowareaMap.get(posE.getKey()).maxnum;
				int i = MonstershowManager.getInstance().getMonsterNum(scene.getSceneID(), posE.getKey());
				for (; i < maxNum && i < 2000;) {
					MonsterInfo.Pos pos = poslist.get(fire.pb.util.Misc.getRandomBetween(0, poslist.size() - 1));
					GridPos gpos = new GridPos(pos.x, pos.y);
					if (scene.checkBlock(gpos, BlockInfo.WALK_BLOCK)) {
						continue;
					}
					i++;
					ArrayList<Integer> monsters = genMonstershowByAreaType(posE.getKey());
					if (monsters != null) {
						genMonstershow(monsters.get(fire.pb.util.Misc.getRandomBetween(0, monsters.size() - 1)), scene,
								gpos);
					}

				}
			}
		}
	}

	private ArrayList<Integer> genMonstershowByAreaType(int areatype) {
		SShowAreainfo info = monstershowareaMap.get(areatype);
		if (info == null)
			return null;
		return info.getMonsters();
	}
}
