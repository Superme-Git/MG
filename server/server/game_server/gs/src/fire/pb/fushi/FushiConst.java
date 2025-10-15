
package fire.pb.fushi;

import java.util.Properties;	

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;

public class FushiConst {
	public static final int CASH_FUSHI = 0; //玩家用rmb充的现金符石
	public static final int SYS_FUSHI = 1;  //系统赠送给玩家的符石
	
	public static final String LOCOJOYPLAT = "ljpl"; //乐动平台

	public static final long SYS_FUSHI_ROLEID = 100L; //系统买卖符石时的默认roleid,跟普通roleid区分开来
	
	public static final int REASON_CASH_FUSHI = 1301;  //充值
	public static final int REASON_BUY_FUSHI = 1302;  //平台购买
	public static final int REASON_ADD_FUSHI_WANFA = 1303;  //玩法活动额外产出（如祈福）
	public static final int REASON_BUY_ITEM = 1304;  //购买物品
	public static final int REASON_SELL_FUSHI = 1305;  //平台出售
	public static final int REASON_SUB_FUSHI_WANFA = 1306;  //玩法活动额外扣除（如祈福）
	public static final int REASON_SUB_FUSHI_ENCHOULU = 1307;  //冰封王座
	public static final int REASON_SUB_FUSHI_MONEY_TREE = 1308;  //摇钱树
	public static final int REASON_SUB_FUSHI_VIP = 1310;  //vip购买额外次数
	public static final int REASON_SUB_FUSHI_PET_PRACTISE = 1311;//宠物训练消耗符石
	public static final int REASON_SUB_FUSHI_PET_ISLAND = 1312;//宠物迷城消耗符石
	public static final int REASON_SUB_FUSHI_PET_DEDUCT_SKILL = 1313;//一键摘除宠物技能
	public static final int REASON_SUB_FUSHI_PET_EXTENDS_SKILL = 1314;//宠物技能扩展格子
	public static final int REASON_SUB_FUSHI_UNFREEZE_FACTION_CONTRI = 1315;//解冻帮贡
	public static final int REASON_SUB_FACTION_FUSHI = 1316;  //帮派运镖
	public static final int REASON_SUB_EQUIP_RENEW = 1317;  //装备精练属性重铸
	public static final int REASON_SUB_FORCE_UNLOCK = 1318;  //强行解锁
	public static final int REASON_SUB_FACTION_CREATE_TEAM = 1320;  //帮派战队
	public static final int REASON_SUB_SHENMISHOP_GOUMAI = 1321;  //神秘商店购买物品
	public static final int REASON_SUB_SHENMISHOP_UPDATE = 1322;  //更新神秘商店
	public static final int REASON_SUB_CAMPlEADER_SENDMSG = 1323;  //阵营领袖群发消息
	public static final int REASON_SUB_FANFANLE_BUYCOUNT = 1324;   //翻翻乐活动购买游戏次数
	public static final int REASON_SUB_FANFANLE_PLAYCOUNT = 1325;  //翻翻乐活动购买额外翻牌次数
	public static final int REASON_SUB_FANFANLE_REFRESH = 1326;    //翻翻乐活动刷新奖励
	public static final int REASON_SUB_LABA_FEE = 1327;    //全服喊话扣除费用
	public static final int REASON_SUB_ITEM_BUYBAGGRID = 1328;   //购买仓库格子
	public static final int REASON_SUB_WEDDING_BLESS = 1329;   //金色祝福花费
	public static final int REASON_SUB_WEDDING_REDPACKAGE = 1330;   //婚礼发红包
	public static final int REASON_SUB_SWORN = 1331; //申请结拜符石花费
	public static final int REASON_SUB_DISMISS_APPRENCE = 1332;//师傅罢黜徒弟
	public static final int REASON_SUB_REMOVE_OUTDATE = 1333;//日本版删除过去符石
	public static final int REASON_SUB_CHANGE_CAMP = 1334;//更改阵营	
	public static final int REASON_SUB_SEND_FACTIONMESSAGE = 1335;//发布帮派群消息
	public static final int REASON_SUB_FACTION_CHANGLONG_STAR = 1336;//发布帮派群消息
	public static final int REASON_SUB_TIGER_BET = 1337;//老虎机下注	
	public static final int REASON_RENXING_CT = 1338;  //任性循环任务
	public static final int REASON_SUB_CHANGEFACTIONNAME = 1339;  //更改公会名称
	public static final int REASON_SUB_CHANGESCHOOL = 1340; //转职
	
	public static final int REASON_SUB_GM_CHARGEBACK = 1999; //GM扣除退单用户符石
	public static final int REASON_SUB_EXCHANGE_CURRENCY = 2000; //货币兑换
	public static final int REASON_SUB_SEND_REDPACK = 2001; //发红包
	public static final int REASON_SUB_SELL_SPOTCARD = 2002; //出售点卡
	public static final int REASON_BUY_DAYPAY = 2003;  //购买日卡 by changhao
	public static final int REASON_CREATE_CLAN = 2004;  //创建公会
	
	public static final int REASON_BUY_MONTHCARD = 2006;  //购买月卡  by changhao
	
	public static final int REASON_YYB_SYNC_SUB = 3004;  //本地跟应用宝同步符石
	
	public static final int REASON_GM_DEC = 4001;  // 平台通过GM扣除	
	
	public static Properties prop = ConfigManager.getInstance().getPropConf("fushi");
	public static final int FEN_DUAN_1 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan1");
	public static final int FEN_DUAN_2 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan2");
	public static final int FEN_DUAN_3 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan3");
	public static final int FEN_DUAN_4 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan4");
	public static final int FEN_DUAN_5 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan5");
	public static final int FEN_DUAN_6 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan6");
	public static final int FEN_DUAN_7 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan7");
	public static final int FEN_DUAN_8 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan8");
	public static final int FEN_DUAN_9 = FireProp.getIntValue(prop, "mtgserv.fushi.fenduan9");

	public static final int FUSHI_DAY_LIMIT = 400000;
}
