package fire.pb.map;

import fire.log.Logger;
import fire.msp.move.GGoto;
import fire.msp.move.GGotoRandom;
import fire.msp.move.GGotoRandomInARegion;
import fire.pb.GsClient;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.util.CGPlayUtil;
import mkdb.Transaction;


/**
 * 传送器，用于GS逻辑模块使用
 * 
 * @author nobody
 *
 */
public final class Transfer {
	public static Logger logger = Logger.getLogger("MAPMAIN");
	
	public static int checkCanChangeMap(long roleId, int gototype){
		BuffAgent bagent = new BuffRoleImpl(roleId,true);
		int conflictId = bagent.checkBuffClash(gototype);
		if(conflictId > 0)
			return conflictId;
		return 0;
	}
	
	public static void sendMsgWhileCanntGoto(long roleId, int conflictId)
	{
		switch(conflictId)
		{
		case BuffConstant.StateType.STATE_TEAM_MEMBER_NORMAL:
			MessageMgr.sendMsgNotify(roleId, 141216, null);
			return;
		case BuffConstant.StateType.STATE_BATTLE_FIGHTER:
			MessageMgr.sendMsgNotify(roleId, 141214, null);
			return;
		case BuffConstant.StateType.STATE_BATTLE_WATCHER:
			return;
		case BuffConstant.StateType.STATE_REPLAY:
			return;
		case BuffConstant.CONTINUAL_PLAY_CG:
			return;
		case BuffConstant.StateType.STATE_PRIVATE_STORE:
			MessageMgr.sendMsgNotify(roleId, 141219, null);
			return;
		case BuffConstant.StateType.STATE_TRANSPORT:
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 140752, null);
			return;
		case BuffConstant.CONTINUAL_KE_JU:
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, fire.pb.util.FireProp.getIntValue("message", "mtgserv.buffConflict"), null);
			return;
		}
	}
	
	/**
	 * 当逻辑功能要使用justGoTo跳转地图时，判断能否跳转，例如使用飞行旗,飞行符,回门派等技能
	 * mapId 如果mapid = 0的时候不检测阵营
	 */
	public static boolean canChangeMap(long roleId,int gototype, int mapId){

		int conflictId = checkCanChangeMap(roleId, gototype);
		if(conflictId == 0)
		{

			Team team = TeamManager.selectTeamByRoleId(roleId);
			if(!checkJumpMap( roleId, team, mapId)){
				return false;
			} 

			if(Transaction.current()!=null)
				new PPreGotoProcess(roleId, gototype).call();
			else{
				try{// 注意，这里必须等待处理完毕后才能继续跳
					new PPreGotoProcess(roleId, gototype).submit().get();
				} catch (Exception e){
					logger.error("跳转前处理出错,roleId=" + roleId + ", gototype=" + gototype,e);
				}
			}
			return true;
		}

		sendMsgWhileCanntGoto(roleId, conflictId);
		return false;
	}

	public static boolean checkJumpMap(long roleId, Team team, int mapId){
		
		if(mapId == 0){
			return true;
		}
		
		MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(mapId);
		if(team == null || cfg == null || cfg.getSafemap() == 1 ){
			return true;
		}
		
		
		//检查队伍中有没有不同真用的
		boolean canJump = true;
//		for(long rid : team.getAllMemberIds()){
//			PropRole temp = new PropRole(rid, true);
//		}
		
		if(!canJump){
			if (team.isTeamLeader(roleId)){
				for(long id : team.getNormalMemberIds()){
					MessageMgr.psendMsgNotify(id,145037, null);
					fire.pb.talk.MessageMgr.sendMsgNotify(id,145037, null);
				}
			}else{
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId,145037, null);
			}
		}
		return canJump;
	}
	
   
   /********************* 以下为整合后新的方法，上面的会被删除********************************/
   
   
   
   /**
	 * 跳转场景, 到达目的地后不再继续寻路, transform类方法都继续寻路
	 * @param roleid	角色(队长角色)ID
	 * @param sceneid	目标场景ID
	 * @param x			地图上的坐标位置（GridPos）	x
	 * @param y										y
	 * @param gototype		跳转类型
	 */
	public static boolean justGoto(long roleid, long sceneid, int x, int y, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid, MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.sendToScene(new GGoto(roleid, sceneid, x, y, 0, gototype ));
		return true;
	}
	
	/**
	 * 跳转场景, 到达目的地后不再继续寻路, transform类方法都继续寻路
	 * @param roleid	角色(队长角色)ID
	 * @param sceneid	目标场景ID
	 * @param x			地图上的坐标位置（GridPos）	x
	 * @param y										y
	 * @param gototype		跳转类型
	 */
	public static boolean justGotoWhileCommit(long roleid, long sceneid, int x, int y, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid, MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.pSendWhileCommit(new GGoto(roleid, sceneid, x, y, 0, gototype));
		return true;
	}
	
	 /**
	 * 跳转场景,delta为随机误差, 到达目的地后不再继续寻路, transform类方法都继续寻路
	 * @param roleid	角色(队长角色)ID
	 * @param sceneid	目标场景ID
	 * @param x			地图上的坐标位置（GridPos）	x
	 * @param y										y
	 * @param gototype		跳转类型
	 */
	public static boolean justGotoDelta(long roleid, long sceneid, int x, int y, int gototype,int delta){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid, MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.sendToScene(new GGoto(roleid, sceneid, x, y, delta, gototype ));
		return true;
	}
	
	/**
	 * 跳转场景, delta为随机误差，到达目的地后不再继续寻路, transform类方法都继续寻路
	 * @param roleid	角色(队长角色)ID
	 * @param sceneid	目标场景ID
	 * @param x			地图上的坐标位置（GridPos）	x
	 * @param y										y
	 * @param delta     偏移的像素
	 * @param gototype		跳转类型
	 */
	public static boolean justGotoDeltaWhileCommit(long roleid, long sceneid, int x, int y, int gototype,int delta){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid, MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.pSendWhileCommit(new GGoto(roleid, sceneid, x, y, delta, gototype));
		return true;
	}
	
	/**
	 * 跳转场景的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 * @param roleid		角色ID
	 * @param sceneid		场景ID
	 * @param gototype		跳转类型
	 */
	public static boolean justGotoRandom(long roleid, int sceneid, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid,MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.sendToScene(new GGotoRandom(roleid, sceneid,gototype));
		return true;
	}
	/**
	 * 跳转场景的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 * @param roleid		角色ID
	 * @param sceneid		场景ID
	 * @param gototype		跳转类型
	 */
	public static boolean justGotoRandomWhileCommit(long roleid, int sceneid, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid,MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.pSendWhileCommit(new GGotoRandom(roleid, sceneid,gototype));
		return true;
	}
	
	/**
	 * 跳转场景一个矩形区域内的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 * @param roleid		角色ID
	 * @param sceneid		场景ID
	 * @param leftTopx  	区域左上定点
	 * @param leftTopy		区域左上定点
	 * @param lenX			区域宽
	 * @param lenY			区域高
	 * @param gototype		跳转类型
	 * @return
	 */
	public static boolean justGotoRandom(long roleid, int sceneid,int leftTopx,int leftTopy,int lenX,int lenY, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid,MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.sendToScene(new GGotoRandomInARegion(roleid, sceneid,leftTopx,leftTopy,lenX,lenY,gototype));
		return true;
	}
	/**
	 * 跳转场景一个矩形区域内的随机位置,如果roleid是队长的角色ID,那么传送整支队伍
	 * @param roleid		角色ID
	 * @param sceneid		场景ID
	 * @param leftTopx  	区域左上定点
	 * @param leftTopy		区域左上定点
	 * @param lenX			区域宽
	 * @param lenY			区域高
	 * @param gototype		跳转类型
	 * @return
	 */
	public static boolean justGotoRandomWhileCommit(long roleid, int sceneid,int leftTopx,int leftTopy,int lenX,int lenY, int gototype){
		if(!canChangeMap(roleid,gototype, (int)sceneid))
			return false;
		CGPlayUtil.removeTeamMemberCGBuff(roleid);
		//DreamLandManager.gotoFromDreamland(roleid,MapUtil.getBaseMapIdBySceneId(sceneid));
		GsClient.pSendWhileCommit(new GGotoRandomInARegion(roleid, sceneid,leftTopx,leftTopy,lenX,lenY,gototype));
		return true;
	}
   
}
