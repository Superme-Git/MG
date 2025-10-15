package fire.pb.scene;

import gnet.link.Onlines;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.locojoy.base.Octets;



import fire.pb.map.MapConfig;
import fire.pb.scene.manager.RoleManager;

/**
 * 场景管理器
 * @author nobody
 *
 */
public class SceneManager {
	public static Logger logger = Logger.getLogger("MAPMAIN"); 
	private static volatile SceneManager instance;	
	private SceneManager(){}
	public static SceneManager getInstance(){
		if(null == instance){instance = new SceneManager();}		
		
		return instance;
	}	

	/**
	 * 通过场景ID获取场景
	 * @param sceneid
	 * @return
	 */
	public Scene getSceneByID(long sceneid){
		int mapid = (int)sceneid;
		if(sceneid == (int)sceneid)
		{//静态场景
			return staticscenes.get(mapid);
		}
		else
		{//动态场景
			DynamicTemplate t = getDynamicTemplate(mapid);
			if (t == null){//addby cn 当策划修改了mapid的时候,也要重定向
				return null;
			}
			DynamicScene s = t.getSceneByID(sceneid);
			if(s != null && !s.isAlive(System.currentTimeMillis()))
			{
				s.unload();
				return null;
			}
			return s;
		}
	}
	
	/**
	 * 尽量不要使用此方法获取全部的场景，如果必须获得全部场景
	 * 注意用isAvlie判断动态场景（副本）是否存活
	 * @return
	 */
	public java.util.Map<Integer, StaticScene> getAllStaticScenes(){
		return staticscenes;
	}
	
	/**
	 * 通过地图ID和拥有者ID，获得副本场景
	 * @param mapId
	 * @param ownerId
	 * @return
	 */
	public DynamicScene getSceneByMapAndOwner(int mapId, long ownerId)
	{
		DynamicTemplate t = getDynamicTemplateByMapID(mapId);
		if(t == null)
			return null;
		return t.getSceneByOwnerID(ownerId);
	}
	
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public void sendAll(mkio.Protocol p2){
		Onlines.getInstance().send(RoleManager.getInstance().getRoles().keySet(), p2);
	}
	
	/**
	 * 向所有场景上的角色发送协议
	 * @param p2
	 */
	public void sendOctetsAll(int ptype, Octets poctets){
		Onlines.getInstance().sendOctets(RoleManager.getInstance().getRoles().keySet(), ptype,poctets);
	}
	
	void init() throws Exception{
		final java.util.Map<Integer,MapConfig> config =
			fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.map.MapConfig.class);
		/**
		 * 根据策划配置表,对地图进行加载和配置
		 */
		for(MapConfig m : config.values()){
			if(0 == m.dynamic)	// 静态地图场景
			{
				final StaticScene ss = SceneFactory.createStaticScene(m);
				mapInfos.put(m.getId(), ss.getMapInfo());
			}
			else		// 副本模板
			{					
				DynamicTemplate dt = SceneFactory.createDynamicTemplate(m);
				mapInfos.put(m.getId(), dt.getMapInfo());
			}
		}
		
	}
	
	DynamicTemplate getDynamicTemplateByMapID(int mapID){
		return templates.get(mapID);
	}
	
	void addNewStaticScene(final StaticScene scene){
		staticscenes.put(scene.getMapID(), scene);
	}

	public void removeStaticScene(final int mapid){
		staticscenes.remove(mapid);
	}
	
	void addNewTemplate(final DynamicTemplate dt){
		templates.put(dt.getMapID(), dt);
	}
	
	public DynamicTemplate getDynamicTemplate(int mapId)
	{
		return templates.get(mapId);
	}
	
	/**
	 * 此方法可以获取某个地图的MapInfo,MapInfo是只读的，以此提供给逻辑使用
	 * 因为逻辑侧需要的太多了
	 * @param mapId
	 * @return
	 */
	public MapInfo getMapInfo(int mapId)
	{
		return mapInfos.get(mapId);
	}
	
	/**
	 * 场景ID : 静态场景 
	 */
	private final java.util.Map<Integer, StaticScene> staticscenes = new java.util.HashMap<Integer, StaticScene>();
	
	/**
	 * 动态地图 模板
	 * 地图ID : 地图的场景模板
	 */
	private final Map<Integer, DynamicTemplate> templates = new java.util.HashMap<Integer, DynamicTemplate>();
	
	/**
	 * 用于生成动态地图ID
	 */
	int curDynamicSceneID = 5000;
	
	private final Map<Integer, MapInfo> mapInfos = new HashMap<Integer, MapInfo>();//所有的mapinfos
}
