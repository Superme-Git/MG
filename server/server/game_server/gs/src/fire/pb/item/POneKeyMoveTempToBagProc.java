package fire.pb.item;

import java.util.HashMap;
import java.util.Map;

import mkdb.Procedure;



public class POneKeyMoveTempToBagProc extends Procedure {
	private final long roleId;    //角色id
	
	public POneKeyMoveTempToBagProc(long roleId) {
		super();
		this.roleId = roleId;
	}
	
	private int getFirstFreePos(ItemMaps dstbag) {
		java.util.List<Integer> freeposes = dstbag.getFreepos();
		if (freeposes.isEmpty())
			return -1;
		int fpos = Integer.MAX_VALUE;
		for (int pos : freeposes) {
			if (fpos > pos)
				fpos = pos;
		}
		if (fpos == Integer.MAX_VALUE)
			return -1;
		return fpos;
	}
	
	private boolean moveItemBetweenBags(final int itemKey, final ItemMaps src, final int srcitemnum, final int dstpos) {
		ItemBase moveitem = src.TransOut(itemKey, srcitemnum, "移动物品");
		if (moveitem == null)
			return false;
		if (!Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false).TransIn(
				moveitem, dstpos)) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 145950, null);
			return false;
		}
		return true;
	}
	
	private boolean moveAutoPos() {
		final Temp src = (Temp)Module.getInstance().getItemMaps(roleId, BagTypes.TEMP, false);
		final Pack dst = (Pack)Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		java.util.List<Integer> removeList = new java.util.ArrayList<Integer>();
		for (ItemBase bi : src)
		{			
			if (bi == null)
				continue;
			if (bi.getPackId() != src.getPackid())
				continue;
			
			removeList.add(bi.getKey());			
		}
		
		for (Integer index : removeList) {
			int freepos = getFirstFreePos(dst);
			if (freepos == -1) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 144965, null);
				return true;
			}
			ItemBase bi = src.getItem(index);
			if (!moveItemBetweenBags(index, src, bi.getNumber(), freepos))
				return false;
		}
		
		return true;
	}
	
	
	
	private Map<Integer, Integer> collectItemInfo() {
		final ItemMaps src = Module.getInstance().getItemMaps(roleId, BagTypes.TEMP, false);	
		Map<Integer, Integer> info = new HashMap<Integer, Integer>();
		for (ItemBase bi : src) {
			Integer hasnum = info.get(bi.getItemId());
			info.put(bi.getItemId(), 
					hasnum == null ? bi.getNumber() : bi.getNumber() + hasnum);
		}

		final ItemMaps dst = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		for (ItemBase bi : dst) {
			Integer hasnum = info.get(bi.getItemId());
			info.put(bi.getItemId(), 
					hasnum == null ? bi.getNumber() : bi.getNumber() + hasnum);
		}
		
		return info;
	}
	
	@Override
	protected boolean process() {
		final ItemMaps src = Module.getInstance().getItemMaps(roleId, BagTypes.TEMP, false);
		if(src == null)
			return false;
		
		final ItemMaps dst = Module.getInstance().getItemMaps(roleId, BagTypes.BAG, false);
		if(dst == null)
			return false;
			
		Map<Integer, Integer> iteminfos = collectItemInfo();
		if (!moveAutoPos())
			return false;
		
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
