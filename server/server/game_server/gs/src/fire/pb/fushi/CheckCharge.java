package fire.pb.fushi;

import java.util.concurrent.TimeUnit;

import xbean.ChargeOrder;

import gnet.DeliveryManager;
import gnet.QueryOrderRequest;

/***
 * 检查
 * @author changhao
 *
 */
public class CheckCharge implements Runnable {
	private final long sn;

	public CheckCharge(long sn) {
		super();
		this.sn = sn;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.ChargeOrder chargeOrder = xtable.Chargeorder.get(sn);
				if (chargeOrder == null) {// 如果不存在,可能已经充值成功了,所以从数据库删除了,直接返回
					FushiManager.logger.info("数据库没有该单号,不再发送请求.chargeGameSn:"+sn);
					return true;
				}
	
				if (chargeOrder.getRetrytimes()<=0){//如果已经尝试10次了,不再继续尝试
					FushiManager.logger.info("尝试10次,不再发送请求,放入废单队列中.chargeGameSn:"+sn);
					xtable.Chargeorder.remove(sn);
					chargeOrder.setStatus(2);
					xtable.Failedchargeorder.insert(sn, chargeOrder.copy());
					xbean.ChargeHistory chargeHistory = xtable.Chargehistory.get(chargeOrder.getUserid());
					if (chargeHistory != null){
						xbean.ChargeOrder chargeOrder2 = chargeHistory.getCharges().get(sn);
						chargeOrder2.setStatus(2);
					}
					if (chargeOrder.getTranscationid()!=null && chargeOrder.getTranscationid().length()>0){//只有苹果官方平台才有用
						xbean.AppstoretidStatus status = xtable.Appstoretidstatus.get(Long.parseLong(chargeOrder.getTranscationid()));
						if (status != null){
							status.setStatus(2);
						}
					}
					return true;
				}
			    chargeOrder.setRetrytimes(chargeOrder.getRetrytimes()-1);	
			    QueryOrderRequest queryOrderRequest = new QueryOrderRequest();
			    queryOrderRequest.platid = chargeOrder.getPlattype();
			    queryOrderRequest.orderserialgame = String.valueOf(sn);
			    handleSpecialPlat(queryOrderRequest,chargeOrder);//有的会要平台的订单id，所以走这里
			    DeliveryManager.getInstance().send(queryOrderRequest); //向deliverManager发送订单查询请求 by changhao
				FushiManager.logger.info("重新发送轮询充值请求.chargeGameSn Or transcationid:"+queryOrderRequest.orderserialgame+"remain times:"+chargeOrder.getRetrytimes());
			    mkdb.Executor.getInstance().schedule(new CheckCharge(sn), (long) (30*Math.pow(2, 10-chargeOrder.getRetrytimes())), TimeUnit.SECONDS); 
				return true;
			}

			private void handleSpecialPlat(QueryOrderRequest queryOrderRequest, ChargeOrder chargeOrder) {
				String platType = chargeOrder.getPlattype();
			    if (chargeOrder.getTranscationid().length()>0 && chargeOrder.getReceipt().length()>0){
			    	queryOrderRequest.orderserialgame = chargeOrder.getTranscationid();
			    	queryOrderRequest.orderserialplat = chargeOrder.getReceipt();
			    }
			}
		}.submit();
	}
}
