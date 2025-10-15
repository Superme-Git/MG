package fire.pb.item;

import java.util.ArrayList;
import java.util.List;

import fire.pb.util.RolePropConf;

public class EquipUtils {
	public static boolean isRoleCanEquip(int itemId, xbean.Properties prop) {
		if (null == prop) {
			return false;
		}

		ItemShuXing itemAttr = Module.getInstance().getItemManager().getAttr(itemId);
		if (itemAttr == null || !(itemAttr instanceof EquipItemShuXing)){
			return false;
		}
		
		EquipItemShuXing equipAttr = (EquipItemShuXing)itemAttr;
		int requirelevel = itemAttr.needlevel;

		ArrayList<Integer> shapes = equipAttr.roleNeed;
		if (shapes != null && !shapes.isEmpty() && !shapes.contains(RolePropConf.getShapeidByXshapeid(prop.getShape()))) {
			return false;
		}

		if (prop.getLevel() < requirelevel)
			return false;
		if (equipAttr.needSex != 0 && !(equipAttr.needSex == prop.getSex()))
			return false;
		
		List<Integer> schoolLst = Module.itemSchoolData.get(itemId);
		if (schoolLst != null && schoolLst.get(0) != 0 && !schoolLst.contains(prop.getSchool()))
			return false;
		
		return true;
	}
}
