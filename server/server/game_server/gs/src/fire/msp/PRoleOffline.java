package fire.msp;

import mkdb.Procedure;
import xbean.Properties;

/**
 * 角色离线时保存基本数值
 * @author nobody
 *
 */
public class PRoleOffline extends Procedure {
	
	public static final int FACTION_WAR_MAP_ID = 1403;
	
	//private final Role who;
	private long roleid;
	private fire.msp.RoleMapInfo mapinfo;
	private fire.msp.RoleMapInfo laststaticmapinfo;
	public PRoleOffline(long roleid, fire.msp.RoleMapInfo mapinfo, fire.msp.RoleMapInfo laststaticmapinfo, int qili){
		this.roleid = roleid;
		this.mapinfo = mapinfo;
		this.laststaticmapinfo = laststaticmapinfo;
	}
	
	@Override
	public boolean process(){
		final xbean.Properties pro = xtable.Properties.get(roleid);
		if(null == pro)
			return false;
		
		//added by ZhangChong
		//某些特殊的场景下线或者掉线需要在上线时设置置顶的坐标
		if(changePosWhileInSpecialScene(pro)){
			return true;
		}
		
		pro.setSceneid(mapinfo.sceneid);
		pro.setPosx(mapinfo.posx);
		pro.setPosy(mapinfo.posy);
		pro.setPosz(mapinfo.posz);
		pro.setLaststaticsceneid(laststaticmapinfo.sceneid);
		pro.setLaststaticposx(laststaticmapinfo.posx);
		pro.setLaststaticposy(laststaticmapinfo.posy);
		pro.setLaststaticposz(laststaticmapinfo.posz);
		
		return true;
	}

	private boolean changePosWhileInSpecialScene(Properties pro) {
		//如果当前正在帮战战场，那么要在上一次的坐标上线
		int mapid = (int)mapinfo.sceneid;
		
		if(mapid == FACTION_WAR_MAP_ID){
			pro.setSceneid(laststaticmapinfo.sceneid);
			pro.setPosx(laststaticmapinfo.posx);
			pro.setPosy(laststaticmapinfo.posy);
			pro.setPosz(laststaticmapinfo.posz);
			pro.setLaststaticsceneid(laststaticmapinfo.sceneid);
			pro.setLaststaticposx(laststaticmapinfo.posx);
			pro.setLaststaticposy(laststaticmapinfo.posy);
			pro.setLaststaticposz(laststaticmapinfo.posz);
			
			return true;
		}

		return false;
	}
	
}
