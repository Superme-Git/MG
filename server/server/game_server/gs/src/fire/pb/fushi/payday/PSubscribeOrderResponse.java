package fire.pb.fushi.payday;

import fire.pb.GsClient;
import fire.pb.fushi.FushiManager;
import mkdb.Procedure;

/**
 * 傻逼订阅游戏的订单响应
 */
public class PSubscribeOrderResponse extends Procedure {
	
	private long roleid;
	private int subtime; //订阅时间,单位是啥？天？
	
	public PSubscribeOrderResponse(long roleid, int subtime)
	{
		this.roleid = roleid;
		this.subtime = subtime;
	}
	
	@Override
	protected boolean process() throws Exception {
		long cur = System.currentTimeMillis();
		xbean.subscription sub = xtable.Subscriptions.get(roleid);
		if(sub == null) {
			sub = xbean.Pod.newsubscription();
			sub.setExpiretime(0);
			sub.setSubscribetime(0);
		}
		sub.setSubscribetime(cur);
		if (sub.getExpiretime() <=0){
			//如果玩家没有订阅记录
			sub.setExpiretime(cur + subtime * 24 * 60 * 60 * 1000);
			FushiManager.logger.info("PSubscribeOrderResponse:["+ roleid +"]首次订阅游戏,到期时间:["+ sub.getExpiretime() + "].");
		} else if(sub.getExpiretime() > 0 && cur <= sub.getExpiretime()) {
			//如果玩家有订阅记录，且订阅过期时间>0，且当前时间在过期时间内，说明订阅有效，叠加时间
			sub.setExpiretime(sub.getExpiretime() + subtime * 24 * 60 * 60 * 1000);
			FushiManager.logger.info("PSubscribeOrderResponse:["+ roleid +"]订阅游戏,叠加后到期时间:["+ sub.getExpiretime() + "].");
		} else if(sub.getExpiretime() > 0 && cur > sub.getExpiretime()) {
			//如果玩家有订阅记录，且订阅过期时间>0，且当前时间超过过期时间，说明订阅过期失效
			sub.setExpiretime(cur + subtime * 24 * 60 * 60 * 1000);
			FushiManager.logger.info("PSubscribeOrderResponse:["+ roleid +"]订阅游戏,到期时间:["+ sub.getExpiretime() + "].");
		}
		fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleid, 162170, null/*java.util.Arrays.asList("0","1","2")*/);
		xtable.Subscriptions.add(roleid, sub);
		GsClient.pSendWhileCommit(new fire.msp.role.GSetNoPayDayState(roleid, (byte)0));
		SQuerySubscribeInfo send = new SQuerySubscribeInfo();
		send.subscribetime = sub.getSubscribetime();
		send.expiretime = sub.getExpiretime();
		psendWhileCommit(roleid, send);
		fire.pb.fushi.payday.SHaveDayPay msg = new fire.pb.fushi.payday.SHaveDayPay();
		msg.daypay = 1;
		mkdb.Procedure.psendWhileCommit(roleid, msg);
		return true;
	}

}
