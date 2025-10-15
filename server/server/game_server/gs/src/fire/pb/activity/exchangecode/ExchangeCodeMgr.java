package fire.pb.activity.exchangecode;

import java.util.Properties;

import fire.pb.main.ConfigManager;
import fire.pb.util.FireProp;

/**
 * 兑换码管理器
 * author yebin @ 2016年6月20日
 */
public class ExchangeCodeMgr {
	private static Properties prop = ConfigManager.getInstance().getPropConf("sys");
	private static volatile boolean QQ_EXCHANGE_CODE_OPEN = FireProp.getBooleanValue(prop, "sys.exchangecode.qqvip");
	
	public static boolean isQQExchangeCodeOpen() {
		return QQ_EXCHANGE_CODE_OPEN;
	}
	public static void setQQExchangeCodeOpen(boolean isOpen) {
		QQ_EXCHANGE_CODE_OPEN = isOpen;
	}
}
