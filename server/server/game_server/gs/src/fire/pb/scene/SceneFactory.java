package fire.pb.scene;

import fire.msp.move.MCreateDynamicScene;
import fire.pb.map.MapConfig;

import com.locojoy.base.Octets;


/**
 * 用于生成场景的工厂类
 * @author nobody
 *
 */
public final class SceneFactory {

	/**
	 * 创建静态场景
	 * @param mapID 静态场景的地图ID
	 * @param name	该场景的名称
	 * @return
	 */
	public static StaticScene createStaticScene(MapConfig cfg){
		
		//MapConfig cfg = ConfigManager.getInstance().getConf(MapConfig.class).get(mapID);
		if(cfg == null)
			return null;
		
		StaticScene ss = null;
		if(cfg.visibletype == Scene.VISIBLE_ALL)//全可见
			ss = new StaticScene();
		else if(cfg.visibletype == Scene.VISIBLE_SINGLE)//自己可见
			ss = new SingleStaticScene();
		else if(cfg.visibletype == Scene.VISIBLE_TEAM)//小队可见
			ss = new TeamStaticScene();
		if(ss == null)
		{
			Module.logger.error("静态场景初始化错误，MapID = " + cfg.id + ", name = " + cfg.mapName + "dynamic = " + cfg.dynamic);
			return null;
		}
		ss.setSceneID(cfg.id);
		ss.setName(cfg.mapName);
		ss.setDynamicType(cfg.dynamic);
		
		ss.load();
		ss.setMapConfig(cfg);
		SceneManager.getInstance().addNewStaticScene(ss);
		
		return ss;
	}
	
	/**
	 * 创建动态场景模板信息
	 * @param mapID 该场景模板的地图ID
	 * @param name	名称
	 * @return
	 */
	public static DynamicTemplate createDynamicTemplate(MapConfig cfg){
		DynamicTemplate dt = new DynamicTemplate();
		dt.setMapID(cfg.id);
		dt.setName(cfg.mapName);
		dt.setDynamic(cfg.dynamic);
		dt.setMapConfig(cfg);
		dt.load();
		SceneManager.getInstance().addNewTemplate(dt);
		return dt;
	}
	
	/**
	 * 创建副本场景,全部角色离开后场景可重新进入
	 * @param mapID			副本地图ID
	 * @param name			副本名称
	 * @param ownerID		副本的所属者ID
	 * @param recyclable	场景是否可重用 true : 全部角色离开后场景可重新进入 
	 * @param scenetype		场景类型
	 * 							
	 * @return
	 */
	public static DynamicScene createDynamicScene(int mapID, long ownerID, String ownerName,int livetime,int scenetype,Octets parameters){
		DynamicScene ds = null;
		ds = new DynamicScene();
		ds.setMapID(mapID);
		ds.setOwnerID(ownerID);
		ds.setScenetype(scenetype);
		ds.setOwnerName(ownerName);
		ds.setLiveTime(livetime);
		ds.setInitparams(parameters);
		ds.load();
		if(ds.getSceneID() == 0L)
			return null;
		
		ds.update(ds.getInitparams());
		SceneClient.pSend(new MCreateDynamicScene(ds.getSceneID(), ownerID, scenetype));
		return ds;
	}
	/*public static void clearTimeoutDynamicScene()
	{
		long now = System.currentTimeMillis();
		if(now - lastcleartime < CLEAR_PERIOD_MS)
			return;
		lastcleartime = now;
		int timeoutnum = 0;
		java.util.List<Scene> deadscenes = new java.util.ArrayList<Scene>(); 
		for(Scene s : SceneManager.getInstance().getAllScenes().values())
		{
			if(s instanceof DynamicScene)
			{
				if(!((DynamicScene) s).isAlive(now))
				{
					
					deadscenes.add(s);
					timeoutnum ++;
				}
			}
		}
		
		for(Scene s : deadscenes)
			s.unload();
		
	}*/
	
}
