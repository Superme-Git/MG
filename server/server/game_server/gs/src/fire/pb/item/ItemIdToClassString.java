package fire.pb.item;

import fire.pb.item.equip.ClothforallItem;

public class ItemIdToClassString {
	private class ItemClassToLoadWrap extends ItemNameClassConfig {
		public ItemClassToLoadWrap(final int id,
				final Class<? extends ItemBase> classtype) {
			this.id = id;
			this.classname = classtype.getName();
		}
	}

	java.util.NavigableMap<Integer, fire.pb.item.ItemNameClassConfig> itemclassMap = new java.util.TreeMap<Integer, fire.pb.item.ItemNameClassConfig>();

	ItemIdToClassString(fire.pb.main.ConfigManager cm) {
		if (instance == null) {
			instance = this;
		}
		itemclassMap = cm.getConf(fire.pb.item.ItemNameClassConfig.class);
		itemclassMap.put(0x38, new ItemClassToLoadWrap(0x38,
				ClothforallItem.class));
	}

	private static ItemIdToClassString instance;

	public static ItemIdToClassString getInstance() {
		return instance;
	}

	public String getItemClass(final int itemid) {
		ItemShuXing attr = Module.getInstance().getItemManager()
				.getAttr(itemid);
		if (attr == null)
			throw new RuntimeException("物品id=" + itemid + "属性为null");
		int itemtypeid = attr.getTypeid();
		String classname = itemclassMap.get(itemtypeid).getClassname();
		return classname;
	}
}
