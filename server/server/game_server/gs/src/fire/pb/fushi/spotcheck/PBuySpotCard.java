package fire.pb.fushi.spotcheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mkdb.Mkdb;
import mkdb.Procedure;
import mkdb.util.AutoKey;

import org.apache.log4j.Logger;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.fushi.SSpotCardConfig;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;

public class PBuySpotCard extends Procedure {
	public static final Logger logger = Logger.getLogger("CARD");
	
	private final long roleid;
	private final int buynum; 
	private final int buyprice; 
	
	public Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);

	public PBuySpotCard(long roleid,int buynum,int buyprice) {
		this.roleid = roleid;
		this.buynum = buynum;
		this.buyprice = buyprice;
	}

	@Override
	protected boolean process() throws Exception {
		//不是点卡服，不能打开这个功能
		if(fire.pb.fushi.Module.GetPayServiceType() != 1){
			return false;
		}
		//兑换所需要玩家进行一次充值后才开启，需要服务器添加限制
		if(!SpotCheckManage.canOpenFun(roleid)){
			MessageMgr.sendMsgNotify(roleid, 300011, null);
			return false;
		}
		// 创建角色超出七天不绑定手机限制拍卖行和兑换所的使用
		if (fire.pb.tel.utils.TelBindUtils.isLimitByBindTel(roleid)) {
			StringBuilder sbd = new StringBuilder();
			sbd.append("role=").append(roleid).append(", 交易所买入道具错误！创建角色超出七天不绑定手机限制交易所");
			logger.error(sbd.toString());
			return false;
		}
		//验证客户端数据是否正确
		if(buynum<=0||buyprice<=0){
			return false;
		}
		SSpotCardConfig sSpotCardConfigdata=sSpotCardConfig.get(1);
		if(sSpotCardConfigdata==null){
			return false;
		}
		//这里需要判断溢出问题，策划需要保证单价最大值和数量最大值相乘不溢出
		if(buynum<sSpotCardConfigdata.getBuynummin()||buynum>sSpotCardConfigdata.getBuynummax()){
			return false;
		}
//		if(buynum%10!=0){
//			return false;
//		}
		if(buyprice<sSpotCardConfigdata.getBuypricemin()||buyprice>sSpotCardConfigdata.getBuypricemax()){
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
		//判断今天玩家购买次数是否已经达到上限
		int fushidaynummax=SpotCheckManage.getRoleTradingFushiMax();
		int oldbuynum=0;//已经购买数量
		xbean.RoleUpdateModelNum roleUpdateModelNum = xtable.Roleupdatemodelnumtab.get(roleid);
		if(roleUpdateModelNum!=null){
			oldbuynum=roleUpdateModelNum.getTradingbuyfushinum();
			if(roleUpdateModelNum.getTradingbuyfushinum()>=fushidaynummax){
				MessageMgr.sendMsgNotify(roleid, 191044, null);
				return false;
			}
		}
		if(oldbuynum+buynum>fushidaynummax){
			String parans=(fushidaynummax-oldbuynum)+"";
			MessageMgr.sendMsgNotify(roleid, 191045, Arrays.asList(parans));
			return false;
		}
		
		
		//计算总的消耗，包括税
		double v=sSpotCardConfigdata.getTradingmoney()/100.0*buynum*buyprice;
		long allgold=(long) Math.ceil(buynum*buyprice+v);
		if(allgold<0){
			return false;
		}
		if(allgold>Integer.MAX_VALUE){
			return false;
		}
		//判断玩家金币是否够
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		if(bag.getGold()<allgold){
			return false;
		}
		
		long cereditPoint = bag.getCurrency(MoneyType.MoneyType_EreditPoint);	
		long costcp = (long) Math.round((allgold*fire.pb.fushi.Module.getCreditPointValue(fire.pb.fushi.Module.CREDITPOINT_OUT_GOLD2FS)));
		if (cereditPoint + costcp < 0) {
			List<String> params = new ArrayList<String>();
			params.add("" +cereditPoint);
			params.add("" +(0-(cereditPoint + costcp)));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 180021, params);	
//			MessageMgr.sendMsgNotify(roleid, 180021, null);
			MessageMgr.sendSystemMessageToRole(roleid, 180026, null);
			return false;
		}
		
		if (bag.addSysCurrency(costcp, MoneyType.MoneyType_EreditPoint, "点卡交易 加金币", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_spotcard, 0)!=costcp) 
			return false;
		
		//扣除金币
		if (bag.subGold(-allgold, "点卡交易挂单", YYLoggerTuJingEnum.tujing_Value_sellspotcard, 0) != -allgold) {
			MessageMgr.sendMsgNotify(roleid, 145005, null);
			return false;
		}
		
		//记录当天买入符石数量
		if(roleUpdateModelNum==null){
			roleUpdateModelNum=xbean.Pod.newRoleUpdateModelNum();
			xtable.Roleupdatemodelnumtab.insert(roleid, roleUpdateModelNum);
		}
		roleUpdateModelNum.setTradingbuyfushinum(roleUpdateModelNum.getTradingbuyfushinum()+buynum);
		logger.info(roleid+"买入，挂单，今天买入符石总数量"+roleUpdateModelNum.getTradingbuyfushinum());
		
		//生成订单
		long time=System.currentTimeMillis();
		
		AutoKey<Long> autoKey = Mkdb.getInstance().getTables().getTableSys().getAutoKeys().getAutoKeyLong("SPOTCARD_UID");
		Long uId = autoKey.next();
		if (uId == null){
			logger.error("点卡交易，生成主键错误");
			return false;
		}
		String tradingid=roleid+"_"+buynum+"_"+buyprice+"_"+time+"_"+uId;//生成id
		
		mkdb.Procedure proc=new mkdb.Procedure(){
			@Override
			protected boolean process() throws Exception {
				xbean.TradingMap tradingmap=xtable.Tradingidtab.get(1);//最大锁,锁住全部交易
				if(tradingmap==null){
					tradingmap=xbean.Pod.newTradingMap();
					xtable.Tradingidtab.insert(1, tradingmap);
				}
				SpotCheckManage.addRoleTradingRecord(tradingid,roleid,SpotCheckManage.TYPE_BUY,buynum,buynum,buyprice,time);
				SpotCheckManage.addcacheDesc(tradingid,buyprice,buynum,time,roleid,tradingmap.getBuylist());
				logger.info(roleid+"买入，挂单号"+tradingid+"\t单价"+buyprice+"\t总数量"+buynum+"\t挂单实际数量"+buynum+"消耗总金币"+allgold);
				mkdb.Procedure.pexecuteWhileCommit(new PTradingSpotCardCenter());
				return true;
			}
		};
		mkdb.Procedure.pexecuteWhileCommit(proc);
		SBuySpotCard sBuySpotCard=new SBuySpotCard();
		psendWhileCommit(roleid, sBuySpotCard);
		return true;
	}

}
