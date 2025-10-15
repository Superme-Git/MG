package fire.pb.scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import fire.msp.npc.MRemoveNpcsFromGS;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.npc.SNpcShare;
import fire.pb.scene.config.NpcDesc;
import fire.pb.scene.manager.PickUpManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Pickup;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.scene.sPos.ScreenPos;
import gnet.link.Onlines;

/**
 * <pre>
 * 场景信息,由地图信息和周边的角色、NPC等构成 场景和地图是有区别的,
 * 地图只是描述阻挡点,地图高度、宽度等信息 每个场景都在一个地图上,
 * 通过mapID标识将二者加以区别主要是想将地图数据和地图的上的基础逻辑分开.
 * </pre>
 */
public abstract class Scene {
	
	public static final Logger LOG = Logger.getLogger("MAPMAIN");
	
	private long sceneId = 0; // 该场景ID
	private String name = ""; // 场景名称
	private MapInfo mapInfo = null; // 地图配置信息
	private int roleNum = 0; // 场景中角色计数
	private MapConfig mapcfg; // 地图配置

	protected final Map<Integer, Screen> screens = new ConcurrentHashMap<Integer, Screen>();

	public static final int VISIBLE_ALL = 0; // 全部可见场景
	public static final int VISIBLE_SINGLE = 1;// 自己可见场景
	public static final int VISIBLE_TEAM = 2; // 小队可见场景

	private int dynamicType; // 0是静态地图,1是普通副本,2是梦境特殊副本(多人共用副本)

	/** 获取场景的地图ID */
	public abstract int getMapID();

	/** 获取场景所属者ID */
	public abstract long getOwnerID();

	/** 获取场景所属者名称 */
	public abstract String getOwnerName();

	/** 加载场景 */
	public abstract void load();

	/** 空场景的处理 */
	public abstract void emptyProcess();

	public void unload() {
		unloadNpc();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSceneID() {
		return sceneId;
	}

	void setSceneID(long sceneID) {
		this.sceneId = sceneID;
	}

	boolean equals(Scene o) {
		if (null == o)
			return false;

		return sceneId == o.sceneId;
	}

	public MapInfo getMapInfo() {
		return mapInfo;
	}

	void setMapInfo(MapInfo info) {
		mapInfo = info;
	}

	/** 返回格子高度 */
	public int getHighth() {
		return mapInfo.getBlockInfo().getHighth();
	}

	/** 返回格子宽度 */
	public int getWidth() {
		return mapInfo.getBlockInfo().getWidth();
	}

	public int getScreenWidth() {
		return getWidth() / ScreenPos.SCREEN_GRID_LENGTH;
	}

	public int getScreenHighth() {
		return getHighth() / ScreenPos.SCREEN_GRID_LENGTH;
	}

	/** 检测阻挡点信息 */
	public boolean checkBlock(GridPos grid, short blockType) {
		return mapInfo.getBlockInfo().checkBlock(grid, blockType);
	}

	/** 检测阻挡点信息 */
	public boolean checkBlock(Position pos, short blockType) {
		return mapInfo.getBlockInfo().checkBlock(pos, blockType);
	}
	
	public boolean checkBaitan(GridPos grid) {
		return mapInfo.getBlockInfo().checkBaitan(grid);
	}

	/** 检测是否为跳转点 */
	public boolean checkChannel(Position pos) {
		return checkBlock(pos.toGridPos(), BlockInfo.CHANNEL_BLOCK);
	}

	/** 获取跳转点信息 */
	public GotoInfo getGotoInfo(GridPos grid) {
		return mapInfo.getBlockInfo().getGotoInfo(grid.getX(), grid.getY());
	}

	/** 获取场景中的角色(所有) */
	public final Map<Long, Role> getSceneRoles() {
		final Map<Long, Role> retMap = new HashMap<Long, Role>();
		for (Screen sc : screens.values()) {
			retMap.putAll(sc.getAllRoles());
		}
		return retMap;
	}

	/** 将协议发送到场景上所有的角色 */
	public void sendAll(mkio.Protocol p2) {
		final Set<Long> roleIDs = new HashSet<Long>();
		for (Role role : getSceneRoles().values()) {
			roleIDs.add(role.getRoleID());
		}

		Onlines.getInstance().send(roleIDs, p2);
	}

	public Map<Long, Role> getSpecialRolesInScreen(int index) {
		final Screen s = screens.get(index);
		if (null == s)
			return new HashMap<Long, Role>();
		return s.getAllSpecialRoles();
	}

	public Map<Long, Role> getAllRolesInScreens(Set<Integer> indexs) {
		Map<Long, Role> roles = new HashMap<Long, Role>();

		for (int i : indexs) {
			final Screen sc = screens.get(i);
			if (null == sc)
				continue;

			roles.putAll(sc.getAllRoles());
		}

		return roles;
	}
	public Map<Long, NPC> getAllNpcsInScreens(Set<Integer> indexs) {
		Map<Long, NPC> npcs = new HashMap<Long, NPC>();

		for (int i : indexs) {
			final Screen sc = screens.get(i);
			if (null == sc)
				continue;

			npcs.putAll(sc.getNpcs());
		}

		return npcs;
	}

	public Map<Long, Pickup> getAllPickups() {
		Map<Long, Pickup> pickups = new HashMap<Long, Pickup>();
		final List<Pickup> pickuplist = PickUpManager.getInstance().getPickupsInScene(this);

		for (Pickup p : pickuplist)
			pickups.put(p.getUniqueID(), p);
		return pickups;
	}

	public Map<Long, Pickup> getAllPickupsInScreens(Set<Integer> indexs) {
		Map<Long, Pickup> pickups = new HashMap<Long, Pickup>();
		final List<Pickup> pickuplist = PickUpManager.getInstance().getPickupsInScreens(this, indexs);

		for (Pickup p : pickuplist)
			pickups.put(p.getUniqueID(), p);

		return pickups;
	}

	public Map<Long, NPC> getNpcsInScreen(int index) {
		final Screen s = screens.get(index);
		if (null == s)
			return new HashMap<Long, NPC>();
		return s.getNpcs();
	}

	public ScreenPos getScreenPos(Position p) {
		int x = p.getX() / ScreenPos.SCREEN_WIDTH;
		int y = p.getY() / ScreenPos.SCREEN_HIGHTH;
		final int w = this.getScreenWidth();
		final int h = this.getScreenHighth();

		x = (x < w) ? x : w - 1;
		y = (y < h) ? y : h - 1;

		return new ScreenPos(x, y);
	}

	public int getScreenIndex(Position p) {
		return getScreenPos(p).getScreenIndex();
	}

	/** 将角色从某屏块中移除 */
	public void removeBodyFromScreen(fire.pb.scene.movable.Body body) {
		final int index = body.getMyScreenIndex();

		final Screen s = screens.get(index);
		if (null == s) {
			SceneManager.logger.error("screen is null.mapid:" + getSceneID() + "index:" + index);
			return;
		}
		s.removeBody(body);
	}

	/** 开始摆摊，在屏块内添加 */
	public void beginStall(Role role) {
		final int index = role.getMyScreenIndex();

		final Screen s = screens.get(index);
		if (null == s)
			return;
		s.addSpecial(role);
	}

	/** 停止摆摊，在屏块内删掉 */
	public void stopStall(Role role) {
		final int index = role.getMyScreenIndex();

		final Screen s = screens.get(index);
		if (null == s)
			return;
		s.removeSpecial(role);
	}

	/** 在指定的index中获取 可见的 最大个数 角色（无序） */
	public Map<Long, Role> getRoleInScreensByNum(Set<Integer> indexs, int num, Role role) {
		Map<Long, Role> map = new HashMap<Long, Role>();

		// 默认的来说，自己队伍成员必须要看见
		if (role != null && role.getTeam() != null) {
			for (Role mem : role.getTeam().getAllTeammates()) {
				if (mem != role && mem.getScene() == this && indexs.contains(mem.getMyScreenIndex()))
					map.put(mem.getRoleID(), mem);
			}
		}

		// 摆摊角色
		for (int i : indexs) {
			final Screen s = screens.get(i);
			if (null == s)
				continue;
			for (Role stallrole : s.getAllSpecialRoles().values()) {
				if (role != null && stallrole.equals(role))
					continue;
				map.put(stallrole.getRoleID(), stallrole);
			}
		}
		int stallnum = map.size();// 能看到的摊位个数
		int sumnum = (int) Math.min((long) Integer.MAX_VALUE, (long) stallnum + (long) num);
		// 其他角色
		for (int i : indexs) {
			final Screen s = screens.get(i);
			if (null == s)
				continue;

			for (Role c : s.getAllRoles().values()) {
				if (map.size() >= sumnum)
					return map;

				if (role != null && c.equals(role))
					continue;

				if (c.isSpecialRole())
					continue;// 摆摊的人已经在之前加入了
				map.put(c.getRoleID(), c);
			}
		}

		return map;
	}

	/**
	 * 获取屏块中那些自己应该看见却看不到的角色
	 * 
	 */
	public Map<Long, Role> getRoleCannotSee(Set<Integer> indexes, int maxnum, Role role) {
		Map<Long, Role> notseeroles = new HashMap<Long, Role>();
		for (int index : indexes) {
			Screen s = getScreen(index);
			if (s == null)
				continue;
			for (Role r : s.getAllRoles().values()) {
				if (notseeroles.size() >= maxnum)
					return notseeroles;
				if (role.getIcansee().containsKey(r.getRoleID()))
					continue;
				if (role == r)
					continue;
				notseeroles.put(r.getRoleID(), r);
			}
		}
		return notseeroles;
	}

	/** 将角色添加至某个屏块索引中 */
	public void addBodyToScreen(fire.pb.scene.movable.Body body) {

		final int screenIndex = body.getMyScreenIndex();

		Screen s = screens.get(screenIndex);
		if (null == s) {
			s = new Screen(screenIndex);
			screens.put(screenIndex, s);
		}

		s.addBody(body);
	}

	public Map<Long, NPC> getSceneNpcs() {
		Map<Long, NPC> map = new HashMap<Long, NPC>();

		for (Screen sc : screens.values()) {
			map.putAll(sc.getNpcs());
		}

		return map;
	}

	public boolean checkPath(final List<Position> poslist, Position destPos, short blockType) {
		final BlockInfo blockInfo = mapInfo.getBlockInfo();
		if (null == blockInfo)
			return false;

		// 当前点不能为阻挡点
		if (blockInfo.checkTolerableBlock(destPos, blockType))
			return false;

		poslist.add(destPos);

		return blockInfo.checkWay(poslist, blockType);
	}

	public Position checkAndChangeGotoPos(Position pos) {
		GridPos grid = pos.toGridPos();
		if (!validPosition(pos)) {
			grid = MapUtil.genPosInMap(getSceneID());
			if (grid == null)
				return null;
			pos = new Position(grid.toPosition().getX(), grid.toPosition().getY());
			Scene.LOG.error("进行跳转的位置已经超过地图宽度,自动转移到一个非阻挡点");
		}

		if (!checkBlock(pos, pos.getWalkBlockType()))
			return pos;

		grid = MapUtil.getNearestUnblockGridPos(getSceneID(), pos.toGridPos(), BlockInfo.WALK_BLOCK);
		if (grid == null)
			return null;

		Scene.LOG.error(new StringBuilder().append("进行跳转的位置上有阻挡点,自动转移到最近的一个非阻挡点")
				.append(",Mapid:").append((int)getSceneID()).append(",posx:").append(pos.toGridPos().getX())
				.append(",posy:").append(pos.toGridPos().getY()));
		return grid.toPosition();
	}

	/**
	 * 检验该坐标在地图上是否是合法的坐标
	 * 
	 */
	public boolean validPosition(Position pos) {
		GridPos grid = pos.toGridPos();
		if (grid.getX() < 0 || grid.getY() < 0 || grid.getX() >= getWidth() || grid.getY() >= getHighth()) {
			return false;
		}
		return true;
	}

	protected void loadNpc() {
		if (null == mapInfo.getConfNpcList())
			return;

		for (final NpcDesc n : mapInfo.getConfNpcList()) {
			// Scene.logger.info("加载NPC\t" + n.toString());
			SNpcShare npcshare = ConfigManager.getInstance().getConf(SNpcShare.class).get(n.getid());
			if(npcshare == null) {
				Scene.LOG.error("我很抱歉~地图:" + getMapID() + ",NPC:" + n.getid() + "数据为null!");
			}
			if (npcshare.getDiankafu() == 1) { //只在点卡服上显示的npc
				if (fire.pb.fushi.Module.GetPayServiceType() == 0)
					continue;
			}
			
			if (npcshare.getDiankafu() == 2) { //点卡服上不能显示的npc
				if (fire.pb.fushi.Module.GetPayServiceType() == 1)
					continue;
			}
			
			NPC npc = SceneNpcManager.getInstance().summonNpcAndUpdateGS(this, n);
			if (npc == null)
				Scene.LOG.error("加载NPC\t" + n.toString() + " 出错");
		}
	}

	/**
	 * 卸载场景的同时也将npc卸载
	 */
	private void unloadNpc() {
		MRemoveNpcsFromGS snd = new MRemoveNpcsFromGS();

		for (NPC n : getSceneNpcs().values()) {
			SceneNpcManager.getInstance().delNpcWhileDestroyScene(n.getNpcKey());// 场景的npc管理器中删除
			snd.npckeys.add(n.getNpcKey());
		}
		if (snd.npckeys.size() > 0)
			SceneClient.pSend(snd);
	}

	public void onRoleEnter(Role r) {
		roleNum++;
	}

	public void onRoleLeave(Role r) {
		roleNum = (roleNum > 0) ? roleNum - 1 : 0;
		if (0 == roleNum)
			emptyProcess();
	}

	public boolean isEmpty() {
		return roleNum == 0;
	}

	@Override
	public String toString() {
		return new StringBuilder("场景ID：").append(sceneId).append("，名称：").append(name).toString();
	}

	public int getDynamicType() {
		return dynamicType;
	}

	public void setDynamicType(int dynamicType) {
		this.dynamicType = dynamicType;
	}

	public Screen getScreen(int screenindex) {
		return screens.get(screenindex);
	}

	public MapConfig getMapConfig() {
		return mapcfg;
	}

	public void setMapConfig(MapConfig mapcfg) {
		this.mapcfg = mapcfg;
	}

	/** 不是所有的场景都有天气 -1表示没有天气 */
	public int getWeatherId() {
		return -1;
	}

	public void setWeatherId(int weatherId) {
	}

	/** 只有人为改变的天气才有参数 其他的情况都没有tips参数 */
	public String getWeatherParm() {
		return "";
	}

}