package fire.pb.http.cbg;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.blackmarket.srv.BlackMarketManager;
import fire.pb.blackmarket.utils.BlackMarketUtils;
import fire.pb.game.MoneyType;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.item.Pack;
import fire.pb.shop.SGoldOrderDownBlackMarket;
import fire.pb.talk.MessageMgr;
import net.sf.json.JSONObject;

/**
 * 藏宝阁异步HTTP请求回调
 * author yebin @ 2016年8月6日
 */
public class CBGOrderDownHttpCallBackHandler extends HttpCallBackHandler {
	
	
	private long roleId = 0, pid = 0;
	private int orderType = 0;
	
	public CBGOrderDownHttpCallBackHandler(long roleId, long pid, int orderType) {
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
			BlackMarketManager.LOG.error(new StringBuilder("平台应答订单下架：").append(json.toString()));
			MessageMgr.sendMsgNotify(roleId, 162178, null);// 订单被平台锁定不能下架
			return false;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.RoleBlackMarket roleBlackMarket = xtable.Blackmarkettab.get(roleId);
				if (null == roleBlackMarket) {
					BlackMarketManager.LOG.error("平台应答订单下架role==" + roleId + ", pid=" + pid + ", orderType=" + BlackMarketUtils.OrderType.GOLD + ", 黑市订单表中不存在此角色信息, 有错误！");
					return false;
				}
				xbean.GoldOrder goldOrder = roleBlackMarket.getGoldordersale().remove(pid);
				if (null == goldOrder) {
					BlackMarketManager.LOG.error(new StringBuilder("平台应答订单下架：role=").append(roleId).append(", pid=").append(pid)
							.append(", orderType=").append(orderType).append(", 角色售卖表中不存在此订单！"));
					return false;
				}
				int orderState = goldOrder.getState();
				if (orderState != BlackMarketUtils.OrderState.STAY_SALE) {
					BlackMarketManager.LOG.error(new StringBuilder("平台应答订单下架：role=").append(roleId).append(", pid=").append(pid)
							.append(", orderType=").append(orderType).append(", order state=").append(orderState).append(", 订单状态非法."));
					return false;
				}
				
				// 返还金币
				Pack pack = new Pack(roleId, false);
				long goldNubmer = pack.getCurrency(MoneyType.MoneyType_GoldCoin);
				if (goldNubmer >= Long.MAX_VALUE) {
					StringBuilder sbd = new StringBuilder();
					sbd.append("平台应答订单下架：role=").append(roleId).append(", gold=").append(goldNubmer).append(", 再添加金币就溢出了！");
					BlackMarketManager.LOG.error(sbd);
					return false;
				}
				long gold = goldOrder.getNumber();
				long backGold = pack.addSysGold(gold, "", YYLoggerTuJingEnum.tujing_Value_baishi, 1);
				if (gold != backGold) {
					BlackMarketManager.LOG.error("严重错误平台应答订单下架：role=" + roleId + ", pid=" + pid + ", gold=" + gold + ", backGold=" + backGold + ", 订单金币数量和下架返回金币数量不匹配！");
					return false;
				}
				pack.addSysCurrency((long)(gold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_CBG))
						, MoneyType.MoneyType_EreditPoint, "平台应答订单下架	", YYLoggerTuJingEnum.tujing_Value_balckmarket, 0);
				
				if (BlackMarketManager.LOG.isInfoEnabled()) {
					BlackMarketManager.LOG.info("平台应答订单下架：role=" + roleId + ", pid=" + pid + ", gold=" + backGold + ", 金币退回！");
				}
				
				MessageMgr.sendMsgNotify(roleId, 162180, null);// 下架成功
				
				// 下架通知
				SGoldOrderDownBlackMarket goldOrderDown = new SGoldOrderDownBlackMarket(goldOrder.getPid());
				mkdb.Procedure.psendWhileCommit(roleId, goldOrderDown);
				
				return true;
			}
		}.submit();
		
		return false;
	}
}
