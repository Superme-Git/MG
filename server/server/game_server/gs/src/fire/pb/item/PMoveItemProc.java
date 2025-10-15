
package fire.pb.item;

import java.util.HashMap;
import java.util.Map;

public class PMoveItemProc extends mkdb.Procedure{

	private final long roleId;    //角色id
	private final int srckey;     //源物品key
	private final int number;     //数量
	private final int srcpackid;   //源背包id
	private final int dstpackid;   //目标背包id
	private final int dstpos;     //目标位置
	private final int page;       //目标页
	
	public PMoveItemProc(long roleId, int srckey, int number, int srcpackid,
			int dstpackid, int dstpos, int page) {
		super();
		this.roleId = roleId;
		this.srckey = srckey;
		this.number = number;
		this.srcpackid = srcpackid;
		this.dstpackid = dstpackid;
		this.dstpos = dstpos;
		this.page = page;
	}

	private boolean moveItemEmptyPos(final ItemMaps src, final int pos, final boolean samebag) {
		ItemBase srcitem = src.getItem(srckey);
		if (srcitem == null)
			return false;
		if (number == srcitem.getNumber() || number == -1) {
			if (samebag) {
				srcitem.getDataItem().setPosition(pos);
				SItemPosChange send = new SItemPosChange();
				send.packid = srcpackid;
				send.keyinpack = srcitem.getKey();
				send.pos = pos;
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			} else {
				return moveItemBetweenBags(src, number, pos);
			}
		} else if (number > srcitem.getNumber()) {
			return false;
		} else {
			if (samebag) {
				xbean.Item removed = srcitem.itemData.copy();
				removed.setUniqueid(0);
				removed.setNumber(0);
				removed.getNumbermap().clear();
				Map<Integer, Integer> splitnum = ItemMaps.doCut(srcitem.itemData, number);
				SItemNumChange mod = new SItemNumChange();
				mod.packid = srcpackid;
				mod.curnum = ItemMaps.getItemNum(srcitem.itemData);
				mod.keyinpack = srckey;
				mkdb.Procedure.psendWhileCommit(roleId, mod);
				ItemMaps.stackNumber(splitnum, removed);
				removed.setPosition(pos);
				int addkey = src.doPushItem(removed);
				if (addkey == 0)
					return false;
				SAddItem add = new SAddItem();
				add.packid = src.getPackid();
				add.data.add(ItemMaps.xItem2Item(removed, addkey, 0));
				mkdb.Procedure.psendWhileCommit(roleId, add);
				return true;
			} else {
				return moveItemBetweenBags(src, number, pos);
			}
		}
	}
	
	private int getFirstFreePos(ItemMaps srcbag, ItemMaps dstbag, int page) {
		final int startpos = (page - 1) * Commontext.BAG_PAGE_SIZE;
		final int endpos = page * Commontext.BAG_PAGE_SIZE - 1;
		
		int fpos = Integer.MAX_VALUE;
		
		ItemBase bi = srcbag.getItem(srckey);
		
		for (int i = startpos; i < endpos + 1; i ++) {
			ItemBase pitem = dstbag.getItemByPos(i);
			if (pitem != null) {
				if (pitem.getItemAttr().getId() == bi.getItemAttr().getId()) {
					if (!ItemMaps.isStack(bi.itemData, pitem.itemData, 0)) {
						continue;
					}
					
					int maxpilenum = pitem.getItemAttr().maxstack - pitem.getNumber();
					if (maxpilenum < bi.getNumber()) {
						continue;
					}
					else {
						fpos = i;
						break;
					}
				}
			}
		}
		
		if (fpos == Integer.MAX_VALUE) {
			java.util.List<Integer> freeposes = dstbag.getFreepos();
			if (freeposes.isEmpty())
				return -1;
			
			for (int pos : freeposes) {
				if (pos >= startpos && pos <= endpos && fpos > pos)
					fpos = pos;
			}
		}
		
		if (fpos == Integer.MAX_VALUE)
			return -1;
		return fpos;
	}
	
	
	private boolean swapItem(final ItemMaps src, final ItemBase srcitem, final ItemBase dstitem) {
		if (srcpackid == dstpackid) {
			int temppos = srcitem.getPosition();
			srcitem.itemData.setPosition(dstitem.getPosition());
			dstitem.itemData.setPosition(temppos);
			SItemPosChange modpos1 = new SItemPosChange();
			modpos1.packid = srcitem.getPackId();
			modpos1.keyinpack = srcitem.getKey();
			modpos1.pos = srcitem.getPosition();
			mkdb.Procedure.psendWhileCommit(roleId, modpos1);
			SItemPosChange modpos2 = new SItemPosChange();
			modpos2.packid = dstitem.getPackId();
			modpos2.keyinpack = dstitem.getKey();
			modpos2.pos = dstitem.getPosition();
			mkdb.Procedure.psendWhileCommit(roleId, modpos2);
			return true;
		} else {
			final ItemMaps dst = Module.getInstance().getItemMaps(roleId, dstpackid, false);
			ItemBase movesrcitem = src.TransOut(srckey, -1, "移动物品");
			ItemBase movedstitem = dst.TransOut(dstitem.getKey(), -1, "移动物品");
			return src.TransIn(movedstitem, srcitem.getPosition())
				&& dst.TransIn(movesrcitem, dstpos);
		}
	}
	
	private boolean moveItemBetweenBags(final ItemMaps src, final int srcitemnum, final int dstpos) {
		ItemBase moveitem = src.TransOut(srckey, number, "移动物品");
		if (moveitem == null)
			return false;
		
		ItemMaps dst = Module.getInstance().getItemMaps(roleId, dstpackid, false);
		ItemBase dstitem = dst.getItemByPos(dstpos);
		if (dstitem == null) {
			if (!dst.TransIn(moveitem, dstpos)) {
				if (dstpackid == BagTypes.BAG ) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145950, null);
				}
				if(dstpackid == BagTypes.DEPOT){
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145946, null);
				}
				return false;
			}
		}
		else {
			if (!dst.TransIn(moveitem, dstpos, dstitem)) {
				if (dstpackid == BagTypes.BAG ) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145950, null);
				}
				if(dstpackid == BagTypes.DEPOT){
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145946, null);
				}
				return false;
			}
		}
		
		return true;
	}
	
	private boolean moveAutoPos() {
		final ItemMaps src = Module.getInstance().getItemMaps(roleId, srcpackid, false);
		if (page > 0) {
			final ItemMaps dst;
			if (srcpackid == dstpackid) {
				dst = src;
			} else {
				dst = Module.getInstance().getItemMaps(roleId, dstpackid, false);
			}
			int freepos = getFirstFreePos(src, dst, page);
			if (freepos == -1) {
				if (dstpackid == BagTypes.BAG ) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145950, null);
				}
				if (dstpackid == BagTypes.DEPOT) {
					fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145946, null);
				}
				return false;
			}
			return moveItemEmptyPos(src, freepos, srcpackid == dstpackid);
		} else {
			if (srcpackid == dstpackid)
				return false;
			else {
				ItemBase bi = src.getItem(srckey);
				if (bi == null)
					return false;
				return moveItemBetweenBags(src, bi.getNumber(), dstpos);
			}
		}
	}
	
	
	private Map<Integer, Integer> collectItemInfo() {
		final ItemMaps src = Module.getInstance().getItemMaps(roleId, srcpackid, false);	
		Map<Integer, Integer> info = new HashMap<Integer, Integer>();
		for (ItemBase bi : src) {
			Integer hasnum = info.get(bi.getItemId());
			info.put(bi.getItemId(), 
					hasnum == null ? bi.getNumber() : bi.getNumber() + hasnum);
		}
		if (srcpackid != dstpackid) {
			final ItemMaps dst = Module.getInstance().getItemMaps(roleId, dstpackid, false);
			for (ItemBase bi : dst) {
				Integer hasnum = info.get(bi.getItemId());
				info.put(bi.getItemId(), 
						hasnum == null ? bi.getNumber() : bi.getNumber() + hasnum);
			}
		}
		return info;
	}
	
	@Override
	protected boolean process() {
		final ItemMaps src = Module.getInstance().getItemMaps(roleId, srcpackid, false);
		if(src == null)
			return false;
		final ItemBase srcitem = src.getItem(srckey);
		if(srcitem == null)
			return false;
		if (dstpackid == BagTypes.DEPOT) {
			ItemMgrImp im = (fire.pb.item.ItemMgrImp) fire.pb.item.Module
					.getInstance().getItemManager();
			if (im.depotNotItems.contains(srcitem.getItemId())) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160033, null);
				return false;
			}
		}
		
		Map<Integer, Integer> iteminfos = collectItemInfo();
		if (dstpos == -1) {   //自动寻找位置转移物品
			if (!moveAutoPos())
				return false;
		} else {
			final ItemMaps dst;
			if (srcpackid == dstpackid) {
				dst = src;
			} else { 
				dst = Module.getInstance().getItemMaps(roleId, dstpackid, false); 
			}
			if (dst == null)
				return false;
			if(dstpos < 0 || dstpos >= dst.getCapacity())
				return false;
			final ItemBase dstitem = dst.getItemByPos(dstpos);
			if (dstitem == null) {
				if (!moveItemEmptyPos(src, dstpos, srcpackid == dstpackid)) {
					return false;
				}
			} else {
				if(srckey == dstitem.getKey() && src.getPackid() == dstpackid){
					Module.logger.error("角色"+roleId+"交换可堆叠物品时，原物品与目标位置物品的背包id及物品key相同。");
					return false;
				}
				if (!ItemMaps.isStack(srcitem.itemData, dstitem.itemData, fire.pb.Item.BIND)) {
					if (number == srcitem.getNumber() || number == -1) {
						if (!swapItem(src, srcitem, dstitem))
							return false;
					} else {
						return false;
					}
				} else {
					ItemShuXing attr = Module.getInstance().getItemManager().getAttr(srcitem.getItemId());
					if (attr == null)
						return false;
					final int maxheap = attr.maxstack - dstitem.getNumber();
					if (maxheap <= 0) {
						if (!swapItem(src, srcitem, dstitem))
							return false;
					} else {		
						final int actnum = number == -1 ? srcitem.getNumber() : number;
						final int pilenum = actnum < maxheap ? actnum : maxheap;
						int oldflag = dstitem.getFlags();
						dstitem.itemData.setFlags(
								srcitem.itemData.getFlags() | dstitem.getFlags());
						if (srcpackid != dstpackid) {
							ItemBase moveitem = src.TransOut(srckey, pilenum, "移动物品");
							if (moveitem == null)
								return false;
							if (!Module.getInstance().getItemMaps(roleId, dstpackid, false).TransIn(
									moveitem, dstpos, dstitem)) {
								return false;
							}
						} else {
							ItemMaps.doStack(srcitem.itemData, dstitem.itemData, pilenum);
							if (0 >= srcitem.getNumber()) {
								src.doDelete(srckey, true);
								SDelItem remove = new SDelItem();
								remove.packid = srcpackid;
								remove.itemkey = srckey;
								mkdb.Procedure.psendWhileCommit(roleId, remove);
								SItemNumChange mod = new SItemNumChange();
								mod.packid = srcpackid;
								mod.curnum = dstitem.getNumber();
								mod.keyinpack = dstitem.getKey();
								mkdb.Procedure.psendWhileCommit(roleId, mod);
							} else {
								SItemNumChange srcmod = new SItemNumChange();
								srcmod.packid = srcpackid;
								srcmod.curnum = srcitem.getNumber();
								srcmod.keyinpack = srcitem.getKey();
								mkdb.Procedure.psendWhileCommit(roleId, srcmod);
								SItemNumChange dstmod = new SItemNumChange();
								dstmod.packid = dstpackid;
								dstmod.curnum = dstitem.getNumber();
								dstmod.keyinpack = dstitem.getKey();
								mkdb.Procedure.psendWhileCommit(roleId, dstmod);
							}
						}
						if (oldflag != dstitem.getFlags()) {
							SItemSign send = new SItemSign();
							send.keyinpack = dstitem.keyinpack;
							send.packid = dstitem.getPackId();
							send.sign = dstitem.getFlags();
							mkdb.Procedure.psendWhileCommit(roleId, send);
						}
					}
				}
			}
		}
		Map<Integer, Integer> iteminfosaftermove = collectItemInfo();
		if (iteminfos.size() != iteminfosaftermove.size())
			return false;
		for (Map.Entry<Integer, Integer> itemelement : iteminfos.entrySet()) {
			Integer itemnum = iteminfosaftermove.get(itemelement.getKey());
			if (itemnum == null || itemnum.intValue() != itemelement.getValue().intValue())
				return false;
		}
		return true;
	}

}
