package fire.pb.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.event.MoneyChangeEvent;
import fire.pb.fushi.PAddFuShi;
import fire.pb.fushi.FushiConst;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.util.Misc;

//发送协议
public class Pack extends ItemMaps {
	private class PackIterator implements Iterator<ItemBase> {
		private Iterator<Integer> bagiter;
		private Iterator<Integer> questiter;

		public PackIterator() {
			bagiter = pack.getItems().keySet().iterator();
			questiter = Pack.this.new QuestItemColumn().pack.getItems().keySet()
					.iterator();
		}

		@Override
		public boolean hasNext() {
			return bagiter.hasNext() || questiter.hasNext();
		}

		@Override
		public ItemBase next() {
			xbean.Item item;
			if (bagiter.hasNext()) {
				int key = bagiter.next();
				item = pack.getItems().get(key);
				if (item == null)
					return null;
				return Module.getInstance().getItemManager()
						.toItemBase(item, roleId, BagTypes.BAG, key);
			} else {
				int key = questiter.next();
				item = Pack.this.new QuestItemColumn().pack.getItems().get(key);
				if (item == null)
					return null;
				return Module.getInstance().getItemManager()
						.toItemBase(item, roleId, BagTypes.QUEST, key);
			}
		}

		@Override
		public void remove() {
			throw new RuntimeException("不支持删除");
		}
	}

	public final class QuestItemColumn extends ItemMaps {

		QuestItemColumn() {
			super(Pack.this.roleId, Pack.this.readonly);
		}

		@Override
		public int doAddItem(final int itemid, final int num, final int numtype,
				final int initflag, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
				final int xiangguanid) {
			if (pack.getItems().size() >= 20 && !isFull()) {

				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 143585, null);
			}
			return super.doAddItem(itemid, num, numtype, initflag, reason,
					countertype, xiangguanid);
		}

		@Override
		public AddItemResult doAddItem(final ItemBase item, final int p,
				final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
				final int xiangguanid) {
			if (pack.getItems().size() >= 20 && !isFull()) {
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 143585, null);
			}
			return super.doAddItem(item, p, reason, countertype, xiangguanid);
		}

		@Override
		public int getPackid() {
			return BagTypes.QUEST;
		}

		@Override
		protected int incNextId() {
			return Pack.this.incNextId();
		}
		
		@Override
		protected boolean doList() {
			throw new RuntimeException("任务背包不允许整理");
		}

		@Override
		protected boolean doListquest() {
			return super.doListquest();
		}

		@Override
		public ItemBase TransOut(final int key, final int number,
				final String reason) {
			ItemBase item = super.TransOut(key, number, reason);
			if (item != null) {
				fire.pb.event.Poster.getPoster().dispatchEvent(
						new fire.pb.event.BagItemChange(roleId, item
								.getItemId()));
			}
			return item;
		}
	}

	public Pack(long roleId, boolean readonly) {
		super(roleId, readonly);
	}

	@Override
	protected long addContribution(final long money, final String reason, YYLoggerTuJingEnum way) {
		long ret = super.addCurrency(money, MoneyType.MoneyType_ProfContribute,
				reason, way);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new MoneyChangeEvent(roleId));
		return ret;
	}

	private long addContribution(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;

		long res = (Long.MAX_VALUE - getContribution() <= money) ? Long.MAX_VALUE
				: money + getContribution();// 防止溢出;
		if (res < 0) {
			Module.logger.error("贡献不足");
			return 0;
		}
		long ret = addContribution(money, reason, countertype);
		return ret;
	}

	// 该方法bag内使用 不对外开放
	@Override
	protected long addCurrency(final long money, final int moneyType,
			final String reason, YYLoggerTuJingEnum way) {
		long ret = super.addCurrency(money, moneyType, reason, way);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new MoneyChangeEvent(roleId));
		return ret;
	}

	private long addCurrency(final long money, final int moneyType,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;

		long res = (Long.MAX_VALUE - getCurrency(moneyType) <= money) ? Long.MAX_VALUE
				: money + getCurrency(moneyType);// 防止溢出;
		if (res < 0) {
			Module.logger.error("金钱不足");
			return 0;
		}
		long ret = addCurrency(money, moneyType, reason, countertype);
		return ret;
	}

	@Override
	protected long addGold(final long money, final String reason, YYLoggerTuJingEnum way) {
		long ret = super.addCurrency(money, MoneyType.MoneyType_GoldCoin,
				reason, way);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new MoneyChangeEvent(roleId));
		return ret;
	}

	private long addGold(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;

		long res = (Long.MAX_VALUE - getGold() <= money) ? Long.MAX_VALUE
				: money + getGold();// 防止溢出;
		if (res < 0) {
			Module.logger.error("金币不足");
			return 0;
		}
		long ret = addGold(money, reason, countertype);
		return ret;
	}

	@Override
	public int doAddItem(final int itemid, final int num, final int numtype,
			final int initflag, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
			final int xiangguanid) {
		return addItem(itemid, num, numtype, initflag, reason, countertype,
				xiangguanid, true);
	}

	public int addItem(final int itemid, final int num, final int numtype,
			final int initflag, final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype,
			final int xiangguanid, final boolean playeffect) {
		ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemid);
		if (attr == null)
			throw new RuntimeException("物品id=" + itemid + "属性为null");
		final int added;
		if (attr instanceof TaskItemShuXing
				&& ((TaskItemShuXing) attr).intoquestpack == 1) {
			added = this.new QuestItemColumn().doAddItem(itemid, num, numtype,
					initflag, reason, countertype, xiangguanid);
		} else {
			added = super.doAddItem(itemid, num, numtype, initflag, reason,
					countertype, xiangguanid);
		}

		// here
		if (added > 0 && playeffect) {
			SItemAdded send = new SItemAdded();
			send.items.add(new ItemAddInfo(itemid, added));
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new fire.pb.event.BagItemChange(roleId, itemid));
		return added;
	}

	public int addItem(final int itemid, final int num, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid,
			final boolean playeffect) {
		return addItem(itemid, num, 0, 0, reason, countertype, xiangguanid,
				playeffect);
	}

	@Override
	public AddItemResult doAddItem(final ItemBase item, final int pos,
			String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		return addItem(item, pos, reason, countertype, xiangguanid, true);
	}

	public AddItemResult addItem(final ItemBase item, final int pos,
			String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid,
			final boolean playeffect) {
		final long oldownerid = item.getOwnerid();
		final int oldpackid = item.getPackId();
		final int oldnumber = item.getNumber();
		if (item instanceof TaskItem
				&& ((TaskItemShuXing) item.itemAttr).intoquestpack == 1) {
			// 任务道具
			AddItemResult ret = new QuestItemColumn().doAddItem(item, pos,
					reason, countertype, xiangguanid);
			if (ret != AddItemResult.SUCC) {
				return ret;
			}
		} else {
			AddItemResult ret = super.doAddItem(item, pos, reason, countertype,
					xiangguanid);
			if (ret != AddItemResult.SUCC) {
				return ret;
			}
		}
		if (roleId != oldownerid && playeffect) {
			SItemAdded send = new SItemAdded();
			send.items.add(new ItemAddInfo(item.getItemId(), oldnumber));
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}
		if (roleId != oldownerid || getPackid() != oldpackid)
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.BagItemChange(roleId, item.getItemId()));
		return AddItemResult.SUCC;
	}

	@Override
	protected long addMoney(final long money, final String reason, YYLoggerTuJingEnum way) {
		long ret = super.addCurrency(money, MoneyType.MoneyType_SilverCoin,
				reason, way);
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new MoneyChangeEvent(roleId));
		return ret;
	}

	private long addMoney(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;

		long res = (Long.MAX_VALUE - getMoney() <= money) ? Long.MAX_VALUE
				: money + getMoney();// 防止溢出;
		if (res < 0) {
			Module.logger.error("金钱不足");
			return 0;
		}
		long ret = addMoney(money, reason, countertype);
		return ret;
	}

	public long addSysContribution(final long gold, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		long ret = addContribution(gold, reason, countertype, xiangguanid);
		return ret;
	}

	public long addSysCurrency(final long money, final int moneyType,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum way, final int xiangguanid) {
		switch (moneyType) {
		case MoneyType.MoneyType_HearthStone: {
			int userId = new PropRole(roleId, true).getUserid();
			new PAddFuShi(userId, roleId, (int) money,
					FushiConst.SYS_FUSHI, way).call();
			break;
		}
		case MoneyType.MoneyType_SilverCoin: // 银币
		case MoneyType.MoneyType_GoldCoin: // 金币
		case MoneyType.MoneyType_ProfContribute: // 职业贡献
		case MoneyType.MoneyType_RongYu: // 荣誉值
		case MoneyType.MoneyType_FactionContribute: // 帮派贡献
		case MoneyType.MoneyType_ShengWang: // 声望
		case MoneyType.MoneyType_FestivalPoint: // 节日积分
		case MoneyType.MoneyType_GoodTeacherVal:// 良师值
		case MoneyType.MoneyType_EreditPoint:// 信用值
		{
			long ret = addCurrency(money, moneyType, reason, way,
					xiangguanid);
						
			if (moneyType == MoneyType.MoneyType_ProfContribute)
			{
				xbean.Properties prop = xtable.Properties.get(this.roleId);
				if (prop != null)
				{
					long cur = getCurrency(moneyType);
					if (cur > prop.getHistorymaxprofessioncontribute())
					{
						prop.setHistorymaxprofessioncontribute(cur);
					}
				}
			}			
			
			return ret;
		}
		}

		return money;
	}

	public long addSysGold(final long gold, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		long ret = addGold(gold, reason, countertype, xiangguanid);
		return ret;
	}

	public long addSysMoney(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		long ret = addMoney(money, reason, countertype, xiangguanid);
		return ret;
	}

	@Override
	public void clear() {
		for (ItemBase bi : this) {
			final SDelItem send = new SDelItem();
			send.itemkey = bi.getKey();
			send.packid = getPackid();
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}
		super.clear();
	}
	
	//通过映射表来得到物品数量
	public int getItemsNumByMapping(int itemsid, int flag) {
		int firstitemnum = getItemNum(itemsid, flag);
		int otheritemnum = 0;
		SItemToItem item2item = ConfigManager.getInstance().getConf(SItemToItem.class).get(itemsid);
		if(item2item != null) {
			for(Integer curitem : item2item.getItemsid()) {
				int curitemnum = getItemNum(curitem.intValue(), flag);
				otheritemnum += curitemnum;
			}
		}
		return firstitemnum + otheritemnum;
	}

	@Override
	public int getItemNum(int itemid, int flag) {
		return super.getItemNum(itemid, flag)
				+ this.new QuestItemColumn().getItemNum(itemid, flag);
	}

	@Override
	public int getItemNumExpStall(int itemid) {
		return super.getItemNumExpStall(itemid)
				+ this.new QuestItemColumn()
						.getItemNumExpStall(itemid);
	}

	@Override
	public int getPackid() {
		return BagTypes.BAG;
	}

	public int getBagItemNum(int itemid) {
		int num = 0;
		for (ItemBase bi : this) {
			if (bi.getItemId() == itemid)
				num += bi.getNumber();
		}
		return num;
	}

	@Override
	public ItemBase getItem(int keyinpack) {
		ItemBase item = super.getItem(keyinpack);
		if (item == null) {
			return this.new QuestItemColumn().getItem(keyinpack);
		}
		return item;
	}

	@Override
	public Iterator<ItemBase> iterator() {
		return new PackIterator();
	}

	@Override
	protected boolean TransIn(ItemBase item, int pos, ItemBase dstitem) {
		if (super.TransIn(item, pos, dstitem)) {
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.BagItemChange(roleId, item.getItemId()));
			return true;
		}
		return false;
	}

	@Override
	protected boolean TranItemToPage(ItemBase item, int page) {
		final long oldpackid = item.getPackId();
		if (!super.TranItemToPage(item, page)) {
			psendMsgNotify(MSG_NOTENOUGH, null);
			return false;
		}
		if (oldpackid != getPackid())
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.BagItemChange(roleId, item.getItemId()));
		return true;
	}

	@Override
	public ItemBase TransOut(final int key, final int number, final String reason) {
		ItemBase item = super.TransOut(key, number, reason);
		if (item == null) {
			item = new QuestItemColumn().TransOut(key, number, reason);
		} else {
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.BagItemChange(roleId, item.getItemId()));
		}
		return item;
	}
	
	public int removeItemById(int itemid, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		int removed = removeItemById(itemid, num, countertype, xiangguanid, reason,
				false, 0);
		
		if (removed == 0) {
			removed = new QuestItemColumn().removeItemById(itemid, num,
					countertype, xiangguanid, reason);
		}
		return removed;
	}
	
	public int removeItemByItemType(int itemType, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		try {
			int itemKey = 0;
			int itemId = 0;
			List<Integer> itemIdList = new ArrayList<Integer>();
			List<Integer> itemNumberList = new ArrayList<Integer>();
			List<Integer> itemKeyList = new ArrayList<Integer>();

			for (ItemBase bi : this) {
				ItemShuXing attr = bi.getItemAttr();
				if (attr == null) {
					continue;
				}
				if (attr.getTypeid() == itemType) {
					itemIdList.add(bi.getItemId());
					itemNumberList.add(bi.getNumber());
					itemKeyList.add(bi.getKey());
				}
			}

			if (itemNumberList.size() > 0) {
				int itemIndex = Misc.getProbability(itemNumberList);
				if (itemIndex < itemKeyList.size()) {
					itemId = itemIdList.get(itemIndex);
					itemKey = itemKeyList.get(itemIndex);
				}
			}

			if (itemKey != 0 && itemId != 0) {
				if (removeItemWithKey(itemKey, 1, countertype, xiangguanid,
						reason) == 1) {
					return itemId;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 0;
	}

	@Override
	public int removeItemWithKey(final int key, final int num,
			final  YYLoggerTuJingEnum countertype, final int xiangguanid, String reason) {
		ItemBase bi = getItem(key);
		if (bi == null)
			return 0;
		int itemid = bi.getItemId();
		int removed = super.removeItemWithKey(key, num, countertype, xiangguanid,
				reason);
		if (removed == 0) {
			removed = new QuestItemColumn().removeItemWithKey(key, num,
					countertype, xiangguanid, reason);
		}
		fire.pb.event.Poster.getPoster().dispatchEvent(
				new fire.pb.event.BagItemChange(roleId, itemid));
		return removed;
	}

	public long subContribution(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;

		return addContribution(money, reason, countertype, xiangguanid);
	}

	public long subCurrency(final long money, final int moneyType,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;
		if (money >= 0) {
			return 0;
		}
		long v = addCurrency(money, moneyType, reason, countertype, xiangguanid);
		
		return v;
	}

	public long subGold(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;
		return addGold(money, reason, countertype, xiangguanid);
	}

	public long subMoney(final long money, final String reason,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		if (readonly)
			return 0;
		if (money >= 0) {
			return 0;
		}
		return addMoney(money, reason, countertype, xiangguanid);
	}
}
