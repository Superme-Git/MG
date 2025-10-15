

package fire.pb.mission.fairyland;

import fire.log.Logger;
import fire.pb.battle.PlayPKManage;
import fire.pb.map.MapConfig;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.map.Transfer;
import fire.pb.mission.FairylandStatus;
import fire.pb.mission.Module;
import fire.pb.mission.SFairylandStatus;
import fire.pb.move.SRoleEnterScene;
import fire.pb.util.MapUtil;
import mkdb.Procedure;

public class PSendFairylandMission extends Procedure {

	private final long roleid;
    private static Logger logger = Logger.getLogger("TASK");
	
	public PSendFairylandMission(long roleId) {
		this.roleid = roleId;
	}

	@Override
	protected boolean process() throws Exception {
//		if (fire.pb.mission.Module.getInstance().hasUnfinishedDreamlandQuest(roleid)>0) {
//			Role role = RoleManager.selectRoleByID(roleid);
//			//如果玩家上线的时候还是在梦境地图中,则发indream,否则发absentdream
//			if (role != null && MapUtil.getBaseMapIdBySceneId(role.getScene()) > 1500) {
//				SDreamlandStatus sds = new SDreamlandStatus(DreamStatus.INDREAM);
//				psendWhileCommit(roleid, sds);
//			} else {
//				SDreamlandStatus sds = new SDreamlandStatus(DreamStatus.ABSENTDREAM);
//				psendWhileCommit(roleid, sds);
//			}
//		}
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (role!=null){
			//如果在梦境里面,则先跳出来,再返回
			xbean.RolePos rolePos = xtable.Roleposes.select(roleid);
			if (FairylandMgr.isInDreamMap(MapUtil.getBaseMapIdBySceneId(role.getScene()))) {
				int mapid = 0;
				int posx = 0;
				int posy = 0;
				if (rolePos!=null){
					mapid = rolePos.getMapid();
					posx = rolePos.getPosx();
					posy = rolePos.getPosy();
				}
				//如果mapid没有(比如从家族副本进入梦境,进入的时候是不记录副本坐标的),则默认回到临安
				if (mapid == 0){
					MapConfig conf = PlayPKManage.getMapConfig(1007);
					mapid = 1007;
					posx = conf.getXjPos();
					posy = conf.getYjPos();
				}

				// 跳转
				if (xtable.Roleid2battleid.select(roleid)==null) 
				Transfer.justGotoWhileCommit(roleid, mapid, posx, posy, SRoleEnterScene.QUEST);
			}
			if (rolePos!=null){ 
				SFairylandStatus sds = new SFairylandStatus();
				sds.status = rolePos.getStatus();
				if (Module.getInstance().hasUnfinishedFairylandMission(roleid)>0) {
					if (!FairylandMgr.isInDreamMap(rolePos.getMapid())){
						sds.status = FairylandStatus.LANDABSENT;
					}
				}
				logger.info("send dreamland status.roleid:"+roleid+"status"+sds.status);		
				psendWhileCommit(roleid, sds);
			}
		}
		return true;
	}

}
