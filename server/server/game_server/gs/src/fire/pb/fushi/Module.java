

package fire.pb.fushi;

import java.util.HashMap;
import java.util.Map;

import xbean.AUUserInfo;
import fire.pb.main.ConfigManager;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.util.FireProp;

public class Module implements ModuleInterface {
	
	public final static int CREDITPOINT_IN_EXCHANGE 	= 1; // 充值符石
	public final static int CREDITPOINT_IN_SALL_RARE 	= 2; // 出售珍品所获得金币
	public final static int CREDITPOINT_IN_SALL 		= 3; // 出售非珍品所获得金币
	public final static int CREDITPOINT_IN_WORLD_BONUS 	= 4; // 获取世界红包
	public final static int CREDITPOINT_IN_CLAN_BONUS 	= 5; // 获取公会红包
	public final static int CREDITPOINT_OUT_BUY 		= 6; // 购买非珍品消耗金币
	public final static int CREDITPOINT_OUT_BUY_RARE 	= 7; // 购买珍品消耗金币
	public final static int CREDITPOINT_ONBORN 			= 8; // 初始信用值
	public final static int CREDITPOINT_IN_FS2GOLD		= 9; // 符石兑换金币
	public final static int CREDITPOINT_IN_OPENGOLDBOX	= 10;// 开氪金箱子获得金币	根据金子算
	public final static int CREDITPOINT_IN_OPENSILVERBOX= 11;// 开瑟银箱子获得金币	根据金子算
	public final static int CREDITPOINT_IN_BUYGOLDBOX	= 12;// 商城买金宝箱	根据金子算
	public final static int CREDITPOINT_IN_FSBUYGOLD	= 13;// 交易所符石买金	根据金子算
	public final static int CREDITPOINT_OUT_GOLD2FS		= 14;// 交易所金买符石	根据金子算
	public final static int CREDITPOINT_IN_DAYCOSTFUSHI	= 15;// 点卡服每日扣点卡	
	public final static int CREDITPOINT_IN_TEAM_BONUS	= 16;// 组队红包	根据获得的金币算
	public final static int CREDITPOINT_OUT_WORLD_BONUS = 17;// 发世界红包
	public final static int CREDITPOINT_OUT_CLAN_BONUS 	= 18;// 发公会红包
	public final static int CREDITPOINT_OUT_TEAM_BONUS	= 19;//  发 组队红包	根据获得的金币算	
	public final static int CREDITPOINT_RETURN_WORLD_BONUS  = 20;// 发送世界红包未领取返还
	public final static int CREDITPOINT_RETURN_CLAN_BONUS 	= 21;// 发送公会红包未领取返还
	public final static int CREDITPOINT_RETURN_TEAM_BONUS	= 22;//  发送组队红包未领取返还	
	public final static int CREDITPOINT_IN_GOLD_DRAGON		= 23;//  小金龙获得金币
	public final static int CREDITPOINT_IN_GOLD_HYD_BONUS	= 24;//  活跃度领取的1000金币
	public final static int CREDITPOINT_IN_RETURN_GOLD2FS	= 25; // 符石兑换金币(交易所金币单未成交返还)
	public final static int CREDITPOINT_OUT_CBG	= 26; // 挂单在藏宝阁
	public final static int CREDITPOINT_IN_CBG	= 27; // 从藏宝阁中获得
	public final static int CREDITPOINT_END	= 28;//结束其他的必须在此之前

	/**/
	
	public static Map<Integer, Double> CreditPointMap = new HashMap<Integer, Double>();

	@Override
	public void exit() {

	}

	@Override
	public void init() throws Exception {
		FushiManager.getInstance().init();
	}

	@Override
	public ReloadResult reload() throws Exception {
		FushiManager.logger.info("fushi reload start!");
		FushiManager.getInstance().initChargeGoodList();
		initCreditPoint();
		FushiManager.logger.info("fushi reload success!");
		return new ReloadResult(true);
	}
	
	public static final int PayServerType = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.payserver.type");

	/***
	 * 得到付费服务器类型 0是普通; 1是点卡 by changhao
	 * @return
	 */
	public static int GetPayServiceType() {return PayServerType;}

	public static fire.pb.fushi.SVipInfoConfig getVipInfoConfig(int level)
	{
		return fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SVipInfoConfig.class).get(level);
	}
	
	public static void initCreditPoint() 
	{
		CreditPointMap.clear();
		for(int i = CREDITPOINT_IN_EXCHANGE; i < CREDITPOINT_END; i++)
		{
			int index = i+PayServerType*100;
			SCreditPoint creditPoint = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.fushi.SCreditPoint.class).get(index);
			if(creditPoint != null)
				CreditPointMap.put(i, creditPoint.eventvalue);
			else
				FushiManager.logger.info("load CreditPoint info error! index:"+index+"的信息找不到！");
		}
	}

	public static float getCreditPointValue(int nType)
	{
		if(CreditPointMap.containsKey(nType))
			return CreditPointMap.get(nType).floatValue();
		return 0;		
	}

	
	public static int getVipTableRight(long roleid, int nType)
	{
		xbean.Vipinfo vipinfo = xtable.Vipinfo.get(roleid);
		if (null == vipinfo) {
			vipinfo = xbean.Pod.newVipinfo();
			xtable.Vipinfo.insert(roleid, vipinfo);
		}
		if(vipinfo.getViplevel() > 0)
		{
			SVipInfoConfig cfg = getVipInfoConfig(vipinfo.getViplevel());
			if(cfg != null)
				return cfg.viprights.get(nType);
		}		
		return 0;
	}
	
	public static int getVipTableRightOfLevel(int type, int level)
	{
		if(level > 0)
		{
			SVipInfoConfig cfg = getVipInfoConfig(level);
			if(cfg != null)
				return cfg.viprights.get(type);
		}		
		return 0;
	}
	
	/**
	 * 返回是否是应用宝用户
	 * @param userid
	 * @return
	 */
	public static boolean getIsYYBUser(int userid) {
		AUUserInfo auUserInfo = xtable.Auuserinfo.select(userid);
		if (auUserInfo == null) {
			FushiManager.logger.info(new StringBuilder().append("userid:").append(userid)
					.append(",is null"));
			return false;
		}
		return auUserInfo.getPlatname().equals("yingyongbao");
	}
	
/*	public static int getVipUsedRight(long roleid, int nType)
	{
		if(nType != SSendVipInfo.RIGHT_DAILY_GOODS_COUNT && nType != SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT )
			return 0;

		xbean.Vipinfo vipinfo = xtable.Vipinfo.get(roleid);
		if (null == vipinfo) {
			vipinfo = xbean.Pod.newVipinfo();
			xtable.Vipinfo.insert(roleid, vipinfo);
		}
			
		if(nType == SSendVipInfo.RIGHT_DAILY_GOODS_COUNT)
			return vipinfo.getDailygoodscount();
		if(nType == SSendVipInfo.RIGHT_WEEKLY_DISCOUNT_COUNT)
			return vipinfo.getWeeklydiscountcount();
		return 0;
	}
*/
}

