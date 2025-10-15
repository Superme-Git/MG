package fire.pb.fushi;

import org.apache.http.client.methods.HttpGet;

import fire.pb.main.Gs;

public class CheckYybCharge implements Runnable {
	private final long chargeorder;
	public CheckYybCharge(final long chargeorder) {
		super();
		this.chargeorder = chargeorder;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.YybOrder yybOrder = xtable.Yybchargeorder.get(chargeorder);
				if (yybOrder == null) {
					FushiManager.logger.info("应用宝订单不存在:"+chargeorder);
					return false;
				}
				
				if (yybOrder.getRetrytimes() <= 0) {
					FushiManager.logger.info("应用宝订单重试次达到上限:"+chargeorder);
					
					//打印出订单信息
					FushiManager.logger.info(new StringBuilder().append("应用宝订单失败,userid:").append(yybOrder.getUserid())
							.append(",roleid:").append(yybOrder.getRoleid())
							.append(",num:").append(yybOrder.getNum())
							.append(",platname:").append(yybOrder.getPlatname()));
					
					pexecuteWhileCommit(new PRevertYybFushi(chargeorder, yybOrder.getUserid(), yybOrder.getRoleid()));
					return true;
				}
				
				yybOrder.setRetrytimes(yybOrder.getRetrytimes() - 1);
				if (yybOrder.getNum() > 0) {
					HttpGet req = FushiManager.makeYybAddCurrencyRequest(yybOrder.getUserid(), yybOrder.getRoleid(), yybOrder.getNum(), chargeorder);
					if (req == null) {
						FushiManager.logger.error(new StringBuilder().append("应用宝重试订单失败,userid:").append(yybOrder.getUserid())
								.append(",roleid:").append(yybOrder.getRoleid()).append(",FushiManager.addFushiToUser:HttpGet is null"));
						return false;
					}
					
					Gs.getHttpClient().execute(req, 
							new YybAddCurrencyHandler(yybOrder.getUserid(), yybOrder.getRoleid(), chargeorder) {
							@Override
							protected boolean executeHandler(int balance, int genBalance, int saveAmt) {
								return true;
							}
					});
				}
				else {
					HttpGet req = FushiManager.makeYybSubCurrencyRequest(yybOrder.getUserid(), yybOrder.getRoleid(), -yybOrder.getNum(), chargeorder);
					if (req == null) {
						FushiManager.logger.error(new StringBuilder().append("应用宝重试订单失败,userid:").append(yybOrder.getUserid())
								.append(",roleid:").append(yybOrder.getRoleid()).append("FushiManager.addFushiToUser:HttpGet is null"));
						return false;
					}
					
					Gs.getHttpClient().execute(req, 
							new YybSubCurrencyHandler(yybOrder.getUserid(), yybOrder.getRoleid(), chargeorder) {
							@Override
							protected boolean executeHandler(int balance, int genBalance, int saveAmt) {
								return true;
							}
					});
				}
				
				return true;
			}
			
		}.submit();
	}
}
