package fire.pb.scene.manager;

import java.util.Map.Entry;

import org.apache.log4j.Logger;

import fire.msp.npc.CreateNpcInfoUniverse;
import fire.msp.npc.MRemoveNpcFromGS;
import fire.msp.npc.MUpdateNpcInfo;
import fire.msp.npc.NpcInfo;
import fire.pb.main.ConfigManager;
import fire.pb.npc.SNpcShare;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneManager;
import fire.pb.scene.config.NpcDesc;
import fire.pb.scene.movable.God;
import fire.pb.scene.movable.Monstershow;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.school.School;

public class SceneNpcManager{
	static public final Logger logger = Logger.getLogger("MAPMAIN");
	private static volatile SceneNpcManager instance;
	public java.util.NavigableMap<Long, NPC> npcs = new java.util.TreeMap<Long, NPC>();;
	private SceneNpcManager(){}
	private long nextkey=0;//FIXME 地图的nextkey从0开始，逻辑从MAX_INTEGER开始，应该够用
	
	
	/**
	 * 获得场景NPC管理器
	 * @return
	 */
	public static SceneNpcManager getInstance()
	{
		if (null == instance)
		{
			if (null == instance)
				instance = new SceneNpcManager();
		}
		return instance;
	}
	
	/**
	 * 在场景npc管理器中添加npc
	 * @param npc
	 * @return
	 */
	private boolean addSceneNpc(NPC npc){
		if (npc == null)
			return false;
		
		for (School school : School.values())//更新职业领袖的NPC 名字和造型 by changhao
		{
			int npcid = school.getNpcid();
			if (npcid == npc.getNpcID())
			{
				xbean.ProfessionLeaderInfo leader = xtable.Professionleader.select(school.getValue());
				if(leader != null)
				{
					xbean.Properties prop = xtable.Properties.select(leader.getRoleid());
					if (prop != null)
					{
						npc.setName(prop.getRolename());
						npc.setShape(prop.getShape());
						for(Entry<Integer, Integer> entry:leader.getShapecomponent().entrySet()){
							if(entry!=null){
								int type=entry.getKey();
								int value=entry.getValue();
								npc.components.put((byte) type, value);
							}
						}
					}
				}					
			}
		}
		
		if (npcs.containsKey(npc.getUniqueID()))
			return false;
		npcs.put(npc.getUniqueID(), npc);
		return true;
	}
	
	/**
	 * 在场景管理器中删除npc
	 * @param npc
	 * @return
	 */
	private boolean removeSceneNpc(NPC npc){
		if (npc == null)
			return false;
		NPC rmvNpc = npcs.remove(npc.getUniqueID());
		if (rmvNpc == null)
			SceneManager.logger.error("remove npc from scene manager failed.npckey:"+npc.getUniqueID()+"npcid:"+npc.getNpcID());
		return true;
	}
	
	/**
	 * 在场景管理器中删除npc，只提供给场景销毁时使用
	 * @param npc
	 * @return
	 */
	public boolean delNpcWhileDestroyScene(long npckey){
		npcs.remove(npckey);
		return true;
	}
	
	public long getNextId(){
		nextkey++;
		return nextkey;
	}
	
	
	/**
	 * 根据 npc key 获得场景的Npc实例
	 * @param key
	 * @return
	 */
	public NPC getNpcByKey(Long key){
		return npcs.get(key);
	}
	
	public java.util.List<NPC> getNpcByID(Integer npcid){
		java.util.ArrayList<NPC> res = new java.util.ArrayList<NPC>();
		for (NPC npc:npcs.values()){
			if (npc.getNpcID() == npcid)
				res.add(npc);
		}
		return res;
	}
	
	/**
	 * 生成一个地图上的NPC，没指定坐标和地图。
	 * 由调用者自己指定,并通知客户端
	 * @param npcid
	 * @param name
	 * @return
	 */
	public NPC createNpc(int npcid, String name, int dir){
		final long npckey = getNextId();
		return createNpcWithKey( npckey, npcid, name, dir );
	}
	
	/**
	 * 生成一个地图上的NPC，没指定坐标和地图。
	 * 由调用者自己指定,并通知客户端
	 * @param npcid
	 * @param name
	 * @return
	 */
	public NPC createNpc(int npcid, String name, int dir, long npcKey){
		return createNpcWithKey(npcKey, npcid, name, dir );
	}
	
	//以指定的npckey生成npc
	public NPC createNpcWithKey(long npckey,int npcid,String name,int dir){
		fire.pb.npc.SNpcShare conf = fire.pb.main.ConfigManager.getInstance().getConf( fire.pb.npc.SNpcShare.class ).get(npcid);
		if ( conf != null && conf.npctype == 5 )
			return GatherManager.getInstance().genGathernpc( npckey, npcid, name, dir );
		else{
			if (name==null||name.equals(""))
				if (conf!=null)
					name = conf.getName();
			
			return God.createNPC(npckey, npcid, name, null, null,dir);
		}
	}
	/**
	 * 在地图mapid的 p坐标，召出npc.npc是通过CreateNpc生出来的，有npckey
	 * 没有通知GS同步NPC数据
	 * @param mapid
	 * @param p
	 * @return
	 */
	public boolean summonNpc(Scene s, Position p, NPC npc)
	{
		if (null == s)	return false;
		if (npc == null) return false;
		if(!s.validPosition(p))
		{
			logger.error("NPC" + npc.getNpcID() +"在地图"+s.getMapID()+"的外面，坐标" + p.toGridPos());
			return false;
		}
		npc.setScene(s);
		SNpcShare npcshare = ConfigManager.getInstance().getConf(SNpcShare.class).get(npc.getNpcID());
		Position finalp = new Position(p.getX(),p.getY(),npcshare.zPos);
		npc.setPos(finalp);
		
		// 添加到npc管理器中
		if (SceneNpcManager.getInstance().addSceneNpc(npc))
		{
			npc.enterMyScreen();
			return true;
		}
		logger.error("summonnpc failed.scene:"+s+"position:"+p.toGridPos()+"npcid:"+npc.getNpcID()+"npckey:"+npc.getNpcKey());
		return false;
	}
	
	/**
	 * 召唤npc后通知GS更新信息
	 * 在地图mapid的 p坐标，召出npc.npc是通过CreateNpc生出来的，有npckey
	 * 通知GS同步NPC数据
	 * @param s 场景
	 * @param p 位置
	 * @param npc npc数据结构
	 * @return
	 */
	public boolean summonNpcAndUpdateGS(Scene s, Position p, NPC npc)
	{
		return summonNpcAndUpdateGS(s, p, npc, 0);
	}
	
	/**
	 * 召唤npc后通知GS更新信息
	 * 在地图mapid的 p坐标，召出npc.npc是通过CreateNpc生出来的，有npckey
	 * 通知GS同步NPC数据
	 * @param s 场景
	 * @param p 位置
	 * @param npc npc数据结构
	 * @param time 如果是限时NPC，填时间（毫秒），非限时NPC默认填0
	 * @return
	 */
	public boolean summonNpcAndUpdateGS(Scene s, Position p, NPC npc,long time)
	{
		if (summonNpc(s, p, npc))
		{
			MUpdateNpcInfo mUpdateNpcInfo = new MUpdateNpcInfo();
			NpcInfo npcinfo = new NpcInfo();
			npcinfo.mapid = npc.getScene().getMapID();
			npcinfo.name = npc.getName();
			npcinfo.shape = npc.getShape();
			npcinfo.npcbaseid = npc.getNpcID();
			npcinfo.npckey = npc.getUniqueID();
			npcinfo.posx = npc.getPos().getX();
			npcinfo.posy = npc.getPos().getY();
			npcinfo.posz = npc.getPos().getZ();
			npcinfo.sceneid = npc.getScene().getSceneID();
			npcinfo.time = time;
			mUpdateNpcInfo.npcs.add(npcinfo);
			SceneClient.pSend(mUpdateNpcInfo);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * 从场景中删除npc，通知客户端,和removeNpcFromSceneWithSP功能现在一样
	 * 会通知GS删除
	 * @param npckey
	 */
	public boolean removeNpcFromScene(final long npckey,String trace){
		final NPC npc = SceneNpcManager.getInstance().getNpcByKey(npckey);
		// 场景中删除
		if(npc == null)	return false;
		
		final Scene ns = npc.getScene();
		if(null == ns) return false;
		
		if (npc.getNpcID() == 161301 
				|| npc.getNpcID() == 161302
				|| npc.getNpcID() == 161303
				|| npc.getNpcID() == 161304) {
			logger.info(new StringBuilder().append("remove npc from scene.npckey:")
					.append(npckey)
					.append(",npcid:")
					.append(npc.getNpcID())
					.append(",trace:")
					.append(trace));
		}
		npc.leaveMyScreen();
		
		// 场景的npc管理器中删除
		SceneNpcManager.getInstance().removeSceneNpc(npc);
		if (npc instanceof Monstershow) {
			if (MonstershowManager.getInstance().remove(npckey))
				new Exception("remove npcerror").printStackTrace();
		}
//		logger.info("remove npc from scene.npckey:"+npckey+"npcid:"+npc.getNpcID()+"trace:"+trace);
		MRemoveNpcFromGS mRemoveNpcFromGS = new MRemoveNpcFromGS();
		mRemoveNpcFromGS.trace = trace;
		mRemoveNpcFromGS.npckey =npckey;
		SceneClient.pSend(mRemoveNpcFromGS);
		return true;
	}
	

	/**
	 * 根据地图上的一个配置生成npc，并添加到npc管理器和具体的场景块中.
	 * 没有通知客户端。
	 * 没有通知GS同步数据。
	 * @param mapid
	 * @param Position
	 * @return
	 */
	public boolean summonNpc(Scene s, NpcDesc npc){
		if (npc == null)
			return false;
		final NPC mapnpc = createNpc(npc.getid(), "", npc.getdir());
		return summonNpc(s, npc.getPos(), mapnpc);
	}
	
	/**
	 * 根据地图上的一个配置生成npc，并添加到npc管理器和具体的场景块中.
	 * 没有通知客户端。
	 * 通知GS同步数据。
	 * @param mapid
	 * @param Position
	 * @return
	 */
	public NPC summonNpcAndUpdateGS(Scene s, NpcDesc npc){
		if (npc == null)
			return null;
		final NPC mapnpc = createNpc(npc.getid(), "", npc.getdir());
		boolean succ = summonNpcAndUpdateGS(s, npc.getPos(), mapnpc,0);
		if(succ)
			return mapnpc;
		else
			return null;
	}

	
	/**
	 * 查找地图上的某个点是否已经有npc了,x或y的差距在3以内即认为是重合
	 * @param pos
	 * @param sceneid
	 * @return
	 */
	public boolean hasNpcInGridPos(GridPos pos,long sceneid){
		
		Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if (null == scene)
			return true;

		final int index = scene.getScreenIndex(pos.toPosition());
		for (NPC n : scene.getNpcsInScreen(index).values()) {
			if (n.getPos().toGridPos().getDistance(pos) < 8)
				return true;
		}
		
		return false;
		
		/*Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if(null == scene) return true;
		
		final int index = scene.getScreenIndex(pos.toPosition());
		List<NPC> sceneNpc = new ArrayList<NPC>();
		sceneNpc.addAll(scene.getNpcsInScreen(index).values());
		for (NPC n : sceneNpc)
		{
			if(n.getPos().toGridPos().getDistance(pos)<8)
				return true;
		}
		
		return false;*/
	}
	
	
	/**
	 * 根据提供的info，生成NPC，并在地图上召出NPC
	 * 使用通用的在地图上召出NPC的处理,调用者根据返回的npclist统一同步到GS.
	 * @param info
	 * @return
	 * @author  
	 */
	public java.util.List<fire.msp.npc.NpcInfo> createNpc(CreateNpcInfoUniverse info){
		if (info.num <= 0)
			return null;
		// 未指定坐标，在地图上随即
		java.util.List<fire.msp.npc.NpcInfo> npclist = new java.util.LinkedList<fire.msp.npc.NpcInfo>();
		//final Scene s = SceneManager.getInstance().getSceneByID(info.mapid);
		
		Scene s = null;
		if (info.ownerid == 0){
			s = SceneManager.getInstance().getSceneByID(info.mapid);
		}
		else{
			s = SceneManager.getInstance().getSceneByMapAndOwner(info.mapid, info.ownerid);
		}
		
		if (s == null)
			return npclist;
		for (int i = 0; i < info.num; i++) {
			NPC mapnpc = null;
			if(info.npckey != 0 && info.num==1){
				mapnpc =  createNpc(info.npcbaseid, info.name, 0, info.npckey);
			}else{
				mapnpc = createNpc(info.npcbaseid, info.name, 0);
			}
			mapnpc.setShape(info.shape);
			GridPos pos = null;
			if (info.posx == 0 || info.posy == 0)
				pos = fire.pb.scene.MapUtil.genPosInMap(s.getSceneID());
			else
				pos = new GridPos(info.posx, info.posy);
			
			if (!summonNpc(s, pos.toPosition(), mapnpc)) {
				for (NpcInfo remove : npclist) {
					// 已召出的需要删除
					SceneNpcManager.getInstance().removeNpcFromScene(
							remove.npckey,"SceneNpcManager.createNpc");
				}
				return npclist;
			}
			

			fire.msp.npc.NpcInfo npcinfo = new fire.msp.npc.NpcInfo(
					mapnpc.getUniqueID(), mapnpc.getNpcID(), mapnpc.getName(),
					mapnpc.getShape(), s.getSceneID(), info.mapid, s.getName(), pos
							.toPosition().getX(), pos.toPosition().getY(),pos.toPosition().getZ(), info.time);
			npclist.add(npcinfo);
		}

		return npclist;
	
	}
	
	/**
	 * 根据一些列创建npc的信息，生成一堆npc
	 * @param info
	 * @return
	 */
	///////////////////////////////////////////静态函数/////////////////////////////////////////////////////
}
