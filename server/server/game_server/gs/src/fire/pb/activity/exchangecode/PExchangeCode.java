package fire.pb.activity.exchangecode;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.util.FireProp;
import gnet.DataBetweenAuAnyAndGS;
import gnet.DeliveryManager;

/**
 * 兑换码
 * 
 * @author user
 *
 */
public class PExchangeCode extends Procedure {
	private Logger logger = Logger.getLogger(getClass());

	private long roleid;
	private int userid;
	private String exchangeCode;

	private static final String requestURL = FireProp.getStringValue(ConfigManager.getInstance().getPropConf("sys"), "sys.takegiftcode.url");

	public PExchangeCode(long roleid, int userid, String exchangeCode) {
		super();
		this.roleid = roleid;
		this.exchangeCode = exchangeCode;
		this.userid = userid;
	}

	@Override
	protected boolean process() throws Exception {
		logger.info(roleid + "收到兑换码:" + this.exchangeCode);
		if (this.exchangeCode.length() != 8) {
			return false;
		}

		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userid);
		if (userinfo == null) {
			logger.error("兑换码userId对应的user不存在");
			return false;
		}
		
		long time = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append(ConfigManager.getGameKey()).append(ConfigManager.getGameId()).append(roleid)
		.append(Gs.serverid).append(userinfo.getChid()).append(exchangeCode).append(time);

		String sign = fire.pb.http.util.LocalHttpUtils.getMD5Str(sb.toString());
		sb.setLength(0);
		sb.append(requestURL).append("?GameId=").append(ConfigManager.getGameId()).append("&RoleId=").append(roleid)
		.append("&ServerId=").append(Gs.serverid).append("&ChannelId=").append(userinfo.getChid())
		.append("&GiftCode=").append(exchangeCode).append("&Time=").append(time).append("&Sign=").append(sign);
		String requrestContent = sb.toString();

		logger.info(roleid + "字符串消息requrestContent=:" + this.exchangeCode);
		DataBetweenAuAnyAndGS ausnd = new DataBetweenAuAnyAndGS();
		ausnd.flag = DataBetweenAuAnyAndGS.GS_TO_AuAny;
		ausnd.qtype = 10001;
		ausnd.userid = userid;
		String url = requrestContent;
		ausnd.info = com.locojoy.base.Octets.wrap(url, "ISO-8859-1");
		DeliveryManager.getInstance().send(ausnd);
		logger.info(roleid + "发送兑换码消息到Au");

		// 测试用Json字符串
		// String result =
		// "{\"Code\":\"1\",\"Count\":\"3\",\"ActivityInfo\":\"MHwwfDB8MHwwfDA=\",\"Awards\":[{\"Award\":\"1|3|10000|1\"},{\"Award\":\"1|2|10000|2\"},{\"Award\":\"2|335013|3|1\"}]}";
		// System.out.println(result);
		return true; // new PExchangeCodeAuReturn(roleid,result).call();
	}
}