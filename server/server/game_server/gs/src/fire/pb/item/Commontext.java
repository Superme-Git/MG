package fire.pb.item;

public class Commontext {
	private Commontext() { throw new AssertionError("不能实例化");}
	public static final int BAG_PAGE_SIZE = 25;
	public static final int ADD_CAPACITY_SIZE = 5;
	public static final int MIN_DEPOT_SIZE = 50;       //仓库最小容量
	public static final int MAX_DEPOT_SIZE = 110;      //仓库最大容量 (此处表示在通用配置表中的ID)
	public static final int ADD_DEPOT_SIZE = 111;       //每次购买增加的格子数(此处表示在通用配置表中的ID)
	public static final int USE_DEPOT_LEVEL = 1;     //使用仓库的用户level
	public static final int DEPOT_MONEY_MAX = 141612; // 仓库金钱溢出
	public static final int BAG_MONEY_MAX = 141585; // 背包金钱溢出
	public static final int DROP_ITEM_SUCC = 141118;
	public static final int DROP_ITEM_FAIL = 141127;
	public static final int EQUIPITEM_LEVEL_NOT_SUIT = 141143;
	public static final int EQUIPITEM_SEX_NOT_SUIT = 141144;
	public static final int EQUIPITEM_MODEL_NOT_SUIT = 141145;
	public static final int DEPOT_MONEY_ALREADY_FULL = 131439; // 仓库金钱已经满了
	public static final int MIN_BAG_SIZE = 60;       //背包最小容量
	public static final int MAX_BAG_SIZE = 142;      //背包最大容量 (此处表示在通用配置表中的ID)
	public enum UseResult{ SUCC, SUCC_NODEL, FAIL, AWAIT } // 使用物品的三种结果,AWAIT暂时是等待场景服务器验证结果

	//无绝装备的区间
	public static final int WU_JUE_EQUIP_MIN = 49011;
	public static final int WU_JUE_EQUIP_MAX = 49126;
	
	//装备名字颜色的加权值
	public static final int WHITE_WEIGHT = 0;
	public static final  int GREEN_WEIGHT = 1;
	public static final  int BLUE_WEIGHT = 2;
	public static final  int PURPLE_WEIGHT = 3;
	public static final  int ORANGE_WEIGHT = 4;
	public static final  int DARK_GOLD_WEIGHT = 5;
	
	public static final int WHITE_ID = 1;
	public static final  int GREEN_ID = 2;
	public static final  int BLUE_ID = 3;
	public static final  int PURPLE_ID = 4;
	public static final  int ORANGE_ID = 5;
	public static final  int DARK_GOLD_ID = 6;
	
	public static final int CONS  = 10;
	public static final int IQ    = 20;
	public static final int STR   = 30;
	public static final int ENDU  = 40;
	public static final int AGI   = 50;
	
	public static String ADD_ITEM_REASON = "捉鬼队长奖励物品";
	public static String ADD_ITEM_REASON1 = "捉鬼任务额外奖励物品"; 
	
	
}
