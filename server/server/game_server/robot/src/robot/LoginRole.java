package robot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mkio.ILoginUI;
import mkio.Protocol;
import robot.pos.GridPos;
import robot.task.Task_RoleBase;
import robot.team.Team;
import robot.team.TeamRoleState;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;

import fire.pb.Bag;
import fire.pb.Item;
import fire.pb.gm.CSendCommand;
import fire.pb.item.BagTypes;
import fire.pb.move.RoleBasicOctets;
import fire.pb.move.SRoleEnterScene;
import fire.pb.move.SRoleJumpDrawback;
import fire.pb.move.SRoleMove;
import fire.pb.talk.CTransChatMessage2Serv;
import fire.pb.talk.ChannelType;

/**
 * 角色相关
 * 
 * @author Bill_Ye
 */
public class LoginRole {

	public final long roleId;

	public final int userId;

	public LoginUI loginui;

	private volatile long sceneid = -1;

	private volatile int posx = -1;

	private volatile int posy = -1;

	private Task_RoleBase roletask = null;

	private RoleBasicOctets rolebase = null;

	private int scenestate = 0;

	public int school;

	public String rolename;

	private Team team;

	// 角色等级
	private int level;

	// 是否跳跃
	private boolean initump = false;

	// 是否可以跳
	private boolean canJump = false;

	// 交易Id
	private long tradeId = -1;

	// 0自由人 1 主动申请 2被动申请人
	private byte tradeState = 0;

	// 家族Id
	private int familyid;

	private int familyLevel;

	// private int positionInFamily = FamilyPositionType.FamilyCommon; //家族职务
	private int positionInFaction = -1; // 帮派职务

	private Map<Long, Integer> friends = new HashMap<Long, Integer>(); // key为好友roleid，value为在线状态：0在线，1为离线

	// private List<CommonGroupInfo> grouplist; // 所有群组的信息
	// 帮派Id
	private int factionId;

	public java.util.HashMap<Integer, fire.pb.Bag> baginfo; // key是bagid,value是包裹的详细信息

	// 角色背包
	private RoleBag roleBag;

	private Lock lock;

	public LoginRole(long roleId, int userId) {
		this.roleId = roleId;
		this.userId = userId;
		lock = new ReentrantLock();
	}

	public LoginRole(long roleId, int userId, fire.pb.Bag bag, java.util.HashMap<Integer, fire.pb.Bag> bags,
			java.util.ArrayList<fire.pb.Pet> pets) {
		this.roleId = roleId;
		this.userId = userId;
		roleBag = new RoleBag(this, bag, pets);
		baginfo = bags;
		lock = new ReentrantLock();
	}

	/**
	 * 向背包增加物品
	 * 
	 * @param key
	 * @param item
	 */
	public void addItem(int type, java.util.ArrayList<fire.pb.Item> items) {
		lock.lock();
		try {
			Bag bag = baginfo.get(type);
			if (bag != null) {
				for (fire.pb.Item item : items) {
					// mkdb.Trace.info("背包添加物品信息  "+item.toString());
					if (item != null)
						bag.items.add(item);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public boolean canJump() {
		return canJump;
	}

	public synchronized boolean canMove() {
		return (scenestate & Constant.CANT_MOVE_STATE_SUM) == 0;
	}

	public int getFactionId() {
		return factionId;
	}

	public int getFamilyid() {
		return familyid;
	}

	public int getFamilyLevel() {
		return familyLevel;
	}

	public Map<Long, Integer> getFriends() {
		return friends;
	}

	public final int getGridX() {
		return posx / GridPos.GRID_WIDTH;
	}

	public final int getGridY() {
		return posy / GridPos.GRID_HIGHTH;
	}

	public int getLevel() {
		return level;
	}

	public LoginUI getLoginui() {
		return loginui;
	}

	public final int getMapId() {
		return (int) sceneid;
	}

	public int getPositionInFaction() {
		return positionInFaction;
	}

	public final int getPosX() {
		return posx;
	}

	public final int getPosY() {
		return posy;
	}

	public RoleBag getRoleBag() {
		return roleBag;
	}

	public RoleBasicOctets getRoleBase() {
		return rolebase;
	}

	public Task_RoleBase getRoleTask() {
		return roletask;
	}

	public final long getSceneID() {
		return sceneid;
	}

	public int getSceneState() {
		return scenestate;
	}

	/**
	 * 根据物品编号获得物品key
	 * 
	 * @param itemId
	 * @return
	 */
	public int getTaskItemKey(int itemId) {
		Bag bag = baginfo.get(BagTypes.QUEST);
		for (Item item : bag.items) {
			if (item == null) {
				continue;
			}
			if (itemId == item.id) {
				return item.key;
			}
		}
		// 任务背包没有，从背包里找一个
		bag = baginfo.get(BagTypes.BAG);
		for (Item item : bag.items) {
			if (item == null) {
				continue;
			}
			if (itemId == item.id) {
				return item.key;
			}
		}
		return -1;
	}

	public Team getTeam() {
		return team;
	}

	/**
	 * 获取组队状态
	 * 
	 * @return 参考TeamState
	 */
	public int getTeamState() {
		if (team == null)
			return TeamRoleState.TEAM_NULL;
		if (team.isLeader(roleId))
			return TeamRoleState.TEAM_LEADER;
		Integer memberstate = team.getMemberState(roleId);
		if (memberstate != null)
			return memberstate;
		else
			return TeamRoleState.TEAM_NULL;
	}

	public long getTradeId() {
		return tradeId;
	}

	public byte getTradeState() {
		return tradeState;
	}

	// gm跳转
	public final void gmgoto(int mapId, int gridx, int gridy) {
		sendProtocol(new CSendCommand("//goto " + gridx + " " + gridy + " " + mapId));
	}

	// 登录的时候发送给过来
	// public final synchronized void onSUpdateFamilyData(SUpdateFamilyData p){
	// if(roletask != null){
	// roletask.processProtocol(p);
	// return;
	// }
	// // familyid = p.familydata.familyid;
	// // familyLevel = p.familydata.familylevel;
	// }
	public boolean hasFriend(long roleid) {
		return friends.containsKey(roleid);
	}

	public boolean isFriendFull(int numMax) {
		return friends.size() >= numMax;
	}

	public boolean isInitump() {
		return initump;
	}

	/**
	 * 是否正在执行
	 * 
	 * @return
	 */
	public boolean isRunTask() {
		return getRoleTask() != null;
	}

	public boolean isTradeFree() {
		return tradeState == 0;
	}

	public boolean isTrading() {
		return tradeId > 0;
	}

	public final synchronized void onDealProtocal(mkio.Protocol p) {
		if (roletask != null) {
			roletask.processProtocol(p);
		}
	}

	public final synchronized void onDisconnected() {
		if (getRoleTask() != null)
			stopRoleTask();
		LoginRoleMgr.getInstance().removeLoginRole(roleId);
	}

	/**
	 * 设置家族职称
	 * 
	 * @param positionInFamily
	 */
	// public void setPositionInFamily(int positionInFamily) {
	// this.positionInFamily = positionInFamily;
	// }
	public void onFriendStatusChange(long roleid, int status) {
		friends.put(roleid, status);
	}

	// public final synchronized void onRoleGetFamilyList(SFamilyInfoList p){
	// if(roletask != null && roletask instanceof RandomFamilyTask){
	// roletask.processProtocol(p);
	// }
	// }
	public final synchronized void onRoleAddProtocol(Protocol p) {
		if (roletask != null) {
			roletask.processProtocol(p);
		}
	}

	/**
	 * 处理跳转
	 * 
	 * @param p
	 */
	public final synchronized void onRoleEnterScene(SRoleEnterScene p) {
		sceneid = p.sceneid;
		posx = p.destpos.x;
		posy = p.destpos.y;
		if (roletask != null) {
			roletask.processProtocol(p);
			return;
		}
	}

	public final synchronized void onRoleJumpBack(SRoleJumpDrawback p) {
		if (p.roleid != this.roleId)
			return;
		if (roletask != null) {
			roletask.processProtocol(p);
			return;
		}
		posx = p.srcpos.x;
		posy = p.srcpos.y;
	}

	/**
	 * 处理服务器行走
	 * 
	 * @param p
	 */
	public final synchronized void onRoleMove(SRoleMove p) {
		if (p.roleid != this.roleId)
			return;
		if (roletask != null) {
			roletask.processProtocol(p);
			return;
		}
		posx = p.destpos.x;
		posy = p.destpos.y;
	}

	public synchronized void runRoleTask() {
		if (this.roletask == null)
			return;
		this.roletask.run();
	}

	/**
	 * 角色说话
	 * 
	 * @param msg
	 */
	public void say(String msg) {
		msg = "<T t=\"" + msg + "\" ></T>";
		CTransChatMessage2Serv p = new CTransChatMessage2Serv();
		p.message = msg;
		p.messagetype = ChannelType.CHANNEL_CURRENT;
		this.sendProtocol(p);
	}

	public final void sendCommand(final String cmd) {
		sendProtocol(new CSendCommand(cmd));
	}

	public final void sendProtocol(final mkio.Protocol p) {
		// System.out.println("send:" + p.getClass().getName());
		getLoginui().getLoginInstance().send(p);
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public void setFactionId(int factionId) {
		this.factionId = factionId;
	}

	public void setFamilyid(int familyid) {
		this.familyid = familyid;
	}

	public void setFamilyLevel(int familyLevel) {
		this.familyLevel = familyLevel;
	}

	public void setInitump(boolean initump) {
		this.initump = initump;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setLoginui(LoginUI loginui) {
		this.loginui = loginui;
	}

	public final synchronized void setPos(int x, int y) {
		posx = x;
		posy = y;
	}

	public void setPositionInFaction(int positionInFaction) {
		this.positionInFaction = positionInFaction;
	}

	public void setRoleBase(RoleBasicOctets rolebase) {
		this.rolebase = rolebase;
		if (this.rolebase == null)
			return;
		Octets ssoct = this.rolebase.datas.get(RoleBasicOctets.SCENE_STATE);
		if (ssoct != null) {
			try {
				setSceneState(OctetsUtil.toInt(ssoct));
			} catch (MarshalException e) {
				e.printStackTrace();
			}
		}
		school = (this.rolebase.dirandschool & 0x0f) + 10;
	}

	/**
	 * 获得家族职称
	 * 
	 * @return
	 */
	// public int getPositionInFamily() {
	// return positionInFamily;
	// }
	public final synchronized void setScene(long sceneid) {
		this.sceneid = sceneid;
	}

	public void setSceneState(int scenestate) {
		this.scenestate = scenestate;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void addFriend(long roleid) {
		friends.put(roleid, 1);
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}

	public void setTradeState(byte tradeState) {
		this.tradeState = tradeState;
	}

	/**
	 * 开始一个roletask，如果已经有正在执行的task，则不能执行
	 * 
	 * @return
	 */
	public synchronized boolean startRoleTask(Task_RoleBase task) {
		if (this.roletask != null)
			return false;
		this.roletask = task;
		task.start();
		return true;
	}

	/**
	 * 停止roletask
	 * 
	 * @return 返回刚才停止的task，可能为null
	 */
	public synchronized Task_RoleBase stopRoleTask() {
		if (this.roletask == null)
			return null;
		this.roletask.stop();
		Task_RoleBase t = this.roletask;
		this.roletask = null;
		return t;
	}
}
