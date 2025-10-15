
package fire.pb.event;

/**
 * 删除物品事件
 */
public class DeleteItemEvent implements Event {
	private long roleId;
	private int itemId;
	private int removeNum;

	public static void init() {
	}

	public DeleteItemEvent(long roleId, int itemId,int itemNum) {
		this.roleId = roleId;
		this.itemId = itemId;
		this.removeNum=itemNum;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	@Override
	public long getRoleid() {
		return roleId;
	}

	/**
	 * @return the removeNum
	 */
	public int getRemoveNum() {
		return removeNum;
	}
}
