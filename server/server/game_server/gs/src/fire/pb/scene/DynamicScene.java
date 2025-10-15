package fire.pb.scene;

import gnet.link.Onlines;

import java.util.Collection;
import java.util.LinkedList;

import fire.msp.move.DynamicSceneParams;
import fire.msp.move.MDestroyDynamicScene;
import fire.msp.npc.MUpdateNpcInfo;
import fire.msp.npc.NpcInfo;
import fire.pb.move.SAddActivityNpc;
import fire.pb.move.SRemoveActivityNpc;
import fire.pb.move.SRoleEnterScene;
import fire.pb.redirect.Redirect;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.Position;
import fire.pb.util.Parser;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 动态地图(副本)基类
 * 
 * @author nobody
 * 
 */
public class DynamicScene extends Scene {

	/**
	 * 加载副本信息 主要是地图阻挡点,布怪和NPC配置
	 */
	public void load() {
		
		template = SceneManager.getInstance().getDynamicTemplateByMapID(getMapID());
		if (null == template) {
			setSceneID(0L);
			Scene.LOG.info("无法找到副本场景模板\t" + getMapID());
			return;
		}

		setName(template.getName());
		setSceneID(template.getNextDynamicSceneId());
		setDynamicType(template.getDynamic());
		setMapInfo(template.getMapInfo());
		setMapConfig(template.getMapConfig());
		loadNpc();

		template.addNewScene(this);
		emptytime = System.currentTimeMillis();
	}

	/**
	 * 参数为octets的原因是便于扩展，普通动态场景下这个octets是DynamicSceneParams类型
	 * 
	 * @param octets
	 */
	public void update(Octets octets) {
		if (octets == null)
			return;
		DynamicSceneParams params = new DynamicSceneParams();
		try {
			params.unmarshal((OctetsStream) octets);

		} catch (MarshalException e) {
			Scene.LOG.error("更新副本时，unmarshal变量出错：", e);
			return;
		}

		// 更新npc

		// 添加的npc
		MUpdateNpcInfo mupdatenpc = new MUpdateNpcInfo();
		for (fire.msp.move.CreateNpcInfo createinfo : params.addnpcs) {
			NPC npc = SceneNpcManager.getInstance().createNpc(createinfo.npcid,
					createinfo.npcname, createinfo.dir);
			if (SceneNpcManager.getInstance().summonNpc(this,
					new Position(createinfo.xpos, createinfo.ypos), npc)) {
				NpcInfo npcinfo = new NpcInfo(npc.getUniqueID(),
						npc.getNpcID(), npc.getName(), npc.getShape(), npc
								.getScene().getSceneID(), npc.getScene()
								.getMapID(), npc.getScene().getName(), npc
								.getPos().getX(), npc.getPos().getY(),npc.getPos().getZ(), 0);
				mupdatenpc.npcs.add(npcinfo);
				// 为minimap保存
				if (npc.isMiniMapShow()) {
					if (removeminimapnpc.npcids.contains(npc.getNpcID()))
						removeminimapnpc.npcids
								.remove((Integer) npc.getNpcID());
					else if (!addminimapnpc.npcids.contains(npc.getNpcID())) {
						addminimapnpc.npcids.add(npc.getNpcID());
						addminimapnpc.poslist.add(npc.getPos().toProtocolPos());
					}
				}
			}
		}
		SceneClient.pSend(mupdatenpc);

		String trace = Parser.convertfireStackTrace2String(Thread.currentThread().getStackTrace());
		// 删除的npc
		for (NPC npc : getSceneNpcs().values()) {
			// Npc npc = SceneNpcManager.getInstance().getNpcByKey(npckey);
			if (params.delnpcs.contains(npc.getNpcID())) {
				SceneNpcManager.getInstance().removeNpcFromScene(
						npc.getNpcKey(),trace);
				// 为minimap保存
				if (npc.isMiniMapShow()) {
					boolean isexist = false;
					for (int i = 0; i < addminimapnpc.npcids.size(); i++) {
						int nid = addminimapnpc.npcids.get(i);
						if (nid == npc.getNpcID()) {
							addminimapnpc.npcids.remove(i);
							addminimapnpc.poslist.remove(i);
							isexist = true;
							break;
						}
					}
					if (!isexist
							&& !removeminimapnpc.npcids
									.contains(npc.getNpcID()))
						removeminimapnpc.npcids.add(npc.getNpcID());
				}
			}
		}

		// 隐藏或显示的npc
		for (NPC npc : getSceneNpcs().values()) {
			Integer state = params.npcstates.get(npc.getNpcID());
			if (state != null)
				npc.updateVisible(state == 0);
		}

		// 更新小地图
		sendActiveMiniMapNpcToAll();
	}

	/**
	 * 卸载地图
	 * 
	 * @throws Throwable
	 */
	public void unload() {
		super.unload();
		// 赶走所有角色
		LinkedList<Long> roleidsInScene = new LinkedList<Long>();
		Collection<Role> roles = getSceneRoles().values();
		for (Role role : roles) {
			roleidsInScene.add(role.getRoleID());
			Redirect.getoutFromDynamicScene(role, this,
					SRoleEnterScene.FORCE_GOTO);
		}
		String trace = Parser.convertfireStackTrace2String(Thread.currentThread().getStackTrace());
		// 删除所有npc
		for (NPC n : getSceneNpcs().values())
			SceneNpcManager.getInstance().removeNpcFromScene(n.getNpcKey(),trace);
		// 删除
		// SceneManager.getInstance().removeScene(getSceneID());
		template.removeScene(getSceneID());
		// 通知逻辑端
		SceneClient.pSend(new MDestroyDynamicScene(getSceneID(), getOwnerID(),
				getScenetype(), roleidsInScene));
		Scene.LOG.info("DynamicScene unloaded. BaseMapId = " + baseMapID
				+ " , OwnerId = " + ownerID + " , OwnerName =" + ownerName);
	}

	@Override
	public void emptyProcess() {
		emptytime = System.currentTimeMillis();
		Scene.LOG.debug("===============================场景中的角色已经空了\t"
				+ getName());
	}

	@Override
	public int getMapID() {
		return baseMapID;
	}

	void setMapID(int mapid) {
		baseMapID = mapid;
	}

	public long getOwnerID() {
		return ownerID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	void setOwnerID(final long ownerid) {
		ownerID = ownerid;
	}

	public int getScenetype() {
		return scenetype;
	}

	public void setScenetype(int scenetype) {
		this.scenetype = scenetype;
	}

	public void setInitparams(Octets initparams) {
		this.initparams = initparams;
	}

	public Octets getInitparams() {
		return initparams;
	}

	public int getBaseMapID() {
		return baseMapID;
	}

	public DynamicTemplate getTemplate() {
		return template;
	}

	/**
	 * livetime为负，一直不销毁 livetime == 0，默认时间销毁，暂定600秒 livetime > 0,x秒后销毁
	 * 
	 * @param seconds
	 */
	public void setLiveTime(int seconds) {
		if (seconds == 0)
			this.livetime = DEFAULT_LIVE_TIME_MILLISECONDS;
		else if (seconds < 0)
			this.livetime = -1;
		else
			this.livetime = seconds * 1000;
	}

	/**
	 * 副本是否存活
	 * 
	 * @param now
	 *            当前时间
	 * @return
	 */
	public boolean isAlive(long now) {
		if (livetime < 0)
			return true;

		if (!isEmpty())
			return true;				// 副本内还有人

		if (emptytime <= 0)
			return true;

		return (now - emptytime) < livetime;
	}

	@Override
	public void onRoleEnter(Role r) {
		super.onRoleEnter(r);
		emptytime = 0;				// 重置场景空的时间
		
		sendActiveMiniMapNpc(r.getRoleID());
	}

	/**
	 * 发送动态的小地图npc变化
	 */
	public void sendActiveMiniMapNpcToAll() {
		if (addminimapnpc.npcids.size() > 0)
			sendAll(addminimapnpc);
		if (removeminimapnpc.npcids.size() > 0)
			sendAll(removeminimapnpc);
	}

	public void sendActiveMiniMapNpc(long roleid) {
		if (addminimapnpc.npcids.size() > 0)
			Onlines.getInstance().send(roleid, addminimapnpc);
		if (removeminimapnpc.npcids.size() > 0)
			Onlines.getInstance().send(roleid, removeminimapnpc);
	}

	private int baseMapID = 0;
	private long ownerID = 0;
	private String ownerName = "";
	private DynamicTemplate template;
	private long emptytime = 0;			// 场景变为空的时间,0表示非空
	private long livetime = 0; 			// 空场景存活的时间
	private int scenetype = 0;			// 动态场景的类型，例如是帮派场景或者梦境场景
	private Octets initparams;			// 初始化场景用到的变量，可能不同类型的场景有所不同

	private SAddActivityNpc addminimapnpc = new SAddActivityNpc();
	private SRemoveActivityNpc removeminimapnpc = new SRemoveActivityNpc();

	private static int DEFAULT_LIVE_TIME_MILLISECONDS = 600000;// 默认副本存活时间，10分钟

}
