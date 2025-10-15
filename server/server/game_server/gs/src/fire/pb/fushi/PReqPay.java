

package fire.pb.fushi;

import mkdb.Procedure;
import java.util.LinkedList;
import java.util.Map;
import fire.pb.fushi.GoodInfo;
import fire.pb.fushi.SAddCash;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;

/***
 * 请求充值
 * @author changhao
 *
 */
public class PReqPay extends Procedure {

	long roleid;
	/**
	 * Creates a new instance of PReqCharge.
	 *
	 * @param roleid
	 */
	
	public PReqPay(long roleid) {

		this.roleid = roleid;
	}
	@Override
	protected boolean process() throws Exception {
		
		Integer userid = xtable.Properties.selectUserid(roleid);
		SReqCharge sReqCharge = new SReqCharge();
		xbean.AUUserInfo auUserInfo = xtable.Auuserinfo.select(userid);
		if (auUserInfo == null){
			fire.pb.fushi.FushiManager.logger.error("auuserinfo null.userid+"+userid);
			return false;
		}
		String platString = "locojoy";//auUserInfo.getNickname().substring(0, 4); //(test)登陆里面有个和平台相关的名字 by changhao
		Map<Integer, LinkedList<GoodInfo>> platGoods = FushiManager.getInstance().goodListMap.get(platString);
		if (platGoods == null)
			platGoods = FushiManager.getInstance().goodListMap.get(FushiConst.LOCOJOYPLAT);
		LinkedList<GoodInfo> commonGoods = platGoods.get(0); //服务器ID by changhao
		if (commonGoods != null){
			for (GoodInfo goodInfo : commonGoods) {
				if(fire.pb.fushi.Module.GetPayServiceType() == 0){
					if (ConfigManager.getInstance().getConf(SAddCash.class).get(goodInfo.goodid).gameshow==1) //加入所用充值商品 by changhao 
						sReqCharge.goods.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}else{
					if (ConfigManager.getInstance().getConf(SAddCashPCard.class).get(goodInfo.goodid).gameshow==1) //加入所用充值商品 by changhao 
						sReqCharge.goods.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}
			}
		}
		LinkedList<GoodInfo> serveridGoods = platGoods.get(Gs.serverid);
		if (serveridGoods != null && ConfigManager.getGsZoneId()>0) {
			for (GoodInfo goodInfo : serveridGoods) {
				if(fire.pb.fushi.Module.GetPayServiceType() == 0){
					if (ConfigManager.getInstance().getConf(SAddCash.class).get(goodInfo.goodid).gameshow==1) 
						sReqCharge.goods.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}else{
					if (ConfigManager.getInstance().getConf(SAddCashPCard.class).get(goodInfo.goodid).gameshow==1) 
						sReqCharge.goods.add(new GoodInfo(goodInfo.goodid, goodInfo.price, goodInfo.fushi, goodInfo.present, goodInfo.beishu));
				}
			}
		}
		
		xbean.RoleDoubleChareInfo multiCharge = xtable.Roledoublecharge.select(roleid); //加倍充值返回记录 by changhao
		
		//初始化多倍符石
		if(multiCharge == null || multiCharge.getActivetime() < FushiManager.MULTICHARGE_CHARGE_START_TIME){
			multiCharge = xtable.Roledoublecharge.get(roleid);
			if(multiCharge == null){
				multiCharge = xbean.Pod.newRoleDoubleChareInfo();
				xtable.Roledoublecharge.insert(roleid, multiCharge);
			}
			FushiManager.getInstance().initRoleDoubleChareInfo(multiCharge, sReqCharge.goods); //初始化玩家的多倍 返还充值情况 by changhao
		}
		sReqCharge.opendy = ConfigManager.OPEN_DY ? 1 : 0;
		psendWhileCommit(roleid, sReqCharge);
		
	    return true;
	}

}

