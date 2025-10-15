package fire.pb.fushi.spotcheck;

import java.util.Arrays;
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
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

public class PSysCancelTrading extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	private final long roleid;
	private final String tradingid;
	
	public Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);

	public PSysCancelTrading(long roleid,String tradingid) {
		this.roleid = roleid;
		this.tradingid = tradingid;
	}

	@Override
	protected boolean process() throws Exception {
		//不是点卡服，不能打开这个功能
		if(fire.pb.fushi.Module.GetPayServiceType() != 1){
			return false;
		}
		xbean.TradingMap tradingmap=xtable.Tradingidtab.get(1);//最大锁,锁住全部交易
		if(tradingmap==null){
			return false;
		}
		//判断订单是否存在
		xbean.RoleTradingRecord roleTradingRecord=xtable.Roletradingrecordstab.get(tradingid);
		if(roleTradingRecord==null){
			return false;
		}
		xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.get(roleid);
		if(roleTradingInfoList==null){
			return false;
		}
		//判断订单是否已经过期，或者已经交易成功了
		if(roleTradingRecord.getState()!=0){
			//提示订单已经交易成功了
			return false;
		}
		if(roleTradingRecord.getCurnum()==0){
			//提示订单已经交易成功了
			return false;
		}
		//同步内存
		SpotCheckManage.delcache(tradingid,roleTradingRecord.getTradingtype(),tradingmap);
		
		//退回逻辑处理
		xtable.Roletradingrecordstab.remove(tradingid);
		roleTradingInfoList.getTradinglist().remove(tradingid);
		if(roleTradingRecord.getTradingtype()==SpotCheckManage.TYPE_BUY){
			//买单
			//退回金币
			int addgold=roleTradingRecord.getCurnum()*roleTradingRecord.getPrice();
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			bag.addSysGold(addgold, "点卡交易 系统自动取消加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0);
			MessageMgr.psendSystemMessageToRole(roleid, 190045, Arrays.asList(addgold+""));
			bag.addSysCurrency( (long) (addgold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_IN_RETURN_GOLD2FS)),
					MoneyType.MoneyType_EreditPoint, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0); 
		}else{
			//卖单,退回符石
			int userid=xtable.Properties.selectUserid(roleid);
			Procedure.pexecuteWhileCommit(new PAddFuShi(userid, roleid,roleTradingRecord.getCurnum(),FushiConst.CASH_FUSHI, YYLoggerTuJingEnum.tujing_Value_spotcard));
			MessageMgr.psendSystemMessageToRole(roleid, 190042, Arrays.asList(roleTradingRecord.getCurnum()+""));
		}
		//运营日志
		OpPointGoldExBean pPointGoldExBean = new OpPointGoldExBean(roleTradingRecord.getTradingtype(), roleTradingRecord.getCurnum(), roleTradingRecord.getPrice(), 0, 0);
		YYLogger.pointGoldExLog(roleid, pPointGoldExBean);
		logger.info("roleid"+roleid+"\t系统自动取消挂单,挂单号"+tradingid+"类型\t"+roleTradingRecord.getTradingtype()+"\t当前数量"+roleTradingRecord.getCurnum()+
				"\t总数量"+roleTradingRecord.getAllnum()+"\t单价"+roleTradingRecord.getPrice());
		return true;
	}
	



	
	
	

}
