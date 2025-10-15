package fire.pb.scene;

import fire.pb.scene.config.NpcDesc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 地图上的基本信息
 * @author nobody
 *
 */
public final class MapInfo {
	
	MapInfo(int mapid)
	{
		mapID = mapid;
		blockInfo = new BlockInfo(mapID);
	}
	/*MapInfo(int mapid,int mazeID){
		this.mazeID = mazeID;
	}*/
	
	int getMapID(){
		return mapID;
	}
	
	public BlockInfo getBlockInfo(){
		return blockInfo;
	}
	
	public MonsterInfo getMonsterInfo(){
		return monsterInfo;
	}
	
	java.util.List<NpcDesc> getConfNpcList(){
		return confNpcList;
	}
	
	public boolean existMazeFile()
	{
		return blockInfo.existMazeFile();
	}
	void load(){
		blockInfo.loadMap();			// 加载阻挡点信息
		blockInfo.loadChannels();		// 加载跳转点信息
		blockInfo.genNoneBlockList(); // 生成非阻挡点列表
		blockInfo.loadArea();        //加载区域信息:切磋,摆摊,投放
		//blockInfo.loadArea2();        //加载区域信息:切磋,摆摊,投放
		blockInfo.genToufangPosList(); //生成区域信息
		
		monsterInfo = new MonsterInfo(getMapID());
		monsterInfo.load();			// 加载布怪信息
		
		loadNpcConfig();
	}
	
	
	@SuppressWarnings("unchecked")
	private void loadNpcConfig(){
		final String loadFile = "gamedata/map/" + mapID + "/npc.dat";
		final XStream s = new XStream(new DomDriver());
		s.alias("data", java.util.List.class);
		s.alias("record", NpcDesc.class);
		s.useAttributeFor(NpcDesc.class, "id");
		s.useAttributeFor(NpcDesc.class, "posx");
		s.useAttributeFor(NpcDesc.class, "posy");
		s.useAttributeFor(NpcDesc.class, "dir");
		try{
			confNpcList = (java.util.List<NpcDesc>)s.fromXML(new java.io.FileInputStream(loadFile));
		}catch(final java.io.FileNotFoundException e){
			//Scene.logger.info("从"+loadFile+"加载npc信息失败,没有npc.dat文件!");
		}	
	}
	
	final int mapID;						// 地图ID
	//final int mazeID;						// mazeID 禁用此种方式
	private BlockInfo blockInfo = null;			// 地图阻挡信息
	private MonsterInfo monsterInfo = null;		// 地图布怪信息
	private java.util.List<NpcDesc> confNpcList = null;	// 地图NPC信息
}
