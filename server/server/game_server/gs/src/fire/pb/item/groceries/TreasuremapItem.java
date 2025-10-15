package fire.pb.item.groceries;

import fire.msp.task.GGenTreasuremapPos;
import fire.pb.GsClient;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemMgr;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

public class TreasuremapItem extends GroceryItem {
	public TreasuremapItem(ItemMgr im, int itemid) {
		super(im, itemid);
	}

	public TreasuremapItem(ItemMgr im, xbean.Item item) {
		super(im, item);
	}

	@Override
	public void onInserted() {
		int mapid = CircleTaskManager.getInstance().getItemToMap(getItemId());
		if (mapid != 0) {
			/*GridPos pos = MapUtil.genPosInMap(mapid);

			ArchMap tmapprop = xbean.Pod.newArchMap();
			tmapprop.setMapid(mapid);
			tmapprop.setPosx(pos.getX());
			tmapprop.setPosy(pos.getY());
			itemData.setExtid(xtable.Archmaps.insert(tmapprop));*/
			
			GGenTreasuremapPos ggtp = new GGenTreasuremapPos();
			ggtp.mapid = mapid;
			ggtp.roleid = getOwnerid();
			ggtp.bagid = getPackId();
			ggtp.key = getKey();
			
			GsClient.pSendWhileCommit(ggtp);
		}
	}

	@Override
	public void onDeleted() {
		xtable.Archmaps.remove(itemData.getExtid());
	}

	@Override
	public xbean.ArchMap getExtInfo() {
		return xtable.Archmaps.select(itemData.getExtid());
	}

	@Override
	public Octets getTips() {
		if (os == null) {
			os = new OctetsStream();
			xbean.ArchMap tMapProp = getExtInfo();
			if (tMapProp == null) {
				os.marshal(0);
				os.marshal(0);
				os.marshal(0);
				return os;
			}
			os.marshal(tMapProp.getMapid());
			os.marshal(tMapProp.getPosx());
			os.marshal(tMapProp.getPosy());
		}

		return os;
	}

}
