package fire.pb.item;

public class ItemUseFilter {
	
	public static boolean doFilter(long roleId, int keyinpack) {
		Pack bag = new Pack(roleId, true);
		ItemBase bi = bag.getItem(keyinpack);
		if (bi == null)
			return true;
		
		if ((bi.getFlags() & fire.pb.Item.TIMEOUT) != 0) {
			if (bi instanceof TimeOutItem) {
				fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 180030, null);
				Module.logger.error(bi.getItemId() + " timeout!");
				return true;
			}
		}
		
		return false;
	}
	
}
