package fire.pb.http.cbg;

import fire.pb.blackmarket.srv.BlackMarketManager;
import fire.pb.blackmarket.utils.BlackMarketUtils;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.shop.SGoldOrderUpBlackMarket;
import fire.pb.talk.MessageMgr;
import net.sf.json.JSONObject;

/**
 * 藏宝阁异步HTTP请求回调
 * author yebin @ 2016年8月6日
 */
public class CBGOrderUpHttpCallBackHandler extends HttpCallBackHandler {
	
	
	private long roleId = 0, pid = 0;
	private int orderType = 0;
	
	public CBGOrderUpHttpCallBackHandler(long roleId, long pid, int orderType) {
		super();
		this.roleId = roleId;
		this.pid = pid;
		this.orderType = orderType;
	}

	/**
	 * 返回code码	含义
	 * 1		成功
	 * 20001	sign校验失败
	 * 20017	缺少参数
	 * 24001	商品编号pid重复
	 * 20001	sign校验失败
	 * 24003	商品编号pid不存在
	 * 10001	其他错误
	 */
	@Override
	protected boolean process(JSONObject json) {
		//打印下Json信息
		BlackMarketManager.LOG.error(json);
		
		// 查看收到的结果
		Integer code = json.getInt("code");
		if (code != 1){
			BlackMarketManager.LOG.error(new StringBuilder("平台应答订单上架：").append(json.toString()));
			return false;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.get(roleId);
				if (null == roleBlackMarket) {
					BlackMarketManager.LOG.error("平台应答订单上架：role=" + roleId + ", pid=" + pid + ", orderType=" + orderType + ", 黑市订单表中不存在此角色信息, 有错误！");
					return false;
				}
				xbean.GoldOrder goldOrder = roleBlackMarket.getGoldordersale().get(pid);
				if (null == goldOrder) {
					BlackMarketManager.LOG.error("平台应答订单上架：role=" + roleId + ", pid=" + pid + ", orderType=" + orderType + ", 角色售卖表中不存在此订单！");
					return false;
				}
				int orderState = goldOrder.getState();
				if (orderState != BlackMarketUtils.OrderState.STAY_UP) {
					BlackMarketManager.LOG.error("平台应答订单上架：role=" + roleId + ", pid=" + pid + ", orderType=" + orderType + ", order state=" + orderState + ", 订单状态非法. ");
					return false;
				}
				goldOrder.setState(BlackMarketUtils.OrderState.STAY_SALE);
				
				if (BlackMarketManager.LOG.isInfoEnabled()) {
					BlackMarketManager.LOG.info("平台应答订单上架订单：role=" + roleId + ", pid=" + pid + ", gold=" + goldOrder.getNumber());
				}
				
				MessageMgr.sendMsgNotify(roleId, 162179, null);// 上架成功
				
				// 上架通知
				fire.pb.shop.GoldOrder order = BlackMarketUtils.xbeanGoldOrderCopyGoldOrder(goldOrder);
				SGoldOrderUpBlackMarket goldOrderUp = new SGoldOrderUpBlackMarket(order);
				mkdb.Procedure.psendWhileCommit(roleId, goldOrderUp);
				
				return true;
			}
		}.submit();
		
		return false;
	}
}
