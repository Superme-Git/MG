package fire.pb.item;

import static fire.pb.item.Commontext.DROP_ITEM_FAIL;
import static fire.pb.item.Commontext.DROP_ITEM_SUCC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.pb.event.DeleteItemEvent;
import fire.pb.event.Poster;
import fire.pb.lock.PCheckLock;
import fire.pb.mission.SNpcFollowEnd;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import mkdb.Procedure;

public class PDestroyItem extends Procedure {

	public PDestroyItem() {
		roleId = 0;
		packid = 0;
		key = 0;
	}

	public PDestroyItem(long roleId, int packid, int key) {
		this.roleId = roleId;
		this.packid = packid;
		this.key = key;
	}

	@Override
	protected boolean process() {
		final ItemMaps bag = Module.getInstance().getItemMaps(roleId,
				packid, false);
		if (bag == null)
			return false;
		final ItemBase bi = bag.getItem(key);
		if (bi == null)
			return false;
		Map<Integer,Sanquan> map = fire.pb.main.ConfigManager.getInstance().getConf(Sanquan.class);
		if(map != null){
			if(map.containsKey(bi.getItemId())){
				// if(!new PCheckLock(roleId).call()){
				// return false;
				// }
				if (GoodsSafeLocksUtils.checkLockStatus(roleId,bi)) {
					return false;
				}
			}
		}
		fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleId,
				DROP_ITEM_FAIL, null);
		if (!bi.itemAttr.destroy) {
			return false;
		} else {
			final ItemBase item = bag.getItem(key);
			if (item == null)
				return false;
			final Integer itemnum = item.getNumber();
			final String units = item.getItemAttr().unit;
			final String itemname = item.getItemAttr().name;
			final int itemid = item.getItemId();
			
			xbean.Properties prop = xtable.Properties.select(roleId);
			if (prop != null)
			{
				if (itemid == prop.getRide())
				{
					MessageMgr.sendMsgNotify(roleId, 180000, null);
					return false;
				}
			}
			
			int removed = bag.removeItemWithKey(key, -1,
					fire.log.enums.YYLoggerTuJingEnum.tujing_Value_diuqi, 0,
					"丢弃物品");
			if (removed != 0) {
				List<String> parameters = new ArrayList<String>();
				parameters.add(itemnum.toString());
				parameters.add(units);
				parameters.add(itemname);
				fire.pb.talk.MessageMgr.psendMsgNotify(roleId, DROP_ITEM_SUCC,
						parameters);
	           if (itemid == 50205) {
	        	   SNpcFollowEnd sNpcFollowEnd = new SNpcFollowEnd(11398);
				   Procedure.psendWhileCommit(roleId, sNpcFollowEnd);
				}

	        	Poster.getPoster().dispatchEvent(new DeleteItemEvent(roleId,itemid,itemnum));
	        	//清除数据
	        	GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
				return true;
			}
			return false;
			
		}
	}

	private long roleId = 0;
	private int packid = 0;
	private int key = 0;
}
