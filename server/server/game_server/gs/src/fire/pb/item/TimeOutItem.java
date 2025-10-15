package fire.pb.item;

import com.locojoy.base.Octets;

public class TimeOutItem extends ItemBase {
	private final ItemBase effectitem;
	TimeOutItem(ItemBase item) {
		super(Module.getInstance().getItemManager(), item.itemData);
		effectitem = item;
	}
	
	public ItemBase getEffectItem() {
		return effectitem;
	}

	@Override
	protected void onInserted() {
	}

	@Override
	protected void onDeleted() {
	}

	@Override
	public Octets getTips() {
		return effectitem.getTips();
	}
	
	@Override
	public void onTimeout() {
		effectitem.onTimeout();
	}
}
