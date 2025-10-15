package fire.pb.scene;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;

/**
 * 地图上的数据信息， 只用来描述地图的基本数据
 * 
 */
public class BlockInfo {
	private static final Logger LOG = Logger.getLogger("MAPMAIN");
	// private final int MIN_FILE_SIZE = 12; // 地图文件头数据大小
	private final int MAX_WH_SIZE = 2048; // 地图最大宽高

	public final static int FLOOR_0_Z = 0;
	public final static int FLOOR_1_Z = 1;

	public final static short NONE_BLOCK = 0x0000; // 无阻挡
	public final static short WALK_BLOCK = 0x0001; // 行走阻挡
	public final static short FLY_BLOCK = 0x0002; // 飞行阻挡
	public final static short CHANNEL_BLOCK = 0x0004; // 传送点
	public final static short WALK_1_BLOCK = 0x0008; // 1层行走阻挡
	public final static short JUMP_FIRST_BLOCK = 0x0100; // 一阶轻功阻挡点（一阶跃不过去，二阶可以）
	public final static short JUMP_SECOND_BLOCK = 0x0200;// 二阶轻功阻挡点（二阶跃不过去，三阶可以）
	public final static short JUMP_THIRD_BLOCK = 0x0400; // 三阶轻功阻挡点（三阶跃不过去，三阶二段跳可以）
	public final static short JUMP_ALL_BLOCK = 0x0800; // 全轻功阻挡点 （全不可跃过去）
	public final static short ALL_BLOCK = (short) 0xffff; // 全阻挡

	private final int mapID; // 地图ID
	// private final int mazeID; // 属于地图的哪个maze
	private int width = 0; // 地图坐标宽度
	private int highth = 0; // 地图坐标高度
	private short[] info = null; // 坐标阻挡信息，从(0,0)按行连续存储
	final List<GridPos> list = new ArrayList<GridPos>();
	final List<GridPos> toufangList = new ArrayList<GridPos>();
	boolean hasToufangArea = false;
	final List<GridPos> toufang2List = new ArrayList<GridPos>();
	boolean hasToufang2Area = false;
	final List<GridPos> pickupList = new ArrayList<GridPos>();
	final Map<Integer, AreaInfo> areaInfos = new HashMap<Integer, AreaInfo>();

	private Map<Integer, GotoInfo> channels = new HashMap<Integer, GotoInfo>();

	public final int getWidth() {
		return width;
	}

	public final int getHighth() {
		return highth;
	}

	public BlockInfo(int mapID) {
		this.mapID = mapID;
	}

	// 生成非阻挡点的list,在SceneManager对mapinfo初始化的时候调用
	void genNoneBlockList() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHighth(); y++) {
				if ((!checkBlock(x, y, BlockInfo.WALK_BLOCK)) && (!checkBlock(x, y, BlockInfo.CHANNEL_BLOCK))) {
					list.add(new GridPos(x, y));
				}
			}
		}
		// 策划要求,打乱,防止两个随机npc刷的非常靠近的情况
		Collections.shuffle(list);
		Collections.shuffle(list);
		Collections.shuffle(list);
	}

	// 生成非阻挡点的list,在SceneManager对mapinfo初始化的时候调用
	void genToufangPosList() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHighth(); y++) {
				if (checkCanToufang(x, y)) {
					toufangList.add(new GridPos(x, y));
					toufang2List.add(new GridPos(x, y));
				}
				if (checkTnsh(x, y)) {
					pickupList.add(new GridPos(x, y));
				}
				if (checkToufang2(x, y)) {
					toufang2List.add(new GridPos(x, y));
				}
			}
		}
		if (!toufangList.isEmpty()) {
			hasToufangArea = true;
		}
		// 策划要求,打乱,防止两个随机npc刷的非常靠近的情况
		Collections.shuffle(toufangList);
		Collections.shuffle(toufangList);
		Collections.shuffle(pickupList);
		Collections.shuffle(pickupList);
	}

	// 是否是可以投放物品或npc的点
	public boolean checkCanToufang(int x, int y) {
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isToufangArea() && (!checkBlock(x, y, BlockInfo.WALK_BLOCK));
		return false;
	}

	// 新增的投放2区,包含了1级轻功区域
	public boolean checkToufang2(int x, int y) {
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isToufang2Area() && (!checkBlock(x, y, BlockInfo.WALK_BLOCK));
		return false;
	}

	// 是否是可以摆摊的点
	public boolean checkCanBaitan(int x, int y) {
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isBaitanArea();
		return false;
	}

	// 是否是可以切磋的点
	public boolean checkCanQiecuo(int x, int y) {
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isQiecuoArea();
		return false;
	}

	// 是否是可以放风筝的点
	public boolean checkCanKite(int x, int y) {
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isKite();
		return false;
	}

	// 是否是天女散花的点
	public boolean checkTnsh(int x, int y) {
		if (checkBlock(x, y, BlockInfo.WALK_BLOCK)) {
			return false;
		}
		AreaInfo areaInfo = areaInfos.get((x << 16) + y);
		if (areaInfo != null)
			return areaInfo.isTnsh();
		return false;
	}

	/**
	 * 得到某个范围内的所有非阻挡点，如果超出地图范围则取边缘
	 * 
	 * @param leftTop
	 *            左上角
	 * @param rightLow
	 *            右下角
	 */
	public List<GridPos> getNoneBlockListBetweenXAndY(GridPos leftTop, GridPos rightLow) {
		List<GridPos> noneBlockList = new ArrayList<GridPos>();

		for (int x = leftTop.getX(); x < rightLow.getX(); x++) {
			for (int y = leftTop.getY(); y < rightLow.getY(); y++) {
				if (!checkBlock(x, y, BlockInfo.WALK_BLOCK)) {
					noneBlockList.add(new GridPos(x, y));
				}
			}
		}
		return noneBlockList;
	}

	/**
	 * 有的玩法要求不能把npc投放的孤岛上,用这个方法,可以保证不会投放到孤岛上
	 */
	public List<GridPos> getNoneBlockAndTouFangListBetweenXAndY(GridPos leftTop, GridPos rightLow) {
		List<GridPos> noneBlockList = new ArrayList<GridPos>();

		for (int x = leftTop.getX(); x < rightLow.getX(); x++) {
			for (int y = leftTop.getY(); y < rightLow.getY(); y++) {
				if (!checkBlock(x, y, BlockInfo.WALK_BLOCK)) {
					if (hasToufangArea) {
						if (checkCanToufang(x, y))
							noneBlockList.add(new GridPos(x, y));
					} else
						noneBlockList.add(new GridPos(x, y));
				}
			}
		}
		return noneBlockList;
	}

	public List<GridPos> getNoneBlockList() {
		return list;
	}

	// 获取该地图上可以投放物品或npc的点的集合
	public List<GridPos> getToufangList() {
		return toufangList;
	}

	public List<GridPos> getToufang2List() {
		return toufang2List;
	}

	public List<GridPos> getPickupList() {
		return pickupList;
	}

	public String getMapFileName() {
		/*
		 * String id = (mazeID == 0)? "" : String.valueOf(mazeID); return
		 * "gamedata/map/" + mapID + "/maze"+id+".dat";
		 */
		return "gamedata/map/" + mapID + "/maze.dat";
	}

	public String getJumpBlockFileName() {
		return "gamedata/map/" + mapID + "/JumpBlock.dat";
	}

	public String getAreaFileName() {
		return "gamedata/map/" + mapID + "/regiontypeinfo.dat";
	}

	/**
	 * @param x, y 坐标点
	 * @return 当前坐标上的阻挡信息
	 */
	private final short getPosBlock(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < highth) {
			final int pos = y * width + x;

			if (pos < info.length) {
				return info[pos];
			}
		}

		return BlockInfo.ALL_BLOCK;
	}

	/**
	 * 将地图某位置设置为跳转点
	 * 
	 * @param x
	 *            格子横坐标
	 * @param y
	 *            格子纵坐标
	 */
	private void setPosChannelBlock(int x, int y) {
		if (x < width && y < highth) {
			final int pos = y * width + x;

			if (pos < info.length) {
				info[pos] = BlockInfo.CHANNEL_BLOCK;
			}
		}
	}

	/**
	 * @param x, y Grid坐标点
	 * @param blockType 指定的阻挡点类型
	 * @return 当前坐标上是否为指定的阻挡点
	 */
	public boolean checkBlock(int x, int y, short blockType) {
		final short bt = getPosBlock(x, y);
		return 0x00 != (blockType & bt);
	}

	public final boolean checkBlock(GridPos grid, short blockType) {
		return checkBlock(grid.getX(), grid.getY(), blockType);
	}

	public final boolean checkBlock(Position pos, short blockType) {
		return checkBlock(pos.getX() / GridPos.GRID_WIDTH, pos.getY() / GridPos.GRID_HIGHTH, blockType);
	}
	
	public final boolean checkBaitan(GridPos grid) {
		return  checkCanBaitan(grid.getX(), grid.getY());
	}

	/**
	 * 有容忍的判断阻挡，判断某个点是否 阻挡时，如果周围有点非阻挡，则此点也可以通过
	 */
	final boolean checkTolerableBlock(Position pos, short blockType) {

		final GridPos grid = pos.toGridPos();
		if (grid.getX() < 0 || grid.getY() < 0 || grid.getX() >= width || grid.getY() >= highth)
			return false;
		if (!checkBlock(grid, blockType))
			return false;

		// 以下是对阻挡进行容忍判断
		return !checkAdjacentPosThrough(pos, blockType);
	}

	/**
	 * 	对阻挡点进行容忍判断，因为当前在客户端对路径进行平滑处理
	 *  的过程中，有可能会导致一个Position(对应客户端的两个像素)
	 *  的偏差，因此一旦被认为是阻挡，那么对周围临界的Position进
	 *  行判断，只需要验证上下左右两个像素的4个点即可
	 *  但下面<图-a>的情形要仔细考虑，布阻挡时请注意
	 *   	  _ 					 _ _
	 *  	 |_|_			 		|_|_|_
	 *  	  /|_|_			=>        |_|_|_
	 *  	 /  /|_|                    |_|_|
	 *		/  /
	 *		 图-a						图-b
	 *  延着<图-a>两处斜线方向前进，在该处理是容忍通过的
	 */ 
	private boolean checkAdjacentPosThrough(Position blockPos, short blockType) {

		int x = 0;
		int y = 0;

		// 验证左边两个像素的点
		x = (blockPos.getX() - 2) / GridPos.GRID_WIDTH;
		y = blockPos.getY() / GridPos.GRID_HIGHTH;
		if (x > 0) {
			if (!checkBlock(x, y, blockType))
				return true;
		}

		// 验证右边两个像素的点
		x = (blockPos.getX() + 2) / GridPos.GRID_WIDTH;
		y = blockPos.getY() / GridPos.GRID_HIGHTH;
		if (!checkBlock(x, y, blockType)) {
			return true;
		}

		// 验证上边两个像素的点
		x = blockPos.getX() / GridPos.GRID_WIDTH;
		y = (blockPos.getY() - 2) / GridPos.GRID_HIGHTH;
		if (y > 0) {
			if (!checkBlock(x, y, blockType))
				return true;
		}
		// 验证下边两个像素的点
		x = blockPos.getX() / GridPos.GRID_WIDTH;
		y = (blockPos.getY() + 2) / GridPos.GRID_HIGHTH;
		if (!checkBlock(x, y, blockType)) {
			return true;
		}

		return false;
	}

	/**
	 * 检测两点间是否畅通（线段）
	 * 
	 * @param src
	 *            起点
	 * @param dest
	 *            终点
	 * @param blockType
	 *            阻挡类型 行走/飞行
	 * @return 是否畅通
	 */
	private boolean checkThrough(Position src, Position dest, short blockType) {

		// 先检测两个点
		if (checkTolerableBlock(src, blockType))
			return false;

		if (checkTolerableBlock(dest, blockType))
			return false;

		// 中间点一定要进行检查，即便src,dest在同一个格子里
		final Position mid = src.getMidPos(dest);
		if (checkTolerableBlock(mid, blockType))
			return false;

		if (mid.checkInSameGrid(src) || mid.checkInSameGrid(dest))
			return true;

		// 递归两端
		return checkThrough(src, mid, blockType) && checkThrough(mid, dest, blockType);
	}

	/**
	 * 检查给定的点集合是否构成一条通路
	 * 
	 * @param pos
	 *            点集合
	 * @param blockType
	 *            阻挡类型
	 * @return 是否畅通
	 */
	public boolean checkWay(final List<Position> pos, short blockType) {

		final int sz = pos.size();

		if (sz <= 1)
			return true;

		for (int i = 0; i < sz - 1; i++) {
			if (!checkThrough(pos.get(i), pos.get(i + 1), blockType))
				return false;
		}

		return true;
	}

	public boolean existMazeFile() {
		File mazefile = new File(getMapFileName());
		return mazefile.exists();
	}

	/**
	 * 加载地图数据 主要解析阻挡信息文件
	 */
	public void loadMap() {
		try {
			byte[] mazes = loadMaze();
			if (mazes == null) {
				Scene.LOG.error("地图 ： " + mapID + " 的maze.dat不存在");
				return;
			}
			Map<Integer, Byte> jumpblocks = loadJumpBlock();
			
			/*
			byte[] jumpblocks = loadDataFile(getJumpBlockFileName());
			if(jumpblocks == null)
				jumpblocks = new byte[mazes.length];
			if(mazes.length != jumpblocks.length)
				Scene.logger.error("地图 ： " + mapID + " 的maze.dat和JumpBlock.dat大小不一致。");
			*/
			
			info = new short[mazes.length];
			for (int i = 0; i < mazes.length; i++) {
				int x = i % width;
				int y = i / width;
				int xy = (x << 16) + y;
				Byte jumpblock = jumpblocks.get(xy);
				if (jumpblock == null)
					jumpblock = 0;
				info[i] = (short) (ByteToShort(jumpblock) << 8 | ByteToShort(mazes[i]));
			}

		} catch (final Exception e) {
			LOG.error(e);
		}
	}

	public byte[] loadMaze() {
		String filename = getMapFileName();
		byte[] blocks = null;
		try {
			File file = new File(filename);
			if (!file.exists())
				return blocks;
			final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

			// 此处读整数要按字节翻转
			final int headLen = Integer.reverseBytes(in.readInt());

			width = Integer.reverseBytes(in.readInt());
			highth = Integer.reverseBytes(in.readInt());
			blocks = new byte[width * highth];
			if (-1 == width || -1 == highth || width > MAX_WH_SIZE || highth > MAX_WH_SIZE) {
				in.close();
				throw new Exception("初始化地图文件数值错误" + filename);
			}

			final int ret = in.read(blocks);
			if (-1 == ret) {
				in.close();
				throw new Exception("初始化地图文件阻挡信息有误" + filename);
			}

			in.close();

			LOG.debug("minLen\t" + headLen + "\twidth\t" + width + "\thighth\t" + highth + "\t");
		} catch (final Exception e) {
			LOG.error("加载地图" + mapID + "的maze.dat时出错", e);
		}
		return blocks;
	}

	public Map<Integer, Byte> loadJumpBlock() {
		Map<Integer, Byte> infos = new HashMap<Integer, Byte>();

		String filePath = getJumpBlockFileName();
		File f = new File(filePath);
		if (!f.exists())
			return infos;
		try {

			final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));

			// 此处读整数要按字节翻转
			Integer.reverseBytes(in.readInt()); // QUYU

			Integer.reverseBytes(in.readInt()); // width
			Integer.reverseBytes(in.readInt()); // height

			final int count = Integer.reverseBytes(in.readInt());
			for (int i = 0; i < count; i++) {
				int pos = Integer.reverseBytes(in.readInt());
				byte val = in.readByte();
				infos.put(pos, val);
			}
			in.close();
			return infos;

		} catch (final Exception e) {
			LOG.error("加载地图" + mapID + "的JumpBlock.dat时出错", e);
			return new HashMap<Integer, Byte>();
		}
	}

	/**
	 * 加载区域数据 主要解析一些特殊区域,比如切磋,摆摊,投放区域
	 */
	public void loadArea() {
		Map<Integer, Short> infos = new HashMap<Integer, Short>();
		String filePath = getAreaFileName();
		File f = new File(filePath);
		if (!f.exists())
			return;
		if (f.length() <= 0)
			return;
		try {

			final DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));

			// 此处读整数要按字节翻转
			Integer.reverseBytes(in.readInt()); // QUYU

			Integer.reverseBytes(in.readInt()); // width
			Integer.reverseBytes(in.readInt()); // height

			final int count = Integer.reverseBytes(in.readInt());
			for (int i = 0; i < count; i++) {
				int pos = Integer.reverseBytes(in.readInt());
				short val = Short.reverseBytes(in.readShort());
				// byte val = in.readByte();
				infos.put(pos, val);
			}
			in.close();

		} catch (final Exception e) {
			e.printStackTrace();
			LOG.info(new StringBuffer().append(e).append(" ").append(filePath));
		}
		for (Entry<Integer, Short> entry : infos.entrySet()) {
			short value = entry.getValue();
			boolean isQiecuo = ((value & 0x0001) == 1 ? true : false);
			boolean isTnsh = ((value & 0x0002) == 2 ? true : false);
			boolean isToufang = ((value & 0x0010) == 16 ? true : false);
			boolean isToufang2 = ((value & 0x0040) == 64 ? true : false);
			boolean isBaitan = ((value & 0x0004) == 4 ? true : false);
			boolean isKite = ((value & 0x0008) == 8 ? true : false);
			Set<Short> areaTypes = new HashSet<Short>(8);
			for (short i = 10; i <= 16; i++) {
				if ((value & (short) (0x0001 << i)) == 0)
					continue;
				areaTypes.add((short) (i + 1));
			}
			AreaInfo areaInfo = new AreaInfo(isBaitan, isQiecuo, isToufang, isToufang2, isKite, isTnsh, areaTypes);
			areaInfos.put(entry.getKey(), areaInfo);
		}
	}

	private String getGotoFileName() {
		return "gamedata/map/" + mapID + "/goto.xml";
	}

	/**
	 * 地图文件中设置关于跳转点的描述
	 */
	@SuppressWarnings("unchecked")
	public void loadChannels() {

		final XStream s = new XStream(new DomDriver());
		s.alias("data", List.class);
		s.alias("item", GotoInfo.class);
		s.useAttributeFor(GotoInfo.class, "posx");
		s.useAttributeFor(GotoInfo.class, "posy");
		s.useAttributeFor(GotoInfo.class, "destmap");
		s.useAttributeFor(GotoInfo.class, "destx");
		s.useAttributeFor(GotoInfo.class, "desty");

		try {
			final List<GotoInfo> gotoItems = (List<GotoInfo>) s.fromXML(new java.io.FileInputStream(getGotoFileName()));

			for (GotoInfo item : gotoItems) {
				// Scene.logger.debug("加载跳转点\t" + item.toString());
				setPosChannelBlock(item.getX(), item.getY());

				channels.put((item.getX() << 16) + item.getY(), item);
			}

		} catch (final java.io.FileNotFoundException e) {
			// logger.info("从"+getGotoFileName()+"加载跳转点信息失败");
		}

	}

	GotoInfo getGotoInfo(int x, int y) {
		return channels.get((x << 16) + y);
	}

	public Map<Integer, AreaInfo> getAreaInfos() {

		return areaInfos;
	}

	/**
	 * 此坐标是否在地图的范围内
	 */
	public boolean isInMapScale(Position pos) {
		if (pos.getX() < 0 || pos.getX() >= getWidth() * GridPos.GRID_HIGHTH)
			return false;
		if (pos.getY() < 0 || pos.getY() >= getHighth() * GridPos.GRID_HIGHTH)
			return false;
		return true;
	}

	/**
	 * 此坐标是否在地图的范围内
	 */
	public boolean isInMapScale(GridPos gpos) {
		if (gpos.getX() < 0 || gpos.getX() >= getWidth())
			return false;
		if (gpos.getY() < 0 || gpos.getY() >= getHighth())
			return false;
		return true;
	}

	/**
	 * 获取以r为半径的，周围一圈Grid坐标 注意，只有最外圈的 常用于以某个点为中心，遍历周围坐标时
	 * 
	 * @param GridPos
	 *            gpos: 中央GridPos
	 * @param int
	 *            r:半径，为1时等同于getAdjacentGridPos
	 */
	public List<GridPos> getAroundGridPoses(GridPos cgpos, int r) {
		// TODO 要不要限制r的大小？？
		final List<GridPos> ret = new ArrayList<GridPos>();
		if (r > 500)
			return ret;
		int leftx = cgpos.getX() - r;
		int rightx = cgpos.getX() + r;
		int upy = cgpos.getY() - r;
		int downy = cgpos.getY() + r;

		// 上面一行
		for (int x = leftx; x < rightx; x++) {
			GridPos gpos = new GridPos(x, upy);
			if (isInMapScale(gpos))
				ret.add(gpos);
		}
		// 右侧一列
		for (int y = upy; y < downy; y++) {
			GridPos gpos = new GridPos(rightx, y);
			if (isInMapScale(gpos))
				ret.add(gpos);
		}
		// 下面一行
		for (int x = rightx; x > leftx; x--) {
			GridPos gpos = new GridPos(x, downy);
			if (isInMapScale(gpos))
				ret.add(gpos);
		}
		// 左侧一列
		for (int y = downy; y > upy; y--) {
			GridPos gpos = new GridPos(leftx, y);
			if (isInMapScale(gpos))
				ret.add(gpos);
		}
		return ret;
	}

	private void getMids(Position src, Position dest, List<Position> ret) {
		if (src.checkInSameGrid(dest))
			return;

		final Position mid = src.getMidPos(dest);
		if (mid.checkInSameGrid(src) || mid.checkInSameGrid(dest))
			return;

		// 跟起点不在同一格继续查找
		if (!mid.checkInSameGrid(src))
			getMids(src, mid, ret);

		ret.add(mid);

		// 跟终点不在同一格继续查找
		if (!mid.checkInSameGrid(dest))
			getMids(mid, dest, ret);
	}

	public void getLinePositions(Position src, Position dest, List<Position> ret) {
		// 把起点加入
		ret.add(src);
		if (src.checkInSameGrid(dest))
			return;

		// 获取起点到终点的所有中间点
		getMids(src, dest, ret);

		// 添加终点
		ret.add(dest);
	}

	/**
	 * 按位把byte转化为short，不保留值的正确，只保留位的正确性
	 */
	public static short ByteToShort(byte b) {
		int tmp = 1;
		short s = 0;
		for (int i = 0; i < 8; i++) {
			if ((tmp & b) != 0)
				s = (short) (s | tmp);
			tmp = tmp << 1;
		}
		return s;
	}
}
