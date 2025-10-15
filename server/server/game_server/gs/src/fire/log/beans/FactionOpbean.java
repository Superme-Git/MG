package fire.log.beans;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class FactionOpbean {
	// 操作类型 类型ID
	public static final int TYPE_CREATE = 5001;// 创建公会 5001
	public static final int TYPE_ADD = 5002;// 加入公会 5002
	public static final int TYPE_QUIT = 5003;// 退出公会 5003
	public static final int TYPE_KICK = 5004;// 踢出公会 5004
	public static final int TYPE_POSITION_UP = 5005;// 提升级别 5005
	public static final int TYPE_POSITION_DOWN = 5006;// 下降级别 5006
	public static final int TYPE_CHANGE_LEDER = 5007;// 转移会长 5007
	public static final int TYPE_CHANGE_AIM = 5008;// 修改公会介绍 5008
	public static final int TYPE_DISMISS = 5009;// 解散公会 5009
	public static final int TYPE_LOBBY_UP = 5010;// 公会建筑大厅升级 5010
	public static final int TYPE_GOLD_UP = 5011;// 公会建筑金库升级 5011
	public static final int TYPE_DRUGSTONE_UP = 5012;// 公会建筑药店升级 5012
	public static final int TYPE_HOTEL_UP = 5013;// 公会建筑旅馆升级 5013

	private long factionkey;// 公会ID
	private String factionname;// 公会名称
	private int opid;// 公会操作（创建公会、加入公会、退出公会、请出公会、成为新帮主、升降会阶级别、修改帮会介绍）
	private int position;// 公会职别

	/*******
	 * 新增 *
	 *******/
	private int guiMemCnt; // 所在公会人数#
	private int guiLvl; // 公会等级#
	private Set<Entry<Integer, Integer>> buildLvl = new HashSet<Entry<Integer, Integer>>(); // 所在建筑物等级

	public FactionOpbean(long factionkey, String factionname, int opid, int position, int guiMemCnt, int guiLvl,
			Set<Entry<Integer, Integer>> buildLvl) {
		this.factionkey = factionkey;
		this.factionname = factionname;
		this.opid = opid;
		this.position = position;
		this.guiMemCnt = guiMemCnt;
		this.guiLvl = guiLvl;
		this.buildLvl.addAll(buildLvl);
	}

	public long getFactionkey() {
		return factionkey;
	}

	public String getFactionname() {
		return factionname;
	}

	public int getOpid() {
		return opid;
	}

	public int getPosition() {
		return position;
	}

	public int getGuiMemCnt() {
		return guiMemCnt;
	}

	public int getGuiLvl() {
		return guiLvl;
	}

	public Set<Entry<Integer, Integer>> getBuildLvl() {
		return buildLvl;
	}
}
