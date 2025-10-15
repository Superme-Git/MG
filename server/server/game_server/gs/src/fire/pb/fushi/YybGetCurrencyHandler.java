package fire.pb.fushi;

import mkdb.Procedure;
import net.sf.json.JSONObject;

import org.apache.http.client.methods.HttpGet;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.spotcheck.SpotCheckManage;
import fire.pb.game.MoneyType;
import fire.pb.http.HttpCallBackHandler;
import fire.pb.main.Gs;
import fire.pb.state.PRoleOffline;
import fire.pb.statistics.StatisticUtil;

/***
 * 获取外部得来的符石货币
 * @author Administrator
 *
 */
public class YybGetCurrencyHandler extends HttpCallBackHandler {
	private final int userId;
	private final long roleId;
	
	public YybGetCurrencyHandler(int userId, long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}
	
	/***
	 * 重载executeHandler以实现各个逻辑不同的回调处理
	 * 注意:该方法在单独的xdb事务中，所以注意返回false时内部逻辑的回滚，以及发消息给客户端的方式
	 * @param 总游戏币，赠送游戏币，第一次充值，累计充值
	 * @return
	 */
	protected boolean executeHandler(int balance, int genBalance, int firstSave, int saveAmt) {
		return false;
	}
	
	@Override
	protected boolean process(JSONObject json) {
		FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:").append(userId)
				.append(",YybGetCurrencyHandler.process:json=").append(json.toString()));
		final String ret = json.getString("ret");
		if (ret.equals("0")) {
        	new mkdb.Procedure() {
				@Override
				protected boolean process() {
					int balance = Integer.valueOf(json.getString("balance")); //游戏币个数，包含赠送游戏币
					int genBalance = Integer.valueOf(json.getString("gen_balance")); //赠送游戏币个数
					int firstSave = Integer.valueOf(json.getString("first_save")); //是否满足首次充值，1满足，0不满足
					int saveAmt = Integer.valueOf(json.getString("save_amt")); //累计充值金额
					
					//记录符石数,并判断是否需要返符石
					xbean.YybFushiNums yybFs = xtable.Yybfushi.get(userId);
					if (yybFs == null) {
						yybFs = xbean.Pod.newYybFushiNums();
						xtable.Yybfushi.insert(userId, yybFs);
					}
					
					xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleId);
					if (yybFushiNum == null) {
						yybFushiNum = xbean.Pod.newYybFushiNum();
						yybFs.getRolefushi().put(roleId, yybFushiNum);
					}
					
					int localSaveAmt = yybFushiNum.getSaveamt();
					int localBalance = yybFushiNum.getBalance();
					int localGenBalance = yybFushiNum.getGenbalance();
					
					//有新的充值
					if (saveAmt - localSaveAmt > 0) {
						//先处理赠送符石的逻辑
						int midnum = saveAmt - localSaveAmt;
						if (midnum > 6480 * 2) {
							//有异常
							FushiManager.warnlogger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:").append(userId)
									.append(",出现异常:").append(midnum));
							return true;
						}
						int addNum = FushiManager.getInstance().getYybGenBalance(midnum);
						
						yybFushiNum.setSaveamt(saveAmt);
						
						yybFushiNum.setBalance(localBalance + midnum + addNum);
						yybFushiNum.setGenbalance(localGenBalance + addNum);
						
						//再处理同步配置的逻辑
						FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:").append(userId)
								.append(",同步数据:content=").append(",balance=").append(balance)
								.append(",genbalance=").append(genBalance)
								.append(",localGenBalance=").append(localGenBalance)
								.append("-------")
								.append(",midnum=").append(midnum)
								.append(",localBalance=").append(localBalance)
								.append(",addNum=").append(addNum)
								);
						
						int retBalance = balance - (localBalance + midnum + addNum);
						FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:").append(userId)
								.append(",公式是:").append("retBalance = balance - (localBalance + midnum + addNum)")
								.append(",计算结果:content=").append(",retBalance=").append(retBalance)
								);
						
						if (retBalance < 0) {
							//调用赠送的接口,改成异步
							addFushi(-retBalance, localSaveAmt, localBalance, localGenBalance);
						}
						else if (retBalance > 0) {
							//调用扣除的接口
							try {
								//生成订单号
								xbean.YingYongBao yyb = xtable.Yingyongbaoinfos.select(userId);
								
								xbean.YybOrder yybOrder = xbean.Pod.newYybOrder();
								long yybbillno = xtable.Yybchargeorder.insert(yybOrder);
								yybOrder.setUserid(userId);
								yybOrder.setRoleid(roleId);
								yybOrder.setNum(-retBalance);
								yybOrder.setLocalsaveamt(localSaveAmt);
								yybOrder.setLocalbalance(localBalance);
								yybOrder.setLocalgenbalance(localGenBalance);
								yybOrder.setStatus(0);
								yybOrder.setCreatetime(System.currentTimeMillis());
								yybOrder.setPlatname(yyb.getPlatformname());
								yybOrder.setConsumetype(FushiConst.REASON_YYB_SYNC_SUB);
								
								//生成扣除操作的请求
								HttpGet req = FushiManager.makeYybSubCurrencyRequest(userId, roleId, retBalance, yybbillno);
								if (req == null) {
									FushiManager.logger.error("FushiManager.subFushiFromUser:HttpGet is null");
									return false;
								}
								
								Gs.getHttpClient().execute(req, 
										new YybSubCurrencyHandler(userId, roleId, yybbillno) {
										@Override
										protected boolean executeHandler(int balance, int genBalance, int saveAmt) {
											return true;
										}
								});
							} catch (Throwable e) {
								e.printStackTrace();
								FushiManager.logger.error(e.toString());
								return false;
							}
						}
						
						if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
							fire.pb.fushi.DayPayManager.getInstance().ProcessDayPay(userId, roleId);
						}
						
						SReqFushiInfo snd = new SReqFushiInfo();
						snd.balance = yybFushiNum.getBalance();
						snd.genbalance = yybFushiNum.getGenbalance();
						snd.firstsave = firstSave;
						snd.saveamt = saveAmt;
						Procedure.psendWhileCommit(roleId, snd);
						FushiManager.logger.info("CReqFushiInfo:userid:" + userId + ",roleid:" + roleId + 
								",balance:" + yybFushiNum.getBalance() +
								",genbalance:" + yybFushiNum.getGenbalance() +
								",firstsave:" + firstSave +
								",saveamt:" + saveAmt);
						
						FushiManager.refreshRoleFushi(roleId, yybFushiNum.getBalance(), saveAmt);
						
						//增加vip经验
						new PAddVipExp(roleId, midnum, PAddVipExp.REASON_ADD_VIP_EXP_CHARGE).call();
						
						long oldall=yybFushiNum.getFushiall();
						yybFushiNum.setFushiall(yybFushiNum.getFushiall()+midnum+retBalance);//历史累计总符石
						
						FushiManager.logger.info(new StringBuilder("roleId=").append(roleId).append(",userId=").append(userId).append(",balance=")
								.append(balance).append(",genBalance=").append(genBalance).append(",firestSave=").append(firstSave).append(",saveAmt=")
								.append(saveAmt));
						
						xbean.Properties properties = xtable.Properties.get(roleId);
						if (properties != null) {
							properties.setLastchargetime(System.currentTimeMillis());
						}
						
						// 运营日志
						fire.log.YYLogger.OpChargeLog(roleId, "", "", 1, midnum / 10, midnum, addNum, "RMB", "", "", "yyb", firstSave);
						fire.log.YYLogger.OpTokenGetLog(roleId, YYLoggerTuJingEnum.tujing_Value_xitongzengsong, MoneyType.MoneyType_HearthStone, addNum, 0, new fire.log.beans.ItemBean());
						SpotCheckManage.refreshTradingOpenState(roleId, oldall,yybFushiNum.getFushiall());//刷新交易所是否开启
						
						//加入统计数据中
						long time = System.currentTimeMillis();
						StatisticUtil.updateChargeDayStats(userId, time, midnum + addNum, midnum / 1000); //里面乘了100,这里就除1000吧,是1块钱对10符石
						StatisticUtil.updateChargeWeekStats(userId, time, midnum + addNum);
						//加入运营数据log
						FushiManager.logCashChange(roleId, midnum + addNum, addNum);
						
						return true;
					}
					else {
						FushiManager.logger.info(new StringBuilder("roleId=").append(roleId).append(",userId=").append(userId).append(",localBalance=")
								.append(localBalance).append(",localGenBalance=").append(localGenBalance).append(",firestSave=").append(0).append(",localSaveAmt=")
								.append(localSaveAmt));
						
						SReqFushiInfo snd = new SReqFushiInfo();
						snd.balance = yybFushiNum.getBalance();
						snd.genbalance = yybFushiNum.getGenbalance();
						snd.firstsave = firstSave;
						snd.saveamt = saveAmt;
						Procedure.psendWhileCommit(roleId, snd);
						
						FushiManager.logger.info("CReqFushiInfo:userid:" + userId + ",roleid:" + roleId + 
								",balance:" + yybFushiNum.getBalance() +
								",genbalance:" + yybFushiNum.getGenbalance() +
								",firstsave:" + firstSave +
								",saveamt:" + saveAmt);
						
						FushiManager.refreshRoleFushi(roleId, yybFushiNum.getBalance(), saveAmt);
						return true;
					}
				}
			}.submit();
			return true;
		} else if (ret.equals("1018")) {
        	//登录效验失败
        	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
					.append(userId).append(",YybGetCurrencyHandler=").append(ret).append(",登录效验失败!"));
        	
        	//通知客户端重新登录
        	new fire.pb.state.PRoleOffline(roleId, PRoleOffline.TYPE_RETURN_LOGIN).submit();
        	
        	return returnLocalFushi();
		} else if (ret.equals("1001")) {
        	//参数错误
        	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
					.append(userId).append(",YybGetCurrencyHandler=").append(ret).append(",参数错误!"));
        	return returnLocalFushi();
        } else {
        	FushiManager.logger.info(new StringBuilder().append("roleid:").append(roleId).append(",userId:")
					.append(userId).append(",YybGetCurrencyHandler=").append(ret).append(",未知错误!"));
        	return returnLocalFushi();
        }
	}
	
	private boolean returnLocalFushi() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.YybFushiNums yybFs = xtable.Yybfushi.get(userId);
				if (yybFs == null) {
					yybFs = xbean.Pod.newYybFushiNums();
					xtable.Yybfushi.insert(userId, yybFs);
				}
				
				xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleId);
				if (yybFushiNum == null) {
					yybFushiNum = xbean.Pod.newYybFushiNum();
					yybFs.getRolefushi().put(roleId, yybFushiNum);
				}
				
				int localSaveAmt = yybFushiNum.getSaveamt();
				int localBalance = yybFushiNum.getBalance();
				int localGenBalance = yybFushiNum.getGenbalance();
				
				FushiManager.logger.info(new StringBuilder("返回本地符石数量,").append("roleId=").append(roleId).append(",userId=").append(userId).append(",localBalance=")
						.append(localBalance).append(",localGenBalance=").append(localGenBalance).append(",firestSave=").append(0).append(",localSaveAmt=")
						.append(localSaveAmt));
				
				SReqFushiInfo snd = new SReqFushiInfo();
				snd.balance = yybFushiNum.getBalance();
				snd.genbalance = yybFushiNum.getGenbalance();
				snd.firstsave = 0;
				snd.saveamt = localSaveAmt;
				Procedure.psendWhileCommit(roleId, snd);
				return true;
			}
		}.submit();
		
		return true;
	}
	
	public void addFushi(int addNum, int localSaveAmt, int localBalance, int localGenBalance) {
		try {
			//生成订单号
			xbean.YingYongBao yyb = xtable.Yingyongbaoinfos.select(userId);
			
			xbean.YybOrder yybOrder = xbean.Pod.newYybOrder();
			long billno = xtable.Yybchargeorder.insert(yybOrder);
			yybOrder.setUserid(userId);
			yybOrder.setRoleid(roleId);
			yybOrder.setNum(addNum);
			yybOrder.setLocalsaveamt(localSaveAmt);
			yybOrder.setLocalbalance(localBalance);;
			yybOrder.setLocalgenbalance(localGenBalance);
			yybOrder.setStatus(0);
			yybOrder.setCreatetime(System.currentTimeMillis());
			yybOrder.setPlatname(yyb.getPlatformname());
			
			//发送请求加符石的接口
			HttpGet req = FushiManager.makeYybAddCurrencyRequest(userId, roleId, addNum, billno);
			if (req == null) {
				FushiManager.logger.error(new StringBuilder("roleId=").append(roleId).append("调用赠送符石数失败"));
				return;
			}
			Gs.getHttpClient().execute(req, 
					new YybAddCurrencyHandler(userId, roleId, billno) {
					@Override
					protected boolean executeHandler(int balance, int genBalance, int saveAmt) {
						return true;
					}
			});
			
		} catch (Throwable e) {
			e.printStackTrace();
			FushiManager.logger.error(e.toString());
			return;
		}
	}
}
