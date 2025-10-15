package fire.pb.cross;

import fire.pb.util.DateValidate;
import fire.pb.util.FireProp;

/**
 * 跨服常量
 * @author dc
 */
public class Constant {
	//去跨服
	public static final byte TO_CROSS = 1;
	//回原服
	public static final byte FROM_CROSS = 2;
	
	//跨服拷数据状态
	public static final short COPY_DATA_RESULT_PROGRESSING = 1;
	public static final short COPY_DATA_RESULT_SUCC = 2;
	public static final short COPY_DATA_RESULT_FAILED = 3;
	
	//跨服拷数据状态,所需要的最长时间
	public static final long CROSS_MAX_TIME = 29L*1000;
	
	//跨服拷贝的数据间隔时间，到期后下次跨服重新拷贝
	//public static final long CROSS_COPY_DATA_EXPIRE_TIME = DateValidate.hourMills * 2;
	public static final long CROSS_COPY_DATA_EXPIRE_TIME =  FireProp.getIntValue("sys", "sys.cross.copyexpire") * DateValidate.minuteMills;
	
	//人在跨服buff
	public static final int CROSS_SERVER_BUFF = 500443;
	
	//去跨服做什么    跨服战队赛
	public static final int GO_CROSS_RESON_FACTION_TEAM_BATTLE = 0;
	
	//跨服战队赛的地图和坐标
	public static final int PK_MAP_ID = 1426;
	public static final int PK_MAP_POSX = 36;
	public static final int PK_MAP_POSY = 37;
	
	
	//以下这些是协议  CommonDataTransfer 中的 dtype值 
	public static final int SendRoleInfo = 1001;
	public static final int SendRoleInfo_Rep = 1002;
	public static final int SendRoleData = 1003;
	public static final int FinishCopyRole = 1004;
}
