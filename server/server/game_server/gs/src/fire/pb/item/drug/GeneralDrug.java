package fire.pb.item.drug;

import fire.pb.item.FoodItem;
import fire.pb.item.ItemMgr;

public class GeneralDrug extends FoodItem {
	public GeneralDrug( ItemMgr im, int number, mkdb.Bean extinfo) {
		super( im, number, extinfo );
	}
	
	public GeneralDrug( ItemMgr im, int number) {
		super( im, number );
	}
	
	public GeneralDrug( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}
}
