package fire.pb.fushi.redpack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fire.pb.common.SCommon;
import fire.pb.fushi.SRedPackConfig;
import fire.pb.main.ConfigManager;
import fire.pb.util.StringConstant;


public class RedPackManage {
	public static final long  ONE_DAY = 1000 * 60 * 60*24;//一天的毫秒数
	
	public static final int CHANGE_NUM=100;//兑换比例
	
	public static long time=ONE_DAY;
	
	public static long getValidRedPackTime(){
		return time;
	}
	
	public static void setValidRedPackTime(long t){
		time=t*60*1000;
	}
	
	public static Map<Integer, SRedPackConfig> sRedPackConfig = ConfigManager.getInstance().getConf(SRedPackConfig.class);
	
	public static ConcurrentHashMap<String, String> rolegetredpackrecords=new ConcurrentHashMap<String, String>();//已经领取完的红包
	
	public static Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);//通用配置表
	
	/**
	 * 获得策划数据
	 * @param modeltype
	 * @return
	 */
	public static SRedPackConfig getSRedPackConfigdataByType(int modeltype){
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){
			return sRedPackConfig.get(modeltype+1000);
		}
		return sRedPackConfig.get(modeltype);
	}
	
	/**
	 * 数据错误默认写10%
	 * @return
	 */
	public static int getSendPackTax(){
		if (commonMap != null) {
			SCommon conf = commonMap.get(365);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 10;
	}
	
	
	
	/**
	 * 根据类型获得红包字符串
	 * @param type
	 * @return
	 */
	public static String getTypeStringByType(int type){
		if(type==RedPackType.TYPE_WORLD){
			return StringConstant.ID2String("", StringConstant.WORLD_REDPACK);
		}else if(type==RedPackType.TYPE_CLAN){
			return StringConstant.ID2String("", StringConstant.CLAN_REDPACK);
		}else{
			return StringConstant.ID2String("", StringConstant.TEAM_REDPACK);
		}
		
	}
	
	/**
	 * 红包是否是发送金币
	 * @param modeltype
	 * @return
	 */
	public static boolean isSendGold(int modeltype){
		if(fire.pb.fushi.Module.GetPayServiceType() == 1){//点卡服务器
			return true;
		}else{
			return false;
		}
	}
	
	
}
