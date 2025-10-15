package fire.pb.map;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import fire.pb.item.Equip;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.move.SpriteComponents;
import fire.pb.scene.Scene;
import fire.pb.util.MapUtil;



/**
 * GS模块的地图role，主要记录场景和坐标
 * 
 * @author nobody
 * 
 */
public class Role extends Body {
	
	private final long roleID; // 角色ID
	private AtomicInteger roadLen = new AtomicInteger(); // 角色累计的行走路程
	private AtomicInteger patrolStep = new AtomicInteger(); // 在门派和帮派任务中，角色累计的行走路程
	private AtomicInteger huobanSize = new AtomicInteger(); // 玩家拥有的伙伴数量
	private AtomicLong lastHideBattleTick  = new AtomicLong(); // 上一次进入暗雷战斗的时刻
	private AtomicLong lastEscortBattleTick  = new AtomicLong(); // 上一次进入护送任务战斗的时刻
	Role(long roleId) {
		
		super(roleId, Body.TYPE_ROLE);
		this.roleID = roleId;
	}
	
	public static void getPlayerComponents(final long roleid, final java.util.Map<Byte, Integer> info) {
		Equip equip = new Equip(roleid, true);
		ItemBase bi = equip.getWeapon();
		if (bi != null && bi instanceof EquipItem && ((EquipItem)bi).getEndure() > 0) {
			info.put((byte)SpriteComponents.SPRITE_WEAPON, bi.getItemId());
			info.put((byte)SpriteComponents.SPRITE_WEAPONCOLOR, 
					((EquipItem)bi).getItemAttr().equipcolor);
		} else {
			info.put((byte)SpriteComponents.SPRITE_WEAPON, 0);
			info.put((byte)SpriteComponents.SPRITE_WEAPONCOLOR, 0);
		}
		bi = equip.getHeaddress();
		if (bi != null && bi instanceof EquipItem && ((EquipItem)bi).getEndure() > 0) {
			info.put((byte)SpriteComponents.SPRITE_HEADDRESS, bi.getItemId());
		}
		
		xbean.Properties prop = xtable.Properties.select(roleid);
		int rideitemid = prop.getRide();
		
		int ride = 0;
		fire.pb.npc.SRideItem rideitem = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRideItem.class).get(rideitemid);
		if (rideitem != null)
		{
			fire.pb.npc.SRide sride = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.npc.SRide.class).get(rideitem.getRideid());
			if (sride != null)
			{				
				ride = sride.ridemodel;
			}
		}
		
		info.put((byte)SpriteComponents.SPRITE_HORSEDRESS, ride); //坐骑 by changhao
		
		//添加染色方案
		info.put((byte)SpriteComponents.ROLE_COLOR1, prop.getRolecolor1());
		info.put((byte)SpriteComponents.ROLE_COLOR2, prop.getRolecolor2());
		
		info.put((byte)SpriteComponents.SPRITE_EQUIP_EFFECT, prop.getEquipeffect());
		
		// info.put((byte)SpriteComponents.SPRITE_WEAPON, bi.getItemid());
		
	//	SpriteComponents.SPRITE_BACKDRESS;
	}

	public long getRoleID() {
		return roleID;
	}
	

	public int getRoadLen()
	{
		return roadLen.get();
	}

	public int getPatrolStep()
	{
		return patrolStep.get();
	}

	public long getLastHideBattleTick()
	{
		return lastHideBattleTick.get();
	}

	public long getLastEscortBattleTick()
	{
		return lastEscortBattleTick.get();
	}
	public int getHuobanSize(){
		return huobanSize.get();
	}
	public void setRoadLen(int roadLen)
	{
		this.roadLen.set(roadLen);
	}

	public void setPatrolStep(int patrolStep)
	{
		this.patrolStep.set(patrolStep);
	}

	public void setLastHideBattleTick(long lastHideBattleTick)
	{
		this.lastHideBattleTick.set(lastHideBattleTick);
	}

	public void setLastEscortBattleTick(long lastEscortBattleTick)
	{
		this.lastEscortBattleTick.set(lastEscortBattleTick);
	}
	public void setHuobanSize(int size){
		this.huobanSize.set(size);
	}
	@Override
	public boolean checkDistance(Body o, int distance)
	{
		if(!super.checkDistance(o, distance))
			return false;
		if(o instanceof Role)
		{//对单人场景和组队场景来说，没关系的人始终距离不够
			MapConfig mapcfg = fire.pb.main.ConfigManager.getInstance().getConf(MapConfig.class).get((int)getScene());
			if(mapcfg == null)
				return false;
			if(mapcfg.visibletype == Scene.VISIBLE_SINGLE)
				return false;
			if(mapcfg.visibletype == Scene.VISIBLE_TEAM)
			{
				Long teamid1 = xtable.Roleid2teamid.select(getRoleID());
				Long teamid2 = xtable.Roleid2teamid.select(((Role)o).getRoleID());
				if(teamid1 != null && teamid2 != null)
					return teamid1.equals(teamid2);
				else
					return false;
			}
		}
		return true;
	}
	
	@Override
	public void updateMapInfo(long sceneId, int posx, int posy, int posz)
	{
		long oldscene = this.getScene();
		int oldposx = this.getPos().getX();
		int oldposy = this.getPos().getY();
		super.updateMapInfo(sceneId, posx, posy, posz);
		
		enterRegion((int)oldscene, oldposx, oldposy);
		leaveRegion((int)oldscene, oldposx, oldposy);
		
	}
	
	private void enterRegion(int oldmapID, int oldposx, int oldposy)
	{
		List<Integer> enterRegions = MapUtil.checkEnterRegion((int)oldmapID, oldposx, oldposy, (int)this.getScene(), this.getPos().getX(), this.getPos().getY());
		if(enterRegions.isEmpty())
			return;
		//进入区域触发事件
		for(int regionId : enterRegions)
			new PEnterRegion(roleID, regionId).submit();
	}
	private void leaveRegion(int oldmapID, int oldposx, int oldposy)
	{
		List<Integer> leaveRegions = MapUtil.checkLeaveRegion((int) oldmapID, oldposx, oldposy, (int) this.getScene(), this.getPos().getX(), this.getPos().getY());
		if (leaveRegions.isEmpty())
			return;
		// TODO 离开区域触发事件

		
	}
	
	/**
	 * 检查角色是否在某个区域中
	 * @param regionId
	 * @return
	 */
	public boolean isInRegion(int regionId)
	{
		return MapUtil.isInRegion((int)this.getScene(), this.getPos().getX(), this.getPos().getY(), regionId);
	}
	
	/**
	 * 检查角色是否在某个区域中
	 * @param regionId
	 * @return
	 */
	public boolean isInRegion2(int regionId)
	{
		return MapUtil.isInRegion2((int)this.getScene(), this.getPos().getX(), this.getPos().getY(), regionId);
	}
	
	
	public MapConfig getCurMapConfig()
	{
		return fire.pb.main.ConfigManager.getInstance().getConf(MapConfig.class).get(getMapId());
	}
	
}
