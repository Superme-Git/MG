package fire.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import mkdb.Procedure;
import mkdb.Procedure.Task;
import mkdb.Transaction;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import xbean.LiveSkill;
import xbean.PracticeSkill;
import fire.log.beans.AchievementBean;
import fire.log.beans.AddZhenfaExpBean;
import fire.log.beans.ClanBuildUPBean;
import fire.log.beans.CompareItemBean;
import fire.log.beans.FactionOpbean;
import fire.log.beans.ItemBean;
import fire.log.beans.MailBean;
import fire.log.beans.OpAucConBean;
import fire.log.beans.OpAucRackBean;
import fire.log.beans.OpBonusBean;
import fire.log.beans.OpEquiMakBean;
import fire.log.beans.OpEquiRepBean;
import fire.log.beans.OpFightBean;
import fire.log.beans.OpLvlBean;
import fire.log.beans.OpNoviBattleBean;
import fire.log.beans.OpPetBean;
import fire.log.beans.OpPetMixBean;
import fire.log.beans.OpPetSkiBean;
import fire.log.beans.OpPetTraBean;
import fire.log.beans.OpPointGoldExBean;
import fire.log.beans.OpSilShopBean;
import fire.log.beans.OpTokShopBean;
import fire.log.beans.OpTradExBean;
import fire.log.beans.RegBean;
import fire.log.beans.ResolveItemBean;
import fire.log.beans.RoleCreateBean;
import fire.log.beans.RoleSkillUpBean;
import fire.log.beans.SNSOpBean;
import fire.log.beans.TeamOpBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.util.DateUtil;
import fire.pb.util.FireProp;

public class YYLogger {
	private static Logger logger = Logger.getLogger("YYLOG");

	static Properties prop = ConfigManager.getInstance().getPropConf("sys");
	protected static final String LOG_GAMEID = FireProp.getStringValue(prop, "sys.charge.gameid"); // 游戏ID

	public static final String FAIL_FLAG = "fail";// 失败标示
	public static final String SUCC_FLAG = "succ";// 成功标示
	public static final String DRAW_FLAG = "draw";// 平局标示

	// 以下是日志自定义事件类型
	public static final int ROLE_CREATE_LOG = 1;// 角色创建日志
	public static final int GUA_LOG = 12;// 挂机系统日志
	public static final int ITEM_RESOLVE_LOG = 19;// 装备道具分解
	public static final int ITEM_COMPTER_LOG = 20;// 装备道具合成

	// 运营要求序列号是需要每个模块单独生成
	private static Map<String, Long> mapMsgSeqs = new HashMap<String, Long>();

	public final synchronized static Long addMsgSeq(String logType) {
		long seq = 1;
		if (mapMsgSeqs.containsKey(logType)) {
			seq = mapMsgSeqs.get(logType) + 1;
		}
		mapMsgSeqs.put(logType, seq);
		return seq;
	}

	/**
	 * 通用基础日志字段
	 * 
	 * @param logType
	 * @param map
	 */
	private final static Map<String, Object> BaseLog(String logType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("LogTbl", logType);// 日志名称(接口固定参数，每个接口参数不同，请注意)
		map.put("MsgSeq", addMsgSeq(logType));// 序列号(上报消息的连续编号)
		map.put("GId", LOG_GAMEID);// 游戏ID
		map.put("Sid", Gs.serverid);// 游戏区服ID
		map.put("Optime", DateUtil.getCurrentTimeFomat());// 操作时间
		return map;
	}

	/**
	 * 用户通用基础日志字段
	 * 
	 * @param userId
	 */
	private final static void setBaseUserLog(String accId, String chId, String platType, Map<String, Object> map) {
		map.put("AccId", accId); // 玩家账号
		map.put("ChId", chId);// 渠道ID
		map.put("PlatType", platType);// 平台ID(IOS,android,wp)
	}

	private final static void BaseUserLog(xbean.UserDeviceInfo userinfo, Map<String, Object> map) {
		if (userinfo == null) {
			LogManager.logger.error("BaseUserLog(2 params) have a logic error!!!");
			return;
		}
		setBaseUserLog(userinfo.getUsername(), userinfo.getChid(), userinfo.getPlattype(), map);
	}

	/**
	 * 玩家and用户通用基础日志字段
	 * 
	 * @param roleId
	 * @param map
	 */
	private final static void setBaseRoleLog(long roleId, int usrLv, int vipLv, long exp, int school, Map<String, Object> map) {
		map.put("UsrId", roleId);// 玩家UserID
		map.put("UsrLvl", usrLv);// 用户等级
		map.put("VipLvl", vipLv);// VIP等级
		map.put("Exp", exp); // 当前经验值
		map.put("School", school); // 所属门派
	}

	private final static void BaseRoleLog(long roleId, xbean.UserDeviceInfo userinfo, xbean.Properties prop, Map<String, Object> map) {
		if (userinfo == null || prop == null) {
			LogManager.logger.error("BaseRoleLog(4 params) have a logic error!!!");
			return;
		}
		BaseUserLog(userinfo, map);
		setBaseRoleLog(roleId, prop.getLevel(), prop.getViplevel(), prop.getExp(), prop.getSchool(), map);
	}

	private final static boolean BaseRoleLog(long roleId, Map<String, Object> map) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null) {
			LogManager.logger.error("BaseRoleLog(2 params) have a error roleId = " + roleId);
			return false;
		}

		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(prop.getUserid());
		if (userinfo == null) {
			LogManager.logger.error("BaseRoleLog(2 params) have a error userId = " + prop.getUserid());
			return false;
		}

		BaseUserLog(userinfo, map);
		setBaseRoleLog(roleId, prop.getLevel(), prop.getViplevel(), prop.getExp(), prop.getSchool(), map);
		return true;
	}

	/**
	 * 登陆接口,在账号登陆的时候触发
	 * 
	 * @param roleId
	 * @param loginType
	 */
	public static void OpLog(int userId, long roleId, String loginType, int Fstlogin, int TypeFlg) {
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}

		Map<String, Object> map = BaseLog("OpLog");// 序列号(上报消息的连续编号)

		long allybNum = 0;// 充值符石数量
		long curyb = 0; // 当前符石数量
		if (roleId != 0) {
			if (!BaseRoleLog(roleId, map)) {
				return;
			}

			if (fire.pb.fushi.Module.getIsYYBUser(userId)) {
				// 记录符石数,并判断是否需要返符石
				xbean.YybFushiNums yybFs = xtable.Yybfushi.select(userId);
				if (yybFs != null) {
					xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleId);
					if (yybFushiNum != null) {
						allybNum = yybFushiNum.getSaveamt();// 充值符石数量
						curyb = yybFushiNum.getBalance();// 可交易符石数量
					}
				}
			} else {
				xbean.YbNums ybNums = xtable.Fushinum.select(userId);
				if (ybNums != null) {
					xbean.YbNum ybNum = ybNums.getRoleyb().get(roleId); // 得到角色对应的符石数量
					if (ybNum != null) {
						allybNum = ybNum.getNopresentnum();// 充值符石数量
						curyb = ybNum.getNum();// 可交易符石数量
					}
				}
			}
		} else {
			BaseUserLog(userinfo, map);
			setBaseRoleLog(roleId, 0, 0, 0, 0, map);
		}

		map.put("TelcoOper", userinfo.getTelcooper());// 用户网络运营商
		map.put("DeviId", userinfo.getDeviid());// 玩家设备(mac/idfv)设备唯一标识
		map.put("IMEI", userinfo.getImei());// 入网标识符
		map.put("Ip", userinfo.getIp());// 玩家设备IP
		map.put("NetEnvir", userinfo.getNetenvir());// 网络环境(如：3G,4G,wifi)
		map.put("OsVer", userinfo.getOsver());// 玩家系统版本(具体版本如ios9,android4.1)
		map.put("BrType", userinfo.getBrtype());// 玩家手机类型(如联想，酷派)
		map.put("Fstlogin", Fstlogin);// 是否是初次登陆(0初次,1非初次)
		map.put("logType", loginType);// 类型标记(login,logout)
		map.put("TypeFlg", TypeFlg);// 标记是账号登录或角色登录，0为账号登录，1为角色登录
		map.put("RegDeviId", userinfo.getFirstdeviid());// 注册设备标识(注册时候mac/idfa)
														// 该功能用于黑卡筛选
		map.put("RemAmnt", curyb);// 身上剩余金币(游戏内主要代币)主要是符石
		map.put("TotlCha", allybNum / 10);

		doLogWhileCommit(map);
	}

	public static void OpLogAsyn(int userId, long roleId, String loginType, int Fstlogin, int TypeFlg) {
		Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				OpLog(userId, roleId, loginType, Fstlogin, TypeFlg);
				return true;
			}
		});
	}

	/**
	 * 注册接口 // 标记是账号登录或角色登录，0为账号注册，1为角色注册
	 * 
	 * @param userId
	 */
	public static void OpRegLog(int userId, long roleId, int TypeFlg) {
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}

		int prof = 0;
		int race = 0;
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			prof = prop.getSchool();
			race = prop.getShape();
		}

		Map<String, Object> map = BaseLog("OpReg");// 序列号(上报消息的连续编号)
		BaseUserLog(userinfo, map);

		map.put("TelcoOper", userinfo.getTelcooper());// 用户网络运营商
		map.put("DeviId", userinfo.getDeviid());// 玩家设备(mac/idfv)设备唯一标识
		map.put("UsrId", roleId);// 角色[必须保留]
		map.put("IMEI", userinfo.getImei());// 入网标识符
		map.put("Ip", userinfo.getIp());// 玩家设备IP
		map.put("NetEnvir", userinfo.getNetenvir());// 网络环境(如：3G,4G,wifi)
		map.put("OsVer", userinfo.getOsver());// 玩家系统版本(具体版本如ios9,android4.1)
		map.put("BrType", userinfo.getBrtype());// 玩家手机类型(如联想，酷派)
		map.put("TypeFlg", TypeFlg);// 标记是账号登录或角色登录，0为账号登录，1为角色登录
		map.put("RegDeviId", userinfo.getFirstdeviid());// 注册设备标识(注册时候mac/idfa)
														// 该功能用于黑卡筛选
		map.put("Prof", prof); // 职业类型
		map.put("races", race); // 种族类型

		doLogWhileCommit(map);
	}

	/**
	 * 记录玩家等级变化信息，包括角色等级以及vip等级
	 * 
	 * @param roleId
	 * @param BfLvl
	 * @param AfLvl
	 * @param BfExp
	 * @param AfExp
	 * @param LvlType
	 */
	public static void OpLvlLog(long roleId, OpLvlBean opLvlBean) {
		Map<String, Object> map = BaseLog("OpLvl");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("LvlType", opLvlBean.getLvlType()); // 等级类型游戏等级/vip等级，
		map.put("BfLvl", opLvlBean.getBfLvl()); // 玩家变化前等级
		map.put("AfLvl", opLvlBean.getAfLvl()); // 玩家变化后等级
		map.put("BfExp", opLvlBean.getBfExp()); // 变化前经验没有记为-1
		map.put("AfExp", opLvlBean.getAfExp()); // 变化后经验没有记为-1
		map.put("Egettype", opLvlBean.getEgettype()); // 获得或者失去经验的类别

		doLogWhileCommit(map);
	}

	public static class acceptTaskLogImpl extends mkdb.Procedure {
		private long roleId;
		private int InsID;

		public acceptTaskLogImpl(long roleId, int InsID) {
			super();
			this.roleId = roleId;
			this.InsID = InsID;
		}

		@Override
		public boolean process() {
			Map<String, Object> map = BaseLog("OpInstGet"); // 序列号(上报消息的连续编号)

			if (!BaseRoleLog(roleId, map)) {
				return true;
			}

			map.put("InsID", InsID);// 任务ID

			doLogWhileCommit(map);

			return true;
		}
	}

	/**
	 * 任务接取
	 * 
	 * @param roleId
	 * @param InsID
	 * @param UsrLvl
	 * @param VipLvl
	 */
	public static void acceptTaskLog(long roleId, int InsID) {
		acceptTaskLogImpl p = new acceptTaskLogImpl(roleId, InsID);
		if (Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(p);
		} else {
			p.submit();
		}
	}

	public static class completeTaskLogImpl extends mkdb.Procedure {
		private long roleId;
		private int InsID;
		private String FinishFlg;
		private String FailDesc;

		public completeTaskLogImpl(long roleId, int InsID, String FinishFlg, String FailDesc) {
			super();
			this.roleId = roleId;
			this.InsID = InsID;
			this.FinishFlg = FinishFlg;
			this.FailDesc = FailDesc;
		}

		@Override
		public boolean process() {
			Map<String, Object> map = BaseLog("OpInstSta");// 序列号(上报消息的连续编号)
			if (!BaseRoleLog(roleId, map)) {
				return true;
			}

			map.put("InsID", InsID);// 任务ID
			map.put("FinishFlg", FinishFlg);// 成功/失败(成功为succ,失败为fail)
			map.put("FailDesc", FailDesc);// 成功/失败原因(自定义字符串,可留空)[

			doLogWhileCommit(map);
			return true;
		}
	}

	/**
	 * 活动相关 author yebin @ 2016年3月23日
	 *
	 */
	public static class completeActivityLogImpl extends mkdb.Procedure {
		private long roleId;
		private int InsID;
		private String FinishFlg;
		private String FailDesc;

		public completeActivityLogImpl(long roleId, int InsID, String FinishFlg, String FailDesc) {
			super();
			this.roleId = roleId;
			this.InsID = InsID;
			this.FinishFlg = FinishFlg;
			this.FailDesc = FailDesc;
		}

		@Override
		public boolean process() {
			Map<String, Object> map = BaseLog("OpPoint");// 序列号(上报消息的连续编号)
			if (!BaseRoleLog(roleId, map)) {
				return true;
			}

			map.put("Point", InsID);// 任务ID
			map.put("Node", "-1");
			map.put("FinishFlag", FinishFlg);// 成功/失败(成功为succ,失败为fail)
			map.put("FailDesc", FailDesc);// 成功/失败原因(自定义字符串,可留空)[

			doLogWhileCommit(map);
			return true;
		}
	}

	public static void completeActivityLog(long roleId, int InsID, String FinishFlg, String FailDesc) {
		completeActivityLogImpl p = new completeActivityLogImpl(roleId, InsID, FinishFlg, FailDesc);
		if (Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(p);
		} else {
			p.submit();
		}
	}

	/**
	 * 完成任务触发事件
	 * 
	 * @param roleId
	 * @param InsID
	 * @param UsrLvl
	 * @param VipLvl
	 * @param FinishFlg
	 * @param FailDesc
	 */
	public static void completeTaskLog(long roleId, int InsID, String FinishFlg, String FailDesc) {
		completeTaskLogImpl p = new completeTaskLogImpl(roleId, InsID, FinishFlg, FailDesc);
		if (Transaction.current() != null) {
			mkdb.Procedure.pexecuteWhileCommit(p);
		} else {
			p.submit();
		}
	}

	/**
	 * 充值触发事件
	 * 
	 * @param roleId
	 * @param UsrLvl
	 * @param VipLvl
	 * @param OrId
	 * @param GoodId
	 * @param GoodQty
	 * @param MoneyAmnt
	 * @param RealAmnt
	 * @param GftAmnt
	 * @param MoneyType
	 * @param ChaDeviId
	 * @param RegDeviId
	 * @param PayCh
	 */
	public static void OpChargeLog(long roleId, String OrId, String GoodId, int GoodQty, int MoneyAmnt, int RealAmnt, int GftAmnt, String MoneyType, String ChaDeviId, String RegDeviId, String PayCh,
			int fstPay) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null) {
			return;
		}

		Integer userId = prop.getUserid();
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}

		Map<String, Object> map = BaseLog("OpCharge");// 序列号(上报消息的连续编号)
		BaseRoleLog(roleId, userinfo, prop, map);

		map.put("OrId", OrId);// 订单号
		map.put("GoodId", GoodId);// 如果是是RMB直接购买道具，请填写道具ID其他请填写-1
		map.put("GoodQty", GoodQty);// 商品购买数量
		map.put("MoneyAmnt", MoneyAmnt);// 充值金额
		map.put("RealAmnt", RealAmnt);// 真金，也就是实际获得(不含赠送)
		map.put("GftAmnt", GftAmnt);// 赠金，赠送金额
		map.put("MoneyType", MoneyType);// 货币类型(如RMB,usd)
		map.put("ChaDeviId", userinfo.getDeviid());// 充值设备标识
		map.put("RegDeviId", userinfo.getFirstdeviid());// 注册设备标识(注册时候mac/idfa)
														// 该功能用于黑卡筛选
		map.put("PayCh", PayCh);// 充值平台（支付宝,微信）
		map.put("Ip", userinfo.getIp()); // 玩家设备IP
		map.put("fstPay", fstPay); // 是否首次充值 0否，1是

		doLogWhileCommit(map);

	}

	/**
	 * 消耗使用物品日志
	 * 
	 * @param roleId
	 * @param item_info
	 * @param way
	 *            这个类型已经在fire.log.beans.ItemBean 中定义
	 */
	public static void OpItemUseLog(long roleId, ItemBean item_info, YYLoggerTuJingEnum way) {
		Map<String, Object> map = BaseLog("OpItemUse");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}
		map.put("way", way.toVal());// 自定义方式ID，请提供配表(购买，任务，关卡，礼品码，赠与，交易)
		map.put("type", "use");// 行为类别 use消耗/get获取
		map.put("Item", item_info);// 消耗道具

		doLogWhileCommit(map);
	}

	/**
	 * 获得物品日志
	 * 
	 * @param roleId
	 * @param item_info
	 * @param way
	 *            这个类型已经在该文件 中定义
	 */
	public static void OpItemGetLog(long roleId, ItemBean item_info, YYLoggerTuJingEnum way) {
		Map<String, Object> map = BaseLog("OpItemGet");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("way", way.toVal());// 自定义方式ID，请提供配表(购买，任务，关卡，礼品码，赠与，交易)
		map.put("type", "get");// 行为类别 use消耗/get获取
		map.put("Item", item_info);// 消耗道具

		doLogWhileCommit(map);
	}

	/**
	 * 消耗代币
	 * 
	 * @param roleId
	 * @param way
	 * @param TokType
	 * @param ChgAmnt
	 * @param AftAmnt
	 * @param item_info
	 */
	public static void OpTokenUseLog(long roleId, YYLoggerTuJingEnum way, int TokType, long ChgAmnt, long AftAmnt, ItemBean item_info) {
		Map<String, Object> map = BaseLog("OpToken");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		boolean isGet = false;
		if (way.isGet() != null) {
			isGet = way.isGet();
		}

		map.put("way", way.toVal());// 消耗途径
		map.put("type", isGet ? "get" : "use");// 类型(获取/消耗)
		map.put("TokType", TokType);// 代币类型（虚拟币既真金,赠金,绑金，钻石,体力等可多种，自定义配表）代币

		if ((ChgAmnt > 0 && !isGet) || (ChgAmnt < 0 && isGet)) {
			LogManager.logger.error("OpTokenLog error:" + " code=" + way.toVal());
		}

		map.put("ChgAmnt", Math.abs(ChgAmnt));// 代币变化数量
		map.put("AftAmnt", AftAmnt);// 代币变化后数量
		map.put("Item", item_info);// 获取/或消耗的道具数量（金币，符石，体力,道具包）

		doLogWhileCommit(map);
	}

	/**
	 * 获得代币
	 * 
	 * @param roleId
	 * @param way
	 * @param TokType
	 * @param ChgAmnt
	 * @param AftAmnt
	 * @param item_info
	 */
	public static void OpTokenGetLog(long roleId, YYLoggerTuJingEnum way, int TokType, long ChgAmnt, long AftAmnt, ItemBean item_info) {
		Map<String, Object> map = BaseLog("OpToken");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		boolean isGet = true;
		if (way.isGet() != null) {
			isGet = way.isGet();
		}

		map.put("way", way.toVal());// 途径
		map.put("type", isGet ? "get" : "use");// 类型(获取/消耗)
		map.put("TokType", TokType);// 代币类型（虚拟币既真金,赠金,绑金，钻石,体力等可多种，自定义配表）代币

		if ((ChgAmnt > 0 && !isGet) || (ChgAmnt < 0 && isGet)) {
			LogManager.logger.error("OpTokenLog error:" + " code=" + way.toVal());
		}

		map.put("ChgAmnt", Math.abs(ChgAmnt));// 代币变化数量
		map.put("AftAmnt", AftAmnt);// 代币变化后数量
		map.put("Item", item_info);// 获取/或消耗的道具数量（金币，符石，体力,道具包）

		doLogWhileCommit(map);
	}

	/**
	 * 创建角色日志
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param roleCreateBean
	 */
	public static void createRoleLog(long roleId, RoleCreateBean roleCreateBean) {
		Map<String, Object> map = BaseLog("CustoEvt");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EvtType", ROLE_CREATE_LOG);// 事件ID,（文字聊天，语音聊天，挂机,摆摊上架，摆摊下架,摆摊购买，摆摊时长。组队,光环,小伙伴。事件类型请提供配表）
		map.put("EvtValue", 0);// 事件值，如文字聊天,语音则EvtValue是使用次数。挂机，摆摊时长EvtValue则是时长。如果是组队EvtValue则是组队人数.小伙伴是上阵个数。
		map.put("Obj", roleCreateBean);// 产生其他事件的时候数组内容。],

		doLogWhileCommit(map);
	}

	/**
	 * 公会操作日志
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param factionOpbean
	 */
	public static void factionOPEventLog(long roleId, FactionOpbean factionOpbean, int WTrib) {
		Map<String, Object> map = BaseLog("OpGuild"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		fire.pb.skill.SkillRole skillrole = new fire.pb.skill.SkillRole(roleId, true);
		xbean.RoleClanPoint clanpoint = fire.pb.clan.ClanUtils.getRoleClanPoint(roleId, true);
		if (clanpoint == null) {
			return;
		}

		int taskSta = fire.pb.circletask.CircleTaskManager.getInstance().getGongHuiRenWuTimes(roleId);
		xbean.RuneInfo runeinfo = xtable.Runeinfotab.select(roleId);
		int quHitFuCnt = 0;
		int getHitFuCnt = 0;
		if (runeinfo != null) {
			quHitFuCnt = runeinfo.getDayrequestnum();
			getHitFuCnt = runeinfo.getAllacceptnum();
		}

		map.put("Guid", factionOpbean.getFactionkey()); // 公会id
		map.put("ChgType", factionOpbean.getOpid()); // 公会操作（创建公会、加入公会、退出公会、请出公会、成为新帮主、升降会阶级别、修改帮会介绍等）
		map.put("Prof", factionOpbean.getPosition()); // 公会职务

		// 新加的
		map.put("GuiMemCnt", factionOpbean.getGuiMemCnt()); // 所在公会人数#
		map.put("GuiLvl", factionOpbean.getGuiLvl()); // 公会等级#

		String buildLvlStr = "";
		if (skillrole.getPracticeSkills().entrySet() != null) {
			for (Entry<Integer, Integer> entry : factionOpbean.getBuildLvl()) {
				String rec = entry.getKey() + "@" + entry.getValue();
				buildLvlStr += rec + "|";
			}
		}

		if (buildLvlStr.length() > 0)
			buildLvlStr = buildLvlStr.substring(0, buildLvlStr.length() - 1);

		map.put("BuildLvl", buildLvlStr); // 所在建筑物等级

		map.put("WTrib", WTrib); // 本周帮贡
		map.put("NTrib", clanpoint.getCurrentclanpoint()); // 现在帮贡
		map.put("HisTrib", clanpoint.getHistoryclanpoint()); // 历史帮贡
		map.put("TaskSta", taskSta); // 公会任务完成情况-70个完成多少
		map.put("QuHitFuCnt", quHitFuCnt); // 请求公会打符次数
		map.put("GetHitFuCnt", getHitFuCnt); // 获得公会打符次数

		String disciLvl = "";
		if (skillrole.getPracticeSkills().entrySet() != null) {
			for (Entry<Integer, PracticeSkill> entry : skillrole.getPracticeSkills().entrySet()) {
				String rec = entry.getKey() + "@" + entry.getValue().getLevel() + "@" + entry.getValue().getExp();
				disciLvl += rec + "|";
			}
		}

		if (disciLvl.length() > 0)
			disciLvl = disciLvl.substring(0, disciLvl.length() - 1);

		map.put("DisciLvl", disciLvl); // 公会修炼-各修炼的等级

		String liveSkillStr = "";
		if (skillrole.getPracticeSkills().entrySet() != null) {
			for (Entry<Integer, LiveSkill> entry : skillrole.getLiveSkills().entrySet()) {
				String rec = entry.getKey() + "@" + entry.getValue().getLevel();
				liveSkillStr += rec + "|";
			}
		}

		if (liveSkillStr.length() > 0)
			liveSkillStr = liveSkillStr.substring(0, liveSkillStr.length() - 1);

		map.put("SkiLvl", liveSkillStr); // 公会技能-各技能等级
		doLogWhileCommit(map);
	}

	/**
	 * 公会建筑升级日志
	 * 
	 * @param roleId
	 * @param factionBuildUPBean
	 */
	public static void factionBuildUPLog(long roleId, ClanBuildUPBean factionBuildUPBean) {
		Map<String, Object> map = BaseLog("OpGuildLvl"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("GuiId", factionBuildUPBean.getFactionkey()); // 公会ID
		map.put("LvlType", factionBuildUPBean.getOpid()); // 升级类别（公会、金库、药房、旅馆）
		map.put("BfGuiLvl", factionBuildUPBean.getPrelevel()); // 升级前等级
		map.put("AfGuiLvl", factionBuildUPBean.getLevel()); // 升级后等级
		map.put("ConsuResce", factionBuildUPBean.getCost()); // 消耗公会资源

		doLogWhileCommit(map);
	}

	/**
	 * 光环经验日志(就是队伍里面的光环阵型)
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param addZhenfaExpBean
	 */
	public static void addTeamzhenfaExpLog(long roleId, AddZhenfaExpBean addZhenfaExpBean) {
		Map<String, Object> map = BaseLog("OpLightLoop"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("LightId", addZhenfaExpBean.getId()); // 光环ID
		map.put("BfExp", addZhenfaExpBean.getCurexp()); // 变化前经验没有记为-1
		map.put("ChgExp", addZhenfaExpBean.getAddexp());// 变化的经验
		map.put("AfExp", addZhenfaExpBean.getAfterexp());// 变化后经验没有记为-1
		map.put("Egettype", "-1"); // 获得途径 目前只有使用光环书

		doLogWhileCommit(map);
	}

	/**
	 * 成就系统快照
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param achievementBean
	 */
	public static void achivementLog(long roleId, long money, AchievementBean achievementBean) {
		Map<String, Object> map = BaseLog("OpAchiv");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("DirId", achievementBean.getId());// 事件ID,（文字聊天，语音聊天，挂机,摆摊上架，摆摊下架,摆摊购买，摆摊时长。组队,光环,小伙伴。事件类型请提供配表）
		map.put("Awards", achievementBean.getItems());// 奖励内容

		doLogWhileCommit(map);
	}

	/**
	 * 角色技能升级日志
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param roleSkillUpBean
	 */
	public static void roleSkillUPLog(long roleId, RoleSkillUpBean roleSkillUpBean) {
		Map<String, Object> map = BaseLog("OpSkill"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("Skillid", roleSkillUpBean.getSkillid()); // 技能ID
		map.put("SkillLvl", roleSkillUpBean.getLevel()); // 技能等级

		doLogWhileCommit(map);
	}

	/**
	 * 挂机日志
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 *            这里记录0开始挂机 1结束挂机
	 */
	public static void guaLog(long roleId, int EvtValue) {
		Map<String, Object> map = BaseLog("CustoEvt");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EvtType", GUA_LOG);// 事件ID,（文字聊天，语音聊天，挂机,摆摊上架，摆摊下架,摆摊购买，摆摊时长。组队,光环,小伙伴。事件类型请提供配表）
		map.put("EvtValue", EvtValue);// 事件值，如文字聊天,语音则EvtValue是使用次数。挂机，摆摊时长EvtValue则是时长。如果是组队EvtValue则是组队人数.小伙伴是上阵个数。

		doLogWhileCommit(map);
	}

	/**
	 * 战斗日志
	 * 
	 * @param roleId
	 * @param EvtType
	 * @param EvtValue
	 * @param fightBean
	 */
	public static void fightLog(long roleId, OpFightBean fightBean) {
		Map<String, Object> map = BaseLog("OpBattle"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("TeamId", fightBean.getTeamId()); // 队伍ID
		map.put("TeamAmd", fightBean.getTeamAmd()); // 组队人数
		map.put("Map", fightBean.getMap()); // 所在地图
		map.put("Result", fightBean.getResult()); // 战斗结果（胜利、失败）
		map.put("FailDesc", fightBean.getFailDesc()); // 成功/失败原因(自定义)
		map.put("BattleId", fightBean.getBattleId()); // 战斗唯一ID

		doLogWhileCommit(map);
	}

	/**
	 * 邮件日志
	 * 
	 * @param roleId
	 * @param mailBean
	 */
	public static void mailLog(long roleId, MailBean mailBean) {
		Map<String, Object> map = BaseLog("OpMail"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("MailScn", mailBean.getMailid()); // 邮件编号
		map.put("Items", mailBean.getItems()); // 邮件道具

		doLogWhileCommit(map);
	}

	/**
	 * 签到日志
	 * 
	 * @param roleId
	 * @param signBean
	 */
	public static void signingLog(long roleId, RegBean signBean) {
		Map<String, Object> map = BaseLog("OpSign"); // 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("SignType", signBean.getOpid()); // 签到类型（每日和七日）
		map.put("TotlSignCnt", signBean.getNum()); // 累计签到次数
		map.put("Items", signBean.getItems()); // 道具信息

		doLogWhileCommit(map);
	}

	/**
	 * 在线日志
	 * 
	 * @param num
	 */
	public static void onlineLog(int num) {
		Map<String, Object> map = BaseLog("OpOnline");// 序列号(上报消息的连续编号)

		map.put("UserCnt", num);// 事件值，如文字聊天,语音则EvtValue是使用次数。挂机，摆摊时长EvtValue则是时长。如果是组队EvtValue则是组队人数.小伙伴是上阵个数。

		doLogWhileCommit(map);
	}

	/**
	 * 道具分解
	 * 
	 * @param roleId
	 * @param resolveItemBean
	 */
	public static void resolveItemLog(long roleId, ResolveItemBean resolveItemBean) {
		Map<String, Object> map = BaseLog("CustoEvt");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EvtType", ITEM_RESOLVE_LOG);// 事件ID,（文字聊天，语音聊天，挂机,摆摊上架，摆摊下架,摆摊购买，摆摊时长。组队,光环,小伙伴。事件类型请提供配表）
		map.put("EvtValue", 0);// 事件值，如文字聊天,语音则EvtValue是使用次数。挂机，摆摊时长EvtValue则是时长。如果是组队EvtValue则是组队人数.小伙伴是上阵个数。
		map.put("Obj", resolveItemBean);// 奖励物品

		doLogWhileCommit(map);
	}

	/**
	 * 道具合成
	 * 
	 * @param roleId
	 * @param compareItemBean
	 */
	public static void compareItemLog(long roleId, CompareItemBean compareItemBean) {
		Map<String, Object> map = BaseLog("CustoEvt");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EvtType", ITEM_COMPTER_LOG);// 事件ID,（文字聊天，语音聊天，挂机,摆摊上架，摆摊下架,摆摊购买，摆摊时长。组队,光环,小伙伴。事件类型请提供配表）
		map.put("EvtValue", 0);// 事件值，如文字聊天,语音则EvtValue是使用次数。挂机，摆摊时长EvtValue则是时长。如果是组队EvtValue则是组队人数.小伙伴是上阵个数。
		map.put("Obj", compareItemBean);// 产生其他事件的时候数组内容。],

		doLogWhileCommit(map);
	}

	/****************************************************
	 * 新增日志 add by yebin
	 ****************************************************/

	/**
	 * 组队
	 */
	public static void teamLog(long roleId, TeamOpBean teamOpBean) {
		Map<String, Object> map = BaseLog("OpTeam");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("TeamId", teamOpBean.getTeamId()); // 队伍ID
		map.put("InsId", teamOpBean.getInsId()); // 组队刷的任务id
		map.put("LightId", teamOpBean.getLightId()); // 组队光环ID
		map.put("LightLvl", teamOpBean.getLightLvl()); // 组队光环等级
		map.put("TeaMemCnt", teamOpBean.getTeaMemCnt()); // 组队人数
		map.put("MemPro", teamOpBean.getMemPro()); // 组队时人员职业分布
		map.put("Race", teamOpBean.getRace());// 组队时人员种族分布
		map.put("TeamDur", teamOpBean.getTeamDur()); // 组队时长
		map.put("TeamLed", teamOpBean.getTeamLed()); // 组队时人员职业分布（包含助战）
		map.put("Map", teamOpBean.getMapId()); // 所在地图ID
		map.put("BattleId", teamOpBean.getBattleId()); // 战斗唯一ID

		doLogWhileCommit(map);
	}

	/**
	 * 社交关系
	 * 
	 * @param roleId
	 * @param sNSOpBean
	 */
	public static void SNSLog(long roleId, SNSOpBean sNSOpBean) {
		Map<String, Object> map = BaseLog("OpSNS");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("FriCnt", sNSOpBean.getFriCnt()); // 好友个数
		map.put("MasUsrId", sNSOpBean.getMasUsrId()); // 师傅UserID（没有记为空）
		map.put("PreUsrId", sNSOpBean.getPreUsrId()); // 徒弟UserID（没有记为空）
		map.put("CouplUsrId", sNSOpBean.getCouplUsrId()); // 妻子/丈夫UserID（没有记为空）
		map.put("MasVal", sNSOpBean.getMasVal()); // 良师值
		map.put("IntiVal", sNSOpBean.getIntiVal()); // 夫妻亲密值
		map.put("MaPrTask", sNSOpBean.getMaPrTask()); // 师徒任务完成项
		map.put("CoupSkLvl", sNSOpBean.getCoupSkLvl()); // 各夫妻技能等级

		doLogWhileCommit(map);
	}

	/**
	 * 人物加点
	 * 
	 * @param roleId
	 * @param sNSOpBean
	 */
	public static void characLog(long roleId, int phych, int magich, int stro, int endu, int flexch) {
		Map<String, Object> map = BaseLog("OpCharac");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("PhyCh", phych); // 体质目前加点值
		map.put("MagiCh", magich); // 魔力目前加点值
		map.put("Stro", stro); // 力量目前加点值
		map.put("Endu", endu); // 耐力目前加点值
		map.put("FlexCh", flexch); // 敏捷目前加点值

		doLogWhileCommit(map);
	}

	/**
	 * 装备打造
	 * 
	 * @param roleId
	 * @param opEquiMakBean
	 */
	public static void equiMakLog(long roleId, OpEquiMakBean opEquiMakBean) {
		Map<String, Object> map = BaseLog("OpEquiMak");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EquID", opEquiMakBean.getEquID()); // 所要打造的装备ID
		map.put("EquLvl", opEquiMakBean.getEquLvl()); // 所要打造的装备等级
		map.put("IsTrea", opEquiMakBean.getIsTrea()); // 是否珍品 0否，1是
		map.put("ItKey", opEquiMakBean.getItKey()); // 珍品系列字段（如果是珍品的话）
		map.put("ConsuBookId", opEquiMakBean.getConsuBookId()); // 消耗制造书ID
		map.put("ConsuBookNum", opEquiMakBean.getConsuBookNum()); // 消耗制造书-数量
		map.put("ConsuTrId", opEquiMakBean.getConsuTrId()); // 消耗铁ID
		map.put("ConsuTrNum", opEquiMakBean.getConsuTrNum()); // 消耗铁数量
		map.put("ConsuFuId", opEquiMakBean.getConsuFuId()); // 消耗打造符ID
		map.put("ConsuFuNum", opEquiMakBean.getConsuFuNum()); // 消耗打造符数量
		map.put("ConsuStoId", opEquiMakBean.getConsuStoId()); // 消耗原石ID（为可选项，没有就空）
		map.put("ConsuStoNum", opEquiMakBean.getConsuStoNum()); // 消耗原石数量（为可选项，没有就空）
		map.put("ConsuSilId", opEquiMakBean.getConsuSilId()); // 消耗银币ID
		map.put("ConsuSilNum", opEquiMakBean.getConsuSilNum()); // 消耗银币数量

		doLogWhileCommit(map);
	}

	/**
	 * 装备镶嵌
	 * 
	 * @param roleId
	 * @param equIDs
	 */
	public static void equiEmbLog(long roleId, Set<Entry<Integer, ArrayList<Integer>>> equIDs) {
		Map<String, Object> map = BaseLog("OpEquiEmb");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EquIDs", equIDs); // 身上的六件装备-id

		doLogWhileCommit(map);
	}

	/**
	 * 装备修理
	 * 
	 * @param roleId
	 * @param opEquiRepBean
	 */
	public static void equiRepLog(long roleId, OpEquiRepBean opEquiRepBean) {
		Map<String, Object> map = BaseLog("OpEquiRep");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("EquID", opEquiRepBean.getEquID()); // 所要修理的装备ID
		map.put("EquLvl", opEquiRepBean.getEquLvl()); // 所要修理的装备等级
		map.put("IsTrea", opEquiRepBean.getIsTrea()); // 是否珍品 0否，1是
		map.put("ItKey", opEquiRepBean.getItKey()); // 珍品系列字段（如果是珍品的话）
		map.put("AftReDegre", opEquiRepBean.getAftReDegre()); // 修理之后耐久度
		map.put("RepType", opEquiRepBean.getRepType()); // 修理类型：0普通/1特殊

		doLogWhileCommit(map);
	}

	/**
	 * 拥有宝宝明细数据
	 * 
	 * @param roleId
	 * @param opPetBean
	 */
	public static void petLog(long roleId, OpPetBean opPetBean) {
		Map<String, Object> map = BaseLog("OpPet");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("PetId", opPetBean.getPetId()); // 宝宝id（包含npc仓库里的宝宝）
		map.put("PetLvl", opPetBean.getPetLvl()); // 宝宝等级
		map.put("IsBind", opPetBean.getIsBind()); // 是否绑定 0未绑定",
		map.put("IsBatt", opPetBean.getIsBatt()); // 是否参战 0未，1是
		map.put("PetSco", opPetBean.getPetSco()); // 宝宝评分
		map.put("Bloo", opPetBean.getBloo()); // 气血上限
		map.put("Magi", opPetBean.getMagi()); // 魔法上限
		map.put("IsTrea", opPetBean.getIsTrea()); // 是否珍品 0否，1是
		map.put("ItKey", opPetBean.getItKey()); // 珍品系列字段（如果是珍品的话）
		map.put("PhyCh", opPetBean.getPhyCh()); // 体质目前加点值
		map.put("MagiCh", opPetBean.getMagiCh()); // 魔力目前加点值
		map.put("Stro", opPetBean.getStro()); // 力量目前加点值
		map.put("Endu", opPetBean.getEndu()); // 耐力目前加点值
		map.put("FlexCh", opPetBean.getFlexCh()); // 敏捷目前加点值
		map.put("QA", opPetBean.getQA()); // 7项资质数据
		map.put("SkilSta", opPetBean.getSkilSta()); // 拥有的技能列表（记录每项技能id+技能状态（分为认证和非认证））

		doLogWhileCommit(map);
	}

	/**
	 * 合宠
	 * 
	 * @param roleId
	 * @param OpPetMixBean
	 */
	public static void petMixLog(long roleId, OpPetMixBean opPetMixBean) {
		Map<String, Object> map = BaseLog("OpPetMix");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("MixPet", opPetMixBean.getMixPet()); // 合宠-宝宝id（判断是否为珍品，是珍品增加珍品字段）
		map.put("MixedPetId", opPetMixBean.getMixedPetId()); // 合成后宝宝id（判断是否为珍品，是珍品增加珍品字段
		map.put("IsTrea", opPetMixBean.getIsTrea()); // 是否珍品 0否，1是

		doLogWhileCommit(map);
	}

	/**
	 * 宝宝放生
	 * 
	 * @param roleId
	 * @param PetId
	 * @param IsTrea
	 * @param ItKey
	 */
	public static void petFreeLog(long roleId, int PetId, int IsTrea, long ItKey) {
		Map<String, Object> map = BaseLog("OpPetFree");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("PetId", PetId); // 放生宝宝id
		map.put("IsTrea", IsTrea); // 是否珍品 0否，1是
		map.put("ItKey", ItKey); // 物品唯一key

		doLogWhileCommit(map);
	}

	/**
	 * 宝宝培养-洗练
	 * 
	 * @param roleId
	 * @param PetId
	 * @param IsTrea
	 * @param ItKey
	 * @param RefWaterId
	 */
	public static void petRefLog(long roleId, int PetId, int IsTrea, long ItKey, int RefWaterId) {
		Map<String, Object> map = BaseLog("OpPetRef");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("PetId", PetId); // 放生宝宝id
		map.put("IsTrea", IsTrea); // 是否珍品0否，1是
		map.put("ItKey", ItKey); // 宠物唯一id
		map.put("RefWaterId", RefWaterId); // 洗练水ID（太阳井水、永恒井水）

		doLogWhileCommit(map);
	}

	/**
	 * 宝宝培养-学技能
	 * 
	 * @param roleId
	 * @param PetId
	 * @param IsTrea
	 * @param SkiId
	 * @param CerMagiId
	 * @param CancMagiId
	 */
	public static void petSkiLog(long roleId, OpPetSkiBean opPetSkiBean) {
		Map<String, Object> map = BaseLog("OpPetSki");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("PetId", opPetSkiBean.getPetId()); // 放生宝宝id
		map.put("IsTrea", opPetSkiBean.getIsTrea()); // 是否珍品0否，1是
		map.put("ItKey", opPetSkiBean.getItKey()); // ItKey
		map.put("SkiId", opPetSkiBean.getSkiId()); // 学习宝宝技能ID
		map.put("OpId", opPetSkiBean.getOpId()); // 操作类型

		doLogWhileCommit(map);
	}

	/**
	 * 宝宝培养-培养
	 * 
	 * @param roleId
	 * @param ItemId
	 * @param ItemNum
	 * @param PropId
	 */
	public static void petTraLog(long roleId, OpPetTraBean opPetTraBean) {
		Map<String, Object> map = BaseLog("OpPetTra");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("ItemId", opPetTraBean.getItemId()); // 培养所用的物品ID
		map.put("ItemNum", opPetTraBean.getItemNum()); // 培养所用的物品个数
		map.put("PropId", opPetTraBean.getPropId()); // 使用在哪项属性上（使用属性id—）

		doLogWhileCommit(map);
	}

	/**
	 * 符石商店：玩家购买行为
	 * 
	 * @param roleId
	 * @param opTokShopBean
	 */
	public static void tokShopLog(long roleId, OpTokShopBean opTokShopBean) {
		Map<String, Object> map = BaseLog("OpTokShop");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("Oper", opTokShopBean.getOper()); // 0购买/1出售
		map.put("ClaId", opTokShopBean.getClaId()); // 物品类id
		map.put("IteId", opTokShopBean.getIteId()); // 物品id
		map.put("IteAmt", opTokShopBean.getIteAmt()); // 物品数量
		map.put("TokTyp", opTokShopBean.getTokTyp()); // 货币类型
		map.put("TokChg", opTokShopBean.getTokChg()); // 货币量
		map.put("Off", opTokShopBean.getOff()); // 折扣（几折）
		map.put("GetTop", opTokShopBean.getGetTop()); // 购买量是否当日达到当日该物品购买上限0否，1是

		doLogWhileCommit(map);
	}

	/**
	 * 金币拍卖所：交易
	 * 
	 * @param roleId
	 * @param flag
	 * @param map
	 */
	private static void BaseTradExLog(long roleId, String flag, Map<String, Object> map) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null) {
			return;
		}

		Integer userId = prop.getUserid();
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}

		map.put(flag + "Sid", Gs.serverid);// 游戏区服ID
		map.put(flag + "ChId", userinfo.getChid());// 渠道ID
		map.put(flag + "UsrId", roleId);// 玩家UserID
		map.put(flag + "AccId", prop.getPlatformuid()); // 玩家roleId
		map.put(flag + "PlatType", userinfo.getPlattype());// 平台ID(IOS,android,wp)

		map.put(flag + "Lvl", prop.getLevel());// 用户等级
		map.put(flag + "VipLvl", prop.getViplevel());// VIP等级
		map.put(flag + "Exp", prop.getExp()); // 当前经验值
	}

	public static void tradExLog(long buyRoleId, long sellRoleId, OpTradExBean opTradExBean) {
		Map<String, Object> map = BaseLog("OpTradEx");
		BaseTradExLog(buyRoleId, "", map);
		BaseTradExLog(sellRoleId, "Sell", map);

		map.put("ItemId", opTradExBean.getItemId()); // 物品id
		map.put("ItClasId", opTradExBean.getItClasId()); // 物品类id
		map.put("ItQA", opTradExBean.getItQA()); // 物品品质
		map.put("IsTrea", opTradExBean.getIsTrea()); // 是否珍品0否，1是
		map.put("ItKey", opTradExBean.getItKey()); // 珍品系列字段（如果是珍品的话）
		map.put("Pric", opTradExBean.getPric()); // 物品单价
		map.put("TraItAmt", opTradExBean.getTraItAmt()); // 交易物品数量
		map.put("TraMonTyp", opTradExBean.getTraMonTyp()); // 交易货币类型
		map.put("MonChg", opTradExBean.getMonChg()); // 货币量
		map.put("TaxMonTyp", opTradExBean.getTaxMonTyp()); // 出售者税收货币类型
		map.put("TaxAmt", opTradExBean.getTaxAmt()); // 出售者税收量
		map.put("Barg", opTradExBean.getBarg()); // 出售者物品定价建议价格百分比

		// [不需要此常用字段]
		map.remove("Sid");

		doLogWhileCommit(map);
	}

	/**
	 * 金币拍卖所-搜索
	 * 
	 * @param roleId
	 * @param SeaItId
	 */
	public static void aucSeacLog(long roleId, int itemId) {
		Map<String, Object> map = BaseLog("OpAucSeac");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("SeaItId", itemId); // 搜索物品id

		doLogWhileCommit(map);
	}

	/**
	 * 金币拍卖所-筛选
	 * 
	 * @param roleId
	 * @param FilMainId
	 * @param FilSubId
	 */
	public static void aucFilLog(long roleId, int FilMainId, int FilSubId) {
		Map<String, Object> map = BaseLog("OpAucFil");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("FilMainId", FilMainId); // 筛选类目主目录id
		map.put("FilSubId", FilSubId); // 筛选类目次目录id

		doLogWhileCommit(map);
	}

	/**
	 * 金币拍卖所-关注
	 * 
	 * @param roleId
	 * @param opAucConBean
	 */
	public static void aucConLog(long roleId, OpAucConBean opAucConBean) {
		Map<String, Object> map = BaseLog("OpAucCon");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("ItMaiPag", opAucConBean.getItMaiPag()); // 物品归类主页签id
		map.put("ItSubPag", opAucConBean.getItSubPag()); // 物品归类次页签id
		map.put("ItemId", opAucConBean.getItemId()); // 物品id
		map.put("ItClasId", opAucConBean.getItClasId()); // 物品类id
		map.put("ItQA", opAucConBean.getItQA()); // 物品品质
		map.put("IsTrea", opAucConBean.getIsTrea()); // 是否珍品0否，1是
		map.put("ItKey", opAucConBean.getItKey()); // 唯一ID
		map.put("ConStat", opAucConBean.getConStat()); // 关注状态（1关注\0取消关注
		map.put("ConItCnt", opAucConBean.getConItCnt()); // 已关注总物品数

		doLogWhileCommit(map);
	}

	/**
	 * 金币拍卖所-上下架
	 * 
	 * @param roleId
	 * @param opAucRackBean
	 */
	public static void aucRackLog(long roleId, OpAucRackBean opAucRackBean) {
		Map<String, Object> map = BaseLog("OpAucRack");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("RacStat", opAucRackBean.getRacStat()); // “1上/0下”架
		map.put("ItSubPag", opAucRackBean.getItSubPag()); // 物品归类次页签id
		map.put("OfRacDesc", opAucRackBean.getOfRacDesc()); // 下架原因（1自动、2手动）
		map.put("StaUsCnt", opAucRackBean.getStaUsCnt()); // 我的摊位使用数
		map.put("ItemId", opAucRackBean.getItemId()); // 物品id
		map.put("ItClasId", opAucRackBean.getItClasId()); // 物品类id
		map.put("ItQA", opAucRackBean.getItQA()); // 物品品质
		map.put("IsTrea", opAucRackBean.getIsTrea()); // 是否珍品0否，1是
		map.put("ItKey", opAucRackBean.getItKey()); // 珍品系列字段（如果是珍品的话）
		map.put("IsDur", opAucRackBean.getIsDur()); // 是否公示期0无，1是

		doLogWhileCommit(map);
	}

	/**
	 * 银币商店-买卖
	 * 
	 * @param roleId
	 * @param opSilShopBean
	 */
	public static void silShopLog(long roleId, OpSilShopBean opSilShopBean) {
		Map<String, Object> map = BaseLog("OpSilShop");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("IsTran", opSilShopBean.getIsTran()); // 交易“1买/2卖”状态
		map.put("ItemId", opSilShopBean.getItemId()); // 物品id
		map.put("ItClasId", opSilShopBean.getItClasId()); // 物品类id
		map.put("ItQA", opSilShopBean.getItQA()); // 物品品质
		map.put("ItNum", opSilShopBean.getItNum()); // 物品数量
		map.put("MonTyp", opSilShopBean.getMonTyp()); // 交易货币类型
		map.put("MonAmt", opSilShopBean.getMonAmt()); // 货币量
		map.put("ItOrPric", opSilShopBean.getItOrPric()); // 物品原价
		map.put("Up", opSilShopBean.getUp()); // 涨幅
		map.put("ItNowPric", opSilShopBean.getItNowPric()); // 成交现价
		map.put("isUp", opSilShopBean.getIsUp()); // 购买量是否当日达到当日上限0无，1是
		map.put("isSuc", opSilShopBean.getIsSuc()); // 交易是否成功0非，1成功
		map.put("FaiDesc", opSilShopBean.getFaiDesc()); // 失败原因

		doLogWhileCommit(map);
	}

	/**
	 * 聊天记录
	 */
	public static void chatLog(long roleId, int gameChId) {
		Map<String, Object> map = BaseLog("OpChat");// 序列号(上报消息的连续编号)
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("GameChid", gameChId); // 频道（系统、组队频道除外，私聊也除外）

		doLogWhileCommit(map);
	}

	/**
	 * 用户标签
	 * 
	 * @param roleId
	 */
	public static void usrFlgLog(long roleId, String evt) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop == null) {
			return;
		}

		Integer userId = prop.getUserid();
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}

		Map<String, Object> map = BaseLog("OpUsrFlg");
		BaseRoleLog(roleId, userinfo, prop, map);

		map.put("DeviId", userinfo.getDeviid());// 玩家设备ID
		map.put("Evt", evt);

		doLogWhileCommit(map);
	}

	/**
	 * 红包日志
	 * 
	 * @param roleId
	 * @param opShareBean
	 */
	public static void bonusLog(long roleId, OpBonusBean opShareBean) {
		Map<String, Object> map = BaseLog("OpBonus");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("BonuType", opShareBean.getBonuType()); // 红包类型（世界红包、公会红包、队伍红包）
		map.put("Payflg", opShareBean.getPayflg()); // 获得或发放标记(标记是获取get/发放use)
		map.put("TokType", opShareBean.getTokType()); // 代币类型（金币或者符石 看配表）代币
		map.put("ChgAmnt", opShareBean.getChgAmnt()); // 代币变化数量
		map.put("BonuxCnt", opShareBean.getBonuxCnt()); // 红包个数
		map.put("SndUsrId", opShareBean.getSndUsrId()); // 红包发送者roleid

		doLogWhileCommit(map);
	}

	/**
	 * 新手战斗行为日志(暂未使用)
	 * 
	 * @param roleId
	 * @param opNoviBattleBean
	 */
	public static void noviBattleLog(long roleId, OpNoviBattleBean opNoviBattleBean) {
		Map<String, Object> map = BaseLog("OpNoviBattle");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}

		map.put("TaskNo", opNoviBattleBean.getTaskNo()); // 任务编号（180134、180138、180147、180149）
		map.put("FinishFlag", opNoviBattleBean.getFinishFlag()); // 成功/失败(成功为succ,失败为fail)
		map.put("FailDesc", opNoviBattleBean.getFailDesc()); // 成功/失败原因(自定义字符串,可留空)[
		map.put("MapId", opNoviBattleBean.getMapId()); // 地图ID
		map.put("IsTeam", opNoviBattleBean.getIsTeam()); // 是否组队1是，0否
		map.put("IsAssBatl", opNoviBattleBean.getIsAssBatl()); // 是否有助战1是，0否
		map.put("AssBatlId", opNoviBattleBean.getAssBatlId()); // 助战ID
		map.put("IsAutBatl", opNoviBattleBean.getIsAutBatl()); // 是否自动战斗1是，0否
		map.put("BatlCom", opNoviBattleBean.getBatlCom()); // 战斗指令

		doLogWhileCommit(map);
	}

	/**
	 * 角色名字(含中文等字符等)日志: (字符集utf8)
	 */
	public static void rolNameLog(int userId, long roleId,int Fstlogin) {
		xbean.UserDeviceInfo userinfo = xtable.Userdeviceinfotab.select(userId);
		if (userinfo == null) {
			return;
		}
		String propname = "";
		int prof = 0;
		int race = 0;
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (prop != null) {
			prof = prop.getSchool();
			race = prop.getShape();
			propname=prop.getRolename();
		}
		Map<String, Object> map = BaseLog("RolName");
		
		long allybNum = 0;// 充值符石数量
		long curyb = 0; // 当前符石数量
		if (roleId != 0) {
			if (!BaseRoleLog(roleId, map)) {
				return;
			}
			if (fire.pb.fushi.Module.getIsYYBUser(userId)) {
				// 记录符石数,并判断是否需要返符石
				xbean.YybFushiNums yybFs = xtable.Yybfushi.select(userId);
				if (yybFs != null) {
					xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleId);
					if (yybFushiNum != null) {
						allybNum = yybFushiNum.getSaveamt();// 充值符石数量
						curyb = yybFushiNum.getBalance();// 可交易符石数量
					}
				}
			} else {
				xbean.YbNums ybNums = xtable.Fushinum.select(userId);
				if (ybNums != null) {
					xbean.YbNum ybNum = ybNums.getRoleyb().get(roleId); // 得到角色对应的符石数量
					if (ybNum != null) {
						allybNum = ybNum.getNopresentnum();// 充值符石数量
						curyb = ybNum.getNum();// 可交易符石数量
					}
				}
			}
		}

		map.put("UsrId", roleId);// 玩家UserID
		map.put("UserName", propname);// 货币类型(如RMB,usd)
		map.put("DeviId", userinfo.getDeviid());// 玩家设备(mac/idfv)设备唯一标识"DeviId": "aklsdjfl",          //玩家设备(mac/idfv)设备唯一标识 
		map.put("RegDeviId", userinfo.getFirstdeviid());// 注册设备标识(注册时候mac/idfa) "RegDeviId": "1231asdfad",	 //注册设备标识
		map.put("RemAmnt", curyb);// 身上剩余金币(游戏内主要代币)主要是符石
	    map.put("TotlCha", allybNum / 10);// "TotlCha:":"234"       //记录上玩家累计充 值的虚拟币，这是个累加的概念
	    map.put("Fstlogin", Fstlogin);// 是否是初次登陆(0初次,1非初次)"Fstlogin": 0,                 //是否是初次登陆(0初次,1非初次)
	    map.put("Prof", prof); // 职业类型
		map.put("races", race); // 种族类型

		doLogWhileCommit(map);
	}
	
	/**
	 * 加速外挂(判断是否使用外挂)
	 * yebin added @ 2016年8月5日 下午8:13:37
	 * @param roleId
	 * @param mapId
	 * @param teamId
	 */
	public static void waiGuaLog(long roleId, int mapId, long teamId, int isKick) {
		Map<String, Object> map = BaseLog("OpWaiGua");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}
		
		map.put("Type", roleId);// 外挂类型  1加速 
		map.put("Map", mapId);// 所在地图
		map.put("TeamId", teamId);// 队伍id
		map.put("IsKick", isKick);// 是否踢人
		
		doLogWhileCommit(map);
	}

	/**************************
	 ******** 点卡服特有日志 ********
	 **************************/

	/**
	 * 点卡服金币交易所
	 * 
	 * @param roleId
	 * @param opPointGoldExBean
	 */
	public static void pointGoldExLog(long roleId, OpPointGoldExBean opPointGoldExBean) {
		Map<String, Object> map = BaseLog("OpPointGoldEx");
		if (!BaseRoleLog(roleId, map)) {
			return;
		}
		// 操作类型（1求购/2寄卖）
		int type = 1;
		if (opPointGoldExBean.getTypte() == SpotCheckManage.TYPE_SELL) {
			type = 2;
		}
		map.put("Typte", type);// 操作类型（1求购/2寄卖）
		map.put("ChgAmnt", opPointGoldExBean.getChgAmnt());// 交易数量
		map.put("Price", opPointGoldExBean.getPrice());// 货币单价
		map.put("Fee", opPointGoldExBean.getFee());// 手续费
		map.put("IsOK", opPointGoldExBean.getIsOK());// 是否成功（取消挂单为失败）0失败，1成功

		doLogWhileCommit(map);
	}

	/**
	 * 格式转换
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatLoggerString(Object obj) {
		try {
			return JSONObject.fromObject(obj).toString();
		} catch (net.sf.json.JSONException e) {
			return JSONArray.fromObject(obj).toString();
		} catch (Exception e) {
			fire.log.LogManager.logger.error("YYLogger格式转换错误!!!");
		}
		return "";
	}

	public static void doLogWhileCommit(Map<String, Object> map) {
		JSONObject jsonObj = null;
		String msg;
		try {
			jsonObj = JSONObject.fromObject(map);
			msg = jsonObj.toString();
		} catch (JSONException e) {
			fire.log.LogManager.logger.error("YYLoggerDoLogWhileCommitJSONError");
			msg = "";
		} catch (Exception e) {
			fire.log.LogManager.logger.error("YYLoggerDoLogWhileCommitError");
			msg = "";
		}

		if (Transaction.current() != null) {
			Procedure.ppostWhileCommit(new LogWhileProcedureEnd(msg));
		} else {
			logger.info(msg);
		}
	}

	public static class LogWhileProcedureEnd extends Task {
		private String message;

		public LogWhileProcedureEnd(String message) {
			super();
			this.message = message;
		}

		@Override
		public void run() {
			logger.info(message);
		}
	}
}