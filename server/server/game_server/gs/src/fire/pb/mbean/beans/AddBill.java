

package fire.pb.mbean.beans;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.fushi.ChargeConfig;
import fire.pb.fushi.PAddCoin;
import fire.pb.fushi.SAddCash;
import fire.pb.fushi.SAddCashPCard;
import fire.pb.fushi.SRefreshChargeState;
import fire.pb.fushi.FushiManager;
import fire.pb.main.ConfigManager;
import fire.pb.mbean.AbstractRequestHandler;
import fire.pb.util.MessageUtil;


public class AddBill extends AbstractRequestHandler {

	
	
	public AddBill(String name) {

		
		super(name);
		
	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {

		try {
			long roleid = Long.parseLong((String) paras.get("roleid"));
			long gamesn = Long.parseLong((String) paras.get("chargesn"));
			String price = (String) paras.get("price");
			int chargetype = Integer.parseInt((String) paras.get("chargetype"));

			PGmAddBill pGmAddBill = new PGmAddBill(roleid, gamesn, price, chargetype);
			PGmAddBill resultProc = (PGmAddBill) pGmAddBill.submit().get();
			if (resultProc.ret){
				return successMsg();
			}else {
				return failedMsg("补发丢单没有成功:"+pGmAddBill.reason);
			}
			
		} catch (Exception e) {
			return failedMsg("补发丢单失败:"+e.getMessage());
		}
	}

	class PGmAddBill extends mkdb.Procedure{
		long roleid;
		long gamesn;
		int chargeType;
		String price;
		String reason;
		boolean ret;
		public PGmAddBill(long roleid,long gamesn, String price,int chargetype) {

			super();
			this.roleid = roleid;
			this.chargeType = chargetype;
			this.gamesn = gamesn;
			this.price = price;
		}
		@Override
		protected boolean process() throws Exception {

			if (chargeType == 1){//apple
				ret = addBillForApple();
			}else if (chargeType == 2){//android
				ret = addBillForAndroid();
			}else {
				reason = "chargeType error.chargeType:"+chargeType;
				ret = false;
			}
			return ret;
		}
		
		private boolean addBillForApple() {

			FushiManager.logger.info("开始处理充值订单.transcationid:" + gamesn);

			java.util.Map<String, String> vars = new java.util.HashMap<String, String>();
			vars.put("transaction_id", String.valueOf(gamesn));
			
			String transaction_id = vars.get("transaction_id");
		
			xbean.AppstoretidStatus appstoretidStatus = xtable.Appstoretidstatus.select(gamesn);
			if (appstoretidStatus == null){
				reason = "transaction id don't exist:"+gamesn;
				return false;
			}
			if (appstoretidStatus.getStatus() == 1) {
				FushiManager.logger.error("订单已经充过值了.transaction_id:" + transaction_id);
				reason = "订单已经充过值了.transaction_id:" + gamesn;
				return false;
			}
			long gamesn = appstoretidStatus.getChargesn();
			xbean.ChargeOrder chargeOrder = xtable.Chargeorder.get(gamesn);
			int userid = 0;
			boolean findInChargeOrder = true;
			if (chargeOrder == null) {
				chargeOrder = xtable.Failedchargeorder.get(gamesn);
				if (chargeOrder == null) {
					FushiManager.logger.info("充值订单在gs未找到,chargesn:" + gamesn);
					reason = "充值订单在gs未找到,chargesn:" + gamesn;
					return false;
				} else
					findInChargeOrder = false;
			}
			userid = chargeOrder.getUserid();

			
			String product_id = price;
			chargeOrder.setGoodid(Integer.parseInt(product_id));
			
			ChargeConfig sChargeCfg = null;
			if(fire.pb.fushi.Module.GetPayServiceType() == 0){
				sChargeCfg = ConfigManager.getInstance().getConf(SAddCash.class).get(chargeOrder.getGoodid());
			}else{
				sChargeCfg = ConfigManager.getInstance().getConf(SAddCashPCard.class).get(chargeOrder.getGoodid());
			}
			
			if (sChargeCfg == null) {
				FushiManager.logger.error("找不到对应的货物:" + chargeOrder.getGoodid());
				reason = "找不到对应的货物:" + chargeOrder.getGoodid();
				return false;
			}
			xbean.User user = xtable.User.get(userid);
			long roleid = user.getPrevloginroleid();
			if (2 <= sChargeCfg.kind && sChargeCfg.kind <= 4) 
			{
			}
			else if (sChargeCfg.kind == 1)
			{
				int fushiNum = sChargeCfg.sellnum + sChargeCfg.sellnummore;
				xbean.FirstCharge firstCharge = xtable.Firstchargenew.get(userid);
				if (firstCharge == null){
					firstCharge = xbean.Pod.newFirstCharge();
					xtable.Firstchargenew.insert(userid, firstCharge);
				}
				if (firstCharge.getFirstchargetime() < FushiManager.FIRST_CHARGE_START_TIME){
					firstCharge.setFirstchargetime(System.currentTimeMillis());
					fushiNum += sChargeCfg.sellnum;
				}
				if (firstCharge.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME) {
					firstCharge.setFirstchargeclearpresenttime(System.currentTimeMillis());
					firstCharge.setChargestatus(1);
					psendWhileCommit(chargeOrder.getRoleid(), new SRefreshChargeState(1, 0));
				}
					
				
				if (!new PAddCoin(userid, ConfigManager.getGsZoneId(), gamesn, fushiNum, vars, sChargeCfg.sellpricenum, String.valueOf(0),
						transaction_id,sChargeCfg.sellnum, "addbill").call())
					return false;
			}else {
				return false;
			}

			chargeOrder.setStatus(0);
			xbean.ChargeHistory chargeHistory = xtable.Chargehistory.get(userid);
			if (chargeHistory != null) {
				xbean.ChargeOrder chargeOrder2 = chargeHistory.getCharges().get(gamesn);
				chargeOrder2.setNum(sChargeCfg.sellpricenum);
				chargeOrder2.setStatus(0);
			} else {
				FushiManager.logger.error("充值订单的history居然不存在,gamesn:" + gamesn);
				reason = "充值订单的history居然不存在,gamesn:" + gamesn;
			}

			if (findInChargeOrder) {
				FushiManager.logger.info("充值订单从Chargeorder删除:" + gamesn);
				xtable.Chargeorder.remove(gamesn);
			} else {
				FushiManager.logger.info("充值订单从FailedChargeorder删除:" + gamesn);
				xtable.Failedchargeorder.remove(gamesn);
			}
			FushiManager.logger.info("充值成功.gamesn:" + gamesn + "金额:" + sChargeCfg.sellpricenum);

			xbean.AppstoretidStatus status = xtable.Appstoretidstatus.get(Long.parseLong(transaction_id));
			if (status == null) {
				FushiManager.logger.error("Appstoretidstatus 找不到数据,tid:" + transaction_id);
				reason = "Appstoretidstatus 找不到数据,tid:" + transaction_id;
				return false;
			}
			status.setStatus(1);
			return true;
		}
		
		public boolean addBillForAndroid() {

			try {
				xbean.ChargeOrder chargeOrder = xtable.Chargeorder.get(gamesn);
				boolean findInChargeOrder = true;
				if (chargeOrder == null) {
					chargeOrder = xtable.Failedchargeorder.get(gamesn);
					if (chargeOrder == null) {
						reason = "充值订单在gs未找到,chargesn:" + gamesn;
						return false;
					} else
						findInChargeOrder  = false;
				}
				int userid = chargeOrder.getUserid();
				if (chargeOrder.getNum()!=Integer.parseInt(price)) {
					reason = "价格不匹配:"+chargeOrder.getNum()+":"+price;
					return false;
				}
				
				ChargeConfig sChargeCfg = null;
				if(fire.pb.fushi.Module.GetPayServiceType() == 0){
					sChargeCfg = ConfigManager.getInstance().getConf(SAddCash.class).get(chargeOrder.getGoodid());
				}else{
					sChargeCfg = ConfigManager.getInstance().getConf(SAddCashPCard.class).get(chargeOrder.getGoodid());
				}
				
				if (sChargeCfg == null) {
					FushiManager.logger.error("找不到对应的货物:" + chargeOrder.getGoodid());
					reason = "找不到对应的货物:" + chargeOrder.getGoodid();
					return false;
				}
				
				if (2 <= sChargeCfg.kind && sChargeCfg.kind <= 4)
				{

				}
				else if (sChargeCfg.kind == 1) {
					int fushiNum = sChargeCfg.sellnum + sChargeCfg.sellnummore;
					xbean.FirstCharge firstCharge = xtable.Firstchargenew.get(userid);
					if (firstCharge == null){
						firstCharge = xbean.Pod.newFirstCharge();
						xtable.Firstchargenew.insert(userid, firstCharge);
					}
					if (firstCharge.getFirstchargetime() < FushiManager.FIRST_CHARGE_START_TIME){
						firstCharge.setFirstchargetime(System.currentTimeMillis());
						fushiNum += sChargeCfg.sellnum;
					}
					if (firstCharge.getFirstchargeclearpresenttime() < FushiManager.FIRST_CHARGE_CLEAR_PRESENT_START_TIME) {
						firstCharge.setFirstchargeclearpresenttime(System.currentTimeMillis());
						firstCharge.setChargestatus(1);
						psendWhileCommit(chargeOrder.getRoleid(), new SRefreshChargeState(1, 0));
					}
					if (!new PAddCoin(userid, ConfigManager.getGsZoneId(), gamesn, fushiNum, new HashMap<String, String>(), chargeOrder.getNum(), String.valueOf(0),
							String.valueOf(gamesn),sChargeCfg.sellnum, "addbill").call())
						return false;
					List<String> paras = MessageUtil.getMsgParaList(String.valueOf(fushiNum));
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 145154, 0, paras);
				} else {
					return false;
				}

				chargeOrder.setStatus(0);
				xbean.ChargeHistory chargeHistory = xtable.Chargehistory.get(userid);
				if (chargeHistory != null) {
					xbean.ChargeOrder chargeOrder2 = chargeHistory.getCharges().get(gamesn);
					chargeOrder2.setStatus(0);
				} else {
					FushiManager.logger.error("充值订单的history居然不存在,gamesn:" + gamesn);
				}

				if (findInChargeOrder) {
					FushiManager.logger.info("充值订单从Chargeorder删除:" + gamesn);
					xtable.Chargeorder.remove(gamesn);
				} else {
					FushiManager.logger.info("充值订单从FailedChargeorder删除:" + gamesn);
					xtable.Failedchargeorder.remove(gamesn);
				}
				FushiManager.logger.info("充值成功.gamesn:" + gamesn + "金额:" + chargeOrder.getNum());
			} catch (Exception e) {
				reason = "补发android订单失败:"+e.getMessage();
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}
}

