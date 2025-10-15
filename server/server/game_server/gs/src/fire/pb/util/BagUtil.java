

package fire.pb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.item.AddItemResult;
import fire.pb.item.BagTypes;
import fire.pb.item.DPGiveItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.ItemShuXing;
import fire.pb.item.Module;
import fire.pb.item.Pack;
import fire.pb.item.SDirectUseItem;
import fire.pb.main.ConfigManager;


public class BagUtil {
	public static int addItem(long roleid, int itemid, int num, String reason,YYLoggerTuJingEnum countertype, int xiangguanid) {
		NavigableMap<Integer, SDirectUseItem> sDircUseItem = ConfigManager.getInstance().getConf(SDirectUseItem.class);
		SDirectUseItem sUseItem = sDircUseItem.get(itemid);
		if (sUseItem == null) {
			return addItem(roleid, itemid, num, reason, countertype, xiangguanid, true);
		} 
		else {
			DPGiveItem giveItem = new DPGiveItem(roleid, itemid, num);
			if (mkdb.Transaction.current() != null) {
				if (giveItem.call())
					return num;
				else
					return 0;
			}
			else {
				giveItem.submit();
			}
			
			return num;
		}
	}
	
	public static int addItem(long roleid, int itemid, int num, String reason,YYLoggerTuJingEnum countertype, int xiangguanid, boolean addtotempwhenfull) {
        if (itemid<=0||num<=0)
        	return 0;
		fire.pb.item.ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.BAG, false);
		fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);

		int realAdd = ((Pack)bag).doAddItem(itemid, num, reason, countertype, xiangguanid);
		if (realAdd != num) {
			if (realAdd < 0 || realAdd > num)
				throw new RuntimeException("add more items!itemid:" + itemid);
			int tempAdd = 0;
			if (bag.isFull() && addtotempwhenfull)
				tempAdd = tempBag.doAddItem(itemid, num - realAdd, reason, countertype, xiangguanid );
			return realAdd + tempAdd;
		}
		return num;
	}
	public static int addItem(final long roleid, final int itemid, int num, boolean playCG, String reason, YYLoggerTuJingEnum countertype, int xiangguanid ) {
		if (itemid<=0||num<=0)
        	return 0;
		if (playCG)
			return addItem(roleid, itemid, num, reason, countertype, xiangguanid);
		else {
			fire.pb.item.Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.BAG, false);
			fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
			final int realAdd = bag.doAddItem(itemid, num, reason, countertype, xiangguanid);
			// 如果没有完全加上,可能是背包满了.需要放到临时背包中
			if (realAdd != num) {
				if (realAdd < 0 || realAdd > num)
					throw new RuntimeException("add more items!itemid:" + itemid);
				int tempAdd = 0;
				if (bag.isFull())
					tempAdd = tempBag.doAddItem(itemid, num - realAdd, reason, countertype, xiangguanid );
				return realAdd + tempAdd;
			}
			return num;
		}
	}
	
	public static int addBindItem(long roleid, int itemid,int num, String reason, YYLoggerTuJingEnum countertype, int xiangguanid,boolean temp ){
		NavigableMap<Integer, SDirectUseItem> sDircUseItem = ConfigManager.getInstance().getConf(SDirectUseItem.class);
		SDirectUseItem sUseItem = sDircUseItem.get(itemid);
		if (sUseItem != null) {
			DPGiveItem giveItem = new DPGiveItem(roleid, itemid, num);
			if (mkdb.Transaction.current() != null) {
				giveItem.call();
			}
			else {
				giveItem.submit();
			}
			
			return num;
		} 
				
		if (itemid <= 0 || num <= 0)
			return 0;
		ItemMgr im = Module.getInstance().getItemManager();
		if ( im == null )
			return 0;
		ItemShuXing attr = im.getAttr( itemid );
		if ( attr.maxstack <= 0 )
			return 0;
		final int maxown = fire.pb.item.Module.getInstance().getMaxOwn(roleid, itemid);
		int leftnum;
		if (maxown > 0) {
			final int hasnum = fire.pb.item.ItemMaps.getItemHasNum(roleid, itemid);
			if (maxown <= hasnum) {
				List<String> parameters = new ArrayList<String>();
				parameters.add(attr.name);
				fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142752, parameters);
				return 0;
			} else {
				leftnum = Math.min(maxown-hasnum, num);
				if (leftnum != num) {
					List<String> parameters = new ArrayList<String>();
					parameters.add(attr.name);
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 142752, parameters);
				}
			}
		} else {
			leftnum = num;
		}
		int added = 0;
		int loopNum = 1000;
		while (leftnum > 0 && loopNum > 0) {
			int gennum = Math.min(leftnum, attr.maxstack);
			loopNum--;
			ItemBase item = Module.getInstance().getItemManager().genItemBase(itemid, gennum);
			item.setFlag(item.getFlags() | fire.pb.Item.BIND);
			fire.pb.item.ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.BAG, false);
			boolean addSucc=bag.doAddItem(item, -1, reason, countertype, xiangguanid) == AddItemResult.SUCC;
			if (addSucc){
				leftnum-=gennum;
				added += gennum;
			}
			else {//可能是背包满了,试一下临时背包
				if (!temp){
					fire.pb.talk.MessageMgr.psendMsgNotify(roleid, 144949, null);
					throw new RuntimeException("玩家背包栏已满,roleid:"+roleid+"itemid:"+itemid+"reason:"+reason);
				}else {
					leftnum-=gennum;
				}
			  if (bag.isFull()&&temp){
				 fire.pb.item.ItemMaps tempBag = fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.TEMP, false);
				 if (tempBag.doAddItem(item, -1, reason, countertype, xiangguanid) == AddItemResult.SUCC){
					 leftnum-=gennum;
					 added += gennum;
				 }else
					 return added;
			  }else {
				 return added;
			  }
			}
			
		}
		
		return added;
	}
	
    public static int removeItem(final long roleid, final int itemid, int num,YYLoggerTuJingEnum countertype,int xiangguanid,String reason){
    	fire.pb.item.Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(roleid, BagTypes.BAG, false);
    	if (bag.getItemNumExpStall(itemid)<num){
    		num = bag.getItemNumExpStall(itemid);
    	}
    	return bag.removeItemById(itemid, num, countertype, xiangguanid, reason);
    	
    }

    public static int removeItemBindFirst(final long roleid, final int itemid, int num,YYLoggerTuJingEnum countertype,int xiangguanid,String reason){
    	
    	fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
    	List<Integer> bindItemKeys = new ArrayList<Integer>();
    	for (ItemBase bi : bag) {
			if (bi.getItemId() == itemid && bi.isBind()) {
				bindItemKeys.add(bi.getKey());
			}
		}
    	int realRemove = 0;
    	if (!bindItemKeys.isEmpty()) {//优先扣绑定的
    		for (Integer bindItemKey : bindItemKeys) {
				ItemBase bi = bag.getItem(bindItemKey);
				if (bi.getNumber() < num){
					int rmv = bag.removeItemWithKey(bindItemKey, bi.getNumber(), countertype, xiangguanid, reason);
					realRemove+=rmv;
					num-=rmv;
				}else {
					int rmv = bag.removeItemWithKey(bindItemKey, num, countertype, xiangguanid, reason);
					realRemove+=rmv;
					num-=rmv;
					break;
				}
			}
    	}
    	if (num>0){//如果绑定的不够,再扣非绑定的
    		int rmv = bag.removeItemById(itemid, num, countertype, xiangguanid, reason);
    		realRemove+=rmv;
    		num-=rmv;
    	}
    	
    	return realRemove;
    }
	
}
