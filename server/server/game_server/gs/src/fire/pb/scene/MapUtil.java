
package fire.pb.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.Role;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;
import fire.pb.util.Misc;

public class MapUtil {

	private static Random random = new Random(System.currentTimeMillis());
	
	public static final int TOUFANG_AREA = 0;//普通投放区
	public static final int TOUFANG_AREA_QINGGONG1 = 1;//带1级轻功区域的投放区
	/**
	 * 获取场景地图上所有可行走部分的格子坐标
	 * 注意：返回值是只读的list
	 * @param mapID
	 * @return
	 */
	public static java.util.List<GridPos> getAllArrivablePos(final long sceneid,final int toufangareatype) {

		final java.util.List<GridPos> list = new java.util.LinkedList<GridPos>();
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if (null == scene)
			return list;

		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return list;
        if (!mi.getToufangList().isEmpty()) {
        	if (toufangareatype == TOUFANG_AREA) 
        	   return Collections.unmodifiableList(mi.getToufangList());
        	else
        		return Collections.unmodifiableList(mi.getToufang2List());
        }else
        	return Collections.unmodifiableList(mi.getNoneBlockList());
	}
	
	/**
	 * 获取以某个Grid坐标为中心，周围最近的非阻挡点
	 * @param sceneid
	 * @param origpos
	 * @param blockType
	 * @return
	 */
	public static GridPos getNearestUnblockGridPos(final long sceneid, final GridPos origpos, short blockType)
	{
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneid);
		if (null == scene)
			return null;
		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return null;
		if(!mi.checkBlock(origpos, blockType))
			return origpos;
		for(int r = 1 ; r < 500; r++)
		{//暂定最大半径为500
			List<GridPos> gposes = mi.getAroundGridPoses(origpos, r);
			if(gposes.size() == 0)
				return null;
			for(GridPos gpos : gposes)
			{
				if(!scene.validPosition(gpos.toPosition()))
					continue;
				if(!mi.checkBlock(gpos, blockType))
					return gpos;
			}
		}
		return null;
	}
	
	public static List<GridPos> getRandomArrivablePosesForNpc(final long sceneID, final int num) {
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneID);
		if (null == scene)
			return null;
		final java.util.List<GridPos> list = new LinkedList<GridPos>(); 
		List<GridPos> gposes = new ArrayList<GridPos>();
		list.addAll(getAllArrivablePos(sceneID,TOUFANG_AREA));
		
		while(!list.isEmpty())
		{
			if(list.isEmpty())
				break;
			int random = Misc.getRandomBetween(0, list.size()-1);
			GridPos gpos = list.remove(random);
			if (SceneNpcManager.getInstance().hasNpcInGridPos(gpos, sceneID))
				continue;
			else
				gposes.add(gpos);
		}
		/*for(int i = 0; i < list.size();i++)
		{
			if (SceneNpcManager.getInstance().hasNpcInGridPos(list.get(i), sceneID))
				list.remove(i);
		}
		
		
		List<Integer> indexs = Misc.getRandomValues(0, list.size() -1, num);
		for(int i = 0 ; i < indexs.size() ; i ++)
		{
			gposes.add(list.get(indexs.get(i)));
		}*/
		return gposes;
	}
	public static GridPos getRandomArrivablePos(final long sceneID){
		return getRandomArrivablePos(sceneID, TOUFANG_AREA);
	}
	public static GridPos getRandomArrivablePos(final long sceneID,final int toufangareatype) {
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneID);
		if (null == scene)
			return null;
		
		java.util.List<GridPos> randomList = new java.util.ArrayList<GridPos>();
		randomList.addAll(getAllArrivablePos(sceneID,toufangareatype));
	
		Collections.shuffle(randomList);
		if (randomList.isEmpty())
			SceneManager.logger.error("map has no available pos.sceneid:"+sceneID+"toufangarea"+toufangareatype);
		GridPos pos = randomList.get(0);
		for (int i = 0; i < randomList.size(); i++) {
			try {
				pos = randomList.get(i);
//				if (scene.checkBlock(pos, BlockInfo.CHANNEL_BLOCK))
//					continue;
				if (scene.checkBaitan(pos))
					continue;
				if (SceneNpcManager.getInstance().hasNpcInGridPos(pos, sceneID))
					continue;
				
				return pos;
				
			} catch (Exception e) {
				SceneManager.logger.error("getRandomArrivablePos list size:" + randomList.size(), e);
			}
		}
		
		/*final java.util.List<GridPos> list = getAllArrivablePos(sceneID,toufangareatype);
		GridPos pos = list.get(0);//还是不要返回null了,如果一直找不到合适的点就默认返回第一个吧,找不到的概率应该是极低极低的
		int i = 0;
		int size = list.size();
		while (i < size) {
			i++;
			pos = list.get(random.nextInt(size));
			if (scene.checkBlock(pos, BlockInfo.CHANNEL_BLOCK))
				continue;
			if (scene.checkBaitan(pos))
				continue;
			if (SceneNpcManager.getInstance().hasNpcInGridPos(pos, sceneID)) {
				continue;
			} else {
				return pos;
			}
		}*/

		return pos;
	}
	
	
	
	
	
	public static GridPos getRandomArrivablePosRamdom(final long sceneID) {
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneID);
		if (null == scene)
			return null;
		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
        	
		final java.util.List<GridPos> list = Collections.unmodifiableList(mi.getNoneBlockList());
		GridPos pos = list.get(0);//还是不要返回null了,如果一直找不到合适的点就默认返回第一个吧,找不到的概率应该是极低极低的
		int i = 0;
		int size = list.size();
		while (i < size) {
			i++;
			pos = list.get(random.nextInt(size));
			if (scene.checkBlock(pos, BlockInfo.CHANNEL_BLOCK))
				continue;
			if (SceneNpcManager.getInstance().hasNpcInGridPos(pos, sceneID)) {
				continue;
			} else {
				return pos;
			}
		}

		return pos;
	}
	
	
	/**
	 * getRandomArrivablePosFromList:可以从一块地图的某个矩形区域内获得非阻挡点,区域内的阻挡点放入一个list中,然后随机一个
	 * @param result
	 * @return
	 */
	public static GridPos getRandomArrivablePosFromList(List<GridPos>result) {

      GridPos pos =null;
		int size = result.size();
		if (size > 0) {
			pos = result.get(random.nextInt(size));
		}

		return pos;
	}
	public static java.util.List<GridPos> getRandomArrivablePosList(final long sceneid, int leftTopX, int leftTopY, int width, int height) {

		final java.util.List<GridPos> list = getAllArrivablePos(sceneid,TOUFANG_AREA);
		final java.util.List<GridPos> result = new ArrayList<GridPos>();
		for (GridPos gridPos : list) {
			int x = gridPos.getX();
			int y = gridPos.getY();
			if (leftTopX <= x && x - leftTopX < width && leftTopY <= y && y - leftTopY < height) {
				result.add(gridPos);
			}
		}

		return result;
	}
	/**
	 * 在地图上随机一个不是阻挡点的GridPos.考虑极端情况下会出现死循环的情况,设置10000次作为上限.将来可以视情况作改变
	 * 
	 * @return
	 */
	public static GridPos genPosInMap(long sceneid) {

		return getRandomArrivablePos(sceneid);
	}

	/**
	 * 获得当前任务九屏范围内一定数量的随机非阻挡点
	 * 
	 * @param mapid
	 * @param roleid
	 * @param num
	 * @return
	 */
	public static java.util.List<GridPos> genPosInLimitedMap(long roleid, int num) {

		java.util.List<GridPos> pos = new ArrayList<GridPos>();
		Role role = RoleManager.getInstance().getRoleByID(roleid);
		final Scene scene = role.getScene();
		if (null == scene)
			return pos;

		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return pos;

		GridPos currentPos = role.getPos().toGridPos();

		java.util.List<GridPos> list = getStandableGridsAround(scene, currentPos, 16);

		if (null == list)
			return pos;

		Integer[] tmpArray = Misc.getRandomValues(0, list.size() - 1, num).toArray(new Integer[num]);
		for (int i = 0; i < tmpArray.length; i++) {
			pos.add(list.get(tmpArray[i]));
		}
		return pos;
	}

	/**
	 * 获取指定坐标的一定数量格子范围内的随机可用坐标
	 * 
	 * @param sceneId 场景Id
	 * @param posx 指定坐标x
	 * @param posy 指定坐标y
	 * @param gridscale 格子范围（例如，如果为16，表示以上述坐标为中心、边长为32的正方形区域）
	 * @param num 返回的可用坐标个数
	 * @return
	 */
	public static java.util.List<GridPos> genPosInGridScale(long sceneId, int posx, int posy, int gridscale, int num, boolean radom) {

		java.util.List<GridPos> pos = new ArrayList<GridPos>();
		final Scene scene = SceneManager.getInstance().getSceneByID(sceneId);
		if (null == scene)
			return pos;

		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return pos;

		GridPos currentPos = new Position(posx, posy).toGridPos();

		java.util.List<GridPos> list = null;
		if(radom){
			list = getStandableGridsAroundRandom(scene, currentPos, gridscale);
		}else{
			list = getStandableGridsAround(scene, currentPos, gridscale);
		}

		if (null == list || list.isEmpty()){
			SceneManager.logger.error("genPosInGridScale error.posx:"+posx+"posy:"+posy+"gridscale:"+gridscale);
			return pos;
		}

		Integer[] tmpArray = Misc.getRandomValues(0, list.size() - 1, num).toArray(new Integer[num]);
		for (int i = 0; i < tmpArray.length; i++) {
			try {
				pos.add(list.get(tmpArray[i]));
			} catch (Exception e) {
			 SceneManager.logger.error("genPosInGridScale error.num:"+num+"list size:"+list.size(), e);
			}
		}
		return pos;
	}

	public static java.util.List<GridPos> getStandableGridsAround(Scene scene, GridPos grid, int space) {

		java.util.List<GridPos> list = new ArrayList<GridPos>();
		if (null == scene)
			return list;

		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return list;

		final int x0 = grid.getX() - space < 0 ? 0 : grid.getX() - space;
		final int y0 = grid.getY() - space < 0 ? 0 : grid.getY() - space;
		final int x1 = grid.getX() + space > mi.getWidth() ? mi.getWidth() : grid.getX() + space;
		final int y1 = grid.getY() + space > mi.getHighth() ? mi.getHighth() : grid.getY() + space;

		list = mi.getNoneBlockAndTouFangListBetweenXAndY(new GridPos(x0, y0), new GridPos(x1, y1));
		return list;
	}
	
	public static java.util.List<GridPos> getStandableGridsAroundRandom(Scene scene, GridPos grid, int space) {

		java.util.List<GridPos> list = new ArrayList<GridPos>();
		if (null == scene)
			return list;

		final BlockInfo mi = scene.getMapInfo().getBlockInfo();
		if (null == mi)
			return list;

		final int x0 = grid.getX() - space < 0 ? 0 : grid.getX() - space;
		final int y0 = grid.getY() - space < 0 ? 0 : grid.getY() - space;
		final int x1 = grid.getX() + space > mi.getWidth() ? mi.getWidth() : grid.getX() + space;
		final int y1 = grid.getY() + space > mi.getHighth() ? mi.getHighth() : grid.getY() + space;

		list = mi.getNoneBlockListBetweenXAndY(new GridPos(x0, y0), new GridPos(x1, y1));
		return list;
	}
	
	/**
	 * ！注意:禁止逻辑模块使用此类的方法，去使用util包里的MapUtil
	 * 根据场景id获得地图的id
	 * 
	 * @param sceneId
	 * @return
	 */
	public static int getBaseMapIdBySceneId(long sceneId){
		int param = (int) (Math.pow(2, 32) - 1);
		Long mapId = sceneId & param;
		
		return mapId.intValue();
	}
	public static boolean isStaticScene(long sceneId){
		
		return sceneId==getBaseMapIdBySceneId(sceneId);
	}
	public static boolean isDynamicScene(long sceneId){
		
		return !isStaticScene(sceneId);
	}
	
	/**
	 * ！注意:禁止逻辑模块使用此类的方法，去使用util包里的MapUtil
	 * 根据副本地图的id和管理器的counter来生成scene的id
	 * 
	 * @param baseMapID
	 * @param countor
	 * @return
	 */
	public static long genSceneIDByCountor(int baseMapID, int counter){
		long sceneId = convertInt2Long(counter);
		sceneId <<= 32;
		sceneId += baseMapID;
		
		return sceneId;
	}
	
	/**
	 * ！注意:禁止逻辑模块使用此类的方法，去使用util包里的MapUtil
	 * 将INT型数值安全转换成long型
	 * 实际就是(long)value
	 * @param iValue
	 * @return
	 */
	public static long convertInt2Long(int iValue){
		Integer intValue = new Integer(iValue);
		return intValue.longValue();
	}

}
