package fire.pb.item;

public interface UseItemHandler {
	public Commontext.UseResult onUse(final long roleId, final ItemBase bi, final int usednum);
}
