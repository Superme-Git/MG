package fire.pb.fushi.spotcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import fire.log.YYLogger;
import fire.log.beans.OpPointGoldExBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.SSpotCardConfig;
import fire.pb.game.MoneyType;
import fire.pb.talk.MessageMgr;

public class PTradingSpotCardCenter extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	public PTradingSpotCardCenter() {
	}

	@Override
	protected boolean process() {
		SSpotCardConfig sSpotCardConfigdata=SpotCheckManage.sSpotCardConfig.get(1);
		
		xbean.TradingMap tradingMap = xtable.Tradingidtab.get(1);// 最大锁,锁住全部交易
		if (tradingMap == null) {
			return false;
		}
		Map<Integer, LinkedList<Integer>> retMap = getTrade(tradingMap);
		for (Map.Entry<Integer, LinkedList<Integer>> e : retMap.entrySet()) {
			xbean.TradingPrice buy = tradingMap.getBuylist().get(e.getKey());
			for (int sellIndex : e.getValue()) {
				xbean.TradingPrice sell = tradingMap.getSelllist().get(sellIndex);
				//交易过程
				//锁住卖单和买单
				xbean.RoleTradingRecord buy_roleTradingRecord=xtable.Roletradingrecordstab.get(buy.getTradingid());
				xbean.RoleTradingRecord sell_roleTradingRecord=xtable.Roletradingrecordstab.get(sell.getTradingid());
				if(buy_roleTradingRecord==null||sell_roleTradingRecord==null){
					//这里处理异常，就是内存中的值和数据库的值无法对应了，需要移除内存中的值
					if(buy_roleTradingRecord==null){
						tradingMap.getBuylist().remove(buy);
						logger.error("点卡服点卡交易\t"+"购买列表有异常了");
					}
					if(sell_roleTradingRecord==null){
						tradingMap.getSelllist().remove(sell);
						logger.error("点卡服点卡交易\t"+"出售列表有异常了");
					}
					break;
				}
				int buynum=buy_roleTradingRecord.getCurnum();
				int sellnum=sell_roleTradingRecord.getCurnum();
				if(buynum<=0 || sellnum <= 0){
					break;
				}
				//加锁
				List<Long> list = new ArrayList<Long>();
				list.add(buy_roleTradingRecord.getRoleid());
				list.add(sell_roleTradingRecord.getRoleid());
				mkdb.Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, list));
				
				if(buynum>=sellnum){
					//处理卖方
					dealSellRole(sell_roleTradingRecord,buy_roleTradingRecord,sellnum,sSpotCardConfigdata.getTradingshui());
					//买方
					dealBuyRole(sell_roleTradingRecord,buy_roleTradingRecord,sellnum);
					//同步内存
					buy.setNum(buy.getNum()-sellnum);
					if(buy.getNum()<=0){
						tradingMap.getBuylist().remove(buy);
					}
					sell.setNum(0);
					tradingMap.getSelllist().remove(sell);
				}else{
					//处理卖方
					dealSellRole(sell_roleTradingRecord,buy_roleTradingRecord,buynum,sSpotCardConfigdata.getTradingshui());
					//买方
					dealBuyRole(sell_roleTradingRecord,buy_roleTradingRecord,buynum);
					//同步内存
					buy.setNum(0);
					tradingMap.getBuylist().remove(buy);
					
					sell.setNum(sell.getNum()-buynum);
					if(sell.getNum()<=0){
						tradingMap.getSelllist().remove(sell);
					}
				}
				//刷新目标界面
				SpotCheckManage.refreshAllView(buy_roleTradingRecord.getRoleid());
				SpotCheckManage.refreshAllView(sell_roleTradingRecord.getRoleid());
				break;
			}
			break;
		}
		
		if (getTrade(tradingMap).size() > 0) {
			mkdb.Procedure.pexecuteWhileCommit(new PTradingSpotCardCenter());
		}
		return true;
	}
	
	/**
	 * 处理卖出玩家的逻辑
	 * @param sell_roleTradingRecord
	 * @param buy_roleTradingRecord
	 * @param num
	 * @param tradingshui
	 */
	private void dealSellRole(xbean.RoleTradingRecord sell_roleTradingRecord,xbean.RoleTradingRecord buy_roleTradingRecord,int num,double tradingshui){
		//处理卖单数量
		sell_roleTradingRecord.setCurnum(sell_roleTradingRecord.getCurnum()-num); // 当前数量
		if(sell_roleTradingRecord.getCurnum()<=0){
			//移除记录
			xtable.Roletradingrecordstab.remove(sell_roleTradingRecord.getTradingid());
			//处理卖单的挂单记录
			xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.get(sell_roleTradingRecord.getRoleid());
			if(roleTradingInfoList!=null){
				//移除记录
				roleTradingInfoList.getTradinglist().remove(sell_roleTradingRecord.getTradingid());
			}
		}
		//添加记录
		xbean.RoleTradingHisRecordList sellroleTradingHisRecordList=xtable.Roletradinghisrecordlisttab.get(sell_roleTradingRecord.getRoleid());
		if(sellroleTradingHisRecordList==null){
			sellroleTradingHisRecordList=xbean.Pod.newRoleTradingHisRecordList();
			xtable.Roletradinghisrecordlisttab.insert(sell_roleTradingRecord.getRoleid(), sellroleTradingHisRecordList);
		}
		if(sellroleTradingHisRecordList.getRoletradinghisrecordlist().size()>SpotCheckManage.MAX_HIS_RECORD){
			//删除一条记录
			sellroleTradingHisRecordList.getRoletradinghisrecordlist().remove(0);
		}
		xbean.RoleTradingHisRecord sellroleTradingHisRecord=SpotCheckManage.createRoleTradingHisRecord(sell_roleTradingRecord.getTradingid(),SpotCheckManage.TYPE_SELL,num,
				sell_roleTradingRecord.getAllnum(),sell_roleTradingRecord.getPrice(),sell_roleTradingRecord.getCreatetime());
		sellroleTradingHisRecordList.getRoletradinghisrecordlist().add(sellroleTradingHisRecord);
		
		
		//卖出玩家添加金币，并且扣除税
		int shui=(int) Math.ceil(sell_roleTradingRecord.getPrice()*num/100.0*tradingshui);
		int addgold=sell_roleTradingRecord.getPrice()*num-shui;
		fire.pb.item.Pack bag = new fire.pb.item.Pack(sell_roleTradingRecord.getRoleid(), false);
		bag.addSysGold(addgold, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0);
		
		bag.addSysCurrency((long)(addgold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_FSBUYGOLD))
				, MoneyType.MoneyType_EreditPoint, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0);
		
/*		fire.pb.item.Pack bag2 = new fire.pb.item.Pack(buy_roleTradingRecord.getRoleid(), false);
		bag2.addSysCurrency((long)((sell_roleTradingRecord.getPrice()*num)*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_GOLD2FS))
				, MoneyType.MoneyType_EreditPoint, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0);
*/
		logger.info(buy_roleTradingRecord.getRoleid()+"买单订单号\t"+buy_roleTradingRecord.getTradingid()+"\t购买符石，购买数量"+num
				+"对应出售玩家，挂单号"+sell_roleTradingRecord.getTradingid()+"\t价格"+sell_roleTradingRecord.getPrice()+"\t总数量\t"+sell_roleTradingRecord.getAllnum()+"\t卖出数量"+num
				+"扣除税"+shui+"\t获得金币"+addgold);
		//运营日志
		OpPointGoldExBean pPointGoldExBean = new OpPointGoldExBean(SpotCheckManage.TYPE_SELL, num, sell_roleTradingRecord.getPrice(), shui, 1);
		YYLogger.pointGoldExLog(sell_roleTradingRecord.getRoleid(), pPointGoldExBean);
		
		MessageMgr.psendSystemMessageToRole(sell_roleTradingRecord.getRoleid(), 300002, Arrays.asList(addgold+""));
	}
	
	/**
	 * 处理买家逻辑
	 * @param sell_roleTradingRecord
	 * @param buy_roleTradingRecord
	 * @param num
	 */
	public void dealBuyRole(xbean.RoleTradingRecord sell_roleTradingRecord,xbean.RoleTradingRecord buy_roleTradingRecord,int num){
		//处理买方
		buy_roleTradingRecord.setCurnum(buy_roleTradingRecord.getCurnum()-num); // 当前数量
		if(buy_roleTradingRecord.getCurnum()<=0){
			//移除记录
			xtable.Roletradingrecordstab.remove(buy_roleTradingRecord.getTradingid());
			//处理买单的挂单记录
			xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.get(buy_roleTradingRecord.getRoleid());
			if(roleTradingInfoList!=null){
				//移除记录
				roleTradingInfoList.getTradinglist().remove(buy_roleTradingRecord.getTradingid());
			}
		}
		//记录买方交易记录
		xbean.RoleTradingHisRecordList buyroleTradingHisRecordList=xtable.Roletradinghisrecordlisttab.get(buy_roleTradingRecord.getRoleid());
		if(buyroleTradingHisRecordList==null){
			buyroleTradingHisRecordList=xbean.Pod.newRoleTradingHisRecordList();
			xtable.Roletradinghisrecordlisttab.insert(buy_roleTradingRecord.getRoleid(), buyroleTradingHisRecordList);
		}
		xbean.RoleTradingHisRecord buyroleTradingHisRecord=SpotCheckManage.createRoleTradingHisRecord(buy_roleTradingRecord.getTradingid(),SpotCheckManage.TYPE_BUY,num,
				buy_roleTradingRecord.getAllnum(),sell_roleTradingRecord.getPrice(),buy_roleTradingRecord.getCreatetime());
		buyroleTradingHisRecordList.getRoletradinghisrecordlist().add(buyroleTradingHisRecord);//添加记录
		//买入玩家添加符石
		int userid= xtable.Properties.selectUserid(buy_roleTradingRecord.getRoleid());
		Procedure.pexecuteWhileCommit(new PAddFuShi(userid, buy_roleTradingRecord.getRoleid(),num,FushiConst.CASH_FUSHI, YYLoggerTuJingEnum.tujing_Value_spotcard));
		//添加差价
		int delgold=buy_roleTradingRecord.getPrice()-sell_roleTradingRecord.getPrice();
		if(delgold>0){
			int alldelnum=delgold*num;
			int allsellgold=sell_roleTradingRecord.getPrice()*num;//成交总金额
			fire.pb.item.Pack bag = new fire.pb.item.Pack(buy_roleTradingRecord.getRoleid(), false);
			bag.addSysGold(alldelnum, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0);
			MessageMgr.psendSystemMessageToRole(buy_roleTradingRecord.getRoleid(), 300009, Arrays.asList(buy_roleTradingRecord.getPrice()+""
					,sell_roleTradingRecord.getPrice()+"",allsellgold+"",num+"",alldelnum+""));
		}else{
			MessageMgr.psendSystemMessageToRole(buy_roleTradingRecord.getRoleid(), 300003, Arrays.asList(num+""));
		}
		logger.info(buy_roleTradingRecord.getRoleid()+"买单订单号\t"+buy_roleTradingRecord.getTradingid()+"\t购买符石，购买数量"+num
				+"对应出售玩家，挂单号"+sell_roleTradingRecord.getTradingid()+"\t价格"+sell_roleTradingRecord.getPrice()+"\t总数量\t"+sell_roleTradingRecord.getAllnum()+"\t卖出数量"+num
				+"\t获得符石"+num);
		//运营日志
		OpPointGoldExBean pPointGoldExBean = new OpPointGoldExBean(SpotCheckManage.TYPE_BUY, num, sell_roleTradingRecord.getPrice(), 0, 1);
		YYLogger.pointGoldExLog(buy_roleTradingRecord.getRoleid(), pPointGoldExBean);
	}
	
	

	private Map<Integer, LinkedList<Integer>> getTrade(xbean.TradingMap tradingmap) {
		// 返回下标
		Map<Integer, LinkedList<Integer>> retMap = new HashMap<Integer, LinkedList<Integer>>();

		for (int buyIndex = 0; buyIndex < tradingmap.getBuylist().size(); ++buyIndex) {
			xbean.TradingPrice buy = tradingmap.getBuylist().get(buyIndex);
			//过滤过期不成交
			if(System.currentTimeMillis()-buy.getCreatetime()>=SpotCheckManage.getliveTime()){
				continue;
			}
			if(buy.getNum()<=0){
				continue;
			}
//			//锁住卖单和买单
//			xbean.RoleTradingRecord buy_roleTradingRecord=xtable.Roletradingrecordstab.select(buy.getTradingid());
//			if(buy_roleTradingRecord==null){
//				continue;
//			}
			for (int sellIndex = 0; sellIndex < tradingmap.getSelllist().size(); ++sellIndex) {
				xbean.TradingPrice sell = tradingmap.getSelllist().get(sellIndex);
				//过滤过期不成交
				if(System.currentTimeMillis()-sell.getCreatetime()>=SpotCheckManage.getliveTime()){
					continue;
				}
				if(sell.getNum()<=0){
					continue;
				}
//				xbean.RoleTradingRecord sell_roleTradingRecord=xtable.Roletradingrecordstab.select(sell.getTradingid());
//				if(sell_roleTradingRecord==null){
//					continue;
//				}
				if (buy.getPrice() >= sell.getPrice()) {
					LinkedList<Integer> tempList = retMap.get(buyIndex);
					if (tempList == null) {
						tempList = new LinkedList<Integer>();
						retMap.put(buyIndex, tempList);
					}
					tempList.add(sellIndex);
				} else if (buy.getPrice() < sell.getPrice()) {
					break;
				}
			}

			if (retMap.size() > 0) {
				break;
			}
		}
		return retMap;
	}
}
