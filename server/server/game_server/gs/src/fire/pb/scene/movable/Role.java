package fire.pb.scene.movable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gnet.link.Onlines;
import fire.msp.move.MCancelProgressBar;
import fire.msp.move.MGotoAfterBattle;
import fire.msp.move.MRoleGotoNotify;
import fire.msp.move.MUpdateUnitPos;
import fire.pb.instancezone.bingfeng.BingFengLandMgr;
import fire.pb.main.ConfigManager;
import fire.pb.map.BasicPos;
import fire.pb.map.MapConfig;
import fire.pb.move.RoleBasic;
import fire.pb.move.RoleBasicOctets;
import fire.pb.move.SAddPickupScreen;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRelocateRolePos;
import fire.pb.move.SRemovePickupScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.move.SRoleEnterScene;
import fire.pb.move.SRolePlayAction;
import fire.pb.move.SSetRoleLocation;
import fire.pb.move.SSetRoleTeamInfo;
import fire.pb.move.SUpdateRoleSceneState;
import fire.pb.move.SceneBattle;
import fire.pb.move.SceneState;
import fire.pb.move.ShowPetOctets;
import fire.pb.move.TeamInfoOctets;
import fire.pb.scene.BlockInfo;
import fire.pb.scene.MapUtil;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneManager;
import fire.pb.scene.SendProtocolThread;
import fire.pb.scene.SingleStaticScene;
import fire.pb.scene.StaticScene;
import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.talk.MessageMgr;
import fire.pb.team.SUpdateMemberPosition;
import fire.pb.util.FireProp;
import fire.pb.util.Misc;
import fire.pb.util.OctetsUtil;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

/**
 * 场景内的人物角色 主要用与处理与所在地图的关系，如移动，跳转等
 */
public class Role extends MoveUnit {

	private boolean enchouJump;

	Role(long rid, String nm, Scene s, Position p, int lv, int school, int camp, int footLogoId) {
		super(rid, nm, s, p, BasicPos.DIR_DOWN);
		level = lv;
		schoolId = school;
		this.camp = camp;
		this.footLogo = footLogoId;
	}

	public long getRoleID() {
		return getUniqueID();
	}

	/**
	 * 判断玩家所在地是否有其他玩家在摆摊
	 */
	public boolean isOthersStallAtPos() {

		final GridPos roleGrid = this.getPos().toGridPos();
		final int index = getMyScreenPos().getScreenIndex();
		for (Role r : getScene().getSpecialRolesInScreen(index).values()) {
			if (r.getPos().toGridPos().equals(roleGrid) && r.isPrivateStore() && r.getRoleID() != this.getRoleID()) {
				return true;
			}
		}
		return false;
	}

	public boolean isSpecialRole() {
		return isPrivateStore() || checkSceneState(SceneState.GM_FLAG);
	}

	/**
	 * 向角色发送协议
	 */
	public boolean send(mkio.Protocol p2) {
		return Onlines.getInstance().send(getRoleID(), p2);
	}

	public void sendWhoSeeMeAndMe(mkio.Protocol p) {
		java.util.Set<Long> roleids = new java.util.HashSet<Long>();
		if (checkVisible())
			roleids.addAll(getWhoseeme().keySet());
		roleids.add(this.getRoleID());
		SendProtocolThread.getInstance().send(roleids, p);
	}

	/**
	 * 向能看到我的角色发送协议，不包括自己
	 */
	public void sendWhoSeeMe(mkio.Protocol p) {
		if (!checkVisible())
			return;
		SendProtocolThread.getInstance().send(this.getWhoseeme().keySet(), p);
	}

	/**
	 * 向周围的其他角色发送数据
	 */
	public void sendOctetsAroundExceptMe(int ptype, Octets poctets) {
		final java.util.Set<Long> roleIDs = getAroundRoleIDs();
		roleIDs.remove(this.getRoleID());
		Onlines.getInstance().sendOctets(roleIDs, ptype, poctets);
	}

	/**
	 * 将角色公之于众（发给附近九个屏块的所有角色）
	 */
	public void notifyAroundSeeMe() {

		if (null == getScene())
			return;
		if (!checkVisible())
			return;

		final SAddUserScreen snd = new SAddUserScreen();
		snd.rolelist.add(this.getRoleBasic());
		sendAround(snd);
	}

	public void notifyAroundForgetMe() {
		if (null == getScene())
			return;
		if (!checkVisible())
			return;

		final SRemoveUserScreen snd = new SRemoveUserScreen();
		snd.roleids.add(this.getRoleID());
		sendAround(snd);
	}

	/**
	 * 将地图角色数据marshal成Octets
	 */
	public Octets marshal() {
		/************ 角色基本的数据信息 ************/
		final RoleBasicOctets base = new RoleBasicOctets();
		base.roleid = this.getRoleID();
		base.rolename = this.getName();
		byte dirshool = (byte) ((this.getDir() << 4) + (getSchoolId() - 10));
		base.dirandschool = dirshool;
		base.level = this.level;
		base.camp = (byte) this.camp;

		/************* 可选的场景信息 ************/
		// 摊位信息相关：如果在摆摊，则不需要其他信息了
		if (isPrivateStore()) {
			base.datas.put((byte) RoleBasicOctets.STALL_NAME, OctetsUtil.toOctets(stallname));
			if (stallboard > 0)
				base.datas.put((byte) RoleBasicOctets.STALL_BOARD, OctetsUtil.toOctets(stallboard));
			rolebasicoctets = base.marshal(new OctetsStream());
			return rolebasicoctets;
		}

		/************* 不摆摊时能看见的场景信息 ************/
		// 变身造型
		if (this.transformid > 0)
			base.datas.put((byte) RoleBasicOctets.ROLE_ACTUALLY_SHAPE, OctetsUtil.toOctets(this.transformid));
		// npc跟随
		if (this.follownpc > 0)
			base.datas.put((byte) RoleBasicOctets.FOLLOW_NPC, OctetsUtil.toOctets(this.follownpc));
		// 巡游
		if (this.cruiseid > 0)
			base.datas.put((byte) RoleBasicOctets.CRUISE, OctetsUtil.toOctets(this.cruiseid));
		
		// 巡游2
		if (this.cruiseid2 > 0)
			base.datas.put((byte) RoleBasicOctets.CRUISE2, OctetsUtil.toOctets(this.cruiseid2));
		
		// 巡游2
		if (this.cruiseid3 > 0)
			base.datas.put((byte) RoleBasicOctets.CRUISE3, OctetsUtil.toOctets(this.cruiseid3));
		
		base.shape = this.modeid;
		if (this.modeltemplate > 0)
			base.datas.put((byte) RoleBasicOctets.MODEL_TEMPLATE, OctetsUtil.toOctets(this.modeltemplate));
		if (this.hat > 0)
			base.datas.put((byte) RoleBasicOctets.HEADRESS_SHAPE, OctetsUtil.toOctets(this.hat));
		// if(this.weaponColor > 0){
		// base.datas.put((byte)RoleBasicOctets.WEAPON_COLOR,
		// OctetsUtil.toOctets((byte)this.weaponColor));
		// }
		// if(this.baiTangStep > 0){
		// base.datas.put((byte)RoleBasicOctets.CEREMONY_STEP,
		// OctetsUtil.toOctets((byte)this.baiTangStep));
		// }
		base.components.putAll(this.components);
		// 队伍相关
		int teamstate = getTeamState();
		if (SceneTeam.TEAM_NONE != teamstate) {
			final TeamInfoOctets teamoctets = new TeamInfoOctets();
			if (teamstate == SceneTeam.TEAM_LEADER)
				teamstate = SceneTeam.TEAM_NORMAL;
			teamoctets.teamid = getTeamID();
			teamoctets.teamindexstate = (byte) ((teamIndex << 4) + teamstate);
			if (getTeam() != null)
			{
				teamoctets.hugindex = getTeam().getHugIndex(getRoleID());
				teamoctets.normalnum = (byte)getTeam().getAllMemberIDs().size();
			}
			base.datas.put((byte) RoleBasicOctets.TEAM_INFO, OctetsUtil.toOctets(teamoctets));
		}
		// 展示宠物
		if (showpet != null) {
			short colorandsize = (short) (((short) showpet.getColour() << 4) + (byte) showpet.getBodySize());
			ShowPetOctets peoctests = new ShowPetOctets(showpet.getPetID(), showpet.getName(), colorandsize,
					(byte) showpet.getShowEffect(), (byte) showpet.getGrade());
			base.datas.put((byte) RoleBasicOctets.SHOW_PET, OctetsUtil.toOctets(peoctests));
		}

		// 称号
		if (null != showtitle) {
			base.datas.put((byte) RoleBasicOctets.TITLE_ID, OctetsUtil.toOctets(showtitle.titleid));
			base.datas.put((byte) RoleBasicOctets.TITLE_NAME, OctetsUtil.toOctets(showtitle.titlename));
		}

		// 状态相关
		if (scenestate != 0) {
			base.datas.put((byte) RoleBasicOctets.SCENE_STATE, OctetsUtil.toOctets(scenestate));
		}

		// 持续性动作
		if (playingAction > 0) {
			base.datas.put((byte) RoleBasicOctets.PLAYING_ACTION, OctetsUtil.toOctets(playingAction));
		}

		// 称号
		if (0 != footLogo) {
			base.datas.put((byte) RoleBasicOctets.FOOT_LOGO_ID, OctetsUtil.toOctets(footLogo));
		}

		// 装备特效
		if (equipeffect > 0) {
			base.datas.put((byte) RoleBasicOctets.EFFECT_EQUIP, OctetsUtil.toOctets(equipeffect));
		}

		rolebasicoctets = base.marshal(new OctetsStream());
		return rolebasicoctets;
	}

	public RoleBasic getRoleBasic() {

		final RoleBasic rolebasic = new RoleBasic();
		rolebasic.rolebasicoctets = rolebasicoctets;
		rolebasic.pos = this.getPos().toProtocolPos();
		rolebasic.posz = (byte) this.getPos().getZ();
		if (null != getCurDestPos()) {
			rolebasic.poses.add(getCurDestPos().toProtocolPos());
		} else if (isJumping()) {
			for (Position pos : getJumpRole().getJumpPositions())
				rolebasic.poses.add(pos.toProtocolPos());
		}

		return rolebasic;
	}

	public void clearISee() {
		clearIcansee();
	}

	public void enterScene() {
		RoleManager.getInstance().insert(getRoleID(), this);

		if (null == getScene()) {
			Scene.LOG.info("找不到角色所在的场景");
			return;
		}
		marshal();// 第一次marshal
		final SRoleEnterScene snd = new SRoleEnterScene();
		snd.sceneid = getScene().getSceneID();
		snd.destpos = getPos().toProtocolPos();
		snd.destz = (byte) getPos().getZ();
		snd.ownername = getScene().getOwnerName();
		snd.changetype = SRoleEnterScene.ENTER;
		snd.weatherid = (byte) getScene().getWeatherId();
		snd.tipsparm = getScene().getWeatherParm();
		send(snd);

		enterMyScreen();

		final SAddUserScreen tome = new SAddUserScreen();
		tome.rolelist.add(getRoleBasic());
		send(tome);
		getScene().onRoleEnter(this);

		SceneClient.pSend(new MUpdateUnitPos(1, getRoleID(), getScene().getSceneID(), getPos().getX(), getPos().getY(),
				getPos().getZ()));
		Scene.LOG.info(new StringBuilder().append("角色:").append(this.getName())
				.append(",进入游戏").append(",sceneid:").append(getScene().getSceneID()).append(",index:").append(getMyScreenPos().getScreenIndex()).append(",pos:").append(getPos().toString()));
	}

	/**
	 * 人物下线时，地图模块进行下线清理
	 * 
	 * 注意：在此处只可以有地图模块相关的角色操作，不要在此写其他模块的下线处理
	 */
	public void leaveScene() {
		if (null == getScene())
			return;
		leaveMyScreen();
		getScene().onRoleLeave(this);
		// 从角色管理器中删除
		RoleManager.getInstance().removeRoleFromSchool(getRoleID());
		RoleManager.getInstance().removeRoleFromCamp(getRoleID());
		RoleManager.getInstance().remove(getRoleID());
		Scene.LOG.info("角色 " + getName() + "(" + getRoleID() + ") 从场景下线。");
	}

	/**
	 * 人物下线时，地图模块进行下线清理
	 * 
	 * 注意：在此处只可以有地图模块相关的角色操作，不要在此写其他模块的下线处理
	 */
	public void offline() {

		if (null == getScene())
			return;

		// 场景位置坐标更新到GS
		final fire.msp.MRoleOfflineScene snd = new fire.msp.MRoleOfflineScene();
		snd.roleid = getRoleID();
		snd.mapinfo.sceneid = getScene().getSceneID();
		snd.mapinfo.posx = getPos().getX();
		snd.mapinfo.posy = getPos().getY();
		snd.mapinfo.posz = getPos().getZ();
		snd.laststaticmapinfo = lastStaticMapInfo;
		if (getJumpRole() != null)
			snd.qili = getJumpRole().getPower(System.currentTimeMillis());
		SceneClient.pSend(snd);
		// leaveScene();
	}

	/**
	 * 设置展示的宠物 注意，人物上线时，也会用到setShowPet方法， 所以，通知周围玩家的消息，放在此方法外面
	 * 
	 */
	public void setShowPet(final fire.pb.scene.movable.ScenePet showpet) {
		this.showpet = showpet;
		marshal();
	}

	public fire.pb.scene.movable.ScenePet getShowPet() {
		return this.showpet;
	}

	/**
	 * 设置角色婚礼的状态
	 * 
	 * @param step
	 */
	public void setBaitangStep(final int step) {
		this.baiTangStep = step;
		marshal();
	}

	public int getBaitangStep() {
		return this.baiTangStep;
	}

	/**
	 * 设置玩家能看到的最大人数
	 */
	public void setMaxShowNum(final int maxnum) {
		this.maxShowNum = maxnum;
	}

	public int getMaxShowNum() {
		return maxShowNum;
	}

	/**
	 * 获取摊位名字
	 */
	public String getStallName() {
		return this.stallname;
	}

	public void setMyTitleInfo(final fire.msp.showtitleinfo titleinfo) {
		showtitle = titleinfo;
		marshal();
	}

	public int getModeid() {
		return modeid;
	}

	public void setModeid(int modeid) {
		this.modeid = modeid;
		marshal();
	}

	public int getModeltemplate() {
		return modeltemplate;
	}

	public void setModeltemplate(int modeltemplate) {
		this.modeltemplate = modeltemplate;
		marshal();
	}

	public int getHat() {
		return hat;
	}

	public void setHat(int hat) {
		this.hat = hat;
		marshal();
	}

	public int getCruiseid() {
		return cruiseid;
	}

	public int getFollownpc() {
		return follownpc;
	}

	public int getTransformid() {
		return transformid;
	}

	public void setCruiseid(int curiseid) {
		this.cruiseid = curiseid;
		marshal();
	}
	
	public void setCruiseid2(int curiseid) {
		this.cruiseid2 = curiseid;
		marshal();
	}
	
	public void setCruiseid3(int curiseid) {
		this.cruiseid3 = curiseid;
		marshal();
	}

	public void setFollownpc(int follownpc) {
		this.follownpc = follownpc;
		marshal();
	}

	public void setTransformid(int transformid) {
		this.transformid = transformid;
		marshal();
	}

	/**
	 * 角色在传送点上的处理
	 * 
	 * @param channel
	 *            所处的传送格子
	 */
	public boolean onChannel(final GridPos channel) {
		if (null == getScene())
			return false;

		final fire.pb.scene.GotoInfo to = getScene().getGotoInfo(channel);
		if (null == to)
			return false;

		if (!checkEnterLevel(to.getDestMapID()))
			return false;

		// 跳转点时传送一整队（原来是只传送一个，加了跳转容错：强/弱跳转之后，现在要传送整队，不然会引起暂离）
		return justGoto((long) to.getDestMapID(), to.getDestX(), to.getDestY(), BlockInfo.FLOOR_0_Z,
				SRoleEnterScene.CHANNEAL);
	}

	private boolean checkEnterLevel(int mapid) {
		final java.util.Map<Integer, MapConfig> config = fire.pb.main.ConfigManager.getInstance()
				.getConf(fire.pb.map.MapConfig.class);

		final MapConfig desMapConfig = config.get(mapid);
		if (desMapConfig == null)
			return false;

		final int desMapLevel = desMapConfig.getEnterlv();

		if (getLevel() >= desMapLevel)
			return true;

		if (getLevel() >= 10) {
			final java.util.List<String> param = new java.util.ArrayList<String>();
			param.add(String.valueOf(desMapConfig.getMinlevel()));
			MessageMgr.sendMsgNotify(getRoleID(), 141678, param);
			return false;
		}

		MessageMgr.sendMsgNotify(getRoleID(), 141677, null);
		return false;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int lv) {
		level = lv;
		marshal();
	}

	public int getCamp() {
		return camp;
	}

	public void setCamp(int camp) {
		this.camp = camp;
		marshal();
	}

	public fire.msp.RoleMapInfo getLastStaticMapInfo() {
		return lastStaticMapInfo;
	}

	public void setLastStaticMapInfo(final fire.msp.RoleMapInfo rmi) {
		lastStaticMapInfo = rmi;
	}

	public boolean isProgressing() {
		return progressing;
	}

	public void setProgressing(boolean tf) {
		progressing = tf;
	}

	/**
	 * 设置人物在场景上的战斗状态，并广播
	 * 
	 */
	public void setBattleFighterStateAndBroacast(boolean inBattle) {
		if (inBattle)
			addSceneStateWithSP(SceneState.BATTLE_FIGHTER);
		else
			removeSceneStateWithSP(SceneState.BATTLE_FIGHTER);
	}

	/**
	 * 设置人物在场景上的观战状态，并广播
	 * 
	 */
	public void setBattleWatcherStateAndBroacast(boolean inWatchBattle) {
		if (inWatchBattle)
			addSceneStateWithSP(SceneState.BATTLE_WATCHER);
		else
			removeSceneStateWithSP(SceneState.BATTLE_WATCHER);
	}
	
	/**
	 * 设置人物在场景上的观看录像状态，并广播
	 * 
	 */
	public void setBattleReplayStateAndBroacast(boolean inReplayBattle) {
		if (inReplayBattle)
			addSceneStateWithSP(SceneState.BATTLE_REPLAY);
		else
			removeSceneStateWithSP(SceneState.BATTLE_REPLAY);
	}
	/**
	 * 人物是否在摆摊中
	 * 
	 */
	public boolean isPrivateStore() {
		if (stallname == null)
			return false;
		return !stallname.equals("");
	}

	/**
	 * 设置人物在场景上的播放动画状态
	 * 
	 */
	public void setPlayingCGState(boolean playingCG) {
		if (playingCG)
			addSceneStateWithSP(SceneState.PLAY_CG);
		else
			removeSceneStateWithSP(SceneState.PLAY_CG);
	}
	
	/**
	 * 设置某人没支付月卡 by changhao
	 * 
	 */
	public void setNoPayDay(boolean ok) {
		if (ok)
			addSceneStateWithSP(SceneState.NO_PAY_DAY);
		else
			removeSceneStateWithSP(SceneState.NO_PAY_DAY);
	}


	/**
	 * 检测是状态是否可以行走
	 * 
	 * @return true: 可行走 false:不可行走
	 */
	public boolean checkCanMoveState() {
		return 0 == (scenestate & CANT_MOVE_STATE_SUM);
	}

	/**
	 * 检查是否可以允许角色行走
	 */
	public boolean canMove() {
		if (!checkCanMoveState())
			return false;

		if (isPrivateStore())
			return false;

		if (null == team)
			return true;

		if (getTeamIndex() == 1 && !team.canMove()) {
			// 暂时注释掉
			// fire.pb.talk.Message.sendMsgNotify( getRoleID(), 141778, null );
			return false;
		}
		return true;
	}

	/**
	 * 检查是否可以允许角色跳转地图，这里只检查绝对不让跳转的情况
	 * 
	 * @return
	 */
	public boolean canChangeMap() {
		if (!checkCanMoveState())
			return false;

		if (isPrivateStore())
			return false;

		// if (null == team) return true;
		// if(getTeamIndex() == 1 && !team.canMove())
		// {
		// fire.pb.talk.Message.sendMsgNotify( getRoleID(), 141778, null );
		// return false;
		// }
		return true;
	}

	/**
	 * 判断角色是否在某 个/些 场景状态中
	 */
	public boolean checkSceneState(int stat) {
		return 0 != (scenestate & stat);
	}

	/**
	 * 设置场景状态，完全置值，初始化时调用
	 */
	public void setSceneState(int stat) {
		scenestate = stat;
	}

	/**
	 * 添加场景状态,只添加值中的某个/些位
	 */
	public int addSceneStateWithSP(int stat) {
		if (!checkSceneState(stat)) {
			scenestate |= stat;
			sendSceneStateAround();
			marshal();
		}
		return scenestate;
	}

	/**
	 * 删除场景状态,只请除其中的某个/些位
	 */
	public int removeSceneStateWithSP(int stat) {
		if (checkSceneState(stat)) {
			scenestate &= (~stat);
			sendSceneStateAround();
			marshal();
		}
		return scenestate;
	}

	/**
	 * 将自己的新状态广播给自己和周围玩家
	 */
	private void sendSceneStateAround() {
		if (!checkVisible()) {
			send(new SUpdateRoleSceneState(getRoleID(), scenestate));
			return;
		}
		sendWhoSeeMeAndMe(new SUpdateRoleSceneState(getRoleID(), scenestate));
	}

	public void init(fire.msp.showpetinfo showpet) {
		ScenePet mypet = God.createPet(showpet.petkey, showpet.petname, showpet.petid, showpet.color, showpet.bodysize,
				showpet.showeffect);
		if (null == mypet)
			return;

		setShowPet(mypet);
	}

	public void init(fire.msp.modeinfo mymode) {
		this.modeid = mymode.modeid;
		// this.weaponColor = mymode.model.weaponcolor;
		// this.hat = mymode.model.deck;
		// this.modeltemplate = mymode.model.modeltemplate;
		this.transformid = mymode.transformid;
		this.follownpc = mymode.follownpc;
	}

	public void init(fire.msp.showtitleinfo title) {
		this.showtitle = title;
	}

	public void init(Map<Byte, Integer> components) {
		this.components.putAll(components);
	}

	@Override
	public boolean updatePosition(Position p) {
		this.lastseenum = this.getIcansee().size();// 跨屏前纪录上次看到的角色数
		if (!super.updatePosition(p))
			return false;

		// 停止正在做的动作
		stopAction();
		// 通知逻辑
		SceneClient.pSend(new MUpdateUnitPos(1, getRoleID(), getScene().getSceneID(), p.getX(), p.getY(), p.getZ()));
		return true;
	}

	@Override
	public boolean updatePosition(Scene ns, Position np) {
		final Scene os = getScene();
		final Position op = getPos();

		if (!super.updatePosition(ns, np))
			return false;

		os.onRoleLeave(this);
		ns.onRoleEnter(this);

		// 停止正在做的动作
		stopAction();
		// 通知逻辑
		SceneClient.pSend(new MUpdateUnitPos(1, getRoleID(), ns.getSceneID(), np.getX(), np.getY(), np.getZ()));
		// 如果是出副本，记录上一个静态场景
		if (!(os instanceof StaticScene))
			return true;
		if (ns instanceof StaticScene)
			return true;
		if (ns.getMapConfig().getVisibletype() != Scene.VISIBLE_ALL)
			return true;
		lastStaticMapInfo.sceneid = os.getSceneID();
		lastStaticMapInfo.posx = op.getX();
		lastStaticMapInfo.posy = op.getY();
		lastStaticMapInfo.posz = op.getZ();
		return true;
	}

	@Override
	public boolean updatePositionForce(Position p) {
		this.lastseenum = this.getIcansee().size();// 跨屏前纪录上次看到的角色数
		if (!super.updatePositionForce(p))
			return false;

		// 停止正在做的动作
		stopAction();
		// 通知逻辑
		SceneClient.pSend(new MUpdateUnitPos(1, getRoleID(), getScene().getSceneID(), p.getX(), p.getY(), p.getZ()));
		return true;
	}

	public int getTeamIndex() {
		return teamIndex;
	}

	public void setTeamIndex(int v) {
		if (v == teamIndex)
			return;
		teamIndex = v;
		marshal();
	}

	public SceneTeam getTeam() {
		return team;
	}

	public void setTeam(SceneTeam st) {
		team = st;
	}

	public long getTeamID() {
		return null == team ? 0 : team.getTeamID();
	}

	public int getRoleSize() {
		if (null == team)
			return 1;

		return team.size();
	}

	private java.util.Map<Long, Role> getIcanseeNew(final java.util.Set<Integer> indexs) {
		int num = Math.max(0, maxShowNum - this.getIcansee().size());

		// 先看到自己屏块内未看见的人
		Set<Integer> selfindexs = new HashSet<Integer>();
		selfindexs.add(getMyScreenPos().getScreenIndex());
		final java.util.Map<Long, Role> roles = getIcanNotsee(selfindexs, num);

		// 看到新屏块内的人
		num = Math.max(0, num - roles.size());
		roles.putAll(getScene().getRoleInScreensByNum(indexs, num, this));

		// 如果能看到的人骤减，补充以前看不见的人进来
		indexs.addAll(selfindexs);
		fillIcanseeNew(roles, indexs);

		for (Role c : roles.values()) {
			this.addIcansee(c);
			c.addWhoseeme(this);
		}

		return roles;
	}

	private void fillIcanseeNew(final java.util.Map<Long, Role> icanseeNew,
			final java.util.Set<Integer> areadySeeIndexs) {
		int nowseenum = this.getIcansee().size() + icanseeNew.size();
		if (nowseenum < this.maxShowNum && this.lastseenum - nowseenum > this.lastseenum * SEE_ARUOND_PERCENT) {
			Set<Integer> otherscreenids = getAroundScreenIndexs();
			otherscreenids.removeAll(areadySeeIndexs);
			icanseeNew.putAll(getIcanNotsee(otherscreenids, this.maxShowNum - nowseenum));
		}
	}

	private java.util.Set<Long> getWhoseemeNew(java.util.Map<Long, Role> icanseeNew) {
		java.util.Set<Long> set = new java.util.HashSet<Long>();
		for (Role c : icanseeNew.values()) {
			if (c.getIcansee().size() < c.getMaxShowNum() || this.isSpecialRole()
					|| (this.getTeam() != null && c.getTeam() != null && this.getTeam() == c.getTeam())) {
				c.addIcansee(this);
				this.addWhoseeme(c);
				set.add(c.getRoleID());
			}
		}

		return set;
	}

	private java.util.Set<Long> getIneedforget(final java.util.Set<Integer> indexs) {
		final java.util.Set<Long> set = new java.util.HashSet<Long>();
		if (indexs.size() > 5) {
			for (Role c : getIcansee().values()) {
				c.rmWhoseeme(this);
				set.add(c.getRoleID());
			}
			clearIcansee();

			return set;
		}

		for (Role c : getIcansee().values()) {
			if (indexs.contains(c.getMyScreenIndex())) {
				c.rmWhoseeme(this);
				set.add(c.getRoleID());
			}
		}

		// 过滤比同屏最大设置人数更多的人
		int seesize = this.getIcansee().size();
		for (Role r : getIcansee().values()) {
			if (seesize > this.maxShowNum && seesize > 0) {
				if (this.getTeam() == null || r.getTeam() == null || this.getTeam() != r.getTeam()) {
					r.rmWhoseeme(this);
					set.add(r.getRoleID());
					seesize--;
				}
			}
		}

		for (long rid : set)
			this.rmIcanseeByID(rid);

		return set;
	}

	private java.util.Set<Long> getWhofogetme(final java.util.Set<Integer> indexs) {
		final java.util.Set<Long> set = new java.util.HashSet<Long>();
		if (indexs.size() > 5) {
			for (Role c : getWhoseeme().values()) {
				c.rmIcansee(this);
				set.add(c.getRoleID());
			}

			clearWhoseeme();
			return set;
		}

		for (Role c : getWhoseeme().values()) {
			if (indexs.contains(c.getMyScreenIndex())) {
				c.rmIcansee(this);
				set.add(c.getRoleID());
			}
		}

		for (long rid : set)
			this.rmWhoseemeByID(rid);

		return set;
	}

	@Override
	public void notifySeeEachother(int dir) {
		final Scene s = getScene();
		if (null == s)
			return;

		final java.util.Set<Integer> screenIndexs = (-1 == dir) ? getAroundScreenIndexs() : getForwardScreenIndex(dir);

		final java.util.Map<Long, Role> icanseeNew = getIcanseeNew(screenIndexs);
		final java.util.Set<Long> whoseemeNew = getWhoseemeNew(icanseeNew);

		final java.util.Map<Long, NPC> freshNpcs = s.getAllNpcsInScreens(screenIndexs);
		final java.util.Map<Long, Pickup> freshPickups = s.getAllPickupsInScreens(screenIndexs);

		sendSeeEachother(icanseeNew, whoseemeNew, freshNpcs, freshPickups);
	}

	/**
	 * 使看见
	 * 
	 * @param icanseeNew
	 *            自己能新看见的角色
	 * @param whoseemeNew
	 *            别人能新看见我的
	 * @param freshNpcs
	 *            能新看见的npc
	 * @param freshPickups
	 *            能新看见的拾取物
	 */
	private void sendSeeEachother(Map<Long, Role> icanseeNew, Set<Long> whoseemeNew, Map<Long, NPC> freshNpcs,
			Map<Long, Pickup> freshPickups) {
		/**************************** 自己新看见的 *******************************/
		final SAddUserScreen fresh = new SAddUserScreen();
		for (Role r : icanseeNew.values()) {

			if (r.getTeamIndex() > 1)
				continue;

			fresh.rolelist.add(r.getRoleBasic());

			if (r.getTeamIndex() != 1)
				continue;

			final SceneTeam team = r.getTeam();
			if (null == team)
				continue;

			for (Role mem : team.getMembers()) {
				// 正常队员且在同一地图范围内（防止组队跳地图时出现的问题）
				if (mem.getTeamIndex() > 1 && mem.getScene() == getScene())
					fresh.rolelist.add(mem.getRoleBasic());
			}
		}

		// 自己队伍成员如果在icanseeNew中，则必须要看见
		if (this.getTeam() != null) {
			for (Role mem : this.getTeam().getAllTeammates()) {
				if (mem != this && icanseeNew.containsKey(mem.getRoleID()))
					fresh.rolelist.add(mem.getRoleBasic());
			}
		}

		if (freshNpcs != null && !freshNpcs.isEmpty()) {
			for (NPC n : freshNpcs.values()) {
				if (n.checkVisible())
					fresh.npclist.add(n.getNpcBase());
			}
		}
		if (!fresh.rolelist.isEmpty() || !fresh.npclist.isEmpty())
			send(fresh);

		/**************************** 别人刚看见自己 *******************************/
		final SAddUserScreen me = new SAddUserScreen();
		me.rolelist.add(getRoleBasic());
		Onlines.getInstance().send(whoseemeNew, me);

		/**************************** 自己新看见拾取物 *******************************/
		if (freshPickups != null && !freshPickups.isEmpty()) {
			final SAddPickupScreen pick = new SAddPickupScreen();
			for (Pickup p : freshPickups.values())
				pick.pickuplist.add(p.getPickupBasic());
			send(pick);
		}
	}

	/**
	 * 进队伍时让队友和自己能互相看见 防止周围角色太多时，开始互相看不见，进队伍后也看不见对方
	 */
	public void notifySeeTeammates() {
		if (getTeam() == null)
			return;
		final java.util.Set<Integer> screenIndexs = getAroundScreenIndexs();
		final java.util.Set<Long> whoseemeNew = new java.util.HashSet<Long>();
		final SAddUserScreen fresh = new SAddUserScreen();
		List<Role> teammates = getTeam().getAllTeammates();
		for (Role teammate : teammates) {
			if (teammate == this)
				continue;
			if (teammate.getScene() != this.getScene())
				continue;
			if (this.getScene() instanceof SingleStaticScene)
				continue;
			if (!screenIndexs.contains(teammate.getMyScreenIndex()))
				continue;// 不在周围
			if (this.getWhoseeme().containsKey(teammate.getRoleID())
					&& this.getIcansee().containsKey(teammate.getRoleID()))
				continue;// 已经互相看见了，不需要添加

			this.addIcansee(teammate);
			teammate.addWhoseeme(this);
			fresh.rolelist.add(teammate.getRoleBasic());

			teammate.addIcansee(this);
			this.addWhoseeme(teammate);
			whoseemeNew.add(teammate.getRoleID());
		}

		if (!fresh.rolelist.isEmpty())
			send(fresh);
		if (!whoseemeNew.isEmpty()) {
			final SAddUserScreen me = new SAddUserScreen();
			me.rolelist.add(this.getRoleBasic());
			Onlines.getInstance().send(whoseemeNew, me);
		}
	}

	@Override
	public void notifyForgetEachother(int dir, boolean includenpc) {
		final Scene s = getScene();
		if (null == s)
			return;

		final java.util.Set<Integer> screenIndexs = (-1 == dir) ? getAroundScreenIndexs() : getBackScreenIndex(dir);

		final java.util.Set<Long> whoforgetme = getWhofogetme(screenIndexs);
		final java.util.Set<Long> ineedforget = getIneedforget(screenIndexs);

		// send self, remove others
		final SRemoveUserScreen old = new SRemoveUserScreen();
		old.roleids.addAll(ineedforget);
		if (includenpc) {
			final java.util.Map<Long, NPC> oldNpcs = s.getAllNpcsInScreens(screenIndexs);
			for (NPC n : oldNpcs.values())
				old.npcids.add(n.getUniqueID());
		}
		if (!old.roleids.isEmpty() || !old.npcids.isEmpty())
			send(old);
		// send others, remove me
		final SRemoveUserScreen me = new SRemoveUserScreen();
		me.roleids.add(getRoleID());

		Onlines.getInstance().send(whoforgetme, me);
		// send me, remove pickups
		if (includenpc) {
			final java.util.Map<Long, Pickup> oldPickups = s.getAllPickupsInScreens(screenIndexs);
			if (oldPickups.isEmpty())
				return;

			final SRemovePickupScreen pick = new SRemovePickupScreen();
			for (Pickup p : oldPickups.values())
				pick.pickupids.add(p.getUniqueID());

			send(pick);
		}
	}

	/**
	 * 角色移动位置,如果角色是队长,那么移动整支队伍
	 */
	public void move(Position to) {
		if (!isJumping() && getScene().checkChannel(to)) {
			if (onChannel(to.toGridPos())) {
				return;
			}
		}

		updatePosition(to);

		if (null == team)
			return;
		if (team.getCapitan() == this) {
			for (Role mem : team.getMembers()) {
				mem.updatePosition(to);
			}
		}

		notifyLeaverPosition();
	}

	/**
	 * 把角色运到某场景的某位置
	 */
	private void gotoScene(final Scene toScene, final Position pos, int transformType) {
		final Scene oldScene = getScene();
		if (null == oldScene)
			return;

		cancelActionWhenRoleMove();
		// 通知客户端跳转地图
		final SRoleEnterScene change = new SRoleEnterScene(toScene.getOwnerName(), pos.toProtocolPos(),
				(byte) pos.getZ(), transformType, toScene.getSceneID(), (byte) toScene.getWeatherId(),
				toScene.getWeatherParm());

		send(change);
		updatePosition(toScene, pos);

		Scene.LOG.info("跳转场景:角色" + getName() + "(" + getRoleID() + ")进入场景 \t" + toScene.getName() + "场景id\t"
				+ toScene.getSceneID() + "\t坐标\t" + pos.toGridPos());

		// 通知给远离自己的队员们
		notifyLeaverPosition();

		SceneClient
				.pSend(new MRoleGotoNotify(getRoleID(), transformType, oldScene.getSceneID(), getScene().getSceneID()));
		// SceneClient.pSend(new MUpdateUnitPos(1,getRoleID(),
		// getScene().getSceneID(), getPos().getX(), getPos().getY()));
	}

	public boolean justGoto(long sceneid, int x, int y, int z, int gototype) {
		int iSceneid = (int) sceneid;
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(iSceneid);
		if (team != null && cfg != null && cfg.getSafemap() == 0) {
			boolean canGo = true;
			for (Role mr : team.getAllTeammates()) {
				if (mr.getCamp() != camp) {
					canGo = false;
					break;
				}
			}
			if (!canGo) {
				for (long roleid : team.getAllMemberIDs()) {
					MessageMgr.sendMsgNotify(roleid, 145037, null);
				}
				return false;
			}
		}

		final Scene toScene = SceneManager.getInstance().getSceneByID(sceneid);
		if (null == toScene)
			return false;

		GridPos destgpos = new GridPos(x, y, z);

		// 1.根据不同的跳转类型，进行相应的处理
		if (gototype == fire.pb.move.SRoleEnterScene.SYSTEM_DRAG) {
			// 系统拉人要随机地点
			final int space = 4;
			java.util.List<GridPos> maylist = MapUtil.getStandableGridsAround(toScene, destgpos, space);

			if (!maylist.isEmpty()) {
				int resultIndex = Misc.getRateValue(maylist.size() - 1);
				destgpos = maylist.get(resultIndex);
			} else {
				SceneManager.logger.error("角色" + getRoleID() + "系统跳转无随机位置,mapId=" + (int) sceneid + "," + destgpos);
			}
		} else if (gototype == fire.pb.move.SRoleEnterScene.FORCE_GOTO) {
			// 如果是强制跳转，并且玩家处于战斗中，则向逻辑端发消息，暂不跳转
			if (checkSceneState(SceneState.BATTLE_FIGHTER)) {
				SceneClient.pSend(new MGotoAfterBattle(getRoleID(), sceneid, x, y, gototype));
				return true;
			}
		} else if (gototype == fire.pb.move.SRoleEnterScene.CHANNEAL) {
			if (BingFengLandMgr.bingFengMaps.contains((int) sceneid)) {
				if (!enchouJump) {
					return false;
				}
			}
		}
		// 2.在场景验证人物是否能够跳转，只验证不管什么情况下、不管任何类型跳转绝对不能跳的情况
		if (!canChangeMap())
			return false;

		// 3.验证跳转的地点
		Position pos;
		if (this.cruiseid <= 0) {
			pos = toScene.checkAndChangeGotoPos(destgpos.toPosition());
		} else {
			pos = destgpos.toPosition();
		}
		if (pos == null)
			return false;

		// 4.根据不同的队伍状态，进行不同的跳转
		if (0 == teamIndex || -1 == teamIndex) {// 无队伍或者暂离，直接跳
			gotoScene(toScene, pos, gototype);
			return true;
		}

		if (1 != teamIndex) {// 普通队员判断是强跳转还是弱跳转
			if (gototype != fire.pb.move.SRoleEnterScene.FORCE_GOTO) // 如果不是强跳转，有队伍的普通队员肯定不能改变位置
				return false;

			gotoScene(toScene, pos, gototype);
			return true;
		}

		if (gototype != fire.pb.move.SRoleEnterScene.FORCE_GOTO) // 不是强制跳转的话，普通队员跟着队长跳走
		{
			gotoScene(toScene, pos, gototype);
			if (null == team)
				return true;

			for (Role mr : team.getMembers()) {
				mr.gotoScene(toScene, pos, gototype);
			}
			return true;
		}

		gotoScene(toScene, pos, gototype);
		return true;
	}

	/**
	 * 跳转场景的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 */
	public void justGotoRandom(long sceneid, int gototype) {
		final GridPos grid = MapUtil.genPosInMap(sceneid);
		if (null == grid) {
			Scene.LOG.info("找不到可去的地方");
			return;
		}

		justGoto(sceneid, grid.getX(), grid.getY(), grid.getZ(), gototype);
	}

	/**
	 * 跳转场景的一个矩形范围内的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 */
	public void justGotoRandom(long sceneid, int leftTopX, int leftTopY, int lenX, int lenY, int gototype) {

		final java.util.List<GridPos> poses = MapUtil.getRandomArrivablePosList(sceneid, leftTopX, leftTopY, lenX,
				lenY);
		final GridPos grid = MapUtil.getRandomArrivablePosFromList(poses);
		if (null == grid) {
			Scene.LOG.info("找不到可去的地方");
			return;
		}
		justGoto(sceneid, grid.getX(), grid.getY(), grid.getZ(), gototype);
	}

	/**
	 * cancelActionWhenRoleMove:当玩家移动或跳转的时候,有些操作是要被打断的
	 */
	private void cancelActionWhenRoleMove() {
		if (progressing)
			SceneClient.pSend(new MCancelProgressBar(getRoleID()));
		if (isJumping())
			getJumpRole().confirmPositionToStart();
	}

	public int getTeamState() {
		if (-1 == teamIndex)
			return SceneTeam.TEAM_APART;
		else if (0 == teamIndex)
			return SceneTeam.TEAM_NONE;
		else if (1 == teamIndex)
			return SceneTeam.TEAM_LEADER;
		else if (teamIndex > 1)
			return SceneTeam.TEAM_NORMAL;

		return 0;
	}

	// 从队伍中暂离，或者加入队伍时就暂离
	public void leaveFromTeam() {
		if (0 != teamIndex) {// 原来是正常队员
			clearCurDestPos();
			Role captain = getTeam().getCapitan();
			if (captain.isJumping()) {// 队长跳跃中，正常队员暂离，设置此队员的位置为队长起跳位置
				updatePosition(captain.getJumpRole().getStartPos());
			}
			// 向客户端发送调整位置的消息
			final SRelocateRolePos rsnd = new SRelocateRolePos();
			send(rsnd);
			setNeedCheckPos();
		}
		setTeamIndex(-1);
	}

	// 离开自己所在的队伍
	public void quitFromTeam() {
		int oldteamIndex = getTeamIndex();
		Role captain = null;
		if (team != null)
			captain = team.getCapitan();
		team = null;
		setTeamIndex(0);
		clearCurDestPos();
		final SSetRoleTeamInfo snd = new SSetRoleTeamInfo();
		snd.roleid = getRoleID();
		sendWhoSeeMeAndMe(snd);
		quitTeamWhileJump(captain, oldteamIndex);
		if (oldteamIndex > 1) {// 向客户端发送调整位置的消息
			final SRelocateRolePos rsnd = new SRelocateRolePos();
			send(rsnd);
			setNeedCheckPos();
		}
		if (getScene().getMapConfig().visibletype == Scene.VISIBLE_TEAM) {
			notifyForgetEachother(-1, false);
		}
	}

	/**
	 * 广播重置位置
	 */
	public void broadResetPosition() {
		final SSetRoleLocation snd = new SSetRoleLocation();
		snd.roleid = getRoleID();
		snd.position = getPos().toProtocolPos();
		snd.locz = (byte) getPos().getZ();
		sendWhoSeeMeAndMe(snd);
	}

	public void notifyLeaverPosition() {
		if (null == team)
			return;

		final SUpdateMemberPosition snd = new SUpdateMemberPosition();
		snd.roleid = getRoleID();
		snd.sceneid = getScene().getSceneID();
		snd.position.x = getPos().getX();
		snd.position.y = getPos().getY();

		if (1 == teamIndex) {
			Onlines.getInstance().send(team.getLeaverIDs(), snd);
			return;
		}

		if (-1 == teamIndex) {
			Onlines.getInstance().send(team.getAllMemberIDs(), snd);
			Onlines.getInstance().send(team.getLeaverIDs(), snd);
			return;
		}
	}

	public boolean checkMoveUnit() {
		return teamIndex < 2;
	}

	/**
	 * 初始化Role时即初始化jumprole
	 * 
	 * @param jrole
	 */
	public void setJumpRole(JumpRole jrole) {
		this.jumprole = jrole;
	}

	/**
	 * 角色是否在跳跃中
	 * 
	 * @return
	 */
	public boolean isJumping() {
		if (jumprole == null)
			return false;
		return jumprole.isJumping();
	}

	/**
	 * 获取为null时该角色没有轻功
	 */
	public JumpRole getJumpRole() {
		return jumprole;
	}

	/**
	 * 跳跃
	 * 
	 * @param pos
	 * @param first
	 * @return
	 */
	public boolean jump(Position spos, Position pos, boolean first) {
		if (jumprole == null) {
			JumpRole.confirmPosition(this);
			return false;
		}
		if (jumprole.jump(spos, pos, first))
			return true;

		// 没跳成功，重置位置
		jumprole.confirmPositionToStart();
		/*
		 * if(first) jumprole.confirmPosition(getPos());//一段跳重置到当前位置 else
		 * if(jumprole.getStartPos() != null)
		 * jumprole.confirmPositionToStart();//二段跳重置到一段的开始位置
		 */
		return false;
	}

	/**
	 * 停止跳跃
	 */
	public boolean stopJump(Position stoppos) {
		if (jumprole == null)
			return false;
		int distance = getPos().getDistance(jumprole.getStartPos());
		if (!jumprole.stop(stoppos))
			return false;

		// 停止跳跃时处理遇雷
		SceneBattle.process(this, distance, System.currentTimeMillis());

		return true;
	}

	/**
	 * 跳跃时离开
	 * 
	 * @param oldcaptain
	 * @param oldteamindex
	 */
	public void quitTeamWhileJump(Role oldcaptain, int oldteamindex) {
		// 队长离开
		if (oldteamindex == 1) {
			if (isJumping()) // 队长跳跃中离开，一般发生在解散队伍，重置所有人的位置到跳跃开始
				jumprole.confirmPositionToStart();
		}
		// 正常队员离开
		if (oldteamindex > 1) {
			if (oldcaptain == null)
				Scene.LOG.error("正常队员离队时没有队长，角色Id = " + getRoleID());
			else if (oldcaptain.isJumping()) // 队长跳跃中，正常队员离开，设置此队员的位置为队长起跳位置
				updatePosition(oldcaptain.getJumpRole().getStartPos());
		}
	}

	/**
	 * 设置装备特效
	 * 
	 * @param equipeffect
	 */
	public void setEquipEffect(int equipeffect) {
		this.equipeffect = equipeffect;
		marshal();
	}

	/**
	 * 获取装备特效
	 * 
	 * @return
	 */
	public int getEquipEffecdt() {
		return this.equipeffect;
	}

	public void seeSomeone(final Role objrole) {
		SAddUserScreen snd = new SAddUserScreen();
		snd.rolelist.add(objrole.getRoleBasic());
		SendProtocolThread.getInstance().send(this.getRoleID(), snd);
		if (getIcansee().containsKey(objrole.getRoleID()))
			return;
		getIcansee().put(objrole.getRoleID(), objrole);
		objrole.getWhoseeme().put(this.getRoleID(), this);
	}

	/**
	 * @param footLogo
	 *            the footLogo to set
	 */
	public void setFootLogo(int footLogo) {
		this.footLogo = footLogo;
	}

	public void stopAction() {
		if (playingAction == 0)
			return;
		playingAction = 0;
		marshal();
		sendWhoSeeMeAndMe(new SRolePlayAction(getRoleID(), playingAction));
	}

	public String toString() {
		return this.getName() /*
								 * + "\t" + this.getRoleID() + "\t" +
								 * this.getPos().toGridPos()
								 */;
	}
	
	/**
	 * 获取屏幕内所有可见的npc
	 */
	public Map<Long, NPC> getAllNpcsInScreens() {
		final Scene s = getScene();
		if (null == s)
			return null;
		
		final java.util.Set<Integer> screenIndexs = getAroundScreenIndexs();
		final java.util.Map<Long, NPC> freshNpcs = s.getAllNpcsInScreens(screenIndexs);
		
		return freshNpcs;
	}

	private boolean progressing = false; // 为进度条增加一个变量 added by cn
	private fire.msp.RoleMapInfo lastStaticMapInfo; // 上一个非静态场景的信息，在副本被销毁后，优先跳到上一次非静态场景
	private Octets rolebasicoctets; // marshal完毕的要广播的人物数据
	/**
	 * 地图人物属性Begin ！注意：因为地图人物属性是需要广播给周围玩家的，所以这些属性同时以Octets的形式存在，避免频繁的marshal。
	 * 所以如果要添加其他人能看到的地图人物属性，要记住，修改属性的时候，重新marshal rolebasicoctets。
	 */
	private String stallname; // 招牌名称
	private byte stallboard = 1; // 招牌id
	private fire.msp.showtitleinfo showtitle; // 展示的称谓信息
	// 造型信息 Start：将造型信息展开，防止用get获取对象修改绕过marshal过程
	public int modeid;
	public int hat;
	public int modeltemplate;
	public int transformid;
	public int follownpc;
	public int cruiseid;// 巡游id
	public int cruiseid2; //巡游2  by changhao
	public int cruiseid3; //巡游3 by changhao
	// public int weaponColor;
	// 角色的婚礼信息
	private int baiTangStep = 0;

	public Map<Byte, Integer> components = new HashMap<Byte, Integer>();

	// 造型信息 End
	private fire.pb.scene.movable.ScenePet showpet; // 展示的宠物信息
	private int level; // 人物等级

	private int schoolId; // 人物门派
	private int scenestate = 0; // 场景状态，按位保存，参考SceneState
	private int teamIndex = 0; // 队伍位置索引
	private SceneTeam team; // 队伍信息
	private JumpRole jumprole;
	private int camp; // 角色的阵营信息 0=无阵营 1=部落 2=联盟
	private int footLogo;// 足印id

	private int maxShowNum = FireProp.getIntValue("sys", "sys.maxScreenShowNum");
	private int lastseenum = 0; // 移动跨屏前看到的角色数
	private static final int CANT_MOVE_STATE_SUM = SceneState.BATTLE_FIGHTER | SceneState.BATTLE_WATCHER| SceneState.BATTLE_REPLAY
			| SceneState.PLAY_CG| SceneState.NO_PAY_DAY;
	private static final float SEE_ARUOND_PERCENT = 0.2f;
	private byte playingAction = 0;// 正在做的动作
	// private static final int MAX_SHOW_NUM = FireProp.getIntValue("sys",
	// "sys.maxScreenShowNum");;

	private int equipeffect;

	public void setEnchouJump(boolean enchouJump) {

		this.enchouJump = enchouJump;
	}

}