
package fire.pb.item;

import java.util.ArrayList;
import java.util.List;

import fire.log.beans.ItemBean;
import fire.log.beans.ResolveItemBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import fire.pb.util.Misc;
import mkdb.Procedure;

public class PResolveItem extends Procedure {
	private final long roleId;
	private final int keyinpack;
	
	private static int RESOLVE_BASE = 10000;
	
	public PResolveItem(final long roleId, final int keyinpack) {
		this.roleId = roleId;
		this.keyinpack = keyinpack;
	}
	
	@Override
	protected boolean process() throws Exception {
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		ItemBase bi = bag.getItem(keyinpack);
		if (bi == null)
			return false;
		
		//分解装备
		ResolveItemData resolveData = Module.resolveItemData.get(bi.getItemId());
		if (resolveData == null)
			return false;
		
		ResolveItemBean resolveItemLog = new ResolveItemBean();
		
		ItemBean sourceItem = new ItemBean();
		//删除原有装备
		int nMoveNum = bag.removeItemWithKey(keyinpack, 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjie, bi.getItemId(), "装备分解");
		if (nMoveNum != 1) {
			return false;
		}
		//道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(roleId,bi)) {
			return false;
		}
		
		final int ownItemNum = ItemMaps.getItemHasNum(roleId, bi.getItemId());
		sourceItem.setItemId(bi.getItemId());
		sourceItem.setAmt(nMoveNum);
		sourceItem.setAftAmt(ownItemNum);
		
		resolveItemLog.setSourceitem(sourceItem);
		
		//加钱
		bag.addMoney(resolveData.money, "装备分解", YYLoggerTuJingEnum.tujing_Value_fenjieget);
		List<String> param = new ArrayList<String>(1);
		param.add(String.valueOf(resolveData.money));
		MessageMgr.psendMsgNotifyWhileCommit(roleId, 160163, param);
		
		resolveItemLog.setMoney(resolveData.money);
		
		//获取的装备
		List<ItemBean> itemBeanLst = new ArrayList<ItemBean>();
		//把装备上的宝石添加到背包
		xbean.Equip equipAttr = ((EquipItem) bi).getEquipAttr();
		List<Integer> diamonds = equipAttr.getDiamonds();
		for (Integer gemId : diamonds) {
			ItemBean getGemItem = new ItemBean();
			bag.doAddItem(gemId, 1, "装备分解", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, gemId);
			
			getGemItem.setAmt(1);
			getGemItem.setAftAmt(ItemMaps.getItemHasNum(roleId, gemId));
			getGemItem.setItemId(gemId);
			
			itemBeanLst.add(getGemItem);
		}
		
		//加装备
		int item1Index = Misc.getProbabilityByBase(resolveData.retItemid1Rate, RESOLVE_BASE);
		if (item1Index != -1) {
			final int itemid = resolveData.retItemid1.get(item1Index);
			final int itemnum = resolveData.retItemid1Num.get(item1Index);
			
			bag.doAddItem(itemid, itemnum, "装备分解",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, itemid);
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "分解装备id:" + itemid + "找不到属性!");
				return true;
			}
			
			List<String> para = new ArrayList<String>(2);
			para.add(String.valueOf(iAttr.getName()));
			para.add(String.valueOf(itemnum));
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
			
			ItemBean getItem = new ItemBean();
			getItem.setAmt(itemnum);
			getItem.setAftAmt(ItemMaps.getItemHasNum(roleId, itemid));
			getItem.setItemId(itemid);
			itemBeanLst.add(getItem);
		}
		
		int item2Index = Misc.getProbabilityByBase(resolveData.retItemid2Rate, RESOLVE_BASE);
		if (item2Index != -1) {
			final int itemid = resolveData.retItemid2.get(item2Index);
			final int itemnum = resolveData.retItemid2Num.get(item2Index);
			
			bag.doAddItem(itemid, itemnum, "装备分解",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, itemid);
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "分解装备id:" + itemid + "找不到属性!");
				return true;
			}
			
			List<String> para = new ArrayList<String>(2);
			para.add(String.valueOf(iAttr.getName()));
			para.add(String.valueOf(itemnum));
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
			
			ItemBean getItem = new ItemBean();
			getItem.setAmt(itemnum);
			getItem.setAftAmt(ItemMaps.getItemHasNum(roleId, itemid));
			getItem.setItemId(itemid);
			itemBeanLst.add(getItem);
		}
		
		int item3Index = Misc.getProbabilityByBase(resolveData.retItemid3Rate, RESOLVE_BASE);
		if (item3Index != -1) {
			final int itemid = resolveData.retItemid3.get(item3Index);
			final int itemnum = resolveData.retItemid3Num.get(item3Index);
			
			bag.doAddItem(itemid, itemnum, "装备分解",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, itemid);
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "分解装备id:" + itemid + "找不到属性!");
				return true;
			}
			
			List<String> para = new ArrayList<String>(2);
			para.add(String.valueOf(iAttr.getName()));
			para.add(String.valueOf(itemnum));
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
			
			ItemBean getItem = new ItemBean();
			getItem.setAmt(itemnum);
			getItem.setAftAmt(ItemMaps.getItemHasNum(roleId, itemid));
			getItem.setItemId(itemid);
			itemBeanLst.add(getItem);
		}
		
		int item4Index = Misc.getProbabilityByBase(resolveData.retItemid4Rate, RESOLVE_BASE);
		if (item4Index != -1) {
			final int itemid = resolveData.retItemid4.get(item4Index);
			final int itemnum = resolveData.retItemid4Num.get(item4Index);
			
			bag.doAddItem(itemid, itemnum, "装备分解",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, itemid);
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "分解装备id:" + itemid + "找不到属性!");
				return true;
			}
			
			List<String> para = new ArrayList<String>(2);
			para.add(String.valueOf(iAttr.getName()));
			para.add(String.valueOf(itemnum));
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
			
			ItemBean getItem = new ItemBean();
			getItem.setAmt(itemnum);
			getItem.setAftAmt(ItemMaps.getItemHasNum(roleId, itemid));
			getItem.setItemId(itemid);
			itemBeanLst.add(getItem);
		}
		
		int item5Index = Misc.getProbabilityByBase(resolveData.retItemid5Rate, RESOLVE_BASE);
		if (item5Index != -1) {
			final int itemid = resolveData.retItemid5.get(item5Index);
			final int itemnum = resolveData.retItemid5Num.get(item5Index);
			
			bag.doAddItem(itemid, itemnum, "装备分解",
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_fenjieget, itemid);
			
			ItemShuXing iAttr = Module.getInstance().getItemManager().getAttr(itemid);
			if (iAttr == null) {
				Module.logger.error("角色:" + roleId + "分解装备id:" + itemid + "找不到属性!");
				return true;
			}
			
			List<String> para = new ArrayList<String>(2);
			para.add(String.valueOf(iAttr.getName()));
			para.add(String.valueOf(itemnum));
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 160164, para);
			
			ItemBean getItem = new ItemBean();
			getItem.setAmt(itemnum);
			getItem.setAftAmt(ItemMaps.getItemHasNum(roleId, itemid));
			getItem.setItemId(itemid);
			itemBeanLst.add(getItem);
		}
		
		resolveItemLog.setResultitems(itemBeanLst);
		fire.log.YYLogger.resolveItemLog(roleId, resolveItemLog);
		GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
		
		return true;
	}
	

}
