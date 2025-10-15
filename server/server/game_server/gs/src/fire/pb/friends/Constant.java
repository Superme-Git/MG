package fire.pb.friends;

import fire.pb.util.FireProp;

public interface Constant {
	
	//系统id
	public static final int SYSTEM_ID = 0;
	
	//删除或者重命名“我的好友”是不被允许的
	public static final int RENAME_NO_ALLOWED = 140868;
	
	public static final int FRIEND_GROUP_NUM_TOTAL = 7;
	public static final int FRIEND_GROUP_NUM_TOTAL_MSG_ID = 140879;
	
	//征友页面每页显示的人数
	public static final int PAGE_INFO_MAX = 10;
	//收徒最小级别
	public static final int RECRUIT_DISCIPLE_LEVEL_MIN = 50;
	//征婚最小级别
	public static final int FIND_COMPANION_LEVEL_MIN = 40;
	//征友最小级别
	public static final int FIND_FRIEND_LEVEL_MIN = 10;
	//拜师最小级别
	public static final int FIND_MASTER_LEVEL_MIN = 10;
	//拜师最大级别
	public static final int FIND_MASTER_LEVEL_MAX = 79;
	
	//是否进行名字反转
	public static final int REVERSE_NAME_2_ID = FireProp.getIntValue("sys", "sys.role.name2idreverse");
	
	//离线消息超时时间(单位是天)
	public static final int OFFLINE_MSG_TIME_OUT_DAY = 90; 

}
