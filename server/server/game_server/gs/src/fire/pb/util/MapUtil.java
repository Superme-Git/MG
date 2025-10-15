
package fire.pb.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fire.pb.map.GridPos;
import fire.pb.map.MoveInfo;
import fire.pb.map.Position;
import fire.pb.scene.AreaInfo;
import fire.pb.scene.BlockInfo;
import fire.pb.scene.MapInfo;



public class MapUtil {
	
	
	public static int getBaseMapIdBySceneId(long sceneId){
		int param = (int) (Math.pow(2, 32) - 1);
		Long mapId = sceneId & param;
		
		return mapId.intValue();
	}
	
	
	public static long genSceneIDByCountor(int baseMapID, int counter){
		long sceneId = convertInt2Long(counter);
		sceneId <<= 32;
		sceneId += baseMapID;
		
		return sceneId;
	}
	
	
	public static long convertInt2Long(int iValue){
		Integer intValue = new Integer(iValue);
		return intValue.longValue();
	}
	
	
	
	public static List<Integer> checkEnterRegion(int prevmapId, int prevposx, int prevposy, int mapId, int posx, int posy)
	{
		return getRegionIdsByMapArea(mapId, checkChangeArea(prevmapId, prevposx, prevposy, mapId, posx, posy));
	}
	
	public static List<Integer> checkLeaveRegion(int prevmapId, int prevposx, int prevposy, int mapId, int posx, int posy)
	{//与进入反向
		return checkEnterRegion(mapId, posx, posy, prevmapId, prevposx, prevposy);
	}
	
	
	private static List<Short> checkChangeArea(int prevmapId, int prevposx, int prevposy, int mapId, int posx, int posy)
	{
		List<Short> enterAreas = getAreaTypes(mapId, posx, posy);
		if(enterAreas.isEmpty())
			return enterAreas;
		
		//对比过去的
		if(prevmapId != mapId)
			return enterAreas;//如果是跨地图的，肯定触发进入区域
		
		List<Short> oldAreas =  getAreaTypes(mapId, prevposx, prevposy);
		if(oldAreas.isEmpty())
			return enterAreas;//该区域没有特殊id
		
		for(Iterator<Short> it = enterAreas.iterator(); it.hasNext();)
		{
			if(oldAreas.contains(it.next()))
				it.remove();//如果上一个位置跟这个位置有同样的areaid，则不是进入
		}
		
		return enterAreas;
	}
	public static List<Integer> getRegionIds(int mapId, int posx, int posy)
	{
		return getRegionIdsByMapArea(mapId, getAreaTypes(mapId, posx, posy));
	}
	
	public static boolean isInRegion(int mapId, int posx, int posy, int regionId)
	{
		if(getMapIdByRegion(regionId) != mapId)
			return false;
		return getAreaTypes(mapId, posx, posy).contains((short)getAreaTypeByRegion(regionId));
	}
	
	public static boolean isInRegion2(int mapId, int posx, int posy, int regionId)
	{
		return getAreaTypes(mapId, posx, posy).contains((short)getAreaTypeByRegion(regionId));
	}
	
	
	public static AreaInfo getAreaInfo(int mapId, int gposx, int gposy)
	{
		BlockInfo blockinfo =  getBlockInfo(mapId);
		if(blockinfo == null)
			return null;
		return blockinfo.getAreaInfos().get((gposx << 16) + gposy);
	}
	
	
	public static MapInfo getMapInfo(int mapId)
	{
		return fire.pb.scene.SceneManager.getInstance().getMapInfo(mapId);
	}
	
	
	public static BlockInfo getBlockInfo(int mapId)
	{
		MapInfo mapinfo =getMapInfo(mapId);
		if(mapinfo == null)
			return null;
		return mapinfo.getBlockInfo();
	}
	
	
	public static List<Short> getAreaTypes(int mapId, int posx, int posy)
	{
		List<Short> areaTypes = new LinkedList<Short>();
		GridPos gpos = new Position(posx, posy).toGridPos();
		AreaInfo areaInfo = getAreaInfo(mapId, gpos.getX(), gpos.getY());
		if(areaInfo == null || areaInfo.getAreaTypes().isEmpty())
			return areaTypes;//该区域没有特殊id
		areaTypes.addAll(areaInfo.getAreaTypes());
		return areaTypes;
	}
	
	
	public static List<Integer> getRegionIdsByMapArea(int mapId, List<Short> areaTypes)
	{
		List<Integer> regionIds = new LinkedList<Integer>();
		for(short areaType : areaTypes)
		{
			regionIds.add(getRegionIdByMapArea(mapId, areaType));
		}
		return regionIds;
	}
	
	
	public static int getRegionIdByMapArea(int mapId, short areaType)
	{
		return mapId * 100 + areaType;
	}
	
	
	public static int getMapIdByRegion(int regionId)
	{
		return regionId / 100;
	}
	
	
	public static int getAreaTypeByRegion(int regionId)
	{
		return regionId % 100;
	}
	
	public static Position getNextPositionByCurrent(Position curPosition, MoveInfo moveInfo){
		Position start = moveInfo.getStartPoint();
		Position end = moveInfo.getEndPoint();
		int moveLength = moveInfo.getLength();
		int quadrant = moveInfo.getQuadrant();
		
		int xLength = Math.abs(start.getX() - end.getX());
		int yLength = Math.abs(start.getY() - end.getY());
		
		int slopeLength = end.getDistance(start);
		
		
		return getDesPostionBySimilarTriangle(curPosition, moveLength, quadrant, xLength, yLength, slopeLength);
		
	}

	private static Position getDesPostionBySimilarTriangle(Position curPosition, int moveLength, int quadrant, int xLength,
			int yLength, int slopeLength) {
		double scale = moveLength/slopeLength;
		int desX = getLengthByQuadrant(curPosition.getX(), quadrant, (int)(xLength*scale), true);
		int desY = getLengthByQuadrant(curPosition.getY(), quadrant, (int)(yLength*scale), false);
		
		return new Position(desX, desY);
	}

	private static int getLengthByQuadrant(int start, int quadrant, int addLength, boolean isX) {
		int result = 0;
		
		switch (quadrant) {
		case 1:
			if(isX){
				result += addLength;
			}else{
				result -= addLength;
			}

		case 2:
			result += addLength;
			
		case 3:
			if(isX){
				result -= addLength;
			}else{
				result += addLength;
			}
		case 4:
				result -= addLength;
			
		default:
			break;
		}
		
		result = result < 0 ? 0:result;
		
		return result;
	}
	
	
	public static boolean checkWay(int mapId, List<Position> poses, short blocktype)
	{
		BlockInfo blockinfo = getBlockInfo(mapId);
		if(blockinfo == null)
			return false;
		int z = -1;
		List<fire.pb.scene.sPos.Position> sceneposes = new LinkedList<fire.pb.scene.sPos.Position>();
		for(Position pos : poses)
		{
			if(z == -1)
				z = pos.getZ();
			else if(z != pos.getZ())
				return false;
			sceneposes.add(new fire.pb.scene.sPos.Position(pos.getX(),pos.getY(),pos.getZ()));
		}
		if(z == -1)
			return false;
		return blockinfo.checkWay(sceneposes, blocktype);
	}
	
}
