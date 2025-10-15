package fire.pb.fushi;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.StateCommon;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.statistics.StatisticUtil;
import mkdb.Procedure;

/***
 * 加现金
 * @author changhao
 *
 */
public class PAddCoin extends Procedure {
	private static Logger logger=Logger.getLogger("RECHARGE");
	
	private final int userid;

	private final int zoneid;

	private final long sn;

	private final int cash;
	
	private final int rmb;
	
	private final int sellnum;
	
	private final Map<String, String> vars;
	
	private final String platSn ;
	private final String gameSn;
	private final String hint;

	public String getPlatSn() {
		return platSn;
	}

	public String getGameSn() {
		return gameSn;
	}

	public String getHint() {
		return hint;
	}

	public PAddCoin(int userid, int zoneid, long sn, int cash,Map<String, String> vars,int rmb,String platsn,String gamesn,int sellnum, String hint) {
		this.userid = userid;
		this.zoneid = zoneid;
		this.sn = sn;
		this.cash = cash;
		this.vars = vars;
		this.rmb = rmb;
		this.platSn = platsn;
		this.gameSn = gamesn;
		this.sellnum = sellnum;
		this.hint = hint;
	}

	@Override
	protected boolean process() throws Exception {
		if(fire.pb.fushi.Module.getIsYYBUser(userid)){
			FushiManager.logger.error("应用宝服务器，符石完全托管，禁用原来订单充值逻辑！");
			return true;
		}
		
        xbean.User user = xtable.User.select(userid);
		xbean.YbNums ybNums = xtable.Fushinum.get(userid);
		if (ybNums == null){
			ybNums = xbean.Pod.newYbNums();
			xtable.Fushinum.insert(userid, ybNums);
		}
		long roleid = user.getPrevloginroleid();
		xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid); //得到角色对应的符石数量 by changhao
		if (ybNum == null) {
			try {
				ybNum = xbean.Pod.newYbNum();
				ybNums.getRoleyb().put(roleid, ybNum);
			} catch (Throwable e) {
				logger.error("PAddCoin.process:User[" + userid + "],角色["+ roleid +"]充值符石YbNum插入错误！");
				return false;
			}
		}
		if (sn > 0 && ybNum.getSnlist().contains(sn)) {// 如果sn小于等于xdb里保存的sn,说明可能是重复的消息,不加符石,直接返回成功即可
		    logger.warn("PAddCoin.process:User[" + userid + "],角色["+ roleid +"]充值符石订单号sn:" + sn + ",已有的重复订单！");
			return true;
		} else {// 如果大于,说明是第一次接到这条协议,加符石,同时xdb里的sn更新
			int num = ybNum.getNum();
			long oldSn = ybNum.getSnlist().isEmpty()?0:ybNum.getSnlist().get(ybNum.getSnlist().size()-1);
			int chongzhiValue = cash;
			int newNum = num + chongzhiValue;// cash以分为单位
			if (newNum < 0 || newNum > FushiManager.MAX_NUM) {
				logger.info("PAddCoin.process:User[" + userid + "],角色["+ roleid +"]充值符石newNum:" + newNum + ",超出范围,错误！");
				return true;
			}
			ybNum.setNum(newNum);
			ybNum.setNopresentnum(sellnum + ybNum.getNopresentnum()); //没有赠送的实际符石 by changhao
			long oldall=ybNum.getFushiall();
			ybNum.setFushiall(ybNum.getFushiall()+cash);//累计获得符石
			if(sn > 0)
				ybNum.getSnlist().add(sn);

			//加入统计数据中
			long time=System.currentTimeMillis();
			StatisticUtil.updateChargeDayStats(userid,time,chongzhiValue,rmb);
			StatisticUtil.updateChargeWeekStats(userid,time,chongzhiValue);
			//加入运营数据log
			FushiManager.logCashChange(user.getPrevloginroleid(),ybNum, chongzhiValue, 0, 0, FushiConst.REASON_CASH_FUSHI);
			SpotCheckManage.refreshTradingOpenState(roleid, oldall,ybNum.getFushiall());//刷新交易所是否开启
			// 刷新符石数量
			final xbean.User u = xtable.User.select(userid);
			if (u != null) {
				List<Long> roleids = u.getIdlist();
				for (Long cur_roleid : roleids) {
					if (StateCommon.isOnline(cur_roleid)) {
						FushiManager.refreshRoleFushi(cur_roleid, ybNum, true);
						break;
					}
				}
			}
			logger.info("PAddCoin.process:User[" + userid + "],角色["+ roleid +"]充值符石数:" + sellnum + ",现有总量:" + (ybNum.getNum() + ybNum.getSysnum()));
		}
		
		pexecuteWhileCommit(new PChargeReturnProfit(user.getPrevloginroleid())); //更新充值返利状态 by changhao
		
		//如果充值成功后要做些处理,比如充值给奖励之类的,在PChongzhiSucc中处理
		//MasterManager.getInstance().updateStlx(user.getPrevloginroleid(), MasterManager.CASH_KEY, 1, cash);
		
		pexecuteWhileCommit(new PReqPay(user.getPrevloginroleid()));
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
		{
			DayPayManager.getInstance().ProcessDayPay(userid, user.getPrevloginroleid());
		}	
		return true;
	}

}
