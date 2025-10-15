

package fire.pb.mission;

import fire.pb.main.ConfigManager;
import fire.pb.map.MapConfig;
import fire.pb.map.Transfer;
import fire.pb.mission.instance.InstanceManager;
import fire.pb.mission.instance.PBackInstanceScenceProc;
import fire.pb.move.SRoleEnterScene;


public class ExecuteReqGoto {

	
	/***
	 * 移动到地图某个点 by changhao
	 * @param roleid
	 * @param mapid
	 * @param xpos
	 * @param ypos
	 * @param nocheckconflict 不检查冲突 by changhao
	 * @return
	 */
	public static boolean trans2Pos(long roleid,int mapid,int xpos,int ypos, boolean nocheckconflict){
		final fire.pb.map.Role maprole =
				fire.pb.map.RoleManager.getInstance().getRoleByID(roleid);
			if(null == maprole)
				return false;

			MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(mapid);
			if(cfg == null){
				return false;
			}
			
			//如果是静态地图
			if(cfg.dynamic == 0){
				//非安全地图
				if(cfg.getSafemap() == 0){
					new PReqGotoProc(roleid, mapid, xpos, ypos).submit();
					return true;
				}
				
				int conflictstate = SRoleEnterScene.QUEST;
				if (nocheckconflict)
				{
					conflictstate = 0;
				}
				
				Transfer.justGoto(roleid, mapid, xpos, ypos, conflictstate);
				return true;
			}
			
			//副本处理
			if (mapid == (int) maprole.getScene()) {// 已经在副本里了直接跳过去
				
				int conflictstate = SRoleEnterScene.QUEST;
				if (nocheckconflict)
				{
					conflictstate = 0;
				}
				
				Transfer.justGoto(roleid, maprole.getScene(), xpos, ypos,
						conflictstate);
	
			} else if (InstanceManager.getInstance().isInstanceCopy(mapid)) {
				if (mkdb.Transaction.current() == null) {
					new PBackInstanceScenceProc(roleid).submit();
				}
				else {
					new PBackInstanceScenceProc(roleid).call();
				}

			} else {// 出副本跳转点
				int reMapId = cfg.getRelocatemapid();
				int posx = cfg.getRelocatex();
				int posy = cfg.getRelocatey();
				if (reMapId != 0 && posx != 0 && posy != 0) {
					
					int conflictstate = SRoleEnterScene.QUEST;
					if (nocheckconflict)
					{
						conflictstate = 0;
					}
					Transfer.justGoto(roleid, reMapId, posx, posy,
							conflictstate);
				}
			}
			return true;
	}
}

