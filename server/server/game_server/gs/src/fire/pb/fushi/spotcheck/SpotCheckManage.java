package fire.pb.fushi.spotcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import mkdb.Procedure;
import mkdb.logs.LogList;

import org.apache.log4j.Logger;

import fire.pb.common.SCommon;
import fire.pb.fushi.SSpotCardConfig;
import fire.pb.main.ConfigManager;
import fire.pb.main.Gs;
import fire.pb.util.DateUtil;
import gnet.link.Onlines;



public class SpotCheckManage {
	public static final Logger logger = Logger.getLogger("CARD");
	private static SpotCheckManage instance = null;
	private SpotCheckManage() {

	}
	public static SpotCheckManage getInstance() {
		if(instance==null){
			instance=new SpotCheckManage();
		}
		return instance;
	}
	
	//0买入  1出售
	public static final int TYPE_SELL=1;
	public static final int TYPE_BUY=0;
	
	public static final int MAX_RECORD=5;//最大记录数
	public static final int MAX_HIS_RECORD=100;//最大记录数玩家交易记录
	
	public static Map<Integer, SSpotCardConfig> sSpotCardConfig = ConfigManager.getInstance().getConf(SSpotCardConfig.class);
	
	
	public static long time=0;
	
	/**
	 * 订单存在时间
	 * @return
	 */
	public static long getliveTime(){
		SSpotCardConfig sSpotCardConfigdata=sSpotCardConfig.get(1);
		if(sSpotCardConfigdata==null){
			return DateUtil.ONE_DAY;
		}
		return sSpotCardConfigdata.getLivetime()*60*1000;
	}
	
	public static void init(xbean.TradingMap tradingmap,xbean.RoleTradingRecord record){
		if(record.getTradingtype()==TYPE_BUY){
			SpotCheckManage.addcacheDesc(record.getTradingid(),record.getPrice(),record.getCurnum(),record.getCreatetime(),record.getRoleid(),tradingmap.getBuylist());
		}else{
			SpotCheckManage.addcacheAsc(record.getTradingid(),record.getPrice(),record.getCurnum(),record.getCreatetime(),record.getRoleid(),tradingmap.getSelllist());
		}
	}
	
	/**
	 * 是否可以打开交易所功能
	 * @param roleid
	 * @return
	 */
	public static boolean canOpenFun(long roleid){
		Integer userid=xtable.Properties.selectUserid(roleid);
		if(userid==null){
			return false;
		}
		if(fire.pb.fushi.Module.getIsYYBUser(userid)){
			xbean.YybFushiNums yybFushiNums=xtable.Yybfushi.select(userid);
			if(yybFushiNums==null){
				return false;
			}
			xbean.YybFushiNum yybFushiNum = yybFushiNums.getRolefushi().get(roleid);
			if(yybFushiNum==null){
				return false;
			}
			//处理一下老号的问题，老号如果充值记录有值就算
			if(yybFushiNum.getSaveamt()>0){
				return true;
			}
			if(yybFushiNum.getFushiall()<=0){
				return false;
			}
			
		}else{
			xbean.YbNums ybnums = xtable.Fushinum.select(userid);
			if(ybnums==null){
				return false;
			}
			xbean.YbNum ybNum = ybnums.getRoleyb().get(roleid);
			if(ybNum==null){
				return false;
			}
			//处理一下老号的问题，老号如果充值记录有值就算
			if(ybNum.getNopresentnum()>0){
				return true;
			}
			if(ybNum.getFushiall()<=0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 登陆时候发生开关
	 * @return
	 */
	public static mkdb.Procedure sendTradingOpenStateLogin(long roleid){
		return new mkdb.Procedure(){
			@Override
			protected boolean process(){
				STradingOpenState sTradingOpenState = new STradingOpenState();
				if(canOpenFun(roleid)){
					//可以打开界面
					sTradingOpenState.openstate=1;
				}else{
					sTradingOpenState.openstate=0;
				}
				Procedure.psendWhileCommit(roleid, sTradingOpenState);
				return true;
			}
			
		};
	}
	
	
	/**
	 * 刷新状态
	 * @param roleid
	 * @param num
	 */
	public static void refreshTradingOpenState(long roleid,long oldnum, long num) {
		if(oldnum!=0){
			return ;
		}
		STradingOpenState sTradingOpenState = new STradingOpenState();
		if(num>0){
			sTradingOpenState.openstate=1;
		}else{
			sTradingOpenState.openstate=0;
		}
		if (mkdb.Transaction.current() != null)
			Procedure.psendWhileCommit(roleid, sTradingOpenState);
		else{
			Onlines.getInstance().send(roleid, sTradingOpenState);
		}
	}
	
	
	/**
	 * 获得玩家交易的最大数量
	 * @return
	 */
	public static int getRoleTradingMax(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(352);
		if(conf==null){
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	/**
	 * 获得玩家每天最大购买符石数量
	 * @return
	 */
	public static int getRoleTradingFushiMax(){
		SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(467);
		if(conf==null){
			return 0;
		}
		return Integer.parseInt(conf.getValue());
	}
	
	
	/**
	 * 添加内存(升序)
	 * @param tradingid
	 * @param price
	 * @param lastnum
	 * @param list
	 */
	public static void addcacheAsc(String tradingid,int price,int lastnum,long createtime,long roleid,java.util.List<xbean.TradingPrice> list){
		xbean.TradingPrice tradingprice=xbean.Pod.newTradingPrice();
		tradingprice.setTradingid(tradingid);
		tradingprice.setPrice(price);
		tradingprice.setNum(lastnum);
		tradingprice.setCreatetime(createtime);
		tradingprice.setRoleid(roleid);
		// 再添加
		boolean isAdd = false;
		ListIterator<xbean.TradingPrice> listIt = ((LogList<xbean.TradingPrice>) (list)).listIterator();
		for (; listIt.hasNext();) {
			xbean.TradingPrice tradingPrice = listIt.next();
			if (price < tradingPrice.getPrice()) {
				listIt.previous();
				listIt.add(tradingprice);
				isAdd = true;
				break;
			}
		}
		if(isAdd == false) {
			list.add(tradingprice);
		}
	}
	
	/**
	 * 添加内存(升序)
	 * @param tradingid
	 * @param price
	 * @param lastnum
	 * @param list
	 */
	public static void addcacheDesc(String tradingid,int price,int lastnum,long createtime,long roleid,java.util.List<xbean.TradingPrice> list){
		xbean.TradingPrice tradingprice=xbean.Pod.newTradingPrice();
		tradingprice.setTradingid(tradingid);
		tradingprice.setPrice(price);
		tradingprice.setNum(lastnum);
		tradingprice.setCreatetime(createtime);
		tradingprice.setRoleid(roleid);
		// 再添加
		boolean isAdd = false;
		ListIterator<xbean.TradingPrice> listIt = ((LogList<xbean.TradingPrice>) (list)).listIterator();
		for (; listIt.hasNext();) {
			xbean.TradingPrice tradingPrice = listIt.next();
			if (price > tradingPrice.getPrice()) {
				listIt.previous();
				listIt.add(tradingprice);
				isAdd = true;
				break;
			}
		}
		if(isAdd == false) {
			list.add(tradingprice);
		}
	}
	
	/**
	 * 移除内存同步
	 * @param tradingid
	 * @param type
	 * @param tradingmap
	 */
	public static void delcache(String tradingid,int type,xbean.TradingMap tradingmap){
		xbean.TradingPrice tradingPrice=null;
		if(type == TYPE_BUY){
			for(xbean.TradingPrice value:tradingmap.getBuylist()){
				if(value.getTradingid().equals(tradingid)){
					tradingPrice=value;
					break;
				}
			}
			if(tradingPrice!=null){
				tradingmap.getBuylist().remove(tradingPrice);
			}
		}else{
			for(xbean.TradingPrice value:tradingmap.getSelllist()){
				if(value.getTradingid().equals(tradingid)){
					tradingPrice=value;
					break;
				}
			}
			if(tradingPrice!=null){
				tradingmap.getSelllist().remove(tradingPrice);
			}
		}
	}
	
	
	
	/**
	 * 创建记录
	 * @param tradingid
	 * @param type
	 * @param num
	 * @param allnum
	 * @param price
	 * @param time
	 * @return
	 */
	public static xbean.RoleTradingHisRecord createRoleTradingHisRecord(String tradingid,int type,int num,int allnum,int price,long time){
		xbean.RoleTradingHisRecord sellroleTradingHisRecord=xbean.Pod.newRoleTradingHisRecord();
		sellroleTradingHisRecord.setTradingid(tradingid); // 挂单号
		sellroleTradingHisRecord.setTradingtype(type); // 交易类型  0买入  1出售
		sellroleTradingHisRecord.setCurnum(num);
		sellroleTradingHisRecord.setAllnum(allnum); // 总数量
		sellroleTradingHisRecord.setPrice(price); // 价格
		sellroleTradingHisRecord.setCreatetime(time); // 挂单时间
		sellroleTradingHisRecord.setTradingtime(System.currentTimeMillis()); // 交易时间
		return sellroleTradingHisRecord;
	}
	
	/**
	 * 存储挂单信息
	 * @param tradingid
	 * @param roleid
	 * @param type
	 * @param curnum
	 * @param allnum
	 * @param price
	 * @param time
	 */
	public static void addRoleTradingRecord(String tradingid,long roleid,int type,int curnum,int allnum,int price,long time){
		xbean.RoleTradingRecord roleTradingRecord=xtable.Roletradingrecordstab.get(tradingid);
		if(roleTradingRecord==null){
			roleTradingRecord=xbean.Pod.newRoleTradingRecord();
			xtable.Roletradingrecordstab.insert(tradingid, roleTradingRecord);
		}
		roleTradingRecord.setTradingid(tradingid); // 挂单号
		roleTradingRecord.setRoleid(roleid); // 角色id
		roleTradingRecord.setTradingtype(type); // 交易类型  0买入  1出售
		roleTradingRecord.setCurnum(curnum); // 当前数量
		roleTradingRecord.setAllnum(allnum); // 总数量
		roleTradingRecord.setPrice(price); // 价格
		roleTradingRecord.setCreatetime(time); // 挂单时间
		roleTradingRecord.setTradingtime(0); // 交易时间
		roleTradingRecord.setState(0); // 状态    1表示已经退回或者交易完
		
		//添加玩家挂单映射
		xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.get(roleid);
		if(roleTradingInfoList==null){
			roleTradingInfoList=xbean.Pod.newRoleTradingInfoList();
			xtable.Roletradinginfolisttab.insert(roleid, roleTradingInfoList);
		}
		roleTradingInfoList.getTradinglist().put(tradingid, tradingid);
		//刷新目标界面
		refreshAllView(roleid);
		
		
	}
	
	
	/**
	 * 刷新玩家交易信息
	 * @param roleid
	 */
	public static void refreshRoleTradingView(long roleid){
		SRoleTradingView sRoleTradingView=new SRoleTradingView();
		xbean.RoleTradingInfoList roleTradingInfoList= xtable.Roletradinginfolisttab.select(roleid);
		if(roleTradingInfoList==null){
			Procedure.psendWhileCommit(roleid, sRoleTradingView);
			return;
		}
		List<RoleTradingRecord> buylist=new ArrayList<RoleTradingRecord>();
		List<RoleTradingRecord> selllist=new ArrayList<RoleTradingRecord>();
		for(String tradingid:roleTradingInfoList.getTradinglist().values()){
			xbean.RoleTradingRecord roleTradingRecord=xtable.Roletradingrecordstab.select(tradingid);
			if(roleTradingRecord!=null){
				if(roleTradingRecord.getCurnum()>0&&roleTradingRecord.getState()==0){
					//过滤过期的订单
					if(System.currentTimeMillis()-roleTradingRecord.getCreatetime()>=SpotCheckManage.getliveTime()){
						continue;
					}
					RoleTradingRecord record=new RoleTradingRecord(roleTradingRecord.getTradingid(), roleTradingRecord.getTradingtype(), roleTradingRecord.getCurnum(), roleTradingRecord.getPrice(), roleTradingRecord.getCreatetime());
					if(roleTradingRecord.getTradingtype()==SpotCheckManage.TYPE_BUY){
						buylist.add(record);
					}else{
						selllist.add(record);
					}
				}
			}
		}
		//根据时间排序
		Collections.sort(buylist,new RoleTradingRecord2Comparator());
		Collections.sort(selllist,new RoleTradingRecord2Comparator());
		
		sRoleTradingView.buyspotcardinfolist.addAll(buylist);
		sRoleTradingView.sellspotcardinfolist.addAll(selllist);
		Procedure.psendWhileCommit(roleid, sRoleTradingView);
	}
	
	
	/**
	 * 刷新交易区界面
	 * @param roleid
	 */
	public static void refreshTradingSpotCardView(long roleid){
		xbean.TradingMap tradingmap=xtable.Tradingidtab.select(1);//最大锁,锁住全部交易
		if(tradingmap==null){
			return;
		}
		STradingSpotCardView sTradingSpotCardView=new STradingSpotCardView();
		
		List<fire.pb.fushi.spotcheck.SpotCardInfo> selllist= new ArrayList<fire.pb.fushi.spotcheck.SpotCardInfo>();
		
		fullData(sTradingSpotCardView.buyspotcardinfolist, tradingmap.getBuylist());
		fullData(selllist, tradingmap.getSelllist());
		
		//出售列表取反
		for(int i=selllist.size()-1;i>=0;i--){
			sTradingSpotCardView.sellspotcardinfolist.add(selllist.get(i));
		}
		
		Procedure.psendWhileCommit(roleid, sTradingSpotCardView);
	}
	
	/**
	 * 填充数据
	 * @param out
	 * @param in
	 */
	public static void fullData(List<SpotCardInfo> out, List<xbean.TradingPrice> in) {
		SpotCardInfo prev = null;
		for(xbean.TradingPrice current :in){
			//过滤一下过期的
			if(System.currentTimeMillis()-current.getCreatetime()>=SpotCheckManage.getliveTime()){
				continue;
			}
			if (prev != null && prev.price == current.getPrice()) {
				prev.num += current.getNum();
			} else {
				if (out.size() >= MAX_RECORD) {
					break;
				}
				prev = new SpotCardInfo();
				prev.price = current.getPrice();
				prev.num = current.getNum();
				out.add(prev);
			}
		}
	}
	
	/**
	 * 个人交易记录界面
	 * @param roleid
	 * @return
	 */
	public static void refreshRoleTradingRecordView(long roleid){
		SRoleTradingRecordView sRoleTradingRecordView=new SRoleTradingRecordView();
		xbean.RoleTradingHisRecordList buyroleTradingHisRecordList=xtable.Roletradinghisrecordlisttab.select(roleid);
		if(buyroleTradingHisRecordList==null){
			Procedure.psendWhileCommit(roleid, sRoleTradingRecordView);
			return ;
		}
		List<xbean.RoleTradingHisRecord> list=buyroleTradingHisRecordList.getRoletradinghisrecordlistAsData();
		//排序
		Collections.sort(list,new RoleTradingHisRecordComparator());
		int index=0;
		for(xbean.RoleTradingHisRecord value:list){
			index++;
			if(index >= MAX_HIS_RECORD){
				break;
			}
			RoleTradingRecord record=new RoleTradingRecord(value.getTradingid(), value.getTradingtype(), value.getCurnum(), value.getPrice(), value.getTradingtime());
			sRoleTradingRecordView.roletradingrecordlist.add(record);
		}
		Procedure.psendWhileCommit(roleid, sRoleTradingRecordView);
	}
	
	
	/**
	 * 刷新指定玩家的3个点卡界面
	 * @param roleid
	 */
	public static void refreshAllView(long roleid){
		mkdb.Procedure.pexecuteWhileCommit(new PTradingSpotCardView(roleid));
		mkdb.Procedure.pexecuteWhileCommit(new PRoleTradingView(roleid));
		mkdb.Procedure.pexecuteWhileCommit(new PRoleTradingRecordView(roleid));
	}
	
	
	
	
	
	
}
