package fire.pb.mission.treasuremap;

import fire.pb.item.ItemBase;
import xbean.ArchMap;
import mkdb.Procedure;

public class PGenTreasuremap extends Procedure {
	
	private final long roleid;
	private final int bagid;
	private final int key;
	private final int mapid;
	private final int posx;
	private final int posy;
	public PGenTreasuremap(final long roleid, final int bagid, final int key, final int mapid, final int posx, 
			final int posy) {
		this.roleid = roleid;
		this.bagid = bagid;
		this.key = key;
		this.mapid = mapid;
		this.posx = posx;
		this.posy = posy;
	}
	@Override
	protected boolean process() throws Exception {
		
		fire.pb.item.ItemMaps bag = fire.pb.item.Module.getInstance().getItemMaps(roleid, bagid, false);
		ItemBase item = bag.getItem(key);
		if (item == null)
			return false;
		
		ArchMap tmapprop = xbean.Pod.newArchMap();
		tmapprop.setMapid(mapid);
		tmapprop.setPosx(posx);
		tmapprop.setPosy(posy);
		item.getDataItem().setExtid(xtable.Archmaps.insert(tmapprop));
		
		return true;
	}
	
	

}
