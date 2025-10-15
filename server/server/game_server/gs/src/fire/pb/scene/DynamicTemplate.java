package fire.pb.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;

/**
 * 动态地图模板
 * @author nobody
 *
 */
public final class DynamicTemplate extends Scene{
	public static long CLEAR_PERIOD_MS = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.dynamicscene.clearperiod") * 1000;//清理过期副本的最小间隔
	
	public void emptyProcess(){}
	public long getOwnerID(){return 0;}
	public String getOwnerName(){return "";}
	public void setSceneID(long sceneID){}
	public String getName(){return super.getName();}
	
	public Map<Long,DynamicScene> scenes = new ConcurrentHashMap<Long, DynamicScene>();
	
	/**
	 * 模板ID从配置中读取
	 */
	public int getMapID(){
		return mapID;
	}
	
	public void setMapID(int mapid){
		mapID = mapid;
	}
	
	/**
	 * 获取当前该副本模板的所有实例
	 * @return
	 */
	public Map<Long,DynamicScene> getScenes()
	{
		return scenes;
	}
	
	DynamicScene getSceneByID(long sceneid)
	{
		return processAlive(scenes.get(sceneid));
	}
	
	DynamicScene getSceneByOwnerID(long ownerId)
	{
		for (DynamicScene s : scenes.values())
			if (ownerId == s.getOwnerID())
				return processAlive(s);
		return null;
	}
	/**
	 * 处理副本是否存活
	 * @param scene
	 * @return
	 */
	private DynamicScene processAlive(DynamicScene scene)
	{
		if(scene!=null && !scene.isAlive(System.currentTimeMillis()))
		{
			scene.unload();
			return null;
		}
		else
			return scene;
	}
	
	DynamicScene addNewScene(DynamicScene scene)
	{
		clearTimeoutDynamicScene();//添加新副本时，清除过期副本
		return scenes.put(scene.getSceneID(), scene);
	}
	
	DynamicScene removeScene(long sceneId)
	{
		return scenes.remove(sceneId);
	}
	
	public void clearTimeoutDynamicScene()
	{
		long now = System.currentTimeMillis();
		if(now - lastcleartime < CLEAR_PERIOD_MS)
			return;
		lastcleartime = now;
		java.util.List<DynamicScene> deadscenes = new java.util.ArrayList<DynamicScene>(); 
		for(DynamicScene s : scenes.values())
		{
			if (!s.isAlive(now))
			{
				deadscenes.add(s);
			}
		}
		for(DynamicScene s : deadscenes)
			s.unload();
	}
	
	public final void load(){
		MapInfo info = new MapInfo(getMapID());
		info.load();
		setMapInfo(info);
		Scene.LOG.info("加载动态地图模板\t" + getName());
	}

	private int mapID = 0;
	private int dynamic = 0;
	private final static long STEP = (long)1 << 32;
	private long currentStepId = STEP;
	private boolean isOverflowed = false;
	private long lastcleartime = 0;
	
	long getNextDynamicSceneId()
	{
		currentStepId += STEP;
		if(currentStepId <= 0)
		{
			currentStepId = STEP;
			isOverflowed = true;//溢出了，循环的时候需要检查是否被占用
		}
		if(isOverflowed)
		{
			while(scenes.containsKey(currentStepId+mapID))
			{
				currentStepId += STEP;
				if(currentStepId <= 0)
					currentStepId = STEP;
			}
		}
		
		return currentStepId + mapID;
	}
	public int getDynamic() {
	
		return dynamic;
	}
	
	public void setDynamic(int dynamic) {
	
		this.dynamic = dynamic;
	}
	
}
