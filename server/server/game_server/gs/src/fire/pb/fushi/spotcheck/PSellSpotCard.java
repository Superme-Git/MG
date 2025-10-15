package fire.pb.fushi.spotcheck;

import java.util.Map;

import mkdb.Mkdb;
import mkdb.Procedure;
import mkdb.util.AutoKey;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.SSpotCardConfig;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

public class PSellSpotCard extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	private final long roleid;
	private final int userid;
	private final int sellnum; 
	private final int sellprice; 
	
	public Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);

	public PSellSpotCard(int userid,long roleid,int sellnum,int sellprice) {
		this.roleid = roleid;
		this.userid = userid;
		this.sellnum = sellnum;
		this.sellprice = sellprice;
	}

	@Override
	protected boolean process() throws Exception {
		//不是点卡服，不能打开这个功能
		if(fire.pb.fushi.Module.GetPayServiceType() != 1){
			return false;
		}
		// 创建角色超出七天不绑定手机限制拍卖行和兑换所的使用
		if (fire.pb.tel.utils.TelBindUtils.isLimitByBindTel(roleid)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(", 交易所卖出错误！创建角色超出七天不绑定手机限制交易所");
			logger.error(sbd.toString());
			return false;
		}
		//验证客户端数据是否正确
		if(sellnum<0||sellprice<0){
			return false;
		}
		SSpotCardConfig sSpotCardConfigdata=sSpotCardConfig.get(1);
		if(sSpotCardConfigdata==null){
			return false;
		}
		//这里需要判断溢出问题，策划需要保证单价最大值和数量最大值相乘不溢出
		if(sellnum<sSpotCardConfigdata.getSellnummin()||sellnum>sSpotCardConfigdata.getSellnummax()){
			return false;
		}
		if(sellprice<sSpotCardConfigdata.getSellpricemin()||sellprice>sSpotCardConfigdata.getSellpricemax()){
			return false;
		}
		//判断挂单的总数量是否超过上限
		int max=SpotCheckManage.getRoleTradingMax();
		xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.select(roleid);
		if(roleTradingInfoList != null){
			if(roleTradingInfoList.getTradinglist().size() >= max){
				MessageMgr.sendMsgNotify(roleid, 300008, null);
				return false;
			}
		}
		
		//生成订单
		long time=System.currentTimeMillis();
		AutoKey<Long> autoKey = Mkdb.getInstance().getTables().getTableSys().getAutoKeys().getAutoKeyLong("SPOTCARD_UID");
		Long uId = autoKey.next();
		if (uId == null){
			logger.error("点卡交易，生成主键错误");
			return false;
		}
		
		//扣除符石
		if (!fire.pb.fushi.FushiManager.subFushiFromUser(userid, roleid, sellnum, 0, 0,fire.pb.fushi.FushiConst.REASON_SUB_SELL_SPOTCARD,
				YYLoggerTuJingEnum.tujing_Value_sellspotcard,
				true)) {
			return false;
		}
		
		String tradingid=roleid+"_"+sellnum+"_"+sellprice+"_"+time+"_"+uId;//生成id
		
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.TradingMap tradingmap=xtable.Tradingidtab.get(1);//最大锁,锁住全部交易
				if(tradingmap==null){
					tradingmap=xbean.Pod.newTradingMap();
					xtable.Tradingidtab.insert(1, tradingmap);
				}
				//还有没有成交的记录，挂单
				SpotCheckManage.addRoleTradingRecord(tradingid,roleid,SpotCheckManage.TYPE_SELL,sellnum,sellnum,sellprice,time);
				SpotCheckManage.addcacheAsc(tradingid,sellprice,sellnum,time,roleid,tradingmap.getSelllist());
				logger.info(roleid+"卖出，挂单号"+tradingid+"\t单价"+sellprice+"\t总数量"+sellnum+"\t实际数量"+sellnum+"消耗总符石"+sellnum);
				
				mkdb.Procedure.pexecuteWhileCommit(new PTradingSpotCardCenter());
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
		
		SSellSpotCard sSellSpotCard=new SSellSpotCard();
		psendWhileCommit(roleid, sSellSpotCard);
		return true;
	}
	



	
	
	

}
