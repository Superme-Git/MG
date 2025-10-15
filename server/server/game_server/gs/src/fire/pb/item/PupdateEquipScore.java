
package fire.pb.item;

import mkdb.Procedure;


public class PupdateEquipScore  extends Procedure {
	private long roleId;
	
	private int equipKey;
	
	private int score;
	
	PupdateEquipScore(long roleId,int equipKey,int score){
		this.roleId=roleId;
		this.equipKey=equipKey;
		this.score=score;
	}
	
	
	@Override
	protected boolean process() throws Exception {
		Equip equip = new Equip(roleId, false);
		ItemBase equipItem = equip.getItem(equipKey);
		
		if (!(equipItem instanceof EquipItem))
			return false;
		
		xbean.Equip equipAttr = ((EquipItem) equipItem).getEquipAttr();
		
		if (score==equipAttr.getEquipscore()) {
			return false;
		}
		equipAttr.setEquipscore(score);
		if (score >= ((EquipItem) equipItem).getItemAttr().getTreasureScore()) {
			((EquipItem) equipItem).getEquipAttr().setTreasure(1);
		}
		else
			((EquipItem) equipItem).getEquipAttr().setTreasure(0);
		return true;
	}

	
}
