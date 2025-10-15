package fire.pb.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import fire.log.beans.CompareItemBean;
import fire.log.beans.ItemBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.main.ConfigManager;
import fire.pb.mission.SAddTreasureMap;
import fire.pb.product.Commontext;
import fire.pb.product.SErrorCode;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PProduceItem extends Procedure {
	static Map<Integer, SProduceItem> produceConfs = ConfigManager.getInstance().getConf(SProduceItem.class);
	
	private final long roleId;
	private final int id;
	private final int number; //使用数量
	private final int keyinpack;
	private final int npcid;
	
	/**
	 * 兑换类型 0表示单个兑换; 1表示全部兑换
	 */
	private final int producetype; 
	
	private Pack bag = null;
	
	private static Logger logger = Logger.getLogger("ITEM");
	
	public PProduceItem(final long roleId, final int id, final int number, final int keyinpack, final int producetype) {
		this.roleId = roleId;
		this.id = id;
		this.number = number;
		this.keyinpack = keyinpack;
		this.npcid = 0;
		this.producetype = producetype;
	}
	
	public PProduceItem(final long roleId, final int id, final int npcid, final int producetype) {
		this.roleId = roleId;
		this.id = id;
		this.npcid = npcid;
		this.producetype = producetype;
		this.number = 0;
		this.keyinpack = 0;
	}
	
	@Override
	protected boolean process() throws Exception {
		bag = new fire.pb.item.Pack(roleId, false);
		
		final SErrorCode sErrorCode = new SErrorCode();
		
		SProduceItem sProduceItem = produceConfs.get(id);
		if (sProduceItem == null) {
			MessageMgr.psendMsgNotify(roleId, 150167, null);
        	return false;
		}
		
		//检查材料是否足够
		List<Integer> itemList = sProduceItem.getItemList();
		List<Integer> itemNumList = sProduceItem.getItemNumList();
		List<String> params = new ArrayList<String>();
		StringBuffer strParam = new StringBuffer();
		if (number == 0) {
			int nIndex = 0;
			for (Integer itemId : itemList) {
				if(!checkMaterialIsEnough(itemId, itemNumList.get(nIndex))) {
					ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
					if (attr == null) {
						return false;
					}
					if (nIndex == itemList.size() - 1) {
						strParam.append(attr.getName());
					}
					else {
						strParam.append(attr.getName()).append(",");
					}
				}
				
				nIndex ++;
			}
		}
		else { //按堆扣物品
			int nIndex = 0;
			for (Integer itemId : itemList) {
				if(number < itemNumList.get(nIndex)) {
					ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(itemId);
					if (attr == null) {
						return false;
					}
					if (nIndex == itemList.size() - 1) {
						strParam.append(attr.getName());
					}
					else {
						strParam.append(attr.getName()).append(",");
					}
				}
				
				nIndex ++;
			}
		}
		
		if (strParam.length() > 0) {
			params.add(strParam.toString());
			if (params.size() > 0) {
				if (npcid != 0) {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, sProduceItem.itemNotEnoughMsg, npcid, params);
				}
				else {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, sProduceItem.itemNotEnoughMsg, params);
				}
				
				return false;
			}
		}
		
		//日志结构
		CompareItemBean compareItemLst = new CompareItemBean();
		
		List<ItemBean> sourceItem = new ArrayList<ItemBean>();
		
		//检查钱是否够
		int money = sProduceItem.money;		
		if (money > bag.getMoney()) {
			sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		//扣钱
		if (bag.subMoney(-money, Commontext.REASON_STRING,
				fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 0) != -money) {
			sErrorCode.errorcode = Commontext.MONEY_NOT_ENOUGH;
			Procedure.psendWhileCommit(roleId, sErrorCode);
			return true;
		}
		
		compareItemLst.setMoney(money);
		
		/**
		 * 生成物品的数量
		 */
		int addnum = 0;
		if (number == 0) {
			//扣材料
			if (producetype == 0) {
				int nIndex = 0;
				for (Integer itemId : itemList) {
					final int itemNum = itemNumList.get(nIndex);
					int removenum = bag.removeItemById(itemId, itemNum,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
							Commontext.REASON_STRING);
					if (removenum != itemNum)
						return false;
					
					logger.debug("Role: " + roleId + "背包栏删除道具: " + itemId	+ "用于" + Commontext.REASON_STRING);
					nIndex ++;
					
					ItemBean itemBean = new ItemBean();
					itemBean.setItemId(itemId);
					itemBean.setAmt(itemNum);
					itemBean.setAftAmt(ItemMaps.getItemHasNum(roleId, itemId));
					sourceItem.add(itemBean);
				}
				
				addnum = 1;
			}
			else if (producetype == 1) {
				//先找出需要物品里在背包中数量最小的
				int nIndex = 0;
				Integer minNum = Integer.MAX_VALUE;
				Integer minNeedNum = 0;
				for (Integer itemId : itemList) {
					int currentNum = bag.getItemNum(itemId, 0);
					final int itemNum = itemNumList.get(nIndex);
					if (minNum > currentNum && currentNum >= itemNum) {
						minNum = currentNum;
						minNeedNum = itemNum;
					}
					nIndex ++;
				}
				
				addnum = minNum / minNeedNum;
				logger.info(new StringBuilder().append("Role:").append(roleId).append("打造出:").append(addnum));
				
				nIndex = 0;
				for (Integer itemId : itemList) {
					final int itemNum = itemNumList.get(nIndex) * addnum; //删除打造addnum个物品需要的数量
					int removenum = bag.removeItemById(itemId, itemNum,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
							Commontext.REASON_STRING);
					if (removenum != itemNum)
						return false;
					
					logger.debug("Role: " + roleId + "背包栏删除道具: " + itemId	+ "用于" + Commontext.REASON_STRING);
					nIndex ++;
					
					ItemBean itemBean = new ItemBean();
					itemBean.setItemId(itemId);
					itemBean.setAmt(itemNum);
					itemBean.setAftAmt(ItemMaps.getItemHasNum(roleId, itemId));
					sourceItem.add(itemBean);
				}
			}
		}
		else {
			//扣材料
			for (Integer itemId : itemList) {
				int removenum = bag.removeItemWithKey(keyinpack, number,
						fire.log.enums.YYLoggerTuJingEnum.tujing_Value_dazao, 5,
						Commontext.REASON_STRING);
				if (removenum != number)
					return false;
				
				logger.debug("Role: " + roleId + "背包栏删除道具: " + itemId	+ "用于" + Commontext.REASON_STRING);
				
				ItemBean itemBean = new ItemBean();
				itemBean.setItemId(itemId);
				itemBean.setAmt(number);
				itemBean.setAftAmt(ItemMaps.getItemHasNum(roleId, itemId));
				sourceItem.add(itemBean);
			}
		}
		
		//生成物品
		if (producetype == 0) {
			int nItemIndex = Misc.getProbability(sProduceItem.resultItemRate);
			List<String> param = new ArrayList<String>();
			if (nItemIndex < sProduceItem.resultItem.size()) {
				int itemId = sProduceItem.resultItem.get(nItemIndex);
				ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemId);
				if (iAttr == null) {
					Module.logger.error("角色:" + roleId + "生产宝图的id:" + sProduceItem.getResultItem() + "找不到属性!");
					return true;
				}
				
				if (BagUtil.addItem(roleId, itemId, 1, "兑换物品", YYLoggerTuJingEnum.tujing_Value_daozaoget, itemId)	!= 1){
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 142338, null);
					return false;
				}
				if (itemId == 331301) {
					SAddTreasureMap addTreasureMap = new SAddTreasureMap();
					mkdb.Procedure.psendWhileCommit(roleId, addTreasureMap);
					param.add(String.valueOf(addnum));
				}
				else {
					param.add(iAttr.getName());
				}
				
				logger.debug("Role: " + roleId + "背包栏增加装备:" + sProduceItem.resultItem + "通过"	+ Commontext.REASON_STRING);
				
				ItemBean getItemBean = new ItemBean();
				getItemBean.setItemId(itemId);
				getItemBean.setAmt(1);
				getItemBean.setAftAmt(ItemMaps.getItemHasNum(roleId, itemId));
				
				compareItemLst.setSourceitems(sourceItem);
				compareItemLst.setResultitem(getItemBean);
				
				fire.log.YYLogger.compareItemLog(roleId, compareItemLst);
			}
			if (npcid != 0) {
				MessageMgr.psendMsgNotify(roleId, sProduceItem.notice, npcid, param);
			}
			else {
				MessageMgr.psendMsgNotify(roleId, sProduceItem.notice, param);
			}
		}
		else if (producetype == 1 && id == 1) { //只有兑换高级藏宝图才会做此特殊处理
			int nItemIndex = Misc.getProbability(sProduceItem.resultItemRate);
			List<String> param = new ArrayList<String>();
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(331301);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "生产宝图的id:" + sProduceItem.getResultItem() + "找不到属性!");
				return true;
			}
			
			if (nItemIndex < sProduceItem.resultItem.size()) {
				int itemId = sProduceItem.resultItem.get(nItemIndex);
				
				if (BagUtil.addItem(roleId, itemId, addnum, "兑换物品", YYLoggerTuJingEnum.tujing_Value_daozaoget, itemId)	!= addnum){
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 142338, null);
					return false;
				}
				
				SAddTreasureMap addTreasureMap = new SAddTreasureMap();
				mkdb.Procedure.psendWhileCommit(roleId, addTreasureMap);
				
				logger.debug("Role: " + roleId + "背包栏增加装备:" + sProduceItem.resultItem + "通过"	+ Commontext.REASON_STRING);
				
//				param.add(iAttr.getName());
				param.add(String.valueOf(addnum));
				
				ItemBean getItemBean = new ItemBean();
				getItemBean.setItemId(itemId);
				getItemBean.setAmt(1);
				getItemBean.setAftAmt(ItemMaps.getItemHasNum(roleId, itemId));
				
				compareItemLst.setSourceitems(sourceItem);
				compareItemLst.setResultitem(getItemBean);
				
				fire.log.YYLogger.compareItemLog(roleId, compareItemLst);
				
				MessageMgr.psendMsgNotify(roleId, sProduceItem.notice, npcid, param);
			}
		}
		
		return true;
	}
	
	
	private boolean checkMaterialIsEnough(int materialID, int num) {
		int currentNum = bag.getItemNum(materialID, 0);
		if (currentNum >= num) {
			return true;
		}
		return false;
	}
	
	
//	private fire.pb.item.ItemBase getItemKey(int itemId) {
//		for (fire.pb.item.ItemBase item : bag) {
//			if (itemId == item.getItemId()){
//				return item;
//			}
//		}
//		return null;
//	}
}
