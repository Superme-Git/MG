package fire.pb.map;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import fire.log.Logger;
import fire.msp.GSendAll;
import fire.msp.GSendAllByCondition;
import fire.msp.GSendAllByScene;
import fire.msp.GSendAllBySchool;
import fire.msp.GSendAroundByPos;
import fire.msp.GSendToSceneByRole;
import fire.msp.role.GSendAround;
import fire.msp.role.GSendAroundByCondition;
import fire.pb.GsClient;
import mkdb.Transaction;

import com.locojoy.base.Marshal.OctetsStream;

/**
 * 场景管理器
 * @author nobody
 *
 */
public abstract class SceneManager {

	public static Logger logger = Logger.getLogger("MAPMAIN");
	public static Map<Integer, MapConfig> configs = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class);
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public static void sendAround(long roleId, mkio.Protocol p2)
	{
		GSendAround gSendAround = new GSendAround();
		gSendAround.roleid = roleId;
		gSendAround.protype = p2.getType();
		gSendAround.protocol =  p2.marshal(new OctetsStream());
		GsClient.sendToScene(gSendAround);
	}
	
	public static void sendAroundByCondition(long roleId, java.util.List<Integer> exceptstates, mkio.Protocol p2){
		GSendAroundByCondition gSendAroundByCondition = new GSendAroundByCondition();
		gSendAroundByCondition.roleid = roleId;
		gSendAroundByCondition.protype = p2.getType();
		gSendAroundByCondition.protocol =  p2.marshal(new OctetsStream());
		if (exceptstates != null && !exceptstates.isEmpty()){
			gSendAroundByCondition.exceptstates.addAll(exceptstates);
		}
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAroundByCondition);
		else
			GsClient.sendToScene(gSendAroundByCondition);
	}
	
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public static void psendAroundWhileCommit(long roleId,mkio.Protocol p2)
	{
		GSendAround gSendAround = new GSendAround();
		gSendAround.roleid = roleId;
		gSendAround.protype = p2.getType();
		gSendAround.protocol =  p2.marshal(new OctetsStream());
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAround);
		else
			GsClient.sendToScene(gSendAround);
	}
	
	/**
	 * 向所有场景上某个点的9屏发送协议
	 * @param p2
	 */
	public static void sendAround(long sceneid,int gposx, int gposy, mkio.Protocol p2)
	{
		GSendAroundByPos gSendAround = new GSendAroundByPos();
		gSendAround.protocol = p2.marshal(new OctetsStream());
		gSendAround.protype = p2.getType();
		gSendAround.sceneid = sceneid;
		gSendAround.gposx = (short)gposx;
		gSendAround.gposy = (short)gposy;
		if(Transaction.current() == null)
			GsClient.sendToScene(gSendAround);
		else
			GsClient.pSendWhileCommit(gSendAround);
	}
	
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public static void sendAll(mkio.Protocol p2)
	{
		GSendAll gSendAll = new GSendAll();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public static void psendAllWhileCommit(mkio.Protocol p2)
	{
		GSendAll gSendAll = new GSendAll();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAll);
		else
			GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向一个门派所有的角色发送协议
	 * @param p2
	 */
	public static void sendAllBySchool(mkio.Protocol p2,int schoolId)
	{
		GSendAllBySchool gSendAll = new GSendAllBySchool();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.schoolid = schoolId;
		GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向一个门派所有的角色发送协议
	 * @param p2
	 */
	public static void psendAllBySchoolWhileCommit(mkio.Protocol p2,int schoolId)
	{
		GSendAllBySchool gSendAll = new GSendAllBySchool();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.schoolid = schoolId;
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAll);
		else
			GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向一个场景内的所有的角色发送协议
	 * @param p2
	 */
	public static void sendAllByScene(mkio.Protocol p2,long sceneId)
	{
		GSendAllByScene gSendAll = new GSendAllByScene();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.sceneid = sceneId;
		GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向一个场景内的所有的角色发送协议
	 * @param p2
	 */
	public static void psendAllBySceneWhileCommit(mkio.Protocol p2,long sceneId)
	{
		GSendAllByScene gSendAll = new GSendAllByScene();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.sceneid = sceneId;
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAll);
		else
			GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向角色所在场景内的所有的角色发送协议
	 * @param p2
	 */
	public static void sendToSceneByRole(mkio.Protocol p2,long roleId)
	{
		GSendToSceneByRole gSendAll = new GSendToSceneByRole();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.roleid = roleId;
		GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向角色所在场景内的所有的角色发送协议
	 * @param p2
	 */
	public static void psendToSceneByRoleWhileCommit(mkio.Protocol p2,long roleId)
	{
		GSendToSceneByRole gSendAll = new GSendToSceneByRole();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.roleid = roleId;
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAll);
		else
			GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 
	 * @param p2
	 * @param sceneId 门派ID，如果无限制填-1
	 * @param schoolId 场景ID，如果无限制填-1
	 * @param level 等级，如果无限制填-1
	 */
	public static void sendAllByCondition(mkio.Protocol p2,int sceneId,int schoolId,int minlevel,int maxlevel,int camp)
	{
		GSendAllByCondition gSendAll = new GSendAllByCondition();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.sceneid = sceneId;
		gSendAll.schoolid = schoolId;
		gSendAll.minlevel = minlevel;
		gSendAll.maxlevel = maxlevel;
		gSendAll.camp = camp;
		GsClient.sendToScene(gSendAll);
	}
	
	/**
	 * 向一个门派所有的角色发送协议
	 * @param p2
	 */
	public static void psendAllByConditionWhileCommit(mkio.Protocol p2,int sceneId,int schoolId,int minlevel,int maxlevel)
	{
		GSendAllByCondition gSendAll = new GSendAllByCondition();
		gSendAll.protype = p2.getType();
		gSendAll.protocol =  p2.marshal(new OctetsStream());
		gSendAll.sceneid = sceneId;
		gSendAll.schoolid = schoolId;
		gSendAll.minlevel = minlevel;
		gSendAll.maxlevel = maxlevel;
		if(Transaction.current()!= null)
			GsClient.pSendWhileCommit(gSendAll);
		else
			GsClient.sendToScene(gSendAll);
	}
	
    public static void psendAllByConditionWhileCommit(mkio.Protocol p2, int sceneId, int schoolId, int minlevel,int maxlevel, Collection<Long> exceptRoles) {
        GSendAllByCondition gSendAll = new GSendAllByCondition();
        gSendAll.protype = p2.getType();
        gSendAll.protocol = p2.marshal(new OctetsStream());
        gSendAll.sceneid = sceneId;
        gSendAll.schoolid = schoolId;
        gSendAll.minlevel = minlevel;
        gSendAll.maxlevel = maxlevel;
        gSendAll.exceptroles = new ArrayList<Long>(exceptRoles);
        if (Transaction.current() != null)
            GsClient.pSendWhileCommit(gSendAll);
        else
            GsClient.sendToScene(gSendAll);
    }
	/**
	 * 根据mapid获得map名字
	 * @param mapid
	 * @return
	 */
	public static String getMapNameByMapID(int mapid){
		final MapConfig conf = configs.get(mapid);
		if (conf == null)
			return "";
		else return conf.mapName;
	}
	
}
