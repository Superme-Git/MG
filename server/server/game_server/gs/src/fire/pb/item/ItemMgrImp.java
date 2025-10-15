package fire.pb.item;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fire.pb.PropRole;
import fire.pb.item.EquipItem.EquipError;
import fire.pb.item.make.ItemMakeFactory;

import org.apache.log4j.Logger;

import xbean.Item;

public class ItemMgrImp implements ItemMgr, ItemMgrMXBean {
	public static class LiBao {
		public static final class Item {
			final boolean isBind;
			final int itemid;
			final int itemnum;

			public Item(final int itemid, final int itemnum,
					final boolean isBind) {
				this.itemid = itemid;
				this.itemnum = itemnum;
				this.isBind = isBind;
			}

			public int getItemid() {
				return itemid;
			}

			public int getItemnum() {
				return itemnum;
			}

			public boolean isBind() {
				return isBind;
			}

		}
		private static final class ItemComparator implements
				java.util.Comparator<ItemMgrImp.LiBao.Item> {
			final Map<Integer, ItemShuXing> attrMap;

			public ItemComparator(final Map<Integer, ItemShuXing> attrMap) {
				this.attrMap = attrMap;
			}

			@Override
			public int compare(ItemMgrImp.LiBao.Item o1,
					ItemMgrImp.LiBao.Item o2) {
				ItemShuXing attr = attrMap.get(o1.getItemid());
				final int sid1;
				if (attr == null) {
					sid1 = 0;
				} else
					sid1 = attr.sortid;
				attr = attrMap.get(o2.getItemid());
				final int sid2;
				if (attr == null) {
					sid2 = 0;
				} else
					sid2 = attr.sortid;
				if (sid1 < sid2)
					return -1;
				else if (sid1 > sid2)
					return 1;
				if (o1.getItemid() < o2.getItemid())
					return -1;
				else if (o1.getItemid() > o2.getItemid())
					return 1;
				if (o1.getItemnum() < o2.getItemnum())
					return -1;
				else if (o1.getItemnum() > o2.getItemnum())
					return 1;
				return 1;
			}

		}
		private final java.util.SortedSet<ItemMgrImp.LiBao.Item> items;
		private final long money;
		private final int moneyType;
		private final long moneyValue;

		private final int msgid;

		private final int rewardid;

		private final int fushi;
		private final int vipexp;		

		public LiBao(final LibaoConfig conf,
				final Map<Integer, ItemShuXing> attrMap) {
			money = conf.moneyaward1;
			msgid = conf.opennotice;
			if (conf.itemids != null
					&& conf.itemids.size() != conf.itemnums.size()) {
				throw new IllegalArgumentException("礼包id=" + conf.id + "参数配置错误");
			}

			items = new java.util.TreeSet<ItemMgrImp.LiBao.Item>(
					new ItemMgrImp.LiBao.ItemComparator(attrMap));
			if (conf.itemids != null) {
				for (int i = 0; i < conf.itemids.size(); i++) {
					if (conf.itembinds == null)
						throw new IllegalArgumentException("礼包id=" + conf.id
								+ "绑定参数配置错误");
					items.add(new ItemMgrImp.LiBao.Item(conf.itemids.get(i),
							conf.itemnums.get(i), conf.itembinds.get(i) == 1));
				}
			}
			fushi = conf.moneyaward2;
			vipexp = conf.vipexpaward;
			moneyType = conf.currencytype;
			moneyValue = conf.currencyvalue;
			this.rewardid = conf.awardtableid;
		}

		public Set<ItemMgrImp.LiBao.Item> getItems() {
			return items;
		}

		public long getMoney() {
			return money;
		}

		public int getMoneyType() {
			return moneyType;
		}

		public long getMoneyValue() {
			return moneyValue;
		}

		public int getMsgid() {
			return msgid;
		}

		public int getRewardid() {
			return rewardid;
		}

		public int getFuShi() {
			return fushi;
		}

		public int getVipExp() {
			return vipexp;
		}
		
	}
	private static class LiBaoKey {
		private final int presentid;
		private final int role;
		private final int sex;
		private final int school;

		public LiBaoKey(final int presentid, final int sex, final int role, final int school) {
			this.presentid = presentid;
			this.sex = sex;
			this.role = role;
			this.school = school;
		}

		public LiBaoKey(final LibaoConfig conf) {
			sex = conf.sextype;
			presentid = conf.libaoid;
			role = conf.roletype;
			school = conf.school;
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof LiBaoKey) {
				final LiBaoKey pk = (LiBaoKey) o;
				return pk.sex == sex && pk.presentid == presentid
						&& pk.role == role && pk.school == school;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return (presentid << 10) + (role << 3) + sex;
		}
	}
	static private final Logger logger = Logger.getLogger("ITEM");
	Map<Integer, Set<Integer>> card2Item = new HashMap<Integer, Set<Integer>>();
	Map<Integer, BagConfig> packCfg;
	
	Map<Integer, Integer> depotExtMap = new HashMap<Integer, Integer>();
	Set<Integer> depotNotItems = new HashSet<Integer>();
	java.util.NavigableMap<Integer, SEquipNaiJiuXiaoHao> endureMap = new java.util.TreeMap<Integer, SEquipNaiJiuXiaoHao>();
	java.util.NavigableMap<Integer, ItemShuXing> itemAttrMap = new java.util.TreeMap<Integer, ItemShuXing>();
	java.util.NavigableMap<Integer, SItemBuff> itemBuffMap = new java.util.TreeMap<Integer, SItemBuff>();
	java.util.Map<LiBaoKey, LiBao> libaoConfMap = new java.util.HashMap<LiBaoKey, LiBao>();

	Map<Integer, Integer> packExtMap = new HashMap<Integer, Integer>();

	java.util.Set<Integer> sendToRolePackIds;

	@SuppressWarnings("unchecked")
	ItemMgrImp(fire.pb.main.ConfigManager cm) {
		java.util.Map<Integer, ItemClassConfig> config = null;
		if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
			java.util.Map<Integer, DItemClassConfig> dItemClassConfig = cm.getConf(fire.pb.item.DItemClassConfig.class);
			config = new java.util.TreeMap<Integer, ItemClassConfig>(dItemClassConfig);
		} else {
			config = cm.getConf(fire.pb.item.ItemClassConfig.class);
		}
		
		for (final fire.pb.item.ItemClassConfig c : config.values()) {
			try {
				Class<?> itemclass = Class.forName(c.classname);
				for (final ItemShuXing attr : ((java.util.Map<Integer, ? extends ItemShuXing>) cm.getConf(itemclass)).values()) {
					if (itemAttrMap.put(attr.id, attr) != null) {
						throw new RuntimeException("在'" + c.classname + "'中存在已有的id:" + attr.id);
					}
				}
			} catch (final ClassCastException ex) {
				throw new RuntimeException("'" + c.classname + "'不是物品类");
			} catch (final ClassNotFoundException ex) {
				throw new RuntimeException("'" + c.classname + "'不是物品类");
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}

		}
		logger.info("物品信息载入完毕,共" + itemAttrMap.size() + "个");

		itemBuffMap = cm.getConf(fire.pb.item.SItemBuff.class);
		packCfg = cm.getConf(fire.pb.item.BagConfig.class);
		sendToRolePackIds = new java.util.TreeSet<Integer>();
		for (BagConfig bagConf : packCfg.values()) {
			if (bagConf.loginsend == 1)
				sendToRolePackIds.add(bagConf.id);
		}
		endureMap = cm.getConf(fire.pb.item.SEquipNaiJiuXiaoHao.class);
		
		if (fire.pb.fushi.Module.GetPayServiceType() == 0) {
			final java.util.NavigableMap<Integer, SLibaoConfig> pConfMap = cm.getConf(fire.pb.item.SLibaoConfig.class);
			libaoConfMap.clear();
			for (final SLibaoConfig pc : pConfMap.values()) {
				if (libaoConfMap.put(new LiBaoKey(pc), new LiBao(pc, itemAttrMap)) != null) {
					Module.logger.error("重复的礼包配置 " + pc.getId());
				}
			}
		}
		else if (fire.pb.fushi.Module.GetPayServiceType() == 1) {
			final java.util.NavigableMap<Integer, SDianKaLibaoConfig> pConfMap = cm.getConf(fire.pb.item.SDianKaLibaoConfig.class);
			libaoConfMap.clear();
			for (final SDianKaLibaoConfig pc : pConfMap.values()) {
				if (libaoConfMap.put(new LiBaoKey(pc), new LiBao(pc, itemAttrMap)) != null) {
					Module.logger.error("重复的礼包配置 " + pc.getId());
				}
			}
		}

		Map<Integer, SChangKuExt> depotBuyMap = cm.getConf(SChangKuExt.class);
		depotExtMap.clear();
		for (SChangKuExt table : depotBuyMap.values()) {
			depotExtMap.put(table.getHaveCount(), table.getNeedyinbi());
		}

		Map<Integer, SParkExtTable> bagBuyMap = cm.getConf(SParkExtTable.class);
		packExtMap.clear();
		for (SParkExtTable table : bagBuyMap.values()) {
			packExtMap.put(table.getHaveCount(), table.getNeedyinbi());
		}

		depotNotItems.clear();
		Map<Integer, SCanntInChangKu> limititemMap = cm
				.getConf(SCanntInChangKu.class);
		for (Integer itemid : limititemMap.keySet()) {
			depotNotItems.add(itemid);
		}
	}

	@Override
	public int addItemToPack(final long roleId, final int packid,
			final int itemid, final int number, final int availtime,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		return addItemToPack(roleId, packid, itemid, number, availtime,
				countertype, xiangguanid, "test");
	}

	@Override
	public int addItemToPack(final long roleId, final int packid,
			final int itemid, final int number, final int availtime,
			final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid, final String reason) {
		final int[] ret = new int[1];
		ret[0] = -1;
		try {
			final mkdb.Procedure p = new mkdb.Procedure() {

				@Override
				protected boolean process() throws Exception {
					Pack bag = new Pack(roleId, false);
					bag.doAddItem(itemid, number, "", countertype, xiangguanid);
					return true;
				}

			};
			if (mkdb.Transaction.current() != null)
				p.call();
			else
				p.submit();
		} catch (final Exception ex) {
		}
		return ret[0];
	}

	public void equipAllBest(final long roleId) {
		PropRole prole = new PropRole(roleId, false);
		int roleLevel = prole.getLevel();
		int rolesex = prole.getSex();
		int shape = prole.getShape();
		int school = prole.getSchool();

		int needlevel = (roleLevel / 10) * 10;
		java.util.List<Integer> poses = new ArrayList<Integer>();
		for (int i = EquipItemType.ARMS; i <= EquipItemType.TIRE; i++)
			poses.add(i);
		Pack bag = (Pack) fire.pb.item.Module.getInstance().getItemMaps(
				roleId, BagTypes.BAG, false);
		for (ItemShuXing attr : itemAttrMap.values()) {
			if (poses.size() == 0)
				break;
			List<Integer> freepos = bag.getFreepos();
			if (freepos.size() == 0)
				break;
			if (attr instanceof EquipItemShuXing) {
				EquipItemShuXing equipattr = (EquipItemShuXing) attr;
				if (equipattr.needlevel != needlevel)
					continue;
				final Equip equips = new Equip(roleId, false);

				EquipItem equipitem = (EquipItem) fire.pb.item.Module.getInstance().getItemManager().genItemBase(attr.id, 1);
				if (equipitem == null)
					continue;

				EquipItem olditem = (EquipItem) equips.getItemByPos(equipitem
						.getEquipPos());
				if (olditem != null && olditem.getItemAttr().getLevel() == needlevel)
					continue;

				if (equipitem.canEquipment(equipitem.getEquipPos(), roleLevel,
						rolesex, shape, school) != EquipError.NO_ERROR)
					continue;
				equipitem.setEndure(equipitem.getCurMaxEndure());
				int pos = freepos.get(0);
				bag.doAddItem(equipitem, freepos.get(0), "GM add", fire.log.enums.YYLoggerTuJingEnum.GM, 0);

				ItemBase item = bag.getItemByPos(pos);
				new PPutOnEquip(roleId, item.getKey(), equipitem.getEquipPos())
						.call();
				poses.remove((Integer) (equipitem.getEquipPos()));
			}
		}
	}

	@Override
	public ItemBase genItemBase(int itemid, int num)
			throws IllegalArgumentException {
		return this.genItemBase(itemid, num, null);
	}

	@Override
	public ItemBase genItemBase(int itemid, int num, int numtype)
			throws RuntimeException, IllegalArgumentException {
		return genItemBase(itemid, num, numtype, null, true);
	}

	@Override
	public ItemBase genItemBase(final int itemid, final int num, int numtype,
			mkdb.Bean extinfo, boolean calcScore) throws RuntimeException,
			IllegalArgumentException {
		final String itemclassname = ItemIdToClassString.getInstance()
				.getItemClass(itemid);
		Module.logger.debug("生成" + itemclassname + "物品对象");
		ItemBase item;
		try {
			Constructor<?> constructor;
			if (extinfo != null) {
				constructor = Class.forName(itemclassname)
						.getDeclaredConstructor(ItemMgr.class, int.class, mkdb.Bean.class);
				constructor.setAccessible(true);
				item = (ItemBase) constructor.newInstance(this, itemid, extinfo);
			} else {
				constructor = Class.forName(itemclassname).getDeclaredConstructor(ItemMgr.class, int.class);
				constructor.setAccessible(true);
				item = (ItemBase) constructor.newInstance(this, itemid);
			}
		} catch (Exception e) {
			mkdb.Trace.error("物品:" + e.toString());
			return null;
		}

		item.setFlag(item.getIniFlag());
		if (numtype == 0) {
			item.getDataItem().setNumber(num);
		} else if (numtype == 1) {
			item.getDataItem().getNumbermap().put(1, num);
		} else if (numtype == 2) {
			item.getDataItem().getNumbermap().put(2, num);
		} else {
			throw new IllegalArgumentException("物品log日志类型异常");
		}
		if (item instanceof EquipItem) {
			EquipItem eItem = (EquipItem) item;
			int endure = ((EquipItemShuXing) eItem.getItemAttr()).maxnaijiu;
			eItem.getEquipAttr().setEndure(endure);
			eItem.getEquipAttr().setCurmaxendure(endure);
			eItem.getEquipAttr().setMaxendure(endure);

			ItemMakeFactory.getFactory().genItem(eItem);

			int score = fire.pb.item.Module.getInstance()
					.getEquipScore(item);
			eItem.getEquipAttr().setEquipscore(score);
			if (score >= eItem.getItemAttr().getTreasureScore()) {
				eItem.getEquipAttr().setTreasure(1);
			} else
				eItem.getEquipAttr().setTreasure(0);

		}

		return item;
	}

	@Override
	public ItemBase genItemBase(int itemid, int num, mkdb.Bean extinfo)
			throws RuntimeException, IllegalArgumentException {
		return genItemBase(itemid, num, 0, extinfo, true);
	}

	@Override
	public ItemShuXing getAttr(int itemid) {
		return itemAttrMap.get(itemid);
	}

	public java.util.NavigableMap<Integer, ItemShuXing> getAttrMap() {
		return itemAttrMap;
	}

	@Override
	public BagConfig getPackCfg(int packid) {
		return packCfg.get(packid);
	}

	public int getBuyPackYinBi(int hasGridNums) {
		if (hasGridNums <= 0)
			return 0;
		if (hasGridNums % 5 != 0)
			hasGridNums = hasGridNums - hasGridNums % 5;
		Integer yinbi = packExtMap.get(hasGridNums);
		if (yinbi == null)
			return 0;
		return yinbi;
	}

	@Override
	public int getBuyDepotMoney(int hasGridNums) {
		if (hasGridNums <= 0)
			return 0;
		if (hasGridNums % 5 != 0)
			hasGridNums = hasGridNums - hasGridNums % 5;
		Integer money = depotExtMap.get(hasGridNums);
		if (money == null)
			return 0;
		return money;
	}

	@Override
	public SItemBuff getItemBuff(int itemid) {
		return itemBuffMap.get(itemid);
	}

	@Override
	public Set<Integer> getItemByCard(final int cardid) {
		return card2Item.get(cardid);
	}

	@Override
	public HashMap<Integer, fire.pb.Bag> getLoginPackInfo(long roleId) {
		final HashMap<Integer, fire.pb.Bag> ret = new HashMap<Integer, fire.pb.Bag>();
		for (final Integer packid : sendToRolePackIds) {
			ItemMaps ic = Module.getInstance().getItemMaps(roleId, packid, true);
			if (ic != null) {
				final fire.pb.Bag bag = ic.getPackInfo();
				ret.put(packid, bag);
			}
		}
		return ret;
	}

	@Override
	public SEquipNaiJiuXiaoHao getLoseNaiJiu(int pos) {
		return endureMap.get(pos);
	}

	@Override
	public LiBao getLiBao(final int present, final int sex, final int shape, final int school) {
		LiBao ret = libaoConfMap.get(new LiBaoKey(present, sex, shape, school));
		if (ret != null)
			return ret;
		ret = libaoConfMap.get(new LiBaoKey(present, 0, shape, school));
		if (ret != null)
			return ret;
		ret = libaoConfMap.get(new LiBaoKey(present, sex, 0, school));
		if (ret != null)
			return ret;
		ret = libaoConfMap.get(new LiBaoKey(present, 0, 0, school));
		if (ret != null)
			return ret;
		
		return libaoConfMap.get(new LiBaoKey(present, 0, 0, 0));
	}

	@Override
	public ItemBase toItemBase(Item i) {
		return this.toItemBase(i, 0, BagTypes.EMPTY, -1);
	}

	@Override
	public ItemBase toItemBase(xbean.Item i, long roleId, int packid, int key) {
		int itemid = i.getId();
		try {
			String itemclassname = ItemIdToClassString.getInstance().getItemClass(itemid);
			java.lang.reflect.Constructor<?> constructor = Class.forName(
					itemclassname).getDeclaredConstructor(ItemMgr.class,
					xbean.Item.class);
			constructor.setAccessible(true);
			ItemBase item = (ItemBase) constructor.newInstance(this, i);
			if (item.isTimeout()) {
				item.packid = packid;
				item.roleid = roleId;
				item.keyinpack = key;
				item = new TimeOutItem(item);
			}
			item.packid = packid;
			item.roleid = roleId;
			item.keyinpack = key;
			return item;
		} catch (Exception e) {
			mkdb.Trace.error("生成物品" + itemid + "出错:" + e.toString());
			return null;
		}
	}
}
