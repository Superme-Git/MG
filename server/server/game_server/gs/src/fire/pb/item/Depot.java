package fire.pb.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.log.enums.YYLoggerTuJingEnum;



// 仓库
public class Depot extends ItemMaps{

	public Depot(long roleId, boolean readonly) {
		super(roleId, readonly );
	}

	@Override
	public int getPackid() {
		return BagTypes.DEPOT;
	}
	@Override
	public fire.pb.Bag getPackInfo() {

		return super.getPackInfo();
	}

//	@Override
//	public boolean addItem( BasicItem item, int pos, String reason ) {
//		if ( readonly )
//			return false;
//		
//		if ( pos < 0 ) {
//			// 自动寻找位置
//		//	return addItemAutoPos( item, -pos, reason );
//			ArrayList<Integer> posidx = new ArrayList<Integer>( bag.getCapacity() );
//			for ( int i = 0; i < bag.getCapacity(); i++ ) {
//				posidx.add( 0 );
//			}
//			for ( Map.Entry<Integer, xbean.Item> xi : bag.getItems().entrySet() ) {
//				posidx.add( xi.getValue().getPosition(), 1 );
//				if ( canPile( item.getDataItem(), xi.getValue() ) ) {
//					int addnum = Math.min( 
//							item.getAttr().最大堆叠数量 - xi.getValue().getNumber(), 
//							item.getNumber() );
//					xi.getValue().setNumber( xi.getValue().getNumber() + addnum );
//					this.notifyModItemNum( xi.getKey(), xi.getValue().getNumber() );
//					item.getDataItem().setNumber( item.getDataItem().getNumber() - addnum );
//				}
//			}
//			
//			if ( item.getNumber() > 0 ) {
//				int freepos = -1;
//				for ( int posi : posidx ) {
//					if ( posi == 0 ) {
//						freepos = posi;
//						break;
//					}
//				}
//				if ( freepos == -1 )
//					return false;
//				return super.addItem( item, freepos, reason );
//			}
//			return true;
//		}
//	
//		if ( this.isFull() ) {
//			this.psendMsgNotify( DEPOTFULL_MSGID, null );
//			return false;
//		}
//		return super.addItem( item, pos, reason );
//	}
	public long addMoney(final long money, final String reason, final int logreasonid, YYLoggerTuJingEnum way) {
		return super.addMoney(money, reason, way);
	}
	
	@Override
	public ItemBase TransOut( final int key, final int number, final String reason ) {
		ItemBase ret = super.TransOut( key, number, reason );
		return ret;
	}
	@Override
	protected boolean TransIn(ItemBase item, int pos, ItemBase dstitem) {
		if (super.TransIn(item, pos, dstitem)) {
			return true;
		}
		return false;
	}
	@Override
	protected boolean TranItemToPage(ItemBase item, int page ) {
		if (!super.TranItemToPage(item, page)) {
			psendMsgNotify( MSG_FULL, null );
			return false;
		}
		return true;
	}
	@Override
	public AddItemResult doAddItem(final ItemBase item, final int pos, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		AddItemResult ret = super.doAddItem(item, pos, reason, countertype, xiangguanid);
		return ret;
	}
	
	
	public static boolean useDepotCheck(long roleId, long npckey){
		return true;
	}
	
	
	private Map<Integer, Integer> collectItemInfo(final int nPage, Map<Integer, xbean.Item> items) {
		final int startpos = (nPage - 1) * Commontext.BAG_PAGE_SIZE;
		final int endpos = nPage * Commontext.BAG_PAGE_SIZE - 1;
		Map<Integer, Integer> iteminfo = new HashMap<Integer, Integer>();
		for (final Map.Entry<Integer, xbean.Item> item : pack.getItems().entrySet()) {
			if (item.getValue().getPosition() >= startpos && item.getValue().getPosition() <= endpos) {
				Integer num = iteminfo.get(item.getValue().getId());
				if (num == null) {
					iteminfo.put(item.getValue().getId(), getItemNum(item.getValue()));
				} else {
					iteminfo.put(item.getValue().getId(), getItemNum(item.getValue()) + num);
				}
				if (items != null) {
					items.put(item.getKey(), item.getValue());
				}
			}
		}
		return iteminfo;
	}
	
	
	protected int sort(int nPage, Map<Integer, xbean.Item> items) {
		if (readonly)
			return 0;
		int count = 0;
		final java.util.SortedSet<ItemBase> myitems = new java.util.TreeSet<ItemBase>(new ItemComparator());
		
		for (Map.Entry<Integer, xbean.Item> itemm : items.entrySet()) {
			ItemBase item = getItem(itemm.getKey());
			
			if (item.getPackId() != BagTypes.QUEST)
				myitems.add(item);
		}
		
		final int startpos = (nPage - 1) * Commontext.BAG_PAGE_SIZE;
		final int endpos = nPage * Commontext.BAG_PAGE_SIZE - 1;
		int pos = startpos;
		for (final ItemBase i : myitems) {
			if (i.getPosition() != pos && i.getPosition() <= endpos) {
				i.getDataItem().setPosition(pos);
				count++;
			}
			pos++;
		}
		return count;
	}
	
	
	protected boolean arrange(final int nPage) {
		if (readonly)
			return false;
		Map<Integer, xbean.Item> items = new HashMap<Integer, xbean.Item>();
		Map<Integer, Integer> backup = collectItemInfo(nPage, items);
		final java.util.Set<Integer> toRemove = new java.util.TreeSet<Integer>();
		final java.util.Set<Integer> toIngore = new java.util.TreeSet<Integer>();
		for (final Map.Entry<Integer, xbean.Item> item : items.entrySet()) {
			if (toIngore.contains(item.getKey())
					|| toRemove.contains(item.getKey()))
				continue;

			final fire.pb.item.ItemShuXing attr = Module.getInstance().getItemManager().getAttr(item.getValue().getId());
			// 此处把不可堆叠的物品已经全部忽略。
			if (getItemNum(item.getValue()) >= attr.maxstack) {
				toIngore.add(item.getKey());
				continue;
			}

			for (final Map.Entry<Integer, xbean.Item> item2 : items.entrySet()) {
				if (toIngore.contains(item2.getKey())
						|| toRemove.contains(item2.getKey()))
					continue;
				if (item2.getKey() == item.getKey()
						|| !isStack(item2.getValue(), item.getValue(), 0))
					continue;
				final int pilednumber = Math.min(attr.maxstack
						- getItemNum(item.getValue()), getItemNum(item2.getValue()));
				if (pilednumber > 0) {
					doStack(item2.getValue(), item.getValue(), pilednumber);
				}
				if (getItemNum(item2.getValue()) == 0)
					toRemove.add(item2.getKey());
				if (getItemNum(item.getValue()) == attr.maxstack) {
					toIngore.add(item.getKey());
					break;
				}
			}
		}
		for (final Integer key : toRemove) {
			xbean.Item xitem = items.get(key);
			if (xitem != null) {
				doDelete(key, true);
				items.remove(key);
			}
			
		}
		//排序
		sort(nPage, items);
		
		Map<Integer, Integer> after = collectItemInfo(nPage, null);
		if (after.size() != backup.size()) {
			Module.logger.error("整理背包发生错误");
			return false;
		}
		for (Map.Entry<Integer, Integer> item : after.entrySet()) {
			Integer num = backup.get(item.getKey());
			if (num == null || num.intValue() != item.getValue().intValue()) {
				Module.logger.error("整理背包发生错误");
				return false;
			}
		}
		
		final SGetDepotInfo res = new SGetDepotInfo();
		res.pageid = nPage;
		res.baginfo = getPackInfoByPage(nPage);
		gnet.link.Onlines.getInstance().send(roleId, res);
		return true;
		
//		final SGetPackInfo res = new SGetPackInfo();
//		res.packid = getPackid();
//		res.baginfo = this.getPackInfo();
//		mkdb.Procedure.psendWhileCommit(roleId, res);
	}
	
	public fire.pb.Bag getPackInfoByPage(final int page) {
		final fire.pb.Bag ret = new fire.pb.Bag();
		
		List<ItemBase> itemBase = getItemInfoByPage(page);
		if (itemBase.size() == 0) {
			ret.capacity = getCapacity();
			return ret;
		}
		
		SRefreshNaiJiu send = new SRefreshNaiJiu();
		send.packid = getPackid();
		for (ItemBase bi : itemBase) {
			if (bi == null)
				continue;
			if (bi.getPackId() != getPackid())
				continue;
			ret.items.add(xItem2Item(bi.getDataItem(), bi.getKey(), 0));
			if (bi instanceof EquipItem) {
				EquipItem ei = (EquipItem) bi;
				if (ei.getEndure() <= 5) {
					EquipNaiJiu ee = new EquipNaiJiu();
					ee.keyinpack = ei.getKey();
					ee.endure = ei.getEndure();
					send.data.add(ee);
				}
			}
			else if (bi instanceof TimeOutItem) {
				bi.onTimeout();
			}
		}
		ret.capacity = getCapacity();
		
		if (!send.data.isEmpty()) {
			if (mkdb.Transaction.current() != null)
				mkdb.Procedure.psendWhileCommit(roleId, send);
			else
				gnet.link.Onlines.getInstance().send(roleId, send);
		}

		return ret;
	}
	
	private List<ItemBase> getItemInfoByPage(final int nPage) {
		List<ItemBase> items = new ArrayList<ItemBase>();
		final int startpos = (nPage - 1) * Commontext.BAG_PAGE_SIZE;
		final int endpos = nPage * Commontext.BAG_PAGE_SIZE - 1;
		for (ItemBase bi : this) {
			if (bi.getDataItem().getPosition() >= startpos && bi.getDataItem().getPosition() <= endpos) {
				items.add(bi);
			}
		}
		return items;
	}

}
