package fire.pb.item;

/**
 * 道具的部分操作
 * @author XGM
 */
public class ItemHelper {

	/**
	 * 设置归属角色
	 */
	public static void setOwnerid(ItemBase item, long roleId) {
		item.setOwnerid(roleId);
	}
}
