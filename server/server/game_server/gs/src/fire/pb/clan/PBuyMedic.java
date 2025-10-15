

package fire.pb.clan;

import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.pb.clan.SClanyaofang;
import fire.pb.common.SCommon;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import mkdb.Procedure;
import xbean.ClanMemberInfo;


/**
 * 购买兑换药房的药品
 *
 */
public class PBuyMedic extends Procedure {
	public static final Logger LOG = Logger.getLogger("CLAN");
	static Map<Integer, SClanyaofang> confs = ConfigManager.getInstance().getConf(SClanyaofang.class);
	
	public static int BUY_NUM_MAX=0;//玩家每天购买的最大上限
	
	private final long roleid;
	private final int itemid;
	private final int itemnum;
	public PBuyMedic(long roleid, int itemid,int itemnum) {

		super();
		this.roleid = roleid;
		this.itemid = itemid;
		this.itemnum = itemnum;
	}
	@Override
	protected boolean process() throws Exception {

		SClanyaofang sClanyaofang = confs.get(itemid);
		if (sClanyaofang == null){
			return false;
		}
		if (itemnum <= 0){
			return false;
		}
		SCommon conf = ClanUtils.commonMap.get(205);//玩家每天购买的最大上限
		if (conf != null) {
			BUY_NUM_MAX= Integer.parseInt(conf.getValue());
		}else{
			return false;
		}
		
		xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, true);
		if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
			return false;
		}
		
		xbean.MedicItemList itemlist=xtable.Medicitemlisttab.get(clanInfo.getKey());
		
		if(itemlist==null){
			//没有可以购买的药品
			LOG.info("玩家id "+roleid+"\t"+"药房物品不存在");
			return false;
		}
		//判断是否可以购买该物品
		xbean.MedicItem medicitem=itemlist.getMedicitems().get(itemid);
		if(medicitem==null){
			LOG.info("玩家id "+roleid+"\t"+"药房物品不存在2");
			return false;
		}
		if(medicitem.getItemnum()<itemnum){
			LOG.info("玩家id "+roleid+"\t"+"药房物品数量不足");
			MessageMgr.sendMsgNotify(roleid, 160247, null);
			return false;
		}
		//判断玩家是否购买已经达到上限(必须有数量限制，要不会被刷)
		xbean.BuyMedicItemNum buyMedicItemNum=xtable.Buymedicitemnum.get(roleid);
		if(buyMedicItemNum==null){
			buyMedicItemNum=xbean.Pod.newBuyMedicItemNum();
			xtable.Buymedicitemnum.insert(roleid, buyMedicItemNum);
		}
		if(buyMedicItemNum.getBuynum()+itemnum>BUY_NUM_MAX){
			LOG.info("玩家id "+roleid+"\t"+"购买数量超出当天最大购买上限");
			MessageMgr.sendMsgNotify(roleid, 160245, null);
			return false;
		}
		
		
		
		//判断银币和帮会贡献是否够
		int costmoney=sClanyaofang.getMoney()*itemnum;
		int costcontri=sClanyaofang.getBanggong()*itemnum;
		if(costmoney<0||costcontri<0){
			return false;
		}
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
		if (bag.getMoney() < costmoney) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160246, 0, null);
			return false;
		}
		if (costmoney <=0){
			return false;
		}
		//判断帮贡是否够
		ClanMemberInfo member = clanInfo.getMembers().get(roleid);
		if(member==null){
			return false;
		}
		xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(roleid, false);
		if(clanpoint==null){
			return false;
		}
		if (clanpoint.getCurrentclanpoint() < costcontri) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160349, 0, null);
			return false;
		}
		if(costcontri<=0){
			return false;
		}
		//扣除银币和帮贡
		long rmv = bag.subMoney(-costmoney, "购买帮派药品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofanguse, -sClanyaofang.money);
		if (rmv != -costmoney){
			return false;
		}
		//提示消耗钱
		fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160375,Arrays.asList(costmoney+""));
		
		fire.pb.clan.srv.ClanManage.delContribution(roleid, costcontri, "购买帮派药品", false);//扣除帮贡
		fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160313,Arrays.asList(costcontri+""));//消耗帮贡提示
		//设置剩余物品数量
		medicitem.setItemnum(medicitem.getItemnum()-itemnum);
		//设置玩家当天购买物品数量
		buyMedicItemNum.setBuynum(buyMedicItemNum.getBuynum()+itemnum);
		//添加道具
		BagUtil.addItem(roleid, itemid, itemnum, "兑换帮派药品", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_gonghuiyaofang, itemid);
		
		SBuyMedic sBuyMedic=new SBuyMedic();
		sBuyMedic.itemid=itemid;
		sBuyMedic.itemnum=medicitem.getItemnum();
		sBuyMedic.buyitemnum=buyMedicItemNum.getBuynum();
		
		psendWhileCommit(roleid, sBuyMedic);
		LOG.info("玩家角色id "+roleid+"\t购买药房药品code "+itemid+"\t数量 "+itemnum+"\t消耗银币 "+costmoney+"\t消耗公会贡献 "+costcontri+"\t当天购买数量 "+buyMedicItemNum.getBuynum());
		//提示消息
		ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
		if(iAttr!=null){
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160374,Arrays.asList(iAttr.getName()));
		}
		
		return true;
	}
	
}

