
package fire.pb.fushi;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.StateCommon;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.master.MasterManager;
import fire.pb.statistics.StatisticUtil;
import mkdb.Procedure;

/***
 * 加现金
 * @author changhao
 *
 */
public class PAddCoinTest extends Procedure {

	private static Logger logger=Logger.getLogger("RECHARGE");
	
	private final int userid;

	private final int zoneid;

	private final long sn;

	private final int cash;
	
	private final int rmb;
	
	private final int sellnum;
	

	public int getSellnum() {
		return sellnum;
	}
	
	

	public int getZoneid() {
		return zoneid;
	}



	public PAddCoinTest(int userid, int zoneid, long sn, int cash,Map<String, String> vars,int rmb,String platsn,String gamesn,int sellnum,String hint) {

		this.userid = userid;
		this.zoneid = zoneid;
		this.sn = sn;
		this.cash = cash;
		this.rmb = rmb;
		this.sellnum = sellnum;
	}

	@Override
	protected boolean process() throws Exception {

        xbean.User user = xtable.User.select(userid);
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		if (ybNums == null){
			ybNums = xbean.Pod.newYbNums();
			xtable.Fushinum.insert(userid, ybNums);
		}
		xbean.YbNum ybNum = ybNums.getRoleyb().get(user.getPrevloginroleid());
		if (ybNum == null) {
			try {
				ybNum = xbean.Pod.newYbNum();
				ybNums.getRoleyb().put(user.getPrevloginroleid(), ybNum);
			} catch (Throwable e) {
				logger.error("PAddCoin.process:User[" + userid + "]充值符石YbNum插入错误！");
				return false;
			}
		}
		if (ybNum.getSnlist().contains(sn)) {// 如果sn小于等于xdb里保存的sn,说明可能是重复的消息,不加符石,直接返回成功即可
			logger.warn("PAddCoinTest.process:User[" + userid + "]充值符石订单号sn:" + sn + ",已有的重复订单！");
			return true;
		} else {// 如果大于,说明是第一次接到这条协议,加符石,同时xdb里的sn更新
			int num = ybNum.getNum();
			ybNum.getSnlist().isEmpty();
			ybNum.getSnlist().get(ybNum.getSnlist().size()-1);
			int chongzhiValue = cash;
			int newNum = num + chongzhiValue;// cash以分为单位
			if (newNum < 0 || newNum > FushiManager.MAX_NUM) {
				logger.info("PAddCoinTest.process:User[" + userid + "]充值符石newNum:" + newNum + ",超出范围,错误！");
				return true;
			}
			ybNum.setNum(newNum);
			ybNum.getSnlist().add(sn);
			long oldall=ybNum.getFushiall();
			ybNum.setFushiall(ybNum.getFushiall()+cash);//累计获得符石

			//加入统计数据中
			long time=System.currentTimeMillis();
			StatisticUtil.updateChargeDayStats(userid,time,chongzhiValue,rmb);
			StatisticUtil.updateChargeWeekStats(userid,time,chongzhiValue);
			//加入运营数据log
			FushiManager.logCashChange(user.getPrevloginroleid(),ybNum, chongzhiValue, 0, 0, FushiConst.REASON_CASH_FUSHI);
			// 刷新符石数量
			final xbean.User u = xtable.User.select(userid);
			if (u != null) {
				List<Long> roleids = u.getIdlist();
				for (Long roleid : roleids) {
					if (StateCommon.isOnline(roleid)) {
						FushiManager.refreshRoleFushi(roleid, ybNum, true);
						SpotCheckManage.refreshTradingOpenState(roleid, oldall,ybNum.getFushiall());//刷新交易所是否开启
						break;
					}
				}
			}
			logger.info("PAddCoinTest.process:User[" + userid + "]充值符石数:" + sellnum + ",现有总量:" + (ybNum.getNum() + ybNum.getSysnum()));
		}
		MasterManager.getInstance().updateStlx(user.getPrevloginroleid(), MasterManager.CASH_KEY, 1, cash);
		return true;
	}

}
