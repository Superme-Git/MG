package fire.pb.map;

import com.locojoy.base.Octets;

import fire.msp.move.GCreateDynamicScene;
import fire.msp.move.GDestroyDynamicSceneById;
import fire.msp.move.GDestroyDynamicSceneByMapOwner;
import fire.msp.move.GEnterDynamicScene;
import fire.msp.move.GEnterDynamicSceneById;
import fire.pb.GsClient;
import mkdb.Transaction;

public class DuplicateHelper {

	/**
	 * 新建一个动态地图，该地图已经存在则返回
	 * 
	 * @param mapId
	 *            地图id
	 * @param ownerId
	 *            地图的所有者ID(roleid or teamid)
	 * @param ownerName
	 * @param reuse
	 *            动态地图是否可重用(角色都离开后是否销毁)
	 * @param lastTime
	 *            空场景存活时间,单位秒.可重用动态地图时,等于0则不销毁;不可重用的副本此字段无效
	 * @param mapType
	 *            场景类型
	 * @param param
	 *            创建动态地图用到的参数,默认为DynamicSceneParams类型,不同的动态地图用到的参数不同,用octets类型实现多样化
	 */
	public static void createDynamicMap(int mapId, long ownerId,
			String ownerName, boolean reuse, int lastTime, int mapType,
			Octets param) {
		GCreateDynamicScene gCreateMap = new GCreateDynamicScene(mapId,
				ownerId, ownerName, (byte) (reuse ? 1 : 0), lastTime,
				mapType, param);
		GsClient.sendToScene(gCreateMap);
	}

	/**
	 * 角色进入动态地图, 如果动态地图不存在 创建
	 * 
	 * @param role
	 *            角色id
	 * @param sceneId
	 *            动态地图唯一id,没有时填-1,地图模块自动生成.
	 * @param mapId
	 *            地图ID
	 * @param grid
	 *            进入坐标(格子)点
	 * @param ownerID
	 *            动态地图的所有者ID(roleId or teamId)
	 * @param reuse
	 *            动态地图是否可重用(角色都离开后是否销毁)
	 * @param sceneType
	 *            场景类型
	 */
	public static boolean enterDynamicMap(long roleId, int mapId, int x,
			int y, long ownerID, String ownerName, boolean reuse,
			int sceneType, int gototype) {
		if (!Transfer.canChangeMap(roleId, gototype, mapId))
			return false;
		byte breuse = 0;
		int livetime = 0;
		if (reuse) {
			breuse = 1;
			livetime = -1;
		}
		GsClient.sendToScene(new GEnterDynamicScene(roleId, mapId, ownerID,
				ownerName, x, y, breuse, livetime, sceneType, gototype, null));
		return true;
	}

	/**
	 * 角色进入副本, 如果副本不存在,那么就创建
	 * 
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
	public static boolean enterDynamicSceneWhileCommit(long roleid,
			int baseMapID, int x, int y, long ownerID, String ownerName,
			boolean reusable, int scenetype, int gototype) {
		if (!Transfer.canChangeMap(roleid, gototype, baseMapID))
			return false;
		byte reuse = 0;
		int livetime = 0;
		if (reusable) {
			reuse = 1;
			livetime = -1;
		}
		if (Transaction.current() != null)
			GsClient.pSendWhileCommit(new GEnterDynamicScene(roleid, baseMapID,
					ownerID, ownerName, x, y, reuse, livetime, scenetype,
					gototype, null));
		else
			GsClient.sendToScene(new GEnterDynamicScene(roleid, baseMapID,
					ownerID, ownerName, x, y, reuse, livetime, scenetype,
					gototype, null));
		return true;
	}

	/**
	 * 角色进入副本, 如果副本不存在,那么就创建
	 * 
	 * @param roleid
	 *            角色对象
	 * @param baseMapID
	 *            副本的地图ID
	 * @param x
	 *            跳入坐标x
	 * @param y
	 *            跳入坐标y
	 * @param ownerID
	 *            所有者ID(可能是角色ID,也可能是团体ID)，根据地图ID和所有者ID可以唯一确定副本
	 * @param ownerName
	 *            所有者名称
	 * @param livetime
	 *            空场景存活时间，单位秒。-1:空场景一直存活不销毁；0:空场景存活默认时间（600秒）；>0 空场景存活x秒
	 * @param scenetype
	 *            场景类型
	 * @param gototype
	 *            跳转类型
	 * @param params
	 *            创建副本用到的参数，默认为DynamicSceneParams类型，不同的副本用到的参数可能不同，所以用octets
	 * @return
	 */
	public static boolean enterDynamicSceneWhileCommit(long roleid,
			int baseMapID, int x, int y, long ownerID, String ownerName,
			int livetime, int scenetype, int gototype, Octets params) {
		if (!Transfer.canChangeMap(roleid, gototype, baseMapID))
			return false;
		byte reuse = 0;
		if (Transaction.current() != null)
			GsClient.pSendWhileCommit(new GEnterDynamicScene(roleid, baseMapID,
					ownerID, ownerName, x, y, reuse, livetime, scenetype,
					gototype, params));
		else
			GsClient.sendToScene(new GEnterDynamicScene(roleid, baseMapID,
					ownerID, ownerName, x, y, reuse, livetime, scenetype,
					gototype, params));
		return true;
	}

	/**
	 * 进入场景
	 * 
	 * @param roleid
	 * @param mapId
	 * @param sceneID
	 * @param x
	 * @param y
	 * @param scenetype
	 * @param gototype
	 * @return
	 */
	public static boolean enterDynamicSceneByIdWhileCommit(final long roleid,
			final int mapId, final long sceneID, final int x, final int y,
			final int scenetype, final int gototype) {
		if (!Transfer.canChangeMap(roleid, gototype, mapId))
			return false;
		if (Transaction.current() != null)
			GsClient.pSendWhileCommit(new GEnterDynamicSceneById(roleid,
					sceneID, x, y, scenetype, gototype));
		else
			GsClient.sendToScene(new GEnterDynamicSceneById(roleid, sceneID, x,
					y, scenetype, gototype));
		return true;
	}

	/*	*//**
	 * 梦境用的跳转梦境场景
	 * 
	 * @param roleid
	 * @param sceneId
	 *            场景ID，没有时填-1。场景ID可以在外面生成后传进来，如果为-1，则在地图模块自动生成
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
	/*
	 * public static void enterDynamicMap(long roleid, int baseMapID, int x, int
	 * y, long ownerID, String ownerName, boolean reusable,boolean team,boolean
	 * playCG,int scenetype) { byte reuse = 0; if(reusable) reuse = 1; byte
	 * _team_ = 0; if(team) _team_ = 1; byte _playcg_ = 0; if(playCG) _playcg_ =
	 * 1; GsClient.sendToScene(new GEnterDynamicMap(roleid,baseMapID, ownerID,
	 * ownerName, x, y, reuse, _team_, _playcg_,scenetype)); }
	 *//**
	 * 梦境用的跳转梦境场景
	 * 
	 * @param roleid
	 * @param sceneId
	 *            场景ID，没有时填-1。场景ID可以在外面生成后传进来，如果为-1，则在地图模块自动生成
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
	/*
	 * public static void enterDynamicMapWhileCommit(long roleid, int baseMapID,
	 * int x, int y, long ownerID, String ownerName, boolean reusable,boolean
	 * team,boolean playCG,int scenetype) { byte reuse = 0; if(reusable) reuse =
	 * 1; byte _team_ = 0; if(team) _team_ = 1; byte _playcg_ = 0; if(playCG)
	 * _playcg_ = 1; if(Transaction.current()!= null)
	 * GsClient.pSendWhileCommit(new GEnterDynamicMap(roleid, baseMapID,
	 * ownerID, ownerName, x, y, reuse, _team_, _playcg_,scenetype)); else
	 * GsClient.sendToScene(new GEnterDynamicMap(roleid,baseMapID, ownerID,
	 * ownerName, x, y, reuse, _team_, _playcg_,scenetype)); }
	 */

	/**
	 * 通过地图的base id 和 ownerid 销毁一个副本地图 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param baseMapID
	 *            副本地图id
	 * @param ownerID
	 *            副本的所有者ID(可能是角色ID,也可能是团体ID)
	 */
	public static void destroyDynamicScene(int baseMapID, long ownerID) {
		GsClient.sendToScene(new GDestroyDynamicSceneByMapOwner(baseMapID,
				ownerID));
	}

	/**
	 * 通过地图的base id 和 ownerid 销毁一个副本地图 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param baseMapID
	 *            副本地图id
	 * @param ownerID
	 *            副本的所有者ID(可能是角色ID,也可能是团体ID)
	 */
	public static void destroyDynamicSceneWhileCommit(int baseMapID,
			long ownerID) {
		if (Transaction.current() != null)
			GsClient.pSendWhileCommit(new GDestroyDynamicSceneByMapOwner(
					baseMapID, ownerID));
		else
			GsClient.sendToScene(new GDestroyDynamicSceneByMapOwner(baseMapID,
					ownerID));
	}

	/**
	 * 通过副本场景ID销毁一个副本地图 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param sceneId
	 *            副本场景ID
	 */
	public static void destroyDynamicSceneById(long sceneId) {
		GsClient.sendToScene(new GDestroyDynamicSceneById(sceneId));
	}

	/**
	 * 通过副本场景ID销毁一个副本地图 这样讲unload这个动态地图，并且通过配置将场景里所有角色传送到一个地方
	 * 
	 * @param sceneId
	 *            副本场景ID
	 */
	public static void destroyDynamicSceneByIdWhileCommit(long sceneId) {
		if (Transaction.current() != null)
			GsClient.pSendWhileCommit(new GDestroyDynamicSceneById(sceneId));
		else
			GsClient.sendToScene(new GDestroyDynamicSceneById(sceneId));
	}

}
