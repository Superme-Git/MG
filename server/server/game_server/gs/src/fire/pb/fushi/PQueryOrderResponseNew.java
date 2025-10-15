package fire.pb.fushi;

import gnet.QueryOrderResponse;
import mkdb.Procedure;

import java.util.HashMap;
import java.util.List;

import fire.pb.fushi.SAddCash;
import fire.pb.fushi.payday.PSubscribeOrderResponse;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.util.MessageUtil;

import com.locojoy.base.Marshal.OctetsStream;

/***
 * 订单回应
 * @author changhao
 * 注意，此处的充值逻辑已经淘汰，外网真充值逻辑见PPayOrderResponse.java
 */

public class PQueryOrderResponseNew extends Procedure {
	QueryOrderResponse queryOrderResponse;
	public PQueryOrderResponseNew(QueryOrderResponse queryOrderResponse) {
		this.queryOrderResponse = queryOrderResponse;
	}

	/**
	 * 注意，此处的充值逻辑已经淘汰，留下只为内网调试，外网真充值逻辑见PPayOrderResponse.java
	 */
	@Override
	protected boolean process() throws Exception {
		FushiManager.logger.info("订单响应类型restype:" + queryOrderResponse.restype + ",开始处理充值订单.gamesn:" + queryOrderResponse.orderserialgame + ",userid:" + queryOrderResponse.userid);
		if (queryOrderResponse.orderserialgame.isEmpty()) {
			FushiManager.logger.error("gamesn is empty!"+ "userid:" + queryOrderResponse.userid);
			return false;
		}
		long gamesn = Long.parseLong(queryOrderResponse.orderserialgame);
		xbean.ChargeOrder chargeOrder = xtable.Chargeorder.get(gamesn);
		int userid = 0;
		boolean findInChargeOrder = true;
		if (chargeOrder == null) {
			chargeOrder = xtable.Failedchargeorder.get(gamesn);
			if (chargeOrder == null) {
				FushiManager.logger.info("充值订单在gs未找到,chargesn:" + gamesn + ",userid:" + queryOrderResponse.userid);
				return false;
			} else
				findInChargeOrder = false;
		}
		userid = chargeOrder.getUserid();
		if(userid != queryOrderResponse.userid) {
			FushiManager.logger.error("游戏服务器订单:"+ gamesn + "属于userid:" + userid + ",但本次请求属于user:" + queryOrderResponse.userid + ",所以充值响应无效!");
			return false;
		}
		
		OctetsStream os = new OctetsStream(queryOrderResponse.vars);
		try {
			int size = os.uncompact_sint32();
 			java.util.Map<String, String> vars = new java.util.HashMap<String, String>(size);
			for (int i = 0; i < size; i++) {
				String key = os.unmarshal_String("UTF-16LE");
				String value = os.unmarshal_String("UTF-16LE");
				vars.put(key, value);
			}
			FushiManager.logger.info("userid:" + userid + ",收到充值订单.gamesn:" + queryOrderResponse.orderserialgame + 
					",platformsn:" + queryOrderResponse.orderserialplat + ",retcode:"
					+ queryOrderResponse.errorcode + ",detail:" + vars);
			if (queryOrderResponse.errorcode != 0) {
				FushiManager.logger.error("userid:" + userid + ",充值订单的errorcode不为0:" + queryOrderResponse.errorcode + "gamesn:" + gamesn);
				return false;
			}
			
			int cash = chargeOrder.getNum(); // 为了测试充值 by changhao queryOrderHandler.getCashNum(chargeOrder, vars); (test) //支持第三方的MONEY by changhao
			if (cash == 0) {
				FushiManager.logger.error("userid:" + userid + ",没有充值金额");
				return false;
			}
			if (cash != chargeOrder.getNum()) {
				FushiManager.logger.info("userid:" + userid + ",充值金额和数据库不匹配.gamesn:" + gamesn + ";cash:" + cash);
				return false;
			}

			if (!queryOrderResponse.platid.equals(chargeOrder.getPlattype())) {
				FushiManager.logger.info("userid:" + userid + ",协议平台和数据库平台不匹配.gamesn:" + gamesn + ",协议平台:" + queryOrderResponse.platid + ",数据库平台:" + chargeOrder.getPlattype());
				return false;
			}
			
			ChargeConfig sChargeCfg = null;
			if(fire.pb.fushi.Module.GetPayServiceType() == 0){
				sChargeCfg = ConfigManager.getInstance().getConf(SAddCash.class).get(chargeOrder.getGoodid());
			}else{
				sChargeCfg = ConfigManager.getInstance().getConf(SAddCashPCard.class).get(chargeOrder.getGoodid());
			}
			
			if (sChargeCfg == null) {
				FushiManager.logger.error("userid:" + userid + ",找不到对应的货物:" + chargeOrder.getGoodid());
				return false;
			}
			//将SAddCash中对应的goodid加入到vars变量中，以方便以后取SAddCash配置
			vars.put("saddcashgooid", String.valueOf(sChargeCfg.id));
			xbean.User user = xtable.User.get(userid);
			long roleid = user.getPrevloginroleid();

            FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]处理充值订单,AddCash.kind:" + sChargeCfg.kind);
            if (sChargeCfg.kind == 1) {
				int fushiNum = sChargeCfg.sellnum + sChargeCfg.sellnummore;
				int extraGold = 0;
				int isFirstCharge = 0;		// add by yebin 是否首次充值
				xbean.FirstCharge firstCharge = xtable.Firstchargenew.get(userid);
				if (firstCharge == null){
					firstCharge = xbean.Pod.newFirstCharge();
					xtable.Firstchargenew.insert(userid, firstCharge);
				}
				if (firstCharge.getFirstchargetime() < FushiManager.FIRST_CHARGE_START_TIME){
					firstCharge.setFirstchargetime(System.currentTimeMillis());
				}
				if (firstCharge.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME) {
					firstCharge.setFirstchargeclearpresenttime(System.currentTimeMillis());
					firstCharge.setChargestatus(1);
					isFirstCharge = 1;
				}
				psendWhileCommit(chargeOrder.getRoleid(), new SRefreshChargeState((int)firstCharge.getChargestatus(), 0));
				
				FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]处理充值订单,准备PAddCoin,数量:" + fushiNum);
				if (!new PAddCoin(userid, ConfigManager.getGsZoneId(), gamesn, fushiNum, vars, cash, queryOrderResponse.orderserialplat,
						queryOrderResponse.orderserialgame,sChargeCfg.sellnum+extraGold, "").call()){
					FushiManager.logger.error("userid:" + userid + ",角色["+roleid+"]处理充值订单,PAddCoin.call()失败！");
					return false;
				}
				
				String sgood = String.valueOf(chargeOrder.getGoodid());			
				
				// 运营日志
				String platstr = vars.get("Channel");
				if(platstr == null){
					platstr = "";
				}
				fire.log.YYLogger.OpChargeLog(user.getPrevloginroleid(), queryOrderResponse.orderserialplat, sgood, chargeOrder.getGoodnum(), cash, sChargeCfg.sellnum, fushiNum - sChargeCfg.sellnum, "RMB", "", "", platstr, isFirstCharge);			
				fire.log.YYLogger.OpTokenGetLog(roleid, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, MoneyType.MoneyType_HearthStone, fushiNum, 0, new fire.log.beans.ItemBean());
				
				List<String> paras = MessageUtil.getMsgParaList(String.valueOf(fushiNum));
				fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 145154, 0, paras);
				
				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
				bag.addSysCurrency((long)(sChargeCfg.sellnum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_EXCHANGE))
						, MoneyType.MoneyType_EreditPoint, "充值加信用值", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, 0);
				
				new PAddVipExp(user.getPrevloginroleid(),sChargeCfg.sellnum, PAddVipExp.REASON_ADD_VIP_EXP_CHARGE).call();
				FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]充值流程就要结束啦~");
            } else if (sChargeCfg.kind == 2) {
            	//处理订阅
            	int subtime = sChargeCfg.sellnum;
            	int fushiNum = sChargeCfg.sellnummore;
            	int extraGold = 0;
            	
            	int isFirstCharge = 0;			// add by yebin 是否首次充值
				xbean.FirstCharge firstCharge = xtable.Firstchargenew.get(userid);
				if (firstCharge == null){
					firstCharge = xbean.Pod.newFirstCharge();
					xtable.Firstchargenew.insert(userid, firstCharge);
				}
				if (firstCharge.getFirstchargetime() < FushiManager.FIRST_CHARGE_START_TIME){
					firstCharge.setFirstchargetime(System.currentTimeMillis());
				}
				if (firstCharge.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME) {
					firstCharge.setFirstchargeclearpresenttime(System.currentTimeMillis());
					firstCharge.setChargestatus(1);
					isFirstCharge = 1;
				}
				psendWhileCommit(chargeOrder.getRoleid(), new SRefreshChargeState((int)firstCharge.getChargestatus(), 0));
            	
            	FushiManager.logger.info("PPayOrderResponse.订单信息gamesn:" + gamesn + ",userid:" + userid + ",角色["+roleid+"]处理充值订阅订单,准备PSubscribeOrderResponse,订阅时长:" + subtime + ",符石:" + fushiNum);
            	if(!new PSubscribeOrderResponse(roleid, subtime).call()) {
            		return false;
            	}
            	if(fushiNum > 0) {
	            	if (!new PAddCoin(userid, ConfigManager.getGsZoneId(), gamesn, fushiNum, new HashMap<String, String>(), cash, queryOrderResponse.orderserialplat,
	            			queryOrderResponse.orderserialgame, sChargeCfg.sellnum + extraGold, "").call()){
	            		FushiManager.logger.error("PPayOrderResponse.订单信息gamesn:" + gamesn + ",userid:" + userid + ",角色["+roleid+"]处理充值订阅订单,PAddCoin.call()失败！");
						return false;
					}
            	}
            	
            	//这里把点卡转成等值符石放入累计符石数据中，以配合其他模块开启交易所
            	final int dika = subtime;
            	final int tuserid = userid;
            	final long troleid = roleid;
				mkdb.Procedure proc = new mkdb.Procedure() {
					@Override
					public boolean process() {
						xbean.YbNums ybNums = xtable.Fushinum.get(tuserid);
						if (ybNums == null){
							ybNums = xbean.Pod.newYbNums();
							xtable.Fushinum.insert(tuserid, ybNums);
						}
						xbean.YbNum ybNum = ybNums.getRoleyb().get(troleid);
						if (ybNum == null) {
							ybNum = xbean.Pod.newYbNum();
							ybNums.getRoleyb().put(troleid, ybNum);
						}
						long oldall=ybNum.getFushiall();
						ybNum.setFushiall(ybNum.getFushiall() + dika * 20);//累计获得符石
						FushiManager.logger.info("PPayOrderResponse.点卡:" + dika + ",转换成符石:" + dika * 20 + ",加入["+roleid+"]累计符石中以开启交易所功能.");
						SpotCheckManage.refreshTradingOpenState(roleid, oldall, ybNum.getFushiall());//刷新交易所是否开启
						return true;
					}
				};
				mkdb.Procedure.pexecuteWhileCommit(proc);
            	
            	String sgood = String.valueOf(chargeOrder.getGoodid());			
				
				// 运营日志
				String platstr = vars.get("Channel");
				if(platstr == null){
					platstr = "";
				}
				fire.log.YYLogger.OpChargeLog(user.getPrevloginroleid(), queryOrderResponse.orderserialplat, sgood, chargeOrder.getGoodnum(), cash, sChargeCfg.sellnum, sChargeCfg.sellnummore, "RMB", "", "", platstr, isFirstCharge);			
				fire.log.YYLogger.OpTokenGetLog(roleid, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, MoneyType.MoneyType_HearthStone, fushiNum, 0, new fire.log.beans.ItemBean());

//				fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
//				bag.addSysCurrency((long)(sChargeCfg.sellnum*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_EXCHANGE))
//						, MoneyType.MoneyType_EreditPoint, "充值加信用值", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_chongzhiget, 0);
//				
//				new PAddVipExp(user.getPrevloginroleid(),sChargeCfg.sellnum, PAddVipExp.REASON_ADD_VIP_EXP_CHARGE).call();
				FushiManager.logger.info("PPayOrderResponse.订单信息gamesn:" + gamesn + ",userid:" + userid + ",角色["+ roleid +"]充值订阅流程即将结束！");
            }else {
            	FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]购买类型kind=" + sChargeCfg.kind + ",没有处理!");
                return false;
            }
			
			chargeOrder.setStatus(0);
			xbean.ChargeHistory chargeHistory = xtable.Chargehistory.get(userid);
			if (chargeHistory != null) {
				xbean.ChargeOrder chargeOrder2 = chargeHistory.getCharges().get(gamesn);
				chargeOrder2.setStatus(0);
			} else {
				FushiManager.logger.error("userid:" + userid + ",角色["+roleid+"]充值订单的history居然不存在,gamesn:" + gamesn);
			}
			
			xbean.Properties properties = xtable.Properties.get(roleid);
			if(properties != null){
				properties.setLastchargetime(System.currentTimeMillis());
			}

			if (findInChargeOrder) {
				FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]充值订单从Chargeorder删除:" + gamesn);
				xtable.Chargeorder.remove(gamesn);
			} else {
				FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]充值订单从FailedChargeorder删除:" + gamesn);
				xtable.Failedchargeorder.remove(gamesn);
			}
			PPayOrderResponse.dealChargeLimit(roleid, chargeOrder.getGoodid());
			FushiManager.logger.info("userid:" + userid + ",角色["+roleid+"]本次充值流程终于结束！");
		} catch (Exception e) {
			FushiManager.logger.error("充值遭遇异常,堆栈去看trace吧,少年！e=" + e);
			e.printStackTrace();
		}
		return true;
	}

}
