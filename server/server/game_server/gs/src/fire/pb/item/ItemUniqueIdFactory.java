package fire.pb.item;

import mkdb.Mkdb;
import mkdb.util.AutoKey;

public class ItemUniqueIdFactory implements UniqueIdFactory {
	@Override
	public long getUniqueId() {
		AutoKey<Long> autokey = Mkdb.getInstance().getTables().getTableSys().getAutoKeys().getAutoKeyLong("ITEM_UID");
		if (autokey == null)
			return 0;
		return autokey.next();
	}
}
