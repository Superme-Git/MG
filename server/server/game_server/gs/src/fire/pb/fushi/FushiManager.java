
package fire.pb.fushi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import mkdb.Procedure;
import mkdb.TTable.IWalk;

import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import xbean.YbNum;

import com.locojoy.base.Marshal.OctetsStream;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.statistics.StatisticUtil;
import fire.pb.util.DateValidate;
import fire.pb.util.FireProp;
import gnet.link.Onlines;

public class FushiManager {
	public static final int MAX_NUM = 1999999999;// 符石数量上限
	
	public static long FIRST_CHARGE_START_TIME = 0;//首冲的起始时间,配置在sys.properties中
	
	public static long MULTICHARGE_CHARGE_START_TIME = 0;//单档充值翻倍起始时间,配置在sys.properties
	
	public static long FIRST_CHARGE_CLEAR_PRESENT_START_TIME = 0;//首冲的起始时间,配置在sys.properties中

	public static String YYBAddress = "";
	public static String YYBGet = "";
	public static String YYBAdd = "";
	public static String YYBSub = "";
	public static Integer YYBOp = 0;
	
	public static final Logger logger = Logger.getLogger("RECHARGE");
	
	public static final Logger warnlogger = Logger.getLogger("FUSHIWARN");

	private static FushiManager instance = new FushiManager();

	//平台ID， 商品信息 by changhao
	public Map<String, Map<Integer, LinkedList<fire.pb.fushi.GoodInfo>>> goodListMap = new HashMap<String, Map<Integer, LinkedList<fire.pb.fushi.GoodInfo>>>();
	
	/**
	 * 应用宝赠送元宝额度 在充值配置表中的id固定的是: 101 102 103 104 105 106 107 108,跟策划对过,应用宝用的是安卓通用配置
	 */
	private static Map<Integer, Integer> yybGenBalance = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
		public int compare(Integer key1, Integer key2) {
			return key2 - key1;
		}
	});
	
	//首充符石数
	public static Map<Integer, Integer> shouchongMap = new HashMap<Integer, Integer>();
	public static float returnRatio = 1.2f;
	public static int returnZoneId = 0;
	
	
	public static FushiManager getInstance() {
		return instance;
	}

	public void init() throws Exception {
		final boolean isdirect = ConfigManager.getChargeDirect();
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				if(isdirect) {
					//TCP直充,不再轮询
					logger.info("乐动TCP方式直接回调!");
				} else {
					final List<Long> keys = new ArrayList<Long>();
					xtable.Chargeorder.getTable().walk(new IWalk<Long, xbean.ChargeOrder>() {
						@Override
						public boolean onRecord(Long k, xbean.ChargeOrder v) {
							keys.add(k);
							return true;
						}
					});
					for (Long gameSn : keys) {
						mkdb.Executor.getInstance().schedule(new CheckCharge(gameSn), 2, TimeUnit.MINUTES);
					}
				}
				return true;
			}

		}.submit();
		initChargeGoodList();
		Module.initCreditPoint();
		DayPayManager.daypayLevel = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(3).serverdata;
		fire.pb.fushi.SCommonDayPay c = ConfigManager.getInstance().getConf(fire.pb.fushi.SCommonDayPay.class).get(9);
		if (c != null)
		{
			DayPayManager.promptLevel = c.serverdata;					
		}
	}

	public void initChargeGoodList() throws Exception{
		YYBAddress = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.plat.yybAddress");
		YYBGet = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.plat.yybGet");
		YYBAdd = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.plat.yybAdd");
		YYBSub = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.plat.yybSub");
		
		YYBOp = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.plat.yybOp");
		
		String str = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.firstcharge.starttime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		FIRST_CHARGE_START_TIME = sdf.parse(str).getTime();
		str = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.firstcharge.clearpresent.starttime");
		FIRST_CHARGE_CLEAR_PRESENT_START_TIME = sdf.parse(str).getTime();
		
		String multiStr = ConfigManager.getInstance().getPropConf("sys").getProperty("sys.multicharge.starttime");
		MULTICHARGE_CHARGE_START_TIME = sdf.parse(multiStr).getTime();
		
		goodListMap.clear();

		if(fire.pb.fushi.Module.GetPayServiceType() == 0){
			//非点卡服
			Map<Integer, SAddCash> rawData = ConfigManager.getInstance().getConf(SAddCash.class);
			for (Entry<Integer, SAddCash> entry : rawData.entrySet()) {
				SAddCash sAddCash = entry.getValue();
				Map<Integer, LinkedList<GoodInfo>> platGoodInfos = goodListMap.get(sAddCash.roofid);
				if (platGoodInfos == null) {
					platGoodInfos = new HashMap<Integer, LinkedList<GoodInfo>>();
					goodListMap.put(sAddCash.roofid, platGoodInfos);
				}
				LinkedList<GoodInfo> serverGoodInfos = platGoodInfos.get(sAddCash.serverid);
				if (serverGoodInfos == null) {
					serverGoodInfos = new LinkedList<GoodInfo>();
					platGoodInfos.put(sAddCash.serverid, serverGoodInfos);
				}
				serverGoodInfos.add(new GoodInfo(sAddCash.id, sAddCash.sellpricenum, sAddCash.sellnum, sAddCash.sellnummore, 0));
			
				//应用宝处理
				if (entry.getKey() == 101 || entry.getKey() == 102 
				|| entry.getKey() == 103 || entry.getKey() == 104 
				|| entry.getKey() == 105 || entry.getKey() == 106
				|| entry.getKey() == 107 || entry.getKey() == 108) {
					yybGenBalance.put(sAddCash.getSellnum(), sAddCash.getSellnummore());
				}
			}
		}else{
			//点卡服
			Map<Integer, SAddCashPCard> rawDataPCard = ConfigManager.getInstance().getConf(SAddCashPCard.class);
			for (Entry<Integer, SAddCashPCard> entry : rawDataPCard.entrySet()) {
				SAddCashPCard sAddCashPCard = entry.getValue();
				Map<Integer, LinkedList<GoodInfo>> platGoodInfos = goodListMap.get(sAddCashPCard.roofid);
				if (platGoodInfos == null) {
					platGoodInfos = new HashMap<Integer, LinkedList<GoodInfo>>();
					goodListMap.put(sAddCashPCard.roofid, platGoodInfos);
				}
				LinkedList<GoodInfo> serverGoodInfos = platGoodInfos.get(sAddCashPCard.serverid);
				if (serverGoodInfos == null) {
					serverGoodInfos = new LinkedList<GoodInfo>();
					platGoodInfos.put(sAddCashPCard.serverid, serverGoodInfos);
				}
				serverGoodInfos.add(new GoodInfo(sAddCashPCard.id, sAddCashPCard.sellpricenum, sAddCashPCard.sellnum, sAddCashPCard.sellnummore, 0));
			
				//应用宝处理
				if (entry.getKey() == 101 || entry.getKey() == 102 
				|| entry.getKey() == 103 || entry.getKey() == 104 
				|| entry.getKey() == 105 || entry.getKey() == 106
				|| entry.getKey() == 107 || entry.getKey() == 108) {
					yybGenBalance.put(sAddCashPCard.getSellnum(), sAddCashPCard.getSellnummore());
				}
			}
		}

	}
	
	public static HttpGet makeYybGetCurrencyRequest(int userId, long roleId) throws Exception {
		xbean.YingYongBao yyb = xtable.Yingyongbaoinfos.select(userId);
	    if (yyb == null){
	    	logger.error("FushiManager.makeYybGetCurrencyRequest:YingYongBao数据为null!");
	    	return null;
	    }
		final String openid = yyb.getOpenid();
		final String openkey = yyb.getOpenkey();
//		final String pay_token = yyb.getPaytoken();
		final String pf = yyb.getPf();
		final String pfkey = yyb.getPfkey();
		final String zoneid = yyb.getZoneid();
		final String platform_name = java.net.URLEncoder.encode(yyb.getPlatformname(), "utf-8");
		final String httpurl = String.format(
				"http://%1$s%2$s?openid=%3$s&openkey=%4$s&pf=%5$s&pfkey=%6$s&zoneid=%7$s&platform_name=%8$s", 
				FushiManager.YYBAddress, FushiManager.YYBGet, 
				openid, openkey, pf, pfkey, zoneid, platform_name);
		logger.info("FushiManager.makeYybGetCurrencyRequest:" + httpurl);
		HttpGet request = new HttpGet(httpurl);
		return request;
	}
	
	public static HttpGet makeYybAddCurrencyRequest(int userId, long roleId, /*int discountid, int giftid,*/ int presenttimes, long billno) throws Exception {
		xbean.YingYongBao yyb = xtable.Yingyongbaoinfos.select(userId);
	    if (yyb == null){
	    	logger.error("FushiManager.makeYybAddCurrencyRequest:YingYongBao数据为null!");
	    	return null;
	    }
		final String openid = yyb.getOpenid();
		final String openkey = yyb.getOpenkey();
//		final String pay_token = yyb.getPaytoken();
		final String pf = yyb.getPf();
		final String pfkey = yyb.getPfkey();
		final String zoneid = yyb.getZoneid();
//		final String discountidStr = String.valueOf(discountid);
//		final String giftidStr = String.valueOf(giftid);
		final String presenttimesStr = String.valueOf(presenttimes);
		final String platform_name = java.net.URLEncoder.encode(yyb.getPlatformname(), "utf-8");
		final String httpurl = String.format(
				"http://%1$s%2$s?openid=%3$s&openkey=%4$s&pf=%5$s&pfkey=%6$s&billno=%7$s&zoneid=%8$s&presenttimes=%9$s&platform_name=%10$s", 
				FushiManager.YYBAddress, FushiManager.YYBAdd, 
				openid, openkey, pf, pfkey, String.valueOf(billno), zoneid, presenttimesStr, platform_name);
		logger.info("FushiManager.makeYybAddCurrencyRequest:" + httpurl);
		HttpGet request = new HttpGet(httpurl);
		return request;
	}
	
	public static HttpGet makeYybSubCurrencyRequest(int userId, long roleId, int amt, long billno) throws Exception {
		xbean.YingYongBao yyb = xtable.Yingyongbaoinfos.select(userId);
	    if (yyb == null){
	    	logger.error("FushiManager.makeYybSubCurrencyRequest:YingYongBao数据为null!");
	    	return null;
	    }
	    
		final String openid = yyb.getOpenid();
		final String openkey = yyb.getOpenkey();
//		final String pay_token = yyb.getPaytoken();
		final String pf = yyb.getPf();
		final String pfkey = yyb.getPfkey();
		final String amtStr = String.valueOf(amt);
		final String zoneid = yyb.getZoneid();
		final String platform_name = java.net.URLEncoder.encode(yyb.getPlatformname(), "utf-8");
		final String httpurl = String.format(
				"http://%1$s%2$s?openid=%3$s&openkey=%4$s&pf=%5$s&pfkey=%6$s&billno=%7$s&amt=%8$s&zoneid=%9$s&platform_name=%10$s", 
				FushiManager.YYBAddress, FushiManager.YYBSub, 
				openid, openkey, pf, pfkey, String.valueOf(billno), amtStr, zoneid, platform_name);
		logger.info("FushiManager.makeYybSubCurrencyRequest:" + httpurl);
		HttpGet request = new HttpGet(httpurl);
		return request;
	}

	public static int getFenduan(int value) {
		if (value <= 0) {
			logger.error("yuan bao chongzhi value must be more than 0:" + value);
			return -1;
		}
		if (value > FushiConst.FEN_DUAN_9)
			return 10;
		if (value > FushiConst.FEN_DUAN_8)
			return 9;
		if (value > FushiConst.FEN_DUAN_7)
			return 8;
		if (value > FushiConst.FEN_DUAN_6)
			return 7;
		if (value > FushiConst.FEN_DUAN_5)
			return 6;
		if (value > FushiConst.FEN_DUAN_4)
			return 5;
		if (value > FushiConst.FEN_DUAN_3)
			return 4;
		if (value > FushiConst.FEN_DUAN_2)
			return 3;
		if (value > FushiConst.FEN_DUAN_1)
			return 2;
		if (value > 0)
			return 1;
		else
			return -1;
	}

	public static boolean addFushiToUser(int userid, long roleid, int fushiNum, int fushitype, YYLoggerTuJingEnum way) {
		if (fire.pb.fushi.Module.getIsYYBUser(userid)) {
			if (userid <= 0 || roleid <= 0) {
				logger.error("FushiManager.addFushiToUser:userid=" + userid + ",roleid=" + roleid + ",错误！");
				return false;
			}
			
			if (fushiNum <= 0) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "<=0,错误！roleid=" + roleid);
				return false;
			}
			
			xbean.YybFushiNums yybFs = xtable.Yybfushi.get(userid);
			if (yybFs == null) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "没有符石数据1,roleid=" + roleid + ",userid=" + userid);
				return false;
			}
			
			xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleid);
			if (yybFushiNum == null) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "没有符石数据2,roleid=" + roleid + ",userid=" + userid);
				return false;
			}
			
			yybFushiNum.setBalance(yybFushiNum.getBalance() + fushiNum);
			yybFushiNum.setGenbalance(yybFushiNum.getGenbalance() + fushiNum);
			long oldall=yybFushiNum.getFushiall();
			yybFushiNum.setFushiall(yybFushiNum.getFushiall()+fushiNum);//历史累计总符石
			
			int balance = yybFushiNum.getBalance();
			int genBalance = yybFushiNum.getGenbalance();
			
			logger.info(new StringBuilder().append("FushiManager.addYYBFushiToUser:User[").append(userid)
					.append("]Role[").append(roleid).append("]增加")
					.append(fushitype).append("类型符石数:").append(fushiNum).append(",现有总量:").append((yybFushiNum.getBalance())));
			FushiManager.refreshRoleFushi(roleid, balance, balance - genBalance);
			SpotCheckManage.refreshTradingOpenState(roleid, oldall,yybFushiNum.getFushiall());//刷新交易所是否开启
			fire.log.YYLogger.OpTokenGetLog(roleid, way, MoneyType.MoneyType_HearthStone, fushiNum, yybFushiNum.getBalance(), new fire.log.beans.ItemBean());
			return true;
		}
		
		if (userid <= 0 || roleid <= 0) {
			logger.error("FushiManager.addFushiToUser:userid=" + userid + ",roleid=" + roleid + ",错误！");
			return false;
		}
		if (fushiNum <= 0) {
			logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "<=0,错误！roleid=" + roleid);
			return false;
		}
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		if (ybNums == null) {
			ybNums = xbean.Pod.newYbNums();
			xtable.Fushinum.insert(userid, ybNums);
		}
		xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid);
		if (ybNum == null) {
			ybNum = xbean.Pod.newYbNum();
			ybNums.getRoleyb().put(roleid, ybNum);
		}
		/*
		if (fushitype == FushiConst.SYS_FUSHI)
			if (!checkFushiDayLimit(ybNum, fushiNum)) {
				List<String> paras = MessageUtil.getMsgParaList(String.valueOf(ybNum.getBindorsysnumtoday()));
				fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 143808, 0, paras);
				logger.info("Add Fushi failed.roleid:" + roleid + "cash Num:" + ybNum.getNum() + "bind Num:" + ybNum.getBindnum() + "sys num:"
						+ ybNum.getSysnum() + "fushiinOneday:" + ybNum.getBindorsysnumtoday() + "fushiNum:" + fushiNum);
				throw new RuntimeException("add fushi failed:over day limit");
			}
			*/
		int cashYbChange = 0;
		int sysYbChange = 0;
		if (fushitype == FushiConst.SYS_FUSHI) {
			int sysNum = ybNum.getSysnum();
			int newNum = sysNum + fushiNum;
			if (newNum < 0 || newNum > FushiManager.MAX_NUM) {
				logger.error("FushiManager.addFushiToUser:User[" + userid + "]Role[" + roleid + "]系统符石newNum=" + newNum + ",超范围！");
				return false;
			}
			ybNum.setSysnum(newNum);
			sysYbChange = fushiNum;
		} else if (fushitype == FushiConst.CASH_FUSHI) {
			int cashNum = ybNum.getNum();
			int newNum = cashNum + fushiNum;
			if (newNum < 0 || newNum > FushiManager.MAX_NUM) {
				logger.error("FushiManager.addFushiToUser:User[" + userid + "]Role[" + roleid + "]现金符石newNum=" + newNum + ",超范围！");
				return false;
			}
			ybNum.setNum(newNum);
			cashYbChange = fushiNum;
		}
		//添加所有的累计符石数
		long oldall=ybNum.getFushiall();
		ybNum.setFushiall(ybNum.getFushiall()+fushiNum);
		logger.info("FushiManager.addFushiToUser:User[" + userid + "]Role[" + roleid + "]增加" + fushitype + "类型符石数:" + fushiNum + ",现有总量:" + (ybNum.getNum() + ybNum.getSysnum()));
		FushiManager.refreshRoleFushi(roleid, ybNum, true);
		SpotCheckManage.refreshTradingOpenState(roleid, oldall,ybNum.getFushiall());//刷新交易所是否开启
		logCashChange(roleid, ybNum, cashYbChange, 0, sysYbChange, FushiConst.REASON_ADD_FUSHI_WANFA);
		fire.log.YYLogger.OpTokenGetLog(roleid, way, MoneyType.MoneyType_HearthStone, fushiNum, ybNum.getNum(), new fire.log.beans.ItemBean());
		return true;
	}
	
	public static boolean subFushiFromUser(int userid, long roleid, int fushiNum, int itemid, int itemNum, int consumetype, YYLoggerTuJingEnum way, boolean showmsg) {
		if (Module.getIsYYBUser(userid)) {
			if (userid <= 0 || roleid <= 0) {
				logger.error("FushiManager.addFushiToUser:userid=" + userid + ",roleid=" + roleid + ",错误！");
				return false;
			}
			
			if (fushiNum <= 0) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "<=0,错误！roleid=" + roleid);
				return false;
			}
			
			xbean.YybFushiNums yybFs = xtable.Yybfushi.get(userid);
			if (yybFs == null) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "没有符石数据1,roleid=" + roleid + ",userid=" + userid);
				return false;
			}
			
			xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleid);
			if (yybFushiNum == null) {
				logger.error("FushiManager.addFushiToUser:fushiNum=" + fushiNum + "没有符石数据2,roleid=" + roleid + ",userid=" + userid);
				return false;
			}
			
			int balance = yybFushiNum.getBalance();
			int genBalance = yybFushiNum.getGenbalance();
			
			if (balance < fushiNum) {
				logger.error("FushiManager.subFushiToUser:fushiNum=" + fushiNum + "<=0,符石不足,roleid=" + roleid);
				return false;
			}
			
			//先扣除赠送的符石
			if (genBalance > fushiNum) {
				yybFushiNum.setBalance(balance - fushiNum);
				yybFushiNum.setGenbalance(genBalance - fushiNum);
			}
			else {
				yybFushiNum.setGenbalance(0);
				yybFushiNum.setBalance(balance - fushiNum);
			}
			
			balance = yybFushiNum.getBalance();
			genBalance = yybFushiNum.getGenbalance();
			
			// 刷新符石数量
			if (consumetype == FushiConst.REASON_BUY_DAYPAY) {
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				bag.addSysCurrency((long)(fushiNum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_DAYCOSTFUSHI))
						, MoneyType.MoneyType_EreditPoint, "点卡服每日扣点卡	", way, 0);
			}
			
			logger.info(new StringBuilder().append("FushiManager.subYYBFushiToUser:User[").append(userid).append("]Role[").append(roleid)
					.append("]扣除").append(consumetype).append("类型符石数:").append(fushiNum).append(",现有总量:").append((yybFushiNum.getBalance())));
			FushiManager.refreshRoleFushi(roleid, balance, balance - genBalance);
			
			FushiManager.logCashChange(roleid, balance, genBalance);
			// 更新符石消耗数据
			if (fushiNum > 0)
				StatisticUtil.updateFushiConsumeStats(roleid, System.currentTimeMillis(), fushiNum, fushiNum);
			//YYLogger
			fire.log.YYLogger.OpTokenUseLog(roleid, way, MoneyType.MoneyType_HearthStone, fushiNum, yybFushiNum.getBalance(), new fire.log.beans.ItemBean());
			return true;
		}	
		
		if (userid <= 0 || roleid <= 0) {
			logger.error("FushiManager.subFushiFromUser:userid=" + userid + ",roleid=" + roleid + ",错误！");
			return false;
		}
		if (fushiNum <= 0) {
			logger.error("FushiManager.subFushiFromUser:fushiNum=" + fushiNum + "<=0,错误！roleid=" + roleid);
			return false;
		}
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		if (ybNums == null) {
			ybNums = xbean.Pod.newYbNums();
			xtable.Fushinum.insert(userid, ybNums);
		}
		xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid);
		if (ybNum == null) {
			ybNum = xbean.Pod.newYbNum();
			ybNums.getRoleyb().put(roleid, ybNum);
		}
		int sysYbUsed = 0;
		int cashYbUsed = 0;
		int num = ybNum.getNum();
		int sysNum = ybNum.getSysnum();

		if (num + sysNum < fushiNum) {
			logger.error("FushiManager.subFushiFromUser:userid=" + userid + ",roleid=" + roleid + ",符石数:" + (num + sysNum) + "不够:" + fushiNum);
			if (showmsg) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142686, null);
			}
			return false;
		}
		
		if (fushiNum <= sysNum) {	//如果系统赠送符石就够了
			sysYbUsed = fushiNum;
			ybNum.setSysnum(ybNum.getSysnum() - fushiNum);
		} else {
			sysYbUsed = ybNum.getSysnum();
			ybNum.setSysnum(0);
			cashYbUsed = fushiNum - sysYbUsed;
			ybNum.setNum(ybNum.getNum() - cashYbUsed);
		}
		if (ybNum.getNum() < 0 || ybNum.getSysnum() < 0) {
			logger.error("FushiManager.subFushiFromUser:userid=" + userid + ",roleid=" + roleid + ",符石数扣除后小于0!");
			return false;
		}

		// 刷新符石数量
		if(consumetype == FushiConst.REASON_BUY_DAYPAY)
		{
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			bag.addSysCurrency((long)(fushiNum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_DAYCOSTFUSHI))
					, MoneyType.MoneyType_EreditPoint, "点卡服每日扣点卡	", way, 0);
		}
		logger.info("FushiManager.subFushiFromUser:User[" + userid + "]Role[" + roleid + "]减少符石数:" + fushiNum + ",现有总量:" + (ybNum.getNum() + ybNum.getSysnum()));
		FushiManager.refreshRoleFushi(roleid, ybNum, true);
		FushiManager.logCashChange(roleid, ybNum, -cashYbUsed, 0, -sysYbUsed, consumetype);
		// 更新符石消耗数据
		if (cashYbUsed + sysYbUsed > 0)
			StatisticUtil.updateFushiConsumeStats(roleid, System.currentTimeMillis(), cashYbUsed + sysYbUsed, cashYbUsed);
		//YYLogger
		fire.log.YYLogger.OpTokenUseLog(roleid, way, MoneyType.MoneyType_HearthStone, fushiNum, ybNum.getNum(), new fire.log.beans.ItemBean());
		return true;
	}

	public static void logCashChange(long roleid, xbean.YbNum ybNum, int cashYbChange, int bindYbChange, int sysYbChange, int reasonid) {
		try {
			if (roleid == FushiConst.SYS_FUSHI_ROLEID)
				return;
			if (roleid != FushiConst.SYS_FUSHI_ROLEID) {
				StatisticUtil.updateFushiNumStats(roleid, ybNum);
			}
		} catch (Exception e) {
			FushiManager.logger.info("cash change log error", e);
		}
	}
	
	public static void logCashChange(long roleid, int balance, int genbalance) {
		try {
			if (roleid == FushiConst.SYS_FUSHI_ROLEID)
				return;
			StatisticUtil.updateFushiNumStats(roleid, balance, genbalance);
		} catch (Exception e) {
			FushiManager.logger.info("cash change log error", e);
		}
	}
	
	public static void refreshRoleFushi(long roleid, int num, int totalnum) {
		SReqFushiNum srybn = new SReqFushiNum();
		srybn.num = num;
		srybn.totalnum = totalnum;
		if (mkdb.Transaction.current() != null)
			Procedure.psendWhileCommit(roleid, srybn);
		else
			Onlines.getInstance().send(roleid, srybn);
	}
	
	public static void refreshRoleFushi(long roleid, YbNum ybNum, boolean inProcedure) {

		SReqFushiNum srybn = new SReqFushiNum();
		if (ybNum == null) {
			srybn.num = 0;
			srybn.bindnum = 0;
			srybn.totalnum = 0;
		} else {
			srybn.num = ybNum.getNum() + ybNum.getSysnum();
			srybn.totalnum = ybNum.getNopresentnum();
			// srybn.bindnum = ybNum.getBindnum() + ybNum.getSysnum();
		}
		if (inProcedure)
			Procedure.psendWhileCommit(roleid, srybn);
		else
			Onlines.getInstance().send(roleid, srybn);
	}

	public static boolean checkFushiDayLimit(xbean.YbNum ybNum, int num) {
		long curTime = System.currentTimeMillis();
		if (!DateValidate.inTheSameDay(ybNum.getBindorsysnumtodaytime(), curTime)) {
			ybNum.setBindorsysnumtoday(0);
		}
		if (ybNum.getBindorsysnumtoday() + num > FushiConst.FUSHI_DAY_LIMIT) {
			return false;
		}
		ybNum.setBindorsysnumtodaytime(curTime);
		ybNum.setBindorsysnumtoday(ybNum.getBindorsysnumtoday() + num);

		return true;
	}

	public static void updateMerchantDiscount(final List<gnet.MerchantDiscount> discountList) {
		logger.info("服务器收到AU-DiscountAnnounce消息开始更新商家折扣信息……");
		// 首先清除原有的商户信息
		try {
			clearOldMerchantDiscount();
		} catch (Exception e1) {
			logger.error("删除旧的商家信息错误： ", e1);
		}

		new mkdb.Procedure() {
			@Override
			public boolean process() {
				// 更新新的商户信息
				for (gnet.MerchantDiscount discount : discountList) {
					OctetsStream os = new OctetsStream(discount.name);
					String name = null;
					try {
						name = os.getString("UTF-16LE");
					} catch (Exception e) {
						logger.error("gs解析商家折扣信息unmarshal名字出错： ", e);
						continue;
					}
					xbean.PlatformInfo info = xbean.Pod.newPlatformInfo();
					info.setDiscount(1);
					info.setId(discount.id);
					info.setName(name);
					logger.info("商家： " + discount.id + "商家名：  " + name + "折扣： " + discount.discount);
					xtable.Payplatform.add(discount.id, info);
				}
				return true;
			}
		}.submit();

	}

	private static void clearOldMerchantDiscount() throws Exception {
		final List<Integer> idList = new ArrayList<Integer>();
		xtable.Payplatform.getTable().browse(new mkdb.TTable.IWalk<Integer, xbean.PlatformInfo>() {

			@Override
			public boolean onRecord(Integer id, xbean.PlatformInfo info) {

				idList.add(id);
				return true;
			}
		});

		new mkdb.Procedure() {

			@Override
			public boolean process() {

				for (Integer id : idList) {
					xtable.Payplatform.remove(id);
				}

				return true;
			}
		}.submit().get();

	}

	public void initReturnFuShi(){
		shouchongMap.clear();
		returnZoneId  = FireProp.getIntValue(ConfigManager.getInstance().getPropConf("sys"), "sys.return.fushi.zoneid");
		returnRatio = FireProp.getFloatValue(ConfigManager.getInstance().getPropConf("sys"), "sys.return.fushi.ratio");
		logger.debug("zoneId="+returnZoneId + "  ratio="+returnRatio);
		int serverId = fire.pb.main.ConfigManager.getGsZoneId();
		if(returnZoneId != serverId){
			return;
		}
		String filePath = ConfigManager.PROPERTY_PATH+"/returnFuShi.txt";
		File file = new File(filePath);
		if (!file.exists()) {
			logger.error("文件路径： " + filePath + "找不到相关文件");
			return;
		}
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				if (line.trim().equals("")){
					continue;
				}
				line = line.replace("，", ",");
				String[] temp = line.split(",");
				if(temp.length != 2){
					logger.info("符石返还 配置有错误"+ line);
					continue;
				}
				int userId = Integer.parseInt(temp[0]);
				int shouchong = Integer.parseInt(temp[1]);
				
				Integer fushi = shouchongMap.get(userId);
				if(fushi == null){
					shouchongMap.put(userId, shouchong);
				}else{
					shouchongMap.put(userId, shouchong + fushi.intValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fileReader && null != bufferedReader) {
					fileReader.close();
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getFuShiFanHuan(int userId){
		Integer fushi = shouchongMap.get(userId);
		if(fushi == null){
			return 0;
		}
		return (int)(fushi/10 * returnRatio);
	}
	
	public static ChargeConfig getChargeConfigByMoney(final int money) {
		ChargeConfig ret = null;
		if(fire.pb.fushi.Module.GetPayServiceType() == 0){
			NavigableMap<Integer, SAddCash> chargeConf = ConfigManager.getInstance().getConf(SAddCash.class);
			if(chargeConf == null) return null;
			for(SAddCash conf : chargeConf.values()) {
				if (conf.sellpricenum == money) {
					ret = conf;
					break;
				}
			}
		}else{
			NavigableMap<Integer, SAddCashPCard> chargeConf = ConfigManager.getInstance().getConf(SAddCashPCard.class);
			if(chargeConf == null) return null;
			for(SAddCashPCard conf : chargeConf.values()) {
				if (conf.sellpricenum == money) {
					ret = conf;
					break;
				}
			}
		}
		return ret;
	}
	
	public static ChargeConfig getChargeConfigByProductID(final String product) {
		ChargeConfig ret = null;
		if(fire.pb.fushi.Module.GetPayServiceType() == 0){
			NavigableMap<Integer, SAddCash> chargeConf = ConfigManager.getInstance().getConf(SAddCash.class);
			if(chargeConf == null) return null;
			for(SAddCash conf : chargeConf.values()) {
				if (conf.productid.equals(product)) {
					ret = conf;
					break;
				}
			}
		}else{
			NavigableMap<Integer, SAddCashPCard> chargeConf = ConfigManager.getInstance().getConf(SAddCashPCard.class);
			if(chargeConf == null) return null;
			for(SAddCashPCard conf : chargeConf.values()) {
				if (conf.productid.equals(product)) {
					ret = conf;
					break;
				}
			}
		}
		return ret;
	}
	
	/**
	 * 注意 有些平台如果平台字符串是空的时候 
	 * @param platString
	 * @return
	 */
	public List<GoodInfo> getGoodsList(String platString){
		List<GoodInfo> list = new ArrayList<GoodInfo>();
		Map<Integer, LinkedList<GoodInfo>> platGoods = FushiManager.getInstance().goodListMap.get(platString);
		if (platGoods == null)
			platGoods = FushiManager.getInstance().goodListMap.get(FushiConst.LOCOJOYPLAT);
		LinkedList<GoodInfo> commonGoods = platGoods.get(0);
		if (commonGoods != null){
			for (GoodInfo goodInfo : commonGoods) {
				if(fire.pb.fushi.Module.GetPayServiceType() == 0){
					if (ConfigManager.getInstance().getConf(SAddCash.class).get(goodInfo.goodid).gameshow==1) 
						list.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}else{
					if (ConfigManager.getInstance().getConf(SAddCashPCard.class).get(goodInfo.goodid).gameshow==1) 
						list.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}
			}
		}
		LinkedList<GoodInfo> serveridGoods = platGoods.get(Gs.serverid);
		if (serveridGoods != null && ConfigManager.getGsZoneId()>0) {
			for (GoodInfo goodInfo : serveridGoods) {
				if(fire.pb.fushi.Module.GetPayServiceType() == 0){
					if (ConfigManager.getInstance().getConf(SAddCash.class).get(goodInfo.goodid).gameshow==1) 
						list.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, 0));
				}else{
					if (ConfigManager.getInstance().getConf(SAddCashPCard.class).get(goodInfo.goodid).gameshow==1) 
						list.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, 0));
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取玩家多倍充的状态
	 * @param multiCharge
	 * @param userid
	 * @return 0=没有  1=有多倍充值
	 */
	public static int getDoubleChareState(xbean.RoleDoubleChareInfo multiCharge, int userid){	
		if(multiCharge == null  || multiCharge.getActivetime()< FushiManager.MULTICHARGE_CHARGE_START_TIME){
			return 1;
		}else{
			
			if(multiCharge.getActivetime() > MULTICHARGE_CHARGE_START_TIME){
				return 0;
			}
			
			if(multiCharge.getFlag() == 1){
				for(Entry<Integer, Integer> temp :multiCharge.getAchievement().entrySet()){
					if(temp.getValue() == 0){
						return 1;
					}
				}
			}else{
				xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(userid);
				if (auUserInfo == null){
					fire.pb.fushi.FushiManager.logger.error("Exception11:auuserinfo null.userid+"+userid);
					return 0;
				}
				String platString = auUserInfo.getNickname().substring(0, 4);
				for(GoodInfo info : FushiManager.getInstance().getGoodsList(platString)){
					if(info.beishu <= 0){
						continue;
					}
					
					Integer value = multiCharge.getAchievement().get(info.goodid);
					if(value == null || value.intValue() != 1){
						return 1;
					}
				}
				fire.pb.fushi.FushiManager.logger.error("Exception111: auuserinfo null.userid+"+userid);
			}
		}
		return 0;
	}
	
	/**
	 * 初始化角色多倍表
	 */
	public void initRoleDoubleChareInfo(xbean.RoleDoubleChareInfo multiCharge, List<GoodInfo> list){
		if(list.size() == 0){
			return;
		}
		
		if(multiCharge.getActivetime() > MULTICHARGE_CHARGE_START_TIME){
			return;
		}
		if(multiCharge.getActivetime() == MULTICHARGE_CHARGE_START_TIME && multiCharge.getFlag() == 2){
			for(GoodInfo gd : list){
				if(multiCharge.getAchievement().get(gd.goodid) == null && gd.beishu > 0){
					multiCharge.getAchievement().put(gd.goodid, 0);
				}
			}
		}
		
		if(multiCharge.getActivetime()<MULTICHARGE_CHARGE_START_TIME){
			multiCharge.getAchievement().clear();
			for(GoodInfo gd : list){
				if(gd.beishu > 0){ //如果额外返还倍数不是0 by changhao
					multiCharge.getAchievement().put(gd.goodid, 0);
				}
			}
		}
		multiCharge.setActivetime(MULTICHARGE_CHARGE_START_TIME);
		multiCharge.setFlag(1);
	}
	
	public int getYybGenBalance(int midnum) {
		for (Map.Entry<Integer, Integer> genBalance : yybGenBalance.entrySet()) {
			if (midnum >= genBalance.getKey()) {
				return genBalance.getValue();
			}
		}
		
		return 0;
	}
}
