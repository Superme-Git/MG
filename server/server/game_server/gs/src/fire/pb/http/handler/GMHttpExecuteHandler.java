/**
 * 
 */
package fire.pb.http.handler;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fire.log.Logger;
import fire.pb.PropRole;
import fire.pb.blackmarket.srv.BlackMarketManager;
import fire.pb.compensation.CreateSingleCompensation;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.FushiManager;
import fire.pb.game.SRoleAccusation;
import fire.pb.gm.About360XuanShang;
import fire.pb.gm.GMInteface;
import fire.pb.http.manager.GMSeachManage;
import fire.pb.http.util.LocalHttpUtils;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.util.DateValidate;
import fire.pb.util.DecodeBase64;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * GM调用接口 http://127.0.0.1:29300/gm? http://xxx.com/yyy?SerialNum=xxx&MessageNum=yyy&MessageBody =zzz&Time=123&Sign=
 */
public class GMHttpExecuteHandler implements HttpHandler {
	public static Logger logger = Logger.getLogger("GMHTTP");

	static private Map<Integer, About360XuanShang> xuanShangConfig = ConfigManager.getInstance().getConf(About360XuanShang.class);

	private static final int FORBID_ACCOUNT = 10; // 封停账号
	private static final int UNFORBID_ACCOUNT = 11; // 解封账号
	private static final int NON_VOICE = 12; // 禁言
	private static final int UNNON_VOICE = 13; // 解除禁言
	private static final int BROADCAST_AND_POST = 20; // 滚屏公告/喇叭公告
	private static final int SINGLE_PLAYER_MAIL = 30; // 单角色邮件
	private static final int SOME_PLAYERS_MAIL = 31; // 群发邮件
	private static final int VERIFY_SPACE_PICTURE = 103; // 审核个人空间照片
	private static final int QUERY_ROLE_INFO = 104; // 查詢角色信息
	private static final int GM_SPACE_GIFT_GET = 105; // 得到空间大师剩余数量
	private static final int GM_SPACE_GIFT_SET = 106; // 空间大师数量设置
	private static final int CHECK_360_GET_GIFT = 107; // 查询领取条件（gamid=88,360悬赏活动专用）
	private static final int GET_360_GIFT = 108; // 按领取条件发奖（gamid=88, 360悬赏活动专用）
	private static final int CBG_CHECK_ITEM_SELL = 109; // 查询道具是否能出售（gamid=88, 藏宝阁专用）
	private static final int CBG_ITEM_SELL = 110; // 出售道具（gamid=88, 藏宝阁专用）
	private static final int COMMON_EXCE_GM = 111; // 通用的执行GM指令
	private static final int FPRBID_DEAL = 112; // 封交易
	private static final int BIND_TEL = 113; // 绑定手机号
	private static final int UNBIND_TEL = 114; // 解除绑定手机号
	private static final int CHANGE_TEL = 115; // 改变绑定手机号

	public static final int GM_ITEM_CURRENCY_START = 1; // 货币类型开始
	public static final int GM_ITEM_CURRENCY_SILVER = 1; // 银币
	public static final int GM_ITEM_CURRENCY_GOLD = 2; // 金币
	public static final int GM_ITEM_CURRENCY_FUSHI = 3; // 符石
	public static final int GM_ITEM_CURRENCY_EXP = 4; // 经验
	public static final int GM_ITEM_CURRENCY_VIPEXP = 5; // VIP经验
	public static final int GM_ITEM_CURRENCY_END = 5; // 货币类型结束

	/**
	 * http handler
	 */
	@Override
	public void handle(HttpExchange exchange) {
		try {
			JSONObject jsonObj = new JSONObject();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			final OutputStream os = exchange.getResponseBody();
			Map<String, String> params = LocalHttpUtils.getParameters(exchange);
			final URI uri = exchange.getRequestURI();
			final String path = uri.getPath();
			logger.info("localhttpserver url=" + path + " params=" + params);
			if (params == null) {
				setResponseInfo(jsonObj, 99);
				os.write(jsonObj.toString().getBytes());
				return;
			}

			String oper = params.get("Operate");
			// 如果有这个字段 说明是举报类型的 按举报类型的流程走
			if (oper != null) {
				int ret = checkParams(params);
				if (ret != 1) {
					jsonObj.accumulate("Code", ret);
					os.write(jsonObj.toString().getBytes());
					return;
				}

				long roleId = Long.parseLong(params.get("RoleId"));
				long time = Long.parseLong(params.get("Time"));
				int operate = Integer.parseInt(params.get("Operate")); // 1为添加,2或其他为删除
				String roleName = params.get("RoleName");

				new mkdb.Procedure() {
					@Override
					protected boolean process() throws Exception {
						xbean.Properties roleProp = xtable.Properties.select(roleId);
						if (roleProp == null || !roleName.equals(roleProp.getRolename())) {
							jsonObj.accumulate("Code", 5);
							return false;
						}

						xbean.AccusationInfo info = xtable.Accusationinfos.get(roleId);
						byte retVal = -1;
						if (operate == 1) {
							retVal = 1;
							if (info == null) {
								info = xbean.Pod.newAccusationInfo();
								xtable.Accusationinfos.insert(roleId, info);
							}
							info.setLasttime(time);
						} else {
							retVal = 0;
							if (info == null) {
								jsonObj.accumulate("Code", 11);
								return false;
							}
							xtable.Accusationinfos.delete(roleId);
						}
						psendWhileCommit(roleId, new SRoleAccusation(retVal));
						jsonObj.accumulate("Code", 1);
						return true;
					}
				}.submit().get(2, TimeUnit.SECONDS);
				os.write(jsonObj.toString().getBytes());
			} else {
				String serialNum = params.get("SerialNum");
				String messageNum = params.get("MessageNum");
				String messageBody = params.get("MessageBody");
				String time = params.get("Time");
				String sign = params.get("Sign");

				if (serialNum == null || messageNum == null || messageBody == null || time == null || sign == null) {
					setResponseInfo(jsonObj, 99);
					os.write(jsonObj.toString().getBytes());
					logger.error("param is = " + "error");
					return;
				}

				// 判断输入的参数是否正确
				if (serialNum.length() > 10 || serialNum.length() == 0) {
					return;
				}

				int messagenum = 0;
				long n64Time = 0;
				try {
					messagenum = Integer.parseInt(messageNum);
					n64Time = Long.parseLong(time);
				} catch (Exception e) {
					e.printStackTrace();
					os.write(jsonObj.toString().getBytes());
					logger.error("param is = " + "error");
					return;
				}

				// 加入md5的验证
				String signSource = ConfigManager.getGameKey() + ConfigManager.getGameId() + serialNum + n64Time;
				String compareSign = LocalHttpUtils.getMD5Str(signSource);
				// 先进行密匙验证
				if (!sign.equalsIgnoreCase(compareSign)) {
					setResponseInfo(jsonObj, 2);
					os.write(jsonObj.toString().getBytes());
					logger.error("sign = " + "error");
				} else {// 通过验证，获取用户信息返回
					int cmdType = messagenum % 10000;
					int gameId = messagenum / 10000;
					if (gameId > 100) {
						gameId /= 10;
					}

					if (gameId != ConfigManager.getGameId()) {
						return;
					}

					Map<String, String> cmdParam = new HashMap<>();
					if (!"".equals(messageBody)) {
						JSONObject json = JSONObject.fromObject(messageBody);
						for (Object k : json.keySet()) {
							Object v = json.get(k);
							if (null != v) {
								cmdParam.put(k.toString(), v.toString());
							}
						}
					}
					int ret = 1;
					// 走这个接口先判断是否是获取的协议，否则是执行的协议
					Map<String, Object> data = getData(cmdType, cmdParam);
					if (data == null) {
						ret = this.getResultInfo(cmdType, cmdParam, serialNum);
					} else {
						jsonObj.accumulateAll(data);
					}
					setResponseInfo(jsonObj, ret);
					os.write(jsonObj.toString().getBytes());
				}
			}
		} catch (Exception e) {
			logger.error("GM_HttpToolHandler exception=", e);
		} finally {
			exchange.close();
		}
	}

	/**
	 * 执行GM命令
	 * yebin added @ 2016年8月10日 下午8:06:03
	 * @param userId
	 * @param roleId
	 * @param cmd
	 * @return
	 */
	public String getResultInfo(int userId, long roleId, String cmd) {
		int succ = GMInteface.execCommand(userId, roleId, 0, cmd, false);
		if (succ == 0) {
			return " command execute success " + " userId " + userId + " roleId " + roleId + " " + cmd;
		}
		return succ + "";
	}

	/**
	 * 处理数据并返回结果字符串
	 * yebin added @ 2016年8月10日 下午8:05:42
	 * @param cmd
	 * @param cmdParam
	 * @param serialNum
	 * @return
	 */
	@SuppressWarnings("null")
	public int getResultInfo(int cmd, Map<String, String> cmdParam, String serialNum) {
		int succ = -1;
		StringBuilder sb = new StringBuilder();
		String strRoleId = cmdParam.get("CharId");

		final long roleId;
		PropRole prole = null;
		if (strRoleId != null) {
			try {
				roleId = Long.parseLong(strRoleId);
			} catch (Exception e) {
				e.printStackTrace();
				return 3;
			}

			prole = new PropRole(roleId, true);
			if (prole.getProperties() == null) {
				return 3;
			}
		} else {
			roleId = -1;
		}

		switch (cmd) {
		case FORBID_ACCOUNT: // 封停账号
		{
			String strDuration = cmdParam.get("BlockDuration");
			String strReason = cmdParam.get("BlockReason");
			int UserId = prole.getUserid();
			sb.append("//forbid  ").append(UserId).append(" ").append(strDuration).append(" ").append(strReason);
		}
			break;
		case UNFORBID_ACCOUNT: // 解封账号
		{
			int UserId = prole.getUserid();
			sb.append("//unforbid  ").append(UserId);
		}
			break;
		case NON_VOICE: // 禁言
		{
			String ChatRoom = cmdParam.get("ChatRoom");
			if (!ChatRoom.equals("1")) {
				return 99;
			}

			String strDuration = cmdParam.get("Duration");
			String strReason = cmdParam.get("Reason");

			long duration = 0;
			try {
				duration = Long.parseLong(strDuration) * 60;
			} catch (Exception e) {
				e.printStackTrace();
				return 99;
			}

			if (duration <= 0) {
				return 99;
			}

			sb.append("//nonvoice ").append(prole.getRoleId()).append(" ").append(duration).append(" ").append(strReason).append(" 0");
		}
			break;
		case UNNON_VOICE: // 解除禁言
		{
			String ChatRoom = cmdParam.get("ChatRoom");
			if (!ChatRoom.equals("1")) {
				return 99;
			}
			sb.append("//unnonvoice ").append(prole.getRoleId());
		}
			break;
		case BROADCAST_AND_POST: // 滚屏公告/喇叭公告
		{
			String Type = cmdParam.get("Type");
			String Content = new String(DecodeBase64.transform(cmdParam.get("Content").getBytes()), Charset.forName("UTF-8"));

			if (Content == null || Content.equals("")) {
				return 99;
			}

			int type = 0;
			try {
				type = Integer.parseInt(Type);
			} catch (Exception e) {
				e.printStackTrace();
				return 99;
			}

			if (type == 1) {
				sb.append("//post  ").append(Content);
			} else if (type == 2) {
				sb.append("//broadcast 0 ").append(Content);
			} else if (type == 3) {
				sb.append("//zmd ").append(Content);
			}
		}
			break;
		case SINGLE_PLAYER_MAIL:
		case SOME_PLAYERS_MAIL: {
			String base64Info = cmdParam.get("EmailTitle");
			String emailTitle = new String(DecodeBase64.transform(base64Info.getBytes()), Charset.forName("UTF-8"));
			if (emailTitle.equals("")) {
				return 99;
			}

			base64Info = cmdParam.get("EmailContent");
			String emailContent = new String(DecodeBase64.transform(base64Info.getBytes()), Charset.forName("UTF-8"));

			String sAwardCount = cmdParam.get("AwardCount");
			String sDuration = cmdParam.get("Duration");
			int awardCount = -1;
			long duration = -1;
			try {
				awardCount = Integer.parseInt(sAwardCount);
				duration = Long.parseLong(sDuration);
			} catch (Exception e) {
				e.printStackTrace();
				return 99;
			}

			boolean bHasItem = false;
			String awards = "";
			if (awardCount > 0) {
				bHasItem = true;
				awards = cmdParam.get("Awards");
			}

			if (awards == null || (awards.equals("") && bHasItem)) {
				return 99;
			}

			Map<Integer, Integer> needDecCurrency = new ConcurrentHashMap<>();
			List<xbean.SingleCompensationAward> listAwards = new LinkedList<>();
			if (bHasItem) {
				if (!parseAward(awardCount, awards, listAwards, needDecCurrency)) {
					return 99;
				}
			}

			// 扣道具相关逻辑
			if (needDecCurrency.size() > 0) {
				new mkdb.Procedure() {
					@Override
					protected boolean process() throws Exception {
						xbean.Properties prop = xtable.Properties.select(roleId);
						if (null == prop){
							return false;
						}
						
						Pack bag = new Pack(roleId, false);
						for (Entry<Integer, Integer> entry : needDecCurrency.entrySet()) {
							long num = entry.getValue();
							if (num >= 0) {
								continue;
							}
							switch (entry.getKey()) {
							case GM_ITEM_CURRENCY_SILVER:
								bag.subMoney(num, "GM扣除", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
								break;
							case GM_ITEM_CURRENCY_GOLD:
								bag.subGold(num, "GM扣除", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
								break;
							case GM_ITEM_CURRENCY_FUSHI:
								//FushiManager.addFushiToUser(prop.getUserid(), roleId, (int) -num, FushiConst.CASH_FUSHI, fire.log.enums.YYLoggerTuJingEnum.GM);
								FushiManager.subFushiFromUser(prop.getUserid(), roleId, (int) -num, 0, 0, FushiConst.REASON_GM_DEC, fire.log.enums.YYLoggerTuJingEnum.GM, true);
								break;
							default:
								break;
							}
						}
						return true;
					}
				}.submit();
			}

			if (SINGLE_PLAYER_MAIL == cmd) {
				return doSendSingleMail(roleId, emailTitle, emailContent, duration, serialNum, listAwards);
			} else if (SOME_PLAYERS_MAIL == cmd) {
				String conds = cmdParam.get("Condition");
				return doSendConditionMail(conds, emailTitle, emailContent, duration, serialNum, listAwards);
			}

			return 99;
		}
		case VERIFY_SPACE_PICTURE: {
			// 审核结果 1=通过 2=未通过
			String verifyResult = cmdParam.get("AuditingType");
			int msgId = 0;
			if (verifyResult.equals("1")) {
				msgId = 162157; // 审核成功
			} else if (verifyResult.equals("2")) {
				msgId = 162158; // 审核不通过
			} else {
				return 99;
			}
			sb.append("//friendsystemmsg ").append(prole.getRoleId()).append(" ").append(msgId);
		}
			break;
		case GM_SPACE_GIFT_SET: {
			// 空间大师礼物数量设置
			String Amount = cmdParam.get("Amount");
			sb.append("//setspacegiftnum ").append(" ").append(Amount);
		}
			break;
		case GET_360_GIFT: {
			long nCharId = -1;
			int nConditions = -1;
			try {
				nCharId = Long.parseLong(cmdParam.get("CharId"));
				nConditions = Integer.parseInt(cmdParam.get("Conditions"));
			} catch (Exception e) {
				e.printStackTrace();
				return 99;
			}
			if (!get360Gift(nCharId, nConditions, serialNum)) {
				return 99;
			}
		}
			break;
		case CBG_ITEM_SELL: { // 出售道具（gamid=88, 藏宝阁专用）
			long productItemId = -1, buyerRoleId = -1, sellerRoleId = -1;
			try {
				productItemId = Long.parseLong(cmdParam.get("PId"));// 产品Id
				buyerRoleId = Long.parseLong(cmdParam.get("BuyerRoleId"));// 买家角色ID
				sellerRoleId = Long.parseLong(cmdParam.get("SellerRoleId"));// 卖家角色ID
			} catch (Exception e) {
				BlackMarketManager.LOG.error("平台通知金币订单出售, 参数解析异常：" + e);
				return 99;
			}
			
			if (productItemId <= 0 || buyerRoleId <= 0 || sellerRoleId <= 0) {
				BlackMarketManager.LOG.error("平台通知金币订单出售传过来的数据有错误：buyerRoleId=" + buyerRoleId + ", sellerRoleId=" + sellerRoleId + ", pid=" + productItemId);
				return 99;
			}

			// 藏宝阁相关逻辑 liangyanpeng
			BlackMarketManager.getInstance().goldOrderTrade(buyerRoleId, sellerRoleId, productItemId);

		}
			break;
		case COMMON_EXCE_GM: {
			String GMOrder = cmdParam.get("GMOrder");
			String parameter1=cmdParam.get("Parameter1");
			String parameter2=cmdParam.get("Parameter2");
			String parameter3=cmdParam.get("Parameter3");
			String parameter4=cmdParam.get("Parameter4");
			String parameter5=cmdParam.get("Parameter5");
			String parameter6=cmdParam.get("Parameter6");
			//通用组织执行GM指令的串
			sb.append("//").append(GMOrder);
			if(parameter1!=null&&parameter1.length()>0){
				sb.append(" ").append(parameter1.trim());
			}
			if(parameter2!=null&&parameter2.length()>0){
				sb.append(" ").append(parameter2.trim());
			}
			if(parameter3!=null&&parameter3.length()>0){
				sb.append(" ").append(parameter3.trim());
			}
			if(parameter4!=null&&parameter4.length()>0){
				sb.append(" ").append(parameter4.trim());
			}
			if(parameter5!=null&&parameter5.length()>0){
				sb.append(" ").append(parameter5.trim());
			}
			if(parameter6!=null&&parameter6.length()>0){
				sb.append(" ").append(parameter6.trim());
			}
			
		}
			break;
		case FPRBID_DEAL: {
			// 封交易
			String CharId = cmdParam.get("CharId");
			sb.append("//forbiddeal").append(" ").append(CharId);
		}
			break;
		case BIND_TEL: {
			// 绑定手机号
			String CharId = cmdParam.get("CharId");
			String Mobile = cmdParam.get("Mobile");
			sb.append("//addbindtel").append(" ").append(CharId).append(" ").append(Mobile);
		}
			break;
		case UNBIND_TEL: {
			// 解除绑定手机号
			String CharId = cmdParam.get("CharId");
			sb.append("//addunbindtel").append(" ").append(CharId);
		}
			break;
		case CHANGE_TEL: {
			// 改变绑定手机号
			String CharId = cmdParam.get("CharId");
			String Mobile = cmdParam.get("Mobile");
			sb.append("//changebindtel").append(" ").append(CharId).append(" ").append(Mobile);
		}
			break;
		}
		if (!sb.toString().equals("")) {
			succ = GMInteface.execCommand(-1, -1, -1, sb.toString(), false);
			if (succ != 0)
				return 99;
		}

		return 1;
	}

	/**
	 * 360获取悬赏任务礼物
	 * yebin added @ 2016年8月10日 下午8:05:17
	 * @param roleId
	 * @param nConditions
	 * @param serialNum
	 * @return
	 */
	private boolean get360Gift(long roleId, int nConditions, String serialNum) {
		About360XuanShang config = xuanShangConfig.get(nConditions);
		if (!doCheck360GetGift(roleId, config)) {
			return false;
		}

		List<xbean.SingleCompensationAward> awards = new LinkedList<xbean.SingleCompensationAward>();
		for (int i = 0; i < config.getPrizeId().size(); i++) {
			int itemId = config.getPrizeId().get(i);
			int itemNum = config.getPrizeNum().get(i);
			if (itemId == 0 || itemNum <= 0) {
				continue;
			}

			xbean.SingleCompensationAward award = xbean.Pod.newSingleCompensationAwardData();
			switch (itemId) {
			case GM_ITEM_CURRENCY_SILVER:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_MONEY);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_GOLD:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_GOLD);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_FUSHI:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_FUSHI);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_EXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_EXP);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_VIPEXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_VIPEXP);
				award.setNum(itemNum);
				break;
			default: {
				ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemId);
				if (attr == null) {
					logger.info("收到物品ID错误" + itemId);
					return false;
				}
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_ITEM);
				award.setId(itemId);
				award.setNum(itemNum);
				award.setFlag(0);
			}
				break;
			}
			awards.add(award);
		}

		try {
			CreateSingleCompensation.createFromYunYing(roleId, awards, "360悬赏任务", config.getDesc(), 0, serialNum, "").submit().get(2, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 查询是否可以360获取悬赏任务礼物
	 * yebin added @ 2016年8月10日 下午8:04:59
	 * @param roleId
	 * @param config
	 * @return
	 */
	private boolean doCheck360GetGift(long roleId, About360XuanShang config) {
		if (config == null) {
			return false;
		}

		xbean.Properties properties = xtable.Properties.select(roleId);
		if (properties == null) {
			return false;
		}

		// 领取等级
		if (config.getNeedRoleLv() > properties.getLevel()) {
			return false;
		}

		// 在线时长
		xbean.ActiveRoleInfo activeRoleInfo = xtable.Activeroletable.select(roleId);
		if (config.getTotalPlayedTime() > activeRoleInfo.getDayonlinetime()) {
			return false;
		}

		// 是否连续充值
		if (config.getChargeDaysExceptFirst() != 0) {
			if (!DateValidate.inTheSameDay(properties.getLastchargetime(), System.currentTimeMillis())) {
				return false;
			}
		}

		// 是否首冲
		if (config.getNeedFirstCharge()) {
			xbean.FirstCharge firstCharge = xtable.Firstchargenew.select(properties.getUserid());
			if (firstCharge == null) {
				return false;
			}
		}

		// 连续登陆天数
		int loginDays = config.getLoginDays();
		if (loginDays != 0) {
			xbean.MulDayLoginGift mulDayLoginGiftTbl = xtable.Rolemuldayloginreward.select(roleId);
			if (mulDayLoginGiftTbl != null) {
				if (loginDays > mulDayLoginGiftTbl.getLogindays()) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private void setResponseInfo(JSONObject jsonobj, int code) {
		if (jsonobj.containsKey("Code")) {
			return;
		}

		String info = "";
		switch (code) {
		case 1:
			info = "成功";
			break;
		case 2:
			info = "签名错误";
			break;
		case 3:
			info = "角色ID不存在";
			break;
		default:
			info = "其他";
			break;
		}
		jsonobj.accumulate("Code", code);
		jsonobj.accumulate("Text", info);
	}

	private int doSendSingleMail(long roleId, String title, String content, long duration, String serialNum, List<xbean.SingleCompensationAward> awards) {
		long endTime = 0;
		if (duration > 0) {
			endTime = System.currentTimeMillis() + (duration * 60 * 1000);
		}
		try {
			CreateSingleCompensation.createFromYunYing(roleId, awards, title, content, endTime, serialNum, "").submit().get(2, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return 99;
		}
		return 1;
	}

	private static int doSendConditionMail(String sConds, String title, String content, long duration, String serialNum, List<xbean.SingleCompensationAward> awards) {
		java.util.Map<Integer, List<String>> conds = new HashMap<Integer, List<String>>();
		if (parseCond(sConds, conds) == false) {
			return 99;
		}
		long endTime = 0;
		if (duration > 0) {
			endTime = System.currentTimeMillis() + (duration * 60 * 1000);
		}
		try {
			CreateSingleCompensation.createFromYunYingCond(conds, awards, title, content, endTime, serialNum, "").submit().get(2, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return 99;
		}
		return 1;
	}

	/**
	 * 解析邮件发送奖励
	 * yebin added @ 2016年8月10日 下午8:02:52
	 * @param itemCount	收到的消息里物品的数量
	 * @param sAwards 未解析的字符串
	 * @param outAwards  解析出,需要加的物品
	 * @param needDecCurrency 需要扣除的货币
	 * @return
	 */
	private static boolean parseAward(int itemCount, String sAwards, List<xbean.SingleCompensationAward> outAwards, Map<Integer, Integer> needDecCurrency) {
		JSONArray jsAwards = JSONArray.fromObject(sAwards);
		logger.info("收到邮件返回兑换码物品信息为:" + jsAwards.toString());
		if (jsAwards.size() != itemCount) {
			return false;
		}

		JSONObject jobj = null;
		for (int i = 0; i < jsAwards.size(); i++) {
			jobj = jsAwards.getJSONObject(i);
			String[] awardString = jobj.getString("Award").split("\\|");
			if (awardString.length < 3) {
				return false;
			}

			int itemId = Integer.parseInt(awardString[1]);
			int itemNum = Integer.parseInt(awardString[2]);
			if (itemNum <= 0) {
				switch (itemId) {
				case GM_ITEM_CURRENCY_SILVER:
				case GM_ITEM_CURRENCY_GOLD:
				case GM_ITEM_CURRENCY_FUSHI:
					if (itemNum != 0) {
						Integer putItemNum = needDecCurrency.get(itemId);
						if (putItemNum != null) {
							putItemNum += itemNum;
						} else {
							putItemNum = itemNum;
						}
						needDecCurrency.put(itemId, putItemNum);
					} else {
						logger.error("收到的邮件物品的数量为:" + itemNum);
						return false;
					}
					continue;
				default:
					logger.error("收到的邮件物品的数量为:" + itemNum);
					return false;
				}
			}

			int flag = Integer.parseInt(awardString[3]); // 1=绑定 2=不绑定

			xbean.SingleCompensationAward award = xbean.Pod.newSingleCompensationAwardData();
			switch (itemId) {
			case GM_ITEM_CURRENCY_SILVER:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_MONEY);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_GOLD:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_GOLD);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_FUSHI:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_FUSHI);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_EXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_EXP);
				award.setNum(itemNum);
				break;
			case GM_ITEM_CURRENCY_VIPEXP:
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_VIPEXP);
				award.setNum(itemNum);
				break;
			default: {
				ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemId);
				if (attr == null) {
					logger.info("收到物品ID错误" + itemId);
					return false;
				}
				
				int itemFlag = (flag == 1 ? fire.pb.compensation.Helper.AWARDFLAG_BIND : 0);
				award.setType(fire.pb.compensation.Helper.AWARDTYPE_ITEM);
				award.setId(itemId);
				award.setNum(itemNum);
				award.setFlag(itemFlag);
			}
				break;
			}
			outAwards.add(award);
		}
		return true;
	}

	/**
	 * 解析邮件发送条件
	 * yebin added @ 2016年8月10日 下午8:04:04
	 * @param sConds
	 * @param outConds
	 * @return
	 */
	public static boolean parseCond(String sConds, Map<Integer, List<String>> outConds) {
		String[] conds = sConds.split("\\|");
		if (conds.length != 3) {
			return false;
		}
		int condId = convertCondId(Integer.parseInt(conds[0]));
		if (condId > 0) {
			outConds.put(condId, new ArrayList<String>());
			outConds.get(condId).add(conds[1]);
			outConds.get(condId).add(conds[2]);
		} else {
			logger.error("邮件的条件id无效" + condId);
			return false;
		}
		return true;
	}

	/**
	 * 运营工具发送的条件ID转换成服务器可用的条件ID
	 * yebin added @ 2016年8月10日 下午8:04:20
	 * @param condId
	 * @return
	 */
	public static int convertCondId(int condId) {
		switch (condId) {
		case 100:
			return 1;
		case 102:
			return 50;
		}
		return -1;
	}

	/**
	 * 检查HTTP协议传过来的参数正确性
	 * yebin added @ 2016年8月10日 下午8:04:25
	 * @param params
	 * @return
	 */
	private int checkParams(Map<String, String> params) {
		String param = null;
		StringBuilder MD5SignSource = new StringBuilder();
		MD5SignSource.append(ConfigManager.getGameKey());

		param = params.get("GameId");
		if (param == null || Integer.parseInt(param) != ConfigManager.getGameId())
			return 3;
		MD5SignSource.append(param);

		param = params.get("RoleId");
		if (param == null || Long.parseLong(param) <= 0)
			return 5;
		MD5SignSource.append(param);

		param = params.get("ServerId");
		if (param == null || !param.equals(Gs.serverid))
			return 6;
		MD5SignSource.append(param);

		// 角色名不在签名里,不用签名
		param = params.get("RoleName");
		if (param == null || param.length() == 0)
			return 7;

		param = params.get("Time");
		if (param == null || Long.parseLong(param) == 0)
			return 8;
		MD5SignSource.append(param);

		param = params.get("Operate");
		if (param == null || (Byte.parseByte(param) != 1 && Byte.parseByte(param) != 2))
			return 9;
		MD5SignSource.append(param);

		param = params.get("Sign");
		if (param == null || !checkMd5Sign(param, MD5SignSource.toString()))
			return 2;
		return 1;
	}

	private boolean checkMd5Sign(String sign, String signSource) {
		String compareSign = LocalHttpUtils.getMD5Str(signSource);
		return sign.equalsIgnoreCase(compareSign);
	}

	/**
	 * 获取接口
	 * @author yangtao
	 * @dateTime 2016年6月22日 下午5:50:59
	 * @version 1.0
	 * @param cmdType
	 * @return
	 */
	private Map<String, Object> getData(int cmdType, Map<String, String> cmdParam) {
		switch (cmdType) {
		case QUERY_ROLE_INFO: // 查詢角色信息
		{
			Map<String, Object> ret = new HashMap<>();
			long nCharId = -1;
			try {
				nCharId = Long.parseLong(cmdParam.get("CharId"));
			} catch (Exception e) {
				e.printStackTrace();
				ret.put("Code", 99);
				ret.put("Text", "传过来的数据有错误");
				return ret;
			}

			xbean.Properties properties = xtable.Properties.select(nCharId);
			if (null != properties) {
				Map<String, Object> retRoleInfo = new HashMap<>();
				ret.put("Code", 1);
				ret.put("Text", "操作成功");

				retRoleInfo.put("RoleId", nCharId);
				retRoleInfo.put("RoleName", properties.getRolename());
				ret.put("RoleInfo", retRoleInfo);
				return ret;
			}

			ret.put("Code", 99);
			ret.put("Text", "传过来的数据有错误");
			return ret;
		}
		case GM_SPACE_GIFT_GET: // 获取空间大师礼物剩余数量
		{
			return GMSeachManage.getSpaceGiftNum();
		}
		case CHECK_360_GET_GIFT: // 查询领取条件（gamid=88,360悬赏活动专用）
		{
			Map<String, Object> ret = new HashMap<>();
			long nCharId = -1;
			int nConditions = -1;
			try {
				nCharId = Long.parseLong(cmdParam.get("CharId"));
				nConditions = Integer.parseInt(cmdParam.get("Conditions"));
			} catch (Exception e) {
				e.printStackTrace();
				ret.put("Code", 99);
				ret.put("Text", "传过来的数据有错误");
				return ret;
			}

			if (nCharId <= 0 || nConditions <= 0) {
				ret.put("Code", 99);
				ret.put("Text", "传过来的数据有错误");
				return ret;
			}

			About360XuanShang config = xuanShangConfig.get(nConditions);
			boolean isSucc = doCheck360GetGift(nCharId, config);
			ret.put("Code", 1);
			ret.put("Text", "成功");
			ret.put("Result", isSucc ? 1 : 0);
			return ret;
		}
		case CBG_CHECK_ITEM_SELL: { // 查询道具是否能出售（gamid=88, 藏宝阁专用）
			Map<String, Object> ret = new HashMap<>();
			ret.put("Code", 99);
			ret.put("Text", "传过来的数据有错误");

			long productItemId = -1, sellerRoleId = -1;
			try {
				sellerRoleId = Long.parseLong(cmdParam.get("SellerRoleId"));// 卖家角色ID
				productItemId = Long.parseLong(cmdParam.get("PId"));
			} catch (Exception e) {
				BlackMarketManager.LOG.error("平台查询金币订单是否能出售, 参数解析异常：" + e);
				return ret;
			}

			if (productItemId <= 0 || sellerRoleId <= 0) {
				BlackMarketManager.LOG.error("平台查询金币订单是否能出售传过来的数据有错误：sellerRoleId=" + sellerRoleId + ", pid=" + productItemId);
				return ret;
			}

			// 藏宝阁相关逻辑 liangyanp
			boolean isSale = BlackMarketManager.getInstance().goldOrderIsSale(sellerRoleId, productItemId);

			ret.put("Result", isSale ? 1 : 0);
			ret.put("Code", isSale ? 1 : 99);
			ret.put("Text", isSale ? "成功" : "失败");
			
			if (BlackMarketManager.LOG.isInfoEnabled()) {
				BlackMarketManager.LOG.error("平台查询金币订单是否能出售传过来的数据：sellerRoleId=" + sellerRoleId + ", pid=" + productItemId);
				BlackMarketManager.LOG.error("返回给平台查询金币订单是否能出售的数据：Result=" + ret.get("Result") + ", Code=" + ret.get("Code") + ", Text=" + ret.get("Text"));
			}
			
			return ret;
		}
		}
		return null;
	}
}
