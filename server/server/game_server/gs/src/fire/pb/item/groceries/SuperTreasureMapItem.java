package fire.pb.item.groceries;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.msp.task.GGenTreasuremapPos;
import fire.pb.GsClient;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.item.GroceryItem;
import fire.pb.item.ItemMgr;
import xbean.Item;

public class SuperTreasureMapItem extends GroceryItem {

	public SuperTreasureMapItem(ItemMgr im, Item item) {
		super(im, item);
	}

	public SuperTreasureMapItem(ItemMgr im, int itemid) {
		super(im, itemid);
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
	public Octets getTips() {
		if (os == null) {
			os = (OctetsStream) super.getTips();
			xbean.ArchMap tMapProp = getExtInfo();
			if (tMapProp != null) {
				os.marshal(tMapProp.getMapid());
				os.marshal(tMapProp.getPosx());
				os.marshal(tMapProp.getPosy());
			}
		}

		return os;
	}

}
