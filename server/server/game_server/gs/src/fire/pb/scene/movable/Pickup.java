package fire.pb.scene.movable;

import fire.pb.move.PickUpItem;
import fire.pb.scene.Scene;
import fire.pb.scene.manager.PickUpManager;
import fire.pb.scene.sPos.Position;

public class Pickup extends SceneObject {
	private int awardId = 0;
	
	Pickup(long key, int baseid, String name, final Scene scene, final Position pos){
		super(key, name, scene, pos);
		baseID = baseid;
//		fire.pb.item.SScreenItemConfig conf = 
//			fire.pb.scene.manager.PickUpManager.getInstance().getPickUpConf( baseid );
//		if ( conf == null ) {
//			lootid = 0;
//			return;
//		}
//		java.util.ArrayList<Integer> lootids = conf.getLootids();
//		java.util.ArrayList<Integer> rates = conf.getRate();
//		int totalweight = 0;
//		for ( int rate : rates ) {
//			totalweight += rate;
//		}
//		int w = fire.pb.util.Misc.getRandomBetween( 1, totalweight );
//		int i = 0;
//		for ( int rate : rates ) {
//			if ( w <= rate )
//				break;
//			w -= rate;
//			i++;
//		}
//		lootid = lootids.get( i );
	}
	Pickup(long key, int baseid, String name, final Scene scene, final Position pos,int pickupType){
		this(key, baseid, name, scene, pos);
		this.pickupType = pickupType;
	}
	public PickUpItem getPickupBasic(){
		final PickUpItem pi = new PickUpItem();
		
		pi.baseid = baseID;
		pi.uniqueid = getUniqueID();
		pi.pos = getPos().toProtocolPos();

		return pi;
	}
	
	public int getBaseID() {
		return baseID;
	}
	
	public int getAwardId() {
		return awardId;
	}
	
	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}
//	private int lootid;
	private final int baseID;
	private  int pickupType = PickUpManager.PICK_UP_TYPE_TNSH;
	
	public int getPickupType() {
	
		return pickupType;
	}

}
