package fire.log;

public interface RemoteLogType{
	public final static int LV_CHANGE_LOGIN = 1;//游戏等级变化	
	public final static int MISSION_ACCEPT = 2;//任务(副本)接口
	public final static int MISSION_ABORT = 3;//放弃任务(副本)接口
	public final static int MISSION_FALSE = 4;//任务(副本)失败接口
	public final static int MISSION_COMPLET = 5;//任务(副本)完成接口
	public final static int MISSION_GATE = 6;//关卡(节点)接口
	public final static int EXCHANGE_LOGIN = 7;//充值数据	
	public final static int ITEM_CHANGE = 8;//虚拟道具获取/消耗
	public final static int MONEY_CHANGE = 9;//代币获取
	public final static int EVENT = 10;//自定义事
}