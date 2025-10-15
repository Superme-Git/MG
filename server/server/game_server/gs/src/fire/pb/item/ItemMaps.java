package fire.pb.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import fire.log.YYLogger;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.Item;
import fire.pb.WorldTime;
import fire.pb.attr.RoleCurrency;
import fire.pb.attr.SRefreshRoleCurrency;
import fire.pb.game.MoneyType;
import fire.pb.util.DateValidate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ItemMaps implements Iterable<ItemBase> {
	private class PackIterator implements Iterator<ItemBase> {
		final private Iterator<Integer> iter;

		public PackIterator() {
			iter = pack.getItems().keySet().iterator();
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public ItemBase next() {
			final int key = iter.next();
			xbean.Item item = pack.getItems().get(key);
			if (item == null)
				return null;
			return Module.getInstance().getItemManager()
					.toItemBase(item, roleId, getPackid(), key);
		}

		@Override
		public void remove() {
			throw new RuntimeException("暂时不支持删除");
		}
	}
	protected final static int MSG_FULL = 140397;
	protected final static int MSG_NOTENOUGH = 120059;
	public static boolean isStack(xbean.Item item) {
		if (item == null)
			return false;
		if (!(item.getMarkettime() == 0 && (item.getExtid() == 0)))
			return false;

		return true;
	}
	public static boolean isStack(xbean.Item srcItem, xbean.Item dstItem,
			int dntcare) {
		if (dstItem == null || srcItem == null)
			return false;
		if (!(srcItem.getId() == dstItem.getId()
				&& srcItem.getTimeout() == dstItem.getTimeout() && (srcItem
				.getExtid() == 0 && dstItem.getExtid() == 0)))
			return false;

		if (dntcare == 0) {
			boolean pile = ((srcItem.getFlags() & fire.pb.Item.BIND) == (dstItem
					.getFlags() & fire.pb.Item.BIND));

			if (pile) {
				if (dstItem.getMarkettime() == 0
						&& srcItem.getMarkettime() == 0)
					return true;

				long srcitemfreezeTime = srcItem.getMarkettime();
				long dstitemfreezeTime = dstItem.getMarkettime();
				
				long now = System.currentTimeMillis();
				if (now >= srcitemfreezeTime && now >= dstitemfreezeTime)
					return true;

				if (DateValidate.inTheSameDay(srcitemfreezeTime,
						dstitemfreezeTime)) {
					dstItem.setMarkettime(Math.max(srcitemfreezeTime,
							dstitemfreezeTime));
					return true;
				} else
					return false;
			}

			return false;
		}
		return (srcItem.getFlags() | dntcare) == (dstItem.getFlags() | dntcare);
	}
	static void doStack(xbean.Item srcItem, xbean.Item dstItem, int num) {

		Map<Integer, Integer> heapnum;
		if (num != -1) {
			assert (getItemNum(srcItem) >= num);
			heapnum = doCut(srcItem, num);
		} else {
			heapnum = new HashMap<Integer, Integer>();
			heapnum.putAll(srcItem.getNumbermap());
			if (srcItem.getNumber() != 0) {
				heapnum.put(0, srcItem.getNumber());
				srcItem.setNumber(0);
			}
			srcItem.getNumbermap().clear();
		}
		stackNumber(heapnum, dstItem);

	}

	public static Map<Integer, Integer> doCut(xbean.Item xItem, int num) {
		Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
		int leftnum = num;
		int curflag = 2;
		for (; curflag > 0 && leftnum > 0; curflag--) {
			Integer n = xItem.getNumbermap().get(curflag);
			if (n == null)
				continue;
			if (leftnum >= n) {
				xItem.getNumbermap().remove(curflag);
				ret.put(curflag, n);
				leftnum -= n;
			} else {
				ret.put(curflag, leftnum);
				xItem.getNumbermap().put(curflag, n - leftnum);
				leftnum = 0;
			}
		}
		if (leftnum > 0) {
			xItem.setNumber(xItem.getNumber() - leftnum);
			ret.put(0, leftnum);
		}
		return ret;
	}

	public static String getPackname(final int bagtype) {
		switch (bagtype) {
		case BagTypes.BAG:
			return "背包";
		case BagTypes.DEPOT:
			return "仓库";
		case BagTypes.EQUIP:
			return "装备背包";
		case BagTypes.QUEST:
			return "任务背包";
		case BagTypes.TEMP:
			return "临时背包";
		case BagTypes.EMPTY:
			return "Null背包";
		case BagTypes.MARKET:
			return "拍卖背包";
		case BagTypes.BLACKMARKET:
			return "黑市背包";
		}
		return "异常背包";
	}

	public static String getItemLogs(List<ItemBase> items) {
		StringBuilder sb = new StringBuilder();
		for (ItemBase item : items) {
			if (item == null)
				continue;
			sb.append(item.getItemLog());
		}
		return sb.toString();
	}

	public static final int getItemHasNum(final long roleId, final int itemId) {
		int num = 0;
		Pack pack = new Pack(roleId, true);
		for (final ItemBase bi : pack) {
			if (bi.getItemId() == itemId)
				num += bi.getNumber();
		}
		for (final ItemBase bi : new Depot(roleId, true)) {
			if (bi.getItemId() == itemId)
				num += bi.getNumber();
		}
		for (final ItemBase bi : new Temp(roleId, true)) {
			if (bi.getItemId() == itemId)
				num += bi.getNumber();
		}
		return num;
	}
	
	public static int getItemNum(xbean.Item item) {
		int itemnum = item.getNumber();
		for (int num : item.getNumbermap().values()) {
			itemnum += num;
		}
		return itemnum;
	}

	public static void stackNumber(Map<Integer, Integer> stackNum, xbean.Item xItem) {
		for (Map.Entry<Integer, Integer> e : stackNum.entrySet()) {
			if (e.getKey() == 0) {
				xItem.setNumber(xItem.getNumber() + e.getValue());
			} else {
				Integer n = xItem.getNumbermap().get(e.getKey());
				if (n == null) {
					xItem.getNumbermap().put(e.getKey(), e.getValue());
				} else {
					xItem.getNumbermap().put(e.getKey(), n + e.getValue());
				}
			}
		}
	}

	protected static fire.pb.Item xItem2Item(xbean.Item dstItem,
			int key, int isNew) {
		final fire.pb.Item ki = new fire.pb.Item();
		ki.flags = dstItem.getFlags();
		ki.id = dstItem.getId();
		ki.number = getItemNum(dstItem);
		ki.position = dstItem.getPosition();
		ki.key = key;
		ki.timeout = dstItem.getTimeout();
		if ((dstItem.getFlags() & fire.pb.Item.TIMEOUT) != 0) {
			if (ItemBase.isTimeout(dstItem)) {
				ki.loseeffecttime = -1;
			}
			else
				ki.loseeffecttime = dstItem.getLoseeffecttime() - WorldTime.getInstance().getTimeInMillis();
		}
		else
			ki.loseeffecttime = -1;
		ki.isnew = isNew;
		ki.markettime = dstItem.getMarkettime();
		return ki;
	}

	protected static fire.pb.Item xItem2Item(xbean.Item dstItem,
			int key, int isNew, int num) {
		final fire.pb.Item ki = new fire.pb.Item();
		ki.flags = dstItem.getFlags();
		ki.id = dstItem.getId();
		ki.number = num;
		ki.position = dstItem.getPosition();
		ki.key = key;
		ki.timeout = dstItem.getTimeout();
		if ((dstItem.getFlags() & fire.pb.Item.TIMEOUT) != 0) {
			if (ItemBase.isTimeout(dstItem)) {
				ki.loseeffecttime = -1;
			}
			else
				ki.loseeffecttime = dstItem.getLoseeffecttime() - WorldTime.getInstance().getTimeInMillis();
		}
		else
			ki.loseeffecttime = -1;
		
		ki.isnew = isNew;
		ki.markettime = dstItem.getMarkettime();
		return ki;
	}

	protected final xbean.Bag pack;

	protected final BagConfig conf;

	protected final boolean readonly;

	protected final long roleId;

	@SuppressWarnings("unchecked")
	public ItemMaps(final long roleId, final boolean readonly) {
		this.readonly = readonly;
		this.roleId = roleId;
		conf = Module.getInstance().getItemManager().getPackCfg(getPackid());
		final mkdb.TTable<Long, xbean.Bag> table = (mkdb.TTable<Long, xbean.Bag>) mkdb.Mkdb
				.getInstance().getTables().getTable(conf.tablename);
		if (table == null)
			throw new RuntimeException("未找到table=" + conf.tablename);
		final xbean.Bag myPack;
		if (readonly) {
			myPack = table.select(roleId,
					new mkdb.TField<xbean.Bag, xbean.Bag>() {
						@Override
						public xbean.Bag get(xbean.Bag v) {
							return v.toData();
						}
					});
		} else {
			myPack = table.get(roleId);
		}
		
		if (myPack == null) {
			if (readonly)
				pack = xbean.Pod.newBagData();
			else
				pack = xbean.Pod.newBag();
			pack.setCapacity(conf.sizesize);
			if (!readonly)
				table.insert(roleId, pack);
		} else {
			pack = myPack;
		}
	}

	public int addCapacity(int size) {
		if (!readonly)
			pack.setCapacity(pack.getCapacity() + size);
		return pack.getCapacity();
	}

	protected long addContribution(final long money, final String reason, YYLoggerTuJingEnum way) {
		if (readonly)
			return 0;
		long res = (Long.MAX_VALUE - getContribution() <= money) ? Long.MAX_VALUE
				: money + getContribution();
		if (res < 0) {
			Module.logger.error("贡献不足");
			return 0;
		} else if (res > conf.maxmoney) {
			return 0;
		}
		long oldvalue = getCurrency(MoneyType.MoneyType_ProfContribute);
		pack.getCurrency().put(MoneyType.MoneyType_ProfContribute, res);
		this.notifyMoney(MoneyType.MoneyType_ProfContribute);
		long realadd = res - oldvalue;
		
		// 运营日志
		YYLogger.OpTokenGetLog(roleId, way, MoneyType.MoneyType_ProfContribute, money, res, new fire.log.beans.ItemBean());
		return realadd;
	}

	protected long addCurrency(final long money, final int moneyType,
			final String reason, YYLoggerTuJingEnum way) {
		if (readonly)
			return 0;

		long res = (Long.MAX_VALUE - getCurrency(moneyType) <= money) ? Long.MAX_VALUE
				: money + getCurrency(moneyType);
		if (res < 0) {
			Module.logger.error("金钱不足:" + roleId);
			return 0;
		} else if (res > conf.maxmoney) {
			long oldvalue = getCurrency(moneyType);
			pack.getCurrency().put(moneyType, conf.maxmoney);
			this.notifyMoney(moneyType);
			long realadd = res - oldvalue;
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160162, null);
			return realadd;
		} else if (res > 99999900000L) {
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 160113, null);
		}

		SRefreshRoleCurrency send = new SRefreshRoleCurrency();
		switch (moneyType) {
		case MoneyType.MoneyType_FactionContribute:
			send.datas.put(RoleCurrency.GUILD_DED, res);
			break;
		case MoneyType.MoneyType_GoodTeacherVal:
			send.datas.put(RoleCurrency.TEACHER_SCORE, res);
			break;
		case MoneyType.MoneyType_Activity:
			send.datas.put(RoleCurrency.ACTIVE_SCORE, res);
			break;
		case MoneyType.MoneyType_ShengWang:
			send.datas.put(RoleCurrency.POP_SCORE, res);
			if (money > 0)
				fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.GET_SHENGWANG, 0, 0, (int)money);
			break;
		case MoneyType.MoneyType_RongYu:
			send.datas.put(RoleCurrency.HONOR_SCORE, res);
			break;
		case MoneyType.MoneyType_ProfContribute:
			send.datas.put(RoleCurrency.PROF_CONTR, res);
			break;
		case MoneyType.MoneyType_EreditPoint:
			send.datas.put(RoleCurrency.EREDITPOINT_SCORE, res);
			break;
		default:
			break;
		}
		if (send.datas.size() > 0) {
			mkdb.Procedure.psend(roleId, send);
		}

		long oldvalue = getCurrency(moneyType);
		pack.getCurrency().put(moneyType, res);
		this.notifyMoney(moneyType);
		long realadd = res - oldvalue;
		
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
		
		// 运营日志
		if(money > 0){
			YYLogger.OpTokenGetLog(roleId, way, moneyType, money, res, new fire.log.beans.ItemBean());
		}else if(money != 0){
			YYLogger.OpTokenUseLog(roleId, way, moneyType, money, res, new fire.log.beans.ItemBean());
		}
		
		Module.logger.info(new StringBuffer().append(roleId).append(" 添加货币:")
				.append(money).append(" 类型:").append(moneyType).append(" 理由: ")
				.append(reason));

		return realadd;
	}

	public boolean addFlag(int key, int flag, int packid) {
		if (readonly)
			return false;
		xbean.Item xi = pack.getItems().get(key);
		if (xi == null)
			return true;
		else {
			xi.setFlags(xi.getFlags() | flag);
			SItemSign ref = new SItemSign(key, xi.getFlags(),
					packid);
			mkdb.Procedure.psendWhileCommit(roleId, ref);
		}
		return false;
	}

	protected long addGold(final long money, final String reason, final YYLoggerTuJingEnum way) {
		if (readonly)
			return 0;
		long res = (Long.MAX_VALUE - getGold() <= money) ? Long.MAX_VALUE
				: money + getGold();
		if (res < 0) {
			Module.logger.error("金币不足");
			return 0;
		} else if (res > conf.maxmoney) {
			return 0;
		}
		long oldvalue = getCurrency(MoneyType.MoneyType_GoldCoin);
		pack.getCurrency().put(MoneyType.MoneyType_GoldCoin, res);
		this.notifyMoney(MoneyType.MoneyType_GoldCoin);
		long realadd = res - oldvalue;
		
		// 运营日志
		YYLogger.OpTokenGetLog(roleId, way, MoneyType.MoneyType_GoldCoin, realadd, res, new fire.log.beans.ItemBean());
		return realadd;
	}

	public int doAddItem(final int itemId, final int num, final int numType,
			final int initFlag, final String reason, final fire.log.enums.YYLoggerTuJingEnum counterType,
			final int xiangGuanId) {
		if (readonly || num <= 0)
			return 0;
		ItemShuXing attr = Module.getInstance().getItemManager().getAttr(itemId);
		if (attr == null)
			return 0;

		int canOwn = Module.getInstance().getMaxOwn(roleId, itemId);
		int canAdd = Integer.MAX_VALUE;
		if (canOwn != 0) {
			canAdd = canOwn - getItemHasNum(roleId, itemId);
		}
		int stacked = doDoStack(itemId, num, numType, initFlag, canAdd,
				counterType);
		int leftNum = num - stacked;
		canAdd -= stacked;

		while (leftNum > 0) {
			if (canAdd <= 0) {
				List<String> parameters = new ArrayList<String>();
				parameters.add(attr.name);
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 142752, parameters);
				return num - leftNum;
			}
			int getNum = Math.min(attr.maxstack, leftNum);
			getNum = Math.min(getNum, canAdd);
			ItemBase itemBase = Module.getInstance().getItemManager().genItemBase(itemId, getNum, numType);
			if (itemBase == null)
				break;
			if (initFlag != 0) {
				itemBase.setFlag(itemBase.getFlags() | initFlag);
			}
			if (!processDoAddItem(itemBase, -1, counterType)) {
				break;
			}
			leftNum -= getNum;
			canAdd -= getNum;
		}

		final int addNum = getItemHasNum(roleId, itemId);
		fire.log.beans.ItemBean itemBean = new fire.log.beans.ItemBean(itemId, num, addNum);
		fire.log.YYLogger.OpItemGetLog(roleId, itemBean, counterType);
		Module.logger.info(new StringBuffer().append("roleId:").append(roleId)
				.append(" 添加物品:").append(itemId).append(" 数量:").append(num).append(" 添加后数量:").append(addNum)
				.append(" 类型:").append(counterType).append(" 理由:")
				.append(reason));

		return num - leftNum;
	}

	public final int doAddItem(final int itemId, final int num,
			final int numType, final String reason, final fire.log.enums.YYLoggerTuJingEnum counterType,
			final int xiangGuanId) {
		return doAddItem(itemId, num, numType, 0, reason, counterType,
				xiangGuanId);
	}

	public final int doAddItem(final int itemId, final int num,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum counterType, final int xiangGuanId) {
		return doAddItem(itemId, num, 0, 0, reason, counterType, xiangGuanId);
	}

	public AddItemResult doAddItem(final ItemBase item, final int pos,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum counterType, final int xiangGuanId) {
		int num = item.getNumber();
		int hasnum = getItemHasNum(roleId, item.getItemId());
		int maxown = Module.getInstance().getMaxOwn(roleId, item.getItemId());
		if (maxown != 0 && hasnum + item.getNumber() > maxown) {
			List<String> parameters = new ArrayList<String>();
			parameters.add(item.getItemAttr().name);
			fire.pb.talk.MessageMgr.psendMsgNotify(roleId, 142752, parameters);
			return AddItemResult.MAX_OWN_NUM;
		}
		if (item.getOwnerid() != 0 && item.getOwnerid() != roleId) {
			if (item.isBind())
				return AddItemResult.BIND_ITEM;
			else if (item.isTimeout()) {
				return AddItemResult.TIMEOUT_CANNOT_TRADE;
			}
		}

		if (!processDoAddItem(item, pos, counterType))
			return AddItemResult.FULL;
		final int addnum = getItemHasNum(roleId, item.getItemId());
		fire.log.beans.ItemBean itemBean = new fire.log.beans.ItemBean(
				item.getItemId(), num, addnum);
		fire.log.YYLogger.OpItemGetLog(roleId, itemBean, counterType);
		
		Module.logger.info(new StringBuffer().append("roleId:").append(roleId)
				.append(" 添加物品:").append(item.getItemId()).append(" 数量:").append(num).append(" 添加后数量:").append(addnum)
				.append(" 类型:").append(counterType).append(" 理由:")
				.append(reason));
		
		return AddItemResult.SUCC;
	}

	private boolean doAddItem2AutoPos(final ItemBase ib,
			final boolean sendMsg, final fire.log.enums.YYLoggerTuJingEnum logtype) {
		if (conf.canpile == 1) {
			doDoStack(ib, 0, getCapacity() - 1, logtype);
		}
		if (ib.getNumber() > 0) {
			List<Integer> freeposes = getFreepos();
			if (freeposes.isEmpty())
				return false;
			if (!doAddItem2Pos(ib, freeposes.get(0), sendMsg, logtype))
				return false;
		}
		return true;
	}

	private boolean doAddItem2Pos(final ItemBase ib, final int pos,
			final boolean sendMsg, final fire.log.enums.YYLoggerTuJingEnum logtype) {
		if (pos < 0 || pos >= getCapacity())
			return false;
		ib.getDataItem().setPosition(pos);
		int key = this.doPushItem(ib.getDataItem());
		if (key == 0)
			return false;
		final long oldowner = ib.getOwnerid();

		ib.setKey(key);
		ib.setOwnerid(roleId);
		ib.setPackId(getPackid());

		if (oldowner == 0) {
			ib.onInsert();
		}

		if (sendMsg) {
			SAddItem send = new SAddItem();
			send.packid = getPackid();
			send.data.add(xItem2Item(ib.itemData, key,
					roleId == oldowner ? 0 : 1));
			mkdb.Transaction.tsendWhileCommit(roleId, send);
		}

		return true;
	}

	private boolean doAddItem2Pos(final ItemBase bi, final int pos,
			final fire.log.enums.YYLoggerTuJingEnum logtype, final boolean sendProcotol) {
		return doAddItem2Pos(bi, pos, sendProcotol, logtype);
	}

	protected long addMoney(final long money, final String reason, final YYLoggerTuJingEnum way) {
		if (readonly)
			return 0;
		long res = (Long.MAX_VALUE - getMoney() <= money) ? Long.MAX_VALUE
				: money + getMoney();
		if (res < 0) {
			Module.logger.error("金钱不足:" + roleId);
			return 0;
		} else if (res > conf.maxmoney) {
			return 0;
		}
		long oldvalue = getCurrency(MoneyType.MoneyType_SilverCoin);
		pack.getCurrency().put(MoneyType.MoneyType_SilverCoin, res);
		this.notifyMoney(MoneyType.MoneyType_SilverCoin);
		long realadd = res - oldvalue;
		return realadd;
	}

	protected boolean doList() {
		if (readonly)
			return false;
		Map<Integer, Integer> backup = collectItemInfo();
		final java.util.Set<Integer> toRemove = new java.util.TreeSet<Integer>();
		final java.util.Set<Integer> toIngore = new java.util.TreeSet<Integer>();
		for (final Map.Entry<Integer, xbean.Item> item : pack.getItems()
				.entrySet()) {
			if (toIngore.contains(item.getKey())
					|| toRemove.contains(item.getKey()))
				continue;

			final fire.pb.item.ItemShuXing attr = Module.getInstance()
					.getItemManager().getAttr(item.getValue().getId());
			if (getItemNum(item.getValue()) >= attr.maxstack) {
				toIngore.add(item.getKey());
				continue;
			}

			for (final Map.Entry<Integer, xbean.Item> item2 : pack.getItems()
					.entrySet()) {
				if (toIngore.contains(item2.getKey())
						|| toRemove.contains(item2.getKey()))
					continue;
				if (item2.getKey() == item.getKey()
						|| !isStack(item2.getValue(), item.getValue(), 0))
					continue;
				final int pilednumber = Math.min(
						attr.maxstack - getItemNum(item.getValue()),
						getItemNum(item2.getValue()));
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
			xbean.Item xitem = pack.getItems().get(key);
			if (xitem != null) {
				doDelete(key, true);
			}

		}
		sort();
		Map<Integer, Integer> after = collectItemInfo();
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
		final SGetPackInfo res = new SGetPackInfo();
		res.packid = getPackid();
		res.baginfo = this.getPackInfo();
		mkdb.Procedure.psendWhileCommit(roleId, res);

		return true;
	}
	
	protected boolean doListquest() {
		if (readonly)
			return false;
		Map<Integer, Integer> backup = collectItemInfo();
		final java.util.Set<Integer> toRemove = new java.util.TreeSet<Integer>();
		final java.util.Set<Integer> toIngore = new java.util.TreeSet<Integer>();
		for (final Map.Entry<Integer, xbean.Item> item : pack.getItems().entrySet()) {
			if (toIngore.contains(item.getKey()) || toRemove.contains(item.getKey()))
				continue;

			final fire.pb.item.ItemShuXing attr = Module.getInstance()
					.getItemManager().getAttr(item.getValue().getId());
			if (getItemNum(item.getValue()) >= attr.maxstack) {
				toIngore.add(item.getKey());
				continue;
			}

			for (final Map.Entry<Integer, xbean.Item> item2 : pack.getItems()
					.entrySet()) {
				if (toIngore.contains(item2.getKey())
						|| toRemove.contains(item2.getKey()))
					continue;
				if (item2.getKey() == item.getKey()
						|| !isStack(item2.getValue(), item.getValue(), 0))
					continue;
				final int pilednumber = Math.min(
						attr.maxstack - getItemNum(item.getValue()),
						getItemNum(item2.getValue()));
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
			xbean.Item xitem = pack.getItems().get(key);
			if (xitem != null) {
				doDelete(key, true);
			}

		}
		sortquest();
		Map<Integer, Integer> after = collectItemInfo();
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
		final SGetPackInfo res = new SGetPackInfo();
		res.packid = getPackid();
		res.baginfo = this.getPackInfo();
		mkdb.Procedure.psendWhileCommit(roleId, res);

		return true;
	}

	public void clear() {
		List<Integer> keys = new ArrayList<Integer>();
		for (ItemBase bi : this) {
			keys.add(bi.getKey());
			bi.onDelete(YYLoggerTuJingEnum.CLEAR);
		}
		for (int key : keys) {
			doDelete(key, true);
		}
	}

	private Map<Integer, Integer> collectItemInfo() {
		Map<Integer, Integer> iteminfo = new HashMap<Integer, Integer>();
		for (xbean.Item item : pack.getItems().values()) {
			Integer num = iteminfo.get(item.getId());
			if (num == null) {
				iteminfo.put(item.getId(), getItemNum(item));
			} else {
				iteminfo.put(item.getId(), getItemNum(item) + num);
			}
		}
		return iteminfo;
	}

	public int getItemNum(int itemid, int flag) {
		int count = 0;
		final Map<Integer, xbean.Item> items = pack.getItems();
		if (null == items)
			return -1;
		for (final xbean.Item e : items.values()) {
			if (itemid == e.getId() && (e.getFlags() & flag) == 0) {
				count += getItemNum(e);
			}
		}
		return count;
	}

	public int getItemNumExpStall(int itemid) {
		int count = 0;
		if (getPackid() != BagTypes.BAG)
			count = getItemNum(itemid, 0);
		else {
			final Map<Integer, xbean.Item> items = pack.getItems();
			if (null == items)
				count = -1;
			else {
				Iterator<Integer> iter = items.keySet().iterator();
				while (iter.hasNext()) {
					int keyinpack = iter.next();
					ItemBase basicitem = getItem(keyinpack);
					xbean.Item e = items.get(keyinpack);
					if (itemid == basicitem.getItemId()
							&& (basicitem.getFlags() & fire.pb.Item.ONSTALL) == 0)
						count += getItemNum(e);
				}
			}
		}
		return count;
	}

	public List<Integer> getItemNumExpStall(int itemid, int bind,
			List<Integer> keys) {
		if (getPackid() != BagTypes.BAG) {
			final Map<Integer, xbean.Item> items = pack.getItems();
			if (null == items)
				return keys;
			for (final Entry<Integer, xbean.Item> e : items.entrySet()) {
				if (itemid == e.getValue().getId()) {
					if ((e.getValue().getFlags() & Item.BIND) == bind)
						keys.add(e.getKey());
				}
			}
		} else {
			final Map<Integer, xbean.Item> items = pack.getItems();
			if (null == items)
				return keys;
			else {
				Iterator<Entry<Integer, xbean.Item>> iter = items.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry<Integer, xbean.Item> entry = iter.next();
					int keyinpack = entry.getKey();
					ItemBase basicitem = getItem(keyinpack);
					if (itemid == basicitem.getItemId()
							&& (basicitem.getFlags() & fire.pb.Item.ONSTALL) == 0)
						if ((entry.getValue().getFlags() & Item.BIND) == bind)
							keys.add(entry.getKey());
				}
			}
		}
		return keys;
	}

	protected int doPushItem(xbean.Item xi) {
		int nextid;
		while (true) {
			nextid = incNextId();
			if (nextid == 0)
				nextid = incNextId();
			if (null == pack.getItems().get(nextid)) {
				if (xi.getTimeout() > 0) {
				}

				pack.getItems().put(nextid, xi);
				if (xi.getUniqueid() == 0) {
					final long uniqueid = Module.getInstance()
							.getItemUniqueIdFactory().getUniqueId();
					xi.setUniqueid(uniqueid);
				}
				break;
			}
		}
		return nextid;
	}

	protected xbean.Item doDelete(int key, boolean removeuid) {
		return pack.getItems().remove(key);
	}

	public SRefreshNaiJiu getBagEndureInfo() {
		SRefreshNaiJiu ret = new SRefreshNaiJiu();
		ret.packid = getPackid();

		for (ItemBase bi : this) {
			if (bi == null)
				continue;
			if (bi.getPackId() != getPackid())
				continue;
			if (bi instanceof EquipItem) {
				EquipItem ei = (EquipItem) bi;

				EquipNaiJiu ee = new EquipNaiJiu();
				ee.keyinpack = ei.getKey();
				ee.endure = ei.getEndure();
				ret.data.add(ee);
			}
		}
		return ret;
	}

	protected abstract int getPackid();

	public fire.pb.Bag getPackInfo() {
		final fire.pb.Bag ret = new fire.pb.Bag();
		SRefreshNaiJiu send = new SRefreshNaiJiu();
		send.packid = getPackid();
		for (ItemBase bi : this) {
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

		for (Map.Entry<Integer, Long> currency : pack.getCurrency().entrySet()) {
			ret.currency.put(currency.getKey().byteValue(), currency.getValue());
		}
		
		if (!send.data.isEmpty()) {
			if (mkdb.Transaction.current() != null)
				mkdb.Procedure.psendWhileCommit(roleId, send);
			else
				gnet.link.Onlines.getInstance().send(roleId, send);
		}

		return ret;
	}

	public int getCapacity() {
		return this.pack.getCapacity();
	}

	public long getContribution() {
		if (!pack.getCurrency().containsKey(MoneyType.MoneyType_ProfContribute))
			return 0;

		return pack.getCurrency().get(MoneyType.MoneyType_ProfContribute);
	}
	public long getCreditPoint() {
		if (!pack.getCurrency().containsKey(MoneyType.MoneyType_EreditPoint))
			return 0;

		return pack.getCurrency().get(MoneyType.MoneyType_EreditPoint);
	}
	
	public long getCurrency(int currencyType) {
		if (!pack.getCurrency().containsKey(currencyType))
			return 0;

		return pack.getCurrency().get(currencyType);
	}

	private int getFirstFreePos(int page) {
		if (isFull())
			return -1;
		final int startpos = (page - 1) * Commontext.BAG_PAGE_SIZE;
		final int endpos = page * Commontext.BAG_PAGE_SIZE - 1;
		int fpos = Integer.MAX_VALUE;
		for (int pos : getFreepos()) {
			if (pos >= startpos && pos <= endpos && fpos > pos)
				fpos = pos;
		}
		if (fpos == Integer.MAX_VALUE)
			return -1;
		return fpos;
	}

	public java.util.ArrayList<Integer> getFreepos() {
		java.util.ArrayList<Integer> frees = new java.util.ArrayList<Integer>();
		for (int i = 0; i < this.getCapacity(); i++) {
			frees.add(i);
		}
		for (xbean.Item xi : pack.getItems().values()) {
			frees.remove((Integer) xi.getPosition());
		}
		Collections.sort(frees);
		return frees;
	}

	public long getGold() {
		if (!pack.getCurrency().containsKey(MoneyType.MoneyType_GoldCoin))
			return 0;

		return pack.getCurrency().get(MoneyType.MoneyType_GoldCoin);
	}

	public ItemBase getItem(int key) {
		xbean.Item item = pack.getItems().get(key);
		if (item == null)
			return null;

		return toBasicItem(item, key);
	}

	public ItemBase getItemByPos(int pos) {
		Map.Entry<Integer, xbean.Item> item = getXitemByPos(pos);
		if (item == null)
			return null;
		return toBasicItem(item.getValue(), item.getKey());
	}

	public String getItemLogsByKeys(List<Integer> keyinpacks) {
		StringBuilder sb = new StringBuilder();
		for (int keyinpack : keyinpacks) {
			ItemBase item = getItem(keyinpack);
			if (item == null)
				continue;
			sb.append(item.getItemLog());
		}
		return sb.toString();
	}

	public long getMaxMoney() {
		return conf.maxmoney;
	}

	public long getMoney() {
		if (!pack.getCurrency().containsKey(MoneyType.MoneyType_SilverCoin))
			return 0;

		return pack.getCurrency().get(MoneyType.MoneyType_SilverCoin);
	}

	public int getRemainSize() {
		return getCapacity() - size();
	}

	private Map.Entry<Integer, xbean.Item> getXitemByPos(int pos) {
		if (pos < 0)
			return null;
		for (Map.Entry<Integer, xbean.Item> item : pack.getItems().entrySet()) {
			if (item.getValue().getPosition() == pos)
				return item;
		}
		return null;
	}

	protected int incNextId() {
		final int id = pack.getNextid() + 1;
		if (id < 0)
			throw new RuntimeException("生成key出错");
		pack.setNextid(id);
		return id;
	}

	protected boolean isBind(xbean.Item xi) {
		return (xi.getFlags() & fire.pb.Item.BIND) == fire.pb.Item.BIND;
	}

	public boolean isEmpty() {
		return pack.getItems().size() == 0;
	}

	public boolean isFull() {
		return size() >= getCapacity();
	}

	public boolean isPosFree(int pos) {
		if (pos < 0 || pos > getCapacity())
			return false;
		for (xbean.Item item : pack.getItems().values()) {
			if (item.getPosition() == pos)
				return false;
		}
		return true;
	}

	@Override
	public Iterator<ItemBase> iterator() {
		return new PackIterator();
	}

	protected final boolean TransIn(final ItemBase ib, final int pos) {
		return TransIn(ib, pos, null);
	}

	protected boolean TransIn(final ItemBase ib, final int pos,
			final ItemBase dstitem) {
		if (pos == -1) {
			return doAddItem2AutoPos(ib, true, YYLoggerTuJingEnum.GENERAL);
		} else {
			if (dstitem != null && dstitem.getPosition() == pos) {
				stackItem(dstitem, ib.itemData.getNumber(),
						ib.itemData.getNumbermap(), YYLoggerTuJingEnum.GENERAL);
				return true;
			} else
				return doAddItem2Pos(ib, pos, YYLoggerTuJingEnum.GENERAL, true);
		}
	}

	protected boolean TranItemToPage(ItemBase item, int page) {
		if (conf.canpile == 1) {
			final int startpos = (page - 1) * Commontext.BAG_PAGE_SIZE;
			final int endpos = page * Commontext.BAG_PAGE_SIZE - 1;
			this.doDoStack(item, startpos, endpos);
		}
		if (item.getNumber() > 0) {
			final int xpos = getFirstFreePos(page);
			if (xpos == -1) {
				return false;
			}
			if (!doAddItem2Pos(item, xpos, YYLoggerTuJingEnum.GENERAL, true))
				return false;
		}
		return true;
	}

	public ItemBase TransOut(final int key, final int number,
			final String reason) {
		xbean.Item xi = pack.getItems().get(key);
		if (xi == null)
			return null;
		final xbean.Item removed;
		final int itemnum = getItemNum(xi);
		if (number > itemnum) {
			return null;
		}
		if (number == -1 || number == itemnum) {
			removed = doDelete(key, false);
			SDelItem send = new SDelItem();
			send.packid = getPackid();
			send.itemkey = key;
			mkdb.Transaction.tsendWhileCommit(roleId, send);
		} else {
			removed = xi.copy();
			Map<Integer, Integer> splitnum = doCut(xi, number);
			if (getItemNum(xi) <= 0) {
				throw new RuntimeException("物品数量有错误");
			}
			SItemNumChange send = new SItemNumChange();
			send.packid = getPackid();
			send.curnum = getItemNum(xi);
			send.keyinpack = key;
			mkdb.Transaction.tsendWhileCommit(roleId, send);
			removed.setNumber(0);
			removed.getNumbermap().clear();
			stackNumber(splitnum, removed);
			removed.setUniqueid(0);
		}
		
		ItemBase item = Module.getInstance().getItemManager()
				.toItemBase(removed, roleId, getPackid(), key);
		return item;
	}

	protected void notifyMoney(final int type) {
		final SRefreshCurrency refmoney = new SRefreshCurrency();
		refmoney.packid = getPackid();

		refmoney.currency.put((byte) type, getCurrency(type));
		mkdb.Procedure.psendWhileCommit(roleId, refmoney);
	}

	private int doDoStack(final int itemid, final int num,
			final int numbertype, final int initflag, final int maxadd,
			final fire.log.enums.YYLoggerTuJingEnum logtype) {
		int added = 0;
		for (ItemBase bi : this) {
			if (added >= maxadd) {
				return added;
			}
			int flag = initflag == -1 ? bi.getIniFlag() : initflag;
			if (bi.getItemId() == itemid && bi.getFlags() == flag) {
				if (isStack(bi.getDataItem())) {
					int maxpile = bi.getItemAttr().maxstack - bi.getNumber();
					if (maxpile <= 0)
						continue;
					maxpile = Math.min(maxpile, num - added);
					int pilenum = Math.min(maxadd - added, maxpile);

					if (numbertype == 0) {
						stackItem(bi, pilenum, null, logtype);
					} else {
						Map<Integer, Integer> numbermap = new HashMap<Integer, Integer>();
						numbermap.put(numbertype, pilenum);
						stackItem(bi, 0, numbermap, logtype);
					}
					added += pilenum;
				}
			}
		}
		return added;
	}

	private void doDoStack(final ItemBase bi, final int minpos,
			final int maxpos) {
		doDoStack(bi, minpos, maxpos, YYLoggerTuJingEnum.GENERAL);
	}

	private void doDoStack(final ItemBase bi, final int minpos,
			final int maxpos, final fire.log.enums.YYLoggerTuJingEnum countertype) {
		int maxheap = bi.getItemAttr().maxstack;
		if (maxheap <= 1)
			return;
		for (Map.Entry<Integer, xbean.Item> xitem : pack.getItems().entrySet()) {
			if (xitem.getValue().getPosition() < minpos
					|| xitem.getValue().getPosition() > maxpos)
				continue;
			if (isStack(bi.getDataItem(), xitem.getValue(), 0)) {
				int heapnum = Math.min(bi.getNumber(), maxheap
						- getItemNum(xitem.getValue()));
				if (heapnum <= 0)
					continue;
				doStack(bi.getDataItem(), xitem.getValue(), heapnum);
				final SItemNumChange send = new SItemNumChange();
				send.packid = getPackid();
				send.curnum = getItemNum(xitem.getValue());
				send.keyinpack = xitem.getKey();
				mkdb.Transaction.tsendWhileCommit(roleId, send);

				if (bi.getDataItem().getMarkettime() > 0) {
					final long oldowner = bi.getOwnerid();

					SAddItem addItem = new SAddItem();
					addItem.packid = getPackid();
					addItem.data.add(xItem2Item(xitem.getValue(),
							xitem.getKey(), roleId == oldowner ? 0 : 1,
							getItemNum(xitem.getValue())));
					mkdb.Procedure.psendWhileCommit(roleId, addItem);
				}

				if (bi.getNumber() <= 0)
					break;
			}
		}
		if (bi.getNumber() <= 0 && bi.getPackId() == getPackid()
				&& bi.getOwnerid() == roleId) {
			doDelete(bi.getKey(), true);
			SDelItem send = new SDelItem();
			send.packid = bi.getPackId();
			send.itemkey = bi.getKey();
			mkdb.Procedure.psendWhileCommit(roleId, send);
		}
	}

	protected void stackItem(final ItemBase ib, final int num,
			final Map<Integer, Integer> nummap, final fire.log.enums.YYLoggerTuJingEnum countertype) {
		assert (ib.getPackId() == getPackid());
		assert (ib.getOwnerid() == roleId);

		ib.itemData.setNumber(ib.itemData.getNumber() + num);
		int addnum = num;
		if (nummap != null) {
			for (Map.Entry<Integer, Integer> e : nummap.entrySet()) {
				Integer oldnum = ib.itemData.getNumbermap()
						.get(e.getKey());
				if (oldnum == null) {
					ib.itemData.getNumbermap().put(e.getKey(),
							e.getValue());
				} else {
					ib.itemData.getNumbermap().put(e.getKey(),
							e.getValue() + oldnum);
				}
				addnum += e.getValue();
			}
		}
		if (addnum != 0) {
			SItemNumChange modnum = new SItemNumChange();
			modnum.packid = getPackid();
			modnum.curnum = ib.getNumber();
			modnum.keyinpack = ib.getKey();
			mkdb.Procedure.psendWhileCommit(roleId, modnum);
		}

	}

	private boolean processDoAddItem(final ItemBase ib, final int p,
			final fire.log.enums.YYLoggerTuJingEnum logtype) {
		if (p == -1) {
			return doAddItem2AutoPos(ib, true, logtype);
		} else {
			ItemBase dstitem = getItemByPos(p);
			if (dstitem == null) {
				if (!doAddItem2Pos(ib, p, logtype, true))
					return false;

			} else {
				if (!isStack(ib.itemData, dstitem.itemData, 0))
					return false;
				int maxpilenum = dstitem.getItemAttr().maxstack - dstitem.getNumber();
				if (maxpilenum < ib.getNumber())
					return false;
				stackItem(dstitem, ib.getDataItem().getNumber(), ib
						.getDataItem().getNumbermap(), logtype);
			}
		}
		return true;
	}

	protected void psendMsgNotify(int msgId, List<String> parameters) {
		fire.pb.talk.MessageMgr.psendMsgNotify(roleId, msgId, parameters);
	}

	public boolean cleanAllItemFlag(int flag) {
		if (readonly)
			return false;
		java.util.Iterator<Integer> iter = pack.getItems().keySet().iterator();
		while (iter.hasNext()) {
			xbean.Item xi = pack.getItems().get(iter.next());
			if (xi == null)
				return false;
			xi.setFlags(xi.getFlags() & ~flag);
		}
		return true;
	}

	public int cleanBindingItemById(int itemid, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		if (readonly)
			return 0;
		if (num <= 0)
			return 0;

		int leftnum = num;
		Map<Integer, Integer> removekeys = new HashMap<Integer, Integer>();
		for (ItemBase item : this) {
			if (leftnum <= 0)
				break;
			if (item == null) {
				continue;
			}
			if (item.getItemId() != itemid) {
				continue;
			}

			if (!isBind(item.getDataItem())) {
				continue;
			}

			int movenum = Math.min(leftnum, item.getNumber());
			leftnum -= movenum;
			removekeys.put(item.getKey(), movenum);
		}
		for (Map.Entry<Integer, Integer> removekey : removekeys.entrySet()) {
			removeItemWithKey(removekey.getKey(), removekey.getValue(),
					countertype, xiangguanid, reason);
		}
		
		return num - leftnum;
	}

	public boolean cleanFlag(int key, int flag, int packid) {
		if (readonly)
			return false;
		xbean.Item xi = pack.getItems().get(key);
		if (xi == null)
			return true;
		else {
			xi.setFlags(xi.getFlags() & ~flag);
			SItemSign ref = new SItemSign(key, xi.getFlags(),
					packid);
			mkdb.Procedure.psendWhileCommit(roleId, ref);
		}
		return true;
	}

	public int removeItemById(int itemid, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		return removeItemById(itemid, num, countertype, xiangguanid, reason,
				false, 0);
	}

	public int removeItemById(int itemid, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason, boolean isLock) {
		return removeItemById(itemid, num, countertype, xiangguanid, reason,
				isLock, 0);
	}

	public int removeItemById(int itemid, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason, boolean isLock, int isBind) {
		if (readonly)
			return 0;
		if (num <= 0)
			return 0;

		int leftnum = num;
		Map<Integer, Integer> removekeys = new HashMap<Integer, Integer>();
		boolean b = false;
		if (isBind == 1) {
			b = true;
		}
		for (ItemBase item : this) {
			if (leftnum <= 0)
				break;
			if (item == null) {
				continue;
			}
			if (item.getItemId() != itemid) {
				continue;
			}

			if (isBind != 0) {
				if (b && isBind(item.getDataItem()))
					continue;
			}

			int movenum = Math.min(leftnum, item.getNumber());
			leftnum -= movenum;
			removekeys.put(item.getKey(), movenum);
		}
		for (Map.Entry<Integer, Integer> removekey : removekeys.entrySet()) {
			removeItemWithKey(removekey.getKey(), removekey.getValue(),
					countertype, xiangguanid, reason);
		}
		if ((num - leftnum) > 0) {
			Module.logger.info(new StringBuffer().append("roleId:").append(roleId)
					.append(" 删除物品id:").append(itemid).append(" 数量:").append(num)
					.append(" 类型:").append(countertype).append(" 理由:")
					.append(reason));
		}
		
		return num - leftnum;
	}

	public int removeItemWithKey(int key, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		ItemBase bi = this.TransOut(key, num, reason);
		if (bi == null)
			return 0;
		List<Number> itemStrs = new ArrayList<Number>();
		itemStrs.add(bi.getItemId());
		itemStrs.add(bi.getNumber());
		itemStrs.add(bi.itemData.getUniqueid());
		bi.onDelete(countertype);

		int hasnum = getItemHasNum(roleId, bi.getItemId());

		fire.log.beans.ItemBean itemBean = new fire.log.beans.ItemBean(
				bi.getItemId(), num, hasnum);
		fire.log.YYLogger.OpItemUseLog(roleId, itemBean, countertype);

		Module.logger.info(new StringBuffer().append("roleId:").append(roleId)
				.append(" 删除物品id:").append(bi.getItemId()).append(" 数量:")
				.append(num).append(" 类型:").append(countertype).append(" 理由:")
				.append(reason));
		return bi.getNumber();
	}

	public int removeItemByPos(int pos, int num, YYLoggerTuJingEnum countertype,
			int xiangguanid, String reason) {
		if (readonly)
			return 0;
		for (ItemBase item : this) {
			if (item.itemData.getPosition() != pos) {
				continue;
			}
			return removeItemWithKey(item.getKey(), num, countertype,
					xiangguanid, reason);
		}
		return 0;
	}

	public int size() {
		return this.pack.getItems().size();
	}

	protected int sort() {
		if (readonly)
			return 0;
		int count = 0;
		final java.util.SortedSet<ItemBase> allItems = new java.util.TreeSet<ItemBase>(
				new ItemComparator());
		for (ItemBase item : this) {
			if (item.getPackId() != BagTypes.QUEST)
				allItems.add(item);
		}
		int pos = 0;
		for (final ItemBase i : allItems) {
			if (i.getPosition() != pos) {
				i.getDataItem().setPosition(pos);
				count++;
			}
			pos++;
		}
		return count;
	}
	
	protected int sortquest() {
		if (readonly)
			return 0;
		int count = 0;
		final java.util.SortedSet<ItemBase> allItems = new java.util.TreeSet<ItemBase>(
				new ItemComparator());
		for (ItemBase item : this) {
			if (item.getPackId() == BagTypes.QUEST)
				allItems.add(item);
		}
		int pos = 0;
		for (final ItemBase i : allItems) {
			if (i.getPosition() != pos) {
				i.getDataItem().setPosition(pos);
				count++;
			}
			pos++;
		}
		return count;
	}

	private ItemBase toBasicItem(xbean.Item item, int key) {
		return Module.getInstance().getItemManager()
				.toItemBase(item, roleId, getPackid(), key);
	}
	
	public final static fire.pb.Item transItemData2SendData(xbean.Item dstItem, int key, int isNew) {
		return xItem2Item(dstItem, key, isNew);
	}
}
