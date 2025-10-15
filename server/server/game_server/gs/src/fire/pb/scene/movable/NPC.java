package fire.pb.scene.movable;


import java.util.HashMap;

import gnet.link.Onlines;
import fire.msp.move.MUpdateUnitPos;
import fire.pb.move.NpcBasic;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.move.SUpdateNpcSceneState;
import fire.pb.move.SceneState;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SendProtocolThread;
import fire.pb.scene.config.TemplateNpc;
import fire.pb.scene.manager.TemplateNpcManager;
import fire.pb.scene.sPos.Position;

/**
 * NPC
 * @author nobody
 *
 */
public class NPC extends Body {

	NPC(long npckey, int npcid, String name, Scene scene, Position pos, int dir){
		super(npckey, name, scene, pos, dir);
		this.npcid = npcid;
		this.shape = 0;
		TemplateNpc tnpc = TemplateNpcManager.getInstance().getTemplateNpc(npcid);
		if(tnpc == null) {
			throw new IllegalArgumentException("没有TemplateNpc，NPCID = " + npcid);
		}
		
		setVisible(tnpc.checkVisible());
		miniMapShow = tnpc.checkMiniMapVisible();
	}
	
	public NpcBasic getNpcBase(){
		final NpcBasic npcBase = new NpcBasic();
		npcBase.npckey = getUniqueID();
		npcBase.id = getNpcID();
		npcBase.name = getName();
		npcBase.pos = getPos().toProtocolPos();
		npcBase.posz = (byte)getPos().getZ();
		npcBase.speed = getSpeed();
		npcBase.scenestate = scenestate;
		if(null !=  this.getCurDestPos())
		{
			npcBase.destpos = this.getCurDestPos().toProtocolPos();
		}
		npcBase.dir = getDir();
		npcBase.shape = getShape();
		npcBase.components = this.components;
		return npcBase;
	}
	
	public long getNpcKey(){
		return getUniqueID();
	}
	
	public int getNpcID(){
		return npcid;
	}
	
	public void setNpcID(int npcid){
		this.npcid = npcid;
	}
	
	public void setShape(int shape){
		this.shape = shape;
	}
	
	public int getShape(){
		return shape;
	}
	
    public int getInBattle(){
    	return battleState;
    }

    /**
     * 设置npc的战斗状态
     * 注意，此方法只能由逻辑过来的同步消息调用修改，地图模块自己的逻辑不能修改，只能读取
     * @param battleState
     */
	public void setInBattle(int battleState) {
		this.battleState = battleState;		
	}
	
	/**
	 * 设置npc在场景上的战斗状态，并广播
	 * 
	 */
	public void setBattleFighterStateAndBroacast(boolean inBattle) {
		if (inBattle)
			addSceneStateWithSP(SceneState.BATTLE_FIGHTER);
		else
			removeSceneStateWithSP(SceneState.BATTLE_FIGHTER);
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
		}
		return scenestate;
	}
	
	/**
	 * 将自己的新状态广播给自己和周围玩家
	 */
	private void sendSceneStateAround() {
		if (!checkVisible()) {
			send(new SUpdateNpcSceneState(getUniqueID(), scenestate));
			return;
		}
		sendWhoSeeMe(new SUpdateNpcSceneState(getUniqueID(), scenestate));
	}
	
	/**
	 * 向角色发送协议
	 */
	public boolean send(mkio.Protocol p2) {
		return Onlines.getInstance().send(getUniqueID(), p2);
	}
	
	public void updateVisible(boolean v)
	{
		if(null == getScene()) return;
		
		if(checkVisible() == v) return;		// 不需要变更
			
		setVisible(v);						// 新的状态（显示/隐藏）
		if(v)
		{
			final SAddUserScreen snd = new SAddUserScreen();
			snd.npclist.add(getNpcBase());
			sendAround(snd);
			return;
		}
		
		final SRemoveUserScreen snd = new SRemoveUserScreen();
		snd.npcids.add(getNpcKey());
		sendAround(snd);
	}
	
	public boolean isMiniMapShow()
	{
		return miniMapShow;
	}

	public void notifySeeEachother(int dir)
	{
		final java.util.Set<Integer> screenIndexs = (-1 == dir) ? getAroundScreenIndexs()
				: getForwardScreenIndex(dir);
		
		final java.util.Set<Long> roleids = getScene().getRoleInScreensByNum(screenIndexs,Integer.MAX_VALUE,null).keySet();
		
		if(roleids.isEmpty())
			return;
		
		final SAddUserScreen snd = new SAddUserScreen();
		snd.npclist.add(getNpcBase());
		Onlines.getInstance().send(roleids, snd);
	}

	@Override
	public void notifyForgetEachother(int dir, boolean includenpc)
	{
		final java.util.Set<Integer> screenIndexs = (-1 == dir) ? getAroundScreenIndexs()
				: getBackScreenIndex(dir);
		
		final java.util.Set<Long> roleids = getScene().getRoleInScreensByNum(screenIndexs,Integer.MAX_VALUE,null).keySet();
		
		if(roleids.isEmpty())
			return;
		
		final SRemoveUserScreen snd = new SRemoveUserScreen();
		snd.npcids.add(getUniqueID());
		Onlines.getInstance().send(roleids, snd);
	}

	/**
	 * 向能看到我的角色发送协议
	 */
	public void sendWhoSeeMe(mkio.Protocol p) {
		final java.util.Set<Integer> screenIndexs = getAroundScreenIndexs();
		
		final java.util.Set<Long> roleids = getScene().getRoleInScreensByNum(screenIndexs,Integer.MAX_VALUE,null).keySet();
		
		if (roleids.isEmpty())
			return;
		if (checkVisible())		
			SendProtocolThread.getInstance().send(roleids, p);
	}
	
	@Override
	public boolean updatePosition(Position p)
	{
		if (!super.updatePosition(p))
			return false;
		//通知逻辑
		SceneClient.pSend(new MUpdateUnitPos(2,getNpcKey(), getScene().getSceneID(), p.getX(), p.getY(), p.getZ()));
		return true;
	}

	public java.util.Set<Long> getRelatives()
	{
		return this.relatives;
	}
	
	public void setRelative(final java.util.Set<Long> rel)
	{
		this.relatives = rel;
	}
	
	private int npcid;
	private int shape;				//npc的造型 值
	private int battleState;
	private boolean miniMapShow;
	private int scenestate;				//npc的场景状态值
	private java.util.Set<Long> relatives = null;
	public java.util.HashMap<Byte,Integer> components = new HashMap<Byte, Integer>();
	
	public static final int NOT_INBATTLE = 0;
	public static final int INBATTLE = 1;
	public static final int INBATTLE_AND_TIMEOUT = 2;
}
