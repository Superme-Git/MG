package fire.pb.scene.movable;

import fire.pb.scene.Scene;
import fire.pb.scene.manager.PickUpManager;
import fire.pb.scene.sPos.Position;

public class HongBaoitem extends Pickup {

	HongBaoitem(long key, int baseid, final Scene scene, final Position pos, long money){
		super(key, baseid, null, scene, pos, PickUpManager.PICK_UP_TYPE_HONGBAO);
		moneyNum = money;
	}

	public long getMoneyNum() {
		return moneyNum;
	}
	
	private long moneyNum;
}
