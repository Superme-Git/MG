package fire.pb.item;

import java.util.Map;

public class GemItem extends ItemBase {
	public GemItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public GemItem(ItemMgr im, xbean.Item itemid) {
		super(im, itemid);
	}

	@Override
	public void onDeleted() {

	}

	@Override
	public void onInserted() {

	}

	public static void addGemProp(int itemid, Map<Integer, Float> finalAttrMap, boolean forScore) {
		gemItemShuXing attr = (gemItemShuXing) Module.getInstance().getItemManager().getAttr(itemid);
		if (attr == null)
			return;

		int nIndex = 0;
		for (Integer effect : attr.getEffectname()) {
			if (attr.getEffect().get(nIndex) > 0) {
				Float iniValue = finalAttrMap.get(effect + 1);
				if (iniValue == null) {
					finalAttrMap.put(effect + 1, (float) attr.getEffect().get(nIndex));
				} else {
					finalAttrMap.put(effect + 1, (float) attr.getEffect().get(nIndex) + iniValue);
				}
				nIndex++;
			}
		}
	}
}
