package fire.pb.redirect;

import mkdb.Procedure;
import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp1.PvP1Helper;
import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.instance.InstanceManager;
import fire.pb.mission.instance.PLeaveInstanceMapProc;
import fire.pb.move.SRoleEnterScene;

/**
 * 推出副本
 * @author nobody
 */
public class PExitCopyProc extends Procedure {
	
	private final long roleId;
	private final int gotoType;
	
	public PExitCopyProc(final long roleId, final int gotoType) {
		this.roleId = roleId;
		this.gotoType = gotoType;
	}

	@Override
	protected boolean process() throws Exception {
		
		Role mapRole = RoleManager.getInstance().getRoleByID(roleId);
		if(mapRole == null){
			return false;
		}
		int mapId = mapRole.getMapId();
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(mapId);
		if(cfg == null){
			return false;
		}
		
		//不是特殊处理的副本地图
		if(!isSpecil(mapId, gotoType) && cfg.getDynamic() ==1){
			
			//非特殊的副本  传送到副本的出口
			int gomapId = cfg.getRemap();
			int gposx = cfg.getRexjPos();
			int gposy = cfg.getReyjPos();
			
			if(gomapId > 0){
				Transfer.justGotoWhileCommit(roleId, gomapId, gposx, gposy, SRoleEnterScene.CHEFU);
			}
		}
		
		return true;
	}
	
	
	/**
	 * 离开特殊的 地图时需要特殊处理
	 * @param mapId
	 * @return
	 */
	private boolean isSpecil(int mapId, int gotoType) {

		// 出PVP地图
		if (PvP1Helper.getRaceGradeByMapId(mapId) != -1) {
			PvP1Control.getInstance().forceLeave(roleId);
			return true;
		}

		// 出组队副本地图
		if (InstanceManager.getInstance().isInstanceCopy(mapId)) {
			new PLeaveInstanceMapProc(roleId, gotoType).call();
			return true;
		}

		if (fire.pb.mission.instance.line.LineInstManager.getInstance().isInstanceCopy(mapId)) {
			new fire.pb.mission.instance.line.PLeaveLineMapProc(roleId, gotoType).call();
			return true;
		}

		return false;
	}
}
