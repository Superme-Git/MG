package fire.pb.fushi;

import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.state.PRoleOffline;

public class YybAddCurrencyHandler extends HttpCallBackHandler {
		private final int userId;
		private final long roleId;
		private final long billno;
		
		public YybAddCurrencyHandler(int userId, long roleId, long billno) {
			this.userId = userId;
			this.roleId = roleId;
			this.billno = billno;
		}
		
		/***
		 * 重载executeHandler以实现各个逻辑不同的回调处理
		 * 注意:该方法在单独的xdb事务中，所以注意返回false时内部逻辑的回滚，以及发消息给客户端的方式
		 * @param
		 * @return
		 */
		protected boolean executeHandler(int balance, int genBalance, int saveAmt) {
			return false;
		}
		
		@Override
		protected boolean process(JSONObject json) {
			FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:").append(userId)
					.append(",YybAddCurrencyHandler.process:json=").append(json.toString()));
			
			final String ret = json.getString("ret");
			if (ret.equals("0")) {
				new mkdb.Procedure() {
    				@Override
    				protected boolean process() {
    					Long addbillno = Long.valueOf(json.getString("billno")); //预扣流水号
    					if (addbillno == null) {
    						FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
    								.append(userId).append(",添加符石流水号为空:").append(json.toString()));
    						return false;
    					}
    					
    					xbean.YybOrder yybOrder = xtable.Yybchargeorder.select(addbillno);
    					if (yybOrder == null) {
    						FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
    								.append(userId).append(",增加符石请求,yybOrder无效:").append(addbillno));
    						return false;
    					}
    					
    					xtable.Yybchargeorder.remove(addbillno);
    					FushiManager.logger.info("YybAddCurrencyHandler.process:json=" + json.toString());
    					
    					return true;
    				}
    			}.submit();
				return true;
            } else if(ret.equals("1018")) {
            	//登录效验失败
            	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
						.append(userId).append(",addFushiToUser:ret=").append(ret).append(",登录效验失败!"));
            	
            	//通知客户端重新登录
            	new fire.pb.state.PRoleOffline(roleId, PRoleOffline.TYPE_RETURN_LOGIN).submit();
            	return true;
            } else if (ret.equals("1001")) {
            	//pfkey not valid, 异常情况，需要把符石扣掉
            	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
						.append(userId).append(",addFushiToUser:ret=").append(ret).append(",出现非法请求"));
            	
            	Long addbillno = Long.valueOf(json.getString("billno")); //预扣流水号
				if (addbillno == null) {
					FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
							.append(userId).append(",添加符石流水号为空:").append(json.toString()));
					return false;
				}
				new PRevertYybFushi(addbillno, userId, roleId).submit();
            	return true;
            }
            else {
            	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
						.append(userId).append(",addFushiToUser:ret=").append(ret).append(",未知错误!"));
            	//轮循处理该订单
            	mkdb.Executor.getInstance().schedule(new CheckYybCharge(billno), 10, TimeUnit.SECONDS);
            	return false;
            }
		}
}
