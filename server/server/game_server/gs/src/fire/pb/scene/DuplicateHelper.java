package fire.pb.scene;

import com.locojoy.base.Octets;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

public class DuplicateHelper {

	/**
	 * 检测指定的副本场景是否存在
	 * 
	 * @param baseMapID
	 *            副本地图ID
	 * @param ownerID
	 *            副本所属者ID
	 * @return 是/否 存在
	 */
	public static boolean checkDynamicScene(int baseMapID, long ownerID) {
		return SceneManager.getInstance().getSceneByMapAndOwner(baseMapID, ownerID)!= null;
	}

	/**
	 * 检测角色是否在副本场景中
	 * 
	 * @param role
	 * @return
	 */
	public static boolean checkRoleInDynamicScene(long roleid) {
		final Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(null == role) return false;
		
		final Scene rs = role.getScene();
		if(null == rs) return false;
		
		return rs instanceof DynamicScene;
	}

	/**
	 * 检测角色是否在指定的副本场景中
	 * 
	 * @param role
	 * @param baseMapID
	 *            副本地图ID
	 * @return
	 */
	public static boolean checkRoleInDynamicScene(long roleid, int baseMapID) {
		
		final Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(null == role) return false;
		
		final Scene rs = role.getScene();
		if(null == rs) return false;
		
		if (rs instanceof DynamicScene)
			return rs.getMapID() == baseMapID;

		return false;
	}

	
	/**
	 * 获取副本场景
	 * @param baseMapID		副本场景地图ID
	 * @param ownerID		副本的所属者ID
	 * @return
	 */
	public static Scene getDynamicScene(int baseMapID, long ownerID){
		Scene my = SceneManager.getInstance().getSceneByMapAndOwner(baseMapID, ownerID);
		return my;
	}
	
	/**
	 * 创建新的副本场景
	 * @param sceneId       场景ID，没有时填-1，场景ID改为在外面生成，因为可能由Gs那边传来
	 * @param baseMapID		副本的地图ID	
	 * @param ownerID		副本的所属者ID
	 * @param ownerName		副本的
	 * @param reEnter		离开后是否可重新进入
	 * @param scenetype		场景类型
	 * @return
	 */
	public static Scene createNewDynamicScene(int baseMapID, long ownerID, String ownerName, int livetime,int scenetype,Octets parameters){
		if(null != getDynamicScene(baseMapID, ownerID)){
			return null;
		}
		return SceneFactory.createDynamicScene(baseMapID, ownerID, ownerName, livetime,scenetype,parameters);
	}
	
	
	/**
	 * 角色进入副本, 如果副本不存在,那么就创建
	 * 
	 * @param sceneId       
	 * 			     场景ID，没有时填-1，场景ID改为在外面生成，因为可能由Gs那边传来
	 * @param role
	 *            角色对象
	 * @param baseMapID
	 *            副本的地图ID
	 * @param grid
	 *            进入坐标(格子)点
	 * @param ownerID
	 *            副本的所有者ID(可能是角色ID,也可能是团体ID)
	 * @param reusable
	 *            副本对象是否可重用(角色都离开后是否可重进入,还是重新创建新的对象)
	 */
	public static Scene enterDynamicScene(long roleid, int baseMapID,
			int x, int y, long ownerID, String ownerName,int livetime, int scenetype,int gototype,Octets parameters) {
		final Role role = RoleManager.getInstance().getRoleByID(roleid);
		if(null == role) return null;
		
		Scene myScene = SceneManager.getInstance().getSceneByMapAndOwner(baseMapID, ownerID);

		if (null == myScene){
			myScene = SceneFactory.createDynamicScene(baseMapID, ownerID, ownerName,livetime,scenetype,parameters);
			
		}
		
		if (null == myScene)
		{
			return null;
		}
		
		role.justGoto(myScene.getSceneID(), x, y, BlockInfo.FLOOR_0_Z, gototype);
		
//		//如果是龙舟地图需要特殊处理
//		if(scenetype == DynamicSceneType.DRAGON_BOAT_SCENE){
//			SceneClient.pSend(new MAfterEnterDragonScene(roleid, ownerID, baseMapID));
//		}
		return myScene;
	}
	
	/**
	 * 根据场景id进入场景内,场景必须是存在的
	 * @param roleid
	 * @param x
	 * @param y
	 * @param sceneid
	 * @param scenetype
	 * @param gototype
	 * @return
	 */
	public static Scene enterDynamicSceneById(final long roleid, final int x, final int y, final long sceneid, final int scenetype, final int gototype) {
		final Role role = RoleManager.getInstance().getRoleByID(roleid);
		if (null == role)
			return null;

		Scene myScene = SceneManager.getInstance().getSceneByID(sceneid);
		if (myScene == null)
			return null;
		
		role.justGoto(myScene.getSceneID(), x, y, BlockInfo.FLOOR_0_Z, gototype);
		return myScene;
	}
	

	/**
	 * 梦境用的跳转梦境场景
	 * @param roleid
	 * @param baseMapID
	 * @param x
	 * @param y
	 * @param ownerID
	 * @param ownerName
	 * @param reusable
	 * @param team
	 * @param playCG
	 * @return
	 */
/*	public static boolean enterDynamicMap(long roleid, int baseMapID,
			int x, int y, long ownerID, String ownerName, boolean reusable,boolean team,boolean playCG,int scenetype) {
		Scene myScene = null;
		for (Scene s : SceneManager.getInstance().getAllScenes().values()) {
			if (s.getMapID() == baseMapID && ownerID == s.getOwnerID())
				myScene = s;
		}

		if (null == myScene)
			myScene = SceneFactory.createDynamicScene(baseMapID, ownerID, ownerName, reusable, scenetype);
	//	Transfer.justGoto(roleid, myScene.getSceneID(), x, y, team);
		if ( playCG ) {
			fire.msp.move.GTaskGoto send = new fire.msp.move.GTaskGoto();
			send.roleid = roleid;
			send.sceneid = myScene.getSceneID();
			send.xpos = x;
			send.ypos = y;
			fire.pb.GsClient.pSendWhileCommit( send );
		} else {
			fire.msp.move.GGoto send = new fire.msp.move.GGoto();
			send.roleid = roleid;
			send.delta = 10;
			send.sceneid = myScene.getSceneID();
			send.xpos = x;
			send.ypos = y;
			fire.pb.GsClient.pSendWhileCommit( send );
		}

		return true;
	}*/
	
	/**
	 * 通过地图的sceneid销毁一个副本地图
	 * 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param sceneId
	 * 				副本id
	 */
	public static void destroyDynamicSceneById(long sceneId){
		final Scene s = SceneManager.getInstance().getSceneByID(sceneId);
		if(s == null)
			return;
		s.unload();
	}
	
	/**
	 * 通过地图的base id 和 ownerid 销毁一个副本地图
	 * 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param baseMapID
	 * 				副本地图id
	 * @param ownerID
	 * 				副本的所有者ID(可能是角色ID,也可能是团体ID)
	 */
	public static void destroyDynamicScene(int baseMapID, long ownerID){
		final DynamicScene s = SceneManager.getInstance().getSceneByMapAndOwner(baseMapID, ownerID);
		if(s == null)
			return;
		s.unload();
	}
}
