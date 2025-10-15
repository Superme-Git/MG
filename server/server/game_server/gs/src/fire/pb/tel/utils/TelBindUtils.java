package fire.pb.tel.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import fire.pb.SBindTel;
import fire.pb.SBindTelAgain;
import fire.pb.SCheckCodeFinishTime;
import fire.pb.SCheckCodeFinishTimePoint;
import fire.pb.SGetBindTel;
import fire.pb.SGetBindTelAward;
import fire.pb.SUnBindTel;
import fire.pb.common.SCommon;
import fire.pb.game.BindTelAwardData;
import fire.pb.game.SBindTelAward;
import fire.pb.game.SPointCardBindTelAward;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.http.util.LocalHttpUtils;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.DateValidate;
import fire.pb.util.MessageUtil;
import gnet.link.Onlines;

/**
 * 手机绑定
 * 
 * @author yangtao
 * @dateTime 2016年8月15日 下午10:10:10
 * @version 1.0
 */
public class TelBindUtils {

	// 日志
	private static final Logger LOG = Logger.getLogger("SYSTEM");

	// 是否开启手机绑定验证
	public static boolean isOpenBindTelValidate = true;

	// 是否是内网发验证码
	public static boolean isTesSendMsg = false;

	/**
	 * 验证码长度
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:13:20
	 * @version 1.0
	 */
	public static final int CHECK_CODE_LEN = 6;

	// 发送验证码倒计时时长（毫秒）
	public static final int CHECK_CODE_TIME = 60000;

	// 时间误差
	public static final int CHECK_CODE_WUCHA = 2000;

	// 短信内容前缀
	private static final String codeStartWith = "您已申请《梦幻西游MG》手机绑定，此次验证码为：";

	// 发送短信验证码锁
	private static final Object sendChcekLock = new Object();

	/**
	 * 发送短信访问的地址
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:33:43
	 * @version 1.0
	 */
	private static final String SYS_SENDMSG_URL = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.sendmsg.url");

	/**
	 * 语音短信地址
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月11日 上午10:10:06
	 * @version 1.0
	 */
	private static final String SYS_SENDVOICEMSG_URL = ConfigManager.getInstance().getPropConf("sys")
			.getProperty("sys.sendvoicemsg.url");

	/**
	 * 手机绑定地址
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:33:43
	 * @version 1.0
	 */
	private static final String SYS_BINDTEL_URL = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.bindtel.url");

	// 获取手机绑定对用的角色数量
	private static final String GET_BINDTEL_ROLEID_NUM_FUN = "/bind/get_count_bymobile";

	// 手机绑定功能
	private static final String BINDTEL_ROLEID_FUN = "/bind/set";

	// 解除手机绑定
	private static final String UNBINDTEL_ROLEID_FUN = "/bind/del";

	// gameKey
	private static final String SYS_SENDMSG_GAMEKEY = ConfigManager.getInstance().getPropConf("sys")
			.getProperty("sys.sendmsg.gamekey");

	// 玩家对应的手机验证码信息
	public static Map<Long, Map<Long, CodeBean>> roleId2TelCodeBeanMap = new ConcurrentHashMap<Long, Map<Long, CodeBean>>();

	// 普通服角色对应的手机绑定奖励信息
	private static Map<Integer, SBindTelAward> mulDayLoginRewardConf = ConfigManager.getInstance().getConf(SBindTelAward.class);

	// 点卡服角色对应的手机绑定奖励信息
	private static Map<Integer, SPointCardBindTelAward> mulDayLoginRewardDianConf = ConfigManager.getInstance().getConf(
			SPointCardBindTelAward.class);

	/**
	 * 七天内是否绑定了手机,超出七天限制拍卖行和兑换所的使用
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午3:24:35
	 * @version 1.0
	 * @param roleid
	 * @return
	 */
	public static boolean isLimitByBindTel(long roleid) {
		if (!isOpenBindTelValidate) {
			return false;
		}
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			if (prop.getIsbindtelagain() == 1 || isBindTelAgain(prop)) {
				SBindTelAgain sBindTelAgain = new SBindTelAgain();
				gnet.link.Onlines.getInstance().send(roleid, sBindTelAgain);
				StringBuilder sbd = new StringBuilder();
				sbd.append("role=").append(roleid).append(",需要重新手机绑定确认");
				LOG.error(sbd.toString());
				return true;
			}
			if (prop.getBindtel() <= 0) {
				MessageMgr.sendMsgToPop(roleid, "需要您先去【基础设置】里进行【手机安全】");
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否需要重新绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 上午9:46:20
	 * @version 1.0
	 * @param prop
	 * @return
	 */
	private static boolean isBindTelAgain(xbean.Properties prop) {
		xbean.ServiceSet serviceSet = xtable.Servicesets.select(1);
		if (serviceSet == null) {
			return false;
		}
		// 要求绑定的次数
		int num = serviceSet.getBindtelagain();
		// 实际绑定的次数
		int bindtelagainnum = prop.getBindtelagainnum();
		if (bindtelagainnum < num && prop.getCreatetime() <= serviceSet.getBindtelagaintime()) {
			return true;
		}
		return false;
	}

	/**
	 * 是否存在相同的手机绑定並且需要重新綁定
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 上午9:46:20
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @return
	 */
	private static boolean isExistBindTel(long roleId, long tel) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			// 之前绑定过，如果是需要重新绑定就不需要从平台获取验证数量了
			if (prop.getBindtel() == tel) {
				if (prop.getIsbindtelagain() == 1 || isBindTelAgain(prop)) {
					LOG.info("需要重新手机关联:" + roleId);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 得到手机绑定信息
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月16日 下午2:37:55
	 * @version 1.0
	 * @param roleid
	 */
	public static void getBindTelInfoProcedure(long roleid) {
		mkdb.Procedure getBindTelInfo = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					if (prop.getIsbindtelagain() != 1) {
						// 是否需要重新绑定手机
						if (isBindTelAgain(prop)) {
							prop.setIsbindtelagain(1);
							MessageMgr.psendSystemMessageToRole(roleid, 191021, null);
						}
					}
					SGetBindTel sGetBindTel = new SGetBindTel(prop.getBindtel(), prop.getCreatetime(),
							(byte) (prop.getLogintimesofday() > 1 ? 0 : 1), (byte) prop.getIsgetbindtelaward(),
							(byte) prop.getIsbindtelagain(), prop.getBindteltime());
					gnet.link.Onlines.getInstance().send(roleid, sGetBindTel);
				}
				return true;
			}
		};
		getBindTelInfo.submit();
	}

	/**
	 * 手机绑定功能处理
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月30日 下午2:32:04
	 * @version 1.0
	 * @param roleId
	 * @param tel 手机号
	 * @param flag 标志（1 获取手机绑定2手机绑定3解除绑定）
	 */
	public static void telProcess(long roleId, long tel, int flag) {
		telProcess(roleId, tel, null, flag);
	}

	/**
	 * 手机绑定功能处理
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月30日 下午2:32:04
	 * @version 1.0
	 * @param roleId
	 * @param tel 手机号
	 * @param code 验证码
	 * @param flag 标志（1 获取手机绑定2手机绑定3解除绑定）
	 */
	public static void telProcess(long roleId, long tel, String code, int flag) {
		String serverkey = Gs.serverid;
		String roleid = String.valueOf(roleId);
		switch (flag) {
		case TelState.GET_BINDTEL: {
			// 之前绑定过，如果是需要重新绑定就不需要从平台获取验证数量了
			if (isExistBindTel(roleId, tel)) {
				sendMsgProcedure(roleId, tel, System.currentTimeMillis(), true);
				return;
			}
			// 组织url数据
			final String buildurl = String.format("http://%1$s%2$s?mobile=%3$s", SYS_BINDTEL_URL, GET_BINDTEL_ROLEID_NUM_FUN, tel);
			HttpGet request = new HttpGet(buildurl);
			// 发送得到手机绑定
			Gs.getHttpClient().execute(request, new GetBindTelHttpHandler(roleId, tel));
		}
			break;
		case TelState.BINDTEL: {
			if (bindTel(roleId, tel, code)) {
				// 之前绑定过，如果是需要重新绑定就不需要更新平台数量了
				if (isExistBindTel(roleId, tel)) {
					bindTelProcedure(roleId, tel, code);
					return;
				}
				// 组织url数据
				final String buildurl = String.format("http://%1$s%2$s?mobile=%3$s&serverid=%4$s&roleid=%5$s", SYS_BINDTEL_URL,
						BINDTEL_ROLEID_FUN, tel, serverkey, roleid);
				HttpGet request = new HttpGet(buildurl);
				// 发送手机绑定
				Gs.getHttpClient().execute(request, new BindTelHttpHandler(roleId, tel, code));
			} else {
				SBindTel sGetBindTel = new SBindTel();
				sGetBindTel.status = 0;
				sGetBindTel.bindteltime = 0;
				gnet.link.Onlines.getInstance().send(roleId, sGetBindTel);
			}
		}
			break;
		case TelState.UNBINDTEL: {
			if (isUnBindTel(roleId, tel, code)) {
				// 组织url数据
				final String buildurl = String.format("http://%1$s%2$s?mobile=%3$s&serverid=%4$s&roleid=%5$s", SYS_BINDTEL_URL,
						UNBINDTEL_ROLEID_FUN, tel, serverkey, roleid);
				HttpGet request = new HttpGet(buildurl);
				// 发送解除绑定
				Gs.getHttpClient().execute(request, new UnBindTelHttpHandler(roleId, tel));
			} else {
				SUnBindTel sGetBindTel = new SUnBindTel();
				sGetBindTel.status = 0;
				gnet.link.Onlines.getInstance().send(roleId, sGetBindTel);
			}
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 手机解除绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月16日 下午2:28:46
	 * @version 1.0
	 * @param roleid
	 */
	private static void unbindTelProcedure(long roleid, long tel) {
		mkdb.Procedure cUnBindTel = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					SUnBindTel sGetBindTel = new SUnBindTel();
					if (prop.getBindtel() > 0l) {
						prop.setBindtel(0l);
						java.util.List<Long> unbindteltimeList = prop.getUnbindteltime();
						unbindteltimeList.add(System.currentTimeMillis());
					}
					// 清除数据
					clearData(roleid);
					sGetBindTel.status = 1;
					mkdb.Procedure.psendWhileCommit(roleid, sGetBindTel);
				}
				return true;
			}
		};
		cUnBindTel.submit();
	}

	/**
	 * 是否能解除绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 下午7:40:09
	 * @version 1.0
	 * @param roleid
	 * @param tel
	 * @return
	 */
	public static boolean isUnBindTel(long roleid, long tel, String code) {
		xbean.Properties prop = xtable.Properties.select(roleid);
		if (prop != null) {
			long bindTelTime = prop.getBindteltime();
			if (bindTelTime == 0 || System.currentTimeMillis() - bindTelTime < DateValidate.weekMills) {
				MessageMgr.sendMsgNotify(roleid, 191016, null);
				LOG.info("七天后才能解除绑定:" + roleid);
				return false;
			}
			long bindTel = prop.getBindtel();
			if (tel != bindTel || tel == 0) {
				MessageMgr.sendMsgNotify(roleid, 191015, null);
				LOG.info("输入的手机号与绑定的手机号不匹配:" + roleid + "输入手机号：" + tel + "绑定手机号：" + bindTel);
				return false;
			}
			CodeBean cb = CheckCodeUtils.getCodeBean(roleid, tel);
			if (cb == null) {
				return false;
			}
			if (code == null || !cb.code.equals(code.trim())) {
				MessageMgr.sendMsgNotify(roleid, 191014, null);
				LOG.info("验证码不对" + roleid);
				return false;
			}
		}
		return true;
	}

	/**
	 * 得到奖励
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午3:19:57
	 * @version 1.0
	 * @param roleid
	 */
	public static void getBindTelAwardProcedure(long roleid) {
		mkdb.Procedure cBindTel = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					SGetBindTelAward sGetBindTelAward = new SGetBindTelAward();
					sGetBindTelAward.status = 0;
					// 只要绑定过手机一次就能领取奖励
					if (prop.getIsgetbindtelaward() == 2) {
						boolean isSucesss = TelBindUtils.getBindTelReward(roleid);
						if (isSucesss) {
							prop.setIsgetbindtelaward(1);
							sGetBindTelAward.status = 1;
						}
					}
					mkdb.Procedure.psendWhileCommit(roleid, sGetBindTelAward);
				}
				return true;
			}
		};
		cBindTel.submit();
	}

	/**
	 * 得到绑定手机奖励
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午8:20:55
	 * @version 1.0
	 * @param roleId
	 * @return
	 */
	private static boolean getBindTelReward(long roleId) {
		// 人物形象
		Integer roleShape = xtable.Properties.selectShape(roleId);
		Integer rewardIndex = roleShape - 1;
		BindTelAwardData mulDayLoginGift = null;
		// 获取奖励数据
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) { // 普通服
			SBindTelAward cfg = mulDayLoginRewardConf.get(1);
			if (cfg == null)
				return false;
			mulDayLoginGift = cfg.getRewardvec().get(rewardIndex);
		} else {
			SPointCardBindTelAward cfg = mulDayLoginRewardDianConf.get(1);
			if (cfg == null)
				return false;
			mulDayLoginGift = cfg.getRewardvec().get(rewardIndex);
		}
		if (mulDayLoginGift == null)
			return false;
		Pack itemBag = new Pack(roleId, false);
		int emptyVolume = itemBag.getFreepos().size();
		if (emptyVolume < 1) {
			// 失败，空间不足
			MessageMgr.sendMsgNotify(roleId, 141095, Arrays.asList(String.valueOf(1)));
			LOG.error("手机绑定 reward error:背包空间不足  roleid:" + roleId + " freePos:" + emptyVolume);
			return false;
		}
		boolean res1 = addItem(roleId, mulDayLoginGift.getItem1id(), mulDayLoginGift.getItem1num(), 0);
		if (!res1) {
			// 添加物品失败
			LOG.error("手机绑定 reward error:添加物品失败1  roleid:" + roleId);
		}
		return true;
	}

	/**
	 * 添加物品
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午7:56:12
	 * @version 1.0
	 * @param roleId
	 * @param itemIdStr
	 * @param itemNum
	 * @param itemBind
	 * @return
	 */
	private static boolean addItem(long roleId, String itemIdStr, int itemNum, int itemBind) {
		if (itemIdStr == null) {
			return false;
		}
		int itemId = Integer.valueOf(itemIdStr);
		if (itemId != 0 && itemNum > 0) {
			// 添加物品
			if (itemBind == 0) {
				if (itemNum != BagUtil.addItem(roleId, itemId, itemNum, "bindTel",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_bind_tel, itemId)) {
					MessageMgr.sendMsgNotify(roleId, 144965, null);
					return false;
				}
			} else {
				if (itemNum != BagUtil.addBindItem(roleId, itemId, itemNum, "bindTel",
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_bind_tel, itemId, true)) {
					MessageMgr.sendMsgNotify(roleId, 144965, null);
					return false;
				}
			}
			MessageUtil.psendAddItemWhileCommit(roleId, itemId, itemNum);
		}
		return true;
	}

	/**
	 * 手机关联
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 上午8:37:05
	 * @version 1.0
	 * @param roleid
	 * @param tel
	 * @param code
	 * @param isTelCheck
	 */
	private static void bindTelProcedure(long roleid, long tel, String code, boolean isTelCheck) {
		mkdb.Procedure cBindTel = new mkdb.Procedure() {

			@Override
			protected boolean process() {
				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop != null) {
					long bindTime = System.currentTimeMillis();
					SBindTel sGetBindTel = new SBindTel();
					sGetBindTel.status = 1;
					sGetBindTel.bindteltime = bindTime;
					prop.setBindtel(tel);
					if (!isTelCheck)
						prop.setBindteltime(bindTime);
					if (prop.getIsbindtelagain() == 1)
						prop.setBindtelagainnum(setBindTelAgainNum());
					prop.setIsbindtelagain(0);
					if (prop.getIsgetbindtelaward() == 0) {
						// 可领奖状态
						prop.setIsgetbindtelaward(2);
					}
					// 清除数据
					clearData(roleid);
					mkdb.Procedure.psendWhileCommit(roleid, sGetBindTel);
				}
				return true;
			}
		};
		cBindTel.submit();
	}

	/**
	 * 手机绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月16日 下午2:31:15
	 * @version 1.0
	 * @param roleid
	 * @param tel
	 * @param code
	 */
	private static void bindTelProcedure(long roleid, long tel, String code) {
		bindTelProcedure(roleid, tel, code, true);
	}

	/**
	 * 手机绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:41:29
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @return
	 */
	private static boolean bindTel(long roleId, long tel, String code) {
		CodeBean cb = CheckCodeUtils.getCodeBean(roleId, tel);
		if (cb == null) {
			return false;
		}
		if (code == null || !cb.code.equals(code.trim())) {
			MessageMgr.sendMsgNotify(roleId, 191014, null);
			LOG.info("验证码不对" + roleId);
			return false;
		}
		if (cb.bindTel != tel || tel == 0) {
			MessageMgr.sendMsgNotify(roleId, 191013, null);
			LOG.info("手机号与验证码不对应(换了手机号)" + roleId);
			return false;
		}
		return true;
	}

	/**
	 * 发送验证码
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月16日 下午2:33:45
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @param currentTime
	 * @param isVoice
	 */
	private static void sendMsgProcedure(long roleid, long tel, long currentTime, boolean isVoice) {
		// 验证绑定
		boolean isSucess = TelBindUtils.sendMsg(roleid, tel, currentTime, isVoice, SendCheckCodeType.BINDTEL);
		if (!isSucess) {
			SCheckCodeFinishTime sGetBindTel = new SCheckCodeFinishTime();
			sGetBindTel.finishtimepoint = 0;
			gnet.link.Onlines.getInstance().send(roleid, sGetBindTel);
		}
	}

	/**
	 * 发送验证码
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:41:29
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @param isVoice
	 * @return
	 */
	public static boolean sendMsg(long roleId, long tel, long currentTime, boolean isVoice, int type) {
		String telStr = String.valueOf(tel);
		// 手机号正则表达式验证
		if (!regexMobileNum(telStr)) {
			MessageMgr.sendMsgNotify(roleId, 191006, null);
			LOG.info("手机号格式不对" + roleId);
			return false;
		}
		String code = null;
		synchronized (sendChcekLock) {
			CodeBean cb = CheckCodeUtils.getCodeBeanNoMessage(roleId, tel);
			if (cb != null) {
				long finishTime = cb.finishTime;
				if (currentTime - finishTime < 0) {
					MessageMgr.sendMsgNotify(roleId, 191032, null);
					LOG.info("发送验证码CD内" + roleId);
					return false;
				}
			}
			// 创建验证码
			code = createCheckCode();
			saveData(roleId, currentTime, code, tel);
		}
		// 内网使用
		if (isTesSendMsg) {
			MessageMgr.sendMsgToPop(roleId, "验证码：" + code);
		}
		CodeBean cb = CheckCodeUtils.getCodeBeanNoMessage(roleId, tel);
		switch (type) {
		case SendCheckCodeType.BINDTEL:
			// 发送数据
			SCheckCodeFinishTime sCheckCodeFinishTime = new SCheckCodeFinishTime(cb.finishTime);
			gnet.link.Onlines.getInstance().send(roleId, sCheckCodeFinishTime);
			break;
		case SendCheckCodeType.GOODS_SAFE_LOCK:
			// 发送数据
			SCheckCodeFinishTimePoint sForceDelCheckCodeFinishTime = new SCheckCodeFinishTimePoint(
					(byte) SendCheckCodeType.GOODS_SAFE_LOCK, cb.finishTime);
			gnet.link.Onlines.getInstance().send(roleId, sForceDelCheckCodeFinishTime);
			break;
		case SendCheckCodeType.CBG_UP:
			break;
		default:
			break;
		}
		// 组织数据
		HttpPost request = sendMobileMsg(code, telStr, isVoice);
		// 发送短信息消息
		Gs.getHttpClient().execute(request, new SendMsgHttpHandler(roleId, code));
		return true;
	}

	/**
	 * 保存数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午6:37:03
	 * @version 1.0
	 * @param roleId
	 * @param currentTime
	 * @param code
	 */
	public static void saveData(long roleId, long currentTime, String code, long bindTel) {
		CodeBean cb;
		// 缓存数据
		cb = new CodeBean();
		cb.code = code;
		cb.createTime = currentTime;
		// 得到发送验证码CD
		int codeCD = getSendCheckCodeCD();
		cb.finishTime = currentTime + codeCD * 1000;
		cb.bindTel = bindTel;
		Map<Long, CodeBean> tel2CbMap = roleId2TelCodeBeanMap.get(roleId);
		if (tel2CbMap == null) {
			tel2CbMap = new ConcurrentHashMap<Long, CodeBean>();
			tel2CbMap.put(bindTel, cb);
			roleId2TelCodeBeanMap.put(roleId, tel2CbMap);
		} else {
			tel2CbMap.put(bindTel, cb);
		}
	}

	/**
	 * 清除数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午11:05:15
	 * @version 1.0
	 * @param roleId
	 */
	public static void clearData(long roleId) {
		roleId2TelCodeBeanMap.remove(roleId);
	}

	/**
	 * 发送消息
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月17日 下午4:25:40
	 * @version 1.0
	 * @param code
	 * @param tel
	 * @param isVoice
	 * @return
	 */
	public static HttpPost sendMobileMsg(String code, String tel, boolean isVoice) {
		int gameId = ConfigManager.getGameId();
		// 发送目的 1 app内置功能 2 运营活动
		String type = "1";
		long time = System.currentTimeMillis() / 1000;
		String dataURLEncodeRst = null;
		if (isVoice) {
			dataURLEncodeRst = code;
		} else {
			StringBuffer codeAll = new StringBuffer();
			codeAll.append(codeStartWith).append(code);
			try {
				dataURLEncodeRst = URLEncoder.encode(codeAll.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		// 校验码
		StringBuffer signBuffer = new StringBuffer();
		signBuffer.append(SYS_SENDMSG_GAMEKEY).append(gameId).append(tel).append(type).append(time);
		String sign = LocalHttpUtils.getMD5Str(signBuffer.toString());
		String buildurl = null;
		if (isVoice) {
			// 组织url数据
			buildurl = String.format("%1$s?GameId=%2$s&Mobiles=%3$s&Msg=%4$s&Type=%5$s&Time=%6$s&Sign=%7$s", SYS_SENDVOICEMSG_URL,
					gameId, tel, dataURLEncodeRst, type, time, sign);
		} else {
			// 组织url数据
			buildurl = String.format("%1$s?GameId=%2$s&Mobiles=%3$s&Msg=%4$s&Type=%5$s&Time=%6$s&Sign=%7$s", SYS_SENDMSG_URL,
					gameId, tel, dataURLEncodeRst, type, time, sign);
		}
		HttpPost post = new HttpPost(buildurl);
		return post;
	}

	/**
	 * 创建验证码
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:13:50
	 * @version 1.0
	 * @return
	 */
	public static String createCheckCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < CHECK_CODE_LEN; i++) {
			double random = Math.random();
			// 避免第一位是0
			if (i == 0 && random < 0.1)
				random = 0.1;
			int rdNum = (int) (random * 10);
			sb.append(rdNum);
		}
		return sb.toString();
	}

	/**
	 * 手机号验证
	 *
	 * @param number
	 * @return
	 */
	private static boolean regexMobileNum(String number) {
		if (StrNULL(number)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(number);
		return matcher.find();
	}

	/**
	 * 非空验证
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:15:06
	 * @version 1.0
	 * @param str
	 * @return
	 */
	private static boolean StrNULL(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 回调
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:45:05
	 * @version 1.0
	 */
	public static class SendMsgHttpHandler extends HttpCallBackHandler {

		private final long roleId;

		private final String checkCode;

		public SendMsgHttpHandler(long roleId, String code) {
			this.roleId = roleId;
			this.checkCode = code;
		}

		@Override
		protected boolean process(JSONObject json) {
			String Code = json.getString("Code").trim();
			Integer code = Integer.parseInt(Code);
			if (code == 1) {
				MessageMgr.sendMsgNotify(roleId, 191001, null);
				LOG.info(checkCode + "发送短信验证码成功：" + roleId);
				return true;
			} else {
				switch (code) {
				case 2:
					LOG.info("Sign校验失败" + roleId);
					MessageMgr.sendMsgNotify(roleId, 191002, null);
					break;
				case 3:
					LOG.info("号码禁止发送" + roleId);
					MessageMgr.sendMsgNotify(roleId, 191003, null);
					break;
				case 4:
					LOG.info("短信内容中含有屏蔽词" + roleId);
					MessageMgr.sendMsgNotify(roleId, 191004, null);
					break;
				case 99:
					LOG.info("发送验证码短信其他错误" + roleId);
					MessageMgr.sendMsgNotify(roleId, 191005, null);
					break;
				default:
					LOG.info("发送验证码短信异常" + roleId);
					break;
				}
			}
			return false;
		}
	}

	/**
	 * 解除手机绑定回调
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:45:05
	 * @version 1.0
	 */
	private static class UnBindTelHttpHandler extends HttpCallBackHandler {

		private final long roleId;

		private final long tel;

		public UnBindTelHttpHandler(long roleId, long tel) {
			this.roleId = roleId;
			this.tel = tel;
		}

		@Override
		protected boolean process(JSONObject json) {
			String errno = json.getString("errno").trim();
			if (errno.equals("")) {
				unbindTelProcedure(roleId, tel);
			} else {
				LOG.error("数据收到出错" + roleId + "json:" + json);
				return false;
			}
			return true;
		}
	}

	/**
	 * 手机绑定回调
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:45:05
	 * @version 1.0
	 */
	private static class BindTelHttpHandler extends HttpCallBackHandler {

		private final long roleId;

		private final long tel;

		private final String checkCode;

		public BindTelHttpHandler(long roleId, long tel, String checkCode) {
			this.roleId = roleId;
			this.tel = tel;
			this.checkCode = checkCode;
		}

		@Override
		protected boolean process(JSONObject json) {
			String errno = json.getString("errno").trim();
			if (errno.equals("")) {
				bindTelProcedure(roleId, tel, checkCode, false);
			} else {
				LOG.error("数据收到出错" + roleId + "json:" + json);
				return false;
			}
			return true;
		}
	}

	/**
	 * 获取手机绑定回调
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月15日 下午10:45:05
	 * @version 1.0
	 */
	private static class GetBindTelHttpHandler extends HttpCallBackHandler {

		private final long roleId;

		private final long tel;

		public GetBindTelHttpHandler(long roleId, long tel) {
			this.roleId = roleId;
			this.tel = tel;
		}

		@Override
		protected boolean process(JSONObject json) {
			String errno = json.getString("errno").trim();
			if (errno.equals("")) {
				xbean.Properties prop = xtable.Properties.select(roleId);
				if (prop != null) {
					// 解除关联的时候发送验证码
					if (prop.getBindtel() == tel) {
						sendMsgProcedure(roleId, tel, System.currentTimeMillis(), false);
					} else {
						Integer num = Integer.parseInt(json.getString("data"));
						int maxBindRoleIdByTel = getMaxBindRoleIdByTel();
						if (maxBindRoleIdByTel != -1) {
							// 手机号正则表达式验证
							if (num >= maxBindRoleIdByTel) {
								List<String> paramsList = new ArrayList<>();
								paramsList.add(String.valueOf(maxBindRoleIdByTel));
								MessageMgr.sendMsgNotify(roleId, 191017, paramsList);
								LOG.error("每个手机最多绑定" + maxBindRoleIdByTel + "个角色" + roleId);
								return false;
							}
							sendMsgProcedure(roleId, tel, System.currentTimeMillis(), false);
						}
					}
				}
			} else {
				LOG.error("数据收到出错" + roleId + "json:" + json);
				return false;
			}
			return true;
		}
	}

	/**
	 * 得到全服一个手机最多绑定的角色数量
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月29日 下午5:10:22
	 * @version 1.0
	 * @return
	 */
	private static int getMaxBindRoleIdByTel() {
		SCommon conf = ConfigManager.getInstance().getConf(SCommon.class).get(437);
		if (conf == null) {
			LOG.error("通用数据表数据错误，不存在编号" + 437);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}

	/**
	 * 设置需要重新绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 上午9:19:01
	 * @version 1.0
	 */
	public static void setBindTelAgain() {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.ServiceSet serviceSet = xtable.Servicesets.get(1);
				if (serviceSet == null) {
					serviceSet = xbean.Pod.newServiceSet();
					xtable.Servicesets.insert(1, serviceSet);
				}
				// 设置手机关联确认次数和时间
				serviceSet.setBindtelagain(serviceSet.getBindtelagain() + 1);
				serviceSet.setBindtelagaintime(System.currentTimeMillis());
				// 给在线的所有角色发送禁止交易，需要手机绑定确认
				SBindTelAgain sBindTelAgain = new SBindTelAgain();
				Onlines.getInstance().broadcast(sBindTelAgain);
				return true;
			}
		}.submit();
	}

	/**
	 * 得到需要重新手机绑定的次数
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 下午4:36:54
	 * @version 1.0
	 * @return
	 */
	private static int setBindTelAgainNum() {
		xbean.ServiceSet serviceSet = xtable.Servicesets.select(1);
		if (serviceSet == null) {
			return 0;
		}
		return serviceSet.getBindtelagain();
	}

	/**
	 * 得到发送验证码CD
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 上午10:46:42
	 * @version 1.0
	 * @return
	 */
	private static int getSendCheckCodeCD() {
		SCommon conf = ConfigManager.getInstance().getConf(SCommon.class).get(450);
		if (conf == null) {
			LOG.error("通用数据表数据错误，不存在编号" + 450);
			return -1;
		}
		return Integer.parseInt(conf.getValue());
	}

	/**
	 * 设置取消重新绑定
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月1日 上午9:19:01
	 * @version 1.0
	 */
	public static void setUnBindTelAgain() {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				xbean.ServiceSet serviceSet = xtable.Servicesets.get(1);
				if (serviceSet == null) {
					serviceSet = xbean.Pod.newServiceSet();
					xtable.Servicesets.insert(1, serviceSet);
				}
				// 设置手机关联确认次数和时间
				serviceSet.setBindtelagain(serviceSet.getBindtelagain() - 1);
				serviceSet.setBindtelagaintime(System.currentTimeMillis());
				// 给在线的所有角色发送取消禁止交易
				SBindTel sGetBindTel = new SBindTel();
				sGetBindTel.status = 1;
				sGetBindTel.bindteltime = 0;
				Onlines.getInstance().broadcast(sGetBindTel);
				return true;
			}
		}.submit();
	}

	/**
	 * 通过gm指令进行手机关联
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午1:15:42
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @param state
	 */
	public static void bindTelByGM(long roleId, long tel) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			String serverkey = Gs.serverid;
			String roleid = String.valueOf(roleId);
			// 组织url数据
			final String buildurl = String.format("http://%1$s%2$s?mobile=%3$s&serverid=%4$s&roleid=%5$s", SYS_BINDTEL_URL,
					BINDTEL_ROLEID_FUN, tel, serverkey, roleid);
			HttpGet request = new HttpGet(buildurl);
			// 发送手机绑定
			Gs.getHttpClient().execute(request, new BindTelHttpHandler(roleId, tel, "123456"));
			bindTelProcedure(roleId, tel, "123456", false);
		}
	}

	/**
	 * 通过gm指令进行解除手机关联
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午1:15:42
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @param state
	 */
	public static void unBindTelByGM(long roleId) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			long tel = prop.getBindtel();
			if (tel <= 0l)
				return;
			String serverkey = Gs.serverid;
			String roleid = String.valueOf(roleId);
			// 组织url数据
			final String buildurl = String.format("http://%1$s%2$s?mobile=%3$s&serverid=%4$s&roleid=%5$s", SYS_BINDTEL_URL,
					UNBINDTEL_ROLEID_FUN, tel, serverkey, roleid);
			HttpGet request = new HttpGet(buildurl);
			// 发送解除绑定
			Gs.getHttpClient().execute(request, new UnBindTelHttpHandler(roleId, tel));
			unbindTelProcedure(roleId, tel);
		}
	}

	/**
	 * 通过gm指令进行改变手机关联
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月18日 下午1:15:42
	 * @version 1.0
	 * @param roleId
	 * @param tel
	 * @param state
	 */
	public static void changeBindTelByGM(long roleId, long tel) {
		unBindTelByGM(roleId);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bindTelByGM(roleId, tel);
	}
}
