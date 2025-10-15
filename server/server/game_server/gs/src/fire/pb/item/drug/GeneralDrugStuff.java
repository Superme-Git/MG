package fire.pb.item.drug;

import fire.pb.item.FoodItem;
import fire.pb.item.ItemMgr;

public class GeneralDrugStuff extends FoodItem {
	public GeneralDrugStuff( ItemMgr im, int number, mkdb.Bean extinfo) {
		super( im, number, extinfo );
	}
	
	public GeneralDrugStuff( ItemMgr im, int number) {
		super( im, number );
	}
	
	public GeneralDrugStuff( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}
}
